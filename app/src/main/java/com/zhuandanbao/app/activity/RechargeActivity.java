package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.alipay.Alipay;
import com.zhuandanbao.app.alipay.PayResult;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.AlipayEntity;
import com.zhuandanbao.app.entity.RechargeEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.WeixinEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;
import com.zhuandanbao.app.weixin.WXPay;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值
 * Created by BFTECH on 2017/3/13.
 */

public class RechargeActivity extends BaseListViewActivity<ShopD> {

    private RvBaseAdapter adapter;
    private List<RechargeEntity> list = new ArrayList<>();
    private View topView;
    private View bottomView;
    private ShopInfoItemEntity shopInfo;
    private EditText money;
    private String pay_way;
    private SkipInfoEntity skipInfoEntity;

    private final int ALIPAY_SDK_PAY_FLAG = 1;
    private final int ALIPAY_SDK_CHECK_FLAG = 2;
    private final int WEIXIN_SDK_PAY_ERR = 800;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 微信支付
                case WEIXIN_SDK_PAY_ERR:// 商户订单号重复或生成错误
                    Log.d("OnlinePayActivity_____", "商户订单号重复或生成错误");
//                    viewDelegate.showErrorHint("商户订单号重复或生成错误", 1, null);
                    break;
                case ALIPAY_SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                    skipInfoEntity.index=1;
                    skipInfoEntity.payCode=resultStatus;
                    viewDelegate.goToActivity(skipInfoEntity,PayResultActivity.class,0);
                    finish();
                    if (TextUtils.equals(resultStatus, "9000")) {// 支付成功
                        MLog.d("OnlinePayActivity", "______________result=" + "支付成功");
                        MLog.d("", "resultInfo=" + resultInfo);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {// 支付结果确认中
                            MLog.d("OnlinePayActivity", "______________result=" + "支付结果确认中");
//                            viewDelegate.showErrorHint("支付结果确认中", 1, null);
                        } else {// 支付失败
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            MLog.d("OnlinePayActivity", "______________result=" + "支付失败");
                            MLog.d("", "resultInfo=" + resultInfo);
                        }
                    }
                    break;
                }
                case ALIPAY_SDK_CHECK_FLAG: {
                    // 检查结果为 ;msg.obj
                    // Util.Toasts(msg.obj+" 是否支付宝",RechargeActivity.this);
                    break;
                }
            }
        }
    };

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("充值");
        list.add(new RechargeEntity(R.mipmap.img_alipay, "支付宝"));
        list.add(new RechargeEntity(R.mipmap.img_weixin, "微信"));
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, list, R.layout.item_recharge_pay, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                RechargeEntity info = (RechargeEntity) item;
                holder.setImageResource(R.id.item_img, info.img);
                holder.setText(R.id.item_content, info.conetent);
            }
        });
        topView = LayoutInflater.from(context).inflate(R.layout.item_rechage_pay_top, null);
        bottomView = LayoutInflater.from(context).inflate(R.layout.item_rechage_pay_bottom, null);
        adapter.addHeaderView(topView);
        adapter.addFooterView(bottomView);
        initVerticalRecyclerView(adapter, false, false, 1, 0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                switch (position) {
                    case 0:
                        pay_way = "alipay";
                        getPay(2);
                        break;
                    case 1:
                        pay_way = "wxpay";
                        getPay(3);
                        break;
                }
            }
        });
        money = ViewHolder.get(topView, R.id.pay_money);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            money.setText(skipInfoEntity.msg);
        }
        getShopInfo();
    }


    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            shopInfo = MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo());
            if (shopInfo.getStype().equals("2") || shopInfo.getStype().equals("3")){
                viewDelegate.showRzDialog(shopInfo,"充值用于转发订单或抢单保证金，账户提现须完成店铺认证。",true);
            }
            TextView name = ViewHolder.get(topView, R.id.pay_account);
            name.setText("充值账户：" + shopInfo.getShopkeeper() + "(" + StrUtils.mobileFormat(shopInfo.getMobile()) + ")");
        }
        if (code == 2) {//支付宝
            AlipayEntity alipayEntity = MJsonUtils.jsonToAlipayEntity(baseEntity.data);
            Alipay.getInstance(context, mHandler, alipayEntity).pay();
            MainApplication.cache.put(Constants.PAY_INFO,alipayEntity);
            setResult(Constants.SHOP_GUIDE_PASS_SUCCESS);
        }
        if (code == 3) {//微信
            WeixinEntity weixinEntity = MJsonUtils.jsonToWeixinEntity(baseEntity.data);
            WXPay.getInstance(context, mHandler, weixinEntity).doPay();
            MainApplication.cache.put(Constants.PAY_INFO,weixinEntity);
            setResult(Constants.SHOP_GUIDE_PASS_SUCCESS);
        }
    }

    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        loadingId = 2;
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("", true, Constants.API_GET_SHOP_INFO, params);
    }

    /**
     * 获取信息
     */
    private void getPay(int code) {
        if (StrUtils.isNull(money.getText().toString().trim())) {
            viewDelegate.showErrorHint("输入金额不能为空", 1, null);
            return;
        }
        int mon = Integer.parseInt(money.getText().toString().trim());
        if (mon < 10) {
            viewDelegate.showErrorHint("请输入大于10的金额", 1, null);
            return;
        }
        if (mon % 10 != 0) {
            viewDelegate.showErrorHint("输入的金额应为10的倍数", 1, null);
            return;
        }
        requestId = code;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("pay_code", pay_way);
        params.put("amount", money.getText().toString().trim());
        baseHttp("正在创建订单...", true, Constants.API_FINANCE_BANK_CARD_RECHARGE, params);
    }

}
