package com.canmeizhexue.common.helper;

import android.content.Context;
import android.os.Build;

import com.canmeizhexue.common.BuildConfig;
import com.canmeizhexue.common.utils.ContextUtils;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * <p>BuglyHelper类 Bugly配置类，Bugly产品页http://bugly.qq.com/</p>
 * https://bugly.qq.com/docs/user-guide/advance-features-android/?v=20160930152416
 *
 * @author canmeizhexue
 * @version 1.0 (2016/01/06 14:38)
 */
public class BuglyHelper {
    private static final String LOG_TAG = BuglyHelper.class.getSimpleName();

    /**=================场景标签相关(在Bugly产品页设置http://bugly.qq.com/manage?app=900017042&pid=1&ptag=1005-10004)=================*/
    /**支付模块id*/
    public static final int SCENE_TAG_ID_PAY = 8090;
    /**登录或注册id*/
    public static final int SCENE_TAG_ID_LOGIN_OR_REGISTER = 8089;


    /**
     * 初始化配置
     * @param appContext 上下文
     */
    public static boolean initConfig(Context appContext){
        if (!BuildConfig.IS_SHOW_BUSI_CODE) {
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(appContext);
            strategy.setAppChannel(ContextUtils.getUmengChannel(appContext));                  //设置渠道
            strategy.setAppVersion(BuildConfig.VERSION_NAME);               //App的版本
            strategy.setAppPackageName(BuildConfig.APPLICATION_ID);         //App的包名
            strategy.setAppReportDelay(60000);             //初始化延迟，启动60s后联网同步数据
            CrashReport.initCrashReport(appContext, "0acbfe93a1", BuildConfig.DEBUG, strategy);
            //开发模式下，关闭
            if (BuildConfig.DEBUG) {
                CrashReport.closeCrashReport();
                CrashReport.closeNativeReport();
            }
            //自定义环境信息
            CrashReport.putUserData(appContext, "clientType", "app");//客户端类型，分为：app/wechat/mobile/pc
            CrashReport.putUserData(appContext, "clientOS", "android");//客户端操作系统类别：ios android等
            CrashReport.putUserData(appContext, "clientOSVersion", Build.VERSION.RELEASE);//客户端操作系统的版本,4.4.2

            return true;
        }else{
            return false;
        }
    }

    /**
     * 根据不同crash场景设置对应属性
     * @param appContext 上下文
     * @param sceneTag 场景标签id
     * @return
     */
    public static void setPageConfig(Context appContext, int sceneTag) {
        //设置标签（用于标记场景），该标签为在Bugly产品页上设置的，上报后的Crash会显示该标签
        if (!BuildConfig.IS_SHOW_BUSI_CODE) {
            CrashReport.setUserSceneTag(appContext, sceneTag);
        }
    }

    /**
     * 设置后，Bugly异常日志用户id都将是这个userId
     * @param userId 用户Id
     * @return
     */
    public static void setUserId(String userId) {
        if (!BuildConfig.IS_SHOW_BUSI_CODE) {
            CrashReport.setUserId(userId);
        }
    }

    /**
     * 上报自己捕捉到的异常，这样app不崩溃，但是依然上报异常信息
     * @param throwable 异常信息
     */
    public static void reportCatchedException(Throwable throwable){
        try {
            if(throwable!=null){
                CrashReport.postCatchedException(throwable);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}

