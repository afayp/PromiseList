package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.pfh.promiselist.R;
import com.pfh.promiselist.utils.DensityUtil;

/**
 * 展示项目或者task参与的人员，点击可添加删除
 */

public class MemberList extends LinearLayout {

    private LayoutParams defaultParams;

    public MemberList(Context context) {
        this(context,null);
    }

    public MemberList(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MemberList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultParams = new LayoutParams(DensityUtil.dp2px(context,30), DensityUtil.dp2px(context,30));
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);
        CircleImageView addMore = new CircleImageView(context);
        addMore.setImageResource(R.drawable.ic_plus);
        addView(addMore,defaultParams);
    }

    public void setMembers(){

    }



}
