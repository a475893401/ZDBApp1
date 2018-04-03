package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ApproveHintD;
import com.zhuandanbao.app.mvp.BaseHttpActivity;

/**
 * 身份证照片示例
 * Created by BFTECH on 2017/2/22.
 */

public class ApproveHintActivity  extends BaseHttpActivity<ApproveHintD> {

    @Override
    protected Class<ApproveHintD> getDelegateClass() {
        return ApproveHintD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("身份证照片示例");
        viewDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        },R.id.finish);
    }
}
