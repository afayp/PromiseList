package com.pfh.promiselist.model;

import io.realm.RealmObject;


/**
 * item 的颜色
 */

public class BgColor extends RealmObject {

    private String value; // #AARRGGBB
    private String chineseName;
    private String englishName;
    private boolean customize; // 是程序预置的还是用户自定义的
//    private User owner; // 暂不考虑多用户


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public boolean isCustomize() {
        return customize;
    }

    public void setCustomize(boolean customize) {
        this.customize = customize;
    }

    @Override
    public String toString() {
        return "BgColor{" +
                "value='" + value + '\'' +
                ", chineseName='" + chineseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", customize=" + customize +
                '}';
    }
}
