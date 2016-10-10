package com.canmeizhexue.common.emojiexpression;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.utils.LogUtils;


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


        //改变字体颜色
        //先构造SpannableString
        SpannableString spanString = new SpannableString("欢迎光临测试span");
        //再构造一个改变字体颜色的Span
        ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
        //将这个Span应用于指定范围的字体
        // 标记位表示的是在起始点开结束点插入字符时，这个span是否还起作用，可以主动在起始点和结束点插入字符，来测试几种不同的标记位
        spanString.setSpan(span, 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置给EditText显示出来
        et_emotion.setText(spanString);

        et_emotion.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这个表明文字改变时，相应的span位置也会随着改变，而不是固定在特定的位置
                ForegroundColorSpan[] foregroundColorSpen = et_emotion.getText().getSpans(0,et_emotion.length(),ForegroundColorSpan.class);
                if(foregroundColorSpen!=null && foregroundColorSpen.length>0){
                    for(int i=0;i<foregroundColorSpen.length;i++){
                        ForegroundColorSpan span1 = foregroundColorSpen[i];
                        int start=et_emotion.getText().getSpanStart(span1);
                        int end = et_emotion.getText().getSpanEnd(span1);
                        int flag = et_emotion.getText().getSpanFlags(span1);
                        LogUtils.d("silence","---start,end,flag-----"+start+","+end+","+flag);
                    }
                }
            }
        },5000);
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
