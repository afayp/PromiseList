package com.pfh.promiselist.view.activity;

import android.os.Bundle;
import android.view.View;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.BubbleDrawer;
import com.pfh.promiselist.widget.FloatBubbleView;
import com.pfh.promiselist.widget.UserToolbar;


/**
 * 用户资料展示，数据统计
 */

public class UserActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        FloatBubbleView float_bubble = (FloatBubbleView) findViewById(R.id.float_bubble);
        //设置气泡绘制者
        BubbleDrawer bubbleDrawer = new BubbleDrawer(this);
        //设置渐变背景 如果不需要渐变 设置相同颜色即可
        bubbleDrawer.setBackgroundGradient(new int[]{0xffffffff, 0xffffffff});
        //给SurfaceView设置一个绘制者
        float_bubble.setBubbleDrawer(bubbleDrawer);

        UserToolbar toolbar = (UserToolbar) findViewById(R.id.toolbar);
        toolbar.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
