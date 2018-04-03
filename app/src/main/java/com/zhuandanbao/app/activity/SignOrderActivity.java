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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.appdelegate.SignOrderD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.SystemSettingEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.FileUtil;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.PictureUtil;
import com.zhuandanbao.app.utils.StrUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单签收
 * Created by BFTECH on 2017/3/8.
 */

public class SignOrderActivity extends BaseHttpActivity<SignOrderD> {
    private SkipInfoEntity skipInfoEntity;
    private RecyclerView recyclerView;
    private ImagePickerAdapter imagePickerAdapter;
    private  int maxImgCount=3;
    private List<ImageItem> itemList=new ArrayList<>();
    private RadioGroup group;
    private String signName="本人签收";
    private Button save;

    private SystemSettingEntity settingEntity;
    @Override
    protected Class<SignOrderD> getDelegateClass() {
        return SignOrderD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        if (null!= MainApplication.cache.getAsObject(Constants.SYSTEM_SETTING)){
            settingEntity= (SystemSettingEntity) MainApplication.cache.getAsObject(Constants.SYSTEM_SETTING);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("订单签收");
        recyclerView=viewDelegate.get(R.id.img_sign_recyclerView);
        group=viewDelegate.get(R.id.sign_name_rg);
        save=viewDelegate.get(R.id.but_blue_hg);
        save.setText("立即签收");
        TextView orderSn=viewDelegate.get(R.id.sign_order_sn);
        TextView orderHint=viewDelegate.get(R.id.sign_order_hint);
        orderSn.setText(Html.fromHtml(StrUtils.setHtml999(null,"订单编号：",skipInfoEntity.pushId)));
        orderHint.setText(Html.fromHtml(StrUtils.getBlueHtml("温馨提示：\n1、请上传签收单拍照或实物图，勿上传非相关图片。\n2、请勿在没有送达的情况下点击签收，送达前确认签收产生的交易纠纷由接单方承担。", "查看详情...", null)));
        orderHint.setOnClickListener(this);
        save.setOnClickListener(this);
        imagePickerAdapter=new ImagePickerAdapter(activity,itemList,maxImgCount,true);
        recyclerView.setAdapter(imagePickerAdapter);
        imagePickerAdapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case Constants.IMAGE_ITEM_ADD:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - itemList.size());
                        Intent intent = new Intent(SignOrderActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
                        break;
                    default:
                        deteleImg(position, true);
                        break;
                }
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.sign_name_rb_self:
                        signName="本人签收";
                        break;
                    case R.id.sign_name_rb_others:
                        signName="他人代收";
                        break;
                }
            }
        });
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            List<ImageItem> list= MJsonUtils.jsonToImageItem(baseEntity.data);
            if (list.size()>0){
                itemList.addAll(list);
                imagePickerAdapter.setImages(itemList);
            }
        }
        if (code==2){
            viewDelegate.showSuccessHint("签收成功", new KProgressDismissClickLister() {
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
        if (view.getId()==R.id.sign_order_hint){
            SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
            skipInfoEntity.pushId="204";
            skipInfoEntity.isPush=false;
            viewDelegate.goToActivity(skipInfoEntity,ZdbMsgInfoActivity.class,0);
        }
        if (view.getId()==R.id.but_blue_hg){
            if (itemList.size()==0&&null != settingEntity && settingEntity.getZD_ORDER_MUST_SIGN_PHOTO().equals("YES")){
                viewDelegate.showErrorHint(settingEntity.getZD_ORDER_MUST_SIGN_PHOTO_EXPLAIN(), 1, null);
                return;
            }
            try {
                if (MUtils.timePast(MUtils.dateToStamp(skipInfoEntity.msg,"yyyy-MM-dd HH:mm:ss"), 120)) {
                    MUtils.showDialog(this, "转单宝提示", "稍后再来", "任性签收", "您刚刚才接了单，未送达前就确认签收造成的客诉问题由您承担哟!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MUtils.dialog.cancel();
                            finish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MUtils.dialog.cancel();
                                signOrder();
                        }
                    });
                } else {
                    signOrder();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 签收
     */
    private void signOrder(){
        requestId=2;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderId", skipInfoEntity.pushId);
        params.put("signUser", signName);
        params.put("signImgs", MUtils.getImageItemPath(itemList, "|"));
        baseHttp("正在提交...",true,Constants.API_ACTION_ORDERS_SIGN,params);
    }

    /**
     * 上傳圖片
     */
    private void postPic(List<ImageItem> fileList){
        if (null==fileList){
            return;
        }
        requestId=1;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        for (int i = 0; i < fileList.size(); i++) {
            try {
                String path = FileUtil.IMAGE_PATH +fileList.get(i).name;
                MLog.e("fileList.get(i).name="+fileList.get(i).name);
                params.put("picture" + i, PictureUtil.saveBitmapFile(PictureUtil.getimage(fileList.get(i).path, width, height), path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        params.put("albname", "complaint");
        baseHttp("正在上传...",true,Constants.API_BATCH_PICTURE_UPLOAD,params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //添加图片返回
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images.size()>0) {
                MLog.e("images.get(0).path="+images.get(0).path);
                postPic(images);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
        }
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
                Intent intentPreview = new Intent(SignOrderActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) imagePickerAdapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                alertDialog.dismiss();
            }
        });
        pcPopCibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                itemList.remove(position);
                imagePickerAdapter.setImages(itemList);
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
