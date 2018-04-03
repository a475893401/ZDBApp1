package com.zhuandanbao.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.model.HttpParams;
import com.mrzk.generatepicturelibrary.GeneratePictureView;
import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.LjListViewD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopMsgInfoEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态详情
 * Created by BFTECH on 2017/2/28.
 */

public class ZdbMsgInfoActivity extends BaseListViewActivity<LjListViewD> {
    private List<ShopMsgInfoEntity> list = new ArrayList<>();
    private SkipInfoEntity indexEntity;
    private ShopMsgInfoEntity msgInfoEntity;

    private ProgressDialog pd;

    @Override
    protected Class<LjListViewD> getDelegateClass() {
        return LjListViewD.class;
    }

    private Handler mHandler = new Handler_Capture(this);

    class Handler_Capture extends Handler {
        WeakReference<Activity> weakReference;

        public Handler_Capture(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null != weakReference.get()) {
                switch (msg.what) {
                    case 1001:
                        pd.dismiss();
                        list.clear();
                        list.add(msgInfoEntity);
                        LJcompleteRefresh();
                        nBaseAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            indexEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        } else {
            finish();
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexEntity.isPush){
                    SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                    skipInfoEntity.index=Constants.GRAB_ORDER;
                    viewDelegate.goToActivity(skipInfoEntity, MainActivity.class,0);
                    finish();
                }else {
                    finish();
                }
            }
        });
        viewDelegate.setTitle("动态详情");
        pd = new ProgressDialog(this);
        pd.setMessage("请稍后...");
        pd.show();
        baseListview = viewDelegate.get(R.id.lj_listview);
        nBaseAdapter = setBaseAdapter(list);
        baseListview.setAdapter(nBaseAdapter);
        setBaseListview(true, false);
        getMsgInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            msgInfoEntity = MJsonUtils.jsonToShopMsgInfoEntity(baseEntity.data);
            mHandler.sendEmptyMessage(1001);
        }
    }


    @Override
    public void onlJRefresh() {
        super.onlJRefresh();
        getMsgInfo();
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = LayoutInflater.from(this).inflate(R.layout.activity_ad_info_layout, null);
        }
        GeneratePictureView webView = ViewHolder.get(view, R.id.gpv);
        webView.init(msgInfoEntity.content, msgInfoEntity.title, "转单宝", MUtils.getTo66TimePHP(msgInfoEntity.time, "yyyy-MM-dd"));
        return view;
    }


    /**
     * 获取动态详情
     */
    private void getMsgInfo() {
        requestId = 1;
        loadingId=0;
        HttpParams params = new HttpParams();
        params.put("art_id", indexEntity.pushId);
        baseHttp(null, true, Constants.API_GET_ZDB_MSG_LIST_INFO, params);
    }
}
