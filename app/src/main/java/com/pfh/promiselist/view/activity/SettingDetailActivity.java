package com.pfh.promiselist.view.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.pfh.promiselist.R;

/**
 * Created by Administrator on 2017/1/3 0003.
 */

public class SettingDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_detail);
        LinearLayout ll_content = (LinearLayout) findViewById(R.id.ll_content);

        // 判断是哪种设置 加载不同布局 并设置不同的事件
//        ll_content.addView();


    }
}
