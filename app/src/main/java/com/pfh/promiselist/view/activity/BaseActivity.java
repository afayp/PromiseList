package com.pfh.promiselist.view.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.pfh.promiselist.R;
import com.pfh.promiselist.model.MsgEvent;
import com.pfh.promiselist.utils.ColorsUtil;
import com.pfh.promiselist.utils.ScreenUtil;
import com.pfh.promiselist.utils.SnackbarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * 基类activity
 */

public class BaseActivity extends AppCompatActivity {

    protected static final int CONFIRM_SNACKBAR_TYPE_ = 1;
    protected static final int WARNING_SNACKBAR_TYPE_ = 2;
    protected static final int CONFIRM_SNACKBAR_TYPE_COLOR = 0xff4caf50;
    protected static final int WARNING_SNACKBAR_TYPE_COLOR = 0xffffc107;
    protected static final int SHORT_SNACKBAR_DURATION = 3000;
    protected static final int LOGNG_SNACKBAR_DURATION = 5000;

    protected Context mContext;
    protected Toast mToast;
    protected Realm mRealm;
    protected Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mRealm = Realm.getDefaultInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            //getWindow().setEnterTransition(new Explode());
            getWindow().setEnterTransition(new Slide());
            //getWindow().setEnterTransition(new Fade());
            //getWindow().setExitTransition(new Explode());
            getWindow().setExitTransition(new Slide());
            //getWindow().setExitTransition(new Fade());
            StatusBarUtil.setColor(this, getResources().getColor(R.color.default_blue),0);
        }

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

    protected void exitActivity(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else {
            finish();
        }
    }

    protected void startActivity(Intent intent, Activity srcActivity){
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(srcActivity).toBundle());
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

    protected void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if(mToast == null){
                mToast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
            }else{
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    protected void showConfrimSnackBar(View view, String message, String actionName, View.OnClickListener action){
        if (mSnackbar != null && mSnackbar.isShown()){
            mSnackbar.dismiss();
        }
        mSnackbar = SnackbarUtil.IndefiniteSnackbar(view,message,SHORT_SNACKBAR_DURATION, Color.WHITE,CONFIRM_SNACKBAR_TYPE_COLOR);
        mSnackbar.setAction(actionName,action);
        mSnackbar.show();
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    protected String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    protected String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }


}
