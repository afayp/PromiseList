package com.pfh.promiselist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class NavigationItem extends LinearLayout {
    public NavigationItem(Context context) {
        this(context,null);
    }

    public NavigationItem(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NavigationItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavigationItem, defStyleAttr, 0);
        int iconId = typedArray.getResourceId(R.styleable.NavigationItem_navigationIcon, R.drawable.ic_menu_black);
        String name = typedArray.getString(R.styleable.NavigationItem_navigationName);
        boolean showExpand = typedArray.getBoolean(R.styleable.NavigationItem_navigationExpand,false);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_navigation_item, this, true);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        ImageView iv_expand = (ImageView) view.findViewById(R.id.iv_expand);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);

        iv_icon.setImageResource(iconId);
        tv_name.setText(name);

    }
}
