package com.canmeizhexue.common.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.canmeizhexue.common.R;


/**
 * <p>图片显示</p>
 *
 *  show()        1. 加载本地或网络，用于加载本地图片 和 如网络图片预览
 *  load()        2. 加载网络的图片，适用于表格，详情，列表等的小图，300x300, 服务器自动生成，方便可备份 （占带宽30KB左右）
 *  loadIcon()    3. 加载网络的图片，适用于头像等的小图，150x150  （占带宽10KB左右）
 *  loadBySize()  4. 加载网络的图片，读取imageView的大小，如广告banner长为800x300 ,就显示 url_800x300.jpg
 *  loadCircle()  5. 加载圆形的图片，本地和网路都适用
 *  setBackgroundResource() 6.设置背景本地资源
 *
 * @author canmeizhexue
 * @version 1.0 (2016-4-5)
 */
public class DisplayManager {

    /**
     * 图片显示，直接展示
     *
     * @param context   上下文
     * @param imageView 控件
     * @param url       地址
     * @param <T> 类型
     */
    public static <T> void show(Context context, ImageView imageView, T url) {
        show(context, imageView, url, R.drawable.imageselector_default_img);
    }

    /**
     * 图片显示
     * @param context 上下文
     * @param imageView 控件
     * @param url 地址
     * @param placeImage 占位符图片
     * @param <T> 类型
     */
    public static <T> void show(Context context, ImageView imageView, T url,
                                int placeImage) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(R.color.lib_gray_light40)
                .error(placeImage)
                .into(imageView);
    }

    /**
     * 图片显示，加载资源文件
     *
     * @param context    上下文
     * @param imageView  控件
     * @param res        资源
     */
    public static void showResource(Context context, ImageView imageView, int res) {
        Glide.with(context)
                .load(res)
                .asBitmap()
                .centerCrop()
                .placeholder(R.color.lib_gray_light40)
                .error(R.color.lib_gray_light40)
                .into(imageView);
    }

    /**
     * 图片显示，加载资源文件
     *
     * @param context    上下文
     * @param imageView  控件
     * @param res        资源
     */
    public static void showResourceFit(Context context, ImageView imageView, int res) {
        Glide.with(context)
                .load(res)
                .asBitmap()
                .fitCenter()
                .into(imageView);
    }

    /**
     * 读取网络图片，根据imageview的大小，"_"+height+"x"+width+".jpg"
     *
     * @param context   上下文
     * @param imageView 控件
     * @param url       地址
     */
    public static void loadBySize(Context context, ImageView imageView, String url) {
        loadBySize(context, imageView, url, R.color.lib_gray_light40);
    }

    /**
     * 读取网络图片，根据imageview的大小，"_"+height+"x"+width+".jpg"
     *
     * @param context   上下文
     * @param imageView 控件
     * @param placeImage 占位符图片
     * @param url       地址
     */
    public static void loadBySize(Context context, ImageView imageView, String url,
                                  int placeImage) {
        loadBySize(context, imageView, url, placeImage,placeImage);
    }

    /**
     * 读取网络图片，根据imageview的大小，"_"+height+"x"+width+".jpg"
     *
     * @param context    上下文
     * @param imageView  控件
     * @param url        地址
     * @param placeImage 占位符图片
     * @param errorImage 加载失败的图片
     */
    public static void loadBySize(Context context, ImageView imageView, String url,
                                  int placeImage, int errorImage) {
        Glide.with(context)
                .load(new CustomImageSizeModel(url))
                .asBitmap()
                .centerCrop()
                .placeholder(placeImage)
                .error(errorImage)
                .into(imageView);
    }

    /**
     * 图片显示，大小为 150x150，适用于头像
     *
     * @param context   上下文
     * @param imageView 控件
     * @param url       地址
     */
    public static void loadIcon(Context context, ImageView imageView, String url) {
        loadIcon(context, imageView, url, R.drawable.imageselector_default_avatar);
    }

    /**
     * 图片显示
     *
     * @param context    上下文
     * @param imageView  控件
     * @param url        地址
     * @param placeImage 占位符图片
     */
    public static void loadIcon(Context context, ImageView imageView, String url, int placeImage) {
        if(url.endsWith(".png") || url.endsWith(".PNG") || url.endsWith(".jpg") || url.endsWith(".JPG")){
            url = url.replace(url.substring(url.length()-4,url.length()-1), "_150x150"+url.substring(url.length()-4,url.length()-1));
        }
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(placeImage)
                .error(placeImage)
                .into(imageView);
    }

    /**
     * 图片显示，大小为 300x300，适用于列表，表格
     *
     * @param context   上下文
     * @param imageView 控件
     * @param url       地址
     */
    public static void load(Context context, ImageView imageView, String url) {
        load(context, imageView, url, R.drawable.imageselector_default_img);
    }

    /**
     * 图片显示
     *
     * @param context    上下文
     * @param imageView  控件
     * @param url        地址
     * @param placeImage 占位符图片
     */
    public static void load(Context context, ImageView imageView, String url, int placeImage) {
        if(url.endsWith(".png") || url.endsWith(".PNG") || url.endsWith(".jpg") || url.endsWith(".JPG")){
            url = url.replace(url.substring(url.length()-4,url.length()-1), "_300x300"+url.substring(url.length()-4,url.length()-1));
        }
        Glide.with(context)
                .load(url)
                .asBitmap()
//                .centerCrop()
                .placeholder(R.color.lib_gray_light40)
                .error(placeImage)
                .into(imageView);
    }

    /**
     * 图片显示
     *
     * @param context    上下文
     * @param imageView  控件
     * @param url        地址
     * @param w 宽
     * @param h 高
     */
    public static void load(Context context, ImageView imageView, String url, int w, int h) {
        if(url.endsWith(".png") || url.endsWith(".PNG") || url.endsWith(".jpg") || url.endsWith(".JPG")){
            url = url.replace(url.substring(url.length()-4,url.length()-1), "_"+w+"x"+h+url.substring(url.length()-4,url.length()-1));
        }
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(R.color.lib_gray_light40)
                .error(R.drawable.imageselector_default_img)
                .into(imageView);
    }

    /**
     * 圆形图片显示
     * @param context 上下文
     * @param imageView 控件
     * @param url 地址
     */
    public static void loadCircle(final Context context, final ImageView imageView, String url) {
        Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 设置背景
     * @param context 上下文
     * @param view 控件
     * @param res 资源
     * @param <T> T
     */
    public static <T> void setBackgroundResource(Context context, final View view, T res){
        Glide.with(context).load(res).error(R.color.lib_gray_light40).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(resource);
                }
            }});
    }

    /**
     * 设置背景
     * @param context 上下文
     * @param view 控件
     * @param res 资源
     * @param width width
     * @param height height
     * @param <T> T
     */
    public static <T> void setBackgroundResource(Context context, final View view, T res, int width, int height){
        Glide.with(context).load(res).override(width,height).error(R.color.lib_gray_light40).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(resource);
                }
            }});
    }
}
