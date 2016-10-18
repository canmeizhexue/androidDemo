package com.canmeizhexue.common.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

import com.canmeizhexue.common.viewpager.FragmentPagerAdapter;
import com.canmeizhexue.common.viewpager.ViewPager;

import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * <p>BaseFragmentTabActivity类 概述，用fragment实现的tab页</p>
 *
 * @author silence
 * @version 1.0 (2016-3-16)
 */
public abstract class BaseFragmentTabActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewpager;

    private ArrayList<Fragment> fragmentList;//
    private MyViewPagerAdapter pagerAdapter;
    private static final String STATE_CUR_TAB = "STATE_CUR_TAB";
    private int currentTab = 0;//
    private int tabCount;//

    protected final void initViews() {
        fragmentList = new ArrayList<Fragment>();
        generateAllFragment();
        pagerAdapter = new MyViewPagerAdapter(getFragmentManager());
        viewpager.setOffscreenPageLimit(offscreenPageLimit());
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOnPageChangeListener(this);
        viewpager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        onInitViews();
        trggierTabEvent(currentTab);
    }

    /**
     * 子类可以在这初始化view
     */
    protected void onInitViews(){

    }
    /**
     * viewpager缓存的view个数
     * @return
     */
    protected int offscreenPageLimit(){
        return 1;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CUR_TAB, currentTab);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentTab = savedInstanceState.getInt(STATE_CUR_TAB, 0);
        trggierTabEvent(currentTab);

    }
    private String getFragmentTag(int viewId, int position) {
        try {
            Method method = FragmentPagerAdapter.class.getDeclaredMethod("makeFragmentName", int.class, long.class);
            if (method != null) {
                method.setAccessible(true);
                Object result = method.invoke(null, viewId, position);
                if (result != null) {
                    return result.toString();
                }
            }
        } catch (Exception throwable) {
            throwable.printStackTrace();
        }
        return "";
    }

    private Fragment generateFragment(int tabIndex) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            Fragment fragment = fragmentManager.findFragmentByTag(getFragmentTag(viewpager.getId(), tabIndex));
            if (fragment != null) {
                return fragment;
            }
            return onGenerateFragment(tabIndex);
        }
        return null;
    }

    /**
     * 具体是生成哪个Fragment
     * @param tabIndex tab下标
     * @return
     */
    protected abstract BaseAppFragment onGenerateFragment(int tabIndex);
    private void generateAllFragment() {
        tabCount = tabCount();
        if(tabCount<1){
            throw new IllegalArgumentException("the return value from tabCount() must be larger than 0");
        }
        for(int index=0;index<tabCount;index++){
            Fragment fragment=generateFragment(index);
            if(fragment!=null){
                fragmentList.add(fragment);
            }
        }
    }

    /**
     * tab个数
     * @return 没做返回值异常检测
     */
    protected abstract int tabCount();
    /**
     * 子类可以调用该函数来触发tab切换
     * @param tabIndex tab下标，没做异常检测
     */
    protected final  void trggierTabEvent(int tabIndex){
        //
        currentTab = tabIndex;
        viewpager.setCurrentItem(currentTab, false);
        onTabChanged(tabIndex);
    }

    /**
     * 子类可以覆盖这个方法,可以做一些tab切换的操作
     * @param tabIndex tab下标 ，没做异常检测
     */
    protected abstract void onTabChanged(int tabIndex);

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentTab = position;
        onTabChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 主要用来动态添加一个item,
     */
    protected void doAddItem(){
        tabCount = tabCount+1;
        Fragment fragment = generateFragment(tabCount-1);
        if(fragment!=null){
            fragmentList.add(fragment);
            if(pagerAdapter!=null){
                pagerAdapter.notifyDataSetChanged();
            }
        }
    }
    /**
     * PagerAdapter的适配器
     */
    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        /**
         * 构造器
         *
         * @param fm fragment管理器
         */
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
}
