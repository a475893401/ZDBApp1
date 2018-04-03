package com.zhuandanbao.app.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopImageD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopImageEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.FileUtil;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.PictureUtil;
import com.zhuandanbao.app.utils.StrUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺形象照
 * Created by BFTECH on 2017/2/23.
 */

public class ShopImageActivity extends BaseListViewActivity<ShopImageD> {

    private List<String> list = new ArrayList<>();
    private ShopImageEntity info;
    private RvBaseAdapter adapter;
    private int imageIndex=-1;

    @Override
    protected Class<ShopImageD> getDelegateClass() {
        return ShopImageD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("店铺形象照");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, list, R.layout.item_shop_image, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                String url= (String) item;
                ImageView imageView=holder.getView(R.id.shop_image);
                if (StrUtils.isNotNull(url)){
                    ImageLoader.getInstance().displayImage(url,imageView, MyImage.deployMemory());
                }
            }
        });
        initVerticalRecyclerView(adapter, false, false, 3, 2);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                imageIndex=position;
                String url= (String) item;
                if (StrUtils.isNull(url)) {
                    //打开选择,本次允许选择的数量
                    ImagePicker.getInstance().setSelectLimit(1);
                    Intent intent = new Intent(ShopImageActivity.this, ImageGridActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
                }else {
                    showDialog("查看大图", "上传图片");
                }
            }
        });
        getShopImage();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            info = MJsonUtils.jsonToShopImageEntity(baseEntity.data);
            list.clear();
            list.add(info.getPhoto_1());
            list.add(info.getPhoto_2());
            list.add(info.getPhoto_3());
            list.add(info.getPhoto_4());
            list.add(info.getPhoto_5());
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
        }
        if (code==2){
            viewDelegate.showSuccessHint("上传成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    getShopImage();
                }
            });
        }
    }

    /**
     * 获取形象照
     */
    private void getShopImage() {
        requestId = 1;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_ACTION_SHOP_FACEPHOTO, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == Constants.REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                postPic(images.get(0));
            }
        }
    }

    /**
     * 上传图片
     */
    private String path = "";
    private void postPic(ImageItem imageItem) {
        if (StrUtils.isNull(imageItem.path)) {
            return;
        }
        path = FileUtil.IMAGE_PATH + imageItem.name;
        File file = null;
        try {
            file = PictureUtil.saveBitmapFile(PictureUtil.getimage(imageItem.path, 0, 0), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("sort", imageIndex + 1);
        params.put("picture", file);
        baseHttp("正在上传图片...", true, Constants.API_ACTION_SHOP_FACE_UPLOAD, params);
    }


    /**
     * 显示选项文字
     *
     * @param one 第一个
     * @param two 第二个
     */
    private AlertDialog myDialog;
    private void showDialog(String one, String two) {
        if (null!=myDialog){
            myDialog.show();
            return;
        }
        myDialog = new AlertDialog.Builder(this).create();
        myDialog.setView(LayoutInflater.from(this).inflate(R.layout.pop_select_image, null));
        myDialog.show();
        myDialog.getWindow().setContentView(R.layout.pop_select_image);
        myDialog.getWindow().setGravity(Gravity.BOTTOM);
        myDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        Button oneBtn = (Button) myDialog.findViewById(R.id.pop_photo_image);
        Button twoBtn = (Button) myDialog.findViewById(R.id.pop_camera_image);
        Button cancleBtn = (Button) myDialog.findViewById(R.id.pop_back_callimage);
        oneBtn.setText(one);
        twoBtn.setText(two);
        if (StrUtils.isNull(list.get(imageIndex))) {
            oneBtn.setVisibility(View.GONE);
        }
        oneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                ImageItem imageItem=new ImageItem();
                imageItem.url=list.get(imageIndex);
                List<ImageItem> list1 = null;
                if (null!=list1){
                    list1.clear();
                }else {
                    list1=new ArrayList<>();
                }
                list1.add(imageItem);
                //打开预览
                Intent intentPreview = new Intent(ShopImageActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) list1);
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, imageIndex);
                startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
            }
        });
        twoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent = new Intent(ShopImageActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myDialog != null) {
                    myDialog.cancel();
                }
            }
        });
    }
}
