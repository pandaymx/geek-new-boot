package com.ppmb.sys.config.internal.presentation.controller;

import com.ppmb.core.presentation.response.Result;
import com.ppmb.sys.config.SysConfigDTO;
import com.ppmb.sys.config.SysConfigSaveRequest;
import com.ppmb.sys.config.SysConfigService;
import com.ppmb.sys.config.SysConfigUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/sys/configs")
public class SysConfigController {

    private final SysConfigService sysConfigService;

    public SysConfigController(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:config:admin')")
    public Result<SysConfigDTO> save(@RequestBody @Validated SysConfigSaveRequest request) {
        return Result.success(sysConfigService.save(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:config:admin')")
    public Result<SysConfigDTO> update(
            @PathVariable Long id, @RequestBody @Validated SysConfigUpdateRequest request) {
        return Result.success(sysConfigService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:config:admin')")
    public Result<Void> delete(@PathVariable Long id) {
        sysConfigService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:config:admin')")
    public Result<SysConfigDTO> getById(@PathVariable Long id) {
        return Result.success(sysConfigService.getById(id));
    }

    @GetMapping("/key/{configKey}")
    @PreAuthorize("hasAuthority('sys:config:admin')")
    public Result<String> getConfigValueByKey(@PathVariable String configKey) {
        return Result.success(sysConfigService.getConfigValueByKey(configKey));
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('sys:config:admin')")
    public Result<Page<SysConfigDTO>> page(
            @RequestParam(required = false) String configName,
            @RequestParam(required = false) String configKey,
            Pageable pageable) {
        return Result.success(sysConfigService.page(configName, configKey, pageable));
    }
}
