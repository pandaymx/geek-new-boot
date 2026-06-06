package com.ppmb.core.domain.base;

public interface Treeable<ID> {

    /**
     * Get the parent ID of this entity.
     *
     * @return the parent ID
     */
    ID getParentId();

    /**
     * Set the parent ID of this entity.
     *
     * @param parentId the new parent ID
     */
    void setParentId(ID parentId);

    /**
     * Get the path of this entity. Path is used for fast tree querying, e.g. ",1,5,".
     *
     * @return the path
     */
    String getPath();

    /**
     * Set the path of this entity.
     *
     * @param path the new path
     */
    void setPath(String path);
}
