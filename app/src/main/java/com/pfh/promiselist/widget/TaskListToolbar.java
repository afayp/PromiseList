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
    private boolean sortByTime = true;
    private boolean streamLayout = true; //linear or stagger

    private ImageView iv_menu;
    private ImageView iv_sort;
    private ImageView iv_layout;
//    private ImageView iv_more;
    private LinearLayout ll_normal;
    private LinearLayout ll_select;
    private ImageView iv_back;
    private TextView tv_num;
    private ImageView iv_palette;
    private ImageView iv_tag;
    private ImageView iv_fixed;
    private ImageView iv_date;
    private ImageView iv_more_select;
    private ImageView iv_select_project;
    private TextView tv_title;

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
        iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
        iv_sort = (ImageView) view.findViewById(R.id.iv_sort);
        iv_layout = (ImageView) view.findViewById(R.id.iv_layout);
//        iv_more = (ImageView) view.findViewById(R.id.iv_more);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        ll_select = (LinearLayout) view.findViewById(R.id.ll_select);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_num = (TextView) view.findViewById(R.id.tv_num);
        iv_palette = (ImageView) view.findViewById(R.id.iv_palette);
        iv_tag = (ImageView) view.findViewById(R.id.iv_tag);
        iv_fixed = (ImageView) view.findViewById(R.id.iv_fixed);
        iv_select_project = (ImageView) view.findViewById(R.id.iv_project);
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

    public void setTitle(String title){
        tv_title.setText(title);
    }

    public boolean isFixedActive(){
        return fixedActive;
    }

    public void setSelectCount(int count){
        tv_num.setText(count+"");
    }

    public ImageView getIvMenu(){
        return iv_menu;
    }

    public ImageView getIvSort(){
        return iv_sort;
    }

    public void toggleSort(){
        sortByTime = !sortByTime;
        iv_sort.setImageResource(sortByTime ? R.drawable.ic_sort_by_time : R.drawable.ic_sort_by_project);
    }

    public void toggleLayout(){
        streamLayout = !streamLayout;
        iv_layout.setImageResource(streamLayout ? R.drawable.ic_stream_layout : R.drawable.ic_stagger_layout);
    }

    public boolean isStreamLayout(){
        return streamLayout;
    }

    public boolean sortByTime(){
        return sortByTime;
    }

    public ImageView getIvLayout(){
        return iv_layout;
    }

//    public ImageView getIvMore(){
//        return iv_more;
//    }

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

    public ImageView getIvSelectPorject() {return iv_select_project;}

    public ImageView getIvDate(){
        return iv_date;
    }

    public ImageView getIVMoreSelect(){
        return iv_more_select;
    }


}
