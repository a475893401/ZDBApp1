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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ApproveSuccesD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.BillOrderShopEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.OtherBillEntity;
import com.zhuandanbao.app.entity.OtherInfoEntity;
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
 * 三方订单修改或发单
 * Created by BFTECH on 2017/3/10.
 */

public class OtherAmendOrderInfoActivity extends BaseListViewActivity<ApproveSuccesD> {
    private SkipInfoEntity skipInfoEntity;
    private OtherInfoEntity infoEntity;
    private RvBaseAdapter adapter;
    private List<OrderItemEntity> orderItemEntities = new ArrayList<>();
    private Button button;
    private View topView;
    private View bottomView;
    private boolean isAmend = true;//true  修改

    private TimePickerView deliveryTime;
    private TimePickerView grabTime;

    private PopArea popNW;
    private PopupWindow popWin;
    private int dvType = 1;//2 送貨上門  1快遞
    private int orderType = 1;// 1 搶單  2一对一


    @Override
    protected Class<ApproveSuccesD> getDelegateClass() {
        return ApproveSuccesD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        button = viewDelegate.get(R.id.but_ok);
        if (skipInfoEntity.index == 1) {
            isAmend = false;
        } else {
            isAmend = true;
        }
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, orderItemEntities, R.layout.item_new_order_shop_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, final int position) {
                final OrderItemEntity info = (OrderItemEntity) item;
                holder.setText(R.id.item_goods_name, info.getItem_name());
                holder.setText(R.id.item_goods_des, info.getItem_remarks());
                holder.setText(R.id.item_goods_num, "x " + info.getItem_total());
                BGABanner bgaBanner = holder.getView(R.id.item_img);
                bgaBanner.setAdapter(new BGABanner.Adapter() {
                    @Override
                    public void fillBannerItem(BGABanner banner, View view, Object model, final int position) {
                        String picpath = (String) model;
                        ImageLoader.getInstance().displayImage(picpath, (ImageView) view, MyImage.deployMemory());
                    }
                });
                if (StrUtils.isNotNull(info.getItem_img())) {
                    List<String> list = JSON.parseArray(info.getItem_img(), String.class);
                    bgaBanner.setData(list, null);
                }
                holder.setOnClickListener(R.id.bill_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        orderItemEntities.remove(position);
                        detele();
                    }
                });
            }
        });
        topView = LayoutInflater.from(context).inflate(R.layout.item_other_amend_top, null);
        bottomView = LayoutInflater.from(context).inflate(R.layout.item_other_amend_bottom, null);
        adapter.addHeaderView(topView);
        adapter.addFooterView(bottomView);
        initVerticalRecyclerView(adapter, false, false, 1, 0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                skipInfoEntity.data = orderItemEntities;
                skipInfoEntity.index = position;
                skipInfoEntity.pushId = infoEntity.getOrder_sn();
                viewDelegate.goToActivity(skipInfoEntity, OtherAmendGoodsActivity.class, 300);
            }
        });
        button.setOnClickListener(this);
        showDeliveryDate();
        showGrabDate();
        loadingId = 2;
        getOtherInfo();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.other_amend_add_goods) {
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            skipInfoEntity.data = orderItemEntities;
            skipInfoEntity.index = -1;
            skipInfoEntity.pushId = infoEntity.getOrder_sn();
            viewDelegate.goToActivity(skipInfoEntity, OtherAmendGoodsActivity.class, 300);
        }
        if (view.getId() == R.id.but_ok) {
            if (isAmend) {//保存
                saveInfo();
            } else {//转单
                fadan();
            }
        }
        if (view.getId() == R.id.jiedan_layout) {//选择接单店铺
            TextView dvArea = ViewHolder.get(topView, R.id.other_area);
            SkipInfoEntity skipInfo = new SkipInfoEntity();
            skipInfo.msg = dvArea.getText().toString().trim();
            TextView jiedan = ViewHolder.get(bottomView, R.id.bill_shop_name);
            if (!"-1".equals(jiedan.getTag().toString())) {
                skipInfo.pushId = jiedan.getTag().toString();
            }
            viewDelegate.goToActivity(skipInfo, ShopListActivity.class, 300);
        }
        if (view.getId() == R.id.other_shop_msg) {
            View layout = LayoutInflater.from(context).inflate(R.layout.reason_layout, null);
            final EditText input = (EditText) layout.findViewById(R.id.reason_input);
            input.setHint("请输入商家备注");
            input.setText(infoEntity.getSeller_remark());
            MUtils.showItemViewDialog(this, layout, "修改商家备注", "取消", "保存", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StrUtils.isNull(input.getText().toString().trim()) || input.getText().toString().trim().equals(infoEntity.getSeller_remark())) {
                        viewDelegate.toast("沒有修改不能保存");
                    } else {
                        MUtils.dialog.cancel();
                        amendShopMsg(input.getText().toString().trim());
                    }
                }
            });
        }
        if (view.getId() == R.id.other_date) {
            deliveryTime.show();
        }
        if (view.getId() == R.id.grab_date_layout) {
            grabTime.show();
        }
        if (view.getId() == R.id.other_area) {
            getAddress();
        }
        if (view.getId() == R.id.other_time) {
            TextView dvDate = ViewHolder.get(topView, R.id.other_date);
            if (StrUtils.isNull(dvDate.getText().toString().trim())) {
                viewDelegate.showErrorHint("请先选择配送日期", 1, null);
                return;
            }
            int dvTime = MUtils.getDvTimeList(dvDate.getText().toString().trim());
            MLog.e("dvTime=" + dvTime);
            if (dvTime == 1) {
                initPopwindows(Constants.DvTime, 0);
            } else if (dvTime == 2) {
                initPopwindows(Constants.DvTime1, 0);
            } else if (dvTime == 3) {
                initPopwindows(Constants.DvTime2, 0);
            }
            popWin.showAsDropDown(view, 0, 15);
            popWin.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E9E9E9")));
            popWin.setAnimationStyle(R.style.popwin_anim_style);
            popWin.setFocusable(true);
            popWin.setOutsideTouchable(true);
            popWin.update();
        }
    }

    /**
     * 保存信息
     */
    private void saveInfo() {
        if (!isNull()) {
            return;
        }
        requestId = 4;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", infoEntity.getOrder_sn());
        params.put("consignee", viewDelegate.getTextStr(topView, R.id.other_name));
        params.put("mobile", viewDelegate.getTextStr(topView, R.id.other_phone));
        params.put("areainfo", viewDelegate.getTextStr(topView, R.id.other_area));
        params.put("address", viewDelegate.getTextStr(topView, R.id.other_address));
        params.put("delivery_type", dvType);
        if (dvType == 2) {
            params.put("delivery_date", viewDelegate.getTextStr(topView, R.id.other_date));
            params.put("delivery_time", viewDelegate.getTextStr(topView, R.id.other_time));
        }
        if (StrUtils.isNotNull(viewDelegate.getTextStr(topView, R.id.other_order_msg))) {
            params.put("order_remarks", viewDelegate.getTextStr(topView, R.id.other_order_msg));
        }
        if (StrUtils.isNotNull(viewDelegate.getTextStr(topView, R.id.other_order_card))) {
            params.put("card_con", viewDelegate.getTextStr(topView, R.id.other_order_card));
        }
        baseHttp("正在保存...", true, Constants.API_OTHER_AMEND_INFO, params);
    }

    /**
     * 转发订单
     */
    private void fadan() {
        if (!isNull()) {
            return;
        }
        OtherBillEntity billInfo = new OtherBillEntity();
        if (orderType == 1) {//抢单
            if (StrUtils.isNull(viewDelegate.getTextStr(bottomView, R.id.grab_time))) {
                viewDelegate.showErrorHint("请选择抢单截止时间", 1, new KProgressDismissClickLister() {
                    @Override
                    public void onDismiss() {
                        TextView editText = ViewHolder.get(bottomView, R.id.grab_time);
                        viewDelegate.setViewFocusable(editText);
                    }
                });
                return;
            }
            billInfo.order_type = 1;
            billInfo.deadline = viewDelegate.getTextStr(bottomView, R.id.grab_time);
        } else {//一对一
            if (StrUtils.isNull(viewDelegate.getTextStr(bottomView, R.id.bill_shop_name))) {
                viewDelegate.showErrorHint("请选择接单店铺", 1, new KProgressDismissClickLister() {
                    @Override
                    public void onDismiss() {
                        TextView editText = ViewHolder.get(bottomView, R.id.bill_shop_name);
                        viewDelegate.setViewFocusable(editText);
                    }
                });
                return;
            }
            billInfo.order_type = 0;
            TextView jiedan = ViewHolder.get(bottomView, R.id.bill_shop_name);
            billInfo.ReceiveStoreId = jiedan.getTag().toString();
        }
        billInfo.delivery_type = dvType;
        if (dvType == 2) {
            billInfo.delivery_date = viewDelegate.getTextStr(topView, R.id.other_date);
            billInfo.delivery_time = viewDelegate.getTextStr(topView, R.id.other_time);
        }
        billInfo.areainfo = viewDelegate.getTextStr(topView, R.id.other_area);
        billInfo.address = viewDelegate.getTextStr(topView, R.id.other_address);
        billInfo.consignee = viewDelegate.getTextStr(topView, R.id.other_name);
        billInfo.mobile = viewDelegate.getTextStr(topView, R.id.other_phone);
        billInfo.order_amount = viewDelegate.getTextStr(bottomView, R.id.other_amount);
        billInfo.order_remarks = viewDelegate.getTextStr(topView, R.id.other_order_msg);
        billInfo.card_con = viewDelegate.getTextStr(topView, R.id.other_order_card);
        billInfo.order_wares = MUtils.getOrderItemList(orderItemEntities);
        requestId = 5;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", infoEntity.getOrder_sn());
        params.put("orderParamJson", JSON.toJSONString(billInfo));
        MLog.e("==============" + JSON.toJSONString(billInfo));
        baseHttp("正在创建订单...", true, Constants.API_OTHER_BILL_ORDER, params);
    }

    /**
     * 判断信息
     */
    private boolean isNull() {
        if (dvType == 2) {
            if (StrUtils.isNull(viewDelegate.getTextStr(topView, R.id.other_date))) {
                viewDelegate.showErrorHint("请选择配送日期", 1, new KProgressDismissClickLister() {
                    @Override
                    public void onDismiss() {
                        TextView editText = ViewHolder.get(topView, R.id.other_date);
                        viewDelegate.setViewFocusable(editText);
                    }
                });
                return false;
            }
            if (StrUtils.isNull(viewDelegate.getTextStr(topView, R.id.other_time))) {
                viewDelegate.showErrorHint("请选择配送时间", 1, new KProgressDismissClickLister() {
                    @Override
                    public void onDismiss() {
                        TextView editText = ViewHolder.get(topView, R.id.other_time);
                        viewDelegate.setViewFocusable(editText);
                    }
                });
                return false;
            }
        }
        if (StrUtils.isNull(viewDelegate.getTextStr(topView, R.id.other_area))) {
            viewDelegate.showErrorHint("请选择配送地区", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    EditText editText = ViewHolder.get(topView, R.id.other_area);
                    viewDelegate.setViewFocusable(editText);
                }
            });
            return false;
        }
        if (StrUtils.isNull(viewDelegate.getTextStr(topView, R.id.other_address))) {
            viewDelegate.showErrorHint("请输入详细的配送地址", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    EditText editText = ViewHolder.get(topView, R.id.other_address);
                    viewDelegate.setViewFocusable(editText);
                }
            });
            return false;
        }
        if (StrUtils.isNull(viewDelegate.getTextStr(topView, R.id.other_name))) {
            viewDelegate.showErrorHint("请输入收货人姓名", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    EditText editText = ViewHolder.get(topView, R.id.other_name);
                    viewDelegate.setViewFocusable(editText);
                }
            });
            return false;
        }
        if (StrUtils.isNull(viewDelegate.getTextStr(topView, R.id.other_phone))) {
            viewDelegate.showErrorHint("请输入收货人电话", 1, new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    EditText editText = ViewHolder.get(topView, R.id.other_phone);
                    viewDelegate.setViewFocusable(editText);
                }
            });
            return false;
        }
        if (!isAmend) {
            if (StrUtils.isNull(viewDelegate.getTextStr(bottomView, R.id.other_amount))) {
                viewDelegate.showErrorHint("请输入发单金额", 1, new KProgressDismissClickLister() {
                    @Override
                    public void onDismiss() {
                        final EditText editText = ViewHolder.get(bottomView, R.id.other_amount);
                        viewDelegate.setViewFocusable(editText);
                    }
                });
                return false;
            }
        }
        return true;
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
        final TextView showDvCity = ViewHolder.get(topView, R.id.other_area);
        if (null == popNW) {
            popNW = PopArea.getInstance(josnStr, this);
        }
        TextView dvArea = ViewHolder.get(topView, R.id.other_area);
        String[] spAddressArray = null;
        if (StrUtils.isNotNull(dvArea.getText().toString().trim())) {
            spAddressArray = dvArea.getText().toString().trim().split("\\|");
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
     * 初始化配送时间
     */
    private void showDeliveryDate() {
        final TextView dvDate = ViewHolder.get(topView, R.id.other_date);
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
                if (choiceY < nowY) {
                    viewDelegate.toast("请选择一个合理配送日期!");
                    return;
                } else if (choiceY==nowY){
                    if (choiceM < nowM) {
                        viewDelegate.toast("请选择一个合理配送日期!");
                        return;
                    } else if (choiceM==nowM){
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
     * 删除商品
     */
    private void detele() {
        MUtils.showDialog(context, "删除商品", "再想想", "删除", "您确认删除此商品?", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MUtils.dialog.cancel();
                requestId = 6;
                loadingId = 1;
                HttpParams params = new HttpParams();
                params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
                params.put("orderSn", infoEntity.getOrder_sn());
                params.put("order_wares", MUtils.getOrderItemString(orderItemEntities));
                baseHttp("正在刪除...", true, Constants.API_OTHER_GOODS, params);
            }
        });
    }

    @Override
    public void initData(int code, final BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            orderItemEntities.clear();
            infoEntity = MJsonUtils.jsonToOtherInfoEntity(baseEntity.data);
            orderItemEntities.addAll(MJsonUtils.jsonToOrderItemEntity(infoEntity.getOrder_items()));
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            initInfo();
        }
        if (code == 2) {
            viewDelegate.showSuccessHint("修改成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    loadingId = 1;
                    getOtherInfo();
                }
            });
        }
        if (code == 3) {
            showAddress(baseEntity.data);
        }
        if (code == 4) {
            MUtils.showDialog(this, "转单宝提示", "取消", "是的", "你已成功修改订单！是否需要立即转发订单", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MUtils.dialog.cancel();
                    setResult(6666);
                    finish();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MUtils.dialog.cancel();
                    isAmend = false;
                    loadingId = 2;
                    getOtherInfo();
                }
            });
        }
        if (code == 5) {
            viewDelegate.showSuccessHint("已成功创建订单", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    XiadanEntity xiadanEntity = JSON.parseObject(baseEntity.data, XiadanEntity.class);
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.pushId = xiadanEntity.order_sn;
                    viewDelegate.goToActivity(skipInfoEntity, OrderPayActivity.class, 0);
                    setResult(6666);
                    finish();
                }
            });
        }
        if (code == 6) {
            viewDelegate.showSuccessHint("已刪除", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    adapter.notifyDataSetChanged();
                }
            });
        }

        if (code==7){
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            viewDelegate.showDialog(MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo()),false);
        }
    }

    /**
     * 修改商家备注
     */
    private void amendShopMsg(String msg) {
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", infoEntity.getOrder_sn());
        params.put("seller_remark", msg);
        baseHttp("正在修改...", true, Constants.API_OTHER_SELLER, params);
    }

    /**
     * 初始化信息
     */
    private void initInfo() {
        LinearLayout fadanLayout = ViewHolder.get(bottomView, R.id.other_fadan_layout);
        if (isAmend) {
            viewDelegate.setTitle("修改订单");
            button.setText("保存");
            fadanLayout.setVisibility(View.GONE);
        } else {
            getShopInfo();
            viewDelegate.setTitle("转发订单");
            button.setText("转单");
            fadanLayout.setVisibility(View.VISIBLE);
        }
        TextView amendBut = ViewHolder.get(topView, R.id.other_amend_add_goods);
        amendBut.setOnClickListener(this);
        TextView orderSn = ViewHolder.get(topView, R.id.other_sn);
        TextView orderStatus = ViewHolder.get(topView, R.id.other_status);
        RadioGroup group = ViewHolder.get(topView, R.id.dv_style_rg);
        final LinearLayout dvLayout = ViewHolder.get(topView, R.id.other_dv_layout);
        TextView dvDate = ViewHolder.get(topView, R.id.other_date);
        TextView dvTime = ViewHolder.get(topView, R.id.other_time);
        TextView dvArea = ViewHolder.get(topView, R.id.other_area);
        EditText dvAddress = ViewHolder.get(topView, R.id.other_address);
        EditText dvName = ViewHolder.get(topView, R.id.other_name);
        EditText dvMobile = ViewHolder.get(topView, R.id.other_phone);
        EditText cardMsg = ViewHolder.get(topView, R.id.other_order_card);
        EditText msgInfo = ViewHolder.get(topView, R.id.other_order_msg);
        TextView shopMsg = ViewHolder.get(topView, R.id.other_shop_msg);
        TextView addShop = ViewHolder.get(topView, R.id.other_amend_add_goods);
        TextView sourceShop = ViewHolder.get(topView, R.id.other_shop);
        TextView orderAmount = ViewHolder.get(topView, R.id.other_order_amount);
        sourceShop.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单来源：", infoEntity.getSource_shop_name())));
        orderAmount.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单金额：", infoEntity.getOrder_amount())));
        if (StrUtils.isNotNull(infoEntity.getDelivery_date())) {
            dvDate.setText(infoEntity.getDelivery_date());
        }
        if (StrUtils.isNotNull(infoEntity.getDelivery_time())) {
            dvTime.setText(infoEntity.getDelivery_time());
        }
        dvArea.setText(infoEntity.getAreainfo());
        dvAddress.setText(infoEntity.getAddress());
        dvName.setText(infoEntity.getConsignee());
        dvMobile.setText(infoEntity.getMobile());
        cardMsg.setText(infoEntity.getCard_con());
        msgInfo.setText(infoEntity.getOrder_remarks());
        shopMsg.setText(infoEntity.getSeller_remark());
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.dv_style_1://送货上门
                        dvLayout.setVisibility(View.VISIBLE);
                        dvType = 2;
                        break;
                    case R.id.dv_style_2://快递
                        dvLayout.setVisibility(View.GONE);
                        dvType = 1;
                        break;
                }
            }
        });
        if (infoEntity.getDelivery_type().equals("1")) {//快递
            RadioButton dv2 = ViewHolder.get(topView, R.id.dv_style_2);
            dv2.setChecked(true);
            dvType = 1;
        } else {//送貨
            RadioButton dv1 = ViewHolder.get(topView, R.id.dv_style_1);
            dv1.setChecked(true);
            dvType = 2;
        }
        orderSn.setText(infoEntity.getOrder_sn());
        orderStatus.setText(infoEntity.getOrder_status_remark());
        CheckBox checkBox = ViewHolder.get(bottomView, R.id.bill_order_mode_cb);
        final LinearLayout gradLayout = ViewHolder.get(bottomView, R.id.grab_date_layout);
        final LinearLayout jiedan = ViewHolder.get(bottomView, R.id.jiedan_layout);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gradLayout.setVisibility(View.VISIBLE);
                    jiedan.setVisibility(View.GONE);
                    orderType = 1;
                } else {
                    gradLayout.setVisibility(View.GONE);
                    jiedan.setVisibility(View.VISIBLE);
                    orderType = 2;
                }
            }
        });
        addShop.setOnClickListener(this);
        gradLayout.setOnClickListener(this);
        jiedan.setOnClickListener(this);
        shopMsg.setOnClickListener(this);
        dvDate.setOnClickListener(this);
        dvArea.setOnClickListener(this);
        dvTime.setOnClickListener(this);
    }

    private void getOtherInfo() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", skipInfoEntity.pushId);
        baseHttp(null, true, Constants.API_OTHER_ORDER_INFO, params);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == 300) {
            if (null != data.getSerializableExtra(ShopListActivity.JIE_DAN_SID)) {
                BillOrderShopEntity jiedanInfo = (BillOrderShopEntity) data.getSerializableExtra(ShopListActivity.JIE_DAN_SID);
                MLog.e("=====" + jiedanInfo.getShop_name());
                TextView jiedan = ViewHolder.get(bottomView, R.id.bill_shop_name);
                jiedan.setText(jiedanInfo.getShop_name());
                jiedan.setTag(jiedanInfo.getSid());
            }
        }
        if (requestCode == 300 && resultCode == 8888) {
            loadingId = 2;
            getOtherInfo();
        }

        if (requestCode==300&&(resultCode==Constants.SHOP_GUIDE_INFO_SUCCESS
                ||resultCode==Constants.SHOP_GUIDE_CONTACT_SUCCESS
                ||resultCode==Constants.SHOP_GUIDE_PASS_SUCCESS)){
            getShopInfo();
        }
    }

    private void initPopwindows(final TypeEntity[] arrayListType, final int indexType) {
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_diaolog_layout, null);
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
                        TextView dvTime = ViewHolder.get(topView, R.id.other_time);
                        if (arrayListType[arg2].name.equals("自定义")) {
                            setZidingyi();
                        } else {
                            dvTime.setText(arrayListType[arg2].name);
                            dvTime.setTag((arrayListType[arg2].id));
                        }
                        break;
                }
                popWin.dismiss();
            }
        });
        spGvPopLayout.setAdapter(new PopAdapter(arrayListType, indexType));
    }

    class PopAdapter extends BaseAdapter {
        TypeEntity[] listDTEntiy;
        private int indexType;

        public PopAdapter(TypeEntity[] onListShpEntity, int indexType) {
            this.listDTEntiy = onListShpEntity;
            this.indexType = indexType;
        }

        @Override
        public int getCount() {
            return listDTEntiy == null ? 0 : listDTEntiy.length;
        }

        @Override
        public Object getItem(int arg0) {
            return listDTEntiy[arg0];
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
                arg1 = arg1.inflate(context, R.layout.item_pop_gradview, null);
                hv.popItemTxInfo = (TextView) arg1.findViewById(R.id.item_pop_gd_textinfo);
                arg1.setTag(hv);
            } else {
                hv = (HolderView) arg1.getTag();
            }
            switch (indexType) {
                case 0:
                    TextView dvTime = ViewHolder.get(topView, R.id.other_time);
                    if (listDTEntiy[arg0].name.equals(dvTime.getText().toString())) {
                        hv.popItemTxInfo.setBackgroundColor(getResources().getColor(R.color.back_bg_color));
                        hv.popItemTxInfo.setTextColor(getResources().getColor(R.color.withe));
                    }
                    break;
            }
            hv.popItemTxInfo.setText(listDTEntiy[arg0].name);
            return arg1;
        }

        class HolderView {
            TextView popItemTxInfo;
        }
    }

    private void setZidingyi() {
        View reason = LayoutInflater.from(context).inflate(R.layout.reason_layout, null);
        final EditText input = (EditText) reason.findViewById(R.id.reason_input);
        input.setHint("请填写配送时间");
        MUtils.showItemViewDialog(context, reason, "自定义配送时间", "返回", "确认", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StrUtils.isNull(input.getText().toString())) {
                    Toast.makeText(context, "请填写配送时间", Toast.LENGTH_LONG).show();
                    return;
                }
                MUtils.dialog.cancel();
                TextView dvTime = ViewHolder.get(topView, R.id.other_time);
                dvTime.setText(input.getText().toString().trim());
                dvTime.setTag(8);
            }
        });
    }


    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        requestId = 7;
        loadingId = 0;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("", true, Constants.API_GET_SHOP_INFO, params);
    }
}
