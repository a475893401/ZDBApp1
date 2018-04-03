package com.zhuandanbao.app.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.BiddingShopListActivity;
import com.zhuandanbao.app.activity.BillOrderInfoActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.BillListEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import app.zhuandanbao.com.reslib.widget.TimeTextView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 发单列表
 * Created by BFTECH on 2017/3/2.
 */

public class BillOrderListFragment extends BaseListViewFragment<ShopD> {
    private static final String[] ordersTypeArray = {"WAIT_CONFIRM", "PROCESSING", "HAS_BEEN_SERVED", "FINISHED", "ALL"};
    private RvBaseAdapter adapter;
    private List<BillListEntity> listEntities = new ArrayList<>();

    public static BillOrderListFragment getInstance(int indexFragment) {
        BillOrderListFragment fragment = new BillOrderListFragment();
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
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, listEntities, R.layout.item_bill_order_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                final BillListEntity billListEntity = (BillListEntity) item;
                MLog.e("Order_status==" + billListEntity.getOrder_status());
                TextView biddingNum = holder.getView(R.id.bidding_num);
                TimeTextView orderTime = holder.getView(R.id.item_order_time);
                BGABanner bgaBanner = holder.getView(R.id.item_banner);
                holder.setText(R.id.bill_order_sn, billListEntity.getOrder_sn());
                holder.setText(R.id.bill_order_status_tx, billListEntity.getOrder_status_remark());
                holder.setText(R.id.item_order_money, Html.fromHtml(StrUtils.setHtml999(null, "发单价格：", "￥ " + billListEntity.getOrder_amount())));
                holder.setText(R.id.item_order_name, Html.fromHtml(StrUtils.setHtml999(null, "收货信息：", billListEntity.getConsignee() + "(" + billListEntity.getMobile() + ")")));
                holder.setText(R.id.item_order_address, Html.fromHtml(StrUtils.setHtml999(null, "配送地址：", billListEntity.getAreainfo())));
                if (StrUtils.isNotNull(billListEntity.getGrab_order_bidding_num()) && !"0".equals(billListEntity.getGrab_order_bidding_num())
                        && billListEntity.getOrder_status().equals("WAIT_CONFIRM") && billListEntity.getOrder_type().equals("1")) {
                    biddingNum.setVisibility(View.VISIBLE);
                    biddingNum.setText(Html.fromHtml(StrUtils.setHtmlRed(null, billListEntity.getGrab_order_bidding_num(), "  家花店报价，立即选择店铺发单")));
                    biddingNum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                            skipInfoEntity.pushId = billListEntity.getOrder_sn();
                            viewDelegate.goToActivity(skipInfoEntity, BiddingShopListActivity.class, 0);
                        }
                    });
                } else {
                    biddingNum.setVisibility(View.GONE);
                }
                if (StrUtils.isNotNull(billListEntity.getJiedan_dianpu_name())) {
                    holder.setText(R.id.item_order_time, Html.fromHtml(StrUtils.setHtml999(null, "接单店铺：", billListEntity.getJiedan_dianpu_name())));
                }
                if (billListEntity.getOrder_type().equals("1")) {//抢单 待接单
                    holder.setImageResource(R.id.bill_order_img, R.mipmap.img_grab_order);
                    if (StrUtils.isNotNull(billListEntity.getJiedan_dianpu_name())) {
                        holder.setText(R.id.item_order_time, Html.fromHtml(StrUtils.setHtml999(null, "接单店铺：", billListEntity.getJiedan_dianpu_name())));
                    } else if (orderTime != null) {//截止时间
                        if (billListEntity.getOrder_status().equals("CANCELED")){
                            orderTime.setText("订单已取消");
                        }else {
                            Long now_time = System.currentTimeMillis() / 1000;


                            if(billListEntity.getGrab_order_deadline()!=null) {
                                long[] showTime = MUtils.callBacKTime(now_time, Long.parseLong(billListEntity.getGrab_order_deadline()));
                                orderTime.setTimes(showTime, null, "  后截止抢单");
                                orderTime.setShowText();
                            }
                        }
                        // 已经在倒计时的时候不再开启计时
                    }
                } else if (billListEntity.getOrder_type().equals("0") || billListEntity.getOrder_type().equals("2")) {// 0一对一 2 竞价
                    holder.setImageResource(R.id.bill_order_img, R.mipmap.img_un_grab_order);
                    holder.setText(R.id.item_order_time, Html.fromHtml(StrUtils.setHtml999(null, "接单店铺：", billListEntity.getJiedan_dianpu_name())));
                }
                List<OrderItemEntity> orderItem = MJsonUtils.jsonToOrderItemEntity(billListEntity.getOrder_items());
                List<String> itemImg = new ArrayList<>();
                for (OrderItemEntity orderItemInfo : orderItem) {
                    itemImg.add(MJsonUtils.josnToGoodsFirstImg(orderItemInfo.getItem_img()));
                }
                if (itemImg.size() > 0) {
                    bgaBanner.setAdapter(new BGABanner.Adapter() {
                        @Override
                        public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                            String picpath = (String) model;
                            ImageLoader.getInstance().displayImage(picpath, (ImageView) view, MyImage.deployMemory());
                        }
                    });
                    bgaBanner.setData(itemImg, null);
                }
            }
        });
        initVerticalRecyclerView(adapter, true, true, 1, 0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                BillListEntity info = (BillListEntity) item;
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.isPush = false;
                skipInfoEntity.pushId = info.getOrder_sn();
                viewDelegate.goToActivity(skipInfoEntity, BillOrderInfoActivity.class, 0);
            }
        });
    }

    @Override
    protected void initD() {
        fragmentIndex = getArguments().getInt(FRAGMENT_INDEX);
        tag = ordersTypeArray[fragmentIndex];
        loadingId = 2;
        page = 1;
        isFristAddData = true;
        getBillList();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            if (page == 1) {
                listEntities.clear();
            }
            List<BillListEntity> itemList = MJsonUtils.jsonToBillListEntity(baseEntity.data);
            listEntities.addAll(MJsonUtils.jsonToBillListEntity(baseEntity.data));
            completeRefresh(itemList.size(), listEntities.size(), false);
        }
    }

    /**
     * 獲取發單列表
     */
    private void getBillList() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        if (!tag.equals("ALL")) {
            params.put("statusCode", tag);
        }
        params.put("page", page);
        params.put("page_size", page_size);
        if (StrUtils.isNotNull(search_str)) {
            params.put("q", search_str);
        }
        baseHttp(null, true, Constants.API_BILL_ORDER_LIST, params);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getBillList();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page = (listEntities.size() / page_size) + 1;
        getBillList();
    }

    /**
     * 搜索
     *
     * @param searchStr
     */
    public void search(String searchStr) {
        search_str = searchStr;
        getBillList();
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        search_str = null;
    }
}
