package com.pfh.promiselist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pfh.promiselist.utils.ColorsUtil;


/**
 * 圆圈包围的文字
 */

public class CircleSurroundText extends TextView {

    private int circleColor = ColorsUtil.BLUE_DARK;
    private int circleWidth = 2;
    private int radius;
    private Paint paint;
    private Paint testpaint;
    private int max;
    private int textW;
    private int textH;


    public CircleSurroundText(Context context) {
        this(context,null);
    }

    public CircleSurroundText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleSurroundText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(circleColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleWidth);

        Rect rect = new Rect();
        paint.getTextBounds(getText().toString(), 0, getText().toString().length(), rect);
        textW = (int) paint.measureText(getText().toString());
//        textW = getTextWidth(paint,getText().toString());
        textH = rect.height();

        testpaint = new Paint();
        testpaint.setAntiAlias(true);
        testpaint.setColor(ColorsUtil.GRAY);
        testpaint.setStyle(Paint.Style.FILL);
        testpaint.setStrokeWidth(circleWidth);


    }

    private int getTextWidth(Paint paint, String str){
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        max = Math.max(getMeasuredWidth(), getMeasuredHeight());
//        radius = max/2;
        radius = (int) (max * Math.sqrt(2))/2;
        setMeasuredDimension(2*radius,2*radius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(radius,radius,radius-circleWidth - 2,paint);//画圆
//        canvas.drawRect(radius -textW/2, radius - textH/2,radius +textW/2,radius +textH/2,testpaint);
        canvas.translate(radius -textW/2, radius - textH/2);//移到中心
        super.onDraw(canvas);
    }
}
