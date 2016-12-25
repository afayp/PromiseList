package com.pfh.promiselist.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.UuidUtils;
import com.pfh.promiselist.widget.OtherSettingSelector;
import com.pfh.promiselist.widget.ProjectSelector;
import com.pfh.promiselist.widget.TimeSelector;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Date;
import java.util.List;


/**
 * 新建单条任务
 */

public class NewTaskActivity extends BaseActivity {

    private ProjectSelector projectSelector;
    private MaterialEditText et_title;
    private TimeSelector timeSelector;
    private OtherSettingSelector otherSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        findViews();
        loadData();

    }

    private void findViews() {
        RelativeLayout rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        TextView tv_new_item = (TextView) findViewById(R.id.tv_new_item);
        ImageView iv_ok = (ImageView) findViewById(R.id.iv_ok);
        ImageView iv_delete = (ImageView) findViewById(R.id.iv_delete);
        et_title = (MaterialEditText) findViewById(R.id.et_title);
        timeSelector = (TimeSelector) findViewById(R.id.timeSelector);
        projectSelector = (ProjectSelector) findViewById(R.id.projectSelector);
        otherSetting = (OtherSettingSelector) findViewById(R.id.otherSetting);
        FloatingActionButton fb_add = (FloatingActionButton) findViewById(R.id.fb_add);

        tv_new_item.setText(R.string.new_task);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });
        iv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
                exitActivity();
            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });
        fb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
                exitActivity();
            }
        });
    }

    private void loadData() {
        List<Project> allActiveProjects = RealmDB.getAllActiveProjectsByUserId(mRealm, RealmDB.getCurrentUserId());
        projectSelector.setProjectList(allActiveProjects);
    }

    private void saveTask() {
        Task task = new Task();
        task.setTaskId(UuidUtils.getShortUuid());
        task.setName(et_title.getText().toString());
        task.setStartTime(new Date().getTime());
        task.setCreatedTime(new Date().getTime());
        task.setDueTime(timeSelector.getSelectedCalendar().getTimeInMillis());
        task.setProject(projectSelector.getSelectedProject());
        //...
        RealmDB.saveTaskToProject(mRealm,projectSelector.getSelectedProject().getProjectId(),task);
    }

}
