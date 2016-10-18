package com.canmeizhexue.common.guidepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.canmeizhexue.common.BuildConfig;
import com.canmeizhexue.common.R;
import com.canmeizhexue.common.base.BaseActivity;
import com.canmeizhexue.common.helper.CommonSharedPreference;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>SplashActivity类 概述，提供XXX功能</p>
 *
 * @author silence
 * @version 1.0 (2016-10-18 15:46)
 */
public class SplashActivity extends BaseActivity {
    @Bind(R.id.splash)
    ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Glide.with(this).load(R.mipmap.guide_1).asBitmap().fitCenter().into(splash);
        SharedPreferences sharedPreferences = CommonSharedPreference.getCommonSharedPreference(this);
        boolean isFirstUse =sharedPreferences .getBoolean(CommonSharedPreference.BOOLEAN_IS_FIRST_USAGE,true);
        if(isFirstUse){
            // 进入引导页
            toGuidePage();
        }else{
            String lastVersionName = sharedPreferences.getString(CommonSharedPreference.STRING_LAST_VERSION_NAME,"");
            if(BuildConfig.VERSION_NAME.equals(lastVersionName)){
                //进入主页
                toMainPage();
            }else{
                //进入引导页
                toGuidePage();
            }
        }
    }
    private void toGuidePage(){
        splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,GuidePageActivity.class));
                finish();
            }
        },2000);
    }
    private void toMainPage(){
        splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }
}
