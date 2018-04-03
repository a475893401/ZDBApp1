package com.zhuandanbao.app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blog.www.guideview.Component;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ItemAdapter;
import com.zhuandanbao.app.adapter.MBaseAdapter;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.OrderInfoD;
import com.zhuandanbao.app.component.BiddingComponent;
import com.zhuandanbao.app.component.OnLineCompontent;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ConditionEntity;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.OrderInfoEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopLatEntity;
import com.zhuandanbao.app.entity.ShowButText;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.mvp.view.HttpCallback;
import com.zhuandanbao.app.utils.GDMapDis;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import app.zhuandanbao.com.reslib.widget.ListViewForScrollView;
import app.zhuandanbao.com.reslib.widget.TimeTextView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 抢单详情
 * Created by BFTECH on 2017/3/6.
 */

public class GradInfoActivity extends BaseListViewActivity<OrderInfoD> {

    private SkipInfoEntity skipInfoEntity;
    private RvBaseAdapter adapter;
    private List<ConditionEntity> list = new ArrayList<>();
    private OrderInfoEntity infoEntity;
    private OrderDataEntity dataEntity;
    private View topView;
    private View contentView;
    private View conView;
    private LinearLayout butLayout;
    private ShowButText butInfo;

    @Override
    protected Class<OrderInfoD> getDelegateClass() {
        return OrderInfoD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (skipInfoEntity.isPush) {
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.index = Constants.GRAB_ORDER;
                    viewDelegate.goToActivity(skipInfoEntity, MainActivity.class, 0);
                    finish();
                } else {
                    finish();
                }
            }
        });
        viewDelegate.setTitle("抢单详情");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        butLayout = viewDelegate.get(R.id.but_layout);
        adapter = new RvBaseAdapter(context, list, R.layout.item_order_info_img, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {

            }
        });
        adapter.addHeaderView(getTopView());
        adapter.addHeaderView(getContentView());
        adapter.addHeaderView(getConView());
        initVerticalRecyclerView(adapter, true, false, 3, 4);
        butInfo = new ShowButText();
        loadingId = 2;
        getGradInfo();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getGradInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && (resultCode == Constants.SHOP_GUIDE_INFO_SUCCESS
                || resultCode == Constants.SHOP_GUIDE_CONTACT_SUCCESS
                || resultCode == Constants.SHOP_GUIDE_RE_SUCCESS)) {
            loadingId = 2;
            getGradInfo();
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            getShopInfo();
            list.clear();
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            infoEntity = JSON.parseObject(baseEntity.data, OrderInfoEntity.class);
            dataEntity = JSON.parseObject(infoEntity.getOrderData(), OrderDataEntity.class);
            initTopInfo(dataEntity.getOrder_items());
        }
        if (code == 2) {
            viewDelegate.showSuccessHint("报价成功，等待发单方确认！", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    loadingId = 2;
                    getGradInfo();
                }
            });
        }
        if (code == 3) {
            viewDelegate.showSuccessHint("成功抢单", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {//跳接单详情
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.isPush = false;
                    skipInfoEntity.pushId = dataEntity.getOrder_sn();
                    viewDelegate.goToActivity(skipInfoEntity, TakingOrderInfoActivity.class, 0);
                    finish();
                }
            });
        }
        if (code == 4) {
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            viewDelegate.showDialog(MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo()), false);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_online_layout) {//在线沟通
            impower();
        }
        if (view.getId() == R.id.but_ok) {//立即搶單
            MUtils.showDialog(context, "抢单提示", "取消", "确认", getResources().getString(R.string.orders_grab_message), null, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MUtils.dialog.cancel();
                    gradOrder();
                }
            });
        }
        if (view.getId() == R.id.but_blue) {//报价竞单
            showBidding();
        }
    }

    private static final int REQUEST_CODE_PERMISSION_LOCATION = 100;

    private static final int REQUEST_CODE_SETTING = 300;
    /**
     * 授权
     */
    private void impower() {
        // 申请权限。
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_LOCATION)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(rationaleListener)
                .start();
    }

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_LOCATION: {
                    im();
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_LOCATION: {
                    viewDelegate.showErrorHint("你已禁止了存储空间权限",1,null);
                    break;
                }
            }
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(activity, REQUEST_CODE_SETTING).show();

                // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingHandle = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingHandle.execute();
