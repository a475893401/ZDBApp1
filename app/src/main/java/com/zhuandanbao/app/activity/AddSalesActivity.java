package com.zhuandanbao.app.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.appdelegate.AddSalesD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.SalesTypeEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.FileUtil;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.PictureUtil;
import com.zhuandanbao.app.utils.StrUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 申请售后
 * Created by BFTECH on 2017/3/15.
 */

public class AddSalesActivity extends BaseHttpActivity<AddSalesD> {
    private SkipInfoEntity skipInfoEntity;
    private OrderDataEntity dataEntity;
    private Button button;
    private List<SalesTypeEntity> list=new ArrayList<>();
    private TextView salesTypeTx;
    private EditText salesMoney;
    private EditText inputContent;
    private RecyclerView recyclerView;
    private List<ImageItem> imgList=new ArrayList<>();
    private ImagePickerAdapter adapter;
    private int maxImgCount=3;
    private int index=0;
    private String applyId;
    private int applyMobe=1;
    @Override
    protected Class<AddSalesD> getDelegateClass() {
        return AddSalesD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            dataEntity= (OrderDataEntity) skipInfoEntity.data;
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("申请售后");
        button=viewDelegate.get(R.id.but_blue);
        button.setText("申请售后");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        TextView orderSn=viewDelegate.get(R.id.order_sn);
        TextView orderAmount=viewDelegate.get(R.id.order_amount);
        TextView orderJiedan=viewDelegate.get(R.id.jiedan_shop);
        salesTypeTx=viewDelegate.get(R.id.sales_type_tx);
        salesMoney=viewDelegate.get(R.id.sales_money);
        inputContent=viewDelegate.get(R.id.sales_input_content);
        recyclerView=viewDelegate.get(R.id.img_recyclerView);
        orderSn.setText(Html.fromHtml(StrUtils.setHtml999(null,"订单编号：",dataEntity.getOrder_sn())));
        orderAmount.setText(Html.fromHtml(StrUtils.setHtml999(null,"订单金额：","￥ "+dataEntity.getOrder_amount())));
        orderJiedan.setText(Html.fromHtml(StrUtils.setHtml999(null,"接单店铺：",dataEntity.getReceive_shop_name())));
        salesMoney.setEnabled(false);
        salesMoney.setText(dataEntity.getOrder_amount());
        orderJiedan.setOnClickListener(this);
        adapter=new ImagePickerAdapter(context,imgList,maxImgCount-imgList.size(),true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case Constants.IMAGE_ITEM_ADD:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - imgList.size());
                        Intent intent = new Intent(AddSalesActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
                        break;
                    default:
                        deteleImg(position, true);
                        break;
                }
            }
        });
        getSalesType();
    }


    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            list.clear();
            list.addAll(MJsonUtils.josnToSalesTypeEntity(baseEntity.data));
            if (list.size()>0) {
                initSalesType();
            }
        }
        if (code==2){
            List<ImageItem> list= MJsonUtils.jsonToImageItem(baseEntity.data);
            if (list.size()>0){
                imgList.addAll(list);
                adapter.setImages(imgList);
            }
        }
        if (code==3){
            viewDelegate.showSuccessHint("申请售后已完成，等待对方回复", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(6666);
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.jiedan_shop){//接单店铺
            SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
            skipInfoEntity.pushId=dataEntity.getReceive_sid();
            viewDelegate.goToActivity(skipInfoEntity,ShopDetailsActivity.class,0);
        }
        if (view.getId()==R.id.sales_type_tx){
            SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
            skipInfoEntity.data=list;
            skipInfoEntity.index=index;
            viewDelegate.goToActivity(skipInfoEntity,SalesTypeListActivity.class,400);
        }
        if (view.getId()==R.id.but_blue){
            if (StrUtils.isNull(inputContent.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入退款原因",1,null);
                return;
            }
            if (StrUtils.isNull(salesMoney.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入退款金额",1,null);
                return;
            }
            requestId=3;
            loadingId=1;
            HttpParams params=new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("orderSn", dataEntity.getOrder_sn());
            params.put("return_mode", applyMobe);
            params.put("issue_type", applyId);
            params.put("applyReason", inputContent.getText().toString().trim());
            params.put("return_amount", salesMoney.getText().toString().trim());
            if (null != MUtils.getPathStr(imgList)) {
                params.put("return_img", MUtils.getImageItemPath(imgList, "|"));
            }
            baseHttp("正在提交...",true,Constants.API_ORDER_SALE_APPLY,params);
        }
    }

    private void initSalesType(){
        SalesTypeEntity info =list.get(index);
        applyId=info.getId();
        salesTypeTx.setText(info.getName());
        salesTypeTx.setOnClickListener(this);
        RadioGroup group=viewDelegate.get(R.id.sales_group);
        final RadioButton full=viewDelegate.get(R.id.sales_full);
        RadioButton part=viewDelegate.get(R.id.sales_part);
        RadioButton oneByone=viewDelegate.get(R.id.sales_one_by_one);
        if (info.getFull_return().equals("1")){
            full.setVisibility(View.VISIBLE);
        }else {
            full.setVisibility(View.GONE);
        }
        if (info.getPart_return().equals("1")){
            part.setVisibility(View.VISIBLE);
        }else {
            part.setVisibility(View.GONE);
        }
        if (info.getDouble_return().equals("1")){
            oneByone.setVisibility(View.VISIBLE);
        }else {
            oneByone.setVisibility(View.GONE);
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.sales_full:
                        salesMoney.setEnabled(false);
                        salesMoney.setText(dataEntity.getOrder_amount());
                        applyMobe = 1;
                        break;
                    case R.id.sales_part:
                        salesMoney.setEnabled(true);
                        salesMoney.setText(dataEntity.getOrder_amount());
                        applyMobe = 2;
                        break;
                    case R.id.sales_one_by_one:
                        salesMoney.setEnabled(false);
                        salesMoney.setText(Double.parseDouble(dataEntity.getOrder_amount()) * 2 + "");
                        applyMobe = 3;
                        break;
                }
            }
        });
    }

    private void getSalesType(){
        requestId=1;
        loadingId=2;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null,true,Constants.API_ORDER_SALE_KIND,params);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==400&&resultCode==6666){
            if (null!=data){
                index=data.getIntExtra(Constants.SKIP_INFO_ID,0);
                initSalesType();
            }
        }
        //添加图片返回
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images.size()>0) {
                postPic(images);
            }
        }
    }

    /**
     * 上传图片
     * @param fileList
     */
    private void postPic(List<ImageItem> fileList){
        if (null==fileList){
            return;
        }
        requestId=2;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        for (int i = 0; i < fileList.size(); i++) {
            try {
                String path = FileUtil.IMAGE_PATH + fileList.get(i).name;
                params.put("picture" + i, PictureUtil.saveBitmapFile(PictureUtil.getimage(fileList.get(i).path, width, height), path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        params.put("albname", "complaint");
        baseHttp("正在上传...",true,Constants.API_BATCH_PICTURE_UPLOAD,params);
    }

    /**
     * 上传  查看  删除
     * @param position
     * @param isDetele
     */
    private void deteleImg(final int position, boolean isDetele) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(R.layout.pop_select_image);
        View views = window.getDecorView();
        Button pcCibtn = (Button) views.findViewById(R.id.pop_photo_check_img);//查看
        Button pcPopPibtn = (Button) views.findViewById(R.id.pop_photo_image);//相机
        Button pcPopCibtn = (Button) views.findViewById(R.id.pop_camera_image);//选择
        Button pcPopBackbtn = (Button) views.findViewById(R.id.pop_back_callimage);//返回
        pcPopPibtn.setText("查看");
        pcPopCibtn.setText("删除");
        if (isDetele) {
            pcPopCibtn.setVisibility(View.VISIBLE);
        } else {
            pcPopCibtn.setVisibility(View.GONE);
        }
        pcPopPibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开预览
                Intent intentPreview = new Intent(AddSalesActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                alertDialog.dismiss();
            }
        });
        pcPopCibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                imgList.remove(position);
                adapter.setImages(imgList);
            }
        });
        pcPopBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
