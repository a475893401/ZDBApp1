package com.zhuandanbao.app.activity;

import android.os.Bundle;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ApproveHint1D;
import com.zhuandanbao.app.mvp.BaseHttpActivity;

/**
 * 上传要求说明
 * Created by BFTECH on 2017/3/29.
 */

public class ApproveHint1Activity extends BaseHttpActivity<ApproveHint1D> {
    @Override
    protected Class<ApproveHint1D> getDelegateClass() {
        return ApproveHint1D.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("上传要求说明");
    }
}
