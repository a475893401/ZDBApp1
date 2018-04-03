package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopInfoD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ItemEntity;
import com.zhuandanbao.app.entity.ShopAuthInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import app.zhuandanbao.com.reslib.widget.MyGridView;
import app.zhuandanbao.com.reslib.widget.area.PopArea;
import app.zhuandanbao.com.reslib.widget.area.SelectAreaWheelPopWOnClick;

/**
 * 店铺基本信息
 * Created by BFTECH on 2017/2/22.
 */

public class ShopInfoActivity extends BaseListViewActivity<ShopInfoD> {
    private RvBaseAdapter adapter;
    private List<String> list = new ArrayList<>();
    private Button button;
    private View contentView;
    private ShopInfoItemEntity shopInfo;
    private ShopInfoEntity shopInfoEntity;
    private List<ItemEntity> typeList = new ArrayList<>();

    private EditText shopName;//店鋪名稱
    private TextView shopType;//店铺分类
    private TextView shopArea;//店铺地区
    private TextView shopAddress;//店铺详细地址
    private EditText shopMenName;//店主姓名
    private EditText shopMenMobile;//店主电话
    private EditText shopMenEmail;//店主邮箱
    private EditText shopPhone;//店主其他电话
    private EditText shopQQ;//店主qq
    private EditText shopMsg;//店铺简介
    private TextView shopNameAmend;//申请修改
    private TextView shopMenNameAmend;//已认证
    private TextView shopMenMobileAmend;//手机已绑定
    private TextView shopMenEmailAmend;//邮箱已绑定

    private PopupWindow popWin;
    private PopArea popArea;

    private String province = null;

    private SkipInfoEntity skipInfoEntity = null;//引导才不为null

