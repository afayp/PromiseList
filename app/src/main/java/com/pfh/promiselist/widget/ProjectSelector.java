package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建task时的项目选择器,暂时只显示name,
 */

public class ProjectSelector extends HorizontalScrollView {

    private List<Project> projectList = new ArrayList<>();
//    private List<View> itemList = new ArrayList<>();
    private Context mContext;
    private LinearLayout.LayoutParams defaultParams;
    private Project selectedProject;

    public ProjectSelector(Context context) {
        this(context,null);
    }

    public ProjectSelector(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProjectSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        defaultParams = new LinearLayout.LayoutParams(DensityUtil.dp2px(mContext,100),DensityUtil.dp2px(mContext,100));
    }

    private void initView() {
        removeAllViews();
        LinearLayout ll_root = new LinearLayout(mContext);
        ll_root.setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0; i < projectList.size(); i++) {
            View ll_item = inflater.inflate(R.layout.item_project_selector, null);
            ll_item.setId(i);
            TextView tv_name = (TextView) ll_item.findViewById(R.id.tv_name);
            tv_name.setText(projectList.get(i).getName());
            ll_item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"click "+projectList.get(v.getId()).getName(),Toast.LENGTH_SHORT).show();
                    selectedProject = projectList.get(v.getId());
                }
            });
//            itemList.add(ll_item);
            ll_root.addView(ll_item,defaultParams);
        }
        addView(ll_root);
    }


    public void setProjectList(List<Project> projectList){
        this.projectList = projectList;
        initView();
    }

    public Project getSelectedProject(){
        return selectedProject;
    }
}
