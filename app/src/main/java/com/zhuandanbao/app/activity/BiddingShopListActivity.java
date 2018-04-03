package com.zhuandanbao.app.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blog.www.guideview.Component;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.BiddingShopListD;
import com.zhuandanbao.app.component.BiddingShopListComponent;
import com.zhuandanbao.app.component.JiedanComponent;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.BiddingOrderInfoEntity;
import com.zhuandanbao.app.entity.BiddingShopListEntity;
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

/**
 * 选择竞单店铺
 * Created by BFTECH on 2017/3/8.
 */

public class BiddingShopListActivity extends BaseListViewActivity<BiddingShopListD> {
    private Button button;
    private View topView = null;
    private RvBaseAdapter adapter;
    private List<BiddingShopListEntity> list = new ArrayList<>();
    private SkipInfoEntity skipInfoEntity;
    private BiddingOrderInfoEntity orderInfoEntity;
    private PopupWindow popWindow;
    private int showTimes = 0;

    @Override
    protected Class<BiddingShopListD> getDelegateClass() {
        return BiddingShopListD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("选择竞单店铺");
        button = viewDelegate.get(R.id.but_blue);
        button.setText("确认发单");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, list, R.layout.item_bidding_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                final BiddingShopListEntity info = (BiddingShopListEntity) item;
                holder.setText(R.id.shop_name, info.getShop_name());
                LinearLayout rz_layout = holder.getView(R.id.shop_rz_layout);
                if (info.getIs_audit().equals("0")) {
                    rz_layout.setVisibility(View.GONE);
                } else if (info.getIs_audit().equals("1")) {
                    rz_layout.setVisibility(View.VISIBLE);
                }
                TextView cx = holder.getView(R.id.shop_cx);
                if (info.getAssure_credit().equals("1")) {
                    cx.setVisibility(View.VISIBLE);
                } else if (info.getAssure_credit().equals("0")) {
                    cx.setVisibility(View.GONE);
                }
                holder.setText(R.id.bidding_money, Html.fromHtml(StrUtils.setHtmlRed("报价：", "￥" + info.getBidding_price(), null)));
                holder.setText(R.id.bidding_time, info.getBidding_time());
                holder.setOnClickListener(R.id.shop_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//店鋪詳情
                        SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
                        skipInfoEntity.pushId=info.getSid();
                        viewDelegate.goToActivity(skipInfoEntity,ShopDetailsActivity.class,0);
                    }
                });
                final CheckBox checkBox = holder.getView(R.id.checkBox);
                checkBox.setChecked(info.isCheck);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean b = info.isCheck;
                        check();
                        info.isCheck = !b;
                        adapter.notifyDataSetChanged();
                    }
                });
                if (position == 0 && showTimes == 0 && 1 != PerfHelper.getIntData(Constants.BIDDING_SHOP_LIST_HINT)) {
                    final LinearLayout layout = holder.getView(R.id.layout);
                    layout.post(new Runnable() {
                        @Override
                        public void run() {
                            showGuideView(layout);
                        }
                    });
                }
            }
        });
        adapter.addHeaderView(getTopView());
        initVerticalRecyclerView(adapter, true, false, 1, 0);
        loadingId = 2;
        getBiddingShop();
    }

    /**
     * 弹出层引导
     * @param targetView
     */
    private void showGuideView(View targetView) {
        PerfHelper.setInfo(Constants.BIDDING_SHOP_LIST_HINT, 1);
        showTimes++;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView)
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
                showGuideView2(button);
            }
        });
        builder.addComponent(new BiddingShopListComponent());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show((Activity) context);
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

        builder1.addComponent(new JiedanComponent());
        Guide guide = builder1.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show((Activity) context);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getBiddingShop();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            list.clear();
            list.addAll(MJsonUtils.jsonToBiddingShopListEntity(baseEntity.data));
            orderInfoEntity = MJsonUtils.jsonToBiddingOrderInfoEntity(baseEntity.data);
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            initInfo();
        }
        if (code == 2) {
            viewDelegate.showSuccessHint("您已成功发单", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    finish();
                }
            });
        }
    }

    /**
     * 显示信息
     */
    private void initInfo() {
        TextView orderSn = ViewHolder.get(topView, R.id.order_sn);
        TextView orderStatus = ViewHolder.get(topView, R.id.order_status_text);
        TextView orderMoney = ViewHolder.get(topView, R.id.order_money);
        orderSn.setText(orderInfoEntity.getOrder_sn());
        orderStatus.setText(orderInfoEntity.getOrder_status_remark());
        orderMoney.setText("￥ " + orderInfoEntity.getOrder_amount());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_blue) {
            showPopup(view);
        }
    }

    /**
     * 显示支付密码
     * @param parent
     */
    private void showPopup(View parent) {
        BiddingShopListEntity infoEntity = null;
        for (BiddingShopListEntity info : list) {
            if (info.isCheck) {
                infoEntity = info;
            }
        }
        if (null == infoEntity) {
            viewDelegate.showErrorHint("请先选择店铺", 1, null);
            return;
        }
        View view = null;
        if (null == popWindow) {
            view = LayoutInflater.from(context).inflate(R.layout.item_bidding_input_password, null);
            // 创建一个PopuWidow对象
            popWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
        if (null != view) {
            EditText input = ViewHolder.get(view, R.id.input_pass);
            popupInputMethodWindow(input);
            pay(infoEntity.getSid(), view);
        }
        backgroundAlpha(0.6f);
        //popupwindow弹出时的动画     popWindow.setAnimationStyle(R.style.popupWindowAnimation);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(false);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置菜单显示的位置
        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //监听菜单的关闭事件
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        //监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    /**
     * 显示软件盘
     * @param editText
     */
    private void popupInputMethodWindow(final EditText editText) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    /**
     * 去支付
     * @param sid
     * @param view
     */
    private void pay(final String sid, View view) {
        ImageView back = ViewHolder.get(view, R.id.pay_back);
        final EditText input = ViewHolder.get(view, R.id.input_pass);
        TextView ok = ViewHolder.get(view, R.id.pay_but);
        TextView forget = ViewHolder.get(view, R.id.forget_pass);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != popWindow) {
                    popWindow.dismiss();
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StrUtils.isNull(input.getText().toString().trim())) {
                    viewDelegate.showErrorHint("请先输入转单宝支付密码", 1, null);
                    return;
                }
                if (null != popWindow) {
                    popWindow.dismiss();
                }
                postShop(sid, input.getText().toString().trim());
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != popWindow) {
                    popWindow.dismiss();
                }
                startActivity(new Intent(context, ForgetPasswordActivity.class));
            }
        });
    }

    /**
     * 获取竞单店铺
     */
    private void getBiddingShop() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderCode", skipInfoEntity.pushId);
        baseHttp(null, true, Constants.API_BIDDING_SHOP_LIST, params);
    }

    /**
     * 確認店鋪
     *
     * @param sid
     * @param pass
     */
    private void postShop(String sid, String pass) {
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderCode", skipInfoEntity.pushId);
        params.put("sid", sid);
        params.put("paypass", pass);
        baseHttp("正在提交...", true, Constants.API_CONFIRM_BIDDING_SHOP, params);
    }

    private View getTopView() {
        topView = LayoutInflater.from(this).inflate(R.layout.item_bidding_top_layout, null);
        return topView;
    }

    private void check() {
        for (BiddingShopListEntity info : list) {
            info.isCheck = false;
        }
    }
}
