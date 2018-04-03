package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.NewOrderD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.BillOrderShopEntity;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.ShopDetailsEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.TypeEntity;
import com.zhuandanbao.app.entity.XiadanEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.zhuandanbao.com.reslib.widget.MyGridView;
import app.zhuandanbao.com.reslib.widget.area.PopArea;
import app.zhuandanbao.com.reslib.widget.area.SelectAreaWheelPopWOnClick;
import app.zhuandanbao.com.reslib.widget.time.TimePickerView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 发布新单
 * Created by BFTECH on 2017/3/8.
 */

public class BillNewOrderActivity extends BaseListViewActivity<NewOrderD> {
    @Override
    protected Class<NewOrderD> getDelegateClass() {
        return NewOrderD.class;
    }

    public static String GOODS_ITEM_INFO = "item_goods_info";
    public static int BILL_NEW_REQUEST_CODE = 400;
    public static String ADDRESS_INFO = "address_info";
    private RvBaseAdapter adapter;
    private List<OrderItemEntity> list;
    private View topView;
    private View bottomView;

    private PopArea popNW;
    private TimePickerView deliveryTime;
    private TimePickerView grabTime;
    private int index = -1;

    private OrderDataEntity orderInfoMoble = null;//复制发单
    private ShopDetailsEntity info = null;//指定发单
    private PopupWindow popWin;

    private int billType = 1;//1 正常发单  2  复制发单  3指定发单
    private boolean isGrabMobe = true;//  true 抢单
    private boolean isZidingyiTime = false;//true 自定义
    private boolean isKuaiDi = false;//true 送货上门

    private BillOrderShopEntity billOrderShopEntity;

