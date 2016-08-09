package com.canmeizhexue.common.image;

import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * <p>监听加载</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2016-4-21)
 */
public class CommonRequestListener implements RequestListener {

    private ProgressBar progressBar;

    /**
     * 构造
     * @param progressBar 进度
     */
    public CommonRequestListener(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        return false;
    }
}
