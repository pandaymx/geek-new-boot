package com.ppmb.sys.config;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SysConfigService {

    /**
     * Get a configuration value by its key. This is the most frequently called method by other
     * modules.
     *
     * @param configKey the configuration key
     * @return the configuration value, or null if not found
     */
    String getConfigValueByKey(String configKey);

    /**
     * Save a new configuration.
     *
     * @param request the save request
     * @return the saved config DTO
     */
    SysConfigDTO save(SysConfigSaveRequest request);

    /**
     * Update an existing configuration.
     *
     * @param id the id of the config to update
     * @param request the update request
     * @return the updated config DTO
     */
    SysConfigDTO update(Long id, SysConfigUpdateRequest request);

    /**
     * Delete a configuration by its id.
     *
     * @param id the id of the config to delete
     */
    void delete(Long id);

    /**
     * Get a configuration by its id.
     *
     * @param id the config id
     * @return the config DTO
     */
    SysConfigDTO getById(Long id);

    /**
     * Find configurations with pagination and optional filtering by name/key.
     *
     * @param configName the config name (optional, partial match)
     * @param configKey the config key (optional, partial match)
     * @param pageable the pagination info
     * @return a page of config DTOs
     */
    Page<SysConfigDTO> page(String configName, String configKey, Pageable pageable);
}
