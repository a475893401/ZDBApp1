package com.zhuandanbao.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopListviewD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.BillOrderShopEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 一对一选择接单店铺
 * Created by BFTECH on 2017/3/9.
 */

public class ShopListActivity extends BaseListViewActivity<ShopListviewD> {
    public static String JIE_DAN_SID = "jie_dan_sid";
    private RvBaseAdapter adapter;
    private List<BillOrderShopEntity> list = new ArrayList<>();
    private String searchKey = null;
    private String areaInfo = null;
    private SkipInfoEntity skipInfoEntity;
    private int index = -1;
    private BillOrderShopEntity jiedanSid = null;

    @Override
    protected Class<ShopListviewD> getDelegateClass() {
        return ShopListviewD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            areaInfo = skipInfoEntity.msg;
            jiedanSid = (BillOrderShopEntity) skipInfoEntity.data;
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("选择接单店辅");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, list, R.layout.item_shop_list_layout, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                BillOrderShopEntity info = (BillOrderShopEntity) item;
                LinearLayout mainLayout = holder.getView(R.id.main_layout);
                RelativeLayout item_check = holder.getView(R.id.item_check);
                TextView name = holder.getView(R.id.shop_name);
                TextView mobile = holder.getView(R.id.shop_mobile);
                TextView address = holder.getView(R.id.shop_address);
                LinearLayout rz_layout = holder.getView(R.id.shop_rz_layout);
                TextView cx = holder.getView(R.id.shop_cx);
                ImageView shopImg=holder.getView(R.id.shop_img);
                if (StrUtils.isNotNull(info.getShop_logo())) {


                    ImageLoader.getInstance().displayImage(info.getShop_logo(), shopImg, MyImage.deployMemory());
                }else {
                    shopImg.setImageResource(R.mipmap.app_defant);
                }
                name.setText(info.getShop_name());
                mobile.setText(Html.fromHtml(StrUtils.setHtmlRed(info.getMobile() + "    (90天内接", info.getJdjdan(), "单)")));
                address.setText(info.getAreainfo());
                if (info.getIs_audit().equals("0")) {
                    rz_layout.setVisibility(View.GONE);
                } else if (info.getIs_audit().equals("1")) {
                    rz_layout.setVisibility(View.VISIBLE);
                }
                if (info.getAssure_credit().equals("1")) {
                    cx.setVisibility(View.VISIBLE);
                } else if (info.getAssure_credit().equals("0")) {
                    cx.setVisibility(View.GONE);
                }
                if (StrUtils.isNotNull(skipInfoEntity.pushId)) {
                    if (skipInfoEntity.pushId.equals(info.getSid())) {
                        jiedanSid = info;
                        index = position;
                    }
                }
                if (index == position) {
                    mainLayout.setBackgroundResource(R.drawable.bill_order_shop_list_check_selector);
                    item_check.setVisibility(View.VISIBLE);
                } else {
                    mainLayout.setBackgroundResource(R.drawable.bill_order_shop_list_uncheck_selector);
                    item_check.setVisibility(View.INVISIBLE);
                }
            }
        });
        initVerticalRecyclerView(adapter, true, false, 1, 0);
        viewDelegate.setOnClickListener(this, R.id.shop_ok_but, R.id.search_ok_btn);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                skipInfoEntity.pushId = null;
                index = position;
                adapter.notifyDataSetChanged();
                jiedanSid = list.get(position);
            }
        });
        loadingId = 2;
        getShopList();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.shop_ok_but) {
            if (null == jiedanSid) {
                viewDelegate.showErrorHint("请选择接单店铺", 1, null);
                return;
            }
            MLog.e("jiedanSid=" + jiedanSid + "SHOP_SID=" + MainApplication.cache.getAsString(Constants.SHOP_SID));
            if (StrUtils.isNotNull(MainApplication.cache.getAsString(Constants.SHOP_SID))) {
                if (jiedanSid.getSid().equals(MainApplication.cache.getAsString(Constants.SHOP_SID))) {
                    viewDelegate.showErrorHint("不能选择自已的店铺", 1, null);
                    jiedanSid = null;
                    return;
                }
            }
            Intent intent = new Intent();
            intent.putExtra(JIE_DAN_SID, jiedanSid);
            setResult(300, intent);
            finish();
        }
        if (view.getId() == R.id.search_ok_btn) {
            EditText input = viewDelegate.get(R.id.input_shop);
            if (StrUtils.isNull(input.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入搜索店铺的信息", 1, null);
                return;
            }
            searchKey = input.getText().toString().trim();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
            getShopList();
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            list.clear();
            list.addAll(MJsonUtils.jsonToBillOrderShopEntity(baseEntity.data));
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getShopList();
    }

    /**
     * 查看接单店铺
     */
    private void getShopList() {
        requestId = 1;
        String[] area = new String[3];
        area[0] = null;
        area[1] = null;
        area[2] = null;
        MLog.e("areaInfo==" + areaInfo);
        if (StrUtils.isNotNull(areaInfo)) {
            area = areaInfo.toString().trim().split("\\|");
        }
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("num", page_size);
        params.put("key", searchKey);
        if (area.length >= 1) {
            if (null != area[0]) {
                params.put("province", area[0]);
            }
        }
        if (area.length >= 2) {
            if (null != area[1]) {
                params.put("city", area[1]);
            }
        }
        baseHttp(null, true, Constants.API_SEARCH_SHOP_LIST, params);
    }
}
