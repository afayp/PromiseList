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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (!tagId.equals(tag.tagId)) return false;
        if (!name.equals(tag.name)) return false;
        return owner.equals(tag.owner);

    }

    @Override
    public int hashCode() {
        int result = tagId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + owner.hashCode();
        return result;
    }
}
