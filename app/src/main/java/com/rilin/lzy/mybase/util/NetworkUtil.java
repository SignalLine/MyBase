package com.rilin.lzy.mybase.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author Mr.jianqiang.hu
 */
public final class NetworkUtil {

    private static final String TAG = NetworkInfo.class.getSimpleName();

    /**
     * 无网络
     */
    public static final int NETWORK_TYPE_NONE = -1;

    /**
     * WIFI
     */
    public static final int NETWORK_TYPE_WIFI = 1;

    /**
     * 移动运营商网络，包括：2G,3G,4G,5G...等移动网络
     */
    public static final int NETWORK_TYPE_MOBILE = 2;

    /**
     * GPRS
     */
    public static final int NETWORK_TYPE_2G = 3;

    /**
     * 2.5G
     */
    public static final int NETWORK_TYPE_25G = 4;

    /**
     * 3G
     */
    public static final int NETWORK_TYPE_3G = 5;
    /**
     * 4G
     */
    public static final int NETWORK_TYPE_4G = 6;
    /**
     * 未知网络-已链接
     */
    public static final int NETWORK_TYPE_UNKNOW = 888;

    /**
     * 是否已经开启网络链接
     *
     * @param context
     * @return
     */
    public final static boolean isConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo aliveInfo = connectivityManager.getActiveNetworkInfo();
            // 一下3种不是很严谨的判断，这种判断是判断，即使返回true，网络也可能处在不通畅状态，比如：连接还处在正在连接但还未连接上的状态

            // 1
            if (aliveInfo != null && aliveInfo.isConnected()) {
                return true;
            }

            // 2
            // if (aliveInfo != null) {
            // return aliveInfo.isAvailable();
            // }

            // 3
            // if (aliveInfo != null && aliveInfo.isConnectedOrConnecting()) {
            // return true;
            // }

            // 4---------更严谨的判断，这种判断是判断网络是否处在通畅的连接状态，但是一般来说没必要这么精确的结果
            // if (aliveInfo != null && aliveInfo.isConnected()) {
            //
            // if (aliveInfo.getState() == NetworkInfo.State.CONNECTED) {
            // return true;
            // }
            // }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Check if there is fast connectivity(判断是否为快速网络，WIFI和3G或者以上网络为快速网络)
     *
     * @param context
     * @return
     */
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

    /**
     * 当前网络状态是否已链接上WIFI
     *
     * @param context
     * @return
     */
    public final static boolean isWifi(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo aliveInfo = connectivityManager.getActiveNetworkInfo();
            if (aliveInfo != null && aliveInfo.isConnected()) {
                if (aliveInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "get newwork type Err:" + e);
        }
        return false;
    }

    /**
     * 当前网络是否已经链接上运营商网络
     *
     * @param context
     * @return
     */
    public final static boolean isMobile(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo aliveInfo = connectivityManager.getActiveNetworkInfo();
            if (aliveInfo != null && aliveInfo.isConnected()) {
                if (aliveInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "get newwork type Err:" + e);
        }
        return false;
    }

