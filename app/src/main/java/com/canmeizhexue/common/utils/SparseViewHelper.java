package com.canmeizhexue.common.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * <p>Adapter控件帮助类类 1、可直接获取ListView Adapter中的控件；</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2015/10/19)
 */
public class SparseViewHelper {
    /**
     * 获取item中的子控件
     *
     * @param view  子控件所在的父布局
     * @param resId 子控件的id
     * @param <V>   范式
     * @return 需要获取的子View
     */
    @SuppressWarnings("unchecked")
    public static <V extends View> V getView(View view, int resId) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(resId);
        if (childView == null) {
            childView = view.findViewById(resId);
            viewHolder.put(resId, childView);
        }

        return (V) childView;
    }
}
