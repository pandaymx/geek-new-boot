package com.ppmb.sys.dict.application.service;

import com.ppmb.core.exception.BusinessException;
import com.ppmb.core.infrastructure.id.SnowflakeIdGenerator;
import com.ppmb.sys.dict.domain.entity.DictData;
import com.ppmb.sys.dict.domain.entity.DictType;
import com.ppmb.sys.dict.domain.repository.DictDataRepository;
import com.ppmb.sys.dict.domain.repository.DictTypeRepository;
import com.ppmb.sys.dict.exception.DictErrorCode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DictApplicationService {

    private final DictTypeRepository dictTypeRepository;
    private final DictDataRepository dictDataRepository;
    private final SnowflakeIdGenerator idGenerator;

    public DictApplicationService(
            DictTypeRepository dictTypeRepository,
            DictDataRepository dictDataRepository,
            SnowflakeIdGenerator idGenerator) {
        this.dictTypeRepository = dictTypeRepository;
        this.dictDataRepository = dictDataRepository;
        this.idGenerator = idGenerator;
    }

    // --- DictType Methods ---

    @Transactional
    public DictType createDictType(
            String dictName, String dictType, Integer status, String remark) {
        if (dictTypeRepository.existsByDictType(dictType)) {
            throw new BusinessException(DictErrorCode.DICT_TYPE_ALREADY_EXISTS);
        }

        DictType type = new DictType(idGenerator.nextId(), dictName, dictType, status, remark);
        return dictTypeRepository.save(type);
    }

    @Transactional
    public DictType updateDictType(Long id, String dictName, Integer status, String remark) {
        DictType type =
                dictTypeRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new BusinessException(DictErrorCode.DICT_TYPE_NOT_FOUND));

        type.setDictName(dictName);
        type.setStatus(status);
        type.setRemark(remark);

        // TODO: Evict cache when dictType is updated or its status changes.

        return dictTypeRepository.save(type);
    }

    @Transactional
    public void deleteDictType(Long id) {
        DictType type =
                dictTypeRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new BusinessException(DictErrorCode.DICT_TYPE_NOT_FOUND));

        dictDataRepository.deleteByDictType(type.getDictType());
        dictTypeRepository.delete(type);

        // TODO: Evict cache when dictType and its data are deleted.
    }

    public List<DictType> getAllDictTypes() {
        return dictTypeRepository.findAll();
    }

    public DictType getDictTypeById(Long id) {
        return dictTypeRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(DictErrorCode.DICT_TYPE_NOT_FOUND));
    }

    // --- DictData Methods ---

    @Transactional
    public DictData createDictData(
            String dictType,
            String dictLabel,
            String dictValue,
            Integer sortOrder,
            String listClass,
            Integer status,
            String remark) {
        if (!dictTypeRepository.existsByDictType(dictType)) {
            throw new BusinessException(DictErrorCode.DICT_TYPE_NOT_FOUND);
        }

        if (dictDataRepository.existsByDictTypeAndDictValue(dictType, dictValue)) {
            throw new BusinessException(DictErrorCode.DICT_DATA_ALREADY_EXISTS);
        }

        DictData data =
                new DictData(
                        idGenerator.nextId(),
                        dictType,
                        dictLabel,
                        dictValue,
                        sortOrder,
                        listClass,
                        status,
                        remark);
        return dictDataRepository.save(data);
    }

    @Transactional
    public DictData updateDictData(
            Long id,
            String dictLabel,
            Integer sortOrder,
            String listClass,
            Integer status,
            String remark) {
        DictData data =
                dictDataRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new BusinessException(DictErrorCode.DICT_DATA_NOT_FOUND));

        data.setDictLabel(dictLabel);
        data.setSortOrder(sortOrder);
        data.setListClass(listClass);
        data.setStatus(status);
        data.setRemark(remark);

        // TODO: Evict cache for the dictType.

        return dictDataRepository.save(data);
    }

    @Transactional
    public void deleteDictData(Long id) {
        DictData data =
                dictDataRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new BusinessException(DictErrorCode.DICT_DATA_NOT_FOUND));

        dictDataRepository.delete(data);

        // TODO: Evict cache for the dictType.
    }

    // TODO: Add @Cacheable for this method
    public List<DictData> getDictDataByType(String dictType) {
        return dictDataRepository.findByDictTypeOrderBySortOrderAsc(dictType);
    }

    public Map<String, List<DictData>> getDictDataByTypes(List<String> dictTypes) {
        // Iterate through cache to fetch, but for now we fetch from DB directly
        // TODO: integrate with cache for each type in the list to avoid full DB scan if cached
        List<DictData> allData = dictDataRepository.findByDictTypeInOrderBySortOrderAsc(dictTypes);
        return allData.stream().collect(Collectors.groupingBy(DictData::getDictType));
    }
}
