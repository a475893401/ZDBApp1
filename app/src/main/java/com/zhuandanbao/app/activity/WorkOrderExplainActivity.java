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

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.appdelegate.WorkOrderExplainD;
import com.zhuandanbao.app.constant.Constants;
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
 * 工单补充说明
 * Created by BFTECH on 2017/3/15.
 */

public class WorkOrderExplainActivity extends BaseHttpActivity<WorkOrderExplainD> {
    private Button button;
    private RecyclerView recyclerView;
    private ImagePickerAdapter adapter;
    private List<ImageItem> list=new ArrayList<>();
    private EditText content;
    private SkipInfoEntity skipInfoEntity;

    @Override
    protected Class<WorkOrderExplainD> getDelegateClass() {
        return WorkOrderExplainD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setTitle("工单补充说明");
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        button=viewDelegate.get(R.id.but_blue);
        recyclerView=viewDelegate.get(R.id.img_recyclerView);
        content=viewDelegate.get(R.id.input_content);
        button.setText("确认提交");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        adapter=new ImagePickerAdapter(context,list,3-list.size(),true);
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
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.but_blue){
            if (StrUtils.isNull(content.getText().toString().trim())){
                viewDelegate.showErrorHint("请输入补充内容",1,null);
                return;
            }
            requestId=2;
            loadingId=1;
            HttpParams params=new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("sn", skipInfoEntity.pushId);
            params.put("content", content.getText().toString().trim());
            params.put("attachment",MUtils.getPath(list,"|"));
            baseHttp("正在上传...",true,Constants.API_WORK_REPLENISH,params);
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            List<ImageItem> itemList= MJsonUtils.jsonToImageItem(baseEntity.data);
            if (itemList.size()>0){
                list.addAll(itemList);
                adapter.setImages(list);
            }
        }
        if (code==2){
            viewDelegate.showSuccessHint("补充已提交成功，我们尽快处理", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(6666);
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
            if (images.size()>0) {
                postPic(images);
            }
        }
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
