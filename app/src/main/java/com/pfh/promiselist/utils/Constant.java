package com.pfh.promiselist.utils;

/**
 * 集中保存常量
 */

public class Constant {

    public static final String CURRENT_USERId = "CURRENT_USERId";//当前登录用户uid

    public static final String LAST_ORDER_MODE = "LAST_ORDER_MODE";// 用户上次选择的排序模式
    public static final int ORDER_BY_DATE = 1;//按日期排序
    public static final int ORDER_BY_PROJECT = 2;// 按项目排序
    public static final int ORDER_BY_IMPORTANCE = 3;// 按优先级排序

    public static final String LAST_SELECTED_PROJECT = "LAST_SELECTED_PROJECT";// 用户上次选择的在主界面展示的projectId

    // tasklist adapter item type
    public static final int ITEM_TYPE_TASK = 1;
    public static final int ITEM_TYPE_TIME = 2;
    public static final int ITEM_TYPE_PROJECT = 3;
    public static final int IITEM_TYPE_IMPORTANCE = 4;

}
