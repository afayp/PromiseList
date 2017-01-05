package com.pfh.promiselist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.TaskInfo;
import com.pfh.promiselist.service.NotificationService;
import com.pfh.promiselist.service.RemindService;
import com.pfh.promiselist.widget.CircleImageView;
import com.pfh.promiselist.widget.SettingItemIconView;
import com.pfh.promiselist.widget.SettingItemSwitchView;

import java.util.ArrayList;

/**
 * 设置界面
 */

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        findViews();

    }

    private void findViews() {
        LinearLayout ll_user = (LinearLayout) findViewById(R.id.ll_user);
        CircleImageView iv_avatar = (CircleImageView) findViewById(R.id.iv_avatar);
        TextView tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        SettingItemIconView setting_preferences = (SettingItemIconView) findViewById(R.id.setting_preferences);
        SettingItemIconView setting_remind = (SettingItemIconView) findViewById(R.id.setting_remind);
        SettingItemSwitchView setting_notification = (SettingItemSwitchView) findViewById(R.id.setting_notification);
        SettingItemSwitchView setting_task_remind = (SettingItemSwitchView) findViewById(R.id.setting_task_remind);
        SettingItemSwitchView setting_gesture_password = (SettingItemSwitchView) findViewById(R.id.setting_gesture_password);

        SettingItemIconView setting_help = (SettingItemIconView) findViewById(R.id.setting_help);
        SettingItemIconView setting_feedback = (SettingItemIconView) findViewById(R.id.setting_feedback);
        SettingItemIconView setting_about = (SettingItemIconView) findViewById(R.id.setting_about);

        setting_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 通知栏
        setting_notification.getSb().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ArrayList<TaskInfo> taskInfos = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        TaskInfo taskInfo = new TaskInfo();
                        taskInfo.setName("name - "+i);
                        taskInfo.setDueTime(i);
                        taskInfo.setTaskId(i);
                        taskInfos.add(taskInfo);
                    }
                    Intent intent = new Intent(SettingActivity.this, NotificationService.class);
                    intent.putExtra("task_info_list",taskInfos);
                    startService(intent);
                }else {
                    Intent intent = new Intent(SettingActivity.this, NotificationService.class);
                    stopService(intent);
                }
            }
        });

        setting_task_remind.getSb().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ArrayList<TaskInfo> taskInfos = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        TaskInfo taskInfo = new TaskInfo();
                        taskInfo.setName("name - "+i);
                        taskInfo.setDueTime(i);
                        taskInfo.setTaskId(i);
                        taskInfos.add(taskInfo);
                    }
                    Intent intent = new Intent(SettingActivity.this, RemindService.class);
                    intent.putExtra("task_info_list",taskInfos);
                    startService(intent);
                }else {
                    Intent intent = new Intent(SettingActivity.this, RemindService.class);
                    stopService(intent);
                }
            }
        });

        setting_gesture_password.getSb().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }
}
