package com.canmeizhexue.common.views.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.canmeizhexue.common.BuildConfig;
import com.canmeizhexue.common.utils.LogUtils;
import com.canmeizhexue.common.utils.NetWorkUtil;


/**
 * <p>WebviewHelper Webview帮助类，统一配置</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2016/1/14 15:45)
 */
public class WebviewHelper {
    //常量区 start===================================================================================
    /**
     * 当前类标识
     */
    private static final String LOG_TAG = WebviewHelper.class.getSimpleName();
    /**
     * handle处理标识，请求错误1002
     */
    private static final int HANDLE_KEY_RESOURCE_ERROR = 1002;
    /**
     * 初始加载假进度值为35%
     */
    private static final int LOAD_PROGRESS_START = 35;
    //变量区 start===================================================================================

    /**
     * Activity 上下文
     */
    private Activity activity;
    /**
     * webview控件的资源id
     */
    private int mWebViewId;
    /**
     * webview控件
     */
    private SafeWebView mWebView;
    /**
     * 加载的js方法
     */
    private String jsMethod = null;

    /**
     * 是否启用缓存:是true（默认）,否false
     */
    private boolean cacheEnable = true;


    /**
     * 当前访问的连接
     */
    private String currUrL;
    /**
     * 是否清除访问历史记录:是true,否false（默认）
     */
    private boolean needClearHistory = false;
    /**
     * 构造方法
     *
     * @param activity    上下文
     * @param webviewId   webview控件的资源id
     * @param cacheEnable 是否启用缓存:是true（默认）,否false
     */
    public WebviewHelper(Activity activity, int webviewId, boolean cacheEnable) {
        super();
        this.activity = activity;
        this.mWebViewId = webviewId;
        this.cacheEnable = cacheEnable;
        mWebView = this.createWebView(mWebViewId);
    }

    /**
     * 构造方法
     *
     * @param activity      上下文
     * @param htSafeWebview webview控件的资源
     * @param cacheEnable   是否启用缓存:是true（默认）,否false
     */
    public WebviewHelper(Activity activity, SafeWebView htSafeWebview, boolean cacheEnable) {
        super();
        this.activity = activity;
        this.cacheEnable = cacheEnable;
        mWebView = this.createWebView(htSafeWebview);
    }


