package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.OrderInfoD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShowButText;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.WorkOrderContentEntity;
import com.zhuandanbao.app.entity.WorkOrderInfoListEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单详情
 * Created by BFTECH on 2017/3/15.
 */

public class WorkOrderInfoActivity extends BaseListViewActivity<OrderInfoD> {
    private SkipInfoEntity skipInfoEntity;
    private List<WorkOrderInfoListEntity> listEntities=new ArrayList<>();
    private RvBaseAdapter adapter;
    private View bottomView;
    private WorkOrderContentEntity workOrderContentEntity;
    private ShowButText showButText;

    @Override
    protected Class<OrderInfoD> getDelegateClass() {
        return OrderInfoD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setTitle("工单详情");
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        adapter=new RvBaseAdapter(context, listEntities, R.layout.item_work_info_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                WorkOrderInfoListEntity infoListEntity= (WorkOrderInfoListEntity) item;
                TextView img=holder.getView(R.id.img_hint);
                if (StrUtils.isNull(infoListEntity.getSid()) || "0".equals(infoListEntity.getSid())) {//转单宝
                    img.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.img_nvs_shop), null, null, null);
                    img.setText("转单宝");
                } else {
                    img.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.me_img), null, null, null);
                    img.setText("我");
                }
                holder.setText(R.id.content,infoListEntity.getContent());
                holder.setText(R.id.time, MUtils.getTo66TimePHP(infoListEntity.getTime(),"yyyy-MM-dd HH:ss"));
                List<String> strList= JSON.parseArray(infoListEntity.getAttachment(),String.class);
                RecyclerView recyclerView=holder.getView(R.id.img_recyclerView);
                if (null!=strList&&strList.size()>0){
                    List<ImageItem> itemList=new ArrayList<>();
                    recyclerView.setVisibility(View.VISIBLE);
                    for (String s:strList){
                        ImageItem imageItem=new ImageItem();
                        imageItem.url=s;
                        itemList.add(imageItem);
                    }
                    final ImagePickerAdapter adapter=new ImagePickerAdapter(context,itemList,itemList.size(),true);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intentPreview = new Intent(activity, ImagePreviewDelActivity.class);
                            intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                            intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                            startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                        }
                    });
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        bottomView= LayoutInflater.from(context).inflate(R.layout.item_work_order_bottom,null);
        adapter.addFooterView(bottomView);
        initVerticalRecyclerView(adapter,true,false,1,0);
        loadingId=2;
        getInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            listEntities.clear();
            listEntities.addAll(MJsonUtils.josnToWorkOrderInfoListEntity(baseEntity.data));
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
            workOrderContentEntity=MJsonUtils.josnToWorkOrderContentEntity(baseEntity.data);
            initInfo();
        }
        if (code==2){
            viewDelegate.showSuccessHint("成功撤销此工单", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    finish();
                }
            });
        }
        if (code==3){
            viewDelegate.showSuccessHint("已刪除", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    finish();
                }
            });
        }
    }

    private void initInfo(){
        final TextView time=ViewHolder.get(bottomView,R.id.des_time);
        TextView sn=ViewHolder.get(bottomView,R.id.order_sn);
        TextView des=ViewHolder.get(bottomView,R.id.order_des);
        time.setText(MUtils.getTo66TimePHP(workOrderContentEntity.getTime(),"yyyy-MM-dd HH:ss"));
        sn.setText(Html.fromHtml(StrUtils.setHtml999(null,"工单编号：",workOrderContentEntity.getSn()+StrUtils.setHtmlRed(null,"("+workOrderContentEntity.getStatus_remark()+")",null))));
        des.setText(Html.fromHtml(StrUtils.setHtml999(null,"问题描述：",workOrderContentEntity.getContent())));
        List<String> strList= JSON.parseArray(workOrderContentEntity.getAttachment(),String.class);
        RecyclerView recyclerView=ViewHolder.get(bottomView,R.id.img_recyclerView);
        if (null!=strList&&strList.size()>0){
            List<ImageItem> itemList=new ArrayList<>();
            recyclerView.setVisibility(View.VISIBLE);
            for (String s:strList){
                ImageItem imageItem=new ImageItem();
                imageItem.url=s;
                itemList.add(imageItem);
            }
            final ImagePickerAdapter adapter=new ImagePickerAdapter(context,itemList,itemList.size(),true);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intentPreview = new Intent(activity, ImagePreviewDelActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                    startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                }
            });
        }else {
            recyclerView.setVisibility(View.VISIBLE);
        }
        LinearLayout butLayout=viewDelegate.get(R.id.but_layout);
        if ("2".equals(workOrderContentEntity.getStatus()) || "-1".equals(workOrderContentEntity.getStatus()) ) {
            viewDelegate.setRight("删除", 0, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MUtils.showDialog(activity, "转单宝提示",  "再想想", "删除","你即将删除此工单？",null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MUtils.dialog.cancel();
                            requestId=3;
                            loadingId=1;
                            HttpParams params=new HttpParams();
                            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
                            params.put("sn", workOrderContentEntity.getSn());
                            baseHttp("正在提交...",true,Constants.API_TICKET_DELETE,params);
                        }
                    });
                }
            });
        }else {
            showButText=new ShowButText();
            showButText.isShowOk="补充说明";
            showButText.isShowAmend="撤销工单";
            MUtils.showGradBut(butLayout,showButText,this);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.but_blue){//撤销工单
            MUtils.showDialog(WorkOrderInfoActivity.this, "撤销工单", "再想想", "撤销","你确定撤销工单？",null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MUtils.dialog.cancel();
                    requestId=2;
                    loadingId=1;
                    HttpParams params=new HttpParams();
                    params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
                    params.put("sn", workOrderContentEntity.getSn());
                    baseHttp("正在提交...",true,Constants.API_REPADL_WORK,params);
                }
            });
        }
        if (view.getId()==R.id.but_ok){//补充说明
            SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
            skipInfoEntity.pushId=workOrderContentEntity.getSn();
            viewDelegate.goToActivity(skipInfoEntity,WorkOrderExplainActivity.class,300);
        }
    }

    /**
     * 获取工单详情
     */
    private void getInfo(){
        requestId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("sn",skipInfoEntity.pushId);
        baseHttp(null,true,Constants.API_WORK_INFO,params);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==300&&resultCode==6666){
            loadingId=2;
            getInfo();
        }
    }
}
