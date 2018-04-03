package com.zhuandanbao.app.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.AddHonestActivity;
import com.zhuandanbao.app.activity.ApproveActivity;
import com.zhuandanbao.app.activity.ApproveSuccessActivity;
import com.zhuandanbao.app.activity.HelperActivity;
import com.zhuandanbao.app.activity.InfoActivity;
import com.zhuandanbao.app.activity.LoginActivity;
import com.zhuandanbao.app.activity.RechargeActivity;
import com.zhuandanbao.app.activity.SafeSettingActivity;
import com.zhuandanbao.app.activity.ShopImageActivity;
import com.zhuandanbao.app.activity.ShopInfoActivity;
import com.zhuandanbao.app.activity.ShopTouchActivity;
import com.zhuandanbao.app.activity.WithdrawActivity;
import com.zhuandanbao.app.activity.WorkOrderActivity;
import com.zhuandanbao.app.activity.ZdbMsgActivity;
import com.zhuandanbao.app.activity.ZuijiaHonestActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.adapter.UILImageLoader;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.callbackinterface.ActivityToFragment;
import com.zhuandanbao.app.callbackinterface.CallbackEntity;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.DataEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.ShopItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.mvp.view.HttpCallback;
import com.zhuandanbao.app.utils.FileUtil;
import com.zhuandanbao.app.utils.JGUtil;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.PictureUtil;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import app.zhuandanbao.com.reslib.widget.CircleImageView;
import app.zhuandanbao.com.reslib.widget.MyGridView;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 店铺
 * Created by BFTECH on 2017/2/13.
 */

public class MainShopFragment extends BaseListViewFragment<ShopD> {

    protected static String FRAGMENT_INDEX = "fragment_index";
    private RvBaseAdapter shopAdapter;
    private List<ShopItemEntity> shopList;
    private View shopTopView;
    private View shopBottomView;
    private Button loginBut;
    private ShopInfoItemEntity shopInfo;
    private CircleImageView shopLogo;
    private TextView shopName;
    private LinearLayout shopGrade;
    private TextView shopBalance;
    private LinearLayout recharge;
    private LinearLayout withdrawal;
    private TextView shopZTname;
    private ImageView shopZTimg;
    private RadioButton shopRZrb;
    private RadioButton shopCXrb;
    private LinearLayout shopYinueLayout;
    private LinearLayout shopCXlayout;
    private LinearLayout approveLayout;
    private MainActivity mainActivity;
    private int num = 0;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        mainActivity.OnCliked(new ActivityToFragment() {
            @Override
            public void sendMsgFragment(CallbackEntity callbackInfo) {
                MLog.e("msg==" + callbackInfo.msg);
                if (null != callbackInfo.msg) {
                    num = Integer.parseInt(callbackInfo.msg);
                }
                if (null != shopAdapter && StrUtils.isNotNull(callbackInfo.msg)) {
//                    ssAdapter.setNum(Integer.parseInt(callbackInfo.msg));
                    shopAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    public static MainShopFragment getInstance() {
        return new MainShopFragment();
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        shopAdapter = new RvBaseAdapter(context, getShopList(), R.layout.item_main_shop_info_list, new RvBaseAdapter.RefreshAdapterImpl() {
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                ShopItemEntity info = (ShopItemEntity) item;
                if (position == 0 || position == 1 || position == 4 || position == 7) {
                    holder.setVisible(R.id.item_shop_view, true);
                } else {
                    holder.setVisible(R.id.item_shop_view, false);
                }
                if (position == 0) {
                    if (num > 0) {
                        holder.setVisible(R.id.item_shop_num, true);
                        holder.setText(R.id.item_shop_num, num + "");
                    } else {
                        holder.setVisible(R.id.item_shop_num, false);
                    }
                } else {
                    holder.setVisible(R.id.item_shop_num, false);
                }
                holder.setImageResource(R.id.item_shop_img, info.img);
                holder.setText(R.id.item_shop_title, info.title);
            }
        });
        shopAdapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                if (position==0){
                    impower();
                }
                if (position == 1) {
                    viewDelegate.goToActivity(null, ShopInfoActivity.class, 0);
                }
                if (position == 2) {
                    viewDelegate.goToActivity(null, ShopTouchActivity.class, 0);
                }
                if (position == 3) {
                    viewDelegate.goToActivity(null, ShopImageActivity.class, 0);
                }
                if (position == 4) {
                    SkipInfoEntity infoEntity = new SkipInfoEntity();
                    infoEntity.msg = shopInfo.getShop_auth_info();
                    infoEntity.mobile = shopInfo.getMobile();
                    viewDelegate.goToActivity(infoEntity, SafeSettingActivity.class, 200);
                }
                if (position == 5) {
                    viewDelegate.goToActivity(null, WorkOrderActivity.class, 0);
                }
                if (position == 6) {
                    viewDelegate.goToActivity(null, HelperActivity.class, 0);
                }
                if (position == 7) {
                    viewDelegate.goToActivity(null, InfoActivity.class, 0);
                }
                if (position==8){
                    viewDelegate.goToActivity(null, ZdbMsgActivity.class, 0);
                }
            }
        });
        shopAdapter.addHeaderView(getShopTopView());
        shopAdapter.addFooterView(getShopBottomView());
        initVerticalRecyclerView(shopAdapter, true, false, 1, 0);
        loginBut = ViewHolder.get(shopBottomView, R.id.main_shop_exit_login);
        shopLogo = ViewHolder.get(shopTopView, R.id.sp_shop_logo);
        shopName = ViewHolder.get(shopTopView, R.id.sp_shop_user_name);
        shopGrade = ViewHolder.get(shopTopView, R.id.shop_grade_img);
        shopBalance = ViewHolder.get(shopTopView, R.id.sp_shop_balance);
        recharge = ViewHolder.get(shopTopView, R.id.frag_id_recharge);
        withdrawal = ViewHolder.get(shopTopView, R.id.frag_id_withdrawals);
        shopZTname = ViewHolder.get(shopTopView, R.id.sp_shop_tx_yingye_state);
        shopZTimg = ViewHolder.get(shopTopView, R.id.sp_shop_img_yingye_state);
        shopRZrb = ViewHolder.get(shopTopView, R.id.sp_shop_item_certfac_rb);
        shopCXrb = ViewHolder.get(shopTopView, R.id.sp_shop_item_rb);
        shopYinueLayout = ViewHolder.get(shopTopView, R.id.sp_shop_item_zt);
        shopCXlayout = ViewHolder.get(shopTopView, R.id.sp_shop_item_cx);
        approveLayout = ViewHolder.get(shopTopView, R.id.sp_shop_item_certfac);

        loginBut.setOnClickListener(this);
        shopLogo.setOnClickListener(this);
        recharge.setOnClickListener(this);
        withdrawal.setOnClickListener(this);
        shopYinueLayout.setOnClickListener(this);
        shopCXlayout.setOnClickListener(this);
        approveLayout.setOnClickListener(this);
        shopName.setOnClickListener(this);
    }

