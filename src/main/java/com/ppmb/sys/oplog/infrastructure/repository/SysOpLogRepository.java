package com.ppmb.sys.oplog.infrastructure.repository;

import com.ppmb.sys.oplog.domain.entity.SysOpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for persisting operation logs. */
@Repository
public interface SysOpLogRepository extends JpaRepository<SysOpLog, Long> {}
