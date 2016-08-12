package com.canmeizhexue.common.ui.viewflowdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.adapter.BaseAdapter;
import com.canmeizhexue.common.image.DisplayManager;


/**
 * 适配器
 */
public class BannerAdapter extends BaseAdapter<String> {

    /**
     * eke
     * @param context ww
     */
    public BannerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_banner, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgView);
        int curPosition = position % getAllItem().size();
        curPosition = curPosition >= getAllItem().size() ? 0 : curPosition;
        DisplayManager.show(mContext,imageView, getItem(curPosition));
        return convertView;
    }
}
