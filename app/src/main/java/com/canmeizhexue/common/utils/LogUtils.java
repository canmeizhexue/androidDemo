package com.canmeizhexue.common.utils;

import android.util.Log;

import com.canmeizhexue.common.BuildConfig;

/**
 * 日志打印工具
 */
public class LogUtils {

    /**
     * 是否打印日志  调试过程中设置为 true , 发包的时候设置为false
     * <br/>应用层在application初始化
     */
    private static boolean enableLog = BuildConfig.DEBUG;

    /**
     * 默认TAG
     */
    private static final String TAG = "---LogUtils---";
    public static void init(boolean enableLog){
        LogUtils.enableLog = enableLog;
    }
    /**
     * d级别 默认TAG的log
     *
     * @param msg log内容
     */
    public static void d(String msg) {
        if (enableLog) {
            Log.d(TAG, msg);
        }
    }

    /**
     * i级别 默认TAG的log
     *
     * @param msg log内容
     */
    public static void i(String msg) {
        if (enableLog) {
            Log.i(TAG, msg);
        }
    }

    /**
     * v级别 默认TAG的log
     *
     * @param msg log内容
     */
    public static void v(String msg) {
        if (enableLog) {
            Log.v(TAG, msg);
        }
    }


    /**
     * e级别 默认TAG的log
     *
     * @param msg log内容
     */
    public static void e(String msg) {
        if (enableLog) {
            Log.e(TAG, msg);
        }
    }

    /**
     * d级别 自定义TAG的log
     *
     * @param tag 标志
     * @param msg log内容
     */
    public static void d(String tag, String msg) {
        if (enableLog) {
            Log.d(tag, msg);

        }
    }

    /**
     * i级别 自定义TAG的log
     *
     * @param tag 标志
     * @param msg log内容
     */
    public static void i(String tag, String msg) {
        if (enableLog) {
            Log.i(tag, msg);
        }
    }

    /**
     * v级别 自定义TAG的log
     *
     * @param tag 标志
     * @param msg log内容
     */
    public static void v(String tag, String msg) {
        if (enableLog) {
            Log.v(tag, msg);
        }
    }

    /**
     * e级别 自定义TAG的log
     *
     * @param tag 标志
     * @param msg log内容
     */
    public static void e(String tag, String msg) {
        if (enableLog) {
            Log.e(tag, msg);
        }
    }

    /**
     * 打印调用堆栈，用来分析代码的运行情况,比如printStackTrace(new Throwable())
     * @param throwable 异常信息
     */
    public static void printStackTrace(Throwable throwable){
        if(enableLog && throwable!=null){
            LogUtils.e(Log.getStackTraceString(throwable));

        }
    }

    /**
     * e级别  在控制台输出并写入SD卡log日志
     *
     * @param exc Exception
     */
/*    public static void we(Exception exc) {
      
        if (isDebug) {
            exc.printStackTrace();

            StringWriter sw = new StringWriter();
            exc.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String content = time + System.getProperty("line.separator") + str + System.getProperty("line.separator");
            FileOutputStream fos = null;

            try {
                File file = new File(SDCardUtils.LOG_FOLDER, "log.txt");
                fos = new FileOutputStream(file, true);
                fos.write(content.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fos) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    /**
     * v级别  在控制台输出并写入SD卡log日志
     *
     * @param info 显示信息
     */
/*    public static void wv(String info) {
        if (isDebug) {
            v(info);
            
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String content = time + System.getProperty("line.separator") + info + System.getProperty("line.separator");
            FileOutputStream fos = null;

            try {
                File file = new File(SDCardUtils.LOG_FOLDER, "log.txt");
                fos = new FileOutputStream(file, true);
                fos.write(content.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fos) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
}
