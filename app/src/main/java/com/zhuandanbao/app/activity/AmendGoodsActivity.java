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
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.adapter.UILImageLoader;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.FileUtil;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.PictureUtil;
import com.zhuandanbao.app.utils.StrUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 添加/修改商品（发布新单）
 * Created by BFTECH on 2017/1/9.
 */

public class AmendGoodsActivity extends BaseListViewActivity<ShopD> {
    public static String GOODS_ITEM_INFO = "item_goods_info";
    private List<OrderItemEntity> baseList;
    private RvBaseAdapter adapter;
    private int maxImgCount = 3;
    private OrderItemEntity goodsEntity;
    private boolean isAmend = true;
    private SkipInfoEntity skipInfoEntity;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        initImagePicker();
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            goodsEntity = (OrderItemEntity) skipInfoEntity.data;
            isAmend = true;
            viewDelegate.setTitle("修改商品");
        } else {
            goodsEntity = new OrderItemEntity();
            isAmend = false;
            viewDelegate.setTitle("添加商品");
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        baseList = new ArrayList<>();
        baseList.add(goodsEntity);
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(this, baseList, R.layout.item_amend_goods_info, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                final OrderItemEntity info = (OrderItemEntity) item;
                if (StrUtils.isNotNull(info.getItem_name())) {
                    holder.setText(R.id.item_name, info.getItem_name());
                }
                if (StrUtils.isNotNull(info.getItem_remarks())) {
                    holder.setText(R.id.item_des, info.getItem_remarks());
                }
                if (StrUtils.isNotNull(info.getItem_total())) {
                    holder.setText(R.id.item_num, info.getItem_total() + "");
                }
                final EditText name = holder.getView(R.id.item_name);
                final EditText des = holder.getView(R.id.item_des);
                final EditText num = holder.getView(R.id.item_num);
                holder.setOnClickListener(R.id.item_save, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (StrUtils.isNull(name.getText().toString().trim())) {
                            viewDelegate.showErrorHint("请输入商品名称", 1, null);
                            return;
                        }
                        if (StrUtils.isNull(des.getText().toString().trim())) {
                            viewDelegate.showErrorHint("请输入商品描述", 1, null);
                            return;
                        }
                        if (StrUtils.isNull(num.getText().toString().trim())) {
                            viewDelegate.showErrorHint("请输入商品数量", 1, null);
                            return;
                        }
                        if (Integer.parseInt(num.getText().toString().trim()) <= 0) {
                            viewDelegate.showErrorHint("商品数量不能为0", 1, null);
                            return;
                        }
                        if (StrUtils.isNull(info.getItem_img())) {
                            viewDelegate.showErrorHint("请上传商品图片", 1, null);
                            return;
                        }
                        info.setItem_name(name.getText().toString().trim());
                        info.setItem_remarks(des.getText().toString().trim());
                        info.setItem_total(num.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra(GOODS_ITEM_INFO, info);
                        setResult(200, intent);
                        finish();
                    }
                });
                RecyclerView recyclerView = holder.getView(R.id.img_recyclerView);
                List<ImageItem> list = null;
                if (null == list) {
                    list = new ArrayList<>();
                } else {
                    list.clear();
                }
                if (StrUtils.isNotNull(info.getItem_img())) {
                    List<String> list1 = JSON.parseArray(info.getItem_img(), String.class);
                    list.addAll(itemTos(list1));
                }
                ImagePickerAdapter adapter = null;
                if (null == adapter) {
                    adapter = new ImagePickerAdapter(context, list, maxImgCount, true);
                } else {
                    adapter.notifyDataSetChanged();
                }
                recyclerView.setAdapter(adapter);
                final List<ImageItem> finalList = list;
                final ImagePickerAdapter finalAdapter = adapter;
                adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case Constants.IMAGE_ITEM_ADD:
                                //打开选择,本次允许选择的数量
                                info.setItem_name(name.getText().toString().trim());
                                info.setItem_remarks(des.getText().toString().trim());
                                info.setItem_total(num.getText().toString());
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - finalList.size());
                                Intent intent = new Intent(context, ImageGridActivity.class);
                                startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
                                break;
                            default:
                                deteleImg(position, true, null, finalAdapter);
                                break;
                        }
                    }
                });
            }
        });
        initVerticalRecyclerView(adapter, false, false, 1, 0);
    }

    /**
     * 查看  删除  图片
     *
     * @param position
     * @param isDetele
     * @param list
     * @param adapter
     */
    private void deteleImg(final int position, boolean isDetele, final List<String> list, final ImagePickerAdapter adapter) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
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
                Intent intentPreview = new Intent(context, ImagePreviewDelActivity.class);
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
                List<String> dataList = JSON.parseArray(baseList.get(0).getItem_img(), String.class);
                dataList.remove(position);
                adapter.setImages(itemTos(dataList));
            }
        });
        pcPopBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void initData(int code, final BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            viewDelegate.showSuccessHint("商品图片上传成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    List<String> list = null;
                    List<ImageItem> listImg = new ArrayList<>();
                    listImg.addAll(JSON.parseArray(baseEntity.data, ImageItem.class));
                    if (StrUtils.isNotNull(baseList.get(0).getItem_img())) {
                        list = JSON.parseArray(baseList.get(0).getItem_img(), String.class);
                        list.addAll(itemToString(listImg));
                    } else {
                        list = new ArrayList<>();
                        list.addAll(itemToString(listImg));
                    }
                    baseList.get(0).setItem_img(JSON.toJSONString(list));
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 图片预览配置
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new UILImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setMultiMode(true);                     //单选
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(3);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(600);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(600);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(800);                         //保存文件的高度。单位像素
    }

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == Constants.REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                uploadImg(images);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == Constants.REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                baseList.get(0).setItem_img(JSON.toJSONString(itemToString(images)));
            }
        }
    }

    /**
     * 上传图片
     */
    private void uploadImg(final List<ImageItem> list) {
        String path = "";
        if (null == list) {
            return;
        }
        requestId = 1;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("albname", "complaint");
        for (int i = 0; i < list.size(); i++) {
            MLog.e("name===" + list.get(i).name);
            try {
                path = FileUtil.IMAGE_PATH + list.get(i).name;
                MLog.e("file====" + FileUtil.IMAGE_PATH + list.get(i).name);
                params.put("picture" + i, PictureUtil.saveBitmapFile(PictureUtil.getimage(list.get(i).path, 0, 0), path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        baseHttp("正在上传图片...", true, Constants.API_BATCH_PICTURE_UPLOAD, params);
    }

    /**
     * 把string类型的转换为ImageItem
     *
     * @param ingList
     * @return
     */
    private List<String> itemToString(List<ImageItem> ingList) {
        List<String> list = new ArrayList<>();
        for (ImageItem info : ingList) {
            if (StrUtils.isNotNull(info.url)) {
                MLog.e("-------------------------" + info.url);
                list.add(info.url);
            }
        }
        return list;
    }

    /**
     * 把ImageItem类型的转换为String
     *
     * @param imgList
     * @return
     */
    private List<ImageItem> itemTos(List<String> imgList) {
        List<ImageItem> list = new ArrayList<>();
        for (String info : imgList) {
            if (StrUtils.isNotNull(info)) {
                ImageItem im = new ImageItem();
                im.url = info;
                list.add(im);
            } else {
                imgList.remove(info);
            }
        }
        return list;
    }
}
