
package com.canmeizhexue.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Msg: 网络工具类
 * Update:  2015-11-09
 * Version: 1.0
 * Created by canmeizhexue on 2015-11-09 14:39.
 */
public final class NetWorkUtil {

    /**
     * Msg: 网络类型
     * Update:  2015-11-09
     * Version: 1.0
     * Created by laijiacai on 2015-11-09 14:39.
     */
    public static class NetworkType {

        /**
         * 无网络
         */
        public static final String UNKNOWN = "unknown";

        /**
         * 2G
         */
        public static final String NET_2G = "2G";
        /**
         * 3G
         */
        public static final String NET_3G = "3G";
        /**
         * wifi
         */
        public static final String WIFI = "wifi";
        /**
         * cmnet
         */
        public static final String NET_CMNET = "cmnet";

        /**
         * cmwap
         */
        public static final String NET_CMWAP = "cmwap";
    }

    /**
     * 当前是否有可用网络
     *
     * @param context 上下文
     * @return 当前是否有可用网络
     */
    public static boolean isNetworkAvailable(Context context) {
        return !(NetworkType.UNKNOWN.endsWith(getNetworkType(context)));
    }

    /**
     * 获取当前的网络类型
     *
     * @param context 上下文
     * @return 获取当前的网络类型
     */
    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        try {
            netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
        }
        if (netInfo == null) {
            return NetworkType.UNKNOWN;
        }
        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.WIFI;
        }
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int netType = tm.getNetworkType();

        // 已知3G类型
        // NETWORK_TYPE_UMTS 3
        // NETWORK_TYPE_EVDO_0 5
        // NETWORK_TYPE_EVDO_A 6
        // NETWORK_TYPE_HSDPA 8
        // NETWORK_TYPE_HSUPA 9
        // NETWORK_TYPE_HSPA 10
        // NETWORK_TYPE_EVDO_B 12
        // NETWORK_TYPE_LTE 13
        // NETWORK_TYPE_EHRPD 14
        // NETWORK_TYPE_HSPAP 15

        // 已知2G类型
        // NETWORK_TYPE_GPRS 1
        // NETWORK_TYPE_EDGE 2
        // NETWORK_TYPE_CDMA 4
        // NETWORK_TYPE_1xRTT 7
        // NETWORK_TYPE_IDEN 11

        if (netType == TelephonyManager.NETWORK_TYPE_GPRS
                || netType == TelephonyManager.NETWORK_TYPE_EDGE
                || netType == TelephonyManager.NETWORK_TYPE_CDMA
                || netType == TelephonyManager.NETWORK_TYPE_1xRTT || netType == 11) {
            return NetworkType.NET_2G;
        }
        return NetworkType.NET_3G;
    }

    /**
     * 是否cmwap
     *
     * @param context 上下文
     * @return 是否cmwap
     */
    public static boolean isCmwap(Context context) {
        String currentNetworkType = getNetworkType(context);

        if (NetworkType.NET_2G.equals(currentNetworkType)) {
            try {
                ConnectivityManager connectMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectMgr != null) {
                    NetworkInfo mobNetInfo = connectMgr
                            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mobNetInfo != null && mobNetInfo.isConnected()) {
                        if ("cmwap".equalsIgnoreCase(mobNetInfo.getExtraInfo())) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        return false;
    }

    /**
     * 网络状态枚举
     */
    public enum NetworkConnectedState {
        /**
         * wifi网络
         */
        WIFI,
        /**
         * 移动网络
         */
        MOBILE,
        /**
         * 断开
         */
        DISCONNECT,
        /**
         * 其他
         */
        OTHER
    }

    /**
     * 判断是否有网络
     *
     * @param context 上下文
     * @return true 有网络，false 没网络
     */
    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo.State mobile = NetworkInfo.State.UNKNOWN;
            if (mobileInfo != null) {
                mobile = mobileInfo.getState();
            }
            NetworkInfo.State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (mobile == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTED) {
                return true;
            }
            NetworkInfo networkInfoEthernet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
            if (networkInfoEthernet != null) {
                NetworkInfo.State defaultNetWork = networkInfoEthernet.getState();
                if (defaultNetWork == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 获取网络连接状态，是3g、wifi还是未连接
     *
     * @param context 上下文
     * @return NetworkConnectedState对象
     */
    public static NetworkConnectedState getNetworkState(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        // 未连接
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return NetworkConnectedState.DISCONNECT;
        }
        // wifi连接
        NetworkInfo.State state = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == NetworkInfo.State.CONNECTED) {
            return NetworkConnectedState.WIFI;
        }
        //mobile连接
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileInfo != null) {
            state = mobileInfo.getState();
            if (state == NetworkInfo.State.CONNECTED) {
                return NetworkConnectedState.MOBILE;
            }
        }
        return NetworkConnectedState.OTHER;
    }

}
