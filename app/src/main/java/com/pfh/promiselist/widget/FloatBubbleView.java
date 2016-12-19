package com.pfh.promiselist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pfh.promiselist.model.BubbleDrawer;

/**
 * Created by Administrator on 2016/12/19 0019.
 */

public class FloatBubbleView extends SurfaceView implements SurfaceHolder.Callback ,Runnable {

    // SurfaceHolder
    private SurfaceHolder mHolder;
    // 用于绘图的Canvas
    private Canvas mCanvas;
    // 子线程标志位，控制子线程
    private boolean mIsDrawing;

    private BubbleDrawer mCurDrawer;    //当前绘制对象
    private int mHeight;
    private int mWidth;


    public FloatBubbleView(Context context) {
        super(context);
        initView();
    }

    public FloatBubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FloatBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //初始化一个SurfaceHolder对象
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.RGBA_8888);  //渐变效果 就是显示SurfaceView的时候从暗到明
//        setFocusable(true);
//        setFocusableInTouchMode(true);
//        this.setKeepScreenOn(true);
    }

    public void setBubbleDrawer(BubbleDrawer drawer){
        mCurDrawer = drawer;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void run() {

        long start = System.currentTimeMillis();
        //通过标志位循环来不停的绘制
        while (mIsDrawing) {
            draw();
        }
        long end = System.currentTimeMillis();
        //不用太过频繁的刷新，判断draw()方法的耗时来sleep线程
        if (end - start < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        try {
            //获得当前canvas的绘图对象,
            //这个canvas对象还是上次的canvas对象，就是上次的内容还在，可以先进行清屏
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            if (mCurDrawer != null){
                mCurDrawer.setViewSize(mWidth,mHeight);
                mCurDrawer.drawBgAndBubble(mCanvas,1f);
            }

        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                //对画板的内容进行提交。
                mHolder.unlockCanvasAndPost(mCanvas);
        }

    }


}
