package com.pfh.promiselist.dao;

import com.pfh.promiselist.MyApplication;
import com.pfh.promiselist.model.BgColor;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.model.User;
import com.pfh.promiselist.others.Constant;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 封装realm数据库的常用操作
 * 以下操作都比较简单，Realm的增删改查操作足够快，就放在UI线程了
 * 尽量将realm相关的东西封装起来，比如不要返回RealmResults,方便以后对realm的替换,
 * 但是这样realm的不会自动保存，所以修改了对象后务必手动保存一遍。
 * 注意这里都是针对本地数据库的操作，网络部分要另外写
 * 对于project，task，tag的操作默认都是针对当前登录用户
 */

public class RealmDB {


    //********************User相关********************//

    /**
     * sp中保存当前登录用户uid
     * @param uid
     */
    public static void setCurrentUserId(String uid){

        SPUtil.put(MyApplication.getContext(), Constant.CURRENT_USERId,uid);

    }

    /**
     * 获得当前登录用户userid
     * @return
     */
    public static String getCurrentUserId(){
        return (String) SPUtil.get(MyApplication.getContext(),Constant.CURRENT_USERId,"");
    }

    /**
     * 保存User对象
     * @param realm
     * @param user
     */
    public static void saveUser(Realm realm, final User user){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        });
    }

    /**
     * 根据uid查询本地User
     * @param realm
     * @param uid
     * @return
     */
    public static User getUserByUserId(Realm realm,String uid){
        return realm.copyFromRealm(realm.where(User.class).equalTo("uid", uid).findFirst());
    }

    /**
     * 根据uid删除本地User
     * @param realm
     * @param uid
     */
    public static void deleteUserByUserId(Realm realm,String uid){
        final User user = realm.where(User.class).equalTo("uid", uid).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.deleteFromRealm();
            }
        });
    }

    /**
     * 查询本地保存的所有User
     * @param realm
     * @return
     */
    public static List<User> getAllUsers(Realm realm){
        return realm.copyFromRealm(realm.where(User.class).findAll());
    }


    //********************Project相关********************//


    /**
     * 保存Project到当前登录user
     * @param realm
     * @param project
     */
    public static void saveProject(Realm realm, final Project project){
        final User user = realm.where(User.class).equalTo("uid", getCurrentUserId()).findFirst();
        project.setOwner(user);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.getProjects().add(project);
            }
        });
    }

    /**
     * 根据project id查询本地Project
     * @param realm
     * @param projectId
     * @return 可能是active 或者inactive
     */
    public static Project getProjectByProjectId(Realm realm,String projectId){
        return realm.copyFromRealm(realm.where(Project.class).equalTo("projectId", projectId).findFirst());
    }

    /**
     * 找出某个user的所有project，包括active 和 deleted
     * @param realm
     * @param uid
     * @return
     */
    public static List<Project> getAllProjectsByUserId(Realm realm,String uid){
        return realm.copyFromRealm(realm.where(Project.class).equalTo("owner.uid", uid).findAll().sort("createdTime"));
    }

    /**
     * 找出某个user的所有active的project(state=1)
     * @param realm
     * @param uid
     * @return
     */
    public static List<Project> getAllActiveProjectsByUserId(Realm realm,String uid){
        return realm.copyFromRealm(realm.where(Project.class)
                .equalTo("owner.uid", uid)
                .equalTo("state",1)
                .findAll()
                .sort("createdTime"));
    }

    /**
     * 找出某个user的所有deleted的project(state=0)
     * @param realm
     * @param uid
     * @return
     */
    public static List<Project> getAllDeletedProjectsByUserId(Realm realm,String uid){
        return realm.copyFromRealm(realm.where(Project.class)
                .equalTo("owner.uid", uid)
                .equalTo("state",0)
                .findAll()
                .sort("createdTime"));
    }

    /**
     * 回收某个project，以及里面的所有task，注意与删除的区别
     * @param realm
     * @param projectId
     */
    public static void disableProjectByProjectId(Realm realm,String projectId){
        final Project project = realm.where(Project.class).equalTo("projectId", projectId).findFirst();
        project.setState(0);
        final RealmResults<Task> allTasks = realm.where(Task.class).equalTo("project.projectId", projectId).findAll();
        for (Task task : allTasks) {
            task.setState(3);
        }
    }

    /**
     * 根据projectId删除本地Project,并且删除该project里面的所有task
     * @param realm
     * @param projectId
     */
    public static void deleteProjectByProjectId(Realm realm,String projectId){
        final Project project = realm.where(Project.class).equalTo("projectId", projectId).findFirst();
        final RealmResults<Task> allTasks = realm.where(Task.class).equalTo("project.projectId", projectId).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                project.deleteFromRealm();
                allTasks.deleteAllFromRealm();
            }
        });
    }

    //********************Task相关********************//

    /**
     * 保存单条task到当前用户指定project
     * @param realm
     * @param task
     */
    public static void saveTaskToProject(Realm realm, String projectId, final Task task){
        //找到当前project
        final Project project = realm.where(Project.class)
                .equalTo("owner.uid", getCurrentUserId())
                .equalTo("projectId", projectId)
                .findFirst();

        //设置task的project
        task.setProject(project);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                project.getTasks().add(task);
            }
        });
    }

    /**
     * 根据taskId查询本地task
     * @param realm
     * @param taskId
     * @return
     */
    public static Task getTaskByTaskId(Realm realm,String taskId){
        return realm.copyFromRealm(realm.where(Task.class).equalTo("taskId", taskId).findFirst());
    }


    /**
     * 找出某projectId下的所有task(包括完成，未完成，已回收)，按dueTime排序
     * @param realm
     * @param projectId
     * @return
     */
    public static List<Task> getAllTasksByProjectId(Realm realm,String projectId){
        RealmResults<Task> all = realm.where(Task.class).equalTo("project.projectId", projectId).findAll();
        return realm.copyFromRealm(all);
    }

    /**
     * 找出某projectId下的所有已完成的task，按dueTime排序
     * @param realm
     * @param projectId
     * @return
     */
    public static List<Task> getAllCompletedTasksByProjectId(Realm realm,String projectId){
        RealmResults<Task> all = realm.where(Task.class).equalTo("project.projectId", projectId).equalTo("state",2).findAll();
        return realm.copyFromRealm(all);
    }

    /**
     * 找出某projectId下的所有未完成的task，按dueTime排序
     * @param realm
     * @param projectId
     * @return
     */
    public static List<Task> getAllUncompletedTasksByProjectId(Realm realm,String projectId){
        RealmResults<Task> all = realm.where(Task.class).equalTo("project.projectId", projectId).equalTo("state",1).findAll();
        return realm.copyFromRealm(all);
    }


    /**
     * 找出某projectId下的所有未完成的task，并且fixed为true或false，按dueTime排序
     * @param realm
     * @param projectId
     * @param fixed
     * @return
     */
    public static List<Task> getFixedUncompletedTasksByProjectId(Realm realm,String projectId,boolean fixed){
        RealmResults<Task> all = realm.where(Task.class).equalTo("project.projectId", projectId).equalTo("state",1).equalTo("fixed",fixed).findAll();
        return realm.copyFromRealm(all);
    }


    /**
     * 找出某projectId下的所有已回收的task，按dueTime排序
     * @param realm
     * @param projectId
     * @return
     */
    public static List<Task> getAllDeletedTasksByProjectId(Realm realm,String projectId){
        RealmResults<Task> all = realm.where(Task.class).equalTo("project.projectId", projectId).equalTo("state",0).findAll();
        return realm.copyFromRealm(all);
    }

    /**
     * 找出某uid下的所有task(包括完成，未完成，已回收)
     * @param realm
     * @param uid
     * @return
     */
    public static List<Task> getAllTasksByUserId(Realm realm,String uid){
        return realm.copyFromRealm(realm.where(Task.class).equalTo("owner.uid",uid).findAll());
    }


    /**
     * 找出某uid下的所有已完成的task
     * @param realm
     * @param uid
     * @return
     */
    public static List<Task> getAllCompletedTasksByUserId(Realm realm,String uid){
        return realm.copyFromRealm(realm.where(Task.class).equalTo("owner.uid",uid).equalTo("state",2).findAll());
    }

    /**
     * 找出某uid下的所有未完成的task
     * @param realm
     * @param uid
     * @return
     */
    public static List<Task> getAllUncompletedTasksByUserId(Realm realm,String uid){
        return realm.copyFromRealm(realm.where(Task.class).equalTo("owner.uid",uid).equalTo("state",1).findAll());
    }

    /**
     * 找出某uid下的所有已回收的task
     * @param realm
     * @param uid
     * @return
     */
    public static List<Task> getAllDeletedTasksByUserId(Realm realm,String uid){
        return realm.copyFromRealm(realm.where(Task.class).equalTo("owner.uid",uid).equalTo("state",0).findAll());
    }

    /**
     * 根据taskId删除task
     * @param realm
     * @param taskId
     */
    public static void deleteTaskByTaskId(Realm realm,String taskId){
        final Task task = realm.where(Task.class).equalTo("taskId", taskId).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                task.deleteFromRealm();
            }
        });
    }

    /**
     * 根据taskId 回收disable某task，注意和删除的区别
     * @param realm
     * @param taskId
     */
    public static void disableTaskByTaskId(Realm realm,String taskId){
        Task task = realm.where(Task.class).equalTo("taskId", taskId).findFirst();
        task.setState(0);
    }

    /**
     * 删除某个projectId下面的所有tasks
     * @param realm
     * @param projectId
     */
    public static void deleteTasksByProjectId(Realm realm,String projectId){
        final RealmResults<Task> allTasks = realm.where(Task.class).equalTo("project.projectId", projectId).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allTasks.deleteAllFromRealm();
            }
        });
    }

    /**
     * 回收某个projectId下面的所有tasks，state置为0
     * @param realm
     * @param projectId
     */
    public static void disableTasksByProjectId(Realm realm,String projectId){
        final RealmResults<Task> allTasks = realm.where(Task.class).equalTo("project.projectId", projectId).findAll();
        for (Task task : allTasks) {
            task.setState(0);
        }
    }

    /**
     * 刷新某条task 注意这里是刷新全部字段 比较耗资源滴
     * @param realm
     * @param newTask
     */
    public static void refreshTask(Realm realm,Task newTask){
        Task oldTask = realm.where(Task.class).equalTo("taskId", newTask.getTaskId()).findFirst();
        realm.beginTransaction();

        realm.copyToRealmOrUpdate(newTask);
//        oldTask.setName(newTask.getName());
//        oldTask.setDueTime(newTask.getDueTime());
//        oldTask.setRemindMode(newTask.isRemindMode());
//        oldTask.setRemindTime(newTask.getRemindTime());
//        oldTask.setDesc(newTask.getDesc());
//        oldTask.setState(newTask.getState());
//        oldTask.setRepeatMode(newTask.getRepeatMode());
//        oldTask.setRepeatTime(newTask.getRepeatTime());
//
//        oldTask.setProject(newTask.getProject());// todo
//        oldTask.setTags(newTask.getTags());// todo
//        oldTask.setBgColor(newTask.getBgColor());// todo
//        oldTask.setOwner(newTask.getOwner());//todo
//        oldTask.setCooperators(newTask.getCooperators()); // todo
        realm.commitTransaction();
    }

    public static void refreshTasks(Realm realm,List<Task> tasks){
        realm.beginTransaction();
        for (int i = 0; i < tasks.size(); i++) {
            realm.copyToRealmOrUpdate(tasks);
        }
        realm.commitTransaction();
    }

    public static void refreshTasksFixed(Realm realm,List<Task> tasks,boolean fixed){
        realm.beginTransaction();
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setFixed(fixed);
            realm.copyToRealmOrUpdate(tasks.get(i));
        }
        realm.commitTransaction();
    }

    public static void refreshTasksColor(Realm realm, List<Task> tasks ,String colorString){
        realm.beginTransaction();
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setColorValue(colorString);
            realm.copyToRealmOrUpdate(tasks.get(i));
        }
        realm.commitTransaction();
    }

    public static void refreshTasksTags(Realm realm, List<Task> tasks){

    }


    //********************Tag相关********************//

    /**
     * 找出某uid的所有tags
     * @param realm
     * @param uid
     */
    public static List<Tag> getAllTagsByUserId(Realm realm,String uid){
        return realm.copyFromRealm(realm.where(Tag.class).equalTo("owner.uid",uid).findAll());
    }

    public static void saveTag(Realm realm, final Tag tag){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(tag);
            }
        });
    }


    //********************Color相关********************//

    public static List<BgColor> getAllBgColor(Realm realm , String uid) {
        return realm.copyFromRealm(realm.where(BgColor.class).findAll());
    }

    public static void saveBgColor(Realm realm , final BgColor bgColor) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(bgColor);
            }
        });
    }

    public static void saveBgColors(Realm realm , final List<BgColor> bgColors) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < bgColors.size(); i++) {
                    realm.copyToRealm(bgColors.get(i));
                }
            }
        });
    }

    public static BgColor getBgColorByName(Realm realm ,String name) {
        return realm.copyFromRealm(realm.where(BgColor.class).equalTo("chineseName",name).or().equalTo("englishName",name).findFirst());
    }



    //********************排序********************//

    /**
     * 筛选出今天的任务
     * @param tasks
     * @return
     */
    public static List<Task> getTodayTasks(List<Task> tasks){
        List<Task> temp = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (DateUtil.isToday(tasks.get(i).getDueTime())){
                temp.add(tasks.get(i));
            }
        }
        return temp;
    }

    /**
     * 筛选出明天的任务
     * @param tasks
     * @return
     */
    public static List<Task> getTomorrowTasks(List<Task> tasks){
        List<Task> temp = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (DateUtil.isTomorrow(tasks.get(i).getDueTime())){
                temp.add(tasks.get(i));
            }
        }
        return temp;
    }


    //********************搜索********************//

    public static List<Task> searchTaskByKeyword(Realm realm,String keyword){
        List<Task> tempList = new ArrayList<>();
        List<Task> allTasks = getAllTasksByUserId(realm, getCurrentUserId());//todo 搜索有待改进
        for (int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).getName().contains(keyword)){
                tempList.add(allTasks.get(i));
            }
        }
        return tempList;
    }


}
