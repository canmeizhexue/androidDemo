package com.canmeizhexue.common.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**管理所有页面,清除通知，退出程序
 * Created by canmeizhexue on 2016-8-8.
 */
public class LocalAppManager {
    private static LocalAppManager sInstance = new LocalAppManager();
    private List<Activity> activities = new ArrayList<>(16);
    private LocalAppManager(){}
    public static void add(Activity activity){
        if(activity!=null && !sInstance.activities.contains(activity)){
            sInstance.activities.add(activity);
        }
    }

    /**
     *
     * @param activity
     */
    public static void remove(Activity activity){
        if(activity!=null){
            sInstance.activities.remove(activity);
        }
    }

    /**
     * 退出程序
     * @param context 上下文
     * @param killCompletelly 是否完全杀死程序
     */
    public static void quitApp(Context context,boolean killCompletelly){
        removeAllActivities();
        if(killCompletelly){
            cancelAllNotification(context);
            System.exit(0);
        }
    }
    private static void removeAllActivities(){
        List<Activity>activities = sInstance.activities;
        int size = activities.size();
        for(int i=0;i<size;i++){
            Activity activity = activities.get(i);
            if(activity!=null){
                activity.finish();
            }
        }
        sInstance.activities.clear();
    }
    private static void cancelAllNotification(Context context){
        if(context!=null){
            //清除所有通知
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }
    }

}
