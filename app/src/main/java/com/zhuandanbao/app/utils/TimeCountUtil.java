package com.zhuandanbao.app.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.zhuandanbao.app.R;

public class TimeCountUtil extends CountDownTimer {

    private Activity mActivity;
    private TextView btn;//按钮
    //millisInFuture:总的时间
    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, TextView btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText(millisUntilFinished / 1000 + "秒后可重新获取");//设置倒计时时间
        btn.setBackgroundColor(mActivity.getResources().getColor(R.color.sms_code_color));//设置灰色,不能点击
        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
        span.setSpan(new ForegroundColorSpan(Color.RED), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
        btn.setText(span);
    }

    @Override
    public void onFinish() {
        btn.setText("获取验证码");
        btn.setClickable(true);//重新获得点击
        btn.setBackgroundColor(mActivity.getResources().getColor(R.color.back_bg_color));//还原背景色
    }
}
