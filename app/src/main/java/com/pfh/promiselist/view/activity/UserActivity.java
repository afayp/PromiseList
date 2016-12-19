package com.pfh.promiselist.view.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;

/**
 * 用户资料展示，数据统计
 */

public class UserActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

//        FloatBubbleView float_bubble = (FloatBubbleView) findViewById(R.id.float_bubble);
//        //设置气泡绘制者
//        BubbleDrawer bubbleDrawer = new BubbleDrawer(this);
//        //设置渐变背景 如果不需要渐变 设置相同颜色即可
//        bubbleDrawer.setBackgroundGradient(new int[]{0xffffffff, 0xffffffff});
//        //给SurfaceView设置一个绘制者
//        float_bubble.setBubbleDrawer(bubbleDrawer);

        LinearLayout ll_content = (LinearLayout) findViewById(R.id.ll_content);
        for (int i = 0; i < 30; i++) {
            TextView textView = new TextView(mContext);
            textView.setText(i+"...");
            textView.setTextSize(24);
            ll_content.addView(textView);
        }
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.e("TAG","verticalOffset: "+verticalOffset);
    }


    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }
}
