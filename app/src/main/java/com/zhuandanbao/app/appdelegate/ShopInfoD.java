package com.zhuandanbao.app.appdelegate;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.mvp.view.BaseDelegate;

/**
 * Created by BFTECH on 2017/2/22.
 */

public class ShopInfoD extends BaseDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_shop_info;
    }

    /**
     * 不可点击和改变字体颜色
     *
     * @param editText
     */
    public void noClick(EditText editText, boolean isClick) {
        int colorId = Color.parseColor("#999999");
        editText.setEnabled(isClick);
        if (!isClick) {
            editText.setTextColor(colorId);
        }
    }

    public void noClick(TextView editText, boolean isClick) {
        int colorId = Color.parseColor("#999999");
        editText.setEnabled(isClick);
        if (!isClick) {
            editText.setTextColor(colorId);
        }
    }
}
