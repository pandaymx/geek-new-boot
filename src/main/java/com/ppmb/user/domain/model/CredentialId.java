package com.ppmb.user.domain.model;

import com.ppmb.core.domain.base.model.DomainId;
import java.io.Serializable;

public record CredentialId(Long value) implements DomainId, Serializable {
    public CredentialId {
        if (value == null) {
            throw new IllegalArgumentException("CredentialId value cannot be null");
        }
    }
}
