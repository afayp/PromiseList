package com.pfh.promiselist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.pfh.promiselist.R;

/**
 * Created by Administrator on 2017/1/3 0003.
 */

public class SettingItemSwitchView extends RelativeLayout {

    private SwitchButton sb;

    public SettingItemSwitchView(Context context) {
        this(context,null);
    }

    public SettingItemSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemSwitchView, defStyleAttr, 0);
        String name = typedArray.getString(R.styleable.SettingItemSwitchView_settingItem_title);
        String desc = typedArray.getString(R.styleable.SettingItemSwitchView_settingItem_desc);
        boolean isShowSwitch = typedArray.getBoolean(R.styleable.SettingItemSwitchView_settingItem_switch_on,true);
        boolean showUnderline = typedArray.getBoolean(R.styleable.SettingItemSwitchView_settingItem_switch_underline,true);
        typedArray.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.layout_setting_item_switch_view, this, true);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_description = (TextView) view.findViewById(R.id.tv_description);
        sb = (SwitchButton) view.findViewById(R.id.sb);
        View underline = view.findViewById(R.id.underline);

        tv_name.setText(name);
        tv_description.setText(desc);
        sb.setVisibility(isShowSwitch ? VISIBLE : GONE);
        underline.setVisibility(showUnderline ? VISIBLE : GONE);

    }

    public boolean isSwitchOn(){
        return sb.isChecked();
    }

    public SwitchButton getSb(){
        return sb;
    }


}
