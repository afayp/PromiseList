package com.pfh.promiselist.model;

/**
 * 用于recyclerview中的数据源
 */

public class MultiItemModel {

    private int itemType;
    private Object data;

    public MultiItemModel(int itemType, Object data){
        this.itemType = itemType;
        this.data = data;
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
