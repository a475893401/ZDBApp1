package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.PayD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.OrderInfoEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
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
 * 订单支付
 * Created by BFTECH on 2017/3/8.
 */

public class OrderPayActivity extends BaseListViewActivity<PayD> {
    private View topView;
    private View bottomView;
    private RvBaseAdapter adapter;
    private List<OrderItemEntity> list=new ArrayList<>();
    private SkipInfoEntity infoEntity;
    private OrderDataEntity dataEntity;
    private ShopInfoItemEntity shopInfoItemEntity;
    private Button save;
    private TextView payForgetPwd;
    private EditText payPassword;
    @Override
    protected Class<PayD> getDelegateClass() {
        return PayD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("订单支付");
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            infoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        save=viewDelegate.get(R.id.but_blue);
        save.setText("立即支付");
        save.setOnClickListener(this);
        save.setVisibility(View.VISIBLE);
        adapter=new RvBaseAdapter(context, list, R.layout.item_order_goods, new RvBaseAdapter.RefreshAdapterImpl() {
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
        adapter.addHeaderView(getTopView());
        adapter.addFooterView(getBottomView());
        initVerticalRecyclerView(adapter,true,false,1,0);
        getOrderInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            list.clear();
            OrderInfoEntity infoEntity= JSON.parseObject(baseEntity.data,OrderInfoEntity.class);
            dataEntity=JSON.parseObject(infoEntity.getOrderData(),OrderDataEntity.class);
            list.addAll(MJsonUtils.jsonToOrderItemEntity(dataEntity.getOrder_items()));
            getShopInfo();
        }
        if (code==2){
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            ShopInfoEntity shopInfoEntity=MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            shopInfoItemEntity=MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo());
            initInfo();
        }
        if (code==3){
            viewDelegate.showSuccessHint("支付成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    if(infoEntity.index==2){//发布新单
                        SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                        skipInfoEntity.isPush=false;
                        skipInfoEntity.pushId=infoEntity.pushId;
                        viewDelegate.goToActivity(skipInfoEntity,BillOrderInfoActivity.class,0);
                    }else {//详情支付
                        setResult(6666);
                    }
                    finish();
                }
            });
        }
    }

    private void initInfo(){
        viewDelegate.showShopPassDialog(shopInfoItemEntity);
        TextView paySn = ViewHolder.get(topView, R.id.order_pay_sn);
        TextView payOrderTime = ViewHolder.get(topView, R.id.pay_order_time);
        TextView payMoney = ViewHolder.get(bottomView, R.id.pay_money);//订单金额
        TextView payShopBalance = ViewHolder.get(bottomView, R.id.pay_shop_balance);//账户余额
        TextView recharge=ViewHolder.get(bottomView,R.id.pay_recharge);
        recharge.setOnClickListener(this);
        payForgetPwd = viewDelegate.get(R.id.pay_forget_pwd);//找回密码
        payPassword = viewDelegate.get(R.id.input_pay_password);
        if (StrUtils.isNotNull(dataEntity.getCreate_time())) {
            payOrderTime.setText(Html.fromHtml(StrUtils.setHtml999(null,"下单时间：",dataEntity.getCreate_time())));
        }
        paySn.setText(Html.fromHtml(StrUtils.setHtml999(null,"订单编号：",dataEntity.getOrder_sn())));
        payMoney.setText(Html.fromHtml(StrUtils.setHtml999(null,"订单金额：","￥ " + dataEntity.getOrder_amount())));
        payShopBalance.setText(Html.fromHtml(StrUtils.setHtml999(null,"当前余额：","￥ " + shopInfoItemEntity.getBalance())));
        payForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderPayActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getShopInfo();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.but_blue){
            password();
        }
        if (view.getId()==R.id.pay_recharge){
            viewDelegate.goToActivity(null,RechargeActivity.class,300);
        }
    }

    /**
     * 是否按要求输入支付密码
     */
    private void password() {
        try {
            if (TextUtils.isEmpty(payPassword.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入支付密码",1,null);
                return;
            }
            String str = shopInfoItemEntity.getBalance().trim().replaceAll(",", "");
            if (str.substring(0).equals("-")) {
                showHint();
                return;
            } else if (Double.parseDouble(str) < Double.parseDouble(dataEntity.getOrder_amount())) {
                showHint();
                return;
            }
            pay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 余额提醒
     */
    private void showHint() {
        View view = LayoutInflater.from(this).inflate(R.layout.pay_recharge_layout, null);
        TextView orderMoney = (TextView) view.findViewById(R.id.pay_money);
        TextView shopBalance = (TextView) view.findViewById(R.id.pay_balance);
        TextView payBtn = (TextView) view.findViewById(R.id.pay_btn_pop);
        orderMoney.setText("￥ " + dataEntity.getOrder_amount());
        shopBalance.setText("￥ " + shopInfoItemEntity.getBalance());
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//充值

            }
        });
        MUtils.showItemViewDialog(this,view,"支付提醒", null, null,  null, null);
    }

    /**
     * 去支付
     */
    private void pay() {
        requestId=3;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn",dataEntity.getOrder_sn());
        params.put("paypass", payPassword.getText().toString().trim());
        baseHttp("正在支付...",true,Constants.API_ORDER_PAY,params);
    }
    /**
     * 获取订单信息
     */
    private void getOrderInfo() {
        requestId=1;
        loadingId=2;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderCode",infoEntity.pushId);
        baseHttp("",true,Constants.API_ORDER_INFO,params);
    }

    /**
     * 獲取店鋪信息
     */
    private void getShopInfo() {
        requestId=2;
        loadingId=2;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null,true,Constants.API_GET_SHOP_INFO,params);
    }

    private View getTopView() {
        topView = LayoutInflater.from(this).inflate(R.layout.item_pay_top_layout, null);
        return topView;
    }

    private View getBottomView() {
        bottomView = LayoutInflater.from(this).inflate(R.layout.item_pay_bottom_layout, null);
        return bottomView;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==300&&resultCode==Constants.SHOP_GUIDE_PASS_SUCCESS){
            getShopInfo();
        }
    }
}
