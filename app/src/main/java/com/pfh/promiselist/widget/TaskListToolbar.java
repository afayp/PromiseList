package com.pfh.promiselist.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pfh.promiselist.R;
import com.pfh.promiselist.utils.ImeUtil;

/**
 * 主界面toolbar
 */

public class TaskListToolbar extends LinearLayout {

    private ImageView iv_projects;
    private ImageView iv_sort;
    private ImageView iv_search;
    private ImageView iv_more;
    private ImageView iv_back;
    private EditText et_task;
    private long lastChangeTime;

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
        iv_projects = (ImageView) view.findViewById(R.id.iv_projects);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        et_task = (EditText) view.findViewById(R.id.et_task);
        iv_sort = (ImageView) view.findViewById(R.id.iv_sort);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_more = (ImageView) view.findViewById(R.id.iv_more);

        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_back.setVisibility(GONE);
                iv_projects.setVisibility(VISIBLE);
                et_task.setVisibility(GONE);
                iv_search.setVisibility(VISIBLE);
                iv_sort.setVisibility(VISIBLE);
                et_task.setText("");
                et_task.clearFocus();
                ImeUtil.hideSoftKeyboard(et_task);

            }
        });

        iv_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_projects.setVisibility(GONE);
                iv_back.setVisibility(VISIBLE);
                et_task.setVisibility(VISIBLE);
                iv_search.setVisibility(GONE);
                iv_sort.setVisibility(GONE);

                et_task.requestFocus();
                ImeUtil.showSoftKeyboard(et_task);
            }
        });

        et_task.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onSearchTaskListener.onSearch(s.toString());// TODO: 2016/12/17 过滤多次请求
            }
        });
    }

    public void setProjectsClickListener(OnClickListener listener){
        iv_projects.setOnClickListener(listener);
    }

    public void setSortClickListener(OnClickListener listener){
        iv_sort.setOnClickListener(listener);
    }

    public void setSearchClickListener(OnClickListener listener){
        iv_search.setOnClickListener(listener);
    }

    public void setMoreClickListener(OnClickListener listener){
        iv_more.setOnClickListener(listener);
    }

    private onSearchTaskListener onSearchTaskListener;

    public interface onSearchTaskListener{
        void onSearch(String keyword);
    }

    public void setOnSearchTaskListener(onSearchTaskListener listener){
        this.onSearchTaskListener = listener;
    }

}
