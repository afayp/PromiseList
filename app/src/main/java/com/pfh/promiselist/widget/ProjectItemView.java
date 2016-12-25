package com.pfh.promiselist.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Task;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * 项目列表中的单条项目
 */

public class ProjectItemView extends LinearLayout {
    private Context mContext;
    private Project mData;
    private TextView tv_project_name;
    private ImageView iv_progress;
    private TextView tv_progerss;
    private LinearLayout ll_tasks;
    private List<Task> tasks;
    private List<CheckBox> checkBoxList = new ArrayList<>();
    private boolean expand = true;//是否显示子任务

    public ProjectItemView(Context context) {
        this(context,null);
    }

    public ProjectItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProjectItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_project_item_view,this,true);
        RelativeLayout rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        LinearLayout ll_summary = (LinearLayout) findViewById(R.id.ll_summary);
        tv_project_name = (TextView) findViewById(R.id.tv_project_name);
        iv_progress = (ImageView) findViewById(R.id.iv_progress);
        tv_progerss = (TextView) findViewById(R.id.tv_progerss);
        ll_tasks = (LinearLayout) findViewById(R.id.ll_tasks);

        //点击进入该项目任务列表页
        rl_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //点击收起子任务
        ll_summary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand = !expand;
                if (expand){
                    ll_tasks.setVisibility(VISIBLE);
                }else {
                    ll_tasks.setVisibility(GONE);
                }
                //todo 通知recyclerview刷新
            }
        });
    }
    public void setProject(Project project, Realm realm){
        mData = project;
        tv_project_name.setText(project.getName());
//        iv_progress.setImageDrawable();
        tv_progerss.setText("4/6");

        tasks = RealmDB.getAllTasksByProjectId(realm, project.getProjectId());
        addTasks();
    }

    private void addTasks(){
        for (int i = 0; i < tasks.size(); i++) {
            CheckBox cb = new CheckBox(mContext);
            cb.setEnabled(false);// 暂时不允许直接修改
            cb.setText(tasks.get(i).getName());
            if (tasks.get(i).getState() == 3){
                continue;// 已删除的不显示
            }else if (tasks.get(i).getState() == 2){
                cb.setChecked(true);//已完成
            }else if (tasks.get(i).getState() == 1){
                cb.setChecked(false);//未完成
            }

            cb.setTextColor(Color.parseColor(tasks.get(i).getBgColor().getValue()));

//            if (tasks.get(i).getImportance() == 1){
//                cb.setTextColor(ColorsUtil.BLUE_LIGHT);
//            }else if (tasks.get(i).getImportance() == 2){
//                cb.setTextColor(ColorsUtil.SKYBLUE);
//            }else if (tasks.get(i).getImportance() == 3){
//                cb.setTextColor(ColorsUtil.RED);
//            }
            checkBoxList.add(cb);
            ll_tasks.addView(cb);
        }
    }
}
