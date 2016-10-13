package com.rilin.lzy.mybase.receiver;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import com.rilin.lzy.mybase.util.L;

/**
 * Created by lzy on 16/9/30.
 *
 * Hold a wakelock that can be acquired in the AlarmReceiver and
 * released in the AlarmAlert activity
 */
public class AlarmAlertWakeLock {
    private static PowerManager.WakeLock sCpuWakeLock;

    public static void acquireCpuWakeLock(Context context) {
        L.i("hu---AlarmAlertWakeLock------", "Acquiring cpu wake lock");
        if (sCpuWakeLock != null) {
            return;
        }

        PowerManager pm =(PowerManager) context.getSystemService(Context.POWER_SERVICE);

        sCpuWakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hu");
        sCpuWakeLock.acquire();
    }

    public static void releaseCpuLock() {
        Log.d("hu","Releasing cpu wake lock");
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }
}
