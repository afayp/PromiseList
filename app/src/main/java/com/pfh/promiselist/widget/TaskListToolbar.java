package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;

/**
 * 主界面toolbar
 */

public class TaskListToolbar extends LinearLayout {
    private boolean select; // 是否选择状态
    private boolean fixedActive;// 固定按钮是否亮起

    private ImageView iv_projects;
    private ImageView iv_sort;
    private ImageView iv_search;
    private ImageView iv_more;
    private LinearLayout ll_normal;
    private LinearLayout ll_select;
    private ImageView iv_back;
    private TextView tv_num;
    private ImageView iv_palette;
    private ImageView iv_tag;
    private ImageView iv_fixed;
    private ImageView iv_date;
    private ImageView iv_more_select;

    public TaskListToolbar(Context context) {
        this(context,null);
    }

    public TaskListToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TaskListToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_task_list_toolbar, this, true);
        ll_normal = (LinearLayout) view.findViewById(R.id.ll_normal);
        iv_projects = (ImageView) view.findViewById(R.id.iv_projects);
        iv_sort = (ImageView) view.findViewById(R.id.iv_sort);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_more = (ImageView) view.findViewById(R.id.iv_more);

        ll_select = (LinearLayout) view.findViewById(R.id.ll_select);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        iv_palette = (ImageView) view.findViewById(R.id.iv_palette);
        iv_tag = (ImageView) view.findViewById(R.id.iv_tag);
        iv_fixed = (ImageView) view.findViewById(R.id.iv_fixed);
        iv_date = (ImageView) view.findViewById(R.id.iv_date);
        iv_more_select = (ImageView) view.findViewById(R.id.iv_more_select);
    }


    public void setSelect(boolean state){
        select = state;
        ll_normal.setVisibility(select ? GONE : VISIBLE);
        ll_select.setVisibility(select ? VISIBLE : GONE);
    }

    public boolean isSelect(){
        return select;
    }

    public void setFixedIconActive(boolean active){
        iv_fixed.setImageResource(active ? R.drawable.ic_fixed_blue : R.drawable.ic_fixed);
        fixedActive = active;
    }

    public boolean isFixedActive(){
        return fixedActive;
    }

    public void setSelectCount(int count){
        tv_num.setText(count+"");
    }

    public ImageView getIvProjects(){
        return iv_projects;
    }

    public ImageView getIvSort(){
        return iv_sort;
    }

    public ImageView getIvSearch(){
        return iv_search;
    }

    public ImageView getIvMore(){
        return iv_more;
    }

    public ImageView getIvBack(){
        return iv_back;
    }

    public ImageView getIvFixed(){
        return iv_fixed;
    }

    public ImageView getIvPalette(){
        return iv_palette;
    }

    public ImageView getIvTag(){
        return iv_tag;
    }

    public ImageView getIvDate(){
        return iv_date;
    }

    public ImageView getIVMoreSelect(){
        return iv_more_select;
    }

}
