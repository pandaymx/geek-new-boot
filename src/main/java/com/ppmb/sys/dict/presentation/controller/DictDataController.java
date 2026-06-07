package com.ppmb.sys.dict.presentation.controller;

import com.ppmb.core.presentation.response.Result;
import com.ppmb.sys.dict.application.service.DictApplicationService;
import com.ppmb.sys.dict.domain.entity.DictData;
import com.ppmb.sys.dict.presentation.dto.DictDataRequest;
import com.ppmb.sys.dict.presentation.dto.DictDataResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/dicts")
public class DictDataController {

    private final DictApplicationService dictApplicationService;

    public DictDataController(DictApplicationService dictApplicationService) {
        this.dictApplicationService = dictApplicationService;
    }

    @PostMapping("/data")
    public Result<DictDataResponse> createDictData(
            @Validated @RequestBody DictDataRequest request) {
        DictData dictData =
                dictApplicationService.createDictData(
                        request.dictType(),
                        request.dictLabel(),
                        request.dictValue(),
                        request.sortOrder(),
                        request.listClass(),
                        request.status(),
                        request.remark());
        return Result.success(DictDataResponse.from(dictData));
    }

    @PutMapping("/data/{id}")
    public Result<DictDataResponse> updateDictData(
            @PathVariable Long id, @Validated @RequestBody DictDataRequest request) {
        DictData dictData =
                dictApplicationService.updateDictData(
                        id,
                        request.dictLabel(),
                        request.sortOrder(),
                        request.listClass(),
                        request.status(),
                        request.remark());
        return Result.success(DictDataResponse.from(dictData));
    }

    @DeleteMapping("/data/{id}")
    public Result<Void> deleteDictData(@PathVariable Long id) {
        dictApplicationService.deleteDictData(id);
        return Result.success();
    }

    /** Query dictionary data list by type. */
    @GetMapping("/type/{dictType}")
    public Result<List<DictDataResponse>> getDictDataByType(@PathVariable String dictType) {
        List<DictDataResponse> list =
                dictApplicationService.getDictDataByType(dictType).stream()
                        .filter(
                                data ->
                                        data.getStatus()
                                                == 0) // Only return normal status for frontend
                        // usage
                        .map(DictDataResponse::from)
                        .collect(Collectors.toList());
        return Result.success(list);
    }

    /** Batch query dictionary data by multiple types. */
    @GetMapping("/batch")
    public Result<Map<String, List<DictDataResponse>>> getDictDataByBatch(
            @RequestParam List<String> types) {
        Map<String, List<DictData>> groupedData = dictApplicationService.getDictDataByTypes(types);

        Map<String, List<DictDataResponse>> responseMap =
                groupedData.entrySet().stream()
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry ->
                                                entry.getValue().stream()
                                                        .filter(
                                                                data ->
                                                                        data.getStatus()
                                                                                == 0) // Only return
                                                        // normal
                                                        // status
                                                        .map(DictDataResponse::from)
                                                        .collect(Collectors.toList())));

        return Result.success(responseMap);
    }
}
