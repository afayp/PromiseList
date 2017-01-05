package com.pfh.promiselist.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pfh.promiselist.model.TaskInfo;
import com.pfh.promiselist.utils.CloneUtils;

import java.util.ArrayList;

/**
 * 该service用来结合alarm manager按时提醒用户
 */

public class RemindService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG","RemindService---onCreate---");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG","RemindService---onStartCommand---");

        // 排序 最近的排前面
        ArrayList<TaskInfo> taskList = (ArrayList<TaskInfo>) intent.getSerializableExtra("task_info_list");
        boolean start = intent.getBooleanExtra("start",false);
        Log.e("TAG","taskList: "+taskList.toString());
        Log.e("TAG","taskList size: "+taskList.size());

        if (start) {
//            TaskInfo latest = taskList.get(0);
            TaskInfo latest = taskList.remove(0);
            Log.e("TAG","taskList size: "+taskList.size());
            // 用latest去桌面启动activity
            Log.e("TAG","latest: "+latest);
        }

        if (taskList.size() > 0) {
            ArrayList<TaskInfo> clone = CloneUtils.clone(taskList);
            startAlarm(clone);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void startAlarm(ArrayList<TaskInfo> taskList){
//        long time = taskList.get(0).getDueTime();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent toService = new Intent(this,RemindService.class);
        Log.e("TAG","taskList size: "+taskList.size());
        toService.putExtra("task_info_list",taskList);// 剩下的继续传下去
        toService.putExtra("start",true);// 剩下的继续传下去
        PendingIntent pi = PendingIntent.getService(this,0,toService,0); // 重新启动本服务
        // 下面仅在程序没有kill时有效， http://www.jianshu.com/p/1f919c6eeff6
        long now = SystemClock.elapsedRealtime();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            manager.setExact(AlarmManager.RTC_WAKEUP, time, pi);
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, now+10*1000, pi);
        }else {
//            manager.set(AlarmManager.RTC_WAKEUP, time, pi);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, now+10*1000, pi);
        }
    }

    public void cancelAlarm(){
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent toService = new Intent(this,RemindService.class);
        PendingIntent pi = PendingIntent.getService(this,0,toService,0);
        manager.cancel(pi);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelAlarm();
        Log.e("TAG","RemindService---onDestroy");
    }
}
