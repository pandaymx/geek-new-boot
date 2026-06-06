package com.ppmb.sys.file.domain.repository;

import com.ppmb.sys.file.domain.entity.SysFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysFileRepository extends JpaRepository<SysFile, Long> {}
