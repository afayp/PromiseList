package com.pfh.promiselist.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pfh.promiselist.R;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class NavigationItem extends LinearLayout {

    private ImageView iv_expand;
    private TextView tv_count;

    private boolean expand;
    private ImageView iv_icon;
    private TextView tv_name;
    private boolean selected;
    private LinearLayout ll_root;

    public NavigationItem(Context context) {
        this(context,null);
    }

    public NavigationItem(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NavigationItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavigationItem, defStyleAttr, 0);
        int iconId = typedArray.getResourceId(R.styleable.NavigationItem_navigationIcon, R.drawable.ic_menu_grey);
        String name = typedArray.getString(R.styleable.NavigationItem_navigationName);
        boolean showExpand = typedArray.getBoolean(R.styleable.NavigationItem_navigationExpand,false); // 是否显示右边箭头

        View view = LayoutInflater.from(context).inflate(R.layout.layout_navigation_item, this, true);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        iv_expand = (ImageView) view.findViewById(R.id.iv_expand);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        ll_root = (LinearLayout) view.findViewById(R.id.ll_root);

        iv_icon.setImageResource(iconId);
        tv_name.setText(name);
        iv_expand.setVisibility(showExpand ? VISIBLE : GONE);
    }

    public void setCount(String count){
        iv_expand.setVisibility(GONE);
        tv_count.setVisibility(VISIBLE);
        tv_count.setText(count);
    }

    public void setExpand(boolean expand){
        this.expand = expand;
        iv_expand.setVisibility(VISIBLE);
        ObjectAnimator animator;
        if (expand) {
            animator = ObjectAnimator.ofFloat(iv_expand, "rotation", 0f, 180f);
        }else {
            animator = ObjectAnimator.ofFloat(iv_expand, "rotation", 180f, 0f);
        }
        iv_expand.setPivotX(iv_expand.getWidth()/2);
        iv_expand.setPivotY(iv_expand.getHeight()/2);
        animator.setDuration(200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    public boolean isExpand(){
        return expand;
    }

    public void setIcon(@DrawableRes int resId){
        iv_icon.setImageResource(resId);
    }

    public void setName(String name){
        tv_name.setText(name);
    }

    public void setSelected(boolean selected){
        this.selected = selected;
        ll_root.setBackgroundResource(selected ? R.color.nav_item_selected_bg : R.color.white);
    }


}
