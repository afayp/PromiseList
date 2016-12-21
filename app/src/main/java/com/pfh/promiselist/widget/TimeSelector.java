package com.pfh.promiselist.widget;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.pfh.promiselist.R;
import com.pfh.promiselist.utils.DensityUtil;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class TimeSelector extends CardView {
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
        setRadius(DensityUtil.dp2px(context,10));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(DensityUtil.dp2px(context,5));
        }
        View view = LayoutInflater.from(context).inflate(R.layout.layout_time_selector,null);
        addView(view);
    }


}
