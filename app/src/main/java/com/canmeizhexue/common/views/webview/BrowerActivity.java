package com.canmeizhexue.common.views.webview;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zengyaping on 2016-8-9.
 */
public class BrowerActivity extends BaseActivity {
    WebviewHelper webviewHelper;
    @Bind(R.id.root)
    LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brower);
        ButterKnife.bind(this);
        webviewHelper = new WebviewHelper(this, (SafeWebView) findViewById(R.id.safewebview), true);
        if(savedInstanceState!=null){
            webviewHelper.getWebview().restoreState(savedInstanceState);
        }
       String HTML = "http://ht-wx.evergrande.com/index.php?s=/addon/Community/ConvenientInfo/index/community_id/145";
        webviewHelper.getWebview().loadUrl(HTML);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (webviewHelper != null && null != webviewHelper.getWebview()) {
            webviewHelper.getWebview().saveState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        if (null != root) {
            root.removeAllViews();
            root = null;
        }
        if (webviewHelper != null) {
            webviewHelper.destroyView();
        }
        super.onDestroy();
    }
}
