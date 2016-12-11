package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pfh.promiselist.R;

/**
 * 主界面toolbar
 */

public class TaskListToolbar extends LinearLayout {

    private ImageView iv_menu;
    private ImageView iv_sort;
    private ImageView iv_search;
    private ImageView iv_more;

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
        iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
        iv_sort = (ImageView) view.findViewById(R.id.iv_sort);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_more = (ImageView) view.findViewById(R.id.iv_more);
    }


}
