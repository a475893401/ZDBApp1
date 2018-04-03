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
import com.zhuandanbao.app.entity.AdInfoEntity;
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
 * 资讯详情
 * Created by BFTECH on 2017/3/1.
 */

public class ZdbInfoActivity extends BaseListViewActivity<LjListViewD> {

    private SkipInfoEntity skipInfoEntity;
    private AdInfoEntity adInfoEntity;
    private ProgressDialog pd;
    private List<AdInfoEntity> list = new ArrayList<>();

    private Handler mHandler = new Handler_Capture(this);

    class Handler_Capture extends Handler {
        WeakReference<Activity> weakReference;

        public Handler_Capture(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get() != null) {
                switch (msg.what) {
                    case 1001:
                        pd.dismiss();
                        list.clear();
                        list.add(adInfoEntity);
                        LJcompleteRefresh();
                        nBaseAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }

    @Override
    protected Class<LjListViewD> getDelegateClass() {
        return LjListViewD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (skipInfoEntity.isPush){
                    SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                    skipInfoEntity.index=Constants.GRAB_ORDER;
                    viewDelegate.goToActivity(skipInfoEntity, MainActivity.class,0);
                    finish();
                }else {
                    finish();
                }
            }
        });
        viewDelegate.setTitle("资讯详情");
        pd = new ProgressDialog(this);
        pd.setMessage("请稍后...");
        pd.show();
        baseListview = viewDelegate.get(R.id.lj_listview);
        nBaseAdapter = setBaseAdapter(list);
        baseListview.setAdapter(nBaseAdapter);
        setBaseListview(true, false);
        loadingId = 0;
        getZdbInfo();
    }

    @Override
    public void onlJRefresh() {
        super.onRefresh();
        loadingId = 0;
        getZdbInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            adInfoEntity = MJsonUtils.jsonToAdInfoEntity(baseEntity.data);
            mHandler.sendEmptyMessage(1001);
        }
    }

    @Override
    public View getItemView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = LayoutInflater.from(this).inflate(R.layout.activity_ad_info_layout, null);
        }
        GeneratePictureView webView = ViewHolder.get(view, R.id.gpv);
        webView.init(adInfoEntity.content, adInfoEntity.title, adInfoEntity.source, MUtils.getTo66TimePHP(adInfoEntity.time, "yyyy-MM-dd"));
        return view;
    }

    /**
     * 获取资讯详情
     */
    private void getZdbInfo() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put("art_id", skipInfoEntity.pushId);
        baseHttp(null, true, Constants.API_ACTION_INFO_CONTENT, params);
    }
}
