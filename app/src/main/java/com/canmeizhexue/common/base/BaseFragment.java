package com.canmeizhexue.common.base;


import android.support.v4.app.Fragment;

/**
 * Created by canmeizhexue on 2016-8-3.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 监控Fragment是否被回收
        BaseApplication.getApplication().watch(this);
    }
}
