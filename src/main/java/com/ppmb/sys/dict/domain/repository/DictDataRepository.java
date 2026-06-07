package com.ppmb.sys.dict.domain.repository;

import com.ppmb.sys.dict.domain.entity.DictData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictDataRepository extends JpaRepository<DictData, Long> {

    List<DictData> findByDictTypeOrderBySortOrderAsc(String dictType);

    List<DictData> findByDictTypeInOrderBySortOrderAsc(List<String> dictTypes);

    boolean existsByDictTypeAndDictValue(String dictType, String dictValue);

    void deleteByDictType(String dictType);
}
