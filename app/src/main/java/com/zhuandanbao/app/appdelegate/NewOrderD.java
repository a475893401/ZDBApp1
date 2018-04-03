package com.zhuandanbao.app.appdelegate;

import android.view.View;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.mvp.view.BaseDelegate;

/**
 * Created by BFTECH on 2017/3/8.
 */

public class NewOrderD extends BaseDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_bill_new_layout;
    }

    /**
     * 强制获取焦点
     * @param view
     */
    public void setViewFocusable(final View view){
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.clearFocus();
                view.requestFocus();
            }
        },500);
    }
}
