package com.pfh.promiselist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pfh.promiselist.model.TaskInfo;

import java.util.ArrayList;

/**
 * 当店家notification上的next时，发出一条广播，这里收到后更新notification下一条
 */

public class NotificationClickNextReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ArrayList<TaskInfo> taskInfos = (ArrayList<TaskInfo>) intent.getSerializableExtra("task_info_list");

    }
}
