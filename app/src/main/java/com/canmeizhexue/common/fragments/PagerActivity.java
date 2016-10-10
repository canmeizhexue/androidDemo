package com.canmeizhexue.common.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by silence on 2016-10-10.
 */
public class PagerActivity extends BaseActivity {
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        List<Fragment> fragments = new ArrayList<>();
        for(int i=0;i<6;i++){
            Fragment fragment = TextFragment.getInstance("第"+i+"页");
            fragments.add(fragment);
        }
        ButterKnife.bind(this);
        viewpager.setAdapter(new MyAdapter(getSupportFragmentManager(),fragments));
    }
//    如文档所述，该类内的每一个生成的 Fragment 都将保存在内存之中，因此适用于那些相对静态的页，数量也比较少的那种；如果需要处理有很多页，并且数据动态性较大、占用内存较多的情况，应该使用FragmentStatePagerAdapter。
    //适用于页面少，且相对静态，

    //内部执行的是fragment的attach和detach，，虽然会销毁view树，但是不会销毁fragment
    static class MyAdapter extends FragmentPagerAdapter {
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
/*        函数中判断一下要生成的 Fragment 是否已经生成过了，如果生成过了，就使用旧的，旧的将被 Fragment.attach()；如果没有，就调用 getItem() 生成一个新的，新的对象将被 FragmentTransation.add()。
        FragmentPagerAdapter 会将所有生成的 Fragment 对象通过 FragmentManager 保存起来备用，以后需要该 Fragment 时，都会从 FragmentManager 读取，而不会再次调用 getItem() 方法。*/
        return super.instantiateItem(container, position);
    }

    @Override
        public int getItemPosition(Object object) {
            // 这个很重要，默认返回是POSITION_UNCHANGED，viewpager内部会用这个来判断是否需要销毁fragment

//            在 PagerAdapter 中的实现是直接传回 POSITION_UNCHANGED。如果该函数不被重载，则会一直返回 POSITION_UNCHANGED，从而导致 ViewPager.dataSetChanged() 被调用时，认为不必触发 PagerAdapter.instantiateItem()。
            return super.getItemPosition(object);
        }
    }
}
