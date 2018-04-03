package com.zhuandanbao.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.InfoLogEntity;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 三方订单跟踪
 * Created by BFTECH on 2017/3/9.
 */

public class OtherInfoLogFragment extends BaseListViewFragment<ShopD> {
    private String orderSn=null;
    private RvBaseAdapter adapter;
    private List<InfoLogEntity> list=new ArrayList<>();
    private View topView;

    public static OtherInfoLogFragment getInstance(String orderSn) {
        OtherInfoLogFragment fragment = new OtherInfoLogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_INDEX, orderSn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initD() {
        orderSn=getArguments().getString(FRAGMENT_INDEX);
        loadingId=2;
        getIngoLog();
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        adapter=new RvBaseAdapter(context, list, R.layout.item_info_log, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                InfoLogEntity info = (InfoLogEntity) item;
                if (position == 0) {
                    holder.setImageResource(R.id.item_log_img, R.mipmap.img_log_bule);
                    holder.setTextColor(R.id.item_log_name, Color.parseColor("#2383cd"));
                } else {
                    holder.setImageResource(R.id.item_log_img, R.mipmap.img_log_gray);
                    holder.setTextColor(R.id.item_log_name, Color.parseColor("#333333"));
                }
                if (StrUtils.isNull(info.getLogs()) && info.getLogistics().equals("log")) {
                    holder.setVisible(R.id.item_log_time,false);
                    holder.setText(R.id.item_log_name, "暂无订单跟踪");
                }else {
                    holder.setVisible(R.id.item_log_time,true);
                    holder.setText(R.id.item_log_name, info.getLogistics() + "  " + info.getLogs());
                    holder.setText(R.id.item_log_time, info.getTime());
                }
            }
        });
        adapter.addHeaderView(getTopView());
        initVerticalRecyclerView(adapter,true,false,1,0);
    }

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            list.clear();
            List<InfoLogEntity> itemList= MJsonUtils.jsonToInfoLogEntity(baseEntity.data);
            OrderDataEntity orderDataEntity=MJsonUtils.jsonToLogOrderDataEntity(baseEntity.data);
            int len=itemList.size();
            if (len>0){
                for (int i=len-1;i>=0;i--){
                    list.add(itemList.get(i));
                }
            }else {
                InfoLogEntity infoLogEntity=new InfoLogEntity();
                infoLogEntity.setLogistics("log");
                infoLogEntity.setLogs("");
                list.add(infoLogEntity);
            }
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            TextView status= ViewHolder.get(topView,R.id.item_log_order_stats);
            TextView orderSn=ViewHolder.get(topView,R.id.item_log_order_code);
            status.setText(Html.fromHtml(StrUtils.setHtml999(null,"订单状态   ",orderDataEntity.getOrder_status_remark())));
            orderSn.setText(Html.fromHtml(StrUtils.setHtml999(null,"订单编号   ",orderDataEntity.getOrder_sn())));
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getIngoLog();
    }

    private View getTopView(){
        topView= LayoutInflater.from(context).inflate(R.layout.item_info_log_top,null);
        return topView;
    }

    /**
     * 获取接单log
     */
    private void getIngoLog(){
        requestId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderCode",orderSn);
        baseHttp(null,true,Constants.API_OTHER_INFO_LOG,params);
    }
}
