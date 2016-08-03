package com.canmeizhexue.common.base;

import android.app.Application;

import com.canmeizhexue.common.utils.performance.AppBlockCanaryContext;
import com.github.moduth.blockcanary.BlockCanary;

/**Application
 * Created by canmeizhexue on 2016-8-3.
 */
public class BaseApplication extends Application{
    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        //TODO 初始化性能监控组件
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }
    public static BaseApplication getApplication(){
        return baseApplication;
    }
}
