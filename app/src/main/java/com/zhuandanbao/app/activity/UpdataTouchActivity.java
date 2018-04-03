package com.zhuandanbao.app.activity;

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

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.UpdataTouchD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopTouchItemEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import app.zhuandanbao.com.reslib.widget.MyGridView;

/**
 * 更新或添加店铺联系人
 * Created by BFTECH on 2017/2/23.
 */

public class UpdataTouchActivity extends BaseHttpActivity<UpdataTouchD> {
    private Button button;
    private ShopTouchItemEntity info;
    private boolean isAmend = false;//是否未修改  是：修改
    private EditText name;
    private EditText mobile;
    private TextView job;
    private EditText phone;
    private EditText qq;
    private EditText mail;
    private PopupWindow popWin;

    @Override
    protected Class<UpdataTouchD> getDelegateClass() {
        return UpdataTouchD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        button = viewDelegate.get(R.id.but_blue_hg);
        button.setText("保存");
        name = viewDelegate.get(R.id.touch_name);
        mobile = viewDelegate.get(R.id.touch_mobile);
        job = viewDelegate.get(R.id.touch_job);
        phone = viewDelegate.get(R.id.touch_phone);
        qq = viewDelegate.get(R.id.touch_qq);
        mail = viewDelegate.get(R.id.touch_mail);
        if (null != getIntent().getSerializableExtra("touch")) {
            info = (ShopTouchItemEntity) getIntent().getSerializableExtra("touch");
            viewDelegate.setTitle("修改店铺联系人");
            isAmend = true;
            name.setText(info.getUser());
            mobile.setText(info.getMobile());
            job.setText(info.getType_name());
            job.setTag(info.getType());
            phone.setText(info.getPhone());
            qq.setText(info.getQq());
            mail.setText(info.getEmail());
            viewDelegate.setRight("删除", 0, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detele();
                }
            });
        } else {
            viewDelegate.setTitle("添加店铺联系人");
            isAmend = false;
        }
        job.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        String str = "更新成功";
        if (code == 1) {
            if (!isAmend) {
                str = "添加成功";
            }
        }
        if (code == 2) {
            str = "成功刪除";
        }
        viewDelegate.showSuccessHint(str, new KProgressDismissClickLister() {
            @Override
            public void onDismiss() {
                setResult(Constants.SHOP_GUIDE_CONTACT_SUCCESS);
                finish();
            }
        });
    }

    /**
     * 删除店铺联系人
     */
    private void detele() {
        MUtils.showDialog(context, "转单宝提示", "再想想", "删除", "你即将删除此店铺联系人？", null,new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MUtils.dialog.cancel();
                requestId = 2;
                loadingId = 1;
                HttpParams params = new HttpParams();
                params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
                params.put("contact_id", info.getId());
                baseHttp("正在刪除...", true, Constants.API_ACTION_SHOP_DELETECONTACTS, params);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.touch_job) {
            initPopwindows(Constants.SHOPTYPE);
            popWin.showAsDropDown(job, 0, 15);
            popWin.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E9E9E9")));
            popWin.setAnimationStyle(R.style.popwin_anim_style);
            popWin.setFocusable(true);
            popWin.setOutsideTouchable(true);
            popWin.update();
        }
        if (view.getId() == R.id.but_blue_hg) {
            updata();
        }
    }

    /**
     * 添加和更新
     */
    private void updata() {
        if (StrUtils.isNull(name.getText().toString().trim())) {
            viewDelegate.showErrorHint("请填写联系人姓名", 1, null);
            return;
        }
        if (!StrUtils.isMobileNum(mobile.getText().toString().trim())) {
            viewDelegate.showErrorHint("请正确填写联系人手机号", 1, null);
            return;
        }
        if (StrUtils.isNotNull(phone.getText().toString().trim()) && !StrUtils.isPhoneNumber(phone.getText().toString().trim())) {
            viewDelegate.showErrorHint("请填写正确的电话号码", 1, null);
            return;
        }
        if (StrUtils.isNotNull(mail.getText().toString().trim()) && !StrUtils.isEmail(mail.getText().toString().trim())) {
            viewDelegate.showErrorHint("请填写正确的邮箱", 1, null);
            return;
        }
        requestId = 1;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        if (isAmend) {
            params.put("contact_id", info.getId());
        }
        params.put("type", job.getTag().toString().trim());
        params.put("user", name.getText().toString().trim());
        params.put("mobile", mobile.getText().toString().trim());
        params.put("phone", phone.getText().toString().trim());
        params.put("qq", qq.getText().toString().trim());
        params.put("email", mail.getText().toString().trim());
        String loadingStr = "正在提交...";
        if (isAmend) {
            loadingStr = "正在更新...";
        }
        baseHttp(loadingStr, true, Constants.API_ACTION_SHOP_ADDCONTACTS, params);
    }

    public void initPopwindows(String[] arrayListType) {
        View popView = LayoutInflater.from(UpdataTouchActivity.this).inflate(R.layout.pop_diaolog_layout, null);
        popWin = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置popupWindow弹出窗体的背景
        popWin.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popWin.setOutsideTouchable(true);
        MyGridView spGvPopLayout = (MyGridView) popView.findViewById(R.id.pop_diaolog_gridview);
        spGvPopLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                job.setText(Constants.SHOPTYPE[arg2]);
                job.setTag(arg2);
                popWin.dismiss();
            }
        });
        spGvPopLayout.setAdapter(new PopAdapter(arrayListType));
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
            return listShopContEntiy[0];
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
                arg1 = arg1.inflate(UpdataTouchActivity.this, R.layout.item_pop_gradview, null);
                hv.popItemTxInfo = (TextView) arg1.findViewById(R.id.item_pop_gd_textinfo);
                arg1.setTag(hv);
            } else {
                hv = (HolderView) arg1.getTag();
            }
            if (listShopContEntiy[arg0].equals(job.getText().toString().trim())) {
                hv.popItemTxInfo.setBackgroundColor(getResources().getColor(R.color.back_bg_color));
                hv.popItemTxInfo.setTextColor(getResources().getColor(R.color.withe));
            }
            hv.popItemTxInfo.setText(listShopContEntiy[arg0]);
            hv.popItemTxInfo.setTag(arg0);
            return arg1;
        }

        class HolderView {
            TextView popItemTxInfo;
        }
    }

}
