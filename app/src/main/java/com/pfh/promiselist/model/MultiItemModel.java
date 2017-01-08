package com.pfh.promiselist.model;

import com.pfh.promiselist.others.Constant;

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

    public MultiItemModel(int itemType, Object content){
        this.itemType = itemType;
        this.content = content;
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiItemModel that = (MultiItemModel) o;

        if (itemType != that.itemType) return false;
        if (expand != that.expand) return false;
        if (isShowSetting != that.isShowSetting) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        return data != null ? data.equals(that.data) : that.data == null;

    }

    @Override
    public int hashCode() {
        int result = itemType;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (expand ? 1 : 0);
        result = 31 * result + (isShowSetting ? 1 : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
