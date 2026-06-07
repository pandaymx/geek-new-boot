package com.ppmb.sys.file.domain.model;

/** Represents the status of a file in the system. */
public enum FileStatus {
    NORMAL(0),
    DELETED(1);

    private final int value;

    FileStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static FileStatus fromValue(int value) {
        for (FileStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown FileStatus value: " + value);
    }
}
