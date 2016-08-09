package com.canmeizhexue.common.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.module.GlideModule;
import com.canmeizhexue.common.utils.SDCardUtils;

import java.io.InputStream;

/**
 * <p>HtGlideModule类 自定义</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2016-4-5)
 */
public class CustomGlideModule implements GlideModule {

    /**
     * 内存缓存的比率
     */
    public static final float CACHE_RATE = 1.0f;
    /**
     * 磁盘缓存 - 150M
     */
    public static final int CACHE_DISK = 150 * 1024 * 1024;
    /**
     * 磁盘缓存文件夹名
     */
    public static final String CACHE_FOLDER = SDCardUtils.IMAGE_CACHE_IN_FOLDER;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (CACHE_RATE * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (CACHE_RATE * defaultBitmapPoolSize);
        //内存缓存
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        //磁盘缓存
        //builder.setDiskCache(new InternalCacheDiskCacheFactory(context, CACHE_DISK));
        builder.setDiskCache(new DiskLruCacheFactory(CACHE_FOLDER, CACHE_DISK)
        );

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(CustomImageSizeModel.class, InputStream.class, new CustomImageSizeModelFactory());
    }

    private class CustomImageSizeModelFactory implements ModelLoaderFactory<CustomImageSizeModel, InputStream> {
        @Override
        public ModelLoader<CustomImageSizeModel, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new CustomImageSizeUrlLoader(context);
        }

        @Override
        public void teardown() {

        }
    }

    /**
     * 接口
     */
    public interface CustomImageSizeModel {
        /**
         * 回调
         * @param width width
         * @param height height
         * @return
         */
        String requestCustomSizeUrl(int width, int height);
    }

    /**
     * 自动大小
     */
    public class CustomImageSizeUrlLoader extends BaseGlideUrlLoader<CustomImageSizeModel> {
        /**
         * 构造
         * @param context 上下文
         */
        public CustomImageSizeUrlLoader(Context context) {
            super(context);
        }

        @Override
        protected String getUrl(CustomImageSizeModel model, int width, int height) {
            return model.requestCustomSizeUrl(width, height);
        }
    }
}
