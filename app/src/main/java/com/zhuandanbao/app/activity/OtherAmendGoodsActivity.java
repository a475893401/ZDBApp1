package com.zhuandanbao.app.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.appdelegate.AmendGoodsD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderItemEntity;
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
 * 三方订单修改或添加商品
 * Created by BFTECH on 2017/3/16.
 */

public class OtherAmendGoodsActivity extends BaseHttpActivity<AmendGoodsD> {

    private RecyclerView recyclerView;
    private List<ImageItem> imgList = new ArrayList<>();
    private ImagePickerAdapter adapter;
    private SkipInfoEntity skipInfoEntity;
    private OrderItemEntity orderItemEntity = null;
    private List<OrderItemEntity> orderItemEntities = new ArrayList<>();

    private EditText name;
    private EditText des;
    private EditText num;

    @Override
    protected Class<AmendGoodsD> getDelegateClass() {
        return AmendGoodsD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        recyclerView = viewDelegate.get(R.id.img_recyclerView);
        name = viewDelegate.get(R.id.item_name);
        des = viewDelegate.get(R.id.item_des);
        num = viewDelegate.get(R.id.item_num);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            viewDelegate.setTitle("修改商品");
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            orderItemEntities = (List<OrderItemEntity>) skipInfoEntity.data;
            if (skipInfoEntity.index!=-1) {
                orderItemEntity = orderItemEntities.get(skipInfoEntity.index);
                List<String> list = JSON.parseArray(orderItemEntity.getItem_img(), String.class);
                if (list.size() > 0) {
                    for (String s : list) {
                        ImageItem imageItem = new ImageItem();
                        imageItem.url = s;
                        imgList.add(imageItem);
                    }
                }
                name.setText(orderItemEntity.getItem_name());
                des.setText(orderItemEntity.getItem_remarks());
                num.setText(orderItemEntity.getItem_total());
            }
        }else {
            viewDelegate.setTitle("添加商品");
        }
        viewDelegate.setOnClickListener(this, R.id.item_save);
        adapter = new ImagePickerAdapter(context, imgList, 3, true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case Constants.IMAGE_ITEM_ADD:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(3 - imgList.size());
                        Intent intent = new Intent(activity, ImageGridActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
                        break;
                    default:
                        deteleImg(position, true);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.item_save){
            if (StrUtils.isNull(name.getText().toString().trim())){
                viewDelegate.showErrorHint("请输入商品名称",1,null);
                return;
            }
            if (StrUtils.isNull(des.getText().toString().trim())){
                viewDelegate.showErrorHint("请输入商品描述",1,null);
                return;
            }
            if (StrUtils.isNull(num.getText().toString().trim())){
                viewDelegate.showErrorHint("请输入商品数量",1,null);
                return;
            }
            if (imgList.size()==0){
                viewDelegate.showErrorHint("请上传商品图片",1,null);
                return;
            }
            if (null==orderItemEntity){
                orderItemEntity=new OrderItemEntity();
            }
            orderItemEntity.setItem_name(name.getText().toString().trim());
            orderItemEntity.setItem_remarks(des.getText().toString().trim());
            orderItemEntity.setItem_total(num.getText().toString().trim());
            List<String> list=new ArrayList<>();
            for (ImageItem imageItem:imgList){
                list.add(imageItem.url);
            }
            orderItemEntity.setItem_img(JSON.toJSONString(list));
            if (skipInfoEntity.index!=-1){
                orderItemEntities.remove(skipInfoEntity.index);
                orderItemEntities.add(skipInfoEntity.index,orderItemEntity);
            }else {
                orderItemEntities.add(orderItemEntity);
            }
            requestId=2;
            loadingId=1;
            HttpParams params=new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("orderSn", skipInfoEntity.pushId);
            params.put("order_wares", MUtils.getOrderItemString(orderItemEntities));
            baseHttp("正在保存...",true,Constants.API_OTHER_GOODS,params);
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            List<ImageItem> list = MJsonUtils.jsonToImageItem(baseEntity.data);
            if (list.size() > 0) {
                imgList.addAll(list);
                adapter.setImages(imgList);
            }
        }
        if (code==2){
            viewDelegate.showSuccessHint("已保存", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(8888);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //添加图片返回
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images.size() > 0) {
                postPic(images);
            }
        }
    }


    /**
     * 上傳圖片
     */
    private void postPic(List<ImageItem> fileList) {
        if (null == fileList) {
            return;
        }
        requestId = 1;
        loadingId = 1;
        HttpParams params = new HttpParams();
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
        baseHttp("正在上传...", true, Constants.API_BATCH_PICTURE_UPLOAD, params);
    }


    /**
     * 查看 删除
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
                Intent intentPreview = new Intent(activity, ImagePreviewDelActivity.class);
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
