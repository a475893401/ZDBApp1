package com.zhuandanbao.app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.AddSalesActivity;
import com.zhuandanbao.app.activity.BiddingShopListActivity;
import com.zhuandanbao.app.activity.BillNewOrderActivity;
import com.zhuandanbao.app.activity.ForgetPasswordActivity;
import com.zhuandanbao.app.activity.OrderCommentActivity;
import com.zhuandanbao.app.activity.OrderPayActivity;
import com.zhuandanbao.app.activity.ShopDetailsActivity;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.BillOrderInfoD;
import com.zhuandanbao.app.component.BiddingShopComponent;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.OrderInfoEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.ShowButText;
import com.zhuandanbao.app.entity.SignInfoEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.CallPhone;
import com.zhuandanbao.app.utils.DateUtils;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ToastUtil;
import com.zhuandanbao.app.utils.ViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.zhuandanbao.com.reslib.widget.TimeTextView;
import app.zhuandanbao.com.reslib.widget.time.WheelTime;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 发单详情
 * Created by BFTECH on 2017/3/8.
 */

public class BillInfoFragment extends BaseListViewFragment<BillOrderInfoD> {
    private RvBaseAdapter adapter;
    private List<OrderItemEntity> itemEntities = new ArrayList<>();
    private View topView;
    private View bottomView;
    private String orderSn = null;
    private OrderInfoEntity infoEntity;
    private OrderDataEntity dataEntity;
    private SignInfoEntity signInfo;


    public static BillInfoFragment getInstance(String orderSn) {
        BillInfoFragment fragment = new BillInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_INDEX, orderSn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected Class<BillOrderInfoD> getDelegateClass() {
        return BillOrderInfoD.class;
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, itemEntities, R.layout.item_order_goods, new RvBaseAdapter.RefreshAdapterImpl() {
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
                                        Intent intentPreview = new Intent(getActivity(), ImagePreviewDelActivity.class);
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
    protected void initD() {
        orderSn = getArguments().getString(FRAGMENT_INDEX);
        loadingId = 2;
        getBillInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            infoEntity = JSON.parseObject(baseEntity.data, OrderInfoEntity.class);
            dataEntity = JSON.parseObject(infoEntity.getOrderData(), OrderDataEntity.class);
            signInfo = JSON.parseObject(infoEntity.getSignInfo(), SignInfoEntity.class);
            itemEntities.clear();
            itemEntities.addAll(MJsonUtils.jsonToOrderItemEntity(dataEntity.getOrder_items()));
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            initBillInfo();
        }
        if (code == 2) {
            viewDelegate.showSuccessHint("已取消订单", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    activity.finish();
                }
            });
        }
        if (code == 3) {
            viewDelegate.showSuccessHint("已结算", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {//订单点评
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.data = infoEntity;
                    Intent intent = new Intent(context, OrderCommentActivity.class);
                    intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
                    startActivityForResult(intent, 300);
                }
            });
        }
        if (code==4){
            viewDelegate.showSuccessHint("已修改", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    loadingId=2;
                    getBillInfo();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_retry) {
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            skipInfoEntity.index = 2;
            skipInfoEntity.data = dataEntity;
            viewDelegate.goToActivity(skipInfoEntity, BillNewOrderActivity.class, 0);
            activity.finish();
        }
        if (view.getId() == R.id.but_cancel) {//取消
            TextView cancel = viewDelegate.get(R.id.but_cancel);
            if (cancel.getText().equals("取消订单")) {
                showCancel();
            } else if (cancel.getText().equals("申请售后")) {
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.data = dataEntity;
                Intent intent = new Intent(context, AddSalesActivity.class);
                intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
                startActivityForResult(intent, 300);
            }
        }
        if (view.getId() == R.id.but_pay) {
            TextView pay = viewDelegate.get(R.id.but_pay);
            if (pay.getText().equals("立即支付")) {

                long curTime=System.currentTimeMillis();
                if(curTime< DateUtils.getTime(dataEntity.getDelivery_date(),"yyyy-MM-dd")*1000L){//未过时效
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.pushId = dataEntity.getOrder_sn();
                    skipInfoEntity.index=1;//发单詳情页面去支付
                    Intent intent = new Intent(activity, OrderPayActivity.class);
                    intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
                    startActivityForResult(intent, 300);
                }else {
                    ToastUtil.show(context,"订单已失效");
                }

            } else if (pay.getText().equals("立即结算")) {
                accountsOrder();
            } else if (pay.getText().equals("订单点评")) {
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.data = infoEntity;
                Intent intent = new Intent(context, OrderCommentActivity.class);
                intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
                startActivityForResult(intent, 300);
            }
        }
        if (view.getId()==R.id.but_amend){
            amendGrabOrder();
        }
    }

