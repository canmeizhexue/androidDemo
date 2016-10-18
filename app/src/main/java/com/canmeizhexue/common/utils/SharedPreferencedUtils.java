package com.canmeizhexue.common.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencedUtils {
    public   SharedPreferences mPreference;
    public SharedPreferencedUtils(Context context,String fileName){
        mPreference = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
    }
    public SharedPreferences getPreference(Context context){
        return mPreference;
    }

    public  void setInteger(Context context, String name, int value) {
        getPreference(context).edit().putInt(name, value).commit();
    }

    public  int getInteger(Context context, String name, int default_i) {
        return getPreference(context).getInt(name, default_i);
    }

    /**
     * 设置字符串类型的配置
     */
    public  void setString(Context context, String name, String value) {
        getPreference(context).edit().putString(name, value).commit();
    }

    public  String getString(Context context, String name) {
        return getPreference(context).getString(name, null);
    }

    /**
     * 获取字符串类型的配置
     */
    public  String getString(Context context, String name, String defalt) {
        return getPreference(context).getString(name, defalt);
    }

    /**
     * 获取boolean类型的配置
     *
     * @param context
     * @param name
     * @param defaultValue
     * @return
     */
    public  boolean getBoolean(Context context, String name,
                                     boolean defaultValue) {
        return getPreference(context).getBoolean(name, defaultValue);
    }

    /**
     * 设置boolean类型的配置
     *
     * @param context
     * @param name
     * @param value
     */
    public  void setBoolean(Context context, String name, boolean value) {
        getPreference(context).edit().putBoolean(name, value).commit();
    }

    public  void setFloat(Context context, String name, Float value) {
        getPreference(context).edit().putFloat(name, value).commit();
    }

    public  Float getFloat(Context context, String name, Float value) {
        return getPreference(context).getFloat(name, 0);
    }

    public  void setLong(Context context, String name, Long value) {
        getPreference(context).edit().putLong(name, value).commit();
    }

    public  Long getLong(Context context, String name, Long defaultValue) {
        return getPreference(context).getLong(name, defaultValue);
    }


}
