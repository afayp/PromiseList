package com.pfh.promiselist.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.TaskInfo;
import com.pfh.promiselist.view.activity.MainActivity;
import com.pfh.promiselist.view.activity.NotificationNewTaskActivity;

import java.util.ArrayList;

/**
 * 该service结合notification在通知栏显示信息
 */

public class NotificationService extends Service {

    private int index = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG","--- onStartCommand ---");
        ArrayList<TaskInfo> taskInfos = (ArrayList<TaskInfo>) intent.getSerializableExtra("task_info_list");
        startForeground(110,createNotification(taskInfos));
        index++;
        index = index % taskInfos.size();
        Log.e("TAG","index: "+index);
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * http://www.sixwolf.net/blog/2016/04/18/Android%E8%87%AA%E5%AE%9A%E4%B9%89Notification%E5%B9%B6%E6%B2%A1%E6%9C%89%E9%82%A3%E4%B9%88%E7%AE%80%E5%8D%95/
     * http://blog.csdn.net/vipzjyno1/article/details/25248021
     * http://blog.csdn.net/w804518214/article/details/51231946
     * http://www.jianshu.com/p/5505390503fa
     */
    public Notification createNotification(ArrayList<TaskInfo> taskInfos){
        Intent jumpIntent = new Intent(this, MainActivity.class);
        jumpIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,jumpIntent , 0);// 点击打开主界面
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("title")
                .setContentText("content")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(contentIntent);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_service);
        remoteViews.setImageViewResource(R.id.iv_icon,R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.tv_name,taskInfos.get(index).getName());
        remoteViews.setTextViewText(R.id.tv_desc,taskInfos.get(index).getDueTime()+"");

        Intent nextIntent = new Intent(this, NotificationService.class); // 点击next会重新start一遍这个service，onStartCommand会再执行一次
        nextIntent.putExtra("task_info_list",taskInfos);
        PendingIntent nextPi = PendingIntent.getService(this, 1, nextIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_next,nextPi);

        Intent newIntent = new Intent(this, NotificationNewTaskActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent newPi = PendingIntent.getActivity(this, 2, newIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_plus,newPi);

        builder.setContent(remoteViews);
        return builder.build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
