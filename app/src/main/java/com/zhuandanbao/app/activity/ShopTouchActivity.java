package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopInfoD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopTouchItemEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺联系人
 * Created by BFTECH on 2017/2/23.
 */

public class ShopTouchActivity extends BaseListViewActivity<ShopInfoD> {

    private Button button;
    private List<ShopTouchItemEntity> list = new ArrayList<>();
    private RvBaseAdapter adapter;

    @Override
    protected Class<ShopInfoD> getDelegateClass() {
        return ShopInfoD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("店铺联系人");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        button = viewDelegate.get(R.id.but_blue_hg);
        button.setText("+ 新增联系人");
        button.setOnClickListener(this);
        adapter = new RvBaseAdapter(this, list, R.layout.item_shop_touch, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                ShopTouchItemEntity info = (ShopTouchItemEntity) item;
                holder.setText(R.id.shop_touch_name, info.getUser());
                holder.setText(R.id.shop_touch_phone, info.getMobile());
                holder.setText(R.id.shop_touch_jop, "职务：" + info.getType_name());
            }
        });
        initVerticalRecyclerView(adapter, true, false, 1, 0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                goToActivity((ShopTouchItemEntity) item);
            }
        });
        loadingId = 2;
        getShopTouch();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            list.clear();
            list.addAll(MJsonUtils.jsonToShopTouchItemEntity(baseEntity.data, null));
            completeRefreshBut(list.size(), "点击去添加~");
        }
    }

    @Override
    protected void emptyOnclick() {
        super.emptyOnclick();
        goToActivity(null);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        loadingId = 0;
        getShopTouch();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_blue_hg) {
            goToActivity(null);
        }
    }

    private void goToActivity(ShopTouchItemEntity info) {
        Intent intent = new Intent(context, UpdataTouchActivity.class);
        if (null != info) {
            intent.putExtra("touch", info);
        }
        startActivityForResult(intent, 200);
    }

    /**
     * 获取联系人
     */
    private void getShopTouch() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_ACTION_SHOP_CONTACTS, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Constants.SHOP_GUIDE_CONTACT_SUCCESS) {
            loadingId = 2;
            getShopTouch();
        }
    }
}
