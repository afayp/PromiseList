package com.pfh.promiselist.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.DensityUtil;

import java.util.Calendar;


/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class TimeSelector extends LinearLayout {

    private int HEIGHT_PX; // 85dp
    public static final int STATE_1 = 1; // 一级状态 选day
    public static final int STATE_2 = 2; // 二级状态 选time
    private int state = STATE_1;
    private LinearLayout.LayoutParams defaultParams;
    private LinearLayout ll_1;
    private Calendar selectedC;// 记录选择的dueTime


    public TimeSelector(Context context) {
        this(context,null);
    }

    public TimeSelector(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TimeSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        HEIGHT_PX = DensityUtil.dp2px(context,85);
        defaultParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,HEIGHT_PX);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_time_selector, null);
        addView(view,new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,HEIGHT_PX));
        initDefaultCalendar();

        LinearLayout ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
        ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
        LinearLayout ll_today = (LinearLayout) view.findViewById(R.id.ll_today);
        LinearLayout ll_tomorrow = (LinearLayout) view.findViewById(R.id.ll_tomorrow);
        LinearLayout ll_next_week = (LinearLayout) view.findViewById(R.id.ll_next_week);
        LinearLayout ll_customize = (LinearLayout) view.findViewById(R.id.ll_customize);

        LinearLayout ll_9 = (LinearLayout) view.findViewById(R.id.ll_9);
        LinearLayout ll_15 = (LinearLayout) view.findViewById(R.id.ll_15);
        LinearLayout ll_18 = (LinearLayout) view.findViewById(R.id.ll_18);
        LinearLayout ll2_customize = (LinearLayout) view.findViewById(R.id.ll2_customize);

        TextView tv_today_date = (TextView) findViewById(R.id.tv_today_date);
        TextView tv_tomorrow_date = (TextView) findViewById(R.id.tv_tomorrow_date);
        TextView tv_next_week_date = (TextView) findViewById(R.id.tv_next_week_date);
        TextView tv_custom_date = (TextView) findViewById(R.id.tv_custom_date);

        tv_today_date.setText(DateUtil.getCurDateStr("dd"));
        tv_tomorrow_date.setText(DateUtil.getTomorrowDateStr("dd"));
        tv_next_week_date.setText(DateUtil.getNextMondayStr("dd"));
        tv_custom_date.setText("/");

        OnClickListener defaultUpListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.ll_today:
                        startFlipAnim(true);
                        state = STATE_2;
                        selectedC.set(Calendar.DAY_OF_MONTH,Integer.parseInt(DateUtil.getCurDateStr("dd")));
                        break;
                    case R.id.ll_tomorrow:
                        startFlipAnim(true);
                        state = STATE_2;
                        selectedC.set(Calendar.DAY_OF_MONTH,Integer.parseInt(DateUtil.getTomorrowDateStr("dd")));
                        break;
                    case R.id.ll_next_week:
                        startFlipAnim(true);
                        state = STATE_2;
                        selectedC.set(Calendar.DAY_OF_MONTH,Integer.parseInt(DateUtil.getNextMondayStr("dd")));
                        break;
                    case R.id.ll_customize:
                        //弹出日期选择器
                        break;
                    case R.id.ll_9:
                        selectedC.set(Calendar.HOUR_OF_DAY,9);
                        break;
                    case R.id.ll_15:
                        selectedC.set(Calendar.HOUR_OF_DAY,15);
                        break;
                    case R.id.ll_18:
                        selectedC.set(Calendar.HOUR_OF_DAY,18);
                        break;
                    case R.id.ll2_customize:
                        //弹出时间选择器
                        break;
                }

            }
        };
        ll_today.setOnClickListener(defaultUpListener);
        ll_tomorrow.setOnClickListener(defaultUpListener);
        ll_next_week.setOnClickListener(defaultUpListener);

        ll_customize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出日期选择器
            }
        });
    }

    private void startFlipAnim(boolean up){
        ValueAnimator animator = up ? ValueAnimator.ofInt(0, HEIGHT_PX) : ValueAnimator.ofInt(HEIGHT_PX,0);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int marginTop = (int) animation.getAnimatedValue();
                defaultParams.setMargins(0,-marginTop,0,0);
                ll_1.setLayoutParams(defaultParams);
                ll_1.requestLayout();
          }
        });
        animator.start();
    }

    // 默认dueTime为今天6点
    private void initDefaultCalendar(){
        selectedC = Calendar.getInstance();
        selectedC.set(Calendar.HOUR_OF_DAY,18);
    }

    public Calendar getSelectedCalendar(){
        return selectedC;
    }
}
