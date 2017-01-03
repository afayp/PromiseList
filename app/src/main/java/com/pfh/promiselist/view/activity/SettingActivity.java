package com.pfh.promiselist.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.widget.CircleImageView;
import com.pfh.promiselist.widget.SettingItemIconView;

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

        setting_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
