package com.pfh.promiselist.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Window;

import com.pfh.promiselist.R;
import com.pfh.promiselist.widget.ProjectListToolbar;

/**
 * 展示用户的所有项目(清单)
 */

public class ProjectListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
//            getWindow().setEnterTransition(new Slide());
//            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Explode());
//            getWindow().setExitTransition(new Slide());
//            getWindow().setExitTransition(new Fade());
        }
        setContentView(R.layout.activity_project_list);

        findViews();
    }

    private void findViews() {
        ProjectListToolbar toolbar = (ProjectListToolbar) findViewById(R.id.toolbar);
        FloatingActionButton fb_add = (FloatingActionButton) findViewById(R.id.fb_add);
        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }
}
