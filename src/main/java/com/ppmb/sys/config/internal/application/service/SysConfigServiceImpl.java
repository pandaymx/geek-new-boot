package com.ppmb.sys.config.internal.application.service;

import com.ppmb.core.exception.BusinessException;
import com.ppmb.core.exception.SystemErrorCode;
import com.ppmb.core.infrastructure.id.SnowflakeIdGenerator;
import com.ppmb.sys.config.ConfigType;
import com.ppmb.sys.config.SysConfigDTO;
import com.ppmb.sys.config.SysConfigSaveRequest;
import com.ppmb.sys.config.SysConfigService;
import com.ppmb.sys.config.SysConfigUpdateRequest;
import com.ppmb.sys.config.internal.domain.entity.SysConfig;
import com.ppmb.sys.config.internal.infrastructure.repository.SysConfigRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigRepository sysConfigRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public SysConfigServiceImpl(
            SysConfigRepository sysConfigRepository, SnowflakeIdGenerator snowflakeIdGenerator) {
        this.sysConfigRepository = sysConfigRepository;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public String getConfigValueByKey(String configKey) {
        // TODO: Add cache (e.g., Valkey/Redis) here as this method is heavily read
        return sysConfigRepository
                .findByConfigKey(configKey)
                .map(SysConfig::getConfigValue)
                .orElse(null);
    }

    @Override
    @Transactional
    public SysConfigDTO save(SysConfigSaveRequest request) {
        if (sysConfigRepository.existsByConfigKey(request.configKey())) {
            throw new BusinessException(SystemErrorCode.BAD_REQUEST, "Config key already exists");
        }

        SysConfig sysConfig =
                new SysConfig(
                        snowflakeIdGenerator.nextId(),
                        request.configName(),
                        request.configKey(),
                        request.configValue(),
                        request.configType(),
                        request.remark());

        SysConfig saved = sysConfigRepository.save(sysConfig);
        return toDto(saved);
    }

    @Override
    @Transactional
    public SysConfigDTO update(Long id, SysConfigUpdateRequest request) {
        SysConfig sysConfig =
                sysConfigRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new BusinessException(
                                                SystemErrorCode.NOT_FOUND, "Config not found"));

        if (sysConfig.getConfigType() == ConfigType.BUILT_IN
                && request.configType() != ConfigType.BUILT_IN) {
            throw new BusinessException(
                    SystemErrorCode.BAD_REQUEST, "Cannot change built-in config type");
        }

        sysConfig.setConfigName(request.configName());
        sysConfig.setConfigValue(request.configValue());
        sysConfig.setConfigType(request.configType());
        sysConfig.setRemark(request.remark());

        SysConfig updated = sysConfigRepository.save(sysConfig);
        // TODO: Evict cache for this configKey here when Redis/Valkey is introduced
        return toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SysConfig sysConfig =
                sysConfigRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new BusinessException(
                                                SystemErrorCode.NOT_FOUND, "Config not found"));

        if (sysConfig.getConfigType() == ConfigType.BUILT_IN) {
            throw new BusinessException(
                    SystemErrorCode.BAD_REQUEST, "Cannot delete built-in configuration");
        }

        sysConfigRepository.delete(sysConfig);
        // TODO: Evict cache for this configKey here when Redis/Valkey is introduced
    }

    @Override
    @Transactional(readOnly = true)
    public SysConfigDTO getById(Long id) {
        SysConfig sysConfig =
                sysConfigRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new BusinessException(
                                                SystemErrorCode.NOT_FOUND, "Config not found"));
        return toDto(sysConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SysConfigDTO> page(String configName, String configKey, Pageable pageable) {
        Specification<SysConfig> spec =
                (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (configName != null && !configName.trim().isEmpty()) {
                        predicates.add(
                                cb.like(root.get("configName"), "%" + configName.trim() + "%"));
                    }
                    if (configKey != null && !configKey.trim().isEmpty()) {
                        predicates.add(
                                cb.like(root.get("configKey"), "%" + configKey.trim() + "%"));
                    }
                    return cb.and(predicates.toArray(new Predicate[0]));
                };

        return sysConfigRepository.findAll(spec, pageable).map(this::toDto);
    }

    private SysConfigDTO toDto(SysConfig entity) {
        return new SysConfigDTO(
                entity.getId(),
                entity.getConfigName(),
                entity.getConfigKey(),
                entity.getConfigValue(),
                entity.getConfigType(),
                entity.getRemark(),
                entity.getAuditInfo() != null ? entity.getAuditInfo().getCreatedBy() : null,
                entity.getAuditInfo() != null ? entity.getAuditInfo().getCreatedTime() : null,
                entity.getAuditInfo() != null ? entity.getAuditInfo().getUpdatedBy() : null,
                entity.getAuditInfo() != null ? entity.getAuditInfo().getUpdatedTime() : null);
    }
}
