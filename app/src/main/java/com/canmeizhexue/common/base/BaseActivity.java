package com.canmeizhexue.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.canmeizhexue.common.utils.LocalAppManager;

/**
 * Created by canmeizhexue on 2016-8-3.
 */
public class BaseActivity extends AppCompatActivity{
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
