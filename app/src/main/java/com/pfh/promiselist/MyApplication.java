package com.pfh.promiselist;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化leanCloud
        AVOSCloud.initialize(this,"xEJmoNg8Xf7qbrQQLFg4FwRi-gzGzoHsz","ipWtLc0KawfT3LWhY3LP5Xxc");
    }
}
