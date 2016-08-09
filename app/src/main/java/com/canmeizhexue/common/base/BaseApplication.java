package com.canmeizhexue.common.base;

import android.app.Application;

import com.canmeizhexue.common.manager.FolderManager;
import com.canmeizhexue.common.utils.performance.AppBlockCanaryContext;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**Application
 * Created by canmeizhexue on 2016-8-3.
 */
public class BaseApplication extends Application{
    private static BaseApplication baseApplication;
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        //TODO 初始化性能监控组件,就算退出程序，这个还是会引起内存增长的，不用担心
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        // 使用leakCanary监控那些本该回收的对象, 会返回一个预定义的 RefWatcher，同时也会启用一个 ActivityRefWatcher，用于自动监控调用Activity.onDestroy() 之后泄露的 activity。
        refWatcher = LeakCanary.install(this);
        FolderManager.initSystemFolder("canmeizhexue");
    }
    public static BaseApplication getApplication(){
        return baseApplication;
    }

    /**
     * 监控某个对象是否被回收
     * @param object 被监控的对象
     */
    public void watch(Object object){
        if(object!=null && refWatcher!=null){
            refWatcher.watch(object);
        }
    }
}
