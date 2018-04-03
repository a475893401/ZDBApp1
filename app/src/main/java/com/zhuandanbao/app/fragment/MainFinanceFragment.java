package com.zhuandanbao.app.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.BackListActivity;
import com.zhuandanbao.app.activity.RechargeActivity;
import com.zhuandanbao.app.activity.TradeLogActivity;
import com.zhuandanbao.app.activity.WithdrawActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.FinanceEntity;
import com.zhuandanbao.app.entity.ShopItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 财务
 * Created by BFTECH on 2017/3/2.
 */

public class MainFinanceFragment  extends BaseListViewFragment<ShopD> {
    private RvBaseAdapter adapter;
    private List<ShopItemEntity> shopList;
    private View topView;
    private FinanceEntity financeEntity;

    public static MainFinanceFragment getInstance() {
        return new MainFinanceFragment();
    }
    @Override
    protected void initD() {
        isFristAddData=true;
        loadingId=2;
        getFinanceInfo();
    }

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }


    @Override
    public void initView() {
        super.initView();
        getShopList();
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        adapter=new RvBaseAdapter(context, shopList, R.layout.item_main_shop_info_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                ShopItemEntity info= (ShopItemEntity) item;
                holder.setImageResource(R.id.item_shop_img, info.img);
                holder.setText(R.id.item_shop_title, info.title);
                holder.setVisible(R.id.item_shop_num, false);
                holder.setVisible(R.id.item_shop_view, false);
            }
        });
        adapter.addHeaderView(getTopView());
        initVerticalRecyclerView(adapter,true,false,1,0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                switch (position){
                    case 0:
                        viewDelegate.goToActivity(null, RechargeActivity.class,0);
                        break;
                    case 1:
                        SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                        skipInfoEntity.data=financeEntity;
                        viewDelegate.goToActivity(skipInfoEntity, WithdrawActivity.class,0);
                        break;
                    case 2:
                        viewDelegate.goToActivity(null, TradeLogActivity.class,0);
                        break;
                    case 3:
                        viewDelegate.goToActivity(null, BackListActivity.class,0);
                        break;
                }
            }
        });
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            financeEntity= MJsonUtils.jsonToFinanceEntity(baseEntity.data);
            TextView balance= ViewHolder.get(topView,R.id.finance_info_balance);
            TextView finance_info_credit_money=ViewHolder.get(topView,R.id.finance_info_credit_money);
            TextView finance_info_trading_fund=ViewHolder.get(topView,R.id.finance_info_trading_fund);
            TextView finance_info_frozen_fund=ViewHolder.get(topView,R.id.finance_info_frozen_fund);
            balance.setText(financeEntity.getAccount_balance());
            finance_info_credit_money.setText(financeEntity.getCredit_money());
            finance_info_trading_fund.setText(financeEntity.getTrading_fund());
            finance_info_frozen_fund.setText(financeEntity.getAvailable_balance());
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getFinanceInfo();
    }

    /**
     * 创建shop列表信息
     *
     * @return
     */
    private List<ShopItemEntity> getShopList() {
        if (null != shopList) {
            shopList.clear();
        } else {
            shopList = new ArrayList<>();
        }
        shopList.add(new ShopItemEntity(R.mipmap.img_recharge, "充值", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_withdraw, "提现", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_jxjl, "交易记录", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_txzh, "提现账户", 0, true));
        return shopList;
    }

    private View getTopView(){
        topView= LayoutInflater.from(context).inflate(R.layout.item_finance_top_layout,null);
        return topView;
    }


    /**
     * 获取财务信息API_GET_FINANCE_INFO
     */
    private void getFinanceInfo(){
        requestId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null,true,Constants.API_GET_FINANCE_INFO,params);
    }
}
