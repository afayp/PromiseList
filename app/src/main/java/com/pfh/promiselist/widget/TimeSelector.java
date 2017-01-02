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

import com.afollestad.materialdialogs.MaterialDialog;
import com.pfh.promiselist.R;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.DensityUtil;

import java.util.Calendar;


/**
 * state1点击弹出date选择器，选完date,再弹出选择time。
 * state2点击弹出tiem选择器
 */

public class TimeSelector extends LinearLayout {

    private int HEIGHT_PX; // 75dp
    public static final int STATE_1 = 1; // 一级状态 选date
    public static final int STATE_2 = 2; // 二级状态 选time
    private int currState = STATE_1;
    private LinearLayout.LayoutParams defaultParams;
    private Calendar dueTimeC;// 记录选择的dueTime
    private Context mContext;
//    private SublimeListenerAdapter sublimeListener;
    private MaterialDialog dialog; // 用来放时间选择器

    private LinearLayout ll_1;
    private TextView tv_custom_date;
    private TextView tv_custom_time;


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
        mContext = context;
        setOrientation(VERTICAL);
        HEIGHT_PX = DensityUtil.dp2px(context,75);
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
        tv_custom_date = (TextView) view.findViewById(R.id.tv_custom_date);
        tv_custom_time = (TextView) view.findViewById(R.id.tv_custom_time);

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
                        currState = STATE_2;
                        dueTimeC.set(Calendar.DAY_OF_MONTH,Integer.parseInt(DateUtil.getCurDateStr("dd")));
                        break;
                    case R.id.ll_tomorrow:
                        startFlipAnim(true);
                        currState = STATE_2;
                        dueTimeC.set(Calendar.DAY_OF_MONTH,Integer.parseInt(DateUtil.getTomorrowDateStr("dd")));
                        break;
                    case R.id.ll_next_week:
                        startFlipAnim(true);
                        currState = STATE_2;
                        dueTimeC.set(Calendar.DAY_OF_MONTH,Integer.parseInt(DateUtil.getNextMondayStr("dd")));
                        break;
                    case R.id.ll_customize:
                        //弹出日期选择器
                        showSublimePicker();
                        break;
                    case R.id.ll_9:
                        dueTimeC.set(Calendar.HOUR_OF_DAY,9);
                        break;
                    case R.id.ll_15:
                        dueTimeC.set(Calendar.HOUR_OF_DAY,15);
                        break;
                    case R.id.ll_18:
                        dueTimeC.set(Calendar.HOUR_OF_DAY,18);
                        break;
                    case R.id.ll2_customize:
                        //弹出时间选择器
                        showSublimePicker();
                        break;
                }

            }
        };
        ll_today.setOnClickListener(defaultUpListener);
        ll_tomorrow.setOnClickListener(defaultUpListener);
        ll_next_week.setOnClickListener(defaultUpListener);
        ll_customize.setOnClickListener(defaultUpListener);
        ll_9.setOnClickListener(defaultUpListener);
        ll_15.setOnClickListener(defaultUpListener);
        ll_18.setOnClickListener(defaultUpListener);
        ll2_customize.setOnClickListener(defaultUpListener);

//        sublimeListener = new SublimeListenerAdapter() {
//            @Override
//            public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate,
//                                                int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
//
//                if (dialog != null){
//                    dialog.dismiss();
//                }
//
//                dueTimeC = selectedDate.getStartDate();//年月日
//                if (hourOfDay != -1 && minute != -1){
//                    dueTimeC.set(Calendar.HOUR_OF_DAY,hourOfDay);
//                    dueTimeC.set(Calendar.MINUTE,minute);
//                }
//                Log.e("TAG",DateUtil.date2Str(dueTimeC));
//
//                if (currState == STATE_1) {
//                    //setDate
//                    tv_custom_date.setText(dueTimeC.get(Calendar.DAY_OF_MONTH)+"");
//                    startFlipAnim(true);
//                    currState = STATE_2;
//                    showSublimePicker();
//                }else {
//                    tv_custom_time.setText(dueTimeC.get(Calendar.HOUR_OF_DAY) +":"+dueTimeC.get(Calendar.MINUTE));
//                }
//            }
//
//            @Override
//            public void onCancelled() {
//                if (dialog != null) dialog.dismiss();
//            }
//        };

    }

    private void showSublimePicker(){
//        SublimePicker sublimePicker = new SublimePicker(mContext);
//        sublimePicker.initializePicker(getSublimeOptions(),sublimeListener);
//        dialog = new MaterialDialog.Builder(mContext)
//                .customView(sublimePicker, false)
//                .show();
    }

//    private SublimeOptions getSublimeOptions(){
//        SublimeOptions options = new SublimeOptions();
//        int displayOptions = 0;
//
//        if (currState == STATE_1) {
//            displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
//            options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
//        }
//        if (currState == STATE_2) {
//            displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;//选择time也可以返回更改date
//            displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
//            options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
//            options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
//        }
//
//        options.setDisplayOptions(displayOptions);
//        options.setCanPickDateRange(false);
//        options.setAnimateLayoutChanges(true);
//        options.setDateParams(dueTimeC);
//        return options;
//    }

    private void startFlipAnim(boolean up){
        ValueAnimator animator = up ? ValueAnimator.ofInt(0, HEIGHT_PX) : ValueAnimator.ofInt(HEIGHT_PX,0);
        animator.setDuration(400);
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
        currState = up ? STATE_2 : STATE_1;
    }

    // 默认dueTime为now..
    private void initDefaultCalendar(){
        dueTimeC = Calendar.getInstance();
//        dueTimeC.set(Calendar.HOUR_OF_DAY,18);
    }

    public Calendar getSelectedCalendar(){
        return dueTimeC;
    }


}
