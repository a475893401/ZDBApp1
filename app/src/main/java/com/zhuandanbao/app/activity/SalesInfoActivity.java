package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.OrderInfoD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.RefundDataEntity;
import com.zhuandanbao.app.entity.SalesInfoEntity;
import com.zhuandanbao.app.entity.ShowButText;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
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
 * 售后详情
 * Created by BFTECH on 2017/3/15.
 */

public class SalesInfoActivity extends BaseListViewActivity<OrderInfoD> {

    private SkipInfoEntity skipInfoEntity;
    private List<OrderItemEntity> itemEntityList = new ArrayList<>();
    private RvBaseAdapter adapter;
    private OrderDataEntity dataEntity;
    private RefundDataEntity refundDataEntity;
    private View topView;
    private ShowButText showButText;

    @Override
    protected Class<OrderInfoD> getDelegateClass() {
        return OrderInfoD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (skipInfoEntity.isPush){
                    SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                    skipInfoEntity.index=Constants.GRAB_ORDER;
                    viewDelegate.goToActivity(skipInfoEntity, MainActivity.class,0);
                    finish();
                }else {
                    finish();
                }
            }
        });
        viewDelegate.setTitle("售后详情");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, itemEntityList, R.layout.item_order_goods, new RvBaseAdapter.RefreshAdapterImpl() {
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
                    final List<String> imgList=JSON.parseArray(itemEntity.getItem_img(),String.class);
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
        topView = LayoutInflater.from(context).inflate(R.layout.item_sales_info_top, null);
        adapter.addHeaderView(topView);
        initVerticalRecyclerView(adapter, true, false, 1, 0);
        loadingId = 2;
        getSalesInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            SalesInfoEntity salesInfoEntity = MJsonUtils.josnToSalesInfoEntity(baseEntity.data);
            dataEntity = JSON.parseObject(salesInfoEntity.getOrderData(), OrderDataEntity.class);
            refundDataEntity = JSON.parseObject(salesInfoEntity.getRefundData(), RefundDataEntity.class);
            itemEntityList.clear();
            itemEntityList.addAll(MJsonUtils.jsonToOrderItemEntity(dataEntity.getOrder_items()));
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            initInfo();
        }
        if (code==2){
            viewDelegate.showSuccessHint("已同意退款", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    loadingId=2;
                    getSalesInfo();
                }
            });
        }
        if (code==3){
            viewDelegate.showSuccessHint("已取消售后", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(6666);
                    SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                    skipInfoEntity.isPush=false;
                    skipInfoEntity.pushId=dataEntity.getOrder_sn();
                    viewDelegate.goToActivity(skipInfoEntity,BillOrderInfoActivity.class,0);
                    finish();
                }
            });
        }
    }

    /**
     * 初始化信息
     */
    private void initInfo() {
        LinearLayout butLayout=viewDelegate.get(R.id.but_layout);
        TextView orderSn = ViewHolder.get(topView, R.id.sales_info_order_sn);
        TextView orderAmount = ViewHolder.get(topView, R.id.sales_info_order_amount);
        TextView orderMoney = ViewHolder.get(topView, R.id.sales_info_money);
        TextView orderTime = ViewHolder.get(topView, R.id.sales_info_order_time);
        TextView orderType = ViewHolder.get(topView, R.id.sales_info_order_type);
        TextView orderReson = ViewHolder.get(topView, R.id.sales_info_order_reson);
        TextView orderShopName = ViewHolder.get(topView, R.id.sales_info_order_shop_name);
        LinearLayout retuenLayout = ViewHolder.get(topView, R.id.sales_info_img_layout);
        RecyclerView returnImg = ViewHolder.get(topView, R.id.img_recyclerView);
        orderSn.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单编号：", dataEntity.getOrder_sn())));
        orderAmount.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单金额：", "￥ " + dataEntity.getOrder_amount())));
        orderMoney.setText(Html.fromHtml(StrUtils.setHtml999(null, "申请金额：", "￥ " + refundDataEntity.getReturn_amount() + "(" + refundDataEntity.getReturn_mode_name() + ")")));
        orderTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "申请时间：", refundDataEntity.getReturn_time_format())));
        orderType.setText(Html.fromHtml(StrUtils.setHtml999(null, "申请类型：", refundDataEntity.getIssue_type_name())));
        orderReson.setText(Html.fromHtml(StrUtils.setHtml999(null, "退款理由：", refundDataEntity.getReturn_reason())));
        if ("N".equals(refundDataEntity.getReturn_self_apply())) {//收到的
            orderShopName.setText(Html.fromHtml(StrUtils.setHtml999(null, "发单店铺：", refundDataEntity.getReturn_shop_name())));
        } else {//发出的
            orderShopName.setText(Html.fromHtml(StrUtils.setHtml999(null, "接单店铺：", refundDataEntity.getShop_name())));
        }
        List<String> strList = JSON.parseArray(refundDataEntity.getReturn_img(), String.class);
        if (strList.size() > 0) {
            retuenLayout.setVisibility(View.VISIBLE);
            List<ImageItem> itemList = new ArrayList<>();
            for (String s : strList) {
                ImageItem imageItem = new ImageItem();
                imageItem.url = s;
                itemList.add(imageItem);
            }
            final ImagePickerAdapter adapter1=new ImagePickerAdapter(context,itemList,itemList.size(),true);
            returnImg.setAdapter(adapter1);
            adapter1.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intentPreview = new Intent(SalesInfoActivity.this, ImagePreviewDelActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter1.getImages());
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                    startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                }
            });
        } else {
            retuenLayout.setVisibility(View.GONE);
        }
        orderShopName.setOnClickListener(this);
        showButText=new ShowButText();
        if ("Y".equals(refundDataEntity.getReturn_self_apply())) {//自己发出的
            if ("0".equals(refundDataEntity.getReturn_judge()) && "-1".equals(refundDataEntity.getReturn_status()) && "0".equals(refundDataEntity.getClose())) {//申请仲裁
                showButText.isShowOk="申请仲裁";
            }
            if ("0".equals(refundDataEntity.getReturn_judge()) && !"1".equals(refundDataEntity.getReturn_status()) && "0".equals(refundDataEntity.getClose())){//取消售后
                showButText.isShowCancle="取消售后";
            }
        } else {//收到的
            if ("0".equals(refundDataEntity.getReturn_judge()) && "0".equals(refundDataEntity.getReturn_status())) {//拒绝退款
                showButText.isShowOk="同意退款";
                showButText.isShowCancle="拒绝退款";
            }
        }
        MUtils.showGradBut(butLayout,showButText,this);
        initTop1();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.sales_info_order_shop_name) {
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            if ("N".equals(refundDataEntity.getReturn_self_apply())) {//收到的
                skipInfoEntity.pushId = refundDataEntity.getReturn_sid();
            } else {
                skipInfoEntity.pushId = refundDataEntity.getSid();
            }
            viewDelegate.goToActivity(skipInfoEntity, ShopDetailsActivity.class, 0);
        }
        if (view.getId()==R.id.but_ok){
            if (showButText.isShowOk.equals("申请仲裁")){
                SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                skipInfoEntity.data=refundDataEntity;
                viewDelegate.goToActivity(skipInfoEntity,ZhongCaiActivity.class,300);
            }else {
                requestId=2;
                loadingId=1;
                HttpParams params=new HttpParams();
                params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
                params.put("rid", refundDataEntity.getRid());
                baseHttp("正在提交...",true,Constants.API_SALE_AGREE,params);
            }
        }
        if (view.getId()==R.id.but_cancle){//拒絕退款
            Button button=viewDelegate.get(R.id.but_cancle);
            if (button.getText().toString().equals("拒绝退款")){
                SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                skipInfoEntity.data=refundDataEntity;
                viewDelegate.goToActivity(skipInfoEntity,RejectSalesActivity.class,300);
            }
            if (button.getText().toString().equals("取消售后")){
                cancleSales();
            }
        }
    }

    /**
     * 申请的售后信息
     */
    private void initTop1(){
        LinearLayout topLayout=ViewHolder.get(topView,R.id.sales_top1);
        TextView topImgHint=ViewHolder.get(topView,R.id.sales_top1_img_hint);
        TextView topTxHint=ViewHolder.get(topView,R.id.sales_top1_tx_hint);
        TextView topReson=ViewHolder.get(topView,R.id.sales_top1_reson);
        TextView topTime=ViewHolder.get(topView,R.id.sales_top1_time);
        RecyclerView recyclerView=ViewHolder.get(topView,R.id.sales_top1_show_img);
        List<String> strList=JSON.parseArray(refundDataEntity.getReturn_audit_img(),String.class);
        if (strList.size()>0){
            List<ImageItem> itemList=new ArrayList<>();
            for (String s:strList){
                ImageItem imageItem=new ImageItem();
                imageItem.url=s;
                itemList.add(imageItem);
            }
            final ImagePickerAdapter adapter1=new ImagePickerAdapter(context,itemList,itemList.size(),true);
            recyclerView.setAdapter(adapter1);
            adapter1.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intentPreview = new Intent(SalesInfoActivity.this, ImagePreviewDelActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter1.getImages());
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                    startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                }
            });
        }
        if ("2".equals(refundDataEntity.getReturn_status()) && "0".equals(refundDataEntity.getReturn_judge())) {//已完成
            topLayout.setVisibility(View.VISIBLE);
            topTxHint.setVisibility(View.GONE);
            topReson.setVisibility(View.GONE);
            topImgHint.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.agree_img), null, null, null);
            topImgHint.setText("已同意退款");
            topTime.setText(Html.fromHtml(StrUtils.setHtml999(null,"退款时间：", MUtils.getTo66TimePHP(refundDataEntity.getReturn_audit_time(),"yyyy-MM-dd HH:mm"))));
        }
        if ("0".equals(refundDataEntity.getReturn_status()) && "0".equals(refundDataEntity.getReturn_judge())) {//待处理
            topLayout.setVisibility(View.VISIBLE);
            topReson.setVisibility(View.GONE);
            topTime.setVisibility(View.GONE);
            topImgHint.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.wait_img), null, null, null);
            if ("N".equals(refundDataEntity.getReturn_self_apply())) {
                topImgHint.setText("请及时受理售后");
                topTxHint.setText("自申请日起7天未受理则自动同意。如同意则按对方申请金额退赔。");
            } else {
                topImgHint.setText("等待对方受理");
                topTxHint.setText("自申请日起7天未受理则自动退赔，如对方拒绝您可以向转单宝申请仲裁。");
            }
        }
        if ("-1".equals(refundDataEntity.getReturn_status())) {//接单方已拒绝
            topLayout.setVisibility(View.VISIBLE);
            topTxHint.setVisibility(View.GONE);
            topReson.setVisibility(View.VISIBLE);
            topTime.setVisibility(View.VISIBLE);
            topImgHint.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.decline_img), null, null, null);
            topImgHint.setText("已驳回退款申请");
            topTime.setText(Html.fromHtml(StrUtils.setHtml999(null,"拒绝时间：", refundDataEntity.getReturn_audit_time_format())));
            topReson.setText(Html.fromHtml(StrUtils.setHtml999(null,"拒绝理由：", refundDataEntity.getReturn_audit_text())));
            initTop2();
        }
    }

    /**
     * 申请的仲裁信息
     */
    private void initTop2(){
        LinearLayout topLayout=ViewHolder.get(topView,R.id.sales_top2);
        TextView topImgHint=ViewHolder.get(topView,R.id.sales_top2_img_hint);
        TextView topTxHint=ViewHolder.get(topView,R.id.sales_top2_tx_hint);
        TextView topReson=ViewHolder.get(topView,R.id.sales_top2_reson);
        TextView topDes=ViewHolder.get(topView,R.id.sales_top2_des);
        TextView topTime=ViewHolder.get(topView,R.id.sales_top2_time);
        RecyclerView recyclerView=ViewHolder.get(topView,R.id.sales_top2_show_img);
        List<String> strList=JSON.parseArray(refundDataEntity.getReturn_img(),String.class);
        if (strList.size()>0){
            List<ImageItem> itemList=new ArrayList<>();
            for (String s:strList){
                ImageItem imageItem=new ImageItem();
                imageItem.url=s;
                itemList.add(imageItem);
            }
            final ImagePickerAdapter adapter1=new ImagePickerAdapter(context,itemList,itemList.size(),true);
            recyclerView.setAdapter(adapter1);
            adapter1.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intentPreview = new Intent(SalesInfoActivity.this, ImagePreviewDelActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter1.getImages());
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                    startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                }
            });
        }
        if ("2".equals(refundDataEntity.getReturn_judge()) || "-1".equals(refundDataEntity.getReturn_judge()) || "1".equals(refundDataEntity.getReturn_judge())) {//申请仲裁 已处理
            topLayout.setVisibility(View.VISIBLE);
            if ("2".equals(refundDataEntity.getReturn_judge())) {//仲裁成功
                topImgHint.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.agree_img), null, null, null);
                topImgHint.setText("仲裁已成立");
                topTxHint.setVisibility(View.GONE);
                topReson.setText(Html.fromHtml(StrUtils.setHtml999(null,"申请理由：",refundDataEntity.getReturn_judge_apply_desc())));
                topTime.setText(Html.fromHtml(StrUtils.setHtml999(null,"介入时间：",refundDataEntity.getReturn_judge_time_format())));
                topDes.setText(Html.fromHtml(StrUtils.setHtml999(null,"仲裁说明：",refundDataEntity.getReturn_judge_text())));
            }
            if ("-1".equals(refundDataEntity.getReturn_judge())) {//仲裁驳回
                topImgHint.setText("仲裁已驳回");
                topTxHint.setVisibility(View.GONE);
                topImgHint.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.decline_img), null, null, null);
                topReson.setText(Html.fromHtml(StrUtils.setHtml999(null,"申请理由：",refundDataEntity.getReturn_judge_apply_desc())));
                topTime.setText(Html.fromHtml(StrUtils.setHtml999(null,"介入时间：",refundDataEntity.getReturn_judge_time_format())));
                topDes.setText(Html.fromHtml(StrUtils.setHtml999(null,"仲裁说明：",refundDataEntity.getReturn_judge_text())));
            }
            if ("1".equals(refundDataEntity.getReturn_judge())) {//等待仲裁
                topDes.setVisibility(View.GONE);
                topImgHint.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.wait_img), null, null, null);
                topImgHint.setText("等待转单宝介入仲裁");
                topTxHint.setText("转单宝会在3个工作日内受理。工作人员可能会与您核实情况，请保持电话或在线联系方式畅通");
                topReson.setText(Html.fromHtml(StrUtils.setHtml999(null,"申请理由：",refundDataEntity.getReturn_judge_apply_desc())));
                topTime.setText(Html.fromHtml(StrUtils.setHtml999(null,"申请时间：",refundDataEntity.getReturn_judge_apply_time_format())));
            }
        }

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getSalesInfo();
    }

    /**
     * 獲取售後信息
     */
    private void getSalesInfo() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("rid", skipInfoEntity.pushId);
        baseHttp(null, true, Constants.API_SALE_ORDER_INFO, params);
    }

    /**
     * 取消售后
     */
    private void cancleSales(){
        MUtils.showDialog(context, "转单宝提示", "再想想", "确认", "你确认取消售后？", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MUtils.dialog.cancel();
                requestId = 3;
                loadingId = 1;
                HttpParams params = new HttpParams();
                params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
                params.put("rid", skipInfoEntity.pushId);
                baseHttp("正在取消售后...", true, Constants.API_CANCLE_SALAS, params);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==300&&resultCode==6666){
            loadingId=2;
            getSalesInfo();
        }
    }
}
