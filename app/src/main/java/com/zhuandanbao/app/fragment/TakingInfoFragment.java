package com.zhuandanbao.app.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.ShopDetailsActivity;
import com.zhuandanbao.app.activity.SignOrderActivity;
import com.zhuandanbao.app.activity.WuliuActivity;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.adapter.ItemAdapter;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.OrderInfoD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.OrderInfoEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.RefundInfoEntity;
import com.zhuandanbao.app.entity.ShopLatEntity;
import com.zhuandanbao.app.entity.ShowButText;
import com.zhuandanbao.app.entity.SignInfoEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.SystemSettingEntity;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.mvp.view.HttpCallback;
import com.zhuandanbao.app.utils.CallPhone;
import com.zhuandanbao.app.utils.FileUtil;
import com.zhuandanbao.app.utils.GDMapDis;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.PictureUtil;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 接单详情
 * Created by BFTECH on 2017/3/6.
 */

public class TakingInfoFragment extends BaseListViewFragment<OrderInfoD> {
    private String orderSn = null;
    private RvBaseAdapter adapter;
    private List<String> list = new ArrayList<>();
    private View topView;
    private View contentView;
    private View dvView;
    private OrderInfoEntity infoEntity;
    private OrderDataEntity dataEntity;
    private SignInfoEntity signInfoEntity;
    private RefundInfoEntity refundInfoEntity;
    private ShowButText butInfo;
    private LinearLayout butLayout;
    private List<ImageItem> kindList = new ArrayList<>();
    private ImagePickerAdapter kindAdapter;
    private int imgNum = 3;
    private List<ImageItem> signList = new ArrayList<>();
    private boolean isKind = true;
    private ImagePickerAdapter signAdqpter;
    private boolean isDetele = false;
    private int maxImgCount = 3;

    private SystemSettingEntity settingEntity = null;

