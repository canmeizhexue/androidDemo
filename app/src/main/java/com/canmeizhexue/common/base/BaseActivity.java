package com.canmeizhexue.common.base;

import android.app.Activity;
import android.os.Bundle;

import com.canmeizhexue.common.utils.LocalAppManager;

/**
 * Created by canmeizhexue on 2016-8-3.
 */
public class BaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalAppManager.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalAppManager.remove(this);
    }
}
