package com.canmeizhexue.common.views.webview;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.canmeizhexue.common.views.webview.webviewhandler.BaseHandler;

import java.util.HashMap;

/**h5页面与android交互的分发平台
 * scheme://authority/path?query#fragment
 *
 * http://blog.csdn.net/harvic880925/article/details/44679239
 * Created by zengyaping on 2016-8-9.
 */
public class H5Dispatcher {
    // TODO 需要更改
    private static final String CUSTOM_SCHEME="canmeizhexue";
    private static H5Dispatcher sInstance= new H5Dispatcher();
    // authority对应不同的功能模块，后续这个可以尝试反射到具体的Class，以免过早加载大量的Class
    private static final HashMap<String,String>authorityToClass = new HashMap<>(16);
    static {
        // 一定要对应到具体的类名
        authorityToClass.put("test","com.canmeizhexue.common.views.webview.webviewhandler.TestHandler");
    }
    public static boolean handleH5Protocol(WebView webView, String url){
        try {
            if(webView!=null && !TextUtils.isEmpty(url)){
                Uri uri = Uri.parse(url);
                String scheme=uri.getScheme();
                if(CUSTOM_SCHEME.equals(scheme)){
                    // 是我们的自定义协议
                    String authority = uri.getAuthority();
                    if(!TextUtils.isEmpty(authority)){
                        String handlerClass = authorityToClass.get(authority);
                        Class cls = Class.forName(handlerClass);
                        BaseHandler baseHandler = (BaseHandler) cls.newInstance();
                        return baseHandler.onProcessProtocol(webView,uri,url);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
