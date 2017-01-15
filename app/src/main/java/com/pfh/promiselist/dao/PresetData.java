package com.pfh.promiselist.dao;

import android.content.Context;

import com.pfh.promiselist.MyApplication;
import com.pfh.promiselist.R;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.model.User;
import com.pfh.promiselist.others.Constant;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.SPUtil;

import io.realm.Realm;
import io.realm.RealmList;


public class PresetData {

    public static String TODAY = "2017-01-08 23:30:00";
    public static String TOMORROW = "2017-01-09 23:30:00";
    public static String FEATURE = "2017-01-10 23:30:00";

    /**
     * 第一次启动时预置的数据
     */
    public void createPresetData(Context context, Realm realm){

        User user = new User();
        user.setUsername("未登录");
        user.setPermission(0);
        RealmDB.saveUser(realm, user);
        SPUtil.put(context, Constant.CURRENT_USER_ID_KEY,user.getUid());

        Project collection = new Project();
        collection.setName("收集箱");
        collection.setOwner(user);

        Project work = new Project();
        work.setName("工作");
        work.setOwner(user);

        Project book = new Project();
        book.setName("书单");
        book.setOwner(user);

        RealmDB.saveProject(realm, collection);
        RealmDB.saveProject(realm, work);
        RealmDB.saveProject(realm, book);

        Tag tag1 = new Tag();
        tag1.setOwner(user);
        tag1.setName("2017");
        RealmDB.saveTag(realm,tag1);
   }