    private SkipInfoEntity skipInfoEntity;//index =1:指定  2：复制 3：正常发单


    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        getShopInfo();
        viewDelegate.setTitle("发布新单");
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        if (skipInfoEntity.index == 1) {
            info = (ShopDetailsEntity) skipInfoEntity.data;
            TextView textView = viewDelegate.get(R.id.bill_appoint_hint);
            textView.setVisibility(View.VISIBLE);
            textView.setText(Html.fromHtml(StrUtils.setHtmlRed("提示：你将指定给“ ", info.getShop_name(), " ”店铺发单")));
            billType = 3;
        }
        if (skipInfoEntity.index == 2) {
            orderInfoMoble = (OrderDataEntity) skipInfoEntity.data;
            billType = 2;
        }
        list = new ArrayList<>();
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(this, list, R.layout.item_new_order_shop_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, final int position) {
                final OrderItemEntity info = (OrderItemEntity) item;
                holder.setText(R.id.item_goods_name, info.getItem_name());
                holder.setText(R.id.item_goods_des, info.getItem_remarks());
                holder.setText(R.id.item_goods_num, "x " + info.getItem_total());
                BGABanner bgaBanner = holder.getView(R.id.item_img);
                List<String> imgList = new ArrayList<>();
                if (StrUtils.isNotNull(info.getItem_img())) {
                    bgaBanner.setAdapter(new BGABanner.Adapter() {
                        @Override
                        public void fillBannerItem(BGABanner banner, View view, Object model, final int position) {
                            String picpath = (String) model;
                            ImageLoader.getInstance().displayImage(picpath, (ImageView) view, MyImage.deployMemory());
                        }
                    });
                    imgList = JSON.parseArray(info.getItem_img(), String.class);
                    if (null != imgList && imgList.size() == 0) {
                        imgList.add("");
                    }
                } else {
                    imgList.add("");
                }
                bgaBanner.setData(imgList, null);
                holder.setOnClickListener(R.id.bill_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        topView = LayoutInflater.from(this).inflate(R.layout.item_new_order_top_layout, null);
        bottomView = LayoutInflater.from(this).inflate(R.layout.item_new_order_bottom_layout, null);
        adapter.addHeaderView(topView);
        adapter.addFooterView(bottomView);
        initVerticalRecyclerView(adapter, false, false, 1, 0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                index = position;
                OrderItemEntity info = (OrderItemEntity) item;
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.data = info;
                viewDelegate.goToActivity(skipInfoEntity, AmendGoodsActivity.class, BILL_NEW_REQUEST_CODE);
            }
        });
        bottomView();
        topView();
        showGrabDate();
        showDeliveryDate();
        copyInfo();
        viewDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                faDan();
            }
        }, R.id.bill_ok_but);
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


    @Override
    public void onClick(View view) {

    }

    @Override
    public void initData(int code, final BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 2) {//创建订单
            viewDelegate.showSuccessHint("您的订单已创建成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    XiadanEntity xiadanEntity = JSON.parseObject(baseEntity.data, XiadanEntity.class);
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.pushId = xiadanEntity.order_sn;
                    skipInfoEntity.index = 2;//发布新单去支付
                    viewDelegate.goToActivity(skipInfoEntity, OrderPayActivity.class, 0);
                    finish();
                }
            });
        }
        if (code == 3) {//獲取地址
            MainApplication.cache.put(Constants.ADDRESS_JSON_DATA, baseEntity.data);
            showAddress(baseEntity.data);
        }
        if (code == 4) {
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            viewDelegate.showDialog(MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo()),true);
        }
    }

    /**
     * 发单
     */
    private void faDan() {
        HttpParams params = null;
        if (null == params) {
            params = new HttpParams();
        } else {
            params.clear();
        }
        final TextView dvDate = ViewHolder.get(topView, R.id.dv_date);//配送日期
        final TextView dvTime = ViewHolder.get(topView, R.id.dv_time_no_input);//选择配送时间
        final EditText dvInputTime = ViewHolder.get(topView, R.id.dv_time);//输入时间
        final TextView dvCity = ViewHolder.get(topView, R.id.dv_show_city);//配送城市
        final TextView dvAddress = ViewHolder.get(topView, R.id.dv_address);//详细地址
        final EditText name = ViewHolder.get(topView, R.id.name);//收貨人
        final EditText mobile = ViewHolder.get(topView, R.id.mobile);//电话
        EditText crad = ViewHolder.get(bottomView, R.id.card_msg);
        EditText msg = ViewHolder.get(bottomView, R.id.order_msg);
        final EditText money = ViewHolder.get(bottomView, R.id.order_money);
        final TextView grabTime = ViewHolder.get(bottomView, R.id.grab_time);
        final TextView jiedanShop = ViewHolder.get(bottomView, R.id.bill_shop_name);
        MLog.e("isKuaiDi=" + isKuaiDi);
        if (isKuaiDi) {//快递
            params.put("delivery_type", 1);
        } else {
            params.put("delivery_type", 2);
            if (StrUtils.isNull(dvDate.getText().toString().trim())) {
                viewDelegate.showErrorHint("请选择配送日期", 1, new KProgressDismissClickLister() {
                    @Override
                    public void onDismiss() {
                        viewDelegate.setViewFocusable(dvDate);
                    }
                });
                return;
            }
            Date now = new Date();
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(now);
            int nowY = calendar1.get(Calendar.YEAR);
            int nowM = calendar1.get(Calendar.MONTH) + 1;
            int nowD = calendar1.get(Calendar.DAY_OF_MONTH);
            Date date = new Date();
            date.setTime(MUtils.getTimeTo(dvDate.getText().toString().trim(), "yyyy-MM-dd"));
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date);
            int choiceY = calendar2.get(Calendar.YEAR);
            int choiceM = calendar2.get(Calendar.MONTH) + 1;
            int choiceD = calendar2.get(Calendar.DAY_OF_MONTH);
            long time = date.getTime();
            if (nowY == choiceY && nowM == choiceM && nowD == choiceD) {
            } else if (time < System.currentTimeMillis()) {
                viewDelegate.showErrorHint("配送日期小于当前日期", 1, new KProgressDismissClickLister() {
                    @Override
                    public void onDismiss() {
                        viewDelegate.setViewFocusable(dvDate);
                    }
                });
                return;
            }
            params.put("delivery_date", dvDate.getText().toString().trim());
            if (isZidingyiTime) {//自定义
                if (StrUtils.isNull(dvInputTime.getText().toString().trim())) {
                    viewDelegate.showErrorHint("请输入配送时间", 1, new KProgressDismissClickLister() {
                        @Override
                        public void onDismiss() {
                            viewDelegate.setViewFocusable(dvInputTime);
                        }
                    });
                    return;
                }
                params.put("delivery_time", dvInputTime.getText().toString().trim());
            } else {
                if (StrUtils.isNull(dvTime.getText().toString().trim())) {
                    viewDelegate.showErrorHint("请选择配送时间", 1, new KProgressDismissClickLister() {
                        @Override
                        public void onDismiss() {
                            viewDelegate.setViewFocusable(dvTime);
                        }
                    });
                    return;
                }
                params.put("delivery_time", dvTime.getText().toString().trim());
            }
        }
        String addressInfo = dvCity.getText().toString().trim();
        if (StrUtils.isNull(addressInfo)) {
            viewDelegate.showErrorHint("请选择配送城市", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    viewDelegate.setViewFocusable(dvCity);
                }
            });
            return;
        }
        String[] area = new String[3];
        area[0] = null;
        area[1] = null;
        area[2] = null;
        MLog.e("areaInfo==" + addressInfo);
        if (StrUtils.isNotNull(addressInfo)) {
            area = addressInfo.toString().trim().split("\\|");
        }
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
        if (area.length >= 3) {
            if (null != area[1]) {
                params.put("county", area[2]);
            }
        }
        if (StrUtils.isNull(dvAddress.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入详细配送地址", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    viewDelegate.setViewFocusable(dvAddress);
                }
            });
            return;
        }
        params.put("address", dvAddress.getText().toString().trim());
        if (StrUtils.isNull(name.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入收货人姓名", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    viewDelegate.setViewFocusable(name);
                }
            });
            return;
        }

        if (StrUtils.isNull(name.getText().toString().trim())) {
            viewDelegate.showErrorHint("收货人姓名长度限制7个字符", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    viewDelegate.setViewFocusable(name);
                }
            });
            return;
        }
        params.put("consignee", name.getText().toString().trim());
        if (!StrUtils.isPhoneNumber(mobile.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入正确的手机号", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    viewDelegate.setViewFocusable(mobile);
                }
            });
            return;
        }
        params.put("mobile", mobile.getText().toString().trim());
        if (null != list && list.size() > 0) {
            params.put("order_wares", MUtils.getOrderItemString(list));
            MLog.e("josnInfo==" + MUtils.getOrderItemString(list));
        } else {
            viewDelegate.showErrorHint("请添加商品", 1, null);
            return;
        }
        if (StrUtils.isNotNull(crad.getText().toString().trim())) {
            params.put("card_con", crad.getText().toString().trim());
        }
        if (StrUtils.isNotNull(msg.getText().toString().trim())) {
            params.put("order_remarks", msg.getText().toString().trim());
        }
        if (StrUtils.isNull(money.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入发单金额", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    viewDelegate.setViewFocusable(money);
                }
            });
            return;
        }
        params.put("order_amount", money.getText().toString().trim());
        if (billType == 3) {
            params.put("order_type", 0);
            params.put("ReceiveStoreId", info.getSid());
        } else {
            if (isGrabMobe) {//抢单
                params.put("order_type", 1);
                if (StrUtils.isNull(grabTime.getText().toString().trim())) {
                    viewDelegate.showErrorHint("请选择抢单结束时间", 1, new KProgressDismissClickLister() {
                        @Override
                        public void onDismiss() {
                            viewDelegate.setViewFocusable(grabTime);
                        }
                    });
                    return;
                }
                params.put("deadline", grabTime.getText().toString().trim());
            } else {//一对一
                params.put("order_type", 0);
                if (StrUtils.isNull(jiedanShop.getText().toString().trim())) {
                    viewDelegate.showErrorHint("请选择接单店铺", 1, new KProgressDismissClickLister() {
                        @Override
                        public void onDismiss() {
                            viewDelegate.setViewFocusable(jiedanShop);
                        }
                    });
                    return;
                }
                params.put("ReceiveStoreId", jiedanShop.getTag().toString().trim());
            }
        }
        requestId = 2;
        loadingId = 1;
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        MLog.e("params.toString()==" + params.toString());
        baseHttp("正在创建订单...", true, Constants.API_BILL_ORDER_REQUEST, params);
        //{"order_amount":"0.01","order_sn":"2017011220330371"}
    }

    private void bottomView() {
        if (billType == 3) {
            LinearLayout layout = ViewHolder.get(bottomView, R.id.layout);
            layout.setVisibility(View.GONE);
        }
        CheckBox checkBox = ViewHolder.get(bottomView, R.id.bill_order_mode_cb);
        final LinearLayout jiedan = ViewHolder.get(bottomView, R.id.jiedan_layout);
        final LinearLayout grabLayout = ViewHolder.get(bottomView, R.id.grab_date_layout);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    grabLayout.setVisibility(View.VISIBLE);
                    jiedan.setVisibility(View.GONE);
                    isGrabMobe = true;
                } else {
                    grabLayout.setVisibility(View.GONE);
                    jiedan.setVisibility(View.VISIBLE);
                    isGrabMobe = false;
                }
            }
        });
        jiedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dvAd = ViewHolder.get(topView, R.id.dv_show_city);
                if (StrUtils.isNull(dvAd.getText().toString())) {
                    viewDelegate.showErrorHint("请先选择配送地址", 1, null);
                    return;
                }
                MLog.e("dvAd==" + dvAd.getText().toString().trim());
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.msg = dvAd.getText().toString().trim();
                if (null != billOrderShopEntity) {
                    skipInfoEntity.pushId = billOrderShopEntity.getSid();
                }
                viewDelegate.goToActivity(skipInfoEntity, ShopListActivity.class, BILL_NEW_REQUEST_CODE);
            }
        });
        grabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grabTime.show();
            }
        });
    }

    private void topView() {
        final LinearLayout dataAndTime = ViewHolder.get(topView, R.id.dv_data_and_time_layout);
        Button button = ViewHolder.get(topView, R.id.add_goods);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = -1;
                viewDelegate.goToActivity(null, AmendGoodsActivity.class, BILL_NEW_REQUEST_CODE);
            }
        });
        RadioGroup rg = ViewHolder.get(topView, R.id.dv_style_rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.dv_style_1://送货
                        isKuaiDi = false;
                        dataAndTime.setVisibility(View.VISIBLE);
                        break;
                    case R.id.dv_style_2://快递
                        isKuaiDi = true;
                        dataAndTime.setVisibility(View.GONE);
                        break;
                }
            }
        });
        final LinearLayout noInputLayout = ViewHolder.get(topView, R.id.dv_time_no_input_layout);
        final LinearLayout inputLayout = ViewHolder.get(topView, R.id.dv_time_layout);
        noInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dvDate = ViewHolder.get(topView, R.id.dv_date);
                if (StrUtils.isNull(dvDate.getText().toString().trim())) {
                    viewDelegate.showErrorHint("请先选择配送日期", 1, null);
                    return;
                }
                int dvTime = MUtils.getDvTimeList(dvDate.getText().toString().trim());
                MLog.e("dvTime=" + dvTime);
                if (dvTime == 1) {
                    initPopwindows(Constants.DvNewTime, 1);
                } else if (dvTime == 2) {
                    initPopwindows(Constants.DvNewTime1, 1);
                } else if (dvTime == 3) {
                    initPopwindows(Constants.DvNewTime2, 1);
                }
                popWin.showAsDropDown(view, 0, 15);
                popWin.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E9E9E9")));
                popWin.setAnimationStyle(R.style.popwin_anim_style);
                popWin.setFocusable(true);
                popWin.setOutsideTouchable(true);
                popWin.update();
            }
        });
        final CheckBox dvCb = ViewHolder.get(topView, R.id.dv_mode_cb);
        dvCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isZidingyiTime = true;
                    noInputLayout.setVisibility(View.GONE);
                    inputLayout.setVisibility(View.VISIBLE);
                } else {
                    isZidingyiTime = false;
                    noInputLayout.setVisibility(View.VISIBLE);
                    inputLayout.setVisibility(View.GONE);
                }
            }
        });
        LinearLayout dvCity = ViewHolder.get(topView, R.id.dv_city_layout);
        dvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });
        LinearLayout dvDateLayout = ViewHolder.get(topView, R.id.dv_date_layout);
        dvDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryTime.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BILL_NEW_REQUEST_CODE) {
            if (resultCode == 200 && null != data.getSerializableExtra(GOODS_ITEM_INFO)) {
                OrderItemEntity info = (OrderItemEntity) data.getSerializableExtra(GOODS_ITEM_INFO);
                MLog.e("info.name=" + info.getItem_name() + "info.des==" + info.getItem_remarks() + "info.item_img.size()=" + info.getItem_img());
                if (index != -1) {
                    list.remove(index);
                    list.add(index, info);
                } else {
                    list.add(info);
                }
                adapter.notifyDataSetChanged();
            }
            if (resultCode == 300 && null != data.getSerializableExtra(ShopListActivity.JIE_DAN_SID)) {
                TextView jiedanName = ViewHolder.get(bottomView, R.id.bill_shop_name);
                billOrderShopEntity = (BillOrderShopEntity) data.getSerializableExtra(ShopListActivity.JIE_DAN_SID);
                jiedanName.setText(billOrderShopEntity.getShop_name());
                jiedanName.setTag(billOrderShopEntity.getSid());
            }
        }
        if (requestCode==300&&(resultCode==Constants.SHOP_GUIDE_INFO_SUCCESS
                ||resultCode==Constants.SHOP_GUIDE_CONTACT_SUCCESS
                ||resultCode==Constants.SHOP_GUIDE_PASS_SUCCESS)){
            getShopInfo();
        }
    }

    /**
     * 获取地址
     */
    private void getAddress() {
        if (StrUtils.isNotNull(MainApplication.cache.getAsString(Constants.ADDRESS_JSON_DATA))) {
            showAddress(MainApplication.cache.getAsString(Constants.ADDRESS_JSON_DATA));
        } else {
            requestId = 3;
            loadingId = 1;
            HttpParams params = new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            baseHttp(null, true, Constants.API_GET_AREA_ALL, params);
        }
    }

    /**
     * show地址
     */
    private void showAddress(String josnStr) {
        MLog.e("josnStr==" + josnStr);
        final TextView showDvCity = ViewHolder.get(topView, R.id.dv_show_city);
        if (null == popNW) {
            popNW = PopArea.getInstance(josnStr, this);
        }
        String[] spAddressArray = null;
        if (StrUtils.isNotNull(showDvCity.getText().toString().trim())) {
            spAddressArray = showDvCity.getText().toString().trim().split("\\|");
        }
        popNW.showPopw(showDvCity, false, spAddressArray, new SelectAreaWheelPopWOnClick() {
            @Override
            public void sureOnClick(int provinceId, int cityId, int regionId, String provinceName, String cityName, String regionName) {
                showDvCity.setText(provinceName + "|" + cityName + "|" + regionName);
            }

            @Override
            public void cancleOnClick() {

            }
        });
    }

    /**
     * 抢单
     */
    private void showGrabDate() {
        final TextView grabDate = ViewHolder.get(bottomView, R.id.grab_time);
        //时间选择器
        if (null == grabTime) {
            grabTime = new TimePickerView(this, TimePickerView.Type.ALL);
        }
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        grabTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        grabTime.setTime(new Date());
        grabTime.setCyclic(false);
        grabTime.setCancelable(true);
        //时间选择后回调
        grabTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                long nowTime = System.currentTimeMillis();
                long grabT = date.getTime();
                if (grabT <= nowTime) {
                    //"请选择一个合理的时间!"
                    viewDelegate.showErrorHint("请选择一个合理的时间", 1, null);
                    return;
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                grabDate.setText(format.format(date));
            }
        });
    }

    /**
     * 初始化配送时间
     */
    private void showDeliveryDate() {
        final TextView dvDate = ViewHolder.get(topView, R.id.dv_date);
        //时间选择器
        if (null == deliveryTime) {
            deliveryTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        }
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        deliveryTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        deliveryTime.setTime(new Date());
        deliveryTime.setCyclic(false);
        deliveryTime.setCancelable(true);
        //时间选择后回调
        deliveryTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(now);
                int nowY = calendar1.get(Calendar.YEAR);
                int nowM = calendar1.get(Calendar.MONTH) + 1;
                int nowD = calendar1.get(Calendar.DAY_OF_MONTH);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date);
                int choiceY = calendar2.get(Calendar.YEAR);
                int choiceM = calendar2.get(Calendar.MONTH) + 1;
                int choiceD = calendar2.get(Calendar.DAY_OF_MONTH);
                MLog.e("choiceY=" + choiceY + "=nowY=" + nowY + "=nowM=" + nowM + "=choiceM=" + choiceM + "=nowD=" + nowD + "=choiceD=" + choiceD);
                if (choiceY < nowY) {
                    viewDelegate.toast("请选择一个合理配送日期!");
                    return;
                } else if (choiceY == nowY) {
                    if (choiceM < nowM) {
                        viewDelegate.toast("请选择一个合理配送日期!");
                        return;
                    } else if (choiceM == nowM) {
                        if (choiceD < nowD) {
                            viewDelegate.toast("请选择一个合理配送日期!");
                            return;
                        }
                    }
                }
                dvDate.setText(format.format(date));
            }
        });
    }

    /**
     * 复制信息
     */
    private void copyInfo() {
        if (null == orderInfoMoble) {
            MLog.e("null==================");
            return;
        }
        if (orderInfoMoble.getDelivery_type().equals("1")) {//快递
            RadioButton dv2 = ViewHolder.get(topView, R.id.dv_style_2);
            dv2.setChecked(true);
            isKuaiDi = true;
        } else {//送貨
            RadioButton dv1 = ViewHolder.get(topView, R.id.dv_style_1);
            dv1.setChecked(true);
            isKuaiDi = false;
        }
        TextView dvDate = ViewHolder.get(topView, R.id.dv_date);
        if (StrUtils.isNotNull(orderInfoMoble.getDelivery_date()) && !"0000-00-00".equals(orderInfoMoble.getDelivery_date())) {
            dvDate.setText(orderInfoMoble.getDelivery_date());
        }
        TextView dvTime = ViewHolder.get(topView, R.id.dv_time_no_input);
        dvTime.setText(orderInfoMoble.getDelivery_time());
        dvTime.setTag("-1");
        TextView dvCity = ViewHolder.get(topView, R.id.dv_show_city);
        dvCity.setText(orderInfoMoble.getAreainfo().trim().replace(",", "|"));
        EditText dvAddress = ViewHolder.get(topView, R.id.dv_address);
        dvAddress.setText(orderInfoMoble.getAddress());
        EditText name = ViewHolder.get(topView, R.id.name);
        name.setText(orderInfoMoble.getConsignee());
        EditText mobile = ViewHolder.get(topView, R.id.mobile);
        mobile.setText(orderInfoMoble.getMobile());
        /*商品信息*/
        if (StrUtils.isNotNull(orderInfoMoble.getOrder_items())) {
            MLog.e("===========" + orderInfoMoble.getOrder_items());
            list.addAll(JSON.parseArray(orderInfoMoble.getOrder_items(), OrderItemEntity.class));
            adapter.notifyDataSetChanged();
        }
        EditText cardMsg = ViewHolder.get(bottomView, R.id.card_msg);
        cardMsg.setText(orderInfoMoble.getCard_con());
        EditText orderMsg = ViewHolder.get(bottomView, R.id.order_msg);
        orderMsg.setText(orderInfoMoble.getOrder_remarks());
        EditText money = ViewHolder.get(bottomView, R.id.order_money);
        money.setText(orderInfoMoble.getOrder_amount());
    }

    /**
     * 选择配送时间
     *
     * @param arrayListType
     * @param indexType
     */
    private void initPopwindows(final TypeEntity[] arrayListType, final int indexType) {
        View popView = LayoutInflater.from(BillNewOrderActivity.this).inflate(R.layout.pop_diaolog_layout, null);
        popWin = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置popupWindow弹出窗体的背景
        popWin.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popWin.setOutsideTouchable(true);
        MyGridView spGvPopLayout = (MyGridView) popView.findViewById(R.id.pop_diaolog_gridview);
        spGvPopLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (indexType) {
                    case 0:
                        break;
                    case 1:
                        TextView dvTime = ViewHolder.get(topView, R.id.dv_time_no_input);
                        dvTime.setText(arrayListType[arg2].name);
                        dvTime.setTag(arrayListType[arg2].id);
                        break;
                }
                popWin.dismiss();
            }
        });
        spGvPopLayout.setAdapter(new PopAdapter(arrayListType, indexType));
    }

    class PopAdapter extends BaseAdapter {
        TypeEntity[] arrayListType;
        private int indexType;

        public PopAdapter(TypeEntity[] arrayListType, int indexType) {
            this.arrayListType = arrayListType;
            this.indexType = indexType;
        }

        @Override
        public int getCount() {
            return arrayListType == null ? 0 : arrayListType.length;
        }

        @Override
        public Object getItem(int arg0) {
            return arrayListType[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            PopAdapter.HolderView hv;
            if (arg1 == null) {
                hv = new PopAdapter.HolderView();
                arg1 = arg1.inflate(BillNewOrderActivity.this, R.layout.item_pop_gradview, null);
                hv.popItemTxInfo = (TextView) arg1.findViewById(R.id.item_pop_gd_textinfo);
                arg1.setTag(hv);
            } else {
                hv = (PopAdapter.HolderView) arg1.getTag();
            }
            switch (indexType) {
                case 0:
                    break;
                case 1:
                    TextView dvTime = ViewHolder.get(topView, R.id.dv_time_no_input);
                    if (dvTime.getTag().toString().equals(arrayListType[arg0].id + "")) {
                        hv.popItemTxInfo.setBackgroundColor(getResources().getColor(R.color.back_bg_color));
                        hv.popItemTxInfo.setTextColor(getResources().getColor(R.color.withe));
                    }
                    break;
            }
            hv.popItemTxInfo.setText(arrayListType[arg0].name);
            return arg1;
        }

        class HolderView {
            TextView popItemTxInfo;
        }
    }
}
