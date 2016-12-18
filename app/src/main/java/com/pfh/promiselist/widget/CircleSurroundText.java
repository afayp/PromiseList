package com.pfh.promiselist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private int spaceOut = 10;//圆外边的留白
    private int spaceIn = 2;//圆内边的留白
    private Paint paint;
    private int max;


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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        max = Math.max(getMeasuredWidth(), getMeasuredHeight());
//        radius = max/2;
        radius = (int) (max * Math.sqrt(2));
        setMeasuredDimension(radius,radius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(radius,radius,radius,paint);//画圆
        canvas.translate(radius - (max/2), radius - (max/2));//移回原来位置
        super.onDraw(canvas);
    }
}