    /**
     * 演示用
     */
    public static void createSimulatedData(Context context , Realm realm){

        User user = new User();
        user.setUsername("小明");
        user.setPermission(0);
        RealmDB.saveUser(realm, user);
        SPUtil.put(context, Constant.CURRENT_USER_ID_KEY,user.getUid());

        Project collection = new Project();
        collection.setName("收集箱");
        collection.setOwner(user);

        Project work = new Project();
        work.setName("工作");
        work.setOwner(user);

        Project book = new Project();
        book.setName("书单");
        book.setOwner(user);

        Project shopping = new Project();
        shopping.setName("购物");
        shopping.setOwner(user);

        Project life = new Project();
        life.setName("生活");
        life.setOwner(user);

        RealmDB.saveProject(realm, collection);
        RealmDB.saveProject(realm, work);
        RealmDB.saveProject(realm, book);
        RealmDB.saveProject(realm, shopping);
        RealmDB.saveProject(realm, life);

        Tag tag1 = new Tag();
        tag1.setOwner(user);
        tag1.setName("千万别忘了");

        Tag tag2 = new Tag();
        tag2.setOwner(user);
        tag2.setName("八块腹肌");

        Tag tag3 = new Tag();
        tag3.setOwner(user);
        tag3.setName("读到老");

        Tag tag4 = new Tag();
        tag4.setOwner(user);
        tag4.setName("好电影");

        RealmDB.saveTag(realm,tag1);
        RealmDB.saveTag(realm,tag2);
        RealmDB.saveTag(realm,tag3);
        RealmDB.saveTag(realm,tag4);

        Task task1 = new Task();
        task1.setName("回复小红邮件");
        task1.setDueTime(DateUtil.str2Date("2017-01-14 10:30:00").getTime());
        task1.setColorValue(getSpecifyColor(0));
        task1.setOwner(user);

        Task task2 = new Task();
        task2.setName("视频会议");
        task2.setDueTime(DateUtil.str2Date("2017-01-14 15:30:00").getTime());
        task2.setColorValue(getSpecifyColor(7));
        RealmList<Tag> tags2 = new RealmList<>();
        tags2.add(tag1);
        task2.setTags(tags2);
        task2.setOwner(user);
        task2.setFixed(true);

        Task task3 = new Task();
        task3.setName("和产品经理撕逼");
        task3.setDueTime(DateUtil.str2Date("2017-01-15 09:20:00").getTime());
        task3.setColorValue(getSpecifyColor(6));
        task3.setOwner(user);

        Task task4 = new Task();
        task4.setName("卷腹50个");
        task4.setDueTime(DateUtil.str2Date("2017-01-14 15:30:00").getTime());
        task4.setColorValue(getSpecifyColor(4));
        RealmList<Tag> tags4 = new RealmList<>();
        tags4.add(tag2);
        task4.setTags(tags4);
        task4.setOwner(user);

        Task task5 = new Task();
        task5.setName("陪爸妈散心");
        task5.setDueTime(DateUtil.str2Date("2017-01-25 09:00:00").getTime());
        task5.setColorValue(getSpecifyColor(3));
        task5.setOwner(user);

        Task task6 = new Task();
        task6.setName("约小红出去玩");
        task6.setDueTime(DateUtil.str2Date("2017-01-18 9:30:00").getTime());
        task6.setColorValue(getSpecifyColor(5));
        task6.setOwner(user);

        Task task7 = new Task();
        task7.setName("准备小红的生日礼物");
        task7.setDueTime(DateUtil.str2Date("2017-01-16 15:30:00").getTime());
        task7.setColorValue(getSpecifyColor(2));
        RealmList<Tag> tags7 = new RealmList<>();
        tags7.add(tag1);
        task7.setTags(tags7);
        task7.setOwner(user);

        Task task8 = new Task();
        task8.setName("《演员的自我修养》");
        task8.setDueTime(DateUtil.str2Date("2017-01-16 15:30:00").getTime());
        task8.setColorValue(getSpecifyColor(3));
        RealmList<Tag> tags8 = new RealmList<>();
        tags8.add(tag3);
        task8.setTags(tags8);
        task8.setOwner(user);

        Task task9 = new Task();
        task9.setName("完美陌生人");
        task9.setDueTime(DateUtil.str2Date("2017-01-21 18:30:00").getTime());
        task9.setColorValue(getSpecifyColor(1));
        RealmList<Tag> tags9 = new RealmList<>();
        tags9.add(tag4);
        task9.setTags(tags9);
        task9.setOwner(user);

        Task task10 = new Task();
        task10.setName("《黄金时代》");
        task10.setDueTime(DateUtil.str2Date("2017-02-02 15:30:00").getTime());
        task10.setColorValue(getSpecifyColor(3));
        RealmList<Tag> tags10 = new RealmList<>();
        tags10.add(tag3);
        task10.setTags(tags10);
        task10.setOwner(user);

        Task task11 = new Task();
        task11.setName("晚上购物清单");
        task11.setDueTime(DateUtil.str2Date("2017-01-16 19:00:00").getTime());
        task11.setColorValue(getSpecifyColor(4));
        task11.setDesc("吐司 \n 果酱 \n 牛奶 \n 酱油");
        task11.setOwner(user);

        RealmDB.saveTaskToProject(realm,work.getProjectId(),task1);
        RealmDB.saveTaskToProject(realm,work.getProjectId(),task2);
        RealmDB.saveTaskToProject(realm,work.getProjectId(),task3);
        RealmDB.saveTaskToProject(realm,collection.getProjectId(),task4);
        RealmDB.saveTaskToProject(realm,life.getProjectId(),task5);
        RealmDB.saveTaskToProject(realm,life.getProjectId(),task6);
        RealmDB.saveTaskToProject(realm,life.getProjectId(),task7);
        RealmDB.saveTaskToProject(realm,book.getProjectId(),task8);
        RealmDB.saveTaskToProject(realm,life.getProjectId(),task9);
        RealmDB.saveTaskToProject(realm,book.getProjectId(),task10);
        RealmDB.saveTaskToProject(realm,collection.getProjectId(),task11);

    }

    public static String getSpecifyColor(int index){
        String[] colorArray = MyApplication.getContext().getResources().getStringArray(R.array.colors);
        return colorArray[index];
    }











}
