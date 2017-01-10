package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;

/**
 * 用来在task中显示时间、tag等信息
 */

public class TagView extends LinearLayout {

    public static final String TIME_TYPE = "time_type";
    public static final String TAG_TYPE = "tag_type";
    public static final String PROJECT_TYPE = "project_type";
    private ImageView iv_icon;
    private TextView tv_content;

    public TagView(Context context) {
        this(context,null);
    }

    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_tag_view,this,true);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_content = (TextView) findViewById(R.id.tv_content);
    }

    public void setData(String type,String content){
        tv_content.setText(content);
        if (type.equals(TIME_TYPE)) {
            iv_icon.setImageResource(R.drawable.ic_clock_grey);
        }else if (type.equals(TAG_TYPE)) {
            iv_icon.setImageResource(R.drawable.ic_tag_grey);
        }else if (type.equals(PROJECT_TYPE)) {
            iv_icon.setImageResource(R.drawable.ic_package_grey);
        }

    }
}
