package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ItemEntity;
import com.zhuandanbao.app.entity.ShopAuthInfoEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全设置
 * Created by BFTECH on 2017/2/24.
 */

public class SafeSettingActivity extends BaseListViewActivity<ShopD> {

    private List<ItemEntity> list = new ArrayList<>();
    private RvBaseAdapter adapter;
    private ShopAuthInfoEntity authInfoEntity = null;
    private SkipInfoEntity infoEntity;

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            infoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            authInfoEntity = MJsonUtils.jsonToShopAuthInfoEntity(infoEntity.msg);
        }
        setList();
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("安全设置");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, list, R.layout.item_safe_setting, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                ItemEntity info = (ItemEntity) item;
                holder.setText(R.id.name, info.name);
                if (StrUtils.isNotNull(info.content)) {
                    holder.setText(R.id.content, info.content);
                }
            }
        });
        initVerticalRecyclerView(adapter, false, false, 1, 0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                if (position == 0) {
                    viewDelegate.goToActivity(null, PayPasswordActivity.class, 0);
                }
                if (position == 1) {
                    viewDelegate.goToActivity(null, AmendMobileActivity.class, 200);
                }
                if (position == 2) {
                    viewDelegate.goToActivity(null,AmendPasswrodActivity.class,0);
                }
            }
        });
    }

    /**
     * 创建信息
     */
    private void setList() {
        if (null != list) {
            list.clear();
        }
        list.add(new ItemEntity("支付密码", null));
        String conetnt = null;
        if (null != authInfoEntity) {
            if (authInfoEntity.getAuth_mobile().equals("1")) {
                conetnt = "已绑定(" + StrUtils.setGetPhoneMask(infoEntity.mobile) + ")";
            } else {
                conetnt = null;
            }
        } else {
            conetnt = null;
        }
        list.add(new ItemEntity("手机绑定", conetnt));
        list.add(new ItemEntity("登录密码", null));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 300) {
            setResult(300);
            finish();
        }
    }
}
