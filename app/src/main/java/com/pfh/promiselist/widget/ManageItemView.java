package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.others.Constant;

import io.realm.Realm;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class ManageItemView extends RelativeLayout {
    private int type;
    private boolean editing; //
    private ImageView iv_icon;
    private ImageView iv_edit_ok;
    private TextView tv_name;

    private Project project;
    private Tag tag;
    private Realm realm;
    private String label;
    private String defaultName;
    private String inputName;
    private ImageView iv_delete;

    public ManageItemView(Context context) {
        this(context,null);
    }

    public ManageItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ManageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_manage_item, this, true);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        iv_edit_ok = (ImageView) view.findViewById(R.id.iv_edit_ok);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
    }

    private void saveName(String name){
        if (type == Constant.MANAGE_TYPE_PROJECT ) {
            project.setName(name);
        }else if (type == Constant.MANAGE_TYPE_TAG) {
            tag.setName(name);
        }
    }

    private boolean checkExits(String name) {
        if (type == Constant.MANAGE_TYPE_PROJECT ) {
            if (project.getName().equals(name)) { // 原来的名字
                return false;
            }
            return RealmDB.isProjectNameExist(realm,name);
        }else if (type == Constant.MANAGE_TYPE_TAG ) {
            if (tag.getName().equals(name)) { // 原来的名字
                return false;
            }
            return RealmDB.isTagNameExist(realm,name);
        }
        return false;
    }

    private void setType(int type){
        this.type = type;
        if (type == Constant.MANAGE_TYPE_PROJECT){
            label = "清单";
            defaultName = project.getName();
            iv_icon.setImageResource(R.drawable.ic_menu_grey );
        }else if (type == Constant.MANAGE_TYPE_TAG) {
            label = "标签";
            defaultName = tag.getName();
            iv_icon.setImageResource(R.drawable.ic_tag_grey);
        }
    }

    public ImageView getIvDelete(){
        return iv_delete;
    }

    public ImageView getIvEdit(){
        return iv_edit_ok;
    }


    public void setProject(Project project,int type,Realm realm){
        this.realm = realm;
        this.project = project;
        tv_name.setText(project.getName());
        setType(type);
    }

    public void setTag(Tag tag,int type,Realm realm){
        this.realm = realm;
        this.tag = tag;
        tv_name.setText(tag.getName());
        setType(type);
    }

}
