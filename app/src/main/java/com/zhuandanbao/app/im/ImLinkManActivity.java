package com.zhuandanbao.app.im;

import android.os.Bundle;
import android.view.View;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.mvp.BaseHttpActivity;


/**
 * Created by BFTECH on 2016/12/16.
 */
public class ImLinkManActivity extends BaseHttpActivity<LinkManD> {
    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ImLoginHelper.getInstance().getIMKit().getContactsFragment())
                .commit();
    }

    @Override
    protected Class<LinkManD> getDelegateClass() {
        return LinkManD.class;
    }

    @Override
    public void onClick(View view) {

    }
}
