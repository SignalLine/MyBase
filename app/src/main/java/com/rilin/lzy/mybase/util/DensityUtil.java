package com.rilin.lzy.mybase.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class DensityUtil {

    //此方法需要保证WIFI在本次开机以来曾经是打开过的，否则会返回null。所以需要后台尝试先打开WIFI再获取
    //尝试打开wifi
    private static boolean tryOpenWIFI(WifiManager manager)
    {
        boolean softOpenWifi = false;
        int state = manager.getWifiState();
        if (state != WifiManager.WIFI_STATE_ENABLED && state != WifiManager.WIFI_STATE_ENABLING)
        {
            manager.setWifiEnabled(true);
            softOpenWifi = true;
        }
        return softOpenWifi;
    }

    //尝试关闭wifi
    private static void tryCloseWIFI(WifiManager manager)
    {
        manager.setWifiEnabled(false);
    }
    //但是如果WIFI在本次开机期间从来没有打开过，返回的MAC地址是不同的（非实际的）
    //尝试获取MAC地址
    private static String tryGetMAC(WifiManager manager)
    {
        WifiInfo wifiInfo = manager.getConnectionInfo();
        if (wifiInfo == null || TextUtils.isEmpty(wifiInfo.getMacAddress()))
        {
            return null;
        }
        String mac = wifiInfo.getMacAddress().replaceAll(":", "").trim().toUpperCase();
        //mac = formatIdentify(mac);
        return mac;
    }

    //尝试读取MAC地址
    private static String getMacFromDevice(Context context,int internal)
    {
        String mac=null;
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        mac = tryGetMAC(wifiManager);
        if(!TextUtils.isEmpty(mac))
        {
            return mac;
        }

        //获取失败，尝试打开wifi获取
        boolean isOkWifi = tryOpenWIFI(wifiManager);
        for(int index=0;index<internal;index++)
        {
            //如果第一次没有成功，第二次做100毫秒的延迟。
            if(index!=0)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            mac = tryGetMAC(wifiManager);
            if(!TextUtils.isEmpty(mac))
            {
                break;
            }
        }

        //尝试关闭wifi
        if(isOkWifi)
        {
            tryCloseWIFI(wifiManager);
        }
        return mac;
    }

    /**
     * 获取dialog宽度(屏幕宽-100px)
     */
    public static int getDialogW(Activity aty)
    {
        DisplayMetrics dm = new DisplayMetrics();
        dm = aty.getResources().getDisplayMetrics();
        int w = dm.widthPixels - 100;
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth() - 100;
        return w;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW(Activity aty)
    {
        DisplayMetrics dm = getScreenDM(aty);
        int w = dm.widthPixels;
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth();
        return w;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenH(Activity aty)
    {
        DisplayMetrics dm = getScreenDM(aty);

        int h = dm.heightPixels;
        // int h = aty.getWindowManager().getDefaultDisplay().getHeight();
        return h;
    }
    /**
     * 获取屏幕高宽
     * @param a
     * @return
     */
    public static String getScreenWH(Activity a)
    {
        DisplayMetrics metrics = getScreenDM(a);
        int w = metrics.widthPixels;
        int h =metrics.heightPixels;
        return String.valueOf(w) + "x" + String.valueOf(h);
    }


    /**
     * 获取DisplayMetrics
     *
     * @param aty
     * @return
     */
    public static DisplayMetrics getScreenDM(Activity aty)
    {
        DisplayMetrics dm = new DisplayMetrics();
        dm = aty.getResources().getDisplayMetrics();
        return dm;
    }


    /**
     * 获取状态栏高
     * @param a
     * @return
     */
    public static int getStatuBarH(Activity a)
    {
        Rect frame = new Rect();
        a.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }
  
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}  