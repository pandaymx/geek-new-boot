package com.ppmb.core.domain.base;

public interface Identifiable<ID> {

    /**
     * Get the identifier of this entity.
     *
     * @return the identifier
     */
    ID getId();
}
