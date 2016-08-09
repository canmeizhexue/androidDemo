package com.canmeizhexue.common.utils;

import android.os.Environment;
import android.os.StatFs;
import com.canmeizhexue.common.base.BaseApplication;

import java.io.File;

/**
 * <p>SD卡工具类</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2015/10/19)
 */
public class SDCardUtils {
    /**
     * 以公司名为根目录文件夹的命名
     */
    public static  String COMPANY_NAME = "/canmeizhexue/";

    /**
     * SDCARD根目录
     */
    public static final String SDCARD_FOLDER = Environment
            .getExternalStorageDirectory().toString();

    /**
     * 包名
     */
    public static final String PACKAGE_NAME = "/" + BaseApplication.getApplication().
            getApplicationContext().getPackageName() + "/";

    /**
     * 应用目录
     */
    public static final String ROOT_FOLDER = SDCARD_FOLDER + COMPANY_NAME + PACKAGE_NAME;

    /**
     * 缓存目录
     */
    public static final String CACHE_FOLDER = ROOT_FOLDER + "cache/";

    /**
     * 压缩图片目录
     */
    public static final String IMAGE_FOLDER = ROOT_FOLDER + "image/";
    /**
     * 图片缓存可见目录
     */
    public static final String IMAGE_CACHE_IN_FOLDER = CACHE_FOLDER + "image/";

    /**
     * 图片缓存目录
     */
    public static final String IMAGE_CACHE_FOLDER = CACHE_FOLDER + ".image/";

    /**
     * 截图缓存目录
     */
    public static final String SCREENSHOT_IMAGE_CACHE_FOLDER = IMAGE_CACHE_FOLDER
            + ".screenshot/";

    /**
     * 用户头像路径
     */
    public static final String USER_HEAD_ICON = IMAGE_CACHE_FOLDER
            + "user_icon.png";

    /**
     * 其他临时文件缓存目录
     */
    public static final String OTHER_CACHE_FOLDER = CACHE_FOLDER + "other/";

    /**
     * 安装包目录
     */
    public static final String APP_FOLDER = ROOT_FOLDER + "apps/";

    /**
     * 欢迎页背景图
     */
    public static final String SPLASH_FOLDER = ROOT_FOLDER + "splash/";

    /**
     * 日志目录
     */
    public static final String LOG_FOLDER = ROOT_FOLDER + "log/";

    /**
     * 配置目录
     */
    public static final String CONFIG_FOLDER = ROOT_FOLDER + "config/";

    /**
     * 临时文件后缀名
     */
    public static final String TEMP_FILE_EXT_NAME = ".temp";

    /**
     * 判断是否存在SDCard
     *
     * @return true 有，false 没有
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    /**
     * SDCard总容量大小
     *
     * @return 字节
     */
    public static long getTotalExternalMemorySize() {
        long result = 0;
        if (hasSDCard()) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long totalBlocks = stat.getBlockCount();
                return totalBlocks * blockSize;

            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
        }
        return result;
    }

    /**
     * SDCard剩余大小
     *
     * @return 字节
     */
    public static long getAvailableExternalMemorySize() {
        long result = 0;
        if (hasSDCard()) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                return availableBlocks * blockSize;
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
        }
        return result;
    }

    /**
     * SDCARD是否有足够的空间
     *
     * @param minSize 最小值
     * @return
     */
    public static boolean hasEnoughSpace(long minSize) {
        return getAvailableExternalMemorySize() > minSize;
    }

    /**
     * 这个是手机内存的总空间大小
     *
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 这个是手机内存的可用空间大小
     *
     * @return 字节
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }


    /**
     * 创建路径
     *
     * @param path 路径
     * @return 成功:绝对路径,以分隔符结尾 失败:null
     */
    public static String createDirectory(String path) {

        String absolutePath = null;

        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File logFolder = new File(path);

            if (logFolder.exists()) {
                absolutePath = logFolder.getAbsolutePath() + File.separator;
            }

            if (logFolder.mkdir()) {
                absolutePath = logFolder.getAbsolutePath() + File.separator;
            }
        }

        return absolutePath;
    }

}
