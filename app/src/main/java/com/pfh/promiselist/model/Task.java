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
    private long startTime;// 默认当前时间
    private long dueTime;
    private long createdTime; // 创建时间 默认当前时间
    private Project project; // 所属的project
//    private boolean remindMode;// 是否开启提醒 废弃有了dueTime就不增加这个设置了
//    private long remindTime;// 结束前几分钟提醒
    private String colorValue; // 背景颜色
    private String desc;
    private User owner;
    //    private int importance; // 低1、正常2、高3 默认正常2 取消 改成用颜色区分 更灵活更好看
    private RealmList<Tag> tags;
    private RealmList<User> cooperators;
    private int state;//1表示未完成uncompleted，2表示已完成completed，0表示已删除(进回收站)deleted

    private String repeatMode; // NOREPEAT、DAILY、WEEKLY、MONTHLY、YEARLY
    private long repeatTime; //一个确切的时间，在创建任务时确定，根据repeatMode和repeatTime算出某天是否符合
                            // (如WEEKLY，算出repeatTime为周几，则下面的每周几都要重复该任务；如MONTHLY，算出repeatTime为几号，则下面每个月几号都要重复)，
                            //如果符合则创建该任务到任务列表
    private boolean fixed; // 固定 如果固定无论何种排序都固定在最上面

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(String repeatMode) {
        this.repeatMode = repeatMode;
    }

    public long getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(long repeatTime) {
        this.repeatTime = repeatTime;
    }

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

//    public boolean isRemindMode() {
//        return remindMode;
//    }
//
//    public void setRemindMode(boolean remindMode) {
//        this.remindMode = remindMode;
//    }

//    public long getRemindTime() {
//        return remindTime;
//    }
//
//    public void setRemindTime(long remindTime) {
//        this.remindTime = remindTime;
//    }

//    public int getImportance() {
//        return importance;
//    }
//
//    public void setImportance(int importance) {
//        this.importance = importance;
//    }

    public RealmList<Tag> getTags() {
        return tags;
    }

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
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

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
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
                ", colorValue='" + colorValue + '\'' +
                ", desc='" + desc + '\'' +
                ", owner=" + owner +
                ", tags=" + tags +
                ", cooperators=" + cooperators +
                ", state=" + state +
                ", repeatMode='" + repeatMode + '\'' +
                ", repeatTime=" + repeatTime +
                ", fixed=" + fixed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (startTime != task.startTime) return false;
        if (dueTime != task.dueTime) return false;
        if (createdTime != task.createdTime) return false;
        if (state != task.state) return false;
        if (repeatTime != task.repeatTime) return false;
        if (fixed != task.fixed) return false;
        if (!taskId.equals(task.taskId)) return false;
        if (!name.equals(task.name)) return false;
        if (!project.equals(task.project)) return false;
        if (colorValue != null ? !colorValue.equals(task.colorValue) : task.colorValue != null)
            return false;
        if (desc != null ? !desc.equals(task.desc) : task.desc != null) return false;
        if (!owner.equals(task.owner)) return false;
        if (tags != null ? !tags.equals(task.tags) : task.tags != null) return false;
        if (cooperators != null ? !cooperators.equals(task.cooperators) : task.cooperators != null)
            return false;
        return repeatMode != null ? repeatMode.equals(task.repeatMode) : task.repeatMode == null;

    }

    @Override
    public int hashCode() {
        int result = taskId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (int) (startTime ^ (startTime >>> 32));
        result = 31 * result + (int) (dueTime ^ (dueTime >>> 32));
        result = 31 * result + (int) (createdTime ^ (createdTime >>> 32));
        result = 31 * result + project.hashCode();
        result = 31 * result + (colorValue != null ? colorValue.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + owner.hashCode();
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (cooperators != null ? cooperators.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (repeatMode != null ? repeatMode.hashCode() : 0);
        result = 31 * result + (int) (repeatTime ^ (repeatTime >>> 32));
        result = 31 * result + (fixed ? 1 : 0);
        return result;
    }
}
