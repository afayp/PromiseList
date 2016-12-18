package com.pfh.promiselist.model;

import com.pfh.promiselist.utils.Constant;

/**
 * 用于recyclerview中的数据源
 */

public class MultiItemModel {

    private int itemType;
    private Object data; // task 或者String
    private String label; // 所属的类别，今天、清单名、高

    public MultiItemModel(int itemType, Object data){
        this.itemType = itemType;
        this.data = data;
    }

    public MultiItemModel(int itemType, Object data,String label){
        this.itemType = itemType;
        this.data = data;
        this.label = label;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getLabel() {
        if (itemType != Constant.ITEM_TYPE_TASK){
            return (String) data;// 非task label就是data
        }
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getData(){
        return data;
    }

    public int getItemType(){
        return itemType;
    }


    @Override
    public String toString() {
        return "MultiItemModel{" +
                "itemType=" + itemType +
                ", data=" + data +
                '}';
    }
}
