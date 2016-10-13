package com.rilin.lzy.mybase.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.rilin.lzy.mybase.util.NetworkUtil;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lzy on 16/9/22.
 */
public class ReceiveNetMsgService extends Service {

    private static final String TAG = ReceiveNetMsgService.class.getSimpleName();

    public interface GetNetConnectState{
        //网络状态发生变化的时候,通过此接口实例通知当前网络的状态,此接口在activity中注入实例对象
        void getNetState(boolean isConnected);
    }

    private GetNetConnectState mNetConnectState;

    public void setNetConnectState(GetNetConnectState getNetConnectState){
        mNetConnectState = getNetConnectState;
    }

    private Binder mBinder = new MyBinder();
    private boolean isConnected;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                Timer timer = new Timer();
                //执行
                timer.schedule(new QunXTask(context),new Date());
            }
        }
    } ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册广播
        IntentFilter filter = new IntentFilter();
        //添加接收网络链接状态改变的action
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver,filter);
        Log.i(TAG,"------------------SERVICE---ONcreate");
    }

    //得到本地服务
    public class MyBinder extends Binder{
        public ReceiveNetMsgService getService(){
            return ReceiveNetMsgService.this;
        }
    }

    class QunXTask extends TimerTask{
        private Context mContext;

        public QunXTask(Context context){
            this.mContext = context;
        }

        @Override
        public void run() {
            if(NetworkUtil.isConnectedFast(mContext)){
                isConnected = true;
            }else {
                isConnected = false;
            }

            if (mNetConnectState != null) {
                //通知网络状态改变
                mNetConnectState.getNetState(isConnected);
                Log.i(TAG,"网络状态发生变化----->>>>" + isConnected);
            }
        }
    }

    //3G网络
    private boolean isNetworkConnected(Context context){
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //3G
            NetworkInfo mobNetInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobNetInfo != null) {
                return mobNetInfo.isAvailable();
            }
        }

        return false;
    }

    //wifi
    private boolean isWifiConnected(Context context){
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }

        return false;
    }

    public final static boolean isConnectedFast(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), tm.getNetworkType()));
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public final static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT: // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:// ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:// ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS: // ~ 100 kbps
                    return false;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:// ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A: // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA: // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:// ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:// ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:// ~ 400-7000 kbps
                    return true;

				/*
                 * 如果不换成数值，那就必须在编译的时候使用大于API
				 * 12的API版本，而是用数值就不要了，这样降低了API版本，是应用适用于更多设备和ANDROID系统。 Above API
				 * level 7, make sure to set android:targetSdkVersion to
				 * appropriate level to use these
				 */
                // API level 11
                case 14/* TelephonyManager.NETWORK_TYPE_EHRPD */: // ~ 1-2 Mbps
                    // API level 9
                case 12/* TelephonyManager.NETWORK_TYPE_EVDO_B */: // ~ 5 Mbps
                    // API level 13
                case 15/* TelephonyManager.NETWORK_TYPE_HSPAP */: // ~ 10-20 Mbps
                    // API level 11
                case 13/* TelephonyManager.NETWORK_TYPE_LTE */: // ~ 10+ Mbps
                    return true;
                // API level 8
                case 11/* TelephonyManager.NETWORK_TYPE_IDEN */:// ~25 kbps
                    return false;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:// Unknown
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //删除广播
        unregisterReceiver(mReceiver);
    }
}
