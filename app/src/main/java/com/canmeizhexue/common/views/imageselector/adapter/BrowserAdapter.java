package com.canmeizhexue.common.views.imageselector.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canmeizhexue.common.R;
import com.canmeizhexue.common.image.CommonRequestListener;
import com.canmeizhexue.common.utils.LogUtils;
import com.canmeizhexue.common.views.imageselector.MultiImageBrowserActivity;
import com.canmeizhexue.common.views.photoview.PhotoView;
import com.canmeizhexue.common.views.photoview.PhotoViewAttacher;

import java.util.ArrayList;

/**
 * <p>图片浏览适配器</p>
 *
 * @author canmeizhexue<br/>
 * @version 1.0 (2015-11-09)
 */
public class BrowserAdapter extends PagerAdapter {
    /**上下文对象*/
    private Context context;
    /**浏览类型*/
    private int browserType;
    /**浏览模式标识*/
    private int selectMode;
    /**选择模式时图片选择监听器*/
    private OnPaperSelectListener listener;
    /**LayoutInflater*/
    private LayoutInflater inflater;
    /**图片集合*/
    private ArrayList<String> images;
    /**已选图片集合*/
    private ArrayList<String> selectedImages;
    /**最大选择数*/
    private int maxCount = 1;
    private ProgressBar progressBar;
    /**
     * 构造方法
     * @param context 上下文对象
     * @param browserType 浏览类型
     */
    public BrowserAdapter(Context context, int browserType) {
        this.context = context;
        this.browserType = browserType;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 设置图片选择监听器，只用于选择模式
     * @param listener 图片选择监听器
     */
    public void setOnselectListener(OnPaperSelectListener listener) {
        this.listener = listener;
    }

    /**
     * 设置已选图片
     * @param selectedImages 已选图片
     */
    public void setSelectedImages(ArrayList<String> selectedImages){
        this.selectedImages = selectedImages;
    }

    /**
     * 设置图片集合
     * @param images 图片集合
     */
    public void setData(ArrayList<String> images){
        this.images = images;
    }

    /**
     * 设置选择模式，单选或多选
     * @param selectMode
     */
    public void setSelectMode(int selectMode) {
        this.selectMode = selectMode;
    }

    /**
     * 设置最高选择数据
     * @param maxCount 最高选择数据
     */
    public void setMaxCount(int maxCount){
        this.maxCount = maxCount;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.imagebrowser_paper_item, container, false);
        final PhotoView photoView = (PhotoView)view.findViewById(R.id.photoview);
        final CheckBox checkBox  = (CheckBox)view.findViewById(R.id.checkbox);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        final String image = images.get(position);

        if(image.startsWith("http://")) {
            String uri = image;
            if(uri.endsWith(".png") || uri.endsWith(".PNG") || uri.endsWith(".jpg") || uri.endsWith(".JPG")){
                uri = uri.replace(uri.substring(uri.length()-4,uri.length()-1), "_300x300"+uri.substring(uri.length()-4,uri.length()-1));
            }
            LogUtils.e("image url: "+uri);
            Glide.with(context)
                    .load(image)
                    .fitCenter()
                    .thumbnail(Glide.with(context).load(image))
                    .listener(new CommonRequestListener(progressBar))
                    .into(photoView);
        }else {
            Glide.with(context).load(image).asBitmap().fitCenter().thumbnail(0.1f).listener(new CommonRequestListener(progressBar)).into(photoView);
        }

        if (browserType == MultiImageBrowserActivity.BROWSER_TYPE_PREVIEW) {
            // 浏览模式
            checkBox.setVisibility(View.GONE);
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (listener != null) {
                        listener.onClicked();
                    }
                }
            });
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onLongClicked(position);
                        return true;
                    }
                    return false;
                }
            });
        } else if (browserType == MultiImageBrowserActivity.BROWSER_TYPE_SELECTOR)  {
            if (selectMode == MultiImageBrowserActivity.SELECT_MODE_SINGLE) {
                // 单选模式
                checkBox.setVisibility(View.GONE);
                photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        if (listener != null) {
                            listener.onSelected(image);
                        }
                    }
                });
            } else {
                // 多选模式
                if(selectedImages != null && selectedImages.size() > 0) {
                    checkBox.setChecked(selectedImages.contains(image));
                }
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (selectedImages.size() >= maxCount && isChecked) {
                            checkBox.setChecked(false);
                            Toast.makeText(context,"已经达到最高选择数量", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (isChecked) {
                            if (listener != null) {
                                listener.onSelected(image);
                            }
                        } else {
                            if (listener != null) {
                                listener.onUnselected(image);
                            }
                        }
                    }
                });
            }
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    /**
     * 图片选择监听器
     */
    public interface OnPaperSelectListener {
        /**
         * 选择
         * @param image 图片对象
         */
        void onSelected(String image);

        /**
         * 退选
         * @param image 图片对象
         */
        void onUnselected(String image);

        /**
         * 点击
         */
        void onClicked();

        /**
         *长按
         * @param position 被长按的图片位置
         */
        void onLongClicked(int position);
    }
}

