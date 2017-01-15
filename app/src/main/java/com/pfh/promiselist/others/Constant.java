package com.pfh.promiselist.others;

/**
 * 集中保存常量
 */

public class Constant {

    public static final String CURRENT_USERId = "current_user_id";//当前登录用户uid


    //----------------main activity-----------------//
    public static final int ORDER_BY_DATE = 1;//按日期排序
    public static final int ORDER_BY_PROJECT = 2;// 按项目排序
    public static final int ORDER_BY_IMPORTANCE = 3;// 按优先级排序
    public static final int ORDER_BY_COLOR = 4;// 按颜色排序

    public static final int DISPLAY_TODAY = 1; // 展示今日任务，
    public static final int DISPLAY_ALL_TASKS = 2; // 所有任务,默认按时间排序
    public static final int DISPLAY_PROJECT = 3; // 某个具体任务
    public static final int DISPLAY_TAG = 4; // 某个具体tag

    // tasklist adapter item type
    public static final int ITEM_TYPE_TASK = 1;
    public static final int ITEM_TYPE_TIME = 2;
    public static final int ITEM_TYPE_PROJECT = 3;
    public static final int ITEM_TYPE_IMPORTANCE = 4;
    public static final int ITEM_TYPE_OTHER = 5;
    public static final int ITEM_TYPE_FIXED = 6;

    //----------------nav item-----------------//
    public static final int NAV_TYPE_TAG = 1;
    public static final int NAV_TYPE_PROJECT = 2;

    //----------------nav item-----------------//
    public static final int MANAGE_TYPE_PROJECT = 1;
    public static final int MANAGE_TYPE_TAG = 2;
    public static final int MANAGE_TYPE_NEW_PROJECT = 3;
    public static final int MANAGE_TYPE_NEW_TAG = 4;


    //----------------preference key-----------------//

    public static final String CURRENT_USER_ID_KEY = "current_user_id_key";
    public static final String IS_FIRST_START_UP_KEY = "is_first_start_up_key"; // 是否是第一次进入应用，如果是要有引导页和预置数据。
    public static final String USER_AVATAR_KEY = "user_avatar_key"; // 用户头像,相册中的文件地址，如果没有没用默认的
    public static final String USER_BG_KEY = "user_bg_key"; // 用户界面背景
    public static final String DISPLAY_INDEX_KEY = "display_index_key"; // 用户上次选择的展示模式
    public static final String LAST_SELECTED_PROJECT_KEY = "last_selected_project_key";// 用户上次选择的在主界面展示的projectId
    public static final String LAST_SELECTED_TAG_KEY = "last_selected_tag_key";// 用户上次选择的在主界面展示的projectId
    public static final String lAST_ORDER_MODE_KEY = "order_mode_key";// 用户上次选择的排序模式



}
