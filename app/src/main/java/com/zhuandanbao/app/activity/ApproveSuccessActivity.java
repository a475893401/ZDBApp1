package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ApproveSuccesD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ApproveSuccessEntity;
import com.zhuandanbao.app.entity.ItemEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证成功
 * Created by BFTECH on 2017/2/21.
 */

public class ApproveSuccessActivity extends BaseListViewActivity<ApproveSuccesD> {
    private ApproveSuccessEntity info;
    private List<ItemEntity> list=new ArrayList<>();
    private RvBaseAdapter adapter;
    private TextView hintText;

    @Override
    protected Class<ApproveSuccesD> getDelegateClass() {
        return ApproveSuccesD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回",R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("认证成功");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        Button button=viewDelegate.get(R.id.but_ok);
        button.setVisibility(View.GONE);
        initApprove();
        loadingId = 2;
        getApproveInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        info = MJsonUtils.josnToApproveSuccessEntity(baseEntity.data);
        setInfo(info);
        superRecyclerView.completeRefresh();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        loadingId=0;
        getApproveInfo();
    }

    /**
     * 获取认证信息
     */
    private void getApproveInfo() {
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_GET_APPROVE_INFO, params);
    }

    /**
     *创建信息列表
     * @param info
     */
    private void setInfo(ApproveSuccessEntity info) {
        list.clear();
        ItemEntity approve1 = new ItemEntity();
        approve1.name = "店主姓名";
        approve1.content = info.getPerson_name();
        list.add(approve1);
        ItemEntity approve2 = new ItemEntity();
        approve2.name = "身份证号";
        approve2.content = info.getCard_id();
        list.add(approve2);
        ItemEntity approve3 = new ItemEntity();
        approve3.name = "企业名称";
        approve3.content = info.getCorporation_name();
        list.add(approve3);
        ItemEntity approve4 = new ItemEntity();
        approve4.name = "执照号码";
        approve4.content = info.getBusiness_license_id();
        list.add(approve4);
        ItemEntity approve5 = new ItemEntity();
        approve5.name = "许可证号";
        approve5.content = info.getFood_circulation_permit();
        list.add(approve5);
    }

    /**
     * 显示认证成功的信息
     */
    private void initApprove() {
        adapter = new RvBaseAdapter(this, list, R.layout.item_approve_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                ItemEntity info = (ItemEntity) item;
                if (StrUtils.isNotNull(info.content)) {
                    holder.setVisible(R.id.layout, true);
                } else {
                    holder.setVisible(R.id.layout, false);
                }
                holder.setText(R.id.name, info.name);
                int len = info.content.length();
                if (len > 0) {
                    if (position == 0) {
                        holder.setText(R.id.content, info.content.charAt(0) + getChar(2));
                    }
                    if (position == 1 || position == 3 || position == 4) {
                        holder.setText(R.id.content, info.content.charAt(0) + getChar(8) + info.content.charAt(len - 1));
                    }
                    if (position == 2) {
                        holder.setText(R.id.content, info.content);
                    }
                } else {
                    holder.setText(R.id.content, info.content);
                }
            }
        });
        View view= LayoutInflater.from(this).inflate(R.layout.item_approve_top_layout,null);
        hintText= (TextView) view.findViewById(R.id.approve_hint_text);
        hintText.setText(Html.fromHtml(StrUtils.setHtmlRed("您的店铺已","认证成功",null)));
        adapter.addHeaderView(view);
        initVerticalRecyclerView(adapter,true,false,1,0);
    }

    /**
     * 对信息进行模糊化处理
     * @param j
     * @return
     */
    private String getChar(int j) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < j; i++) {
            str.append("*");
        }
        return str.toString();
    }
}
