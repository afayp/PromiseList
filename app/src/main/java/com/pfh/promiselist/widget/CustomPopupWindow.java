package com.pfh.promiselist.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.PopupWindow;

import com.pfh.promiselist.utils.L;


/**
 * Created by Administrator on 2016/12/17.
 */

public class CustomPopupWindow {

    private Context mContext;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private int animationTime = 150;
    private int viewHeight;

    public CustomPopupWindow(Context context,View contentView) {
        this.mContext = context;
        this.mContentView = contentView;
        initView();
    }

    public CustomPopupWindow(Context context,@LayoutRes int layoutId) {
        this.mContext = context;
        this.mContentView = LayoutInflater.from(mContext).inflate(layoutId,null);
        initView();
    }

    private void initView() {
        mPopupWindow = new PopupWindow(mContext);
        mContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int viewWidth = mContentView.getMeasuredWidth();
        viewHeight = mContentView.getMeasuredHeight();
        L.e("mContentView.getHeight(): "+ viewHeight);
        mPopupWindow.setWidth(viewWidth);// ViewGroup.LayoutParams.WRAP_CONTENT
        mPopupWindow.setHeight(viewHeight);
        mContentView.setVisibility(View.INVISIBLE);// 方便设置动画
        mPopupWindow.setContentView(mContentView);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
    }

    public void showAsDropDown(View anchor){
        mPopupWindow.showAsDropDown(anchor);
        startAnim();
    }

    public void showAsDropDown(View anchor, int xoff, int yoff){
        mPopupWindow.showAsDropDown(anchor,xoff,yoff);
        startAnim();
    }

    public void showAtLocation(View parent, int gravity, int x, int y){
        mPopupWindow.showAtLocation(parent,gravity,x,y);
        startAnim();
    }

    public void dismiss(){
        mPopupWindow.dismiss();
    }

    private void startAnim(){
        mContentView.setVisibility(View.VISIBLE);
        ValueAnimator animator = ValueAnimator.ofInt(0, viewHeight);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(animationTime);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mContentView.getLayoutParams().height = (int) animation.getAnimatedValue();
                Log.e("tag","height: "+ (int) animation.getAnimatedValue());
                mContentView.requestLayout();
            }
        });
        animator.start();
    }

    public View getContentView(){
        return mContentView;
    }
}
