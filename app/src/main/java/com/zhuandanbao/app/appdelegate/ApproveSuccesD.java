package com.zhuandanbao.app.appdelegate;

import android.view.View;
import android.widget.TextView;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.mvp.view.BaseDelegate;
import com.zhuandanbao.app.utils.ViewHolder;

/**
 * Created by BFTECH on 2017/2/21.
 */

public class ApproveSuccesD extends BaseDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_approve_success;
    }

    public String getTextStr(View view,int id) {
        TextView textView = ViewHolder.get(view,id);
        if (null != textView) {
            return textView.getText().toString().trim();
        }
        return null;
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
