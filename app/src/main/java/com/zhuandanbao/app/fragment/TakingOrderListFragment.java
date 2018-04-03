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
import com.zhuandanbao.app.activity.GradInfoActivity;
import com.zhuandanbao.app.activity.TakingOrderInfoActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.TakingListEntity;
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
 * 接单列表
 * Created by BFTECH on 2017/3/1.
 */

public class TakingOrderListFragment extends BaseListViewFragment<ShopD> {
    private static final String[] ordersTypeArray = {"WAIT_CONFIRM", "PROCESSING", "HAS_BEEN_SERVED", "FINISHED", "ALL", "CANCELED", "GRAB", "WAIT_PAY", "REFUND"};
    private List<TakingListEntity> listEntities = new ArrayList<>();
    private RvBaseAdapter adapter;

    public static TakingOrderListFragment getInstance(int indexFragment) {
        TakingOrderListFragment fragment = new TakingOrderListFragment();
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
    protected void initD() {
        fragmentIndex = getArguments().getInt(FRAGMENT_INDEX);
        tag = ordersTypeArray[fragmentIndex];
        loadingId = 2;
        page = 1;
        isFristAddData=true;
        getTakingList();
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, listEntities, R.layout.item_taking_order_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                TakingListEntity takingListEntity = (TakingListEntity) item;
                TimeTextView orderTime = holder.getView(R.id.order_time);
                TextView dvTime = holder.getView(R.id.order_dv_time);
                BGABanner bgaBanner = holder.getView(R.id.item_banner);
                holder.setText(R.id.order_sn, takingListEntity.getOrder_sn());
                holder.setText(R.id.order_status_tx, takingListEntity.getOrder_status_remark());
                holder.setText(R.id.order_address, Html.fromHtml(StrUtils.setHtml999(null, "配送地址：", takingListEntity.getAreainfo())));
                holder.setText(R.id.order_price, "￥  " + takingListEntity.getOrder_amount());
                holder.setText(R.id.order_distance, takingListEntity.getDistance() + "公里");
                orderTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "发单店铺：", takingListEntity.getFadan_dianpu_name())));
                if (takingListEntity.getOrder_type().equals("1")) {// 抢单  待接单
                    holder.setImageResource(R.id.order_img, R.mipmap.img_grab_order);
                    if (takingListEntity.getOrder_status().equals("WAIT_GRAB")) {//待抢单
                        if (orderTime != null) {//截止时间
                            Long now_time = System.currentTimeMillis() / 1000;
                            long[] showTime = MUtils.callBacKTime(now_time, Long.parseLong(takingListEntity.getGrab_order_deadline()));
                            orderTime.setTimes(showTime, null, "  后截止抢单");
                            orderTime.setShowText();
                            // 已经在倒计时的时候不再开启计时
                        }
                    }
                } else if (takingListEntity.getOrder_type().equals("0") || takingListEntity.getOrder_type().equals("2")) {//一对一接单  待接单
                    holder.setImageResource(R.id.order_img, R.mipmap.img_un_grab_order);
                }
                if (takingListEntity.getDelivery_type().equals("1")) {
                    if (StrUtils.isNotNull(takingListEntity.getExpress_sn()) || StrUtils.isNotNull(takingListEntity.getExpress_name())) {
                        MLog.e("express_sn=" + takingListEntity.getExpress_sn());
                        dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流信息：", takingListEntity.getExpress_name() + "(" + takingListEntity.getExpress_sn() + ")")));
                    } else {
                        dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送方式：", "快递配送")));
                    }
                } else {
                    dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送时间：", takingListEntity.getDelivery_date() + "  " + takingListEntity.getDelivery_time())));
                }
                List<OrderItemEntity> orderItem = MJsonUtils.jsonToOrderItemEntity(takingListEntity.getOrder_items());
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
                TakingListEntity takingListEntity = (TakingListEntity) item;
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.isPush = false;
                skipInfoEntity.pushId = takingListEntity.getOrder_sn();
                if (takingListEntity.getOrder_type().equals("1") && takingListEntity.getOrder_status().equals("WAIT_GRAB")) {//搶單詳情
                    viewDelegate.goToActivity(skipInfoEntity, GradInfoActivity.class, 0);
                } else {//接單詳情
                    viewDelegate.goToActivity(skipInfoEntity, TakingOrderInfoActivity.class, 0);
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
            List<TakingListEntity> itemList = MJsonUtils.jsonToTakingListEntity(baseEntity.data);
            listEntities.addAll(MJsonUtils.jsonToTakingListEntity(baseEntity.data));
            completeRefresh(itemList.size(), listEntities.size(), false);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getTakingList();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page = (listEntities.size() / page_size) + 1;
        getTakingList();
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
        getTakingList();
    }

    /**
     * 获取接单列表
     */
    private void getTakingList() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        if (!tag.equals("ALL")) {
            params.put("status_code", tag);
        }
        params.put("page", page);
        params.put("page_size", page_size);
        if (StrUtils.isNotNull(search_str)) {
            params.put("q", search_str);
        }
        baseHttp(null, true, Constants.API_ACTION_ORDERS_INFO, params);
    }
}
