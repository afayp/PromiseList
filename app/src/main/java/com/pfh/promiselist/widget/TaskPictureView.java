package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.pfh.promiselist.R;

/**
 * 用于task详情页显示图片
 */

public class TaskPictureView extends RelativeLayout {

    private PhotoView photoView;
    private ImageView iv_delete;
    private Context mContext;
    private String picUrl;

    public TaskPictureView(Context context) {
        this(context,null);
    }

    public TaskPictureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TaskPictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_task_picture_view, this, true);
        photoView = (PhotoView) view.findViewById(R.id.photoView);
        iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        photoView.disenable();
    }

    public void setPicUrl(String picUrl){
        this.picUrl = picUrl;
        Glide.with(mContext)
                .load(picUrl)
                .into(photoView);
    }

    public PhotoView getPicView(){
        return photoView;
    }

    public ImageView getIvDelete(){
        return iv_delete;
    }

    public String getPicUrl(){
        return picUrl;
    }
}
