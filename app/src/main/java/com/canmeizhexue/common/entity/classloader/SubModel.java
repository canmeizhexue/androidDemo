package com.canmeizhexue.common.entity.classloader;

import com.canmeizhexue.common.utils.LogUtils;

/**
 * Created by zengyaping on 2016-9-1.
 */
public class SubModel extends SuperModel{
    static {
        LogUtils.d("silence","--------SubModel--------init");
    }
    public static int subValue = 4;
    public static final int subConstantValue = 6;
    public static void subStaticMethod(){
        LogUtils.d("silence","--------SubModel--------subStaticMethod");
    }
}
