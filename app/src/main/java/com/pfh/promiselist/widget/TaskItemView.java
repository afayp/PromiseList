package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.Task;
import com.pfh.promiselist.utils.Constant;
import com.pfh.promiselist.utils.DateUtil;

/**
 * Created by Administrator on 2016/12/11.
 */

public class TaskItemView extends LinearLayout {

//    @BindView(R.id.ll_content)
    LinearLayout ll_content;
//    @BindView(R.id.iv_icon)
    ImageView iv_icon;
//    @BindView(R.id.tv_title)
    TextView tv_title;
//    @BindView(R.id.tv_info)
    TextView tv_info;
//    @BindView(R.id.view_importance1)
    View view_importance1;
//    @BindView(R.id.view_importance2)
    View view_importance2;

    private boolean expand;//是否展开状态
    private Context mContext;
    private Task mTask;
    private int mOrderMode;

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
//        ButterKnife.bind(this,view);
    }

    private void findViews(View view) {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_info = (TextView) view.findViewById(R.id.tv_info);
        ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
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
        }

        ll_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand();
            }
        });

    }



    public void expand(){
        expand = !expand;
        if (expand){
            Toast.makeText(mContext,expand+" 展开",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(mContext,expand+" 收起",Toast.LENGTH_LONG).show();
        }
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