    /**
     * 修改订单
     */
    private EditText BBOAmendMoney;
    private EditText BBOAmendPwd;
    private WheelTime wheelTime;
    private Handler handler=new Handler();
    private void amendGrabOrder() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_amend_grab_order, null);
        final TextView hint = (TextView) view.findViewById(R.id.bill_order_amend_grab_hint);
        final TextView forGetPwd = (TextView) view.findViewById(R.id.bill_order_amend_grab_forget_pwd);
        BBOAmendMoney = (EditText) view.findViewById(R.id.bill_order_amend_grab_money);
        BBOAmendPwd = (EditText) view.findViewById(R.id.bill_order_amend_grab_pwd);
        LinearLayout billOrderAmendTime = (LinearLayout) view.findViewById(R.id.bill_order_amend_grab_time);
        TextView biiOrderAmendBtn = (TextView) view.findViewById(R.id.bill_order_amend_grab_amend);
        BBOAmendMoney.setText(dataEntity.getOrder_amount());
        wheelTime = new WheelTime(billOrderAmendTime);
        Calendar calendar = Calendar.getInstance();
        if (StrUtils.isNotNull(dataEntity.getGrab_order_deadline())) {
            long ttt = Long.parseLong(dataEntity.getGrab_order_deadline());
            calendar.setTimeInMillis(ttt * 1000);
        } else {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        final int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
        wheelTime.setCyclic(false);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final Dialog dialog=builder.setView(view).create();
        dialog.show();
        forGetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        biiOrderAmendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StrUtils.isNull(BBOAmendMoney.getText().toString().trim())) {
                    hint.setVisibility(View.VISIBLE);
                    hint.setText("* 请输入订单金额");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hint.setVisibility(View.INVISIBLE);
                        }
                    }, 2500);
                    return;
                }
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    Long nowTime = System.currentTimeMillis();
                    Long amendTime = date.getTime();
                    if (amendTime <= nowTime) {
                        hint.setVisibility(View.VISIBLE);
                        hint.setText("* 请选择一个合理的时间");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hint.setVisibility(View.INVISIBLE);
                            }
                        }, 2500);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (StrUtils.isNull(BBOAmendPwd.getText().toString().trim())) {
                    hint.setVisibility(View.VISIBLE);
                    hint.setText("* 请输入支付密码");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hint.setVisibility(View.INVISIBLE);
                        }
                    }, 2500);
                    return;
                }
                dialog.cancel();
                amendOrder();
            }
        });
    }


    /**
     * 修改订单
     */
    private void amendOrder(){
        requestId=4;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", dataEntity.getOrder_sn());
        params.put("amount", BBOAmendMoney.getText().toString().trim());
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
            params.put("deadline", format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        params.put("paypass", BBOAmendPwd.getText().toString().trim());
        baseHttp("正在修改...",true,Constants.API_AMEND_GRAB_ORDER,params);
    }

    /**
     * 订单结算
     */
    private void accountsOrder() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.bill_order_accounts_dialog, null);
        TextView accountsMoney = (TextView) view.findViewById(R.id.bill_order_accounts_money);
        final EditText accountsPwd = (EditText) view.findViewById(R.id.bill_order_accounts_pwd);
        TextView forgetPwd = (TextView) view.findViewById(R.id.bill_order_accounts_forget_pwd);
        final TextView billOrderAccountsHint = (TextView) view.findViewById(R.id.bill_order_accounts_hint);
        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        accountsMoney.setText("￥ " + dataEntity.getOrder_amount());
        MUtils.showItemViewDialog(getActivity(), view, "订单结算", null, "立即结算", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StrUtils.isNull(accountsPwd.getText().toString().trim())) {
                    billOrderAccountsHint.setVisibility(View.VISIBLE);
                    billOrderAccountsHint.setText("* 请输入支付密码");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            billOrderAccountsHint.setVisibility(View.INVISIBLE);
                        }
                    }, 1500);
                    return;
                }
                MUtils.dialog.cancel();
                postAccountsOrder(accountsPwd.getText().toString().trim());
            }
        });
    }


    /**
     * 订单结算
     *
     * @param pass
     */
    private void postAccountsOrder(String pass) {
        requestId = 3;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", orderSn);
        params.put("paypass", pass);
        baseHttp("正在结算...", true, Constants.API_ORDER_ACCOUNTS, params);
    }

    /**
     * 取消订单
     */
    private void showCancel() {
        View layout = LayoutInflater.from(context).inflate(R.layout.reason_layout, null);
        final EditText input = (EditText) layout.findViewById(R.id.reason_input);
        input.setHint("请填写订单取消原因");
        MUtils.showItemViewDialog(context, layout, "订单取消原因", "返回", "确认", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StrUtils.isNull(input.getText().toString())) {
                    Toast.makeText(context, "请填写订单取消原因", Toast.LENGTH_LONG).show();
                    return;
                }
                MUtils.dialog.cancel();
                postCancelOrder(input.getText().toString().trim());
            }
        });
    }

    /**
     * 订单取消
     */
    private void postCancelOrder(String cancelReason) {
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", dataEntity.getOrder_sn());
        params.put("cancelReason", cancelReason);
        baseHttp("正在提交...", true, Constants.API_BILL_ORDER_CANCEL, params);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getBillInfo();
    }

    /**
     * 初始化
     */
    private void initBillInfo() {
        TextView orderSn = viewDelegate.get(R.id.bill_info_order_sn);
        TextView orderStatus = viewDelegate.get(R.id.bill_info_order_status_text);
        TextView orderAmount = ViewHolder.get(topView, R.id.bill_info_order_money);
        TextView jiedan = ViewHolder.get(topView, R.id.bill_info_jiedan);
        final TextView biddingNum = ViewHolder.get(topView, R.id.bill_bidding_num);
        TextView orderDvType = ViewHolder.get(bottomView, R.id.bill_info_dv_time_type);
        TextView orderDvTime = ViewHolder.get(bottomView, R.id.bill_info_dv_time);
        orderSn.setText(dataEntity.getOrder_sn());
        orderStatus.setText(dataEntity.getOrder_status_remark());
        orderAmount.setText("￥ " + dataEntity.getOrder_amount());
        if (dataEntity.getOrder_status().equals("WAIT_GRAB")) {
            if (StrUtils.isNotNull(dataEntity.getOrder_bidding_number()) && !"0".equals(dataEntity.getOrder_bidding_number())) {
                biddingNum.setVisibility(View.VISIBLE);
                biddingNum.setText(Html.fromHtml(StrUtils.setHtmlRed(null, dataEntity.getOrder_bidding_number(), "  家花店报价，立即选择店铺发单")));
                biddingNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//竞单列表
                        SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                        skipInfoEntity.pushId = dataEntity.getOrder_sn();
                        viewDelegate.goToActivity(skipInfoEntity, BiddingShopListActivity.class, 0);
                    }
                });
                if (1 != PerfHelper.getIntData(Constants.BIDDING_SHOP_HINT)) {
                    biddingNum.post(new Runnable() {
                        @Override
                        public void run() {
                            showGuideView(biddingNum);
                        }
                    });
                }
            }
        } else {
            biddingNum.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(dataEntity.getReceive_shop_name())) {
            jiedan.setText(Html.fromHtml(StrUtils.setHtml999(null, "接单店铺：", dataEntity.getReceive_shop_name())));
            jiedan.setOnClickListener(this);
            jiedan.setVisibility(View.VISIBLE);
            jiedan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.pushId = dataEntity.getReceive_sid();
                    viewDelegate.goToActivity(skipInfoEntity, ShopDetailsActivity.class, 0);
                }
            });
        } else {
            jiedan.setVisibility(View.GONE);
        }
        if (dataEntity.getDelivery_type().equals("1")) {//快递
            orderDvType.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送方式：", "快递配送")));
            if (StrUtils.isNull(dataEntity.getExpress_name()) || StrUtils.isNull(dataEntity.getExpress_sn())) {
                orderDvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流信息：", "暂无信息")));
            } else {
                orderDvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流信息：", dataEntity.getExpress_name() + "(" + dataEntity.getExpress_sn() + ")")));
            }
        } else {
            orderDvType.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送方式：", "送货上门")));
            orderDvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送时间：", dataEntity.getDelivery_date() + " " + dataEntity.getDelivery_time())));
        }
        TextView consignee = ViewHolder.get(bottomView, R.id.bill_info_consignee);
        TextView mobile = ViewHolder.get(bottomView, R.id.bill_info_phone);
        TextView address = ViewHolder.get(bottomView, R.id.bill_info_dv_address);
        consignee.setText(Html.fromHtml(StrUtils.setHtml999(null, "收货信息：", dataEntity.getConsignee())));
        mobile.setText(dataEntity.getMobile());
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallPhone.callPhone(context,dataEntity.getMobile());
            }
        });
        address.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送地址：", dataEntity.getAreainfo() + "  " + dataEntity.getAddress())));
        TextView cardMsg = ViewHolder.get(bottomView, R.id.bill_info_order_card);
        TextView orderMsg = ViewHolder.get(bottomView, R.id.bill_info_order_msg);
        if (StrUtils.isNotNull(dataEntity.getOrder_remarks())) {
            orderMsg.setVisibility(View.VISIBLE);
            orderMsg.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单备注：", dataEntity.getOrder_remarks())));
        } else {
            orderMsg.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(dataEntity.getCard_con())) {
            cardMsg.setVisibility(View.VISIBLE);
            cardMsg.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单贺卡：", dataEntity.getCard_con())));
        } else {
            cardMsg.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(infoEntity.getSignInfo())) {
            signImg();
        }
        if (StrUtils.isNotNull(dataEntity.getReal_photo())) {
            kindImg();
        }
        grabTime();
        showBut();
    }

    Guide guide;

    public void showGuideView(View view) {
        PerfHelper.setInfo(Constants.BIDDING_SHOP_HINT, 1);
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(view)
                .setAlpha(150)
                .setHighTargetCorner(0)
                .setHighTargetPadding(0)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
            }
        });
        builder.addComponent(new BiddingShopComponent());
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(getActivity());
    }

    /**
     * 签收照
     */
    private void signImg() {
        List<ImageItem> signImgList = new ArrayList<>();
        LinearLayout signLayout = ViewHolder.get(bottomView, R.id.sign_layout);
        TextView signName = ViewHolder.get(bottomView, R.id.order_info_sign_info);
        RecyclerView recyclerView = ViewHolder.get(bottomView, R.id.sign_recyclerView);
        if (null != signInfo && StrUtils.isNotNull(signInfo.getSign_user())) {
            signLayout.setVisibility(View.VISIBLE);
            signName.setText(signInfo.getSign_user() + " | " + MUtils.getTo66TimePHP(signInfo.getSign_time(), "yyyy-MM-dd HH:mm:ss"));
            if (StrUtils.isNotNull(signInfo.getSign_voucher())) {
                List<String> signList = JSON.parseArray(signInfo.getSign_voucher(), String.class);
                for (String s : signList) {
                    ImageItem img = new ImageItem();
                    img.url = s;
                    signImgList.add(img);
                }
            }
            final ImagePickerAdapter adapter = new ImagePickerAdapter(context, signImgList, signImgList.size(), true);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //打开预览
                    Intent intentPreview = new Intent(getActivity(), ImagePreviewDelActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                    startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                }
            });
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * 实物照
     */
    private void kindImg() {
        List<ImageItem> list = new ArrayList<>();
        LinearLayout kindLayout = ViewHolder.get(bottomView, R.id.order_info_kind_layout);
        RecyclerView recyclerView = ViewHolder.get(bottomView, R.id.kind_recyclerView);
        if (StrUtils.isNotNull(dataEntity.getReal_photo())) {
            List<String> kindList = JSON.parseArray(dataEntity.getReal_photo(), String.class);
            for (String info : kindList) {
                MLog.e("real_photo==" + info);
                ImageItem imageItem = new ImageItem();
                imageItem.url = info;
                list.add(imageItem);
            }
            if (kindList.size() > 0) {
                kindLayout.setVisibility(View.VISIBLE);
                final ImagePickerAdapter adapter = new ImagePickerAdapter(context, list, list.size(), true);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //打开预览
                        Intent intentPreview = new Intent(getActivity(), ImagePreviewDelActivity.class);
                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                        startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                    }
                });
            }
        } else {
            kindLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 操作
     */
    private void showBut() {
        ShowButText butText = new ShowButText();
        butText.isShowOnline = "复制重发";
        if (dataEntity.getOrder_status().equals("REFUND")
                || dataEntity.getOrder_status().equals("CANCELED")
                || dataEntity.getOrder_status().equals("RECALL")) {
            butText.isShowOk = null;
            butText.isShowCancle = null;
            butText.isShowAmend = null;
        }
        if (dataEntity.getOrder_status().equals("FINISHED") && dataEntity.getIs_comment().equals("N")) {
            butText.isShowOk = "订单点评";
            butText.isShowCancle = null;
            butText.isShowAmend = null;
        }
        if (dataEntity.getOrder_status().equals("PROCESSING")
                && "0".equals(dataEntity.getIs_refund())) {
            butText.isShowOk = null;
            butText.isShowCancle = "申请售后";
            butText.isShowAmend = null;
        }
        MLog.e("is_complain=" + dataEntity.getIs_complain() + "is_refund=" + dataEntity.getIs_refund());
        if (dataEntity.getOrder_status().equals("HAS_BEEN_SERVED")) {
            butText.isShowCancle = null;
            butText.isShowAmend = null;
            if (dataEntity.getIs_complain().equals("Y") || dataEntity.getIs_refund().equals("1")) {
                butText.isShowOk = null;
            } else {
                butText.isShowOk = "立即结算";
            }
            if (dataEntity.getIs_refund().equals("0")) {
                butText.isShowCancle = "申请售后";
            } else if (dataEntity.getIs_complain().equals("N")) {
                butText.isShowCancle = null;
            }
        }
        if (dataEntity.getOrder_status().equals("WAIT_PAY")) {
            butText.isShowOk = "立即支付";
            butText.isShowCancle = "取消订单";
            butText.isShowAmend = null;
            if (dataEntity.getOrder_type().equals("1")) {
                butText.isShowAmend = "修改订单";
            }
        }
        if (dataEntity.getOrder_status().equals("WAIT_CONFIRM") ||
                dataEntity.getOrder_status().equals("WAIT_GRAB")) {
            butText.isShowOk = null;
            butText.isShowCancle = "取消订单";
            butText.isShowAmend = null;
            if (dataEntity.getOrder_type().equals("1")) {
                butText.isShowAmend = "修改订单";
            }
        }
        MUtils.showBillInfoBut((LinearLayout) viewDelegate.get(R.id.bill_info_but_layout), butText, this);
    }

    /**
     * 搶單時間
     */
    private void grabTime() {
        //待确认  要不要显示倒计时
        LinearLayout grabLayout = ViewHolder.get(bottomView, R.id.bill_info_grab_time_layout);
        TimeTextView grabTime = ViewHolder.get(bottomView, R.id.bill_info_grab_time);
        if (StrUtils.isNotNull(dataEntity.getGrab_order_deadline()) && (dataEntity.getOrder_status().equals("WAIT_GRAB")
                || dataEntity.getOrder_status().equals("WAIT_PAY"))) {
            grabLayout.setVisibility(View.VISIBLE);
            if (grabTime != null) {//截止时间
                Long now_time = System.currentTimeMillis() / 1000;
                long[] showTime = MUtils.callBacKTime(now_time, Long.parseLong(dataEntity.getGrab_order_deadline()));
                grabTime.setTimes(showTime, "系统将在 ", " 后结束抢单。");
                // 已经在倒计时的时候不再开启计时
                if (!grabTime.isFlagStart()) {
                    grabTime.isStart();
                }
            }
        } else {
            grabLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 獲取發單詳情
     */
    private void getBillInfo() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderCode", orderSn);
        baseHttp("", true, Constants.API_ORDER_INFO, params);
    }

    /**
     * 发单底部
     *
     * @return
     */
    private View getBottomView() {
        bottomView = LayoutInflater.from(context).inflate(R.layout.item_bill_order_info, null);
        return bottomView;
    }

    /**
     * 发单头部
     *
     * @return
     */
    private View getTopView() {
        topView = LayoutInflater.from(context).inflate(R.layout.item_top_bill_order_info, null);
        return topView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == 6666) {
            loadingId = 2;
            getBillInfo();
        }
    }
}
