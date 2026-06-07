package com.ppmb.sys.oplog.domain.model;

import com.ppmb.core.domain.base.model.DomainId;

public record SysOpLogId(Long value) implements DomainId {
    public SysOpLogId {
        if (value == null) {
            throw new IllegalArgumentException("SysOpLogId value cannot be null");
        }
    }
}