    @Override
    protected void initD() {
        loadingId = 2;
        getShopInfo();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        loadingId = 0;
        getShopInfo();
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
                    try {
                        Intent intent = ImLoginHelper.getInstance().getIMKit().getConversationActivityIntent();
                        startActivity(intent);
                    } catch (Exception e) {
                        loginIm(shopInfo.getSid());
                    }
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
            MUtils.showDialog(getActivity(), "转单宝提示", "取消", "去设置", "你已禁止了存储空间权限，不能查看消息，请手动去设置？", new View.OnClickListener() {
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

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        MLog.e("code=" + code + ";baseEntity" + baseEntity.data);
        //获取商品信息
        if (code == 1) {
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            MainApplication.cache.put(Constants.SHOP_INFO, shopInfoEntity.getShopInfo());
            shopInfo = MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo());
            MainApplication.cache.put(Constants.SHOP_SID, shopInfo.getSid());
            JGUtil.setAlias(context, shopInfo.getSid(), new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    MLog.e("i=" + i + ";s+" + s);
                }
            });
            initShopInfo();
        }
        //更新店铺营业状态
        if (code == 2) {
            loadingId = 0;
            getShopInfo();
        }
        if (code == 3) {
            viewDelegate.showSuccessHint("上传成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    loadingId = 0;
                    getShopInfo();
                }
            });
        }
        if (code==4){
            ImLoginHelper.getInstance().imLogin(activity, PerfHelper.getStringData(ImLoginHelper.IM_USER_ID_NAME) + ImLoginHelper.Im_PASSWORD, new HttpCallback() {
                @Override
                public void onSuccess(String data) {
                    Intent intent = ImLoginHelper.getInstance().getIMKit().getConversationActivityIntent();
                    startActivity(intent);
                }
                @Override
                public void onError(int code, String message) {

                }
            });
        }
    }

    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("", true, Constants.API_GET_SHOP_INFO, params);
    }

    /**
     * 初始化店铺数据
     */
    private void initShopInfo() {
        if (null != superRecyclerView) {
            superRecyclerView.completeRefresh();
        }
        if (StrUtils.isNotNull(shopInfo.getShop_logo())) {//头像
            ImageLoader.getInstance().displayImage(shopInfo.getShop_logo(), shopLogo, MainApplication.optionOnDisk);
        } else {
            shopLogo.setImageResource(R.mipmap.shop_logo);
        }
        if (StrUtils.isNull(shopInfo.getShop_name())){
            shopName.setClickable(true);
            shopName.setText("立即完善店铺信息");
        }else {
            shopName.setClickable(false);
            shopName.setText(shopInfo.getShop_name());
        }
        shopBalance.setText("￥ " + shopInfo.getBalance());
        //认证状态
        if ("0".equals(shopInfo.getIs_audit())) {
            shopRZrb.setChecked(false);
        } else {
            shopRZrb.setChecked(true);
        }
        //诚信
        if ("0".equals(shopInfo.getAssure_credit())) {
            shopCXrb.setChecked(false);
        } else {
            shopCXrb.setChecked(true);
        }
        initGrade();
        showYyeState(shopInfo.getYingye_state());
    }

    /**
     * 等級
     */
    private void initGrade() {
        // 等级
        DataEntity imageGradeMap = MUtils.showCountShopGrade(shopInfo.getCredit());
        ImageView spGradeImage;
        shopGrade.removeAllViews();
        switch (imageGradeMap.gradeColor) {
            case 1: // 红心
                for (int i = 0; i < imageGradeMap.grade; i++) {
                    spGradeImage = new ImageView(activity);
                    spGradeImage.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    spGradeImage.setImageResource(R.mipmap.shop_grade_one);
                    shopGrade.addView(spGradeImage);
                }
                break;
            case 2:
                for (int i = 0; i < imageGradeMap.grade; i++) {
                    spGradeImage = new ImageView(getActivity());
                    spGradeImage.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    spGradeImage.setImageResource(R.mipmap.shop_grade_two);
                    shopGrade.addView(spGradeImage);
                }
                break;
            case 3:
                for (int i = 0; i < imageGradeMap.grade; i++) {
                    spGradeImage = new ImageView(getActivity());
                    spGradeImage.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    spGradeImage.setImageResource(R.mipmap.shop_grade_three);
                    shopGrade.addView(spGradeImage);
                }
                break;
            case 4:
                for (int i = 0; i < imageGradeMap.grade; i++) {
                    spGradeImage = new ImageView(getActivity());
                    spGradeImage.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    spGradeImage.setImageResource(R.mipmap.shop_grade_four);
                    shopGrade.addView(spGradeImage);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 营业状态
     *
     * @param state
     */
    public void showYyeState(String state) {
        if (state.equals("0")) {
            shopZTimg.setImageResource(R.mipmap.shop_yingye_zero);
            shopZTname.setText("正常营业");
        } else if (state.equals("1")) {
            shopZTimg.setImageResource(R.mipmap.shop_yingye_two_or_one);
            shopZTname.setText("隐藏店铺");
        } else if (state.equals("3")) {
            shopZTimg.setImageResource(R.mipmap.shop_yingye_three);
            shopZTname.setText("大量接单");
        } else if (state.equals("2")) {
            shopZTimg.setImageResource(R.mipmap.shop_yingye_two_or_one);
            shopZTname.setText("订单饱和");
        }
        shopZTname.setTag(state);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.main_shop_exit_login://退出登錄
                PerfHelper.setInfo(Constants.APP_OPENID, null);
                MainApplication.cache.clear();
                startActivity(new Intent(context, LoginActivity.class));
                activity.finish();
                break;
            case R.id.sp_shop_item_zt://營業狀態
                initPopwindows(Constants.SHOPBUSINESSSTATE);
                popWin.showAsDropDown(shopYinueLayout, 0, 15);
                popWin.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E9E9E9")));
                popWin.setAnimationStyle(R.style.popwin_anim_style);
                popWin.setFocusable(true);
                popWin.setOutsideTouchable(true);
                popWin.update();
                break;
            case R.id.sp_shop_item_cx:
                if ("0".equals(shopInfo.getAssure_credit())) {
                    viewDelegate.goToActivity(null, AddHonestActivity.class, 0);
                }
                if ("1".equals(shopInfo.getAssure_credit())) {
                    viewDelegate.goToActivity(null, ZuijiaHonestActivity.class, 0);
                }
                break;
            case R.id.sp_shop_logo:
                initImagePicker();
                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
                break;
            case R.id.sp_shop_item_certfac:
                if ("1".equals(shopInfo.getIs_audit())) {//已认证
                    viewDelegate.goToActivity(null, ApproveSuccessActivity.class, 0);
                } else {//未认证ApproveActivity
                    viewDelegate.goToActivity(null, ApproveActivity.class, 0);
                }
                break;
            case R.id.frag_id_recharge:
                viewDelegate.goToActivity(null, RechargeActivity.class, 0);
                break;
            case R.id.frag_id_withdrawals:
                viewDelegate.goToActivity(null, WithdrawActivity.class, 0);
                break;
            case R.id.sp_shop_user_name:
                viewDelegate.goToActivity(null,ShopInfoActivity.class,0);
                break;
        }
    }

    /**
     * 创建shop列表信息
     *
     * @return
     */
    private List<ShopItemEntity> getShopList() {
        if (null != shopList) {
            shopList.clear();
        } else {
            shopList = new ArrayList<>();
        }
        shopList.add(new ShopItemEntity(R.mipmap.img_shop_msg, "我的消息", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_shop_info, "基本信息", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_shop_main_user, "店铺联系人", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_shop_photo, "店铺形象照", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_shop_safesetting, "安全设置", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_shop_question, "我的工单", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_shop_help, "帮助中心", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_shop_infomation, "行业资讯", 0, true));
        shopList.add(new ShopItemEntity(R.mipmap.img_shop_zdb_info, "转单宝动态", 0, true));

        return shopList;
    }

    private View getShopTopView() {
        shopTopView = LayoutInflater.from(context).inflate(R.layout.item_main_shop_top, null);
        return shopTopView;
    }

    private View getShopBottomView() {
        shopBottomView = LayoutInflater.from(context).inflate(R.layout.layout_main_shop_but, null);
        return shopBottomView;
    }

    private PopupWindow popWin;
    public static int yingYeType;

    public void initPopwindows(String[] arrayListType) {
        View popView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_diaolog_layout, null);
        popWin = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置popupWindow弹出窗体的背景
        popWin.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popWin.setOutsideTouchable(true);
        MyGridView spGvPopLayout = (MyGridView) popView.findViewById(R.id.pop_diaolog_gridview);
        spGvPopLayout.setBackgroundColor(getResources().getColor(R.color.fg_bg_color));
        spGvPopLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                popWin.dismiss();
                if (arg2 == 1) {
                    yingYeType = 3;
                } else if (arg2 == 3) {
                    yingYeType = 1;
                } else {
                    yingYeType = arg2;
                }
                shopZTname.setTag(yingYeType);
                upDataYinye();
            }
        });
        spGvPopLayout.setAdapter(new PopAdapter(arrayListType));
    }

    /**
     * 更新营业状态
     */
    private void upDataYinye() {
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("yingye_state", shopZTname.getTag().toString());
        baseHttp("正在更新...", true, Constants.API_UPDATA_SHOP_INFO, params);
    }

    public class PopAdapter extends BaseAdapter {
        String[] listShopContEntiy;

        public PopAdapter(String[] onListShpEntity) {
            this.listShopContEntiy = onListShpEntity;
        }

        @Override
        public int getCount() {
            return listShopContEntiy == null ? 0 : listShopContEntiy.length;
        }

        @Override
        public Object getItem(int arg0) {
            return listShopContEntiy[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                arg1 = arg1.inflate(getActivity(), R.layout.item_pop_gradview, null);
            }
            TextView popItemTxInfo = ViewHolder.get(arg1, R.id.item_pop_gd_textinfo);
            if (listShopContEntiy[arg0].equals(shopZTname.getText().toString())) {
                popItemTxInfo.setBackgroundColor(getResources().getColor(R.color.back_bg_color));
                popItemTxInfo.setTextColor(getResources().getColor(R.color.withe));
            }
            popItemTxInfo.setText(listShopContEntiy[arg0]);
            popItemTxInfo.setTag(arg0);
            return arg1;
        }
    }

    /**
     * 选择头像
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new UILImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setMultiMode(false);                     //单选
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(600);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(600);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(800);                         //保存文件的高度。单位像素
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        MainApplication.sInstance.initImagePicker();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            MLog.e("path===" + images.get(0).path);
            if (StrUtils.isNotNull(images.get(0).path)) {
                upDataLogo(images.get(0));
            }
        }
        if (requestCode == 200 && resultCode == 300) {
            loadingId = 2;
            getShopInfo();
        }
    }

    /**
     * 上传头像
     */
    private void upDataLogo(ImageItem imageItem) {
        File file = null;
        String logoPath = FileUtil.IMAGE_PATH + "shopLogo.jpg";
        try {
            file = PictureUtil.saveBitmapFile(PictureUtil.getimage(imageItem.path, 0, 0), logoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null == file) {
            return;
        }
        requestId = 3;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("albname", "shop_avatars");
        params.put("picture", file);
        baseHttp("正在上传...", true, Constants.API_UPDATE_SHOP_LOGO, params);
    }

    private void loginIm(String sid) {
        MLog.e("sid==" + sid);
        PerfHelper.setInfo(ImLoginHelper.IM_USER_ID_NAME, sid);
        requestId=4;
        loadingId=0;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null,true,Constants.REG_IM_USER,params);
    }
}
