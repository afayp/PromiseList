package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pfh.promiselist.R;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class UserToolbar extends LinearLayout {

    private ImageView iv_back;
    private ImageView iv_edit;

    public UserToolbar(Context context) {
        this(context,null);
    }

    public UserToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UserToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_user_toolbar,this,true);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
    }

    public void setBackListener(OnClickListener listener){
        iv_back.setOnClickListener(listener);
    }

    public void setEditListener(OnClickListener listener) {
        iv_edit.setOnClickListener(listener);
    }
}
