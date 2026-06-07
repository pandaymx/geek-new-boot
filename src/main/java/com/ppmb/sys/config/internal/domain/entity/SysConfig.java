package com.ppmb.sys.config.internal.domain.entity;

import com.ppmb.core.domain.base.AbstractEntity;
import com.ppmb.core.domain.base.Auditable;
import com.ppmb.core.domain.base.model.AuditInfo;
import com.ppmb.sys.config.ConfigType;
import com.ppmb.sys.config.internal.domain.converter.ConfigTypeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_config")
public class SysConfig extends AbstractEntity<Long> implements Auditable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "config_name", length = 100, nullable = false)
    private String configName;

    @Column(name = "config_key", length = 100, nullable = false, unique = true)
    private String configKey;

    @Column(name = "config_value", length = 500, nullable = false)
    private String configValue;

    @Convert(converter = ConfigTypeConverter.class)
    @Column(name = "config_type", length = 30, nullable = false)
    private ConfigType configType;

    @Column(name = "remark", length = 500)
    private String remark;

    @Embedded private AuditInfo auditInfo = AuditInfo.empty();

    protected SysConfig() {
        // Required by JPA
    }

    public SysConfig(
            Long id,
            String configName,
            String configKey,
            String configValue,
            ConfigType configType,
            String remark) {
        this.id = id;
        this.configName = configName;
        this.configKey = configKey;
        this.configValue = configValue;
        this.configType = configType;
        this.remark = remark;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    @Override
    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
