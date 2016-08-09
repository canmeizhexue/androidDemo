package com.canmeizhexue.common.entity;

/**
 * <p>DemoModel </p>
 *
 * @author canmeizhexue
 * @version 1.0 (2015/10/19)
 */
public class DemoModel {
    /**
     * 名字
     */
    public String name;
    /**
     * 跳转界面的action
     */
    public Class clazz;

    /**
     * 构造方法
     *
     * @param name  名字
     * @param clazz 跳转的Activity的class
     */
    public DemoModel(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }
}
