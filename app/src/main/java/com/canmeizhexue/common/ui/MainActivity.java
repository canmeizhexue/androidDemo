package com.canmeizhexue.common.ui;

import android.os.Bundle;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
/*        // TODO 测试BlockCanary
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