//            你的dialog点击了取消调用：
//            settingHandle.cancel();
            }
        }
    };

    /**
     * 单聊
     */
    private void im(){
        final String target = dataEntity.getSource_sid(); //消息接收者ID
        final String appkey = ImLoginHelper.IM_APP_KEY; //消息接收者appKey
        try {
            Intent intent = ImLoginHelper.getInstance().getIMKit().getChattingActivityIntent(target, appkey);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ImLoginHelper.getInstance().imLogin(activity, PerfHelper.getStringData(ImLoginHelper.IM_USER_ID_NAME) + ImLoginHelper.Im_PASSWORD, new HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    final String target = dataEntity.getSource_sid(); //消息接收者ID
                    final String appkey = ImLoginHelper.IM_APP_KEY; //消息接收者appKey
                    Intent intent = ImLoginHelper.getInstance().getIMKit().getChattingActivityIntent(target, appkey);
                    startActivity(intent);
                }
                @Override
                public void onError(int code, String message) {
                }
            });
        }
    }

    /**
     * Rationale支持，这里自定义对话框。
     *  这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
     *AndPermission.rationaleDialog(Context, Rationale).show();
     * 自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            MUtils.showDialog(context, "转单宝提示", "取消", "去设置", "你已禁止了存储空间权限，不能在线沟通，请手动去设置？", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MUtils.dialog.cancel();
                    rationale.cancel();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MUtils.dialog.cancel();
                    rationale.resume();
                }
            });
        }
    };

    /**
     * 抢单
     */
    private void gradOrder() {
        requestId = 3;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderId", dataEntity.getOrder_sn());
        baseHttp("正在提交...", true, Constants.API_ACTION_ORDERS_GRAB, params);
    }

    /**
     * 竞价
     */
    private void showBidding() {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_bidding_layout, null);
        Button button = ViewHolder.get(view, R.id.but_blue);
        ImageView imageView = ViewHolder.get(view, R.id.delete);
        TextView orderMoney = ViewHolder.get(view, R.id.bidding_order_money);
        final EditText biddingMoney = ViewHolder.get(view, R.id.bidding_money);
        orderMoney.setText("￥ " + dataEntity.getOrder_amount());
        button.setText("确认");
        button.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StrUtils.isNull(biddingMoney.getText().toString().trim())) {
                    viewDelegate.showErrorHint("请输入竞单价", 1, null);
                    return;
                }
                if (!StrUtils.isDigital(biddingMoney.getText().toString().trim())) {
                    viewDelegate.showErrorHint("请输入正确的金额", 1, null);
                    return;
                }
                dialog.dismiss();
                bidding(biddingMoney.getText().toString().trim());
            }
        });
    }

    /**
     * 竞价
     *
     * @param money
     */
    private void bidding(String money) {
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderCode", skipInfoEntity.pushId);
        params.put("bidding_price", money);
        baseHttp("正在报价...", true, Constants.API_ORDER_BIDDING, params);
    }

    /**
     * 初始化商品
     */
    private void initTopInfo(String itemStr) {
        TextView money = ViewHolder.get(contentView, R.id.order_info_money);
        TextView status = ViewHolder.get(contentView, R.id.order_info_state);
        TextView biddingMoney = ViewHolder.get(contentView, R.id.order_bidding_info_money);
        TextView dvTime = ViewHolder.get(contentView, R.id.order_info_dv_time);
        TextView address = ViewHolder.get(contentView, R.id.order_info_address);
        LinearLayout gradLayout = ViewHolder.get(contentView, R.id.order_info_time_layout);
        TimeTextView gradTime = ViewHolder.get(contentView, R.id.order_info_time);
        ListViewForScrollView conList = ViewHolder.get(conView, R.id.con_list);
        ImageView mapImg = ViewHolder.get(contentView, R.id.order_info_gd_map);
        mapImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopLatEntity info = (ShopLatEntity) MainApplication.cache.getAsObject(Constants.SHOP_LAT_INFO);
                if (null == info) {
                    viewDelegate.showErrorHint("沒有获取到地址信息", 1, null);
                    return;
                } else {
                    GDMapDis.showGDMap(activity, dataEntity, info);
                }
            }
        });
        money.setText(Html.fromHtml(StrUtils.setHtml999(null, "金额：", "￥ " + dataEntity.getOrder_amount())));
        status.setText(dataEntity.getOrder_status_remark());
        address.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送地址：", dataEntity.getAreainfo() + "  " + dataEntity.getAddress())));
        if (!dataEntity.getOrder_bidding_price().equals("0")) {
            biddingMoney.setVisibility(View.VISIBLE);
            biddingMoney.setText(Html.fromHtml(StrUtils.setHtml999(null, "您的竞价：", StrUtils.setHtmlRed(null, "￥ " + dataEntity.getOrder_bidding_price(), null) + " | " + dataEntity.getOrder_bidding_time())));
        }
        if (dataEntity.getDelivery_type().equals("1")) {
            dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送方式：", "快递配送")));
        } else {
            dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送时间：", dataEntity.getDelivery_date() + "  " + dataEntity.getDelivery_time())));
        }
        if (dataEntity.getOrder_status().equals("WAIT_GRAB")) {
            gradLayout.setVisibility(View.VISIBLE);
            Long now_time = System.currentTimeMillis() / 1000;
            long[] showTime = MUtils.callBacKTime(now_time, Long.parseLong(dataEntity.getGrab_order_deadline()));
            gradTime.setTimes(showTime, null, "后截止抢单");
            gradTime.setShowText();
            //  已经在倒计时的时候不再开启计时
            if (!gradTime.isFlagStart()) {
                gradTime.isStart();
            }
        } else if (StrUtils.isNotNull(dataEntity.getReceive_shop_name()) && StrUtils.isNotNull(dataEntity.getConfirm_time())) {
            gradLayout.setVisibility(View.VISIBLE);
            gradTime.setText(Html.fromHtml(StrUtils.setHtmlRed(null, dataEntity.getReceive_shop_name(), " 抢单成功")));
            gradLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.pushId = dataEntity.getReceive_sid();
                    viewDelegate.goToActivity(skipInfoEntity, ShopDetailsActivity.class, 0);
                }
            });
        } else {
            gradLayout.setVisibility(View.GONE);
        }
        List<ConditionEntity> list = new ArrayList<>();
        list.addAll(MJsonUtils.jsonToConditionEntity(infoEntity.getTest_grab_condition()));
        if (list.size() > 0) {
            conView.setVisibility(View.VISIBLE);
            conList.setAdapter(new ConAdapter(list, context));
        } else {
            conView.setVisibility(View.GONE);
        }
        final List<OrderItemEntity> orderItemEntities = MJsonUtils.jsonToOrderItemEntity(itemStr);
        initRefreshTopInfo(orderItemEntities.get(0));
        LinearLayout layout = ViewHolder.get(topView, R.id.order_info_gv_layout);
        GridView gridView = ViewHolder.get(topView, R.id.order_info_gv);
        TextView totalNum = ViewHolder.get(topView, R.id.order_info_total_num);
        if (orderItemEntities.size() > 1) {
            layout.setVisibility(View.VISIBLE);
            totalNum.setText("共 " + orderItemEntities.size() + " 件");
            final ItemAdapter adapter = new ItemAdapter(orderItemEntities, context);
            adapter.setIndex(0);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    adapter.setIndex(i);
                    adapter.notifyDataSetChanged();
                    initRefreshTopInfo(orderItemEntities.get(i));
                }
            });
        } else {
            layout.setVisibility(View.GONE);
        }
        if (dataEntity.getOrder_status().equals("WAIT_GRAB")) {//待抢单
            butInfo.isShowOnline = "在线沟通";
            if (list.size() == 0) {
                butInfo.isShowOk = "立即抢单";
            }
            if (dataEntity.getOrder_bidding_price().equals("0") && list.size() == 0) {
                butInfo.isShowAmend = "报价竞单";
            } else {
                butInfo.isShowAmend = "";
            }
        } else {
            butInfo.isShowOnline = "";
            butInfo.isShowAmend = "";
            butInfo.isShowCancle = "";
            butInfo.isShowOk = "";
        }
        MUtils.showGradBut(butLayout, butInfo, this);
        if (StrUtils.isNotNull(butInfo.isShowOnline) && 1 != PerfHelper.getIntData(Constants.BIDDING_BUT_HINT)) {
            final Button button = viewDelegate.get(R.id.but_blue);
            button.post(new Runnable() {
                @Override
                public void run() {
                    showBiddingGuideView(button);
                }
            });
        }
    }

    private Guide guide;

    public void showBiddingGuideView(View view) {
        PerfHelper.setInfo(Constants.BIDDING_BUT_HINT, 1);
        final GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(view)
                .setFullingViewId(R.id.but_layout)
                .setAlpha(150)
                .setHighTargetCorner(0)
                .setHighTargetPadding(1)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                if (StrUtils.isNotNull(butInfo.isShowOnline)) {
                    LinearLayout qq = viewDelegate.get(R.id.but_online_layout);
                    showGuideView2(qq);
                }
            }
        });
        builder.addComponent(new BiddingComponent());
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(activity);
    }

    public void showGuideView2(View view) {
        final GuideBuilder builder1 = new GuideBuilder();
        builder1.setTargetView(view)
                .setAlpha(150)
                .setHighTargetPadding(1)
                .setHighTargetGraphStyle(Component.ROUNDRECT)
                .setOverlayTarget(false)
                .setExitAnimationId(android.R.anim.fade_out)
                .setOutsideTouchable(false);
        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
            }
        });
        builder1.addComponent(new OnLineCompontent());
        Guide guide = builder1.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(activity);
    }

    /**
     * 多個商品刷新
     *
     * @param info
     */
    private void initRefreshTopInfo(OrderItemEntity info) {
        if (null == info) {
            return;
        }
        TextView orderInfoName = ViewHolder.get(topView, R.id.order_info_name);
        TextView orderInfoNum = ViewHolder.get(topView, R.id.order_info_num);
        TextView orderInfoDsc = ViewHolder.get(topView, R.id.order_info_dsc);
        BGABanner bgaBanner = ViewHolder.get(topView, R.id.order_info_banner);
        orderInfoName.setText(info.getItem_name());
        orderInfoDsc.setText(info.getItem_remarks());
        orderInfoNum.setText("X " + info.getItem_total());
        List<String> itemList = null;
        if (null == itemList) {
            itemList = new ArrayList<>();
        } else {
            itemList.clear();
        }
        itemList.addAll(JSON.parseArray(info.getItem_img(), String.class));
        if (itemList.size() == 1) {
            bgaBanner.setAutoPlayAble(false);
        } else {
            bgaBanner.setAutoPlayAble(true);
        }
        if (itemList.size() != 0) {
            bgaBanner.setAdapter(new BGABanner.Adapter() {
                @Override
                public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                    String picpath = (String) model;
                    ImageLoader.getInstance().displayImage(picpath, (ImageView) view, MyImage.deploy());
                }
            });
            bgaBanner.setData(itemList, null);
        }
    }

    private View getTopView() {
        topView = LayoutInflater.from(context).inflate(R.layout.item_order_info_top_layout, null);
        return topView;
    }

    private View getContentView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.item_order_info_content_layout, null);
        return contentView;
    }

    private View getConView() {
        conView = LayoutInflater.from(context).inflate(R.layout.item_con_layout, null);
        return conView;
    }

    /**
     * 获取抢单详情
     */
    private void getGradInfo() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderCode", skipInfoEntity.pushId);
        baseHttp(null, true, Constants.API_ORDER_INFO, params);
    }

    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        requestId = 4;
        loadingId = 0;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("", true, Constants.API_GET_SHOP_INFO, params);
    }

    private class ConAdapter extends MBaseAdapter {
        private Context context;
        private List<ConditionEntity> list;

        public ConAdapter(List list, Context context) {
            super(list, context);
            this.list = list;
            this.context = context;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = LayoutInflater.from(context).inflate(R.layout.item_grad_con_layout, null);
            }
            LinearLayout layout = ViewHolder.get(view, R.id.con_layout);
            TextView name = ViewHolder.get(view, R.id.con_name);
            TextView hint = ViewHolder.get(view, R.id.con_hint);
            name.setText(list.get(i).getDesc());
            if (list.get(i).getId() == 5) {//未认证
                hint.setText("立即认证");
                hint.setVisibility(View.VISIBLE);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                        skipInfoEntity.index = 1;
                        viewDelegate.goToActivity(skipInfoEntity, ApproveActivity.class, 300);//认证后因刷新
                    }
                });
            } else if (list.get(i).getId() == 9) {
                hint.setText("立即充值");
                hint.setVisibility(View.VISIBLE);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewDelegate.goToActivity(null, RechargeActivity.class, 300);//认证后因刷新
                    }
                });
            } else {
                hint.setVisibility(View.GONE);
            }
            return view;
        }
    }

}
