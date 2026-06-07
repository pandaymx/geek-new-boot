package com.ppmb.dept.infrastructure.repository;

import com.ppmb.dept.domain.entity.Dept;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptRepository extends JpaRepository<Dept, Long> {

    /**
     * Find all children departments based on path. Use LIKE '%,parentId,%' to strictly match path
     * parts.
     *
     * @param path the path like string
     * @return list of departments
     */
    List<Dept> findByPathLike(String path);

    /**
     * Check if a department has children.
     *
     * @param parentId the parent ID
     * @return true if children exist, false otherwise
     */
    boolean existsByParentId(Long parentId);
}
