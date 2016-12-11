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
                ", tasks=" + tasks +
                ", owner=" + owner +
                ", cooperators=" + cooperators +
                ", state=" + state +
                ", completion=" + completion +
                ", createdTime=" + createdTime +
                '}';
    }
}
