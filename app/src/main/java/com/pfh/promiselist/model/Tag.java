package com.pfh.promiselist.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 标签
 */

public class Tag extends RealmObject{
    @PrimaryKey
    private String tagId;
    private String name;
    private User owner;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId='" + tagId + '\'' +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }
}
