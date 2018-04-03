package com.zhuandanbao.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.SalesInfoActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SaleListEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我申请的售后列表
 * Created by BFTECH on 2017/3/2.
 */

public class MyApplySaleListFragment extends BaseListViewFragment<ShopD> {

    private List<SaleListEntity> listEntities=new ArrayList<>();
    private RvBaseAdapter adapter;
    private static final String[] ordersTypeArray = {"pending", "overrule", "arbitrations", "finish"};

    public static MyApplySaleListFragment getInstance(int indexFragment) {
        MyApplySaleListFragment fragment = new MyApplySaleListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, indexFragment);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected void initD() {
        fragmentIndex=getArguments().getInt(FRAGMENT_INDEX);
        page=1;
        tag=ordersTypeArray[fragmentIndex];
        isFristAddData=true;
        getSaleList();
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        adapter=new RvBaseAdapter(context, listEntities, R.layout.item_sale_list_layout, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                SaleListEntity info= (SaleListEntity) item;
                holder.setText(R.id.order_sn,info.getOrder_sn());
                holder.setText(R.id.order_status_tx,info.getReturn_status_remark());
                holder.setText(R.id.order_amount, Html.fromHtml(StrUtils.setHtml999(null,"交易金额：","￥ "+info.getOrder_amount())));
                holder.setText(R.id.order_return_amount, Html.fromHtml(StrUtils.setHtml999(null,"退款金额：","￥ "+info.getReturn_amount())));
                holder.setText(R.id.order_shop_name, "您 因");
                holder.setText(R.id.order_content, info.getReturn_reason());
                if (info.getReturn_mode().equals("1")){
                    holder.setText(R.id.order_sale_type, Html.fromHtml(StrUtils.setHtml999(null, "向“"+info.getShop_name()+"”申请：", "全额退款")));
                }else if (info.getReturn_mode().equals("2")){
                    holder.setText(R.id.order_sale_type, Html.fromHtml(StrUtils.setHtml999(null, "向“"+info.getShop_name()+"”申请：", "部分退款")));
                }else {
                    holder.setText(R.id.order_sale_type, Html.fromHtml(StrUtils.setHtml999(null, "向“"+info.getShop_name()+"”申请：", "退一賠一")));
                }
            }
        });
        initVerticalRecyclerView(adapter,true,true,1,0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                SaleListEntity info= (SaleListEntity) item;
                SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                skipInfoEntity.pushId=info.getRid();
                skipInfoEntity.isPush=false;
                Intent intent=new Intent(activity,SalesInfoActivity.class);
                intent.putExtra(Constants.SKIP_INFO_ID,skipInfoEntity);
                startActivityForResult(intent,300);
            }
        });
    }

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getSaleList();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page=(listEntities.size()/page_size)+1;
        getSaleList();
    }


    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            if (page==1){
                listEntities.clear();
            }
            List<SaleListEntity> itemList= MJsonUtils.jsonToSaleListEntity(baseEntity.data);
            listEntities.addAll(MJsonUtils.jsonToSaleListEntity(baseEntity.data));
            completeRefresh(itemList.size(),listEntities.size(),false);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==300&&resultCode==6666){
            loadingId=0;
            getSaleList();
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        search_str = null;
    }

    /**
     * 搜索
     *
     * @param searchStr
     */
    public void search(String searchStr) {
        search_str = searchStr;
        loadingId=2;
        getSaleList();
    }


    /**
     * 获取售后详情
     */
    private void getSaleList() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("list_type", "apply");
        params.put("status", tag);
        params.put("page", page);
        params.put("page_size", page_size);
        if (StrUtils.isNotNull(search_str)) {
            params.put("q", search_str);
        }
        baseHttp(null,true,Constants.API_ORDER_SALE_LIST,params);
    }
}
