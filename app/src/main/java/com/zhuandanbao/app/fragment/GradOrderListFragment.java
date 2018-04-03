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
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.BillOrderInfoActivity;
import com.zhuandanbao.app.activity.GradInfoActivity;
import com.zhuandanbao.app.activity.TakingOrderInfoActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.GradListEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
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
 * 抢单列表
 * Created by BFTECH on 2017/3/1.
 */

public class GradOrderListFragment extends BaseListViewFragment<ShopD> {

    private static final String[] ordersTypeArray = {"mycity", "FINISHED"};
    private List<GradListEntity> listEntities = new ArrayList<>();
    private RvBaseAdapter adapter;
    private ShopInfoItemEntity infoEntity = null;

    public static GradOrderListFragment getInstance(int indexFragment) {
        GradOrderListFragment fragment = new GradOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, indexFragment);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initD() {
        loadingId = 2;
        fragmentIndex = getArguments().getInt(FRAGMENT_INDEX);
        tag = ordersTypeArray[fragmentIndex];
        page = 1;
        isFristAddData = true;
        getGradList();
    }

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    public void initView() {
        super.initView();
        if (null != MainApplication.cache.getAsString(Constants.SHOP_INFO)) {
            infoEntity = MJsonUtils.josnToShopInfoItemEntity(MainApplication.cache.getAsString(Constants.SHOP_INFO));
        }
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, listEntities, R.layout.item_grad_order_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                GradListEntity info = (GradListEntity) item;
                TimeTextView orderEndTime = holder.getView(R.id.item_order_time);
                TextView dvTime = holder.getView(R.id.item_order_dv_time);
                BGABanner bgaBanner = holder.getView(R.id.item_banner);
                ImageView imageStatsHint = holder.getView(R.id.item_order_hint_img);
                holder.setText(R.id.item_order_sn, info.getOrder_sn());
                holder.setText(R.id.order_state_tx, info.getOrder_status_remark());
                holder.setText(R.id.item_order_address, Html.fromHtml(StrUtils.setHtml999(null, "配送地址：", info.getAreainfo())));
                holder.setText(R.id.item_order_price, "￥  " + info.getOrder_amount());
                if (StrUtils.isNotNull(info.getDistance())) {
                    holder.setText(R.id.item_order_distance, info.getDistance() + " 公里");
                }
                if (info.getOrder_status().equals("WAIT_GRAB")) {//待抢单
                    if (orderEndTime != null) {//截止时间
                        Long now_time = System.currentTimeMillis() / 1000;
                        long[] showTime = MUtils.callBacKTime(now_time, Long.parseLong(info.getGrab_order_deadline()));
                        orderEndTime.setTimes(showTime, null, "  后截止抢单");
                        orderEndTime.setShowText();
                        // 已经在倒计时的时候不再开启计时
//                if (!orderEndTime.isFlagStart()) {
//                    orderEndTime.isStart();
//                }
                    }
                } else {
                    if (StrUtils.isNotNull(info.getReceive_shop_name())) {
                        orderEndTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "抢单店铺：", info.getReceive_shop_name())));
                    } else {
                        orderEndTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "发单店铺：", info.getSource_shop_name())));
                    }
                }
                if (info.getDelivery_type().equals("1")) {
                    dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送方式：", "快递配送")));
                } else {
                    dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送时间：", info.getDelivery_date() + "  " + info.getDelivery_time())));
                }
                List<OrderItemEntity> orderItem = MJsonUtils.jsonToOrderItemEntity(info.getOrder_items());
                List<String> itemImg = new ArrayList<>();
                for (OrderItemEntity orderItemInfo : orderItem) {
                    itemImg.add(MJsonUtils.josnToGoodsFirstImg(orderItemInfo.getItem_img()));
                }
                if (itemImg.size() > 0) {
                    bgaBanner.setAdapter(new BGABanner.Adapter() {
                        @Override
                        public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                            String picpath = (String) model;
                            ImageLoader.getInstance().displayImage(picpath, (ImageView) view, MyImage.deploy());
                        }
                    });
                    bgaBanner.setData(itemImg, null);
                }
                MLog.e("getOrder_status=" + info.getOrder_status() + "==sid==" + info.getReceive_sid());
                imageStatsHint.setVisibility(View.VISIBLE);
                if (info.getOrder_status().equals("FINISHED")) {//已搶單
                    if (null != infoEntity && info.getReceive_sid().equals(infoEntity.getSid())) {//自己抢到
                        imageStatsHint.setImageResource(R.mipmap.img_order_grab_oneself);
                    } else {
                        imageStatsHint.setImageResource(R.mipmap.img_order_grabed);
                    }
                } else if (info.getOrder_status().equals("RECALL")) {
                    imageStatsHint.setImageResource(R.mipmap.img_order_grab_backout);
                } else {
                    imageStatsHint.setVisibility(View.GONE);
                }
            }
        });
        initVerticalRecyclerView(adapter, true, true, 1, 0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                GradListEntity info = (GradListEntity) item;
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.pushId = info.getOrder_sn();
                skipInfoEntity.isPush = false;
                if (null != infoEntity && info.getReceive_sid().equals(infoEntity.getSid())) {//接单
                    viewDelegate.goToActivity(skipInfoEntity, TakingOrderInfoActivity.class, 0);
                } else if (null != infoEntity && info.getSource_sid().equals(infoEntity.getSid())) {//发单
                    viewDelegate.goToActivity(skipInfoEntity, BillOrderInfoActivity.class, 0);
                } else {//抢单
                    viewDelegate.goToActivity(skipInfoEntity, GradInfoActivity.class, 0);
                }
            }
        });
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            if (page == 1) {
                listEntities.clear();
            }
            List<GradListEntity> itemList = MJsonUtils.jsonToGradListEntity(baseEntity.data);
            listEntities.addAll(itemList);
            completeRefresh(itemList.size(), listEntities.size(), false);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getGradList();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page = (listEntities.size() / page_size) + 1;
        getGradList();
    }

    /**
     * 获取抢单列表
     */
    private void getGradList() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put("page", page);
        params.put("page_size", page_size);
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("local", tag);
        baseHttp(null, true, Constants.API_GRAD_ORDER_LISTS_API, params);
    }
}
