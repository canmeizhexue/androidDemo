package com.canmeizhexue.common.utils;

import android.os.CountDownTimer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**倒计时帮助类
 * Created by canmeizhexue on 2016-8-15.
 */
public class CountDownHelper {
    private CountDownTimer countDownTimer;
    private String timeFormater;
    private OnCountDownEventListener onCountDownEventListener;
    private Calendar calendar;
    /**
     * 默认的时间格式化串
     */
    public static final String DEFAULT_TIME_FORMATER="HH:mm:ss";
    private SimpleDateFormat simpleDateFormat;

    /**
     * 钩子函数
     * @param millisInFuture 计数的总毫秒数
     * @param countDownInterval 计数更新间隔，毫秒数为单位
     * @param timeFormater 格式化字符串比如"HH:mm:ss"
     * @param listener 回调接口
     */
    public CountDownHelper(long millisInFuture, long countDownInterval, String timeFormater, OnCountDownEventListener listener){
        this.timeFormater = timeFormater;
        simpleDateFormat = new SimpleDateFormat(timeFormater);
        this.onCountDownEventListener = listener;
        resetBaseTime();
        countDownTimer = new CountDownTimer(millisInFuture,countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(onCountDownEventListener!=null){
                    onCountDownEventListener.onTick(CountDownHelper.this,formatTime(millisUntilFinished),millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if(onCountDownEventListener!=null){
                    onCountDownEventListener.onFinish(CountDownHelper.this);
                }
            }
        };
    }

    /**
     * 开始倒计时
     */
    public void start(){
        countDownTimer.start();
    }

    /**
     * 取消倒计时
     */
    public void cancel(){
        countDownTimer.cancel();
    }
    private String formatTime(long millisUntilFinished){
        resetBaseTime();
        calendar.setTimeInMillis(calendar.getTimeInMillis()+millisUntilFinished);
        return simpleDateFormat.format(calendar.getTime());
    }
    private void resetBaseTime(){
        if(calendar==null){
            calendar = Calendar.getInstance();
        }
        calendar.set(2000,1,1,0,0,0);
    }

    /**
     * 倒计时事件监听
     */
    public interface OnCountDownEventListener{
        /**
         * 倒计时事件
         * @param countDownHelper 倒计时帮助类
         * @param timeFormated 格式化后的时间字符串
         * @param millisUntilFinished 距离倒计时结束还剩多少时间
         */
        void onTick(CountDownHelper countDownHelper, String timeFormated, long millisUntilFinished);

        /**
         * 倒计时结束
         * @param countDownHelper 倒计时帮助类
         */
        void onFinish(CountDownHelper countDownHelper);
    }
}
