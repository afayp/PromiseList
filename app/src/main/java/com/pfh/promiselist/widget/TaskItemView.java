package com.pfh.promiselist.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.Tag;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.others.Constant;
import com.pfh.promiselist.utils.ColorsUtil;
import com.pfh.promiselist.utils.DateUtil;
import com.pfh.promiselist.utils.DensityUtil;

import io.realm.RealmList;

/**
 * Created by Administrator on 2016/12/25 0025.
 */

public class TaskItemView extends LinearLayout {
    private Context mContext;
    private Task mTask;
    private int mOrderMode;
    private int parentWidthMeasureSpec;
    private int parentHeightMeasureSpec;
    private boolean expand;
    private int animationTime = 150;
    private float selectedStrokeWidth = 1.5f; // 选中是边框宽度 dp
    private int taskCornerRadius = 2; //dp

    private TextView tv_title;
    private RelativeLayout rl_root;
    private FlowLayout flow_tag_container;
    private CardView cardview;
    private ImageView iv_fixed;

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
        cardview = (CardView) view.findViewById(R.id.cardview);
        rl_root = (RelativeLayout) view.findViewById(R.id.rl_root);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_fixed = (ImageView) view.findViewById(R.id.iv_fixed);
        flow_tag_container = (FlowLayout) view.findViewById(R.id.flow_tag_container);
        flow_tag_container.setHorizontalSpacing(8);
        flow_tag_container.setVerticalSpacing(5);
    }

    public void setData(Task task,int orderMode){
        mTask = task;
        mOrderMode = orderMode;
        initView();
    }

    private void initView() {
        flow_tag_container.removeAllViews();
        tv_title.setText(mTask.getName());

        //---日期----//
        TagView tag_date = new TagView(mContext);
        tag_date.setData(TagView.TIME_TYPE,getStrByTime(mTask.getDueTime()));
        //---项目名----//
        TagView tag_project = new TagView(mContext);
        tag_project.setData(TagView.PROJECT_TYPE,mTask.getProject().getName());

        if (mOrderMode == Constant.ORDER_BY_DATE){
            flow_tag_container.addView(tag_date);
            flow_tag_container.addView(tag_project);

        }else if (mOrderMode == Constant.ORDER_BY_PROJECT || mOrderMode == Constant.ORDER_BY_IMPORTANCE){
            flow_tag_container.addView(tag_date);
            flow_tag_container.addView(tag_project);
        }
        //---tags----//
        RealmList<Tag> tags = mTask.getTags();
        for (int i = 0; i < tags.size(); i++) {
            TagView tag_tag = new TagView(mContext);
            Tag tag = tags.get(i);
            tag_tag.setData(TagView.TAG_TYPE,tag.getName());
            flow_tag_container.addView(tag_tag);
        }
        //其他设置...
        if (mTask.getColorValue() != null) {
            cardview.setCardBackgroundColor(Color.parseColor(mTask.getColorValue()));
            rl_root.setBackground(createStateListDrawable(Color.parseColor(mTask.getColorValue())));
        }
        iv_fixed.setVisibility(mTask.isFixed() ? VISIBLE : GONE);
        setListener();
    }

    public void setSelectedBg(boolean select){
        if (select) {
            rl_root.setBackground(createSelectedDrawable(Color.parseColor(mTask.getColorValue())));
        }else {
            rl_root.setBackground(createStateListDrawable(Color.parseColor(mTask.getColorValue())));
        }
    }

    private StateListDrawable createStateListDrawable(int normalColor){
        GradientDrawable pressedShape = new GradientDrawable();
        pressedShape.setShape(GradientDrawable.RECTANGLE);
        pressedShape.setColor(ColorsUtil.colorBurn(normalColor));
        pressedShape.setCornerRadius(DensityUtil.dp2px(mContext,taskCornerRadius));
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-android.R.attr.state_pressed}, new ColorDrawable(normalColor));
        states.addState(new int[]{android.R.attr.state_pressed}, pressedShape);
        return states;
    }

    private GradientDrawable createSelectedDrawable(int normalColor){
        GradientDrawable selectedShape = new GradientDrawable();
        selectedShape.setShape(GradientDrawable.RECTANGLE);
        selectedShape.setColor(ColorsUtil.colorBurn(normalColor));
        selectedShape.setStroke(DensityUtil.dp2px(mContext, selectedStrokeWidth),getResources().getColor(R.color.task_selected_stroke_bg));
        return selectedShape;
    }

    private void setListener() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidthMeasureSpec = widthMeasureSpec;
        parentHeightMeasureSpec = heightMeasureSpec;
    }

//    public void toggleView(){
//        expand = !expand;
//        if (expand){
//            expand(ll_setting);
//        }else {
//            collapse(ll_setting);
//        }
//    }
//
//    public void toggleView(boolean status){
//        if (status == expand){//如果要求的状态和目前状态一致，就不需要改变
//            return;
//        }
//        expand = status;
//        if (expand){
//            expand(ll_setting);
//        }else {
//            collapse(ll_setting);
//        }
//    }
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
