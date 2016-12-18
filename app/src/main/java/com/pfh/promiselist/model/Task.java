package com.pfh.promiselist.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2016/12/10.
 */

public class Task extends RealmObject {

    @PrimaryKey
    private String taskId; // 任务唯一标识 task_UuidUtils.getShortUuid() 如 task_pCoApzyH
    private String name;
    private long startTime;
    private long dueTime;
    private long createdTime; // 创建时间
    private Project project; // 所属的project
    private boolean remindMode;// 是否开启提醒
    private long remindTime;// 结束前几分钟提醒
    private int importance; // 低1、正常2、高3
    private RealmList<Tag> tags;
    private String iconUrl;
    private String desc;
    private User owner;
    private RealmList<User> cooperators;
    private int state;//1表示未完成uncompleted，2表示已完成completed，0表示已删除(进回收站)deleted

//    private int repeatMode;
//    private long repeatTime;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isRemindMode() {
        return remindMode;
    }

    public void setRemindMode(boolean remindMode) {
        this.remindMode = remindMode;
    }

    public long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(long remindTime) {
        this.remindTime = remindTime;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public RealmList<Tag> getTags() {
        return tags;
    }

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", dueTime=" + dueTime +
                ", createdTime=" + createdTime +
                ", project=" + project +
                ", remindMode=" + remindMode +
                ", remindTime=" + remindTime +
                ", importance=" + importance +
                ", tags=" + tags +
                ", iconUrl='" + iconUrl + '\'' +
                ", desc='" + desc + '\'' +
                ", owner=" + owner +
                ", cooperators=" + cooperators +
                ", state=" + state +
                '}';
    }
}
