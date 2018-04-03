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
import com.zhuandanbao.app.activity.OtherOrderInfoActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.OtherListEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 三方订单列表
 * Created by BFTECH on 2017/3/2.
 */

public class OtherOrderListFragment extends BaseListViewFragment<ShopD> {

    private String[] ordersTypeArray = {"1001", "1002", "1003"};
    private List<OtherListEntity> listEntities = new ArrayList<>();
    private RvBaseAdapter adapter;

    public static OtherOrderListFragment getInstance(int indexFragment) {
        OtherOrderListFragment fragment = new OtherOrderListFragment();
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
        page = 1;
        fragmentIndex = getArguments().getInt(FRAGMENT_INDEX);
        tag = ordersTypeArray[fragmentIndex];
        isFristAddData=true;
        getOtherList();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            if (page == 1) {
                listEntities.clear();
            }
            List<OtherListEntity> itemList = MJsonUtils.jsonToOtherListEntity(baseEntity.data);
            listEntities.addAll(MJsonUtils.jsonToOtherListEntity(baseEntity.data));
            completeRefresh(itemList.size(), listEntities.size(), false);
        }
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, listEntities, R.layout.item_bill_order_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                OtherListEntity otherListEntity = (OtherListEntity) item;
                TextView dvTime = holder.getView(R.id.item_order_name);
                TextView dvAddress = holder.getView(R.id.item_order_address);
                BGABanner bgaBanner = holder.getView(R.id.item_banner);
                holder.setVisible(R.id.bill_order_img, false);
                holder.setText(R.id.bill_order_sn, otherListEntity.getOrder_sn());
                holder.setText(R.id.bill_order_status_tx, otherListEntity.getOrder_status_remark());
                holder.setText(R.id.item_order_time, Html.fromHtml(StrUtils.setHtml999(null, "订单来源：", otherListEntity.getSource_shop_name())));
                holder.setText(R.id.item_order_money, Html.fromHtml(StrUtils.setHtml999(null, "收货信息：", otherListEntity.getConsignee())));
                holder.setTextColorRes(R.id.item_order_money, R.color.text_color_333);
                dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送时间：", otherListEntity.getDelivery_date() + "   " + otherListEntity.getDelivery_time())));
                dvAddress.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送地址：", otherListEntity.getAreainfo())));
                if (otherListEntity.getOrder_status().equals("1") && otherListEntity.getSingle_way().equals("0") && StrUtils.isNull(otherListEntity.getZdb_order_sn())) {//标记自处理
                    if (otherListEntity.getDelivery_type().equals("1")) {//快递配送
                        if (StrUtils.isNull(otherListEntity.getExpress_name()) || StrUtils.isNull(otherListEntity.getExpress_sn())) {//表示没有添加物流
                            dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送方式：", "快递配送")));
                        } else {//有物流
                            dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流信息：", otherListEntity.getExpress_name() + "(" + otherListEntity.getExpress_sn() + ")")));
                        }
                    } else {//非快递
                        dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送时间：", otherListEntity.getDelivery_date() + "   " + otherListEntity.getDelivery_time())));
                    }
                    dvAddress.setText(Html.fromHtml(StrUtils.setHtml999(null, "处理方式：", "自处理")));
                }
                if (StrUtils.isNotNull(otherListEntity.getZdb_order_sn())) {
                    if (StrUtils.isNotNull(otherListEntity.getReceive_shop_name())) {
                        dvAddress.setText(Html.fromHtml(StrUtils.setHtml999(null, "接单店铺：", otherListEntity.getReceive_shop_name())));
                    } else {
                        dvAddress.setText(Html.fromHtml(StrUtils.setHtml999(null, "接单店铺：", StrUtils.setHtmlRed(null, "暂未接单", null))));
                    }
                }
                List<OrderItemEntity> orderItem = MJsonUtils.jsonToOrderItemEntity(otherListEntity.getOrder_items());
                List<String> itemImg = new ArrayList<>();
                for (OrderItemEntity orderItemInfo : orderItem) {
                    if (StrUtils.isNotNull(orderItemInfo.getItem_img())) {
                        itemImg.add(MJsonUtils.josnToGoodsFirstImg(orderItemInfo.getItem_img()));
                    }
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
                OtherListEntity otherListEntity = (OtherListEntity) item;
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.isPush = false;
                skipInfoEntity.pushId = otherListEntity.getOrder_sn();
                viewDelegate.goToActivity(skipInfoEntity, OtherOrderInfoActivity.class, 0);
            }
        });
    }

    /**
     * 获取三方订单列表
     */
    private void getOtherList() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        if (StrUtils.isNotNull(search_str)) {
            params.put("keyword", search_str);
        }
        params.put("order_status", tag);
        params.put("page", page);
        params.put("page_size", page_size);
        baseHttp(null, true, Constants.API_OTHER_ORDER_LIST, params);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getOtherList();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page = (listEntities.size() / page_size) + 1;
        getOtherList();
    }

    /**
     * 搜索
     *
     * @param searchStr
     */
    public void search(String searchStr) {
        search_str = searchStr;
        getOtherList();
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        search_str = null;
    }
}
