package com.canmeizhexue.common.manager;

import android.os.Environment;
import android.text.TextUtils;

import com.canmeizhexue.common.utils.SDCardUtils;

import java.io.File;

/**
 * <p>SD卡文件夹管理类</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2015-10-19)
 */
public class FolderManager {
    /**
     * 初始化文件系统 , 文件夹不存在  则创建该文件夹
     * @param companyName 公司名称 比如canmeizhexue
     */
    public static void initSystemFolder(String companyName) {
        if(!TextUtils.isEmpty(companyName)){
            SDCardUtils.COMPANY_NAME="/"+companyName+"/";
        }
        boolean isSDCardAvailable = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (!isSDCardAvailable) {
            // 存储卡不可用，返回
            return;
        }
        checkFolder(SDCardUtils.ROOT_FOLDER);
        checkFolder(SDCardUtils.CACHE_FOLDER);
        checkFolder(SDCardUtils.IMAGE_CACHE_FOLDER);
        checkFolder(SDCardUtils.OTHER_CACHE_FOLDER);
        checkFolder(SDCardUtils.APP_FOLDER);
        checkFolder(SDCardUtils.LOG_FOLDER);
        checkFolder(SDCardUtils.CONFIG_FOLDER);
    }

    /**
     * 检查文件夹
     *
     * @param folder 文件夹名称
     */
    private static void checkFolder(String folder) {
        File temp = new File(folder);
        if (!temp.exists()) {
            temp.mkdirs();
        }
    }

}

