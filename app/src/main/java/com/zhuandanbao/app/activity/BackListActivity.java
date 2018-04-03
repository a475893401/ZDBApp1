package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ApproveSuccesD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.BackListEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现银行卡
 * Created by BFTECH on 2017/3/14.
 */

public class BackListActivity extends BaseListViewActivity<ApproveSuccesD> {

    private List<BackListEntity> listEntities = new ArrayList<>();
    private RvBaseAdapter adapter;
    private Button button;

    @Override
    protected Class<ApproveSuccesD> getDelegateClass() {
        return ApproveSuccesD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("提现银行卡");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        button = viewDelegate.get(R.id.but_ok);
        button.setText(" + 添加");
        button.setOnClickListener(this);
        adapter = new RvBaseAdapter(context, listEntities, R.layout.item_back_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                BackListEntity info = (BackListEntity) item;
                holder.setText(R.id.item_back_name, info.getAccount());
                holder.setText(R.id.item_back_content, info.getName() + "(" + info.getBank_card_num().substring(info.getBank_card_num().length() - 4, info.getBank_card_num().length()) + ")");
                ImageView img = holder.getView(R.id.item_back_img);
                ImageLoader.getInstance().displayImage(info.getLogo(), img, MyImage.deployMemory());
                holder.setVisible(R.id.go_back,true);
            }
        });
        initVerticalRecyclerView(adapter, true, false, 1, 0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                BackListEntity info = (BackListEntity) item;
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.index = 1;
                skipInfoEntity.data = info;
                viewDelegate.goToActivity(skipInfoEntity, AddBackActivity.class, 300);
            }
        });
        getBack();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_ok) {
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            skipInfoEntity.index = 2;
            viewDelegate.goToActivity(skipInfoEntity, AddBackActivity.class, 300);
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            listEntities.clear();
            listEntities.addAll(MJsonUtils.jsonToBackListEntity(baseEntity.data));
            completeRefreshBut(listEntities.size(),"点击去添加~");
        }
    }

    @Override
    protected void emptyOnclick() {
        super.emptyOnclick();
        SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
        skipInfoEntity.index = 2;
        viewDelegate.goToActivity(skipInfoEntity, AddBackActivity.class, 300);
    }

    /**
     * 获取用户已添加的银行卡
     */
    private void getBack() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_GET_FINANCE_ADD_BANK_CARD_LISTS, params);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getBack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == 6666) {
            loadingId = 2;
            getBack();
        }
    }
}
