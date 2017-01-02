package com.pfh.promiselist.model;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 用户对象
 */

public class User extends RealmObject implements Serializable{

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private String uid;// 用户唯一标识 user_UuidUtils.getShortUuid() 如 user_pCoApzyH
    private String username;
    private String password;
    private String mobilePhoneNumber;
    private String email;
    private boolean mobilePhoneVerified;
    private boolean emailVerified;
    private int gender; // 1-male;2-female;3-other
    private long registerTime;
    private int permission;// 权限 0-普通账户 1-高级账户
    private RealmList<Project> projects;// 所有项目
    private RealmList<Task> tasks; // 所有任务
    private RealmList<Tag> tags;// 所有标签

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMobilePhoneVerified() {
        return mobilePhoneVerified;
    }

    public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
        this.mobilePhoneVerified = mobilePhoneVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public RealmList<Project> getProjects() {
        return projects;
    }

    public void setProjects(RealmList<Project> projects) {
        this.projects = projects;
    }

    public RealmList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }

    public RealmList<Tag> getTags() {
        return tags;
    }

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", mobilePhoneVerified=" + mobilePhoneVerified +
                ", emailVerified=" + emailVerified +
                ", gender=" + gender +
                ", registerTime=" + registerTime +
                ", permission=" + permission +
                ", projects=" + projects +
                ", tasks=" + tasks +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (mobilePhoneVerified != user.mobilePhoneVerified) return false;
        if (emailVerified != user.emailVerified) return false;
        if (gender != user.gender) return false;
        if (registerTime != user.registerTime) return false;
        if (permission != user.permission) return false;
        if (!uid.equals(user.uid)) return false;
        if (!username.equals(user.username)) return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (mobilePhoneNumber != null ? !mobilePhoneNumber.equals(user.mobilePhoneNumber) : user.mobilePhoneNumber != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (projects != null ? !projects.equals(user.projects) : user.projects != null)
            return false;
        if (tasks != null ? !tasks.equals(user.tasks) : user.tasks != null) return false;
        return tags != null ? tags.equals(user.tags) : user.tags == null;

    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (mobilePhoneNumber != null ? mobilePhoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (mobilePhoneVerified ? 1 : 0);
        result = 31 * result + (emailVerified ? 1 : 0);
        result = 31 * result + gender;
        result = 31 * result + (int) (registerTime ^ (registerTime >>> 32));
        result = 31 * result + permission;
        result = 31 * result + (tasks != null ? tasks.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }
}
