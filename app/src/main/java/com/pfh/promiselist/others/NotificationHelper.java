package com.pfh.promiselist.others;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.pfh.promiselist.R;
import com.pfh.promiselist.model.TaskInfo;
import com.pfh.promiselist.receiver.NotificationClickNextReceiver;
import com.pfh.promiselist.view.activity.MainActivity;
import com.pfh.promiselist.view.activity.NotificationNewTaskActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

public class NotificationHelper {

    public static Notification createServiceNotification(Context context,ArrayList<TaskInfo> taskInfos,int index) {
        Intent jumpIntent = new Intent(context, MainActivity.class);
        jumpIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, jumpIntent, 0);// 点击打开主界面
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder
//                .setContentTitle(title)
//                .setContentText(content)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(contentIntent);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_service);
        remoteViews.setImageViewResource(R.id.iv_icon, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.tv_name, taskInfos.get(index).getName());
        remoteViews.setTextViewText(R.id.tv_desc, taskInfos.get(index).getDueTime() + "");

        Intent nextIntent = new Intent(context, NotificationClickNextReceiver.class);
        nextIntent.putExtra("task_info_list", taskInfos);
//        nextIntent.putExtra("index",index);//当前是哪条
        PendingIntent nextPi = PendingIntent.getBroadcast(context, 1, nextIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_next, nextPi);

        Intent newIntent = new Intent(context, NotificationNewTaskActivity.class);
        PendingIntent newPi = PendingIntent.getActivity(context, 2, newIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_plus, newPi);

        builder.setContent(remoteViews);
        return builder.build();
    }
}
