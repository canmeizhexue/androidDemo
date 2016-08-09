package com.canmeizhexue.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.entity.DemoModel;
import com.canmeizhexue.common.utils.SparseViewHelper;

import java.util.List;

/**
 * <p>DemoAdapter类 </p>
 *
 * @author hxm
 * @version 1.0 (2015/10/19)
 */
public class DemoAdapter extends BaseAdapter<DemoModel> {
    /**
     * 构造函数，在这里初始化各项属性值
     *
     * @param context  上下文
     * @param list     数据列表
     */
    public DemoAdapter(Context context, List<DemoModel> list) {
        super(context, list);
    }

    /**
     * 构造函数，在这里初始化各项属性值
     *
     * @param context  上下文
     */
    public DemoAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_demo, parent, false);
        }
        DemoModel currDemo = list.get(position);

        TextView tvName = SparseViewHelper.getView(convertView, R.id.tv_name);
        tvName.setText(currDemo.name);

        return convertView;
    }
}
