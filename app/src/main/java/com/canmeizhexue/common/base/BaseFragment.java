package com.canmeizhexue.common.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.canmeizhexue.common.utils.LogUtils;

/**
 * Created by canmeizhexue on 2016-8-3.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onAttach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onDestroyView");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onDetach");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onResume");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onSaveInstanceState");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onStop");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 在和viewpager配合使用时，这个可以标识页面可见与否
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----setUserVisibleHint");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onViewCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 监控Fragment是否被回收
        BaseApplication.getApplication().watch(this);
        LogUtils.d("silence",getClass().getSimpleName()+"---"+this+"----onDestroy");
    }

}
