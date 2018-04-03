package com.zhuandanbao.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.BillOrderInfoActivity;
import com.zhuandanbao.app.activity.OtherAmendOrderInfoActivity;
import com.zhuandanbao.app.activity.ShopDetailsActivity;
import com.zhuandanbao.app.activity.WuliuActivity;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.BillOrderInfoD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.OtherInfoEntity;
import com.zhuandanbao.app.entity.OtherInfoZdbInfoEntity;
import com.zhuandanbao.app.entity.ShowButText;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 三方订单详情
 * Created by BFTECH on 2017/3/9.
 */

public class OtherInfoFragment extends BaseListViewFragment<BillOrderInfoD> {
    private String orderSn = null;
    private OtherInfoEntity infoEntity;
    private OtherInfoZdbInfoEntity zdbInfoEntity;
    private RvBaseAdapter adapter;
    private List<OrderItemEntity> orderItemEntities = new ArrayList<>();
    private View topView;
    private View bottomView;
    private ShowButText showButText;
    private LinearLayout butLayout;

    public static OtherInfoFragment getInstance(String orderSn) {
        OtherInfoFragment fragment = new OtherInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_INDEX, orderSn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initD() {
        orderSn = getArguments().getString(FRAGMENT_INDEX);
        getOtherInfo();
    }

    @Override
    protected Class<BillOrderInfoD> getDelegateClass() {
        return BillOrderInfoD.class;
    }

    @Override
    public void initView() {
        super.initView();
        butLayout = viewDelegate.get(R.id.bill_info_but_layout);
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, orderItemEntities, R.layout.item_order_goods, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                OrderItemEntity itemEntity = (OrderItemEntity) item;
                if (StrUtils.isNotNull(itemEntity.getItem_name())) {
                    holder.setText(R.id.item_goods_name, itemEntity.getItem_name());
                    holder.setText(R.id.item_goods_dsc, itemEntity.getItem_remarks());
                } else {
                    holder.setText(R.id.item_goods_name, itemEntity.getItem_remarks());
                }
                holder.setText(R.id.item_goods_num, "x " + itemEntity.getItem_total());

                BGABanner bgaBanner=holder.getView(R.id.item_img);
                if (StrUtils.isNotNull(itemEntity.getItem_img())){
                    final List<String> imgList= JSON.parseArray(itemEntity.getItem_img(),String.class);
                    if (imgList.size()>0){
                        bgaBanner.setAdapter(new BGABanner.Adapter() {
                            @Override
                            public void fillBannerItem(BGABanner banner, View itemView, Object model, final int position) {
                                String picpath = (String) model;
                                ImageLoader.getInstance().displayImage(picpath, (ImageView) itemView, MyImage.deployMemoryNoLoading());
                                itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //打开预览
                                        Intent intentPreview = new Intent(activity, ImagePreviewDelActivity.class);
                                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>)  MUtils.itemToString(imgList));
                                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                                        startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                                    }
                                });
                            }
                        });
                        bgaBanner.setData(imgList, null);
                    }
                }
            }
        });
        adapter.addHeaderView(getTopView());
        adapter.addFooterView(getBottomView());
        initVerticalRecyclerView(adapter, true, false, 1, 0);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getOtherInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            orderItemEntities.clear();
            infoEntity = MJsonUtils.jsonToOtherInfoEntity(baseEntity.data);
            zdbInfoEntity = MJsonUtils.jsonToOtherInfoZdbInfoEntity(baseEntity.data);
            orderItemEntities.addAll(MJsonUtils.jsonToOrderItemEntity(infoEntity.getOrder_items()));
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            initOtherInfo();
        }
        if (code == 2) {
            viewDelegate.showSuccessHint("已标为处理中", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    getOtherInfo();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_retry) {//自处理
            MUtils.showDialog(getActivity(), "转单宝提醒", "取消", "确认", "订单标记为自处理，将由商家自行处理订单。并位于“三方订单”管理中的处理中。", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {//订单详情
                    MUtils.dialog.cancel();
                    ziChuli();
                }
            });
        }
        if (view.getId() == R.id.but_amend) {//修改
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            skipInfoEntity.pushId = infoEntity.getOrder_sn();
            skipInfoEntity.index = 2;
            Intent intent = new Intent(activity, OtherAmendOrderInfoActivity.class);
            intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
            startActivityForResult(intent, 300);
        }
        if (view.getId() == R.id.but_pay) {//转发
            TextView textView = viewDelegate.get(R.id.but_pay);
            String str = textView.getText().toString().trim();
            if (str.equals("添加物流") || str.equals("修改物流")) {
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.index = 2;
                skipInfoEntity.msg = infoEntity.getConsignee() + "，" + infoEntity.getMobile() + "\n"
                        + infoEntity.getAreainfo() + " " + infoEntity.getAddress();
                skipInfoEntity.pushId = infoEntity.getOrder_sn();
                skipInfoEntity.payCode = infoEntity.getExpress_sn();
                skipInfoEntity.mobile = infoEntity.getExpress_name();
                skipInfoEntity.expressId = infoEntity.getExpress();
                Intent intent = new Intent(activity, WuliuActivity.class);
                intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
                startActivityForResult(intent, 300);
            }
            if (str.equals("转发单")) {
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.pushId = infoEntity.getOrder_sn();
                skipInfoEntity.index = 1;
                Intent intent = new Intent(activity, OtherAmendOrderInfoActivity.class);
                intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
                startActivityForResult(intent, 300);
            }
        }
    }

    /**
     * 標記自處理
     */
    private void ziChuli() {
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", infoEntity.getOrder_sn());
        baseHttp("正在提交...", true, Constants.API_OTHER_ORDER_ZCL, params);
    }

    /**
     * 獲取三方訂單詳情
     */
    private void getOtherInfo() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", orderSn);
        baseHttp(null, true, Constants.API_OTHER_ORDER_INFO, params);
    }

    /**
     * 初始化信息
     */
    private void initOtherInfo() {
        TextView orderSn = viewDelegate.get(R.id.bill_info_order_sn);
        TextView orderStatus = viewDelegate.get(R.id.bill_info_order_status_text);
        TextView orderSource = ViewHolder.get(topView, R.id.order_source);
        TextView orderAmount = ViewHolder.get(topView, R.id.order_amount);
        TextView orderZdbSn = ViewHolder.get(topView, R.id.order_zdb_sn);
        TextView orderJiedan = ViewHolder.get(topView, R.id.order_jiedan);
        orderSn.setText(infoEntity.getOrder_sn());
        orderStatus.setText(infoEntity.getOrder_status_remark());
        orderSource.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单来源：", infoEntity.getSource_shop_name())));
        orderAmount.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单金额：", "￥ " + infoEntity.getOrder_amount())));
        if (StrUtils.isNotNull(infoEntity.getZdb_order_sn())) {
            orderZdbSn.setVisibility(View.VISIBLE);
            orderZdbSn.setText(Html.fromHtml(StrUtils.setHtml999(null, "转单单号：", StrUtils.setHtmlRed(infoEntity.getZdb_order_sn(), "(" + infoEntity.getZdb_order_status_remark() + ")", null))));
            orderZdbSn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                    skipInfoEntity.pushId=infoEntity.getZdb_order_sn();
                    skipInfoEntity.isPush=false;
                    viewDelegate.goToActivity(skipInfoEntity, BillOrderInfoActivity.class,0);
                }
            });
        } else {
            orderZdbSn.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(infoEntity.getZhuandanOrder()) && StrUtils.isNotNull(zdbInfoEntity.getReceive_shop_name())) {
            orderJiedan.setVisibility(View.VISIBLE);
            orderJiedan.setText(Html.fromHtml(StrUtils.setHtml999(null, "接单店铺：", zdbInfoEntity.getReceive_shop_name())));
            orderJiedan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                    skipInfoEntity.pushId=zdbInfoEntity.getReceive_sid();
                    viewDelegate.goToActivity(skipInfoEntity, ShopDetailsActivity.class,0);
                }
            });
        } else {
            orderJiedan.setVisibility(View.GONE);
        }
        LinearLayout signLayout = ViewHolder.get(bottomView, R.id.sign_layout);
        TextView signName = ViewHolder.get(bottomView, R.id.order_info_sign_info);
        RecyclerView signRc = ViewHolder.get(bottomView, R.id.sign_recyclerView);
        if (StrUtils.isNotNull(zdbInfoEntity.getConsignee()) && StrUtils.isNotNull(zdbInfoEntity.getSign_time())) {
            signLayout.setVisibility(View.VISIBLE);
            signName.setText(zdbInfoEntity.getConsignee() + " | " + MUtils.getTo66TimePHP(zdbInfoEntity.getSign_time(), "yyyy-MM-dd HH:mm"));
            List<ImageItem> items = MJsonUtils.stingToImageItem(zdbInfoEntity.getSign_voucher());
            if (items.size() > 0) {
                ImagePickerAdapter adapter = new ImagePickerAdapter(context, items, items.size(), true);
                signRc.setAdapter(adapter);
            }
        } else {
            signLayout.setVisibility(View.GONE);
        }
        TextView orderDvType = ViewHolder.get(topView, R.id.order_dv_type);
        TextView orderDvTime = ViewHolder.get(topView, R.id.order_dv_time);
        TextView orderTakeName = ViewHolder.get(topView, R.id.order_take_name);
        TextView orderTakeMobile = ViewHolder.get(topView, R.id.order_take_mobile);
        TextView orderDvAddress = ViewHolder.get(topView, R.id.order_dv_address);
        if ("1".equals(infoEntity.getDelivery_type())) {//配送信息
            orderDvType.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送方式：", "快递配送")));
            if (StrUtils.isNull(infoEntity.getExpress_name()) || StrUtils.isNull(infoEntity.getExpress_sn())) {
                orderDvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流信息：", "暂无信息")));
            } else {
                orderDvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流信息：", infoEntity.getExpress_name() + "(" + infoEntity.getExpress_sn() + ")")));
            }
        } else {
            orderDvType.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送方式：", "送货上门")));
            orderDvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送时间：", infoEntity.getDelivery_date() + infoEntity.getDelivery_time())));
        }
        orderTakeName.setText(Html.fromHtml(StrUtils.setHtml999(null, "收货信息：", infoEntity.getConsignee())));
        orderTakeMobile.setText(infoEntity.getMobile());
        orderDvAddress.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送地址：", infoEntity.getAreainfo() + "  " + infoEntity.getAddress())));

        TextView orderMsgInfo = ViewHolder.get(topView, R.id.order_msg_info);
        TextView orderCardMsg = ViewHolder.get(topView, R.id.order_card_msg);
        TextView orderShopMsg = ViewHolder.get(topView, R.id.order_shop_msg);
        if (StrUtils.isNotNull(infoEntity.getOrder_remarks())) {
            orderMsgInfo.setVisibility(View.VISIBLE);
            orderMsgInfo.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单备注：", infoEntity.getOrder_remarks())));
        } else {
            orderMsgInfo.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(infoEntity.getCard_con())) {
            orderCardMsg.setVisibility(View.VISIBLE);
            orderCardMsg.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单贺卡：", infoEntity.getCard_con())));
        } else {
            orderCardMsg.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(infoEntity.getSeller_remark())) {
            orderShopMsg.setVisibility(View.VISIBLE);
            orderShopMsg.setText(Html.fromHtml(StrUtils.setHtml999(null, "商家备注：", infoEntity.getSeller_remark())));
        } else {
            orderShopMsg.setVisibility(View.GONE);
        }
        TextView name = ViewHolder.get(bottomView, R.id.order_order_name);
        name.setText(Html.fromHtml(StrUtils.setHtml999(null, "订货信息：", infoEntity.getBuyer_name())));
        showBut();
    }

    /**
     * 显示按钮
     */
    private void showBut() {
        showButText = new ShowButText();
        if (infoEntity.getOrder_status().equals("0") && infoEntity.getSingle_way() < 2
                && infoEntity.getIssues_order().equals("N") && infoEntity.getRefund_status() <= 0) {
            showButText.isShowOk = "转发单";
            showButText.isShowAmend = "修改订单";
            showButText.isShowOnline = "自处理";
        } else if (infoEntity.getOrder_status().equals("1") && infoEntity.getSingle_way() == 0
                && StrUtils.isNull(infoEntity.getZdb_order_sn())) {//标记自处理
            if (infoEntity.getDelivery_type().equals("1")) {//快递配送
                if (StrUtils.isNull(infoEntity.getExpress_name()) || StrUtils.isNull(infoEntity.getExpress_sn())) {//表示没有添加物流
                    showButText.isShowOk = "添加物流";
                } else {
                    showButText.isShowOk = "修改物流";
                }
            }
        } else {
            butLayout.setVisibility(View.GONE);
        }
        MUtils.showBillInfoBut(butLayout, showButText, this);
    }

    private View getTopView() {
        topView = LayoutInflater.from(context).inflate(R.layout.item_other_info_top, null);
        return topView;
    }

    private View getBottomView() {
        bottomView = LayoutInflater.from(context).inflate(R.layout.item_other_info_bottom, null);
        return bottomView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == 6666) {
            loadingId=2;
            getOtherInfo();
        }
    }
}
