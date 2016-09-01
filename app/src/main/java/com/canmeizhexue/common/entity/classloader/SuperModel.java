package com.canmeizhexue.common.entity.classloader;

import com.canmeizhexue.common.utils.LogUtils;

/**
 * Created by zengyaping on 2016-9-1.
 */
public class SuperModel {
    static {
        LogUtils.d("silence","--------SuperModel--------init");
    }
    public static int superValue = 4;
    public static final int superConstantValue = 6;

    public static void superStaticMethod(){
        LogUtils.d("silence","--------SuperModel--------superStaticMethod");
    }
}