    /**
     * 加载网络链接
     *
     * @param webView webview控件
     * @param url     html网络链接
     */
    private void loadUrl(final SafeWebView webView, final String url) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                currUrL = url;
                webView.loadUrl(url);
            }
        });
    }

    //对外方法区 start===============================================================================

    /**
     * 获取webview控件
     *
     * @return
     */
    public SafeWebView getWebview() {
        return mWebView;
    }

    /**
     * 销毁
     */
    public void destroyView() {
        if (null != mWebView) {
            mWebView.stopLoading();
            mWebView.clearCache(true);
            mWebView.clearFormData();
            mWebView.clearHistory();
            mWebView.clearAnimation();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    /**
     * 加载url
     *
     * @param url html网络链接
     */
    public void loadUrl(String url) {
        loadUrl(mWebView, url);
    }

    /**
     * 加载方法，网页加载完毕时加载方法，防止加载不到方法
     *
     * @param method js方法
     */
    public void loadMethod(String method) {
        this.jsMethod = method;
    }

    /**
     * 刷新页面-重新加载
     */
    public void reload() {
        if (null != mWebView) {
            mWebView.reload();
        }
    }



//创建WebView相关配置 start
//==============================

    /**
     * 初始化webview
     *
     * @param webviewId webview控件id
     * @return
     */
    @SuppressLint("SetJavaScriptEnabled")
    private SafeWebView createWebView(int webviewId) {
        try {
            final SafeWebView webView = (SafeWebView) activity.findViewById(webviewId);
            return createWebView(webView);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 初始化webview
     *
     * @param webView webview控件
     * @return
     */
    @SuppressLint("SetJavaScriptEnabled")
    private SafeWebView createWebView(SafeWebView webView) {
        try {
            /********************webView 设置相关 start************************/
            WebSettings settings = webView.getSettings();
            settings.setSupportZoom(true);            //将图片调整到适合webview的大小
            settings.setLoadWithOverviewMode(true);  //页面自动缩放
            settings.setBuiltInZoomControls(false);    //设置显示缩放按钮
            settings.setJavaScriptEnabled(true);    //启用JS脚本
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setAllowFileAccess(true); // 允许访问文件
            settings.setDomStorageEnabled(true);

            /**
             * 用WebView显示图片，可使用这个参数
             * 设置网页布局类型：
             * 1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
             * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
             */
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setDefaultTextEncodingName("utf-8"); //设置文本编码
            settings.setAppCacheEnabled(cacheEnable);

            if (NetWorkUtil.isNetworkConnected(activity)) {
                if (cacheEnable) {
                    settings.setCacheMode(WebSettings.LOAD_DEFAULT);//启用-默认缓存模式
                } else {
                    settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不启用缓存
                }
            } else {//没网，则从本地获取，即离线加载
                settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
//        settings.setBlockNetworkImage(true);
//        settings.setGeolocationEnabled(true);
            //获取APP当前的版本信息,用于设置 user-agent,作特征标记
            //特别注意：此值为和第三方web（如衣家帮、我们的前端）约定的，不可改动
            // TODO ua
//            String ua = "htmmowner_android_" + AppUtils.getVersionName(activity) + "_" + BuildConfig.BUILD_TYPE;
//            webView.getSettings().setUserAgentString(ua);

            webView.setHapticFeedbackEnabled(false);
            webView.setBackgroundColor(0xffeeeeee);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            // 这里是关闭硬件加速  关闭加速后, 首页图片会闪烁
            //webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            webView.requestFocus();
            webView.setFocusable(true);

            webView.setWebViewClient(new WebViewClientUtil());
            final SafeWebView finalWebView = webView;
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress >= 100) {
                        if (!TextUtils.isEmpty(jsMethod)) {
                            view.loadUrl(jsMethod);
                        }
                    }
                    //处理进度条的显隐
                    if (newProgress == 100) {
                        finalWebView.getProgressbar().setVisibility(View.GONE);
                    } else {
                        if (finalWebView.getProgressbar().getVisibility() == View.GONE) {
                            finalWebView.getProgressbar().setVisibility(View.VISIBLE);
                        }
                        if (newProgress >= LOAD_PROGRESS_START) {
                            finalWebView.getProgressbar().setProgress(newProgress);
                        }
                    }
                    super.onProgressChanged(view, newProgress);
                }

                @Override
                public boolean onJsAlert(WebView view, String url, String message,
                                         final JsResult result) {
                    return true;
                }

                @Override
                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                    if (BuildConfig.DEBUG) {
                        LogUtils.d(LOG_TAG, "error message：" + consoleMessage.message() + "\nfrom line" + consoleMessage.lineNumber() + " of\n"
                                + consoleMessage.sourceId());
                    }
                    return true;
                }

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
                    if (!TextUtils.isEmpty(title)
                            && (
                            title.equals("找不到网页")
                                    || title.equals("404 Not Found")
                                    || title.toLowerCase().contains("error"))) {
                        view.stopLoading();
                    }

                }
            });
            /********************webView 设置相关 end************************/
            return webView;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 配置链接处理方式
     */
    private class WebViewClientUtil extends WebViewClient {
        /**
         * <p>
         * 页面开始加载
         * </p>
         *
         * @param view    view
         * @param url     url
         * @param favicon favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //处理进度条的显示
            if (mWebView.getProgressbar().getVisibility() == View.GONE) {
                mWebView.getProgressbar().setVisibility(View.VISIBLE);
            }
            mWebView.getProgressbar().setProgress(LOAD_PROGRESS_START);//初始显示进度35%
        }

        /**
         * onPageFinished指页面加载完成
         *
         * @param view view
         * @param url  url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            try {
                if (null != view) {
                    //获取历史列表~此方法在页面未加载完毕，就跳转至其他页面加载url，会导致崩溃
                    WebBackForwardList mWebBackForwardList = view.copyBackForwardList();
                    //TODO 可能存在问题，若H5页面出错，获取到的标题可能不友好
                    if (null != activity && !activity.isFinishing()
                            && !TextUtils.isEmpty(view.getTitle())
                            && !(view.getTitle().contains("错误")
                            || view.getTitle().contains("error")
                            || view.getTitle().contains("异常")
                            || view.getTitle().contains("找不到网页")
                            || view.getTitle().contains("404 Not Found"))
                            && (null != mWebBackForwardList && mWebBackForwardList.getSize() > 1)) {
                        //获取当前加载页面的标题，并设置为当前activity的标题，activity监听到标题变化则作处理
                        activity.setTitle(view.getTitle());
                    }
                }
            }catch (Exception e){
                 LogUtils.e(LOG_TAG, e.getMessage());
            }
        }

        /**
         * url重定向会执行此方法以及点击页面某些链接也会执行此方法
         *
         * @param view 当前webview
         * @param url  即将重定向的url
         * @return <p>
         * true:表示当前url已经加载完成，即使url还会重定向都不会再进行加载
         * (只有当不需要加载网址而是拦截做其他处理，如拦截tel:xxx等特殊url做拨号处理的时候，才应该返回true)
         * false 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
         * (如果不需要对url进行拦截做处理，而是简单的继续加载此网址。
         * 则建议采用返回false的方式而不是loadUrl的方式进行加载网址。)
         * </p>
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean handled = H5Dispatcher.handleH5Protocol(view,url);
            if(handled){
                return true;
            }
            return super.shouldOverrideUrlLoading(view,url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            LogUtils.e(LOG_TAG, "super.onReceivedError(view, " + errorCode + ", " + description + ", " + failingUrl + ");");
            if (!TextUtils.isEmpty(currUrL) && currUrL.equals(failingUrl)) {
                view.pauseTimers();
                try {
                    view.stopLoading();
                } catch (Exception e) {
                }
                try {
                    view.clearView();
                } catch (Exception e) {
                }
                if (view.canGoBack()) {
                    view.goBack();
                }
                if (errorCode == WebViewClient.ERROR_HOST_LOOKUP
                        || errorCode == WebViewClient.ERROR_CONNECT
                        || errorCode == WebViewClient.ERROR_TIMEOUT) {
                    Message msg = webhandler.get().obtainMessage();//发送通知，加入线程
                    msg.what = HANDLE_KEY_RESOURCE_ERROR;//通知类型
                    webhandler.get().sendMessage(msg);//通知发送！
                    return;
                }
            }
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        /**
         * Report web resource loading error to the host application. These errors usually indicate
         * inability to connect to the server. Note that unlike the deprecated version of the callback,
         * the new version will be called for any resource (iframe, image, etc), not just for the main
         * page. Thus, it is recommended to perform minimum required work in this callback.
         *
         * @param view    The WebView that is initiating the callback.
         * @param request The originating request.
         * @param error   Information about the error occured.
         */
        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
        }

        /**
         * This callback will be called
         * for any resource (iframe, image, etc), not just for the main page. Thus, it is recommended to
         * perform minimum required work in this callback
         *
         * @param view          view
         * @param request       request
         * @param errorResponse errorResponse
         */
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            LogUtils.e(LOG_TAG, "super.onReceivedHttpError(view, request, " + errorResponse.toString() + ");");
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            LogUtils.e(LOG_TAG, "super.onReceivedSslError(view, handler, " + error.toString() + ");");
            //忽略证书的错误继续Load页面内容
            handler.proceed();
            //handler.cancel(); // Android默认的处理方式
            //handleMessage(Message msg); // 进行其他处理
            //super.onReceivedSslError(view, handler, error);
        }

        /**
         * 是否清除访问的历史记录
         * @param view view
         * @param url url
         * @param isReload isReload
         */
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
//            LogUtils.e(LOG_TAG, "历史栈变化："+view.copyBackForwardList().getSize());
//            WebBackForwardList wbfList = view.copyBackForwardList();
//            if (null != wbfList && wbfList.getSize()>0) {
//                for (int i = 0; i < wbfList.getSize(); i++) {
//                      LogUtils.e(LOG_TAG, "访问的记录链接："+wbfList.getItemAtIndex(i).getOriginalUrl());
//                }
//            }
            if (needClearHistory) {
                needClearHistory = false;
                view.clearHistory();//清除历史记录
            }
        }
    }

    /**
     * 处理网络异常
     */
    final ThreadLocal<Handler> webhandler = new ThreadLocal<Handler>() {
        @Override
        protected Handler initialValue() {
            return new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case HANDLE_KEY_RESOURCE_ERROR: {//请求异常
                            LogUtils.e(LOG_TAG, "请求异常：handleMessage：" + msg.toString());
                            if (null != mWebView && mWebView.isShown()) {
                                mWebView.loadUrl("file:///android_asset/net_error_files/showerror.html");
                            }
                        }
                        break;
                        default:
                            break;
                    }
                }
            };
        }
    };
//创建WebView相关 end
//==============================

    /**
     * 设置是否清除历史记录
     * @param needClearHistory true是，false否
     */
    public void setNeedClearHistory(boolean needClearHistory) {
        this.needClearHistory = needClearHistory;
    }
}
