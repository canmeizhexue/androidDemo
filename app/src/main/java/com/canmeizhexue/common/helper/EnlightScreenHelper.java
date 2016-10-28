package com.canmeizhexue.common.helper;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

/**
 * <p>EnlightScreenHelper类 概述，提供亮屏和解锁功能</p>
 *需要权限    <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
 <uses-permission android:name="android.permission.WAKE_LOCK"/>
 * @author silence
 * @version 1.0 (2016-1-18)
 */
public class EnlightScreenHelper {
    private Context context;

    private PowerManager.WakeLock wakeLock;
    private KeyguardManager.KeyguardLock keyguardLock;

    /**
     * 构造函数
     * @param context 上下文
     */
    public EnlightScreenHelper(Context context) {
        this.context = context;
    }

    /**
     * 获取锁
     */
    public void acquireLock(){
        try {
            if(wakeLock==null){
                PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,getClass().getCanonicalName());
                wakeLock.acquire();
            }
            if(keyguardLock==null){
                KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                keyguardLock = keyguardManager.newKeyguardLock(getClass().getCanonicalName());
                keyguardLock.disableKeyguard();

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 释放锁
     */
    public void releaseLock(){
        try {
            if(wakeLock!=null && wakeLock.isHeld()){
                wakeLock.release();
            }
            wakeLock=null;
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if(keyguardLock!=null){
                keyguardLock.reenableKeyguard();
            }
            keyguardLock=null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 页面销毁
     */
    public void onActivityDestroyed(){
        releaseLock();
        context = null;
    }
}
