package com.lenovo.frame.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author feizai
 * @date 12/22/2020 022 11:35:56 AM
 */
public class NetworkUtil {

    public static int NET_CNNT_BAIDU_OK = 1; // NetworkAvailable
    public static int NET_CNNT_BAIDU_TIMEOUT = 2; // no NetworkAvailable
    public static int NET_NOT_PREPARE = 3; // Net no ready
    public static int NET_ERROR = 4; //net error
    private static int TIMEOUT = 3000; // TIMEOUT


    /**
     * check NetworkAvailable
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (null == manager)
            return false;
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (null == info || !info.isAvailable())
            return false;
        return true;
    }

    /**
     * 返回当前网络状态
     *
     * @param context
     * @return
     */
    public static int getNetState(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();
                if (networkinfo != null) {
                    if (networkinfo.isAvailable() && networkinfo.isConnected()) {
                        if (!ping(""))
                            return NET_CNNT_BAIDU_TIMEOUT;
                        else
                            return NET_CNNT_BAIDU_OK;
                    } else {
                        return NET_NOT_PREPARE;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NET_ERROR;
    }

    /**
     * check is5G
     * 检测是否5G网
     *
     * @param context
     * @return boolean
     */
    public static boolean is5G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_NR)) {
            return true;
        }
        return false;
    }

    /**
     * check is4G
     * 检测是否4G网
     *
     * @param context
     * @return boolean
     */
    public static boolean is4G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE)) {
            return true;
        }
        return false;
    }

    /**
     * check is3G
     * 检测是否3G网
     *
     * @param context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EHRPD
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_B
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPAP
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_HSUPA
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_TD_SCDMA)) {
            return true;
        }
        return false;
    }

    /**
     * is2G
     * 检测是否2G联网
     *
     * @param context
     * @return boolean
     */
    public static boolean is2G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_1xRTT
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_IDEN
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GSM
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)) {
            return true;
        }
        return false;
    }

    /**
     * isWifi
     * 检测是否WiFi联网
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
            return true;
        }
        return false;
    }

    /**
     * is wifi on
     * 检测WiFi是否打开
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null
                && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED)
                || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * ping "http://www.baidu.com"
     * ping操作
     *
     * @return
     */
    public static boolean ping(String url) {
        if (url == null || "".equals(url)) {
            url = "http://www.baidu.com";
        }else {
            if (!(url.contains("http://") || url.contains("https://"))){
                url = "http://www.baidu.com";
            }
        }
        boolean result = false;
        HttpURLConnection httpUrl = null;
        try {
            httpUrl = (HttpURLConnection) new URL(url).openConnection();
            httpUrl.setConnectTimeout(TIMEOUT);//3000ms超时
            httpUrl.connect();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
            httpUrl = null;
        }
        return result;
    }
}
