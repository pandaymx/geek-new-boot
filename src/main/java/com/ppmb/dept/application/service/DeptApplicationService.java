package com.ppmb.dept.application.service;

import com.ppmb.core.infrastructure.id.SnowflakeIdGenerator;
import com.ppmb.dept.domain.entity.Dept;
import com.ppmb.dept.domain.model.DeptId;
import com.ppmb.dept.domain.model.DeptStatus;
import com.ppmb.dept.exception.DeptCircularReferenceException;
import com.ppmb.dept.exception.DeptHasChildrenException;
import com.ppmb.dept.exception.DeptNotFoundException;
import com.ppmb.dept.infrastructure.repository.DeptRepository;
import com.ppmb.user.domain.model.UserId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeptApplicationService {

    private final DeptRepository deptRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public DeptApplicationService(
            DeptRepository deptRepository, SnowflakeIdGenerator snowflakeIdGenerator) {
        this.deptRepository = deptRepository;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
    }

    /**
     * Create a new department.
     *
     * @param parentId the parent ID, null if it is a root department
     * @param deptName the department name
     * @param deptCode the department code
     * @param sortOrder the sort order
     * @param leaderUserId the leader user ID
     * @param status the department status
     * @return the created department
     */
    @Transactional
    public Dept createDept(
            DeptId parentId,
            String deptName,
            String deptCode,
            Integer sortOrder,
            UserId leaderUserId,
            DeptStatus status) {

        long newId = snowflakeIdGenerator.nextId();
        DeptId deptId = new DeptId(newId);

        Dept dept = new Dept();
        dept.setId(deptId);
        dept.setDeptName(deptName);
        dept.setDeptCode(deptCode);
        dept.setSortOrder(sortOrder != null ? sortOrder : 0);
        dept.setLeaderUserId(leaderUserId);
        dept.setStatus(status != null ? status : DeptStatus.NORMAL);

        String parentPath = ",";
        if (parentId != null) {
            Dept parentDept =
                    deptRepository
                            .findById(parentId.value())
                            .orElseThrow(DeptNotFoundException::new);
            parentPath = parentDept.getPath();
            dept.setParentId(parentId);
        }

        dept.setPath(parentPath + newId + ",");

        return deptRepository.save(dept);
    }

    /**
     * Delete a department. Enforces rule: Cannot delete if children exist.
     *
     * @param deptId the department id
     */
    @Transactional
    public void deleteDept(DeptId deptId) {
        // Find department
        Dept dept = deptRepository.findById(deptId.value()).orElseThrow(DeptNotFoundException::new);

        // Rule 3: Prevent deletion if children exist
        if (deptRepository.existsByParentId(deptId.value())) {
            throw new DeptHasChildrenException();
        }

        deptRepository.delete(dept);
    }

    /**
     * Update a department, enforcing circular reference prevention and cascading path updates.
     *
     * @param deptId the ID of the department to update
     * @param newParentId the new parent ID
     * @param deptName new name
     * @param deptCode new code
     * @param sortOrder new sort order
     * @param leaderUserId new leader user ID
     * @param status new status
     * @return the updated department
     */
    @Transactional
    public Dept updateDept(
            DeptId deptId,
            DeptId newParentId,
            String deptName,
            String deptCode,
            Integer sortOrder,
            UserId leaderUserId,
            DeptStatus status) {
        Dept dept = deptRepository.findById(deptId.value()).orElseThrow(DeptNotFoundException::new);

        DeptId oldParentId = dept.getParentId();
        String oldPath = dept.getPath();

        // Check if parent ID has changed
        boolean parentChanged =
                (oldParentId == null && newParentId != null)
                        || (oldParentId != null && !oldParentId.equals(newParentId));

        if (parentChanged) {
            // Rule 1: Prevent circular reference
            if (newParentId != null && newParentId.equals(deptId)) {
                throw new DeptCircularReferenceException();
            }

            String newParentPath = ",";
            if (newParentId != null) {
                Dept parentDept =
                        deptRepository
                                .findById(newParentId.value())
                                .orElseThrow(DeptNotFoundException::new);
                newParentPath = parentDept.getPath();

                // If the new parent's path contains the current department's path, it's a circular
                // reference.
                // e.g. Current dept path is ",1,2,", new parent path is ",1,2,5," -> invalid.
                if (newParentPath.startsWith(oldPath)) {
                    throw new DeptCircularReferenceException();
                }
            }

            // Calculate new path for current department
            String newPath = newParentPath + deptId.value() + ",";
            dept.setParentId(newParentId);
            dept.setPath(newPath);

            // Rule 2: Cascade update paths for all children
            // Find all children using LIKE '%,deptId,%'
            List<Dept> children = deptRepository.findByPathLike("%" + oldPath + "%");
            for (Dept child : children) {
                // Ignore the department itself, although findByPathLike might return it depending
                // on the exact oldPath.
                // oldPath already includes commas, e.g. ",1,2,".
                if (!child.getId().equals(deptId)) {
                    String childPath = child.getPath();
                    child.setPath(childPath.replaceFirst(oldPath, newPath));
                    deptRepository.save(child);
                }
            }
        }

        // Update other properties
        if (deptName != null) {
            dept.setDeptName(deptName);
        }
        if (deptCode != null) {
            dept.setDeptCode(deptCode);
        }
        if (sortOrder != null) {
            dept.setSortOrder(sortOrder);
        }
        if (leaderUserId != null) {
            dept.setLeaderUserId(leaderUserId);
        }
        if (status != null) {
            dept.setStatus(status);
        }

        return deptRepository.save(dept);
    }
}
