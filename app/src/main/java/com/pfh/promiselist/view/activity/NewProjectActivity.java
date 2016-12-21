package com.pfh.promiselist.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;

import com.pfh.promiselist.R;

/**
 * 新建项目(清单)
 */

public class NewProjectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_new_project);


    }
}
