package com.pfh.promiselist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.pfh.promiselist.R;
import com.pfh.promiselist.utils.DensityUtil;

/**
 * Created by Administrator on 2016/12/31 0031.
 */

public class CustomCheckBox extends CheckBox {

    private boolean halfChecked;//用于一部分tag选择的情况，中间画一条横线
    private Paint mPaint;

    public CustomCheckBox(Context context) {
        super(context);
        initPaint(context);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context){
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.grey));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(DensityUtil.dp2px(context,3));
    }

    public void setHalfChecked(boolean halfChecked){
        this.halfChecked = halfChecked;
        setChecked(false);
        invalidate();
    }


    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (checked){
            halfChecked = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (halfChecked){
            canvas.translate(getWidth()/2,getHeight()/2);
            canvas.drawLine(-getWidth()/2 +getPaddingLeft(),0,getWidth()/2-getPaddingRight(),0,mPaint);
        }

    }
}
