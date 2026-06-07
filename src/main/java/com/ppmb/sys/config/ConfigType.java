package com.ppmb.sys.config;

public enum ConfigType {
    BUILT_IN("Built-in System Configuration"),
    CUSTOM("User Custom Configuration");

    private final String description;

    ConfigType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
