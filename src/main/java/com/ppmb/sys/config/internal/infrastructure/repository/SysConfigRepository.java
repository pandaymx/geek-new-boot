package com.ppmb.sys.config.internal.infrastructure.repository;

import com.ppmb.sys.config.internal.domain.entity.SysConfig;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysConfigRepository
        extends JpaRepository<SysConfig, Long>, JpaSpecificationExecutor<SysConfig> {

    Optional<SysConfig> findByConfigKey(String configKey);

    boolean existsByConfigKey(String configKey);
}
