package com.canmeizhexue.common.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>基础BaseAdapter类 ，BaseAdapter二次包装类, 统一规范Activity并提供通用方法
 * <br/>1.所有BaseAdapter的基类，保证全部是该类的子类
 * <br/>2.复写公共方法，避免业务Adapter重复代码
 * </p>
 *
 * @author canmeizhexue
 * @version 1.0（2015-11-16）
 *
 * @param <T> 泛型实体
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    /**本类标识*/
    public static final String TAG = BaseAdapter.class.getSimpleName();
    protected List<T> list;//数据列表
    protected Context mContext;//上下文
    protected LayoutInflater layoutInflater;//布局填充器
    protected Handler handler;//Activity的Handler

    /**
     * BaseAdapter的构造函数，在这里初始化各项属性值
     *
     * @param context  上下文
     */
    public BaseAdapter(Context context) {
        this(context,null);
    }

    /**
     * BaseAdapter的构造函数，在这里初始化各项属性值
     *
     * @param context  上下文
     * @param list     数据列表
     */
    public BaseAdapter(Context context, List<T> list) {
        this.list = list == null ? (List<T>) new ArrayList<T>() : new ArrayList<>(list);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);//获取布局填充器实例
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置Handler的方法
     * @param handler 控制类
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /** Clear data list */
    public void clear() {
        clear(true);
    }

    /**
     * Clear data list
     * @param isSetChanged 是否立即刷新 true是，false否
     */
    public void clear(boolean isSetChanged) {
        list.clear();
        if (isSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 增加item节点
     * @param item 节点实体
     */
    public void add(T item) {
        add(item, true);
    }
    /**
     * 增加item节点
     * @param item 节点实体
     * @param isSetChanged 是否立即刷新 true是，false否
     */
    public void add(T item, boolean isSetChanged) {
        list.add(item);
        if (isSetChanged) {
            notifyDataSetChanged();
        }
    }
    /**
     * 增加多个item 节点
     * @param items 节点集
     */
    public void addAll(List<T> items) {
        addAll(items,true);
    }

    /**
     * 增加多个item 节点
     * @param items 节点集
     * @param isSetChanged 是否立即刷新 true是，false否
     */
    public void addAll(List<T> items, boolean isSetChanged) {
        list.addAll(items);
        if (isSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 替换节点
     * @param oldItem 旧节点
     * @param newItem 新节点
     */
    public void set(T oldItem, T newItem) {
        set(list.indexOf(oldItem), newItem);
    }

    /**
     * 更新节点
     * @param index 节点所在索引号
     * @param item 节点内容
     */
    public void set(int index, T item) {
        set(index, item, true);
    }

    /**
     * 更新节点
     * @param index 节点所在索引号
     * @param item 节点内容
     * @param isSetChanged 是否立即刷新 true是，false否
     */
    public void set(int index, T item, boolean isSetChanged) {
        list.set(index, item);
        if (isSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 移除某个节点
     * @param item 节点内容
     */
    public void remove(T item) {
        remove(item, true);
    }

    /**
     * 移除某个节点
     * @param item 节点内容
     * @param isSetChanged 是否立即刷新 true是，false否
     */
    public void remove(T item, boolean isSetChanged) {
        list.remove(item);
        if (isSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 移除某个节点
     * @param index 节点所在索引
     */
    public void remove(int index) {
        remove(index,true);
    }

    /**
     * 移除某个节点
     * @param index 节点所在索引
     * @param isSetChanged 是否立即刷新 true是，false否
     */
    public void remove(int index, boolean isSetChanged) {
        list.remove(index);
        if (isSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 替换全部节点
     * @param items 节点集
     */
    public void replaceAll(List<T> items) {
        replaceAll(items, true);
    }

    /**
     * 替换全部节点
     * @param items 节点集
     * @param isSetChanged 是否立即刷新 true是，false否
     */
    public void replaceAll(List<T> items, boolean isSetChanged) {
        list.clear();
        list.addAll(items);
        if (isSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 获取已有全部数据
     * @return
     */
    public List<T> getAllItem(){
        return list;
    }
}
