package com.canmeizhexue.common.utils.performance;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.canmeizhexue.common.BuildConfig;
import com.canmeizhexue.common.base.BaseApplication;
import com.canmeizhexue.common.utils.DeviceUtil;
import com.canmeizhexue.common.utils.LogUtils;
import com.github.moduth.blockcanary.BlockCanaryContext;

import java.io.File;

/**
 * TODO 监控环境的上下文实现，仅供开发阶段用，发布即删除
 * <p/>
 * Created by canmeizhexue on 15/9/25.
 */
public class AppBlockCanaryContext extends BlockCanaryContext {
    /**
     * 当前类标识
     */
    private static final String LOG_TAG = AppBlockCanaryContext.class.getSimpleName();

    /**
     * 标示符，可以唯一标示该安装版本号，如版本+渠道名+编译平台
     *
     * @return apk唯一标示符
     */
    @Override
    public String getQualifier() {
        String qualifier = "";
        try {
            PackageInfo info = BaseApplication.getApplication().getPackageManager()
                    .getPackageInfo(BaseApplication.getApplication().getPackageName(), 0);
            qualifier += info.versionCode + "_" + info.versionName + "_YYB";
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(LOG_TAG, "getQualifier exception：" + e.getMessage());
        }
        LogUtils.i(LOG_TAG, "getQualifier info：" + qualifier);
        return qualifier;
    }

    @Override
    public String getUid() {
        return DeviceUtil.generateUUID(BaseApplication.getApplication())+"";
    }

    @Override
    public String getNetworkType() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) BaseApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            LogUtils.d(LOG_TAG, "当前网络名称：" + name);
            return name;
        } else {
            LogUtils.d(LOG_TAG, "没有可用网络");
            return "no network";
        }
    }

    @Override
    public int getConfigDuration() {
        return 9999;
    }

    @Override
    public int getConfigBlockThreshold() {
        return 500;
    }

    @Override
    public boolean isNeedDisplay() {
        return BuildConfig.DEBUG;
    }

    @Override
    public String getLogPath() {
        return "/blockcanary/performance";
    }

    @Override
    public boolean zipLogFile(File[] src, File dest) {
        return false;
    }

    @Override
    public void uploadLogFile(File zippedFile) {
       //上传日志文件
    }
}