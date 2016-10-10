package com.canmeizhexue.common.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by silence on 2016-10-10.
 */
public class StatePagerActivity extends BaseActivity {
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        List<Fragment>fragments = new ArrayList<>();
        for(int i=0;i<6;i++){
            Fragment fragment = TextFragment.getInstance("第"+i+"页");
            fragments.add(fragment);
        }
        ButterKnife.bind(this);
        viewpager.setAdapter(new MyAdapter(getSupportFragmentManager(),fragments));
    }
    //使用数据动态变化大，页面很多，占用内存大的页面,会销毁fragment对象
    static class MyAdapter extends FragmentStatePagerAdapter{
        List<Fragment> fragments;

        public MyAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
