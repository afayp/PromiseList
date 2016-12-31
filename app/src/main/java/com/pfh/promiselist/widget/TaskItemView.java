package com.pfh.promiselist.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
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
import com.pfh.promiselist.utils.DateUtil;

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

    private TextView tv_title;
//    private LinearLayout ll_root;
    private RelativeLayout rl_root;
    private FlowLayout flow_tag_container;
    private CardView cardview;
    private ImageView iv_fixed;

//    private TextView tv_info;
//    private LinearLayout ll_setting;
//    private ImageView iv_palette;
//    private ImageView iv_date;
//    private ImageView iv_tag;
//    private ImageView iv_bell;
//    private ImageView iv_fixed;
//    private ImageView iv_remark;

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
        flow_tag_container.setHorizontalSpacing(5);

//        tv_info = (TextView) view.findViewById(R.id.tv_info);
//        ll_setting = (LinearLayout) view.findViewById(R.id.ll_setting);
//        iv_palette = (ImageView) view.findViewById(R.id.iv_palette);
//        iv_date = (ImageView) view.findViewById(R.id.iv_date);
//        iv_tag = (ImageView) view.findViewById(R.id.iv_tag);
//        iv_bell = (ImageView) view.findViewById(R.id.iv_bell);
//        iv_fixed = (ImageView) view.findViewById(R.id.iv_fixed);
//        iv_remark = (ImageView) view.findViewById(R.id.iv_remark);
//        ll_tag_container = (LinearLayout) view.findViewById(R.id.ll_tag_container);

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
        cardview.setCardBackgroundColor(Color.parseColor(mTask.getColorValue()));
        iv_fixed.setVisibility(mTask.isFixed() ? VISIBLE : GONE);
        setListener();
    }

    public void setSelectedBg(boolean select){
        if (select) {
            rl_root.setBackgroundResource(R.drawable.item_selected_bg);
        }else {
//            rl_root.setBackgroundColor(0);
            rl_root.setBackgroundResource(R.color.transparent);
        }
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
