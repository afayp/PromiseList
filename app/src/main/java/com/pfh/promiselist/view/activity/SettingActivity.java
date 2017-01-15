package com.pfh.promiselist.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pfh.promiselist.R.id.setting_gesture_password;
import static com.pfh.promiselist.R.id.setting_notification;
import static com.pfh.promiselist.R.id.setting_task_remind;

/**
 * 设置界面
 */

public class SettingActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.setting_preferences)
    SettingItemIconView settingPreferences;
    @BindView(R.id.setting_remind)
    SettingItemIconView settingRemind;
    @BindView(setting_notification)
    SettingItemSwitchView settingNotification;
    @BindView(setting_task_remind)
    SettingItemSwitchView settingTaskRemind;
    @BindView(setting_gesture_password)
    SettingItemSwitchView settingGesturePassword;
    @BindView(R.id.setting_help)
    SettingItemIconView settingHelp;
    @BindView(R.id.setting_feedback)
    SettingItemIconView settingFeedback;
    @BindView(R.id.setting_about)
    SettingItemIconView settingAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initViews();

    }

    private void initViews() {

        settingPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 通知栏
        settingNotification.getSb().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ArrayList<TaskInfo> taskInfos = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        TaskInfo taskInfo = new TaskInfo();
                        taskInfo.setName("name - " + i);
                        taskInfo.setDueTime(i);
                        taskInfo.setTaskId(i);
                        taskInfos.add(taskInfo);
                    }
                    Intent intent = new Intent(SettingActivity.this, NotificationService.class);
                    intent.putExtra("task_info_list", taskInfos);
                    startService(intent);
                } else {
                    Intent intent = new Intent(SettingActivity.this, NotificationService.class);
                    stopService(intent);
                }
            }
        });

        settingTaskRemind.getSb().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ArrayList<TaskInfo> taskInfos = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        TaskInfo taskInfo = new TaskInfo();
                        taskInfo.setName("name - " + i);
                        taskInfo.setDueTime(i);
                        taskInfo.setTaskId(i);
                        taskInfos.add(taskInfo);
                    }
                    Intent intent = new Intent(SettingActivity.this, RemindService.class);
                    intent.putExtra("task_info_list", taskInfos);
                    startService(intent);
                } else {
                    Intent intent = new Intent(SettingActivity.this, RemindService.class);
                    stopService(intent);
                }
            }
        });

        settingGesturePassword.getSb().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back(){
        exitActivity();
    }

    @OnClick(R.id.setting_about)
    public void about(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent,this);
    }
}
