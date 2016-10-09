package com.canmeizhexue.common.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContextUtils {
	

	
	public static void hideSoftKeyboard(Activity activity) {
		try {
			View view = activity.getCurrentFocus();
			if (view != null) {
				InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
			}
		} catch (Throwable tr) {
			tr.printStackTrace();
		}
	}
	
	public static final boolean isLocationSettingOPen(Context context) {
		if (context != null) {
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
			boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
			boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (gps || network) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}
	
public static String resizePhoto(Context c,String originPhotoPath,int defaultPath){
		
		/**
		 * 压缩图片尺寸
		 */
		   int MAX_PICTURE_WIDTH = 725;
		   int MAX_PICTURE_HEIGHT = 825;
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = BitmapUtil.THUMBNAIL_FILE_PREFIX + System.currentTimeMillis() + "_";
		String filePath = BitmapUtil.getUnScanPictureStorePath(c,defaultPath);
		try {
			File thumbnailFile=new File(filePath+imageFileName+BitmapUtil.JPEG_FILE_SUFFIX);
			Bitmap bitmap = BitmapUtil.resizeBitmap(originPhotoPath, MAX_PICTURE_WIDTH,MAX_PICTURE_HEIGHT);
			if (bitmap!=null){
				BitmapUtil.saveBitmap(c, bitmap, thumbnailFile);
				bitmap.recycle();
			}
			if (thumbnailFile.exists()) {
				BitmapUtil.correctOrientation(originPhotoPath, thumbnailFile.getAbsolutePath());
				return thumbnailFile.getPath();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从最近30个任务栈中获取当前App最近的一个任务栈id,如果无最近任务栈，那么返回-1.
	 * @param context
	 * @return
	 */
	public static  int getCurrentAppTaskID(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(30);
		if (!tasks.isEmpty()) {
			for (RunningTaskInfo runningTaskInfo : tasks) {
				if (runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
					return runningTaskInfo.id;
				}	
			}
		}
		return -1;
	}
	
	/**
	 * 清除某个view的背景，一般使用在onDestory，可能会有异常，请自行捕捉
	 * 
	 * @param activity
	 * @param viewId
	 */
	public static void clearViewBackground(Activity activity, int viewId) throws Exception {
		View view = activity.findViewById(viewId);
		BitmapDrawable bd = (BitmapDrawable) view.getBackground();

		if (bd != null && bd.getBitmap() != null && !bd.getBitmap().isRecycled()) {
			view.setBackgroundDrawable(null);
			bd.setCallback(null);
			Bitmap bitmap = bd.getBitmap();
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
		
	}
	
	/**
	 * 获取系统相片的文件夹路径
	 * 
	 * @return 获取成功返回路径，失败返回Null
	 */
	public static String getCameraDir() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File cameraDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (cameraDir != null) {
				return cameraDir.getAbsolutePath() + File.separator;
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param isSpeak
	 *            true则是扬声器模式, false 则是听筒模式
	 */
	public static void switchSpeakMode(Context c, boolean isSpeak) {
		
		AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		if (isSpeak) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
		} else {
			if(Build.VERSION.SDK_INT>=11){
				audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
			}else{
				audioManager.setMode(AudioManager.MODE_IN_CALL);
			}
			audioManager.setSpeakerphoneOn(false);			
		}
	}
	public static void switchMusicMute(Context c,boolean isMute){
		AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamMute(AudioManager.STREAM_MUSIC, isMute);
	}
	
	
	/**
	 * 判断当前网络状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getNetWorkState(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 弹出系统短息编辑页面
	 */
	public static void showSystemSMSPage(Context context, String number, String message) {
		try {
			Uri uri = Uri.parse("smsto:" + number);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			intent.putExtra("sms_body", message);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static String getProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
	
	public static int getScreenWidthBydp(Activity context) {
		if (screenWidth == 0 || screenHeight == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenDensity = dm.density;
			screenHeight = dm.heightPixels;
			screenWidth = dm.widthPixels;
		}

		return ContextUtils.px2dp(context, screenWidth);
	}
	
	/**
	 * 显示没有网络的对话框
	 * 
	 * @param context
	 *            对话框的父activity
	 */
	public static void showNoneNetworkDialog(Context context,int unavailable_text,int webpage_fail_text) {
		if (context instanceof Activity) {
			Activity activity = (Activity) context;
			if (!activity.isFinishing()) {
				ContextUtils.showToast(context, unavailable_text);
				return;
			}
		}
		showToast(context, webpage_fail_text);
	}
	
	/**
	 * 显示Toast
	 * @param context 程序上下文
	 * @param resId 资源id
	 */
	public static void showToast(Context context,int resId) {
		Toast.makeText(context,resId , Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示Toast
	 * @param context 程序上下文
	 * @param text 字符串
	 */
	
	public static void showToast(Context context,String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	//判断当前有没有网络连接
	public static boolean getNetWorkSate(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();    
        if (networkinfo == null || !networkinfo.isAvailable()) {    
            return false;  
        }  
        return true;  
	}
	/*
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			Toast.makeText(context, "网络连接异常", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}*/
	
	//判断当前有没有网络连接
	public static boolean is2gConnected(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();    
        if (networkinfo == null || !networkinfo.isAvailable()) {    
            return false;  
        }  
        int type=networkinfo.getType();
        if(type==ConnectivityManager.TYPE_MOBILE){
        	int subType=networkinfo.getSubtype();
        	if (subType==TelephonyManager.NETWORK_TYPE_CDMA||
        			subType==TelephonyManager.NETWORK_TYPE_EDGE||
        			subType==TelephonyManager.NETWORK_TYPE_GPRS||
        					subType==TelephonyManager.NETWORK_TYPE_EVDO_0||
        					subType==TelephonyManager.NETWORK_TYPE_EVDO_A){
        		return true;
        	}
        }
        return false;  
	}
	public static String getVersionName(Context context){
		PackageManager pm = context.getPackageManager();   
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;  
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}   
		return null;
	}
	
	/**
	 * 获取MCC码
	 */
	public static String getMCC(Context c) {
		TelephonyManager telManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String network = telManager.getNetworkOperator();
		if (!TextUtils.isEmpty(network) && network.length() > 3) {
			return network.substring(0, 3);
		}
		return null;
	}

	/**
	 * 
	 * 字符串去掉特殊符号（用于兴趣爱好和话题显示）
	 * 
	 * @param value
	 * @return
	 */
	public static String parserString(String value) {
		String result = "";
		if (!TextUtils.isEmpty(value)) {
			String str = value.replace("##", "、");
			if (!TextUtils.isEmpty(str) && str.length() > 0) {
				result = str.substring(0, str.length() - 1);
			}
		}
		return result;
	}
	
	public static void hidesoftInput(Context context, View editText) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (editText != null) {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
	
	public static void hidesoftInput(Activity activity) {
		try{
			if (activity.getCurrentFocus()!=null){
			    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
			}
		}catch(Throwable tr){
			tr.printStackTrace();
		}
	}
	public static void toggleSoftInputMethod(Context context){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm!=null){
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

    public static boolean isAppRunAtBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> infos = activityManager.getRunningTasks(1);
        if (infos != null && infos.size() > 0) {
            RunningTaskInfo info = infos.get(0);
            ComponentName cn = info.topActivity;
            String name = cn.getClassName();
            //Log.d(TAG, " info.numActivities=" + info.numActivities);
            //Log.d(TAG, " info.numRunning=" + info.numRunning);
            //Log.d(TAG, " info topActivity= :" + info.topActivity.toString());
            //Log.d(TAG, " info baseActivity= :" + info.baseActivity.toString());

            if (name.indexOf("Launcher") >= 0) {
                return true;
            } else {
                try {
                    Class c = Class.forName(name);
                    return false;
                } catch (ClassNotFoundException e) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     *判断当前应用程序处于前台还是后台
     * 
     * @param context

    * @return    

    */
	public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

	}
	/**
     * 
     * @param context
     * @return
     */
	public static boolean isBackground(Context context) {
     

    	 ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
    		 	if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
    		 		//Log.i(TAG,String.format("Background App:", appProcess.processName));
    		 		return true;
    		 	}else{
    		 		//Log.i(String.format("Foreground App:", appProcess.processName));
    		 		return false;
    		 	}
			}
		}
		return false;
     }
	
	
	// 获取AppKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
        	return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
            	apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }
    
	/** 转换dip为px*/
	public static int dp2px(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/** 转换px为dip*/
	public static int px2dp(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}
	
	 /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param context
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }
	
	private volatile static int screenHeight=0;
	private volatile static int screenWidth=0;
	private volatile static float screenDensity=0;
	public static int getScreenHeight(Activity context){
		if(screenWidth==0||screenHeight==0){
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenDensity = dm.density;
			screenHeight = dm.heightPixels;
			screenWidth = dm.widthPixels;
		}
		return screenHeight;
	}
	public static int getScreenWidth(Activity context){
		if(screenWidth==0||screenHeight==0){
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenDensity = dm.density;
			screenHeight = dm.heightPixels;
			screenWidth = dm.widthPixels;
		}
		return screenWidth;
	}
	
	public static void openUrlInBrower(Context context , String url){
		Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }
	
	
	/**
	 * 检查程序是否已安装在手机上
	 * @param context
	 * @param packageName
	 * @return
	 */
	 public static boolean appInstalledOrNot(Context context,String packageName){
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try
        {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (NameNotFoundException e)
        {
            app_installed = false;
        }
        return app_installed ;
	 }
	 
	 /**
	  * 启动某个app
	  * @param context
	  * @param packageName
	  * @param className
	  */
	 public static void startApp(Context context,String packageName, String className){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);           
		ComponentName cn = new ComponentName(packageName, className);           
		intent.setComponent(cn);
		context.startActivity(intent);
	}
	 /**
	  * 是否没有sim卡
	  * true : 代表手机没有sim卡
	  * false : 代表手机可能有sim卡
	  * @param context
	  * @return
	  */
	 public static boolean isSIMStateAbsent(Context context){
		 TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		 int simState = mTelephonyManager.getSimState();
		 if(simState==TelephonyManager.SIM_STATE_ABSENT){
			 return true;
		 }
		 return false;
	 }
	 /**
	  * 
	  * @param v
	  * @return int[]数组  [0] 返回宽  [1]返回高,如果获取不到则返回 0,0
	  */
	 public static int [] getViewSize(View v){
		 
		 int result [] = new int[2];
		 if(v==null) return result;
		 result[0] = 0;
		 result[1] = 0;
		   int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
	        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
	        v.measure(w, h);
	        result[0] = v.getMeasuredWidth();
	        result[1] = v.getMeasuredHeight();
		 return result;
	 }
	 public static void test(){
		 
	 }
	 
	 
}
