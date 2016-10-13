package com.rilin.lzy.mybase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rilin.lzy.mybase.service.TimerService;

/**
 * Created by lzy on 16/9/30.
 */
public class TimerReceiver extends BroadcastReceiver {

    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    public static final String ALARM_ACTION = "com.rilintech.hxk_301.service.AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ALARM_ACTION)) {

            Log.d("hu", "uuid===== " + intent.getIntExtra("uuid", -1));
            AlarmAlertWakeLock.acquireCpuWakeLock(context);
            //弹出Activity提示闹铃
            /*Intent intent = new Intent(context, ClockShowActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("uuid", data.getIntExtra("uuid", -1));
            context.startActivity(intent);*/

            //弹出系统级别的dialog提示闹铃
            Intent intent1 = new Intent(context, TimerService.class);
            intent1.setAction(TimerService.ALARM_SERVICE_ACTION);
            intent1.putExtra("uuid", intent.getIntExtra("uuid", -1));
            context.startService(intent1);

        }
    }
}
