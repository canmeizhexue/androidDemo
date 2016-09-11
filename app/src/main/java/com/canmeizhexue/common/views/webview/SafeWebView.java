package com.canmeizhexue.common.views.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.canmeizhexue.common.BuildConfig;
import com.canmeizhexue.common.R;
import com.canmeizhexue.common.utils.LogUtils;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangguojun on 2015/6/21.<p/>
 *<p>来源于 https://github.com/seven456/SafeWebView<p/>
 * Android4.2.2以下版本WebView有远程执行代码漏洞
 * 乌云上的介绍：http://www.wooyun.org/bugs/wooyun-2010-067676
 * 测试方法：让自己的WebView加载http://drops.wooyun.org/webview.html
 */
public class SafeWebView extends WebView {
    private static final String TAG = "SafeWebView";
    private Map<String, JsCallJava> mJsCallJavas;
    private Map<Integer, String> mInjectJavaScripts;
    private SafeWebChromeClient mWebChromeClient;
    private SafeWebViewClient mWebViewClient;
    /**加载进度条*/
    private ProgressBar progressbar;

    public SafeWebView(Context context) {
        this(context, null);
    }

    public SafeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //增加横向滚动条，表示加载进度
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 8, 0, 0));
        progressbar.setProgressDrawable(this.getResources().getDrawable(R.drawable.bg_progress_webview));
        addView(progressbar);
        removeSearchBoxJavaBridge();

        // WebView跨源（加载本地文件）攻击分析：http://blogs.360.cn/360mobile/2014/09/22/webview%E8%B7%A8%E6%BA%90%E6%94%BB%E5%87%BB%E5%88%86%E6%9E%90/
        // 是否允许WebView使用File协议，移动版的Chrome默认禁止加载file协议的文件；
        getSettings().setAllowFileAccess(false);
    }

    /**
     * 经过大量的测试，按照以下方式才能保证JS脚本100%注入成功：
     * 1、在第一次loadUrl之前注入JS（在addJavascriptInterface里面注入即可）；
     * 2、在webViewClient.onPageStarted中都注入JS；
     * 3、在webChromeClient.onProgressChanged中都注入JS，并且不能通过自检查（onJsPrompt里面判断）JS是否注入成功来减少注入JS的次数，因为网页中的JS可以同时打开多个url导致无法控制检查的准确性；
     * 4、注入的JS中已经在脚本（./library/doc/notRepeat.js）中检查注入的对象是否已经存在，避免注入对象被重新赋值导致网页引用该对象的方法时发生异常；
     *
     * @deprecated Android4.2.2及以上版本的addJavascriptInterface方法已经解决了安全问题，如果不使用“网页能将JS函数传到Java层”功能，不建议使用该类，毕竟系统的JS注入效率才是最高的；
     */
    @Override
    @Deprecated
    public void addJavascriptInterface(Object interfaceObj, String interfaceName) {
        if (mJsCallJavas == null) {
            mJsCallJavas = new HashMap<String, JsCallJava>();
        }
        mJsCallJavas.put(interfaceName, new JsCallJava(interfaceObj, interfaceName));
        setClient();
        if (mJsCallJavas != null) {
            injectJavaScript();
            if (BuildConfig.DEBUG) {
                LogUtils.d(TAG, "injectJavaScript, addJavascriptInterface.interfaceObj = " + interfaceObj + ", interfaceName = " + interfaceName);
            }
        }
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        if (client instanceof SafeWebViewClient) {
            if (mJsCallJavas != null) {
                super.setWebViewClient(client);
            } else {
                mWebViewClient = (SafeWebViewClient) client;
            }
        } else {
            super.setWebViewClient(client);
        }
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        if (client instanceof SafeWebChromeClient) {
            if (mJsCallJavas != null) {
                super.setWebChromeClient(client);
            } else {
                mWebChromeClient = (SafeWebChromeClient) client;
            }
        } else {
            super.setWebChromeClient(client);
        }
    }

    @Override
    public void destroy() {
        if (mJsCallJavas != null) {
            mJsCallJavas.clear();
        }
        if (mInjectJavaScripts != null) {
            mInjectJavaScripts.clear();
        }
        removeAllViews();
        //WebView中包含一个ZoomButtonsController，当使用web.getSettings().setBuiltInZoomControls(true);启用该设置后，用户一旦触摸屏幕，就会出现缩放控制图标。这个图标过上几秒会自动消失，但在3.0系统以上上，如果图标自动消失前退出当前Activity的话，就会发生ZoomButton找不到依附的Window而造成程序崩溃，解决办法很简单就是在Activity的ondestory方法中调用web.setVisibility(View.GONE);方法，手动将其隐藏，就不会崩溃了。在3.0一下系统上不会出现该崩溃问题，真是各种崩溃，防不胜防啊！
        setVisibility(View.GONE);
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup mWebViewContainer = (ViewGroup) getParent();
            mWebViewContainer.removeAllViews();
        }
        releaseConfigCallback();
        super.destroy();
    }

    @Override
    public void loadUrl(String url) {
        if (mJsCallJavas == null) {
            setClient();
        }
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (mJsCallJavas == null) {
            setClient();
        }
        super.loadUrl(url, additionalHttpHeaders);
    }

    private void setClient() {
        if (mWebChromeClient != null) {
            setWebChromeClient(mWebChromeClient);
            mWebChromeClient = null;
        }
        if (mWebViewClient != null) {
            setWebViewClient(mWebViewClient);
            mWebViewClient = null;
        }
    }

    /**
     * 添加并注入JavaScript脚本（和“addJavascriptInterface”注入对象的注入时机一致，100%能注入成功）；
     * 注意：为了做到能100%注入，需要在注入的js中自行判断对象是否已经存在（如：if (typeof(window.Android) = 'undefined')）；
     * @param javaScript
     */
    public void addInjectJavaScript(String javaScript) {
        if (mInjectJavaScripts == null) {
            mInjectJavaScripts = new HashMap<Integer, String>();
        }
        mInjectJavaScripts.put(javaScript.hashCode(), javaScript);
        injectExtraJavaScript();
    }

    private void injectJavaScript() {
        for (Map.Entry<String, JsCallJava> entry : mJsCallJavas.entrySet()) {
            this.loadUrl(buildNotRepeatInjectJS(entry.getKey(), entry.getValue().getPreloadInterfaceJS()));
        }
    }

    private void injectExtraJavaScript() {
        for (Map.Entry<Integer, String> entry : mInjectJavaScripts.entrySet()) {
            this.loadUrl(buildTryCatchInjectJS(entry.getValue()));
        }
    }

    /**
     * 构建一个“不会重复注入”的js脚本；
     * @param key
     * @param js
     * @return
     */
    public String buildNotRepeatInjectJS(String key, String js) {
        String obj = String.format("__injectFlag_%1$s__", key);
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:try{(function(){if(window.");
        sb.append(obj);
        sb.append("){console.log('");
        sb.append(obj);
        sb.append(" has been injected');return;}window.");
        sb.append(obj);
        sb.append("=true;");
        sb.append(js);
        sb.append("}())}catch(e){console.warn(e)}");
        return sb.toString();
    }

    /**
     * 构建一个“带try catch”的js脚本；
     * @param js
     * @return
     */
    public String buildTryCatchInjectJS(String js) {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:try{");
        sb.append(js);
        sb.append("}catch(e){console.warn(e)}");
        return sb.toString();
    }

    /**
     * 如果没有使用addJavascriptInterface方法，不需要使用这个类；
     */
    public class SafeWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (mJsCallJavas != null) {
                injectJavaScript();
                if (BuildConfig.DEBUG) {
                    LogUtils.d(TAG, "injectJavaScript, onPageStarted.url = " + view.getUrl());
                }
            }
            if (mInjectJavaScripts != null) {
                injectExtraJavaScript();
            }
            super.onPageStarted(view, url, favicon);
        }
    }
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private final static int FILE_CHOOSE_RESULT_CODE_ABOVE_L=10001;
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        if(getContext() instanceof Activity){
            Activity activity = (Activity) getContext();
            activity.startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
        }

    }

    // TODO 如果要使用上传文件功能，必须添加这个，
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }else if(FILE_CHOOSE_RESULT_CODE_ABOVE_L==requestCode){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                if (uploadMessageAboveL == null)
                    return;
                uploadMessageAboveL.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                uploadMessageAboveL = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            // 可以用这个解析一下
//                WebChromeClient.FileChooserParams.parseResult(resultCode,intent);
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }
    /**
     * 如果没有使用addJavascriptInterface方法，不需要使用这个类；
     */
    public class SafeWebChromeClient extends WebChromeClient {
        //http://stackoverflow.com/questions/5907369/file-upload-in-webview

        // google自己浏览器，，http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android-apps/4.0.4_r1.2/com/android/browser/UploadHandler.java#UploadHandler.openFileChooser%28android.webkit.ValueCallback,java.lang.String%29
        // 4.4  4.4.1  4.4.2这三个不会调用openFileChooser，4.4.3才修复这个bug

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            Log.d("silence","ValueCallback<Uri> valueCallback");
            uploadMessage = valueCallback;
            openImageChooserActivity();
        }

        // For Android  >= 3.0
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            Log.d("silence","ValueCallback valueCallback, String acceptType");
            uploadMessage = valueCallback;
            openImageChooserActivity();
        }

        //For Android  >= 4.1  这个方法对于4.4不一定有用
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            Log.d("silence","ValueCallback<Uri> valueCallback, String acceptType, String capture");

            uploadMessage = valueCallback;
            openImageChooserActivity();
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            Log.d("silence","onShowFileChooser");
/*            // 5.0以上版本可以使用这个来创建intent，，，，  Intent intent = fileChooserParams.createIntent();
            uploadMessageAboveL = filePathCallback;
            openImageChooserActivity();
            return true;*/

            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessageAboveL = filePathCallback;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                Intent intent = fileChooserParams.createIntent();
                try
                {
                    Activity activity = (Activity) getContext();
                    activity.startActivityForResult(intent, FILE_CHOOSE_RESULT_CODE_ABOVE_L);
                } catch (Exception e)
                {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }else{
                openImageChooserActivity();
            }
            return true;


        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mJsCallJavas != null) {
                injectJavaScript();
                if (BuildConfig.DEBUG) {
                    LogUtils.d(TAG, "injectJavaScript, onProgressChanged.newProgress = " + newProgress + ", url = " + view.getUrl());
                }
            }
            if (mInjectJavaScripts != null) {
                injectExtraJavaScript();
            }
            //处理进度条的显隐
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE) {
                    progressbar.setVisibility(VISIBLE);
                }
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (mJsCallJavas != null && JsCallJava.isSafeWebViewCallMsg(message)) {
                JSONObject jsonObject = JsCallJava.getMsgJSONObject(message);
                String interfacedName = JsCallJava.getInterfacedName(jsonObject);
                if (interfacedName != null) {
                    JsCallJava jsCallJava = mJsCallJavas.get(interfacedName);
                    if (jsCallJava != null) {
                        result.confirm(jsCallJava.call(view, jsonObject));
                    }
                }
                return true;
            } else {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        }
    }

    // 解决WebView内存泄漏问题；
    private void releaseConfigCallback() {
        if (android.os.Build.VERSION.SDK_INT < 16) { // JELLY_BEAN
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (NoSuchFieldException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Android 4.4 KitKat 使用Chrome DevTools 远程调试WebView
     * WebView.setWebContentsDebuggingEnabled(true);
     * http://blog.csdn.net/t12x3456/article/details/14225235
     */
    @TargetApi(19)
    protected void trySetWebDebuggEnabled() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= 19) {
            try {
                Class<?> clazz = WebView.class;
                Method method = clazz.getMethod("setWebContentsDebuggingEnabled", boolean.class);
                method.invoke(null, true);
            } catch (Throwable e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解决Webview远程执行代码漏洞，避免被“getClass”方法恶意利用（在loadUrl之前调用，如：MyWebView(Context context, AttributeSet attrs)里面）；
     * 漏洞详解：http://drops.wooyun.org/papers/548
     * <p/>
     * function execute(cmdArgs)
     * {
     *     for (var obj in window) {
     *        if ("getClass" in window[obj]) {
     *            alert(obj);
     *            return ?window[obj].getClass().forName("java.lang.Runtime")
     *                 .getMethod("getRuntime",null).invoke(null,null).exec(cmdArgs);
     *        }
     *     }
     * }
     *
     * @return
     */
    @TargetApi(11)
    protected boolean removeSearchBoxJavaBridge() {
        try {
            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
                Method method = this.getClass().getMethod("removeJavascriptInterface", String.class);
                method.invoke(this, "searchBoxJavaBridge_");
                return true;
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解决Android4.2中开启了辅助模式后，LocalActivityManager控制的Activity与AccessibilityInjector不兼容导致的崩溃问题；
     * Caused by: java.lang.NullPointerException
     * at android.webkit.AccessibilityInjector$TextToSpeechWrapper$1.onInit(AccessibilityInjector.java:753)
     * ...
     * at android.webkit.WebSettingsClassic.setJavaScriptEnabled(WebSettingsClassic.java:1125)
     * 必须放在webSettings.setJavaScriptEnabled之前执行；
     */
    protected void fixedAccessibilityInjectorException() {
        if (Build.VERSION.SDK_INT == 17) {
            try {
                Object webViewProvider = WebView.class.getMethod("getWebViewProvider").invoke(this);
                Method getAccessibilityInjector = webViewProvider.getClass().getDeclaredMethod("getAccessibilityInjector");
                getAccessibilityInjector.setAccessible(true);
                Object accessibilityInjector = getAccessibilityInjector.invoke(webViewProvider);
                getAccessibilityInjector.setAccessible(false);
                Field mAccessibilityManagerField = accessibilityInjector.getClass().getDeclaredField("mAccessibilityManager");
                mAccessibilityManagerField.setAccessible(true);
                Object mAccessibilityManager = mAccessibilityManagerField.get(accessibilityInjector);
                mAccessibilityManagerField.setAccessible(false);
                Field mIsEnabledField = mAccessibilityManager.getClass().getDeclaredField("mIsEnabled");
                mIsEnabledField.setAccessible(true);
                mIsEnabledField.set(mAccessibilityManager, false);
                mIsEnabledField.setAccessible(false);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 向网页更新Cookie，设置cookie后不需要页面刷新即可生效；
     */
    protected void updateCookies(String url, String value) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) { // 2.3及以下
            CookieSyncManager.createInstance(getContext().getApplicationContext());
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, value);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) { // 2.3及以下
            CookieSyncManager.getInstance().sync();
        }
    }
    /**
     * 增加对进度条的处理
     * @param l l
     * @param t t
     * @param oldl oldl
     * @param oldt oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 获取进度条控件
     * @return
     */
    public ProgressBar getProgressbar() {
        return progressbar;
    }
}