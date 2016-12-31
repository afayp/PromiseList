package com.pfh.promiselist.model;

import com.pfh.promiselist.others.Constant;

import java.util.List;

/**
 * 用于recyclerview中的数据源
 */

public class MultiItemModel {

    private int itemType;
    private Object content; // 对task type即是task,对非task type即是string
    private String label; // 所属的类别，今天、清单名、高、固定、其他
    private boolean expand = true; // 是否展开(可见)，对title来标识是否显示子任务数 对task来标识是否gone
    private boolean isShowSetting; // for taskitem 是否显示setting部分 @废弃
    private Object data;// 保存真实类型 对非task type即是project,对task type即是task。
    private List<MultiItemModel> childern;

    public MultiItemModel(int itemType, Object content){
        this.itemType = itemType;
        this.content = content;
    }

    public MultiItemModel(int itemType, Object content, List<MultiItemModel> childern) {
        this.itemType = itemType;
        this.content = content;
        this.childern = childern;
    }

    // for task
    public MultiItemModel(int itemType, Object content, String label){
        this.itemType = itemType;
        this.content = content;
        this.label = label;
    }

    public boolean isShowSetting() {
        return isShowSetting;
    }

    public void setShowSetting(boolean showSetting) {
        isShowSetting = showSetting;
    }

    public List<MultiItemModel> getChildern() {
        return childern;
    }

    public void setChildern(List<MultiItemModel> childern) {
        this.childern = childern;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getLabel() {
        if (itemType != Constant.ITEM_TYPE_TASK){
            return (String) content;// 非task label就是content
        }
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getContent(){
        return content;
    }

    public int getItemType(){
        return itemType;
    }


    @Override
    public String toString() {
        return "MultiItemModel{" +
                "itemType=" + itemType +
                ", content=" + content +
                ", label='" + label + '\'' +
                ", expand=" + expand +
                ", isShowSetting=" + isShowSetting +
                ", data=" + data +
                ", childern=" + childern +
                '}';
    }
}
