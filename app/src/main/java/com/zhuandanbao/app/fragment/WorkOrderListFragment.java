package com.zhuandanbao.app.fragment;

import android.os.Bundle;
import android.view.View;

import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.WorkOrderInfoActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.WorkOrderEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.PerfHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单列表
 * Created by BFTECH on 2017/3/15.
 */

public class WorkOrderListFragment extends BaseListViewFragment<ShopD> {

    private static final String[] ordersTypeArray = {"IN_PROCESS", "FINISH"};
    private List<WorkOrderEntity> list=new ArrayList<>();
    private RvBaseAdapter adapter;

    public static WorkOrderListFragment getInstance(int indexFragment) {
        WorkOrderListFragment fragment = new WorkOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, indexFragment);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        adapter=new RvBaseAdapter(context, list, R.layout.item_work_order_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                WorkOrderEntity info= (WorkOrderEntity) item;
                holder.setText(R.id.order_sn, info.getSn());
                holder.setText(R.id.order_status,info.getStatus_remark());
                holder.setText(R.id.content,info.getTitle());
                holder.setText(R.id.time, MUtils.getTo66TimePHP(info.getTime(),"yyyy-MM-dd hh:ss"));
            }
        });
        initVerticalRecyclerView(adapter,true,true,1,0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                WorkOrderEntity info= (WorkOrderEntity) item;
                SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                skipInfoEntity.pushId=info.getSn();
                viewDelegate.goToActivity(skipInfoEntity, WorkOrderInfoActivity.class,0);
            }
        });
    }

    @Override
    protected void initD() {
        fragmentIndex=getArguments().getInt(FRAGMENT_INDEX);
        tag=ordersTypeArray[fragmentIndex];
        loadingId=2;
        getWorkList();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            if (page==1){
                list.clear();
            }
            List<WorkOrderEntity> itemList= MJsonUtils.josnToWorkOrderEntity(baseEntity.data);
            list.addAll(MJsonUtils.josnToWorkOrderEntity(baseEntity.data));
            completeRefresh(itemList.size(),list.size(),false);
        }
    }

    private void getWorkList(){
        requestId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("status_code", tag);
        params.put("page", page);
        params.put("page_size", page_size);
        baseHttp(null,true, Constants.API_TICKET_LIST,params);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getWorkList();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page=(list.size()/page_size)+1;
        getWorkList();
    }
}
