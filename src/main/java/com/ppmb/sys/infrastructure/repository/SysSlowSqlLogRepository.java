package com.ppmb.sys.infrastructure.repository;

import com.ppmb.sys.domain.model.SysSlowSqlLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for persisting slow SQL logs. */
@Repository
public interface SysSlowSqlLogRepository extends JpaRepository<SysSlowSqlLog, Long> {}
