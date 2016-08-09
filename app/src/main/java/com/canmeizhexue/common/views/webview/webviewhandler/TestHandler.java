package com.canmeizhexue.common.views.webview.webviewhandler;

import android.net.Uri;
import android.webkit.WebView;

import com.canmeizhexue.common.utils.LogUtils;

/**测试类
 * Created by zengyaping on 2016-8-9.
 */
public class TestHandler extends BaseHandler{
    @Override
    public boolean onProcessProtocol(WebView mWebview, Uri uri, String originalUrl) {
        LogUtils.d("silence","TestHandler-----");
        return false;
    }
}
