package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;

import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopMsgListEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 转单宝动态
 * Created by BFTECH on 2017/2/28.
 */

public class ZdbMsgActivity  extends BaseListViewActivity<ShopD> {
    private List<ShopMsgListEntity> list=new ArrayList<>();
    private RvBaseAdapter adapter;
    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("转单宝动态");
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        adapter=new RvBaseAdapter(context, list,R.layout.item_shop_msg, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                ShopMsgListEntity info= (ShopMsgListEntity) item;
                holder.setText(R.id.msg_content,info.title);
                holder.setText(R.id.msg_time, MUtils.getTo66TimePHP(info.time,"yyyy-MM-dd hh:mm"));
            }
        });
        initVerticalRecyclerView(adapter,true,true,1,0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                ShopMsgListEntity info= (ShopMsgListEntity) item;
                SkipInfoEntity indexEntity=new SkipInfoEntity();
                indexEntity.pushId=info.aid;
                indexEntity.isPush=false;
                viewDelegate.goToActivity(indexEntity,ZdbMsgInfoActivity.class,0);
            }
        });
        loadingId=2;
        getMsgList();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            if (page==1){
                list.clear();
            }
            List<ShopMsgListEntity> itemList= MJsonUtils.jsonToShopMsgListEntity(baseEntity.data);
            list.addAll(itemList);
            completeRefresh(itemList.size(),list.size(),false);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getMsgList();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page=(list.size()/page_size)+1;
        getMsgList();
    }

    /**
     * 获取转单宝动态
     */
    private void getMsgList(){
        requestId=1;
        HttpParams params=new HttpParams();
        params.put("page",page);
        params.put("page_size",page_size);
        baseHttp(null,true, Constants.API_GET_ZDB_MSG_LIST,params);
    }
}
