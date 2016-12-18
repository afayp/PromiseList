package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pfh.promiselist.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/12/12.
 */

public class ProjectListToolbar extends LinearLayout {
    public ProjectListToolbar(Context context) {
        this(context,null);
    }

    public ProjectListToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProjectListToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_project_list_toolbar, this, true);
        CircleImageView iv_avatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
        ImageView iv_more = (ImageView) view.findViewById(R.id.iv_more);
    }
}
