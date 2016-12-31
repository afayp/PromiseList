package com.pfh.promiselist.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 项目/清单bean
 */

public class Project extends RealmObject {

    @PrimaryKey
    private String projectId;// 项目唯一标识 project_UuidUtils.getShortUuid() 如 project_pCoApzyH
    private String name;
    private String category;
    private String backgroundUrl;//背景图片地址
    private String desc;
    private RealmList<Task> tasks; //该project下所有task
    private User owner;
    private RealmList<User> cooperators;
    private int state;// 1表示激活active,0表示已删除deleted(进回收站)
    private int completion;// 0表示一项都没完成，1表示全部完成，2表示部分完成
    private long createdTime;// 创建时间

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public RealmList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public RealmList<User> getCooperators() {
        return cooperators;
    }

    public void setCooperators(RealmList<User> cooperators) {
        this.cooperators = cooperators;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId='" + projectId + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", backgroundUrl='" + backgroundUrl + '\'' +
                ", desc='" + desc + '\'' +
                ", owner=" + owner +
                ", cooperators=" + cooperators +
                ", state=" + state +
                ", completion=" + completion +
                ", createdTime=" + createdTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (state != project.state) return false;
        if (completion != project.completion) return false;
        if (createdTime != project.createdTime) return false;
        if (!projectId.equals(project.projectId)) return false;
        if (!name.equals(project.name)) return false;
        if (category != null ? !category.equals(project.category) : project.category != null)
            return false;
        if (backgroundUrl != null ? !backgroundUrl.equals(project.backgroundUrl) : project.backgroundUrl != null)
            return false;
        if (desc != null ? !desc.equals(project.desc) : project.desc != null) return false;
        if (tasks != null ? !tasks.equals(project.tasks) : project.tasks != null) return false;
        if (!owner.equals(project.owner)) return false;
        return cooperators != null ? cooperators.equals(project.cooperators) : project.cooperators == null;

    }

    @Override
    public int hashCode() {
        int result = projectId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (backgroundUrl != null ? backgroundUrl.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (tasks != null ? tasks.hashCode() : 0);
        result = 31 * result + owner.hashCode();
        result = 31 * result + (cooperators != null ? cooperators.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + completion;
        result = 31 * result + (int) (createdTime ^ (createdTime >>> 32));
        return result;
    }
}
