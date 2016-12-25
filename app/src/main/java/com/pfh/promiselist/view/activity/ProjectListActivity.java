package com.pfh.promiselist.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.pfh.promiselist.R;
import com.pfh.promiselist.adapter.ProjectListAdapter;
import com.pfh.promiselist.dao.RealmDB;
import com.pfh.promiselist.model.Project;
import com.pfh.promiselist.widget.ProjectListToolbar;

import java.util.List;

/**
 * 展示用户的所有项目(清单)
 */

public class ProjectListActivity extends BaseActivity {

    private ProjectListToolbar toolbar;
    private FloatingActionButton fb_add;
    private RecyclerView recyclerview;

    private List<Project> projectList;
    private ProjectListAdapter projectListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        findViews();
        initViews();
        loadData();
    }

    private void findViews() {
        toolbar = (ProjectListToolbar) findViewById(R.id.toolbar);
        fb_add = (FloatingActionButton) findViewById(R.id.fb_add);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void initViews() {
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        projectListAdapter = new ProjectListAdapter(mContext, projectList,mRealm);
        recyclerview.setAdapter(projectListAdapter);

        toolbar.getAvatar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectListActivity.this,UserActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(ProjectListActivity.this,toolbar.getAvatar(),"avatar").toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });

        fb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectListActivity.this,NewProjectActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(ProjectListActivity.this,fb_add,"add").toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });
    }

    private void loadData() {
        projectList = RealmDB.getAllActiveProjectsByUserId(mRealm,RealmDB.getCurrentUserId());
        projectListAdapter.refreshData(projectList);
    }
}
