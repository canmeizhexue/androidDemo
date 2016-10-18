package com.canmeizhexue.common.guidepage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canmeizhexue.common.BuildConfig;
import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;
import com.canmeizhexue.common.emojiexpression.EmojiIndicatorView;
import com.canmeizhexue.common.helper.CommonSharedPreference;
import com.canmeizhexue.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>GuidePageActivity类 引导页</p>
 *
 * @author silence
 * @version 1.0 (2016-10-18 15:01)
 */
public class GuidePageActivity extends BaseActivity implements ViewPager.OnPageChangeListener ,View.OnClickListener{
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.indicator)
    EmojiIndicatorView indicator;

    //适配器
    private GuidePageAdapter guidePageAdapter;
    //引导页视图
    private final List<View> views = new ArrayList<>();
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private final int[]imgRes = {R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3,R.mipmap.guide_4};
    int oldPagerPos=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        ButterKnife.bind(this);


        LayoutInflater inflater = LayoutInflater.from(this);
        for(int i=0;i<imgRes.length;i++){
            //初始化引导页列表
            views.add(inflater.inflate(R.layout.item_welcome_content, null));
        }

        guidePageAdapter = new GuidePageAdapter(views);
        viewpager.setAdapter(guidePageAdapter);
        viewpager.addOnPageChangeListener(this);

        //初始化指示器
        indicator.initIndicator(imgRes.length);

       SharedPreferences.Editor editor= CommonSharedPreference.getCommonSharedPreference(this).edit();
        editor.putString(CommonSharedPreference.STRING_LAST_VERSION_NAME, BuildConfig.VERSION_NAME);
        editor.putBoolean(CommonSharedPreference.BOOLEAN_IS_FIRST_USAGE,false);
        editor.apply();
        toPage(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        toPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private void toPage(int newIndex){
        View currentView = views.get(newIndex);
        ImageView guideImage = (ImageView) currentView.findViewById(R.id.guide_image);
        View btn = currentView.findViewById(R.id.toMain);
        btn.setOnClickListener(this);
        //////////////////////////////////////////////////////////////////
        Glide.with(this).load(imgRes[newIndex]).asBitmap().centerCrop().into(guideImage);
        if(imgRes.length-1==newIndex){
            // 最后一页
            btn.setVisibility(View.VISIBLE);
        }else{
            btn.setVisibility(View.GONE);
        }
        indicator.playByStartPointToNext(oldPagerPos,newIndex);
        oldPagerPos = newIndex;
    }

    @Override
    public void onClick(View v) {
        /////////////////////////////////////////////////////////////////////////////////////////
        Toast.makeText(this,"我要到主页啦啦啦",Toast.LENGTH_LONG).show();
        LogUtils.d("silence","------GuidePage--onClick");
    }

    static class GuidePageAdapter extends PagerAdapter{

        //界面列表
        private List<View> views;

        /**
         * 构造器
         * @param views 视图实体集
         */
        public GuidePageAdapter(List<View> views){
            this.views = views;
        }

        //销毁arg1位置的界面
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        //获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }

            return 0;
        }

        //初始化arg1位置的界面
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        //判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

    }
}
