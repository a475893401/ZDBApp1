package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.WithdrawD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.BackListEntity;
import com.zhuandanbao.app.entity.FinanceEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.SystemSettingEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.TimeCountUtil;
import com.zhuandanbao.app.utils.ViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提现
 * Created by BFTECH on 2017/3/14.
 */

public class WithdrawActivity extends BaseListViewActivity<WithdrawD> {

    private List<BackListEntity> listEntities = new ArrayList<>();
    private RvBaseAdapter adapter;
    private View bottomView;
    private SkipInfoEntity skipInfoEntity;
    private FinanceEntity financeEntity;
    private Button button;
    private EditText inputMoney;
    private TextView getVerify;
    private TimeCountUtil countUtil;
    private int index = 0;
    private EditText verifyCode;
    private EditText pass;

    private LinearLayout conLayout;
    private LinearLayout enjoinLayout;

    private LinearLayout topView;

    private SystemSettingEntity systemSettingEntity = null;

    @Override
    protected Class<WithdrawD> getDelegateClass() {
        return WithdrawD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            financeEntity = (FinanceEntity) skipInfoEntity.data;
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("提现");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        conLayout = viewDelegate.get(R.id.withdraw_con_layout);
        enjoinLayout = viewDelegate.get(R.id.enjoin_withdraw);
        topView=viewDelegate.get(R.id.withdraw_top_layout);
        button = viewDelegate.get(R.id.but_blue);
        button.setText("确认提现");
        button.setOnClickListener(this);
        adapter = new RvBaseAdapter(context, listEntities, R.layout.item_back_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, final int position) {
                final BackListEntity info = (BackListEntity) item;
                holder.setText(R.id.item_back_name, info.getAccount());
                holder.setText(R.id.item_back_content, info.getName() + "(" + info.getBank_card_num().substring(info.getBank_card_num().length() - 4, info.getBank_card_num().length()) + ")");
                ImageView img = holder.getView(R.id.item_back_img);
                ImageLoader.getInstance().displayImage(info.getLogo(), img, MyImage.deployMemory());
                CheckBox checkBox = holder.getView(R.id.checkBox);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(info.isCheck());
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean b = info.isCheck();
                        for (BackListEntity infos : listEntities) {
                            infos.setCheck(false);
                        }
                        info.setCheck(!b);
                        if (!b) {
                            index = position;
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        bottomView = LayoutInflater.from(context).inflate(R.layout.item_withdraw_bottom, null);
        adapter.addFooterView(bottomView);
        initVerticalRecyclerView(adapter, false, false, 1, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSetting();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            listEntities.clear();
            listEntities.addAll(MJsonUtils.jsonToBackListEntity(baseEntity.data));
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            initInfo();
        }
        if (code == 2) {
            countUtil = new TimeCountUtil(activity, 120 * 1000, 1000, getVerify);
            countUtil.start();
            viewDelegate.showSuccessHint("已发送成功，请注意查收", null);
        }
        if (code == 3) {
            financeEntity = MJsonUtils.jsonToFinanceEntity(baseEntity.data);
            getBack();
        }
        if (code == 4) {
            systemSettingEntity = MJsonUtils.jsonToSystemSettingEntity(baseEntity.data);
            MainApplication.cache.put(Constants.SYSTEM_SETTING, systemSettingEntity);
            if (null == skipInfoEntity) {
                getFinanceInfo();
            } else {
                getBack();
            }
        }
    }

    private void initInfo() {
        long now = System.currentTimeMillis();
        long start = 0;
        long stop = 0;
        if (StrUtils.isNotNull(systemSettingEntity.getZD_PROHIBIT_WITHDRAW_DATE_LIMIT())) {
            String[] str = new String[2];
            str = systemSettingEntity.getZD_PROHIBIT_WITHDRAW_DATE_LIMIT().split("\\|");
            if (str.length > 0) {
                try {
                    if (StrUtils.isNotNull(str[0])) {
                        start = MUtils.dateToStamp(str[0],"yyyy-MM-dd");
                    }
                    if (StrUtils.isNotNull(str[1])) {
                        stop = MUtils.dateToStamp(str[1],"yyyy-MM-dd");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (null != systemSettingEntity && systemSettingEntity.getZD_ALLOW_WITHDRAW().equals("NO") && now >= start && now <= stop) {//禁止提現
            enjoinLayout.setVisibility(View.VISIBLE);
            conLayout.setVisibility(View.GONE);
            superRecyclerView.setVisibility(View.GONE);
            topView.setVisibility(View.GONE);
            TextView conetnt = viewDelegate.get(R.id.content);
            TextView time = viewDelegate.get(R.id.time);
            conetnt.setText(systemSettingEntity.getZD_PROHIBIT_WITHDRAW_REASON());
            if (StrUtils.isNotNull(systemSettingEntity.getZD_PROHIBIT_WITHDRAW_DATE_LIMIT())) {
                time.setText("暂停提现日期：" + systemSettingEntity.getZD_PROHIBIT_WITHDRAW_DATE_LIMIT());
            }
        } else if (null!=financeEntity&&(financeEntity.getIs_audit().equals("0") || financeEntity.getIs_set_paypass() == 0)) {
            conLayout.setVisibility(View.VISIBLE);
            topView.setVisibility(View.VISIBLE);
            enjoinLayout.setVisibility(View.GONE);
            superRecyclerView.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            if (financeEntity.getIs_set_paypass() == 0) {
                TextView con1 = viewDelegate.get(R.id.withdrawals_con_1);
                con1.setVisibility(View.VISIBLE);
                con1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewDelegate.goToActivity(null, PayPasswordActivity.class, 0);
                    }
                });
            }
            if (financeEntity.getIs_audit().equals("0")) {
                TextView con2 = viewDelegate.get(R.id.withdrawals_con_2);
                con2.setVisibility(View.VISIBLE);
                con2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewDelegate.goToActivity(null, ApproveActivity.class, 0);
                    }
                });
            }
        } else {
            enjoinLayout.setVisibility(View.GONE);
            conLayout.setVisibility(View.GONE);
            topView.setVisibility(View.VISIBLE);
            superRecyclerView.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            TextView noBack = viewDelegate.get(R.id.withdraw_no_back);
            if (listEntities.size() == 0) {
                noBack.setVisibility(View.VISIBLE);
                noBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                        skipInfoEntity.index = 2;
                        viewDelegate.goToActivity(skipInfoEntity, AddBackActivity.class, 300);
                    }
                });
            } else {
                noBack.setVisibility(View.GONE);
            }
        }
        TextView money = viewDelegate.get(R.id.withdraw_money);
        TextView backList = viewDelegate.get(R.id.withdraw_back);
        TextView forgetPass = ViewHolder.get(bottomView, R.id.withdraw_forget_pass);
        TextView hintMsg = ViewHolder.get(bottomView, R.id.withdrawals_hint_message);
        getVerify = ViewHolder.get(bottomView, R.id.withdraw_get_verify);
        verifyCode = ViewHolder.get(bottomView, R.id.withdraw_verify_code);
        pass = ViewHolder.get(bottomView, R.id.withdraw_pass);
        money.setText(Html.fromHtml(StrUtils.setHtml999(null, "可提现金额：", financeEntity.getAvailable_balance())));
        inputMoney = ViewHolder.get(bottomView, R.id.withdraw_input_money);
        if (financeEntity.getMonth_free_Withdraw_max_num() > 0) {
            hintMsg.setText(getString(R.string.withdrawals_hint_message)
                    + "\n2、单次提现金额不小于" + systemSettingEntity.getZD_SINGLE_WITHDRAW_MIN_AMOUNT() + "元且不大于" + systemSettingEntity.getZD_SINGLE_WITHDRAW_MAX_AMOUNT() + "元。"
                    + "\n3、每个自然月可免费提现" + systemSettingEntity.getZD_WITHDRAW_MONTH_FREE_NUM() + "次，超过" + systemSettingEntity.getZD_WITHDRAW_MONTH_FREE_NUM()
                    + "次后每笔收取" + systemSettingEntity.getZD_SINGLE_WITHDRAW_FEE() + "元提现手续费。"
                    + "\n4、店铺每日最多可申请提现" + systemSettingEntity.getZD_DAY_WITHDRAW_LIMIT()
                    + "次，每日累计提现申请额度" + systemSettingEntity.getZD_DAY_WITHDRAW_MAX_AMOUNT() + "元"
            );
        } else {
            hintMsg.setText(getString(R.string.withdrawals_hint_message)
                    + "\n2、单次提现金额不小于" + systemSettingEntity.getZD_SINGLE_WITHDRAW_MIN_AMOUNT() + "元且不大于" + systemSettingEntity.getZD_SINGLE_WITHDRAW_MAX_AMOUNT() + "元。"
                    + "\n3、每次提现须支付提现手续费" + systemSettingEntity.getZD_SINGLE_WITHDRAW_FEE() + "元。"
                    + "\n4、店铺每日最多可申请提现" + systemSettingEntity.getZD_DAY_WITHDRAW_LIMIT()
                    + "次，每日累计提现申请额度" + systemSettingEntity.getZD_DAY_WITHDRAW_MAX_AMOUNT() + "元");
        }
        backList.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
        getVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.withdraw_back) {
            viewDelegate.goToActivity(null, BackListActivity.class, 300);
        }
        if (view.getId() == R.id.withdraw_forget_pass) {
            viewDelegate.goToActivity(null, ForgetPasswordActivity.class, 0);
        }
        if (view.getId() == R.id.but_blue) {
            if (listEntities.size() == 0) {
                viewDelegate.showErrorHint("请先添加银行卡", 1, null);
                return;
            }
            if (StrUtils.isNull(inputMoney.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入提现金额", 1, null);
                return;
            }
            double money = Double.parseDouble(inputMoney.getText().toString().trim());
            if (money < systemSettingEntity.getZD_SINGLE_WITHDRAW_MIN_AMOUNT() || money > systemSettingEntity.getZD_SINGLE_WITHDRAW_MAX_AMOUNT()) {
                viewDelegate.showErrorHint("单笔提现金额需在" + systemSettingEntity.getZD_SINGLE_WITHDRAW_MIN_AMOUNT() + " 至 " + systemSettingEntity.getZD_SINGLE_WITHDRAW_MAX_AMOUNT() + "之间", 1, null);
                return;
            }
            if (StrUtils.isNull(verifyCode.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入验证码", 1, null);
                return;
            }
            if (StrUtils.isNull(pass.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入转单宝支付密码", 1, null);
                return;
            }
            BackListEntity backListEntity = listEntities.get(index);
            requestId = 3;
            loadingId = 1;
            HttpParams params = new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("bcid", backListEntity.getBcid());
            params.put("amount", inputMoney.getText().toString().trim());
            params.put("smscode", verifyCode.getText().toString());
            params.put("paypass", pass.getText().toString());
            baseHttp("正在提交...", true, Constants.API_FINANCE_BANK_CARD_GET_MONEY, params);
        }
        if (view.getId() == R.id.withdraw_get_verify) {
            if (listEntities.size() == 0) {
                viewDelegate.showErrorHint("请先添加银行卡", 1, null);
                return;
            }
            if (StrUtils.isNull(inputMoney.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入提现金额", 1, null);
                return;
            }
            double money = Double.parseDouble(inputMoney.getText().toString().trim());
            if (money < systemSettingEntity.getZD_SINGLE_WITHDRAW_MIN_AMOUNT() || money > systemSettingEntity.getZD_SINGLE_WITHDRAW_MAX_AMOUNT()) {
                viewDelegate.showErrorHint("单笔提现金额需在" + systemSettingEntity.getZD_SINGLE_WITHDRAW_MIN_AMOUNT() + " 至 " + systemSettingEntity.getZD_SINGLE_WITHDRAW_MAX_AMOUNT() + "之间", 1, null);
                return;
            }
            try {
                if (systemSettingEntity.getZD_DAY_WITHDRAW_LIMIT() <= financeEntity.getToday_withdraw_num() && systemSettingEntity.getZD_DAY_WITHDRAW_LIMIT() != 0) {
                    viewDelegate.showErrorHint("你今日提现次数已达上限", 1, null);
                    return;
                }
                if (StrUtils.isNotNull(financeEntity.getToday_withdraw_amount())
                        && Double.parseDouble(financeEntity.getToday_withdraw_amount()) >= systemSettingEntity.getZD_DAY_WITHDRAW_MAX_AMOUNT()
                        && systemSettingEntity.getZD_DAY_WITHDRAW_MAX_AMOUNT() != 0) {
                    viewDelegate.showErrorHint("你今日提现金额已达上限", 1, null);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            requestId = 2;
            loadingId = 1;
            HttpParams params = new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("smscode", "getcode");
            baseHttp("正在获取验证码...", true, Constants.API_FINANCE_BANK_CARD_GET_MONEY, params);
        }
    }

    private void getBack() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_GET_FINANCE_ADD_BANK_CARD_LISTS, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != countUtil) {
            countUtil.cancel();
        }
    }

    /**
     * 获取财务信息API_GET_FINANCE_INFO
     */
    private void getFinanceInfo() {
        requestId = 3;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_GET_FINANCE_INFO, params);
    }

    /**
     * 获取设置信息
     */
    private void getSetting() {
        requestId = 4;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_SYSTEMS_SETTING, params);
    }
}
