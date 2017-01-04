package com.pfh.promiselist.model;

import java.io.Serializable;

/**
 * 因为Task不能序列化，用这个model来代替
 */

public class TaskInfo implements Serializable {

    private int taskId;
    private String name;
    private long dueTime;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", dueTime=" + dueTime +
                '}';
    }
}
