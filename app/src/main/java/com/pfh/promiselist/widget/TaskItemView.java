package com.pfh.promiselist.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.ColorsUtil;
import com.pfh.promiselist.utils.Constant;
import com.pfh.promiselist.utils.DateUtil;

/**
 * 任务列表中的单条任务
 */

public class TaskItemView extends LinearLayout {

    private LinearLayout ll_content;
    private ImageView iv_icon;
    private TextView tv_title;
    private TextView tv_info;
    private View view_importance1;
    private View view_importance2;
    private LinearLayout ll_setting;

    private boolean expand;//是否展开状态
    private Context mContext;
    private Task mTask;
    private int mOrderMode;
    private int parentWidthMeasureSpec;
    private int parentHeightMeasureSpec;
    private int animationTime = 250;

    public TaskItemView(Context context) {
        this(context,null);
    }

    public TaskItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TaskItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_task_item_view, this, true);
        findViews(view);
    }

    private void findViews(View view) {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_info = (TextView) view.findViewById(R.id.tv_info);
        ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        view_importance1 = view.findViewById(R.id.view_importance1);
        view_importance2 = view.findViewById(R.id.view_importance2);
        ll_setting = (LinearLayout) view.findViewById(R.id.ll_setting);
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
        int color = ColorsUtil.SKYBLUE;
        if (mTask.getImportance() == 1){
            color = ColorsUtil.BLUE_LIGHT;
        }else if (mTask.getImportance() == 2){
            color = ColorsUtil.SKYBLUE;
        }else if (mTask.getImportance() == 3){
            color = ColorsUtil.RED;
        }
        view_importance1.setBackgroundColor(color);
        view_importance2.setBackgroundColor(color);
    }

    public void toggleView(){
        expand = !expand;
        if (expand){
            expand(ll_setting);
        }else {
            collapse(ll_setting);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidthMeasureSpec = widthMeasureSpec;
        parentHeightMeasureSpec = heightMeasureSpec;
    }

    private void expand(final View view){
        view.measure(parentWidthMeasureSpec,parentHeightMeasureSpec);
        final int measuredWidth = view.getMeasuredWidth();
        final int measuredHeight = view.getMeasuredHeight();
        view.setVisibility(VISIBLE);

        ValueAnimator animator = ValueAnimator.ofInt(0, measuredHeight);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
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
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealShow(View view){
        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealHide(final View view){
        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        // get the initial radius for the clipping circle
        int initialRadius = view.getWidth();

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        // start the animation
        anim.start();
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
