package com.pfh.promiselist.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.widget.MemberList;
import com.pfh.promiselist.widget.SmoothSwitch;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * 新建项目(清单)
 */

public class NewProjectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        findViews();
    }

    private void findViews() {
        RelativeLayout rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        TextView tv_new_item = (TextView) findViewById(R.id.tv_new_item);
        ImageView iv_ok = (ImageView) findViewById(R.id.iv_ok);
        ImageView iv_delete = (ImageView) findViewById(R.id.iv_delete);
        MaterialEditText et_title = (MaterialEditText) findViewById(R.id.et_title);
        FloatingActionButton fb_add = (FloatingActionButton) findViewById(R.id.fb_add);
        MemberList memberList = (MemberList) findViewById(R.id.memberList);
        SmoothSwitch smoothSwitch = (SmoothSwitch) findViewById(R.id.smoothSwitch);
        ImageView iv_bg = (ImageView) findViewById(R.id.iv_bg);

        tv_new_item.setText(R.string.new_project);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProject();
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
                saveProject();
                exitActivity();
            }
        });
    }

    private void saveProject() {
    }

}
