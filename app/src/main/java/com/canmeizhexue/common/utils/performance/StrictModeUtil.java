package com.canmeizhexue.common.utils.performance;

import android.os.Build;
import android.os.StrictMode;

/**严苛模式
 * Created by canmeizhexue on 2016-7-29.
 */
public class StrictModeUtil {
    private StrictModeUtil() {
        throw new UnsupportedOperationException("this class should not be instantiated");
    }

    /**
     *开启严苛模式
     */
    public static void enableStrictMode(){
        StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder();
        threadPolicyBuilder.detectNetwork().detectDiskWrites().detectDiskReads().detectCustomSlowCalls();
        threadPolicyBuilder.penaltyLog();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            threadPolicyBuilder.detectResourceMismatches();
        }
        StrictMode.setThreadPolicy(threadPolicyBuilder.build());

        StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
        vmPolicyBuilder.detectLeakedSqlLiteObjects().detectLeakedClosableObjects().detectActivityLeaks();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            vmPolicyBuilder.detectLeakedRegistrationObjects();
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
            vmPolicyBuilder.detectFileUriExposure();
        }
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
    }
}
