package com.zhuandanbao.app.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.adapter.WorkSpinnerAdapter;
import com.zhuandanbao.app.appdelegate.AddWorkOrderD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.WorkOrderTypeEntity;
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
 * 创建工单
 * Created by BFTECH on 2017/3/16.
 */

public class AddWorkOrderActivity extends BaseHttpActivity<AddWorkOrderD> {

    private RecyclerView recyclerView;
    private ImagePickerAdapter adapter;
    private List<ImageItem> list = new ArrayList<>();

    private List<WorkOrderTypeEntity> typeList = new ArrayList<>();
    private List<WorkOrderTypeEntity> typeItemList = new ArrayList<>();
    private WorkSpinnerAdapter typeAdapter;
    private WorkSpinnerAdapter typeItemAdapter;
    private Spinner workType;
    private Spinner workQType;

    private EditText workQDsc;
    private EditText workPhone;

    private TextView okBtn;
    private LinearLayout shopLayout;
    private EditText shopInfo;

    private String parentID;

    private SkipInfoEntity skipInfoEntity;


    @Override
    protected Class<AddWorkOrderD> getDelegateClass() {
        return AddWorkOrderD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("创建工单");
        recyclerView = viewDelegate.get(R.id.img_recyclerView);
        workType = viewDelegate.get(R.id.work_order_type);
        workQType = viewDelegate.get(R.id.work_order_quest_type);
        workQDsc = viewDelegate.get(R.id.work_order_quest_dsc);
        workPhone = viewDelegate.get(R.id.work_order_quest_phone);
        okBtn = viewDelegate.get(R.id.work_btn);
        shopLayout = viewDelegate.get(R.id.work_order_shop_layout);
        shopInfo = viewDelegate.get(R.id.work_order_shop);
        adapter = new ImagePickerAdapter(context, list, 3 - list.size(), true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case Constants.IMAGE_ITEM_ADD:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(3 - list.size());
                        Intent intent = new Intent(activity, ImageGridActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
                        break;
                    default:
                        deteleImg(position, true);
                        break;
                }
            }
        });
        okBtn.setOnClickListener(this);
        if (StrUtils.isNotNull(MainApplication.cache.getAsString(Constants.SHOP_INFO))) {
            ShopInfoItemEntity shopInfo = MJsonUtils.josnToShopInfoItemEntity(MainApplication.cache.getAsString(Constants.SHOP_INFO));
            workPhone.setText(shopInfo.getMobile());
        }
        if (skipInfoEntity.index == 4) {
            shopInfo.setText(skipInfoEntity.msg);
        }
        getType();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.work_btn) {//添加工单
            if (StrUtils.isNull(workQDsc.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入问题描述", 1, null);
                return;
            }
            if (!StrUtils.isMobileNum(workPhone.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入正确手机号", 1, null);
                return;
            }
            if (parentID.equals("5") && StrUtils.isNull(shopInfo.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入投诉的店铺名称或店主手机号", 1, null);
                return;
            }
            requestId = 3;
            loadingId = 1;
            HttpParams params = new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("parentCateID", typeAdapter.getSpItemView().getTag().toString());
            params.put("CateID", typeItemAdapter.getSpItemView().getTag().toString());
            params.put("content", workQDsc.getText().toString().trim());
            params.put("mobile", workPhone.getText().toString().trim());
            params.put("attachment", MUtils.getPath(list, "|"));
            if (parentID.equals("5")) {
                params.put("to_shop_name", shopInfo.getText().toString().trim());
            }
            baseHttp("正在提交...", true, Constants.API_ADD_WORK, params);
        }
    }

    /**
     * 获取工单类型
     */
    private void getType() {
        requestId = 2;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_WORK_TYPE, params);
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            List<ImageItem> itemList = MJsonUtils.jsonToImageItem(baseEntity.data);
            if (itemList.size() > 0) {
                list.addAll(itemList);
                adapter.setImages(list);
            }
        }
        if (code == 2) {
            typeList.clear();
            typeItemList.clear();
            typeList.addAll(MJsonUtils.josnToWorkOrderTypeEntity(baseEntity.data, false));
            typeAdapter = new WorkSpinnerAdapter(context, typeList);
            workType.setAdapter(typeAdapter);
            typeItemList.addAll(MJsonUtils.josnToWorkOrderTypeEntity(typeList.get(0).getItem(), true));
            typeItemAdapter = new WorkSpinnerAdapter(context, typeItemList);
            workQType.setAdapter(typeItemAdapter);
            workType.setSelection(skipInfoEntity.index);
            workType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    typeItemList.clear();
                    typeItemList.addAll(MJsonUtils.josnToWorkOrderTypeEntity(typeList.get(position).getItem(), true));
                    parentID = typeList.get(position).getId();
                    if (typeList.get(position).getId().equals("5")) {
                        shopLayout.setVisibility(View.VISIBLE);
                    } else {
                        shopLayout.setVisibility(View.GONE);
                    }
                    typeItemAdapter.notifyDataSetChanged();
                    workQType.setSelection(skipInfoEntity.item);
                    skipInfoEntity.item = 0;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        if (code == 3) {
            viewDelegate.showSuccessHint("提交成功,我们在2-3个工作日回复", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
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
     * 查看  删除
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
                list.remove(position);
                adapter.setImages(list);
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
