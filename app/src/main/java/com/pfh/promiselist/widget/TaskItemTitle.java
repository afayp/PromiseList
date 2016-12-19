package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;

/**
 * 任务列表中的title
 */

public class TaskItemTitle extends LinearLayout{
    private boolean expand;// 下面的子任务是否是展开状态
    private CircleSurroundText tv_num;
    private TextView tv_title;

    public TaskItemTitle(Context context) {
        this(context,null);
    }

    public TaskItemTitle(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TaskItemTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_title_item_view,this,true);
        LinearLayout ll_root = (LinearLayout) findViewById(R.id.ll_root);
        tv_num = (CircleSurroundText) findViewById(R.id.tv_num);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    public void toggle(){
        expand = !expand;
        tv_num.setVisibility(expand ? INVISIBLE : VISIBLE);
    }

    public void setNum(String text){
        tv_num.setText(text);
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }
}
