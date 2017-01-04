package com.pfh.promiselist.view.activity;

import android.os.Bundle;

import com.pfh.promiselist.R;

/**
 * 在通知栏点击加号 出现这个activity，背景透明
 */

public class NotificationNewTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_new_task);
    }
}
