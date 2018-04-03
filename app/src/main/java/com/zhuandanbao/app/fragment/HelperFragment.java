package com.zhuandanbao.app.fragment;

import android.os.Bundle;
import android.view.View;

import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.ZdbHelpInfoActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.HelpListEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮助中心
 * Created by BFTECH on 2017/4/10.
 */

public class HelperFragment  extends BaseListViewFragment<ShopD> {
    private List<HelpListEntity> list=new ArrayList<>();
    private RvBaseAdapter adapter;

    public static HelperFragment getInstance(String fragmentId) {
        HelperFragment fragment = new HelperFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_INDEX, fragmentId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        adapter=new RvBaseAdapter(context, list, R.layout.item_help_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                HelpListEntity info= (HelpListEntity) item;
                holder.setText(R.id.item_content,info.getTitle());
            }
        });
        initVerticalRecyclerView(adapter,true,false,1,0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                HelpListEntity info= (HelpListEntity) item;
                SkipInfoEntity infoEntity=new SkipInfoEntity();
                infoEntity.pushId=info.getAid();
                infoEntity.isPush=false;
                viewDelegate.goToActivity(infoEntity, ZdbHelpInfoActivity.class,0);
            }
        });
    }

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    protected void initD() {
        loadingId=2;
        getHelpList();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            if (page==1){
                list.clear();
            }
            List<HelpListEntity> itemList=MJsonUtils.personHelpListEntity(baseEntity.data);
            list.addAll(MJsonUtils.personHelpListEntity(baseEntity.data));
            completeRefresh(itemList.size(),list.size(),false);
            if (list.size()<10){
                superRecyclerView.setNoMore(false);
            }
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getHelpList();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page=(list.size()/page_size)+1;
        getHelpList();
    }

    private void getHelpList(){
        requestId=1;
        HttpParams params=new HttpParams();
        params.put("page",page);
        params.put("page_size",page_size);
        params.put("categoryId",getArguments().getString(FRAGMENT_INDEX));
        baseHttp(null,true, Constants.API_HELP_LIST,params);
    }
}
