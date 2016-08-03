package com.canmeizhexue.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * <p>
 * 设备唯一标识UUID生成器
 * <br/>用于生成设备唯一标识,避免无法获取deviceId,androidId的问题
 * <p/>
 * @author canmeizhexue
 * @version 1.0 (2015-10-22)
 */
public class DeviceUtil {
    /**本地缓存文件*/
    private static final String PREFS_FILE = "device_uuid.xml";
    /**本地缓存文件中uuid对应key*/
    private static final String PREFS_DEVICE_UUID = "device_uuid";
    /**设备唯一标识*/
    private static String uuid;

    /**
     * 按照规则生成设备唯一标识
     *<br/>UUID格式形式例如:
     * 550E8400-E29B-11D4-A716-446655440000
     * @param context 上下文对象
     * @return uuid
     */
    public static synchronized String generateUUID(Context context) {
        if (TextUtils.isEmpty(uuid)) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
            String id = prefs.getString(PREFS_DEVICE_UUID, null);

            if (!TextUtils.isEmpty(id)) {
                // Use the ids previously computed and stored in the prefs file
                uuid = UUID.fromString(id).toString();
            } else {
                // Use the deviceIdv unless it's broken, in which case
                // fallback on Android ID,
                // unless it's not available, then fallback on a random
                // number which we store
                // to a prefs file

                try {
                    String deviceId =getIMEI(context);
                    if(!TextUtils.isEmpty(deviceId)) {
                        uuid = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString();
                    } else {
                        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        if(!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                        }
                    }
                } catch (Exception e) {
                    LogUtils.e("generateUUID exception", e.toString());
                }

                // 获取deviceId,androidId失败，则根据UUID算法随机生成UUID
                if(TextUtils.isEmpty(uuid)) {
                    uuid = UUID.randomUUID().toString();
                }

                // Write the value out to the prefs file
                prefs.edit().putString(PREFS_DEVICE_UUID, uuid).commit();
            }
        }
        return uuid;
    }

    /**
     * 获取手机串号(IMEI)
     *
     * @param ctx 上下文
     * @return 手机串号
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei == null) {
            return "";
        } else {
            return imei;
        }
    }

    /**
     * 获取用户识别码（IMSI）
     *
     * @param ctx 上下文
     * @return 用户识别码
     */
    public static String getSubscriberId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String tel = tm.getSubscriberId();
        return TextUtils.isEmpty(tel) ? "" : tel;
    }

    /**
     * 获取手机号码
     *
     * @param ctx 上下文
     * @return 手机号码
     */
    public static String getPhoneNumber(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    /**
     * 获取运营商
     * 其中46000、46002和46007标识中国移动，46001标识中国联通，46003标识中国电信
     *
     * @param ctx 上下文
     * @return 运营商
     */
    public static String getMNC(Context ctx) {
        String providersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            providersName = telephonyManager.getSimOperator();
            providersName = providersName == null ? "" : providersName;
        }
        return providersName;
    }
    private static String phoneModelCache;
    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static synchronized String getPhoneModel() {
        if(phoneModelCache==null){
            StringBuilder stringBuilder = new StringBuilder(32);
            String manufacturer = Build.MANUFACTURER;
            String brand=Build.BRAND,model=Build.MODEL;
            if(manufacturer!=null){
                //代表公司名称
                manufacturer = manufacturer.toLowerCase();
                stringBuilder.append(manufacturer).append(" ## ");
            }

            if(brand!=null){
                // 一般代表手机品牌，
                brand = brand.toLowerCase();
                if(stringBuilder.indexOf(brand)<0){
                    stringBuilder.append(brand).append(" ## ");
                }
            }
            if(model!=null){
                // 一般代表具体手机型号
                model=model.toLowerCase();
                if(stringBuilder.indexOf(model)<0){
                    stringBuilder.append(model).append(" ## ");
                }
            }
            phoneModelCache = stringBuilder.toString();
        }
        return phoneModelCache;
    }


    /**
     * 获取MAC地址
     *
     * @param ctx 上下文
     * @return MAC地址
     */
    public static String getWifiMacAddr(Context ctx) {
        String macAddr = "";
        try {
            WifiManager wifi = (WifiManager) ctx
                    .getSystemService(Context.WIFI_SERVICE);
            macAddr = wifi.getConnectionInfo().getMacAddress();
            if (macAddr == null) {
                macAddr = "";
            }
        } catch (Exception e) {
        }
        return macAddr;
    }
}