    public static TakingInfoFragment getInstance(String orderSn) {
        TakingInfoFragment fragment = new TakingInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_INDEX, orderSn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initD() {
        orderSn = getArguments().getString(FRAGMENT_INDEX);
        loadingId = 2;
        getTakingInfo();
    }

    @Override
    protected Class<OrderInfoD> getDelegateClass() {
        return OrderInfoD.class;
    }

    @Override
    public void initView() {
        super.initView();
        if (null != MainApplication.cache.getAsObject(Constants.SYSTEM_SETTING)) {
            settingEntity = (SystemSettingEntity) MainApplication.cache.getAsObject(Constants.SYSTEM_SETTING);
        }
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        butLayout = viewDelegate.get(R.id.but_layout);
        adapter = new RvBaseAdapter(context, list, R.layout.item_order_info_img, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {

            }
        });
        adapter.addHeaderView(getTopView());
        adapter.addHeaderView(getContentView());
        adapter.addHeaderView(getDvView());
        initVerticalRecyclerView(adapter, true, false, 1, 0);
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            list.clear();
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            infoEntity = JSON.parseObject(baseEntity.data, OrderInfoEntity.class);
            dataEntity = JSON.parseObject(infoEntity.getOrderData(), OrderDataEntity.class);
            signInfoEntity = JSON.parseObject(infoEntity.getSignInfo(), SignInfoEntity.class);
            refundInfoEntity = JSON.parseObject(infoEntity.getRefundInfo(), RefundInfoEntity.class);
            initShopInfo();
        }
        if (code == 2) {
            kindList.addAll(MJsonUtils.jsonToImageItem(baseEntity.data));
            kindAdapter.setImages(kindList);
        }
        if (code == 3) {
            viewDelegate.showSuccessHint("刪除成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    loadingId = 2;
                    getTakingInfo();
                }
            });
        }
        if (code == 4) {
            viewDelegate.showSuccessHint("成功接单", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    loadingId = 2;
                    getTakingInfo();
                }
            });
        }
        if (code == 5) {
            viewDelegate.showSuccessHint("你已同意退款", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    activity.finish();
                }
            });
        }
        if (code == 8) {
            viewDelegate.showSuccessHint("已拒绝接单", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    activity.finish();
                }
            });
        }
        if (code == 9) {
            viewDelegate.showSuccessHint("已拒绝退款", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    activity.finish();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_online_layout) {//在线
            impower();
        }
        if (view.getId() == R.id.but_ok) {
            if (butInfo.isShowOk.equals("立即接单")) {
                jieDan();
            } else if (butInfo.isShowOk.equals("同意退款")) {
                agreeRefund();
            } else if (butInfo.isShowOk.equals("确认发货")) {
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.index = 1;
                skipInfoEntity.msg = dataEntity.getConsignee() + "，" + dataEntity.getMobile() + "\n"
                        + dataEntity.getAreainfo() + " " + dataEntity.getAddress();
                skipInfoEntity.pushId = dataEntity.getOrder_sn();
                skipInfoEntity.payCode = dataEntity.getExpress_sn();
                skipInfoEntity.mobile = dataEntity.getExpress_name();
                skipInfoEntity.expressId = dataEntity.getExpress();
                Intent intent = new Intent(activity, WuliuActivity.class);
                intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
                startActivityForResult(intent, 300);
            } else if (butInfo.isShowOk.equals("立即签收")) {
                //"您还未上传实物照，上传后可以增加发单方对您的信任度！以便获取更多的订单。"
                if (kindList.size() > 0) {
                    getToSign();
                } else if (null != settingEntity && settingEntity.getZD_ORDER_MUST_REAL_PHOTO().equals("YES")) {//必传
                    MUtils.showDialog(activity, "转单宝提示", "暂不上传", "立即上传", settingEntity.getZD_ORDER_MUST_REAL_PHOTO_EXPLAIN(), null, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MUtils.dialog.cancel();
                            isKind = true;
                            ImagePicker.getInstance().setSelectLimit(maxImgCount - kindList.size());
                            Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                            startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
                        }
                    });
                } else {
                    getToSign();
                }
            }
        }
        if (view.getId() == R.id.but_cancle) {
            View reason = getReasonLayout();
            final EditText input = (EditText) reason.findViewById(R.id.reason_input);
            if (butInfo.isShowCancle.equals("拒绝接单")) {
                MUtils.showItemViewDialog(getActivity(), reason, "拒绝接单", "返回", "确认", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StrUtils.isNull(input.getText().toString())) {
                            Toast.makeText(getActivity(), "请填写拒绝原因", Toast.LENGTH_LONG).show();
                            return;
                        }
                        MUtils.dialog.cancel();
                        rejectJieDan(input.getText().toString());
                    }
                });
            } else if (butInfo.isShowCancle.equals("拒绝退款")) {
                MUtils.showItemViewDialog(getActivity(), reason, "拒绝退款", "返回", "确认", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StrUtils.isNull(input.getText().toString())) {
                            Toast.makeText(getActivity(), "请填写拒绝原因", Toast.LENGTH_LONG).show();
                            return;
                        }
                        MUtils.dialog.cancel();
                        unAgreeRefund(input.getText().toString());
                    }
                });
            } else if (butInfo.isShowCancle.equals("修改物流")) {
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.index = 1;
                skipInfoEntity.msg = dataEntity.getConsignee() + "，" + dataEntity.getMobile() + "\n"
                        + dataEntity.getAreainfo() + " " + dataEntity.getAddress();
                skipInfoEntity.pushId = dataEntity.getOrder_sn();
                skipInfoEntity.payCode = dataEntity.getExpress_sn();
                skipInfoEntity.mobile = dataEntity.getExpress_name();
                skipInfoEntity.expressId = dataEntity.getExpress();
                Intent intent = new Intent(activity, WuliuActivity.class);
                intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
                startActivityForResult(intent, 300);
            }
        }
    }
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 100;

    private static final int REQUEST_CODE_SETTING = 3000;
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
            if (AndPermission.hasAlwaysDeniedPermission(getActivity(), deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(getActivity(), REQUEST_CODE_SETTING).show();

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
     * Rationale支持，这里自定义对话框。
     *  这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
     *AndPermission.rationaleDialog(Context, Rationale).show();
     * 自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            MUtils.showDialog(getActivity(), "转单宝提示", "取消", "去设置", "你已禁止了存储空间权限，不能在线沟通，请手动去设置？", new View.OnClickListener() {
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
     * 签收
     */
    private void getToSign() {
        SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
        skipInfoEntity.pushId = dataEntity.getOrder_sn();
        skipInfoEntity.msg = dataEntity.getConfirm_time();
        Intent intent = new Intent(activity, SignOrderActivity.class);
        intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
        startActivityForResult(intent, 300);
    }

    /**
     * 接单
     */
    private void jieDan() {
        requestId = 4;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderId", dataEntity.getOrder_sn());
        baseHttp("正在提交...", true, Constants.API_ACTION_ORDERS_CONFIRM, params);
    }

    /**
     * 同意退款
     */
    private void agreeRefund() {
        requestId = 5;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("ISAgree", "Y");
        params.put("reason", "同意退款");
        params.put("refundId", refundInfoEntity.getId());
        baseHttp("正在提交...", true, Constants.API_ACTION_ORDERS_RECEIVED, params);
    }


    /**
     * 拒绝接单
     */
    private void rejectJieDan(String reasonStr) {
        requestId = 8;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("cancelReason", reasonStr);
        params.put("orderId", dataEntity.getOrder_sn());
        baseHttp("正在提交...", true, Constants.API_ACTION_ORDERS_CANCEL, params);
    }

    /**
     * 拒绝退款
     */
    private void unAgreeRefund(String reasonStr) {
        requestId = 9;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("ISAgree", "N");
        params.put("reason", reasonStr);
        params.put("refundId", refundInfoEntity.getId());
        baseHttp("正在提交...", true, Constants.API_ACTION_ORDERS_RECEIVED, params);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getTakingInfo();
    }

    /**
     * 初始化
     */
    private void initShopInfo() {
        TextView money = ViewHolder.get(contentView, R.id.order_info_money);
        TextView status = ViewHolder.get(contentView, R.id.order_info_state);
        final TextView dvTime = ViewHolder.get(contentView, R.id.order_info_dv_time);
        TextView address = ViewHolder.get(contentView, R.id.order_info_address);
        ImageView mapImg = ViewHolder.get(contentView, R.id.order_info_gd_map);
        TextView cardInfo = ViewHolder.get(dvView, R.id.order_info_card);
        TextView msgInfo = ViewHolder.get(dvView, R.id.order_info_msg);
        TextView sourceName = ViewHolder.get(dvView, R.id.order_info_source_name);
        TextView orderSn = ViewHolder.get(dvView, R.id.order_info_sn);
        LinearLayout infoLayout = ViewHolder.get(contentView, R.id.order_info_goods_layout);
        TextView name = ViewHolder.get(contentView, R.id.order_info_goods_name);
        TextView phone = ViewHolder.get(contentView, R.id.order_info_goods_phone);
        LinearLayout expressLayout = ViewHolder.get(contentView, R.id.order_info_express_layout);
        TextView expressName = ViewHolder.get(contentView, R.id.order_info_express_name);
        TextView expressSn = ViewHolder.get(contentView, R.id.order_info_express_sn);
        if (StrUtils.isNotNull(dataEntity.getExpress_name()) && StrUtils.isNotNull(dataEntity.getExpress_sn())) {
            expressLayout.setVisibility(View.VISIBLE);
            expressName.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流名称：", dataEntity.getExpress_name())));
            expressSn.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流单号：", dataEntity.getExpress_sn())));
        } else {
            expressLayout.setVisibility(View.GONE);
        }
        orderSn.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单编号：", dataEntity.getOrder_sn())));
        if (StrUtils.isNotNull(dataEntity.getCard_con())) {
            cardInfo.setVisibility(View.VISIBLE);
            cardInfo.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单贺卡：", dataEntity.getCard_con())));
        } else {
            cardInfo.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(dataEntity.getOrder_remarks())) {
            msgInfo.setVisibility(View.VISIBLE);
            msgInfo.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单备注：", dataEntity.getOrder_remarks())));
        } else {
            msgInfo.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(dataEntity.getSource_shop_name())) {
            sourceName.setText(Html.fromHtml(StrUtils.setHtml999(null, "发单店铺：", dataEntity.getSource_shop_name())));
            sourceName.setOnClickListener(new View.OnClickListener() {//店铺详情
                @Override
                public void onClick(View view) {
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.pushId = dataEntity.getSource_sid();
                    viewDelegate.goToActivity(skipInfoEntity, ShopDetailsActivity.class, 0);
                }
            });
        }
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
        address.setText(Html.fromHtml(StrUtils.setHtml999(null, "地址：", dataEntity.getAreainfo() + "  " + dataEntity.getAddress())));
        // 判断配送方式 快递派送 1:快递派送 其余送货上门
        if (dataEntity.getDelivery_type().equals("1")) {
            dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "配送方式：", "快递配送")));
        } else {
            dvTime.setText(Html.fromHtml(StrUtils.setHtml999(null, "时间：", dataEntity.getDelivery_date() + "  " + dataEntity.getDelivery_time())));
        }
        final List<OrderItemEntity> orderItemEntities = MJsonUtils.jsonToOrderItemEntity(dataEntity.getOrder_items());
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
        infoLayout.setVisibility(View.VISIBLE);
        name.setText(Html.fromHtml(StrUtils.setHtml999(null, "姓名：", dataEntity.getConsignee())));
        phone.setText(dataEntity.getMobile());
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StrUtils.isNotNull(dataEntity.getMobile())) {
                    CallPhone.callPhone(context, dataEntity.getMobile());
                }
            }
        });
        List<String> kindItemList = new ArrayList<>();
        kindItemList.addAll(JSON.parseArray(dataEntity.getReal_photo(), String.class));
        LinearLayout kindLayout = ViewHolder.get(dvView, R.id.order_info_kind_layout);
        TextView kindHintText = ViewHolder.get(dvView, R.id.kind_hint_name);
        RecyclerView kindRv = ViewHolder.get(dvView, R.id.kind_recyclerView);
        kindHintText.setText("实物照");
        if (dataEntity.getOrder_status().equals("WAIT_CONFIRM")) {//待接單
            kindLayout.setVisibility(View.GONE);
        } else if (kindItemList.size() > 0) {//有实物照
            kindLayout.setVisibility(View.VISIBLE);
            kindList.clear();
            for (String kindS : kindItemList) {
                ImageItem kind = new ImageItem();
                kind.url = kindS;
                kindList.add(kind);
            }
            if (dataEntity.getOrder_status().equals("PROCESSING")) {//待處理
                imgNum = 3;
            } else {
                imgNum = kindList.size();
            }
        } else if (dataEntity.getOrder_status().equals("PROCESSING")) {//待處理
            kindLayout.setVisibility(View.VISIBLE);
            kindHintText.setText("实物照（最多3张，小于2M）");
            isDetele = true;
        } else {
            kindLayout.setVisibility(View.GONE);
        }
        kindAdapter = new ImagePickerAdapter(getActivity(), kindList, imgNum, true);
        kindRv.setAdapter(kindAdapter);
        kindAdapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                isKind = true;
                switch (position) {
                    case Constants.IMAGE_ITEM_ADD:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - kindList.size());
                        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
                        break;
                    default:
                        deteleImg(position, isDetele);
                        break;
                }
            }
        });
        LinearLayout signLayout = ViewHolder.get(dvView, R.id.sign_layout);
        TextView signName = ViewHolder.get(dvView, R.id.order_info_sign_info);
        RecyclerView signRv = ViewHolder.get(dvView, R.id.sign_recyclerView);
        if (StrUtils.isNotNull(signInfoEntity.getSign_user())) {//签收姓名不为null
            signLayout.setVisibility(View.VISIBLE);
            signName.setText(signInfoEntity.getSign_user() + " | " + MUtils.getTo66TimePHP(signInfoEntity.getSign_time(), "yyyy-MM-dd HH:mm"));
        }
        List<String> signItemList = new ArrayList<>();
        if (StrUtils.isNotNull(signInfoEntity.getSign_voucher())) {
            signItemList.addAll(JSON.parseArray(signInfoEntity.getSign_voucher(), String.class));
        }
        if (signItemList.size() > 0) {
            signList.clear();
            for (String s : signItemList) {
                ImageItem image = new ImageItem();
                image.url = s;
                signList.add(image);
            }
        }
        if (signList.size() > 0) {
            signAdqpter = new ImagePickerAdapter(getActivity(), signList, signList.size(), true);
            signRv.setAdapter(signAdqpter);
            signAdqpter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    isKind = false;
                    deteleImg(position, false);
                }
            });
        }
        showBut();
    }

    /**
     * 顯示按鈕
     */
    private void showBut() {
        butInfo = new ShowButText();
        if (!dataEntity.getOrder_status().equals("FINISHED")) {
            butInfo.isShowOnline = "在线沟通";
        }
        if (dataEntity.getOrder_status().equals("WAIT_CONFIRM")) {// 待接单
            if (!"1".equals(dataEntity.getOrder_type())) {//非抢单
                butInfo.isShowOk = "立即接单";
                butInfo.isShowCancle = "拒绝接单";
            }
        }
        if (dataEntity.getOrder_status().equals("PROCESSING")||dataEntity.getOrder_status().equals("HAS_BEEN_SERVED")) {//处理中
            if ("1".equals(dataEntity.getIs_refund())) {//退款
                butInfo.isShowOk = "同意退款";
                butInfo.isShowCancle = "拒绝退款";
            } else if (dataEntity.getOrder_status().equals("PROCESSING")){
                // 判断配送方式 快递派送 1:快递派送 其余送货上门
                if ("1".equals(dataEntity.getDelivery_type())) {
                    // 判断是否有物流信息
                    if (StrUtils.isNull(dataEntity.getExpress()) || StrUtils.isNull(dataEntity.getExpress_sn())) {
                        butInfo.isShowOk = "确认发货";
                    } else {
                        butInfo.isShowOk = "立即签收";
                        butInfo.isShowCancle = "修改物流";
                    }
                } else {
                    butInfo.isShowOk = "立即签收";
                }
            }
        }
        MUtils.showGradBut(butLayout, butInfo, this);
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

    private View getDvView() {
        dvView = LayoutInflater.from(context).inflate(R.layout.item_order_info_dv_layout, null);
        return dvView;
    }

    private View getReasonLayout() {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.reason_layout, null);
        return layout;
    }

    /**
     * 获取抢单详情
     */
    private void getTakingInfo() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderCode", orderSn);
        baseHttp(null, true, Constants.API_ORDER_INFO, params);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == Constants.REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                postPic(images);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == Constants.REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (isKind) {
                    kindList.clear();
                    kindList.addAll(images);
                    kindAdapter.setImages(kindList);
                } else {
                    signList.clear();
                    signList.addAll(images);
                    signAdqpter.setImages(signList);
                }
            }
        }else if (requestCode == 300 && resultCode == 6666) {
            getTakingInfo();
        }else if (requestCode==REQUEST_CODE_SETTING){//申請權限返回
            MLog.e("===============impower()============");
            impower();
        }
    }

    /**
     * 上傳實物照  remove_picture
     *
     * @param imgList
     */
    private void postPic(List<ImageItem> imgList) {
        String path = "";
        if (null == imgList || null == dataEntity) {
            return;
        }
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        for (int i = 0; i < imgList.size(); i++) {
            try {
                path = FileUtil.IMAGE_PATH + imgList.get(i).name;
                params.put("picture" + i, PictureUtil.saveBitmapFile(PictureUtil.getimage(imgList.get(i).path, 0, 0), path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderId", dataEntity.getOrder_sn());
        baseHttp("正在上传实物照...", true, Constants.API_REAL_PIC, params);
    }

    /**
     * 删除实物照
     *
     * @param url
     */
    private void postDetele(String url) {
        requestId = 3;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderId", dataEntity.getOrder_sn());
        params.put("remove_picture", url);
        baseHttp("正在删除...", true, Constants.API_REAL_PIC, params);
    }

    /**
     * 删除或者查看
     *
     * @param position
     * @param isDetele
     */
    private void deteleImg(final int position, boolean isDetele) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(R.layout.pop_select_image);
        View views = window.getDecorView();
        Button pcCibtn = (Button) views.findViewById(R.id.pop_photo_check_img);//查看
        Button pcPopPibtn = (Button) views.findViewById(R.id.pop_photo_image);//相机
        Button pcPopCibtn = (Button) views.findViewById(R.id.pop_camera_image);//选择
        Button pcPopBackbtn = (Button) views.findViewById(R.id.pop_back_callimage);//返回
        pcPopPibtn.setText("查看");
        pcPopCibtn.setText("删除");
        if (isDetele) {
            pcPopCibtn.setVisibility(View.VISIBLE);
        } else {
            pcPopCibtn.setVisibility(View.GONE);
        }
        pcPopPibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开预览
                Intent intentPreview = new Intent(getActivity(), ImagePreviewDelActivity.class);
                if (isKind) {
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) kindAdapter.getImages());
                } else {
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) signAdqpter.getImages());
                }
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                alertDialog.dismiss();
            }
        });
        pcPopCibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                ImageItem imageItem = kindList.get(position);
                if (null != imageItem.url) {
                    postDetele(imageItem.url);
                    kindList.remove(position);
                    kindAdapter.setImages(kindList);
                }
            }
        });
        pcPopBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
