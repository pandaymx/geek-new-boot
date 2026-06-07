package com.ppmb.sys.dict.presentation.controller;

import com.ppmb.core.presentation.response.Result;
import com.ppmb.sys.dict.application.service.DictApplicationService;
import com.ppmb.sys.dict.domain.entity.DictType;
import com.ppmb.sys.dict.presentation.dto.DictTypeRequest;
import com.ppmb.sys.dict.presentation.dto.DictTypeResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/dict/type")
public class DictTypeController {

    private final DictApplicationService dictApplicationService;

    public DictTypeController(DictApplicationService dictApplicationService) {
        this.dictApplicationService = dictApplicationService;
    }

    @PostMapping
    public Result<DictTypeResponse> createDictType(
            @Validated @RequestBody DictTypeRequest request) {
        DictType dictType =
                dictApplicationService.createDictType(
                        request.dictName(), request.dictType(), request.status(), request.remark());
        return Result.success(DictTypeResponse.from(dictType));
    }

    @PutMapping("/{id}")
    public Result<DictTypeResponse> updateDictType(
            @PathVariable Long id, @Validated @RequestBody DictTypeRequest request) {
        DictType dictType =
                dictApplicationService.updateDictType(
                        id, request.dictName(), request.status(), request.remark());
        return Result.success(DictTypeResponse.from(dictType));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteDictType(@PathVariable Long id) {
        dictApplicationService.deleteDictType(id);
        return Result.success();
    }

    @GetMapping
    public Result<List<DictTypeResponse>> getAllDictTypes() {
        List<DictTypeResponse> list =
                dictApplicationService.getAllDictTypes().stream()
                        .map(DictTypeResponse::from)
                        .collect(Collectors.toList());
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<DictTypeResponse> getDictTypeById(@PathVariable Long id) {
        DictType dictType = dictApplicationService.getDictTypeById(id);
        return Result.success(DictTypeResponse.from(dictType));
    }
}