    /**
     * @param context
     * @return
     */
    public final static boolean is3G(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo aliveInfo = connectivityManager.getActiveNetworkInfo();
            if (aliveInfo != null && aliveInfo.isConnected()) {
                TelephonyManager manager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                int networkType = manager.getNetworkType();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case 12/* TelephonyManager.NETWORK_TYPE_EVDO_B */:
                    case 14/* TelephonyManager.NETWORK_TYPE_EHRPD */:
                    case 15/* TelephonyManager.NETWORK_TYPE_HSPAP */:
                        return true;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "get newwork type Err:" + e);
        }
        return false;
    }

    /**
     * @param context
     * @return
     */
    public final static boolean is2G(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo aliveInfo = connectivityManager.getActiveNetworkInfo();
            if (aliveInfo != null && aliveInfo.isConnected()) {
                TelephonyManager manager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                int networkType = manager.getNetworkType();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return true;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "get newwork type Err:" + e);
        }
        return false;
    }

    /**
     * 是否为2.5G网络
     *
     * @param context
     * @return
     */
    public final static boolean is25G(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo aliveInfo = connectivityManager.getActiveNetworkInfo();
            if (aliveInfo != null && aliveInfo.isConnected()) {
                TelephonyManager manager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                int networkType = manager.getNetworkType();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case 11/* TelephonyManager.NETWORK_TYPE_IDEN */:
                        return true;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "get newwork type Err:" + e);
        }
        return false;
    }

    /**
     * 是否为4G网络
     *
     * @param context
     * @return
     */
    public final static boolean is4G(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo aliveInfo = connectivityManager.getActiveNetworkInfo();
            if (aliveInfo != null && aliveInfo.isConnected()) {
                TelephonyManager manager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                int networkType = manager.getNetworkType();
                switch (networkType) {
                    case 13/* TelephonyManager.NETWORK_TYPE_LTE */:
                        return true;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "get newwork type Err:" + e);
        }
        return false;
    }

    /**
     * 根据网络类型获取网络类型名称。
     *
     * @param netType {@link #NETWORK_TYPE_NONE}对应“NONE”--无网络，
     *                {@link #NETWORK_TYPE_UNKNOW}对应“UNKNOW“--位置类型的网络，
     *                {@link #NETWORK_TYPE_WIFI}对应“WIFI”，{@link #NETWORK_TYPE_3G}
     *                对应“3G”，{@link #NETWORK_TYPE_MOBILE}对应“MOBILE”，
     *                {@link #NETWORK_TYPE_2G}对应“2G”
     * @return
     */
    public final static String getNetworkTypeName(int netType) {
        String result = "";
        switch (netType) {
            case NETWORK_TYPE_NONE:
                result = "NONE";
                break;
            case NETWORK_TYPE_WIFI:
                result = "WIFI";
                break;
            case NETWORK_TYPE_MOBILE:
                result = "MOBILE";
                break;
            case NETWORK_TYPE_2G:
                result = "2G";
                break;
            case NETWORK_TYPE_25G:
                result = "2.xG";
                break;
            case NETWORK_TYPE_3G:
                result = "3G";
                break;
            case NETWORK_TYPE_4G:
                result = "4G";
                break;
            default:
                result = "UNKNOW";
                break;
        }
        return result;
    }

    /**
     * 获取当前的网络状态码。 返回值包括：{@link #NETWORK_TYPE_NONE}对应“NONE”--无网络，
     * {@link #NETWORK_TYPE_UNKNOW}对应“UNKNOW“--位置类型的网络，
     * {@link #NETWORK_TYPE_WIFI} 对应“WIFI”，{@link #NETWORK_TYPE_3G} 对应“3G”，
     * {@link #NETWORK_TYPE_MOBILE} 对应“MOBILE”， {@link #NETWORK_TYPE_2G}对应“2G”
     *
     * @param context
     * @return -1 标识无网络 , 0 表示链接上WIFI , 1 标识链接上3G信号, 2 表示链接上GPRS
     */
    public final static int getNetworkType(Context context) {
        int netType = NETWORK_TYPE_NONE;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo aliveInfo = connectivityManager.getActiveNetworkInfo();
            if (aliveInfo != null && aliveInfo.isConnected()) {
                switch (aliveInfo.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                        netType = NETWORK_TYPE_WIFI;
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        TelephonyManager manager = (TelephonyManager) context
                                .getSystemService(Context.TELEPHONY_SERVICE);
                        int networkType = manager.getNetworkType();
                        switch (networkType) {
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                                netType = NETWORK_TYPE_2G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case 11/* TelephonyManager.NETWORK_TYPE_IDEN */:
                                netType = NETWORK_TYPE_25G;
                                break;
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case 12/* TelephonyManager.NETWORK_TYPE_EVDO_B */:
                            case 14/* TelephonyManager.NETWORK_TYPE_EHRPD */:
                            case 15/* TelephonyManager.NETWORK_TYPE_HSPAP */:
                                netType = NETWORK_TYPE_3G;
                                break;
                            case 13/* TelephonyManager.NETWORK_TYPE_LTE */:
                                netType = NETWORK_TYPE_4G;
                                break;
                            default:
                                netType = NETWORK_TYPE_UNKNOW;
                                break;
                        }
                        break;
                    default:
                        netType = NETWORK_TYPE_UNKNOW;
                        break;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "get newwork type Err:" + e);
        }
        return netType;
    }

    /**
     * 获取设备网络MAC地址
     *
     * @param context
     * @return
     */
    public final static String getMAC(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getMacAddress();
        } catch (Exception e) {
            Log.i(TAG, "get MAC Err:" + e);
        }
        return null;
    }

    /**
     * 获取设备当前连接网络的IP地址
     *
     * @return
     */
    public final static String getIP(Context context) {
        if (isWifi(context)) {
            return getWifiIp(context);
        } else {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                        .hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (Exception e) {
                Log.i(TAG, "Get IP Err:" + e);
            }
        }
        return null;
    }

    public final static String getWifiIp(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private final static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }
}
