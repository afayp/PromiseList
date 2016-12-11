package com.pfh.promiselist;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化leanCloud
        AVOSCloud.initialize(this,"xEJmoNg8Xf7qbrQQLFg4FwRi-gzGzoHsz","ipWtLc0KawfT3LWhY3LP5Xxc");
        //初始化日志
        Logger.init("PromiseList");
        //初始化realm
        Realm.init(this);
        RealmConfiguration config = new  RealmConfiguration.Builder()
                .name("pl.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
