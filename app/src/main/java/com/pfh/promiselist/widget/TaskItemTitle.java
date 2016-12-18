package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/12/18.
 */

public class TaskItemTitle extends LinearLayout{
    public TaskItemTitle(Context context) {
        this(context,null);
    }

    public TaskItemTitle(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TaskItemTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
