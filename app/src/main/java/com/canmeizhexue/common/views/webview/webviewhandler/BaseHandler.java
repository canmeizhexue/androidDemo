package com.canmeizhexue.common.views.webview.webviewhandler;

import android.net.Uri;
import android.webkit.WebView;

/**
 * <p>BaseProcessor类 概述，自定义协议处理器基类</p>
 *
 * @author zengyaping
 * @version 1.0 (2016-3-17)
 */
public abstract class BaseHandler {

    /**
     * 子类覆写该方法，来进行具体的处理
     * @param mWebview webview
     * @param uri uri形式的链接
     * @param originalUrl 原始的链接
     */
    public abstract boolean onProcessProtocol(WebView mWebview, Uri uri, String originalUrl);
}
