package com.canmeizhexue.common.emojiexpression;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.canmeizhexue.common.R;


public class EditTextActivity extends AppCompatActivity {

    private EditText et_emotion; //编辑器
    private EmotionMainFragment emotionMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_edittext);
        initView();
        initListentener();
        initDatas();
    }

    /**
     * 初始化控件
     */
    private void initView()
    {
        et_emotion= (EditText) findViewById(R.id.et_emotion);
        et_emotion.setFocusable(true);
        et_emotion.setFocusableInTouchMode(true);
        et_emotion.requestFocus();
        InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        im.showSoftInput(et_emotion, 0);
    }

    /**
     * 初始化监听器
     */
    public void initListentener(){

    }

    /**
     * 初始化布局数据
     */
    private void initDatas(){
        initEmotionMainFragment();
    }

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment(){

        //替换fragment
        //创建修改实例
        emotionMainFragment = (EmotionMainFragment) EmotionMainFragment.getInstance(true,false);
        emotionMainFragment.bindToContentView(et_emotion);
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        // Replace whatever is in thefragment_container view with this fragment,
        // and add the transaction to the backstack
        transaction.replace(R.id.fl_emotionview_main,emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        /**
         * 判断是否拦截返回键操作
         */
        if (!emotionMainFragment.isInterceptBackPress()) {
            super.onBackPressed();
        }
    }
}
