package com.pfh.promiselist.view.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pfh.promiselist.event.MsgEvent;
import com.pfh.promiselist.utils.ColorsUtil;
import com.pfh.promiselist.utils.ScreenUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.Realm;

/**
 * 基类activity
 */

public class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected Toast mToast;
    protected Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mRealm = Realm.getDefaultInstance();

    }

    public  void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup decorViewGroup = (ViewGroup)getWindow().getDecorView();
            //获取自己布局的根视图
            View rootView = ((ViewGroup) (decorViewGroup.findViewById(android.R.id.content))).getChildAt(0);
            //预留状态栏位置
            rootView.setFitsSystemWindows(true);

            //添加状态栏高度的视图布局，并填充颜色
            View statusBarTintView = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    getInternalDimensionSize(getResources(), "status_bar_height"));
            params.gravity = Gravity.TOP;
            statusBarTintView.setLayoutParams(params);
            statusBarTintView.setBackgroundColor(color);
            decorViewGroup.addView(statusBarTintView);
        }
    }

    public int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected void initStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //状态栏高度
            int statusHeight = ScreenUtil.getStatusHeight(mContext);

            ViewGroup decorViewGroup = (ViewGroup)getWindow().getDecorView();
            //获取自己布局的根视图
            View rootView = ((ViewGroup) (decorViewGroup.findViewById(android.R.id.content))).getChildAt(0);
            //主动加上状态栏高度的paddingLeft
            rootView.setPadding(rootView.getPaddingLeft(),rootView.getPaddingTop()+statusHeight
                    ,rootView.getPaddingRight(),rootView.getPaddingBottom());

            if (Build.VERSION.SDK_INT >= 21){
                getWindow().setStatusBarColor(ColorsUtil.SKYBLUE_DARK_TRANSLUCENT);
            }
        }
    }






    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.removeAllChangeListeners();
        mRealm.close();
    }

    /**
     * 注册一个默认的事件防止crash
     * @param msgEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDefalultMsgEvent(MsgEvent msgEvent){
    }

    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if(mToast == null){
                mToast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
            }else{
                mToast.setText(text);
            }
            mToast.show();
        }
    }
}
