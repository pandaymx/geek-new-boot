package com.ppmb.sys.dict.domain.repository;

import com.ppmb.sys.dict.domain.entity.DictType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictTypeRepository extends JpaRepository<DictType, Long> {

    Optional<DictType> findByDictType(String dictType);

    boolean existsByDictType(String dictType);
}
