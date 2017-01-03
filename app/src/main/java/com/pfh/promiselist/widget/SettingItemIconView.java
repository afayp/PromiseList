package com.pfh.promiselist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;

/**
 * 设置界面中带icon的item
 */

public class SettingItemIconView extends RelativeLayout {


    public SettingItemIconView(Context context) {
        this(context,null);
    }

    public SettingItemIconView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemIconView, defStyleAttr, 0);
        int iconResId = typedArray.getResourceId(R.styleable.SettingItemIconView_setting_item_icon, R.drawable.ic_setting_grey);
        String name = typedArray.getString(R.styleable.SettingItemIconView_setting_item_name);
        typedArray.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.layout_setting_item_icon_view, this, true);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);

        iv_icon.setImageResource(iconResId);
        tv_name.setText(name);
    }


}
