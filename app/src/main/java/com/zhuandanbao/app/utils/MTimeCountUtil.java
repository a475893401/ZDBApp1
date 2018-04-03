package com.zhuandanbao.app.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;


public class MTimeCountUtil extends CountDownTimer {

    private Activity mActivity;
    private TextView showTime;//按钮

    //millisInFuture:总的时间
    public MTimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, TextView showTime) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.showTime = showTime;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        MLog.e("millisUntilFinished="+millisUntilFinished);
        int day = (int)(millisUntilFinished / (1000 * 60 * 60 * 24));
        int hour = (int)((millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        int minute = (int)((millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60));
        int second = (int)((millisUntilFinished % (1000 * 60)) / 1000);
        int millisecond = (int)(millisUntilFinished % 1000);
        setTimes(day,hour,minute,second,millisecond);
    }

    @Override
    public void onFinish() {
        cancel();
        setTimes(0,0,0,0,0);
    }

    private void setTimes(int day, int hour, int minute, int second, int millisecond){
        if (millisecond>10) {
            String time = String.valueOf(millisecond);
            showTime.setText(String.format("%02d : %02d ", minute, second) + " : " + time.charAt(0)+time.charAt(1));
        }else if (minute==0&&millisecond==0&&second==0){
            showTime.setText(minute+" : "+second+" : "+millisecond);
        }
    }
}
