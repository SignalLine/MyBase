package com.rilin.lzy.mybase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dengyaming on 16/9/14.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"----------AlarmReceiver-------------");
        startVib(context);
    }

    private void startVib(Context context){
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100,400,100,400};//停,开,停,开
        vibrator.vibrate(pattern,-1);//震动一次
    }

}