    @Override
    protected Class<ShopInfoD> getDelegateClass() {
        return ShopInfoD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("店铺基本信息");
        list.add("店铺信息");
        button = viewDelegate.get(R.id.but_blue_hg);
        button.setText("保存");
        button.setOnClickListener(this);
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, list, R.layout.top_title_layout, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                holder.setText(R.id.top_title, (String) item);
            }
        });
        contentView = LayoutInflater.from(context).inflate(R.layout.item_shop_info_content, null);
        adapter.addFooterView(contentView);
        initVerticalRecyclerView(adapter, true, false, 1, 0);
        shopName = ViewHolder.get(contentView, R.id.shop_name);
        shopType = ViewHolder.get(contentView, R.id.shop_type);
        shopArea = ViewHolder.get(contentView, R.id.shop_area);
        shopAddress = ViewHolder.get(contentView, R.id.shop_address);
        shopMenName = ViewHolder.get(contentView, R.id.shop_men_name);
        shopMenMobile = ViewHolder.get(contentView, R.id.shop_men_mobile);
        shopMenEmail = ViewHolder.get(contentView, R.id.shop_men_e_mail);
        shopPhone = ViewHolder.get(contentView, R.id.shop_men_phone);
        shopQQ = ViewHolder.get(contentView, R.id.shop_men_qq);
        shopMsg = ViewHolder.get(contentView, R.id.shop_info);
        shopNameAmend = ViewHolder.get(contentView, R.id.shop_name_amend);
        shopMenNameAmend = ViewHolder.get(contentView, R.id.shop_men_name_hint);
        shopMenMobileAmend = ViewHolder.get(contentView, R.id.shop_men_mobile_hint);
        shopMenEmailAmend = ViewHolder.get(contentView, R.id.shop_men_e_mail_hint);
        shopType.setOnClickListener(this);
        shopArea.setOnClickListener(this);
        shopNameAmend.setOnClickListener(this);
        shopAddress.setOnClickListener(this);
        loadingId = 2;
        getShopInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            shopInfo = MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo());
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            initShopInfo();
        }
        if (code == 2) {
            popArea = PopArea.getInstance(baseEntity.data, this);
            onClickArea();
        }
        if (code == 3) {
            viewDelegate.showSuccessHint("信息保存成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    if (null != skipInfoEntity && skipInfoEntity.index == 1) {
                        setResult(Constants.SHOP_GUIDE_INFO_SUCCESS);
                        finish();
                    } else {
                        loadingId = 2;
                        getShopInfo();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.shop_type) {
            initPopwindows(typeList, 0);
            popWin.showAsDropDown(shopType, 0, 15);
            popWin.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E9E9E9")));
            popWin.setAnimationStyle(R.style.popwin_anim_style);
            popWin.setFocusable(true);
            popWin.setOutsideTouchable(true);
            popWin.update();
        }
        if (view.getId() == R.id.shop_area) {
            if (null == popArea) {
                getAllArea();
            } else {
                onClickArea();
            }
        }
        if (view.getId() == R.id.but_blue_hg) {
            saveShopInfo();
        }
        if (view.getId() == R.id.shop_name_amend) {
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            skipInfoEntity.index = 2;
            skipInfoEntity.item = 4;
            viewDelegate.goToActivity(skipInfoEntity, AddWorkOrderActivity.class, 0);
        }
        if (view.getId() == R.id.shop_address) {
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            if (StrUtils.isNull(shopArea.getText().toString().trim())){
                viewDelegate.showErrorHint("请先选择所在地区",1,null);
                return;
            }else if (StrUtils.isNull(province)){
                province=shopInfo.getProvince();
            }
            skipInfoEntity.pushId = province;
            skipInfoEntity.payCode=shopArea.getText().toString();
            skipInfoEntity.data=shopAddress.getText().toString();
            skipInfoEntity.msg = shopArea.getText().toString()+shopAddress.getText().toString();
            viewDelegate.goToActivity(skipInfoEntity, GDMapActivity.class, 400);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        loadingId = 0;
        getShopInfo();
    }

    /**
     * 初始化店鋪信息
     */
    private void initShopInfo() {
        typeList.clear();
        typeList.addAll(JSON.parseArray(shopInfoEntity.getShopCategory(), ItemEntity.class));
        shopName.setText(shopInfo.getShop_name());
        shopMenName.setText(shopInfo.getShopkeeper());
        shopMenMobile.setText(shopInfo.getMobile());
        shopMenEmail.setText(shopInfo.getEmail());
        shopPhone.setText(shopInfo.getPhone());
        shopQQ.setText(shopInfo.getQq());
        shopMsg.setText(shopInfo.getIntroduce());
        if (shopInfo.getShop_cat_id() > 0) {
            shopType.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            viewDelegate.noClick(shopType, false);
        } else {
            shopType.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.app_go_back), null);
            viewDelegate.noClick(shopType, true);
        }
        for (ItemEntity info : typeList) {
            if (info.id == shopInfo.getShop_cat_id()) {
                shopType.setText(info.name);
                shopType.setTag(info.id);
            }
        }
        if (StrUtils.isNotNull(shopInfo.getAreainfo())) {
            shopArea.setText(shopInfo.getAreainfo());
            shopArea.setTag("1");
        } else {
            shopArea.setTag("-1");
        }
        if (StrUtils.isNotNull(shopInfo.getAddress())) {
            shopAddress.setText(shopInfo.getAddress());
        }
        if ("1".equals(shopInfo.getIs_audit())) {//已认证
            viewDelegate.noClick(shopName, false);
            viewDelegate.noClick(shopArea, false);
            viewDelegate.noClick(shopMenName, false);
            shopArea.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            shopNameAmend.setVisibility(View.VISIBLE);
            shopMenNameAmend.setVisibility(View.VISIBLE);
        } else {
            shopArea.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.app_go_back), null);
            viewDelegate.noClick(shopName, true);
            viewDelegate.noClick(shopArea, true);
            viewDelegate.noClick(shopMenName, true);
            shopNameAmend.setVisibility(View.GONE);
            shopMenNameAmend.setVisibility(View.GONE);
        }
        ShopAuthInfoEntity shopAuthInfoEntity = JSON.parseObject(shopInfo.getShop_auth_info(), ShopAuthInfoEntity.class);
        if (shopAuthInfoEntity.getAuth_mobile().equals("1")) {
            viewDelegate.noClick(shopMenMobile, false);
            shopMenMobileAmend.setVisibility(View.VISIBLE);
        } else {
            viewDelegate.noClick(shopMenMobile, true);
            shopMenMobileAmend.setVisibility(View.GONE);
        }
        if (shopAuthInfoEntity.getAuth_email().equals("1")) {
            viewDelegate.noClick(shopMenEmail, false);
            shopMenEmailAmend.setVisibility(View.VISIBLE);
            shopMenEmail.setTag("1");
        } else {
            viewDelegate.noClick(shopMenEmail, true);
            shopMenEmailAmend.setVisibility(View.GONE);
            shopMenEmail.setTag("-1");
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
     * 獲取地區
     */
    private void getAllArea() {
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("正在获取地区...", true, Constants.API_GET_AREA_ALL, params);
    }

    /**
     * 保存数据
     */
    private void saveShopInfo() {
        if (StrUtils.isNull(shopName.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入你的店铺名称", 1, null);
            return;
        }
        if ("-1".equals(shopType.getTag().toString().trim())) {
            viewDelegate.showErrorHint("请选择店铺分类", 1, null);
            return;
        }
        if (StrUtils.isNull(shopArea.getText().toString().trim())) {
            viewDelegate.showErrorHint("请选择店铺所在地区", 1, null);
            return;
        }
        if (StrUtils.isNull(shopAddress.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入店铺详细地址", 1, null);
            return;
        }
        if (StrUtils.isNull(shopMenName.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入店主姓名", 1, null);
            return;
        }
        if (!StrUtils.isMobileNum(shopMenMobile.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入正确店主手机号", 1, null);
            return;
        }
        if (StrUtils.isNotNull(shopMenEmail.getText().toString().trim()) && !StrUtils.isEmail(shopMenEmail.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入正确的邮箱", 1, null);
            return;
        }
        if (StrUtils.isNotNull(shopPhone.getText().toString().trim()) && !StrUtils.isPhoneNumber(shopPhone.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入正确的电话号", 1, null);
            return;
        }
        if (StrUtils.isNull(shopMsg.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入店铺简介", 1, null);
            return;
        }
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("shop_name", shopName.getText().toString().trim());
        params.put("shop_cat_id", shopType.getTag().toString().trim());
        params.put("shopkeeper", shopMenName.getText().toString().trim());
        params.put("mobile", shopMenMobile.getText().toString().trim());
        // 判断邮箱没有认证  和不为null 上传 可以提交null
        if (shopMenEmail.getTag().toString().equals("-1")) {
            params.put("email", shopMenEmail.getText().toString().trim());
        }
        if (StrUtils.isNotNull(shopArea.getText().toString().trim())) {
            String[] spSpritAddress = shopArea.getText().toString().trim().split("\\|");
            for (int i = 0; i < spSpritAddress.length; i++) {
                if (i == 0) {
                    params.put("province", spSpritAddress[i]);
                } else if (i == 1) {
                    params.put("city", spSpritAddress[i]);
                } else if (i == 2) {
                    params.put("county", spSpritAddress[i]);
                }
            }
        }
        params.put("address", shopAddress.getText().toString().trim());
        if (StrUtils.isNotNull(shopQQ.getText().toString().trim())) {
            params.put("qq", shopQQ.getText().toString().trim());
        }
        if (StrUtils.isNotNull(shopPhone.getText().toString().trim())) {
            params.put("phone", shopPhone.getText().toString());
        }
        if (StrUtils.isNotNull(shopMsg.getText().toString().trim())) {
            params.put("introduce", shopMsg.getText().toString().trim());
        }
        requestId = 3;
        loadingId = 1;
        baseHttp("正在保存...", true, Constants.PAI_ACTION_SHOP_UPDAE, params);
    }

    /**
     * 点击
     */
    private void onClickArea() {
        String[] spAddressArray = null;
        if (!"-1".equals(shopArea.getTag().toString())) {
            spAddressArray = shopArea.getText().toString().split("\\|");
        }
        popArea.showPopw(shopArea, false, spAddressArray, new SelectAreaWheelPopWOnClick() {
            @Override
            public void sureOnClick(int provinceId, int cityId, int regionId, String provinceName, String cityName, String regionName) {
                province = provinceName;
                shopArea.setText(provinceName + "|" + cityName + "|" + regionName);
                shopArea.setTag(provinceId + "|" + cityId + "|" + regionId);
                shopAddress.setText(null);
            }

            @Override
            public void cancleOnClick() {
            }
        });
    }

    /**
     * popupWindow弹出窗体的背景
     *
     * @param arrayListType
     * @param indexType
     */
    public void initPopwindows(final List<ItemEntity> arrayListType, final int indexType) {
        View popView = LayoutInflater.from(ShopInfoActivity.this).inflate(R.layout.pop_diaolog_layout, null);
        popWin = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置popupWindow弹出窗体的背景
        popWin.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popWin.setOutsideTouchable(true);
        MyGridView spGvPopLayout = (MyGridView) popView.findViewById(R.id.pop_diaolog_gridview);
        spGvPopLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                switch (indexType) {
                    case 0:
                        shopType.setText(arrayListType.get(arg2).name);
                        shopType.setTag(arrayListType.get(arg2).id);
                        break;
                    default:
                        break;
                }
                popWin.dismiss();
            }
        });
        spGvPopLayout.setAdapter(new PopAdapter(arrayListType, indexType));
    }

    public class PopAdapter extends BaseAdapter {
        List<ItemEntity> listShopEntiy;
        private int indexType;

        public PopAdapter(List<ItemEntity> onListShpEntity, int indexType) {
            this.listShopEntiy = onListShpEntity;
            this.indexType = indexType;
        }

        @Override
        public int getCount() {
            return listShopEntiy == null ? 0 : listShopEntiy.size();
        }

        @Override
        public Object getItem(int arg0) {
            return listShopEntiy.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            HolderView hv;
            if (arg1 == null) {
                hv = new HolderView();
                arg1 = arg1.inflate(ShopInfoActivity.this, R.layout.item_pop_gradview, null);
                hv.popItemTxInfo = (TextView) arg1.findViewById(R.id.item_pop_gd_textinfo);
                arg1.setTag(hv);
            } else {
                hv = (HolderView) arg1.getTag();
            }
            switch (indexType) {
                case 0:
                    if ((shopType.getTag().toString().equals(listShopEntiy.get(arg0).id + ""))) {
                        hv.popItemTxInfo.setBackgroundColor(getResources().getColor(R.color.back_bg_color));
                        hv.popItemTxInfo.setTextColor(getResources().getColor(R.color.withe));
                    }
                    break;
                default:
                    break;
            }
            hv.popItemTxInfo.setText(listShopEntiy.get(arg0).name);
            return arg1;
        }

        class HolderView {
            TextView popItemTxInfo;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==400&&resultCode==200&&null!=data&&null!=data.getStringExtra("address")){
            shopAddress.setText(data.getStringExtra("address"));
        }
    }
}
