package com.canmeizhexue.common.utils.performance;

import com.canmeizhexue.common.utils.LogUtils;

import java.lang.reflect.Method;

/**检测某个类是否已经加载
 * Created by canmeizhexue on 2016-8-31.
 */
public class ClassLoadCheckerUtil {
    /**
     * 检测某个类是否已经被classLoader加载了
     * @param classLoader classloader
     * @param className 类的全称
     */
    public static void checkLoaded(ClassLoader classLoader,String className){
        Method method;
        try {
            method = ClassLoader.class.getDeclaredMethod("findLoadedClass",String.class);
            method.setAccessible(true);
            Object cls= method.invoke(classLoader,className);
            if(cls!=null){
                LogUtils.d("silence","------invoke----"+cls);
            }else{
                LogUtils.d("silence","------invoke----null");
            }

//            Vector classes=(Vector) f.get(getClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
