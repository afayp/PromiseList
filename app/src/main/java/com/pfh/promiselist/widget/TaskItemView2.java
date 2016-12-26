package com.pfh.promiselist.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.Constant;
import com.pfh.promiselist.utils.DateUtil;

/**
 * Created by Administrator on 2016/12/25 0025.
 */

public class TaskItemView2 extends LinearLayout {
    private Context mContext;
    private Task mTask;
    private int mOrderMode;
    private LinearLayout ll_root;
    private TextView tv_title;
    private TextView tv_info;
    private CardView cardview;
    private LinearLayout ll_setting;
    private int parentWidthMeasureSpec;
    private int parentHeightMeasureSpec;
    private boolean expand;
    private int animationTime = 150;
    private ImageView iv_palette;
    private ImageView iv_date;
    private ImageView iv_tag;
    private ImageView iv_bell;
    private ImageView iv_fixed;
    private ImageView iv_remark;

    public TaskItemView2(Context context) {
        this(context,null);
    }

    public TaskItemView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TaskItemView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_task_item_view_2, this, true);
        findViews(view);
    }

    private void findViews(View view) {
        cardview = (CardView) view.findViewById(R.id.cardview);
        ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_info = (TextView) view.findViewById(R.id.tv_info);
        ll_setting = (LinearLayout) view.findViewById(R.id.ll_setting);
        iv_palette = (ImageView) view.findViewById(R.id.iv_palette);
        iv_date = (ImageView) view.findViewById(R.id.iv_date);
        iv_tag = (ImageView) view.findViewById(R.id.iv_tag);
        iv_bell = (ImageView) view.findViewById(R.id.iv_bell);
        iv_fixed = (ImageView) view.findViewById(R.id.iv_fixed);
        iv_remark = (ImageView) view.findViewById(R.id.iv_remark);
    }

    public void setData(Task task,int orderMode){
        mTask = task;
        mOrderMode = orderMode;
        initView();
    }

    private void initView() {

        tv_title.setText(mTask.getName());
        if (mOrderMode == Constant.ORDER_BY_DATE){
            tv_info.setText("#"+mTask.getProject().getName() +" | " + getStrByTime(mTask.getDueTime()) );
        }else if (mOrderMode == Constant.ORDER_BY_PROJECT || mOrderMode == Constant.ORDER_BY_IMPORTANCE){
            String date = "";
            String time = "";
            if (DateUtil.isToday(mTask.getDueTime())){
                date = "今天";
            }else if (DateUtil.isTomorrow(mTask.getDueTime())){
                date = "明天";
            }else {
                date = DateUtil.timeStamp2Str(mTask.getDueTime(),"MM月dd日");
            }
            time = DateUtil.timeStamp2Str(mTask.getDueTime(),"HH:mm");
            tv_info.setText(date +" " +time);
        }
        //其他设置...
        cardview.setCardBackgroundColor(Color.parseColor(mTask.getBgColor().getValue()));

//        switch (mTask.getImportance()){
//            case 1:
//                cardview.setCardBackgroundColor(getResources().getColor(R.color.importance_low));
//                break;
//            case 2:
//                cardview.setCardBackgroundColor(getResources().getColor(R.color.importance_normal));
//                break;
//            case 3:
//                cardview.setCardBackgroundColor(getResources().getColor(R.color.importance_high));
//                break;
//        }

        setListener();
    }

    private void setListener() {
        iv_palette.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG","palette");
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidthMeasureSpec = widthMeasureSpec;
        parentHeightMeasureSpec = heightMeasureSpec;
    }

    public void toggleView(){
        expand = !expand;
        if (expand){
            expand(ll_setting);
        }else {
            collapse(ll_setting);
        }
    }

    public void toggleView(boolean status){
        if (status == expand){//如果要求的状态和目前状态一致，就不需要改变
            return;
        }
        expand = status;
        if (expand){
            expand(ll_setting);
        }else {
            collapse(ll_setting);
        }
    }


    private void expand(final View view){
        view.measure(parentWidthMeasureSpec,parentHeightMeasureSpec);
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        view.setVisibility(VISIBLE);

        ValueAnimator animator = ValueAnimator.ofInt(0, measuredHeight);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.setDuration(animationTime);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        animator.start();
    }

    private void collapse(final View view){
        int measuredHeight = view.getMeasuredHeight();
        ValueAnimator animator = ValueAnimator.ofInt(measuredHeight, 0);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(animationTime);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                if (height == 0){
                    view.setVisibility(GONE);
                }else {
                    view.getLayoutParams().height = height;
                    view.requestLayout();
                }
            }
        });
        animator.start();
    }

    private String getStrByTime(long time){
        if (DateUtil.isToday(time)){
            return "今天 "+DateUtil.timeStamp2Str(time,"HH:mm"); // 今天 10:00
        }else if (DateUtil.isTomorrow(time)){
            return "明天 "+DateUtil.timeStamp2Str(time,"HH:mm"); // 明天 10:00
        }else {
            return DateUtil.timeStamp2Str(time,"MM-dd HH:mm"); // 12-18 11:00
        }
    }
}
