package com.pfh.promiselist.dao;

import com.pfh.promiselist.model.BgColor;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.model.User;
import com.pfh.promiselist.utils.DateUtil;

/**
 * 模拟数据，测试用
 */

public class SimulatedData {

    public static String TODAY = "2016-12-25 23:30:00";
    public static String TOMORROW = "2016-12-26 23:30:00";
    public static String FEATURE = "2016-12-29 23:30:00";

    public static User getCurrentUser(){

        User user = new User();
        user.setUid("user_111");
        user.setUsername("afayp");
        user.setPassword("12345");
        user.setMobilePhoneNumber("15757172672");
        user.setEmail("pan464324435@qq.com");
        user.setMobilePhoneVerified(false);
        user.setEmailVerified(false);
        user.setGender(2);
        user.setRegisterTime(1481353391289L);
        user.setPermission(0);
        return user;
    }


    public static Project getWorkProject(){
        Project work = new Project();
        work.setName("Work");
        work.setProjectId("project_111");
        work.setOwner(getCurrentUser());
        work.setState(1);
        return work;
    }

    public static Project getReadProject(){
        Project read = new Project();
        read.setName("Read");
        read.setProjectId("project_222");
        read.setOwner(getCurrentUser());
        read.setState(1);
        return read;
    }

    public static Project getTravelProject(){
        Project travel = new Project();
        travel.setName("Travel");
        travel.setProjectId("project_333");
        travel.setOwner(getCurrentUser());
        travel.setState(1);
        return travel;
    }

    public static Task getTask1(){
        Task task1 = new Task();
        task1.setName("Email jack back");
        task1.setTaskId("task_1");
        task1.setOwner(getCurrentUser());
        task1.setState(1);
        task1.setDueTime(DateUtil.str2Date(TODAY).getTime());
        task1.setBgColor(getLowBgColor());
        return task1;
    }

    public static Task getTask2(){
        Task task2 = new Task();
        task2.setName("Finish the job");
        task2.setTaskId("task_2");
        task2.setOwner(getCurrentUser());
        task2.setState(1);
        task2.setDueTime(DateUtil.str2Date(FEATURE).getTime());
        task2.setBgColor(getNormalBgColor());
        return task2;
    }

    public static Task getTask3(){
        Task task3 = new Task();
        task3.setName("演员的自我修养");
        task3.setTaskId("task_3");
        task3.setOwner(getCurrentUser());
        task3.setState(1);
        task3.setDueTime(DateUtil.str2Date(TODAY).getTime());
        task3.setBgColor(getHighBgColor());
        return task3;
    }

    public static Task getTask4(){
        Task task4 = new Task();
        task4.setName("马尔代夫");
        task4.setTaskId("task_4");
        task4.setOwner(getCurrentUser());
        task4.setState(1);
        task4.setDueTime(DateUtil.str2Date(TODAY).getTime());
        task4.setBgColor(getLowBgColor());
        return task4;
    }
    public static Task getTask5(){
        Task task5 = new Task();
        task5.setName("巴厘岛");
        task5.setTaskId("task_5");
        task5.setOwner(getCurrentUser());
        task5.setState(1);
        task5.setDueTime(DateUtil.str2Date(TOMORROW).getTime());
        task5.setBgColor(getHighBgColor());
        return task5;
    }

    public static Task getTask6(){
        Task task6 = new Task();
        task6.setName("NewYork");
        task6.setTaskId("task_6");
        task6.setOwner(getCurrentUser());
        task6.setState(1);
        task6.setDueTime(DateUtil.str2Date(TOMORROW).getTime());
        task6.setBgColor(getNormalBgColor());
        return task6;
    }

    public static Task getTask7(){
        Task task7 = new Task();
        task7.setName("Jogging thirty minutes");
        task7.setTaskId("task_7");
        task7.setOwner(getCurrentUser());
        task7.setState(1);
        task7.setDueTime(DateUtil.str2Date(FEATURE).getTime());
        task7.setBgColor(getLowBgColor());
        return task7;
    }

    public static Task getTask8(){
        Task task8 = new Task();
        task8.setName("哈利波特");
        task8.setTaskId("task_8");
        task8.setOwner(getCurrentUser());
        task8.setState(1);
        task8.setDueTime(DateUtil.str2Date(TODAY).getTime());
        task8.setBgColor(getHighBgColor());
        return task8;
    }

    public static BgColor getHighBgColor(){
        BgColor bgColor = new BgColor();
        bgColor.setChineseName("高优先级");
        bgColor.setEnglishName("highImportance");
        bgColor.setValue("#E1D2C8");
        bgColor.setCustomize(false);
        return bgColor;
    }

    public static BgColor getNormalBgColor(){
        BgColor bgColor = new BgColor();
        bgColor.setChineseName("正常优先级");
        bgColor.setEnglishName("normalImportance");
        bgColor.setValue("#6EC9FD");
        bgColor.setCustomize(false);
        return bgColor;
    }

    public static BgColor getLowBgColor(){
        BgColor bgColor = new BgColor();
        bgColor.setChineseName("低优先级");
        bgColor.setEnglishName("lowImportance");
        bgColor.setValue("#BBD9BD");
        bgColor.setCustomize(false);
        return bgColor;
    }
}
