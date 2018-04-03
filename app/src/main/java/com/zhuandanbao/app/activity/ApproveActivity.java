package com.zhuandanbao.app.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImageApproveAdapter;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.adapter.UILImageLoader;
import com.zhuandanbao.app.appdelegate.ApproveSuccesD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ApproveSuccessEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.CardIdUtil;
import com.zhuandanbao.app.utils.FileUtil;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.PictureUtil;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺认证
 * Created by BFTECH on 2017/2/21.
 */

public class ApproveActivity extends BaseListViewActivity<ApproveSuccesD> {
    private ApproveSuccessEntity approveInfo;
    private RvBaseAdapter adapter;
    private List<ApproveSuccessEntity> list = new ArrayList<>();
    private View contentView;
    private Button butOk;

    private List<ImageItem> cradList = new ArrayList<>();
    private List<ImageItem> comList = new ArrayList<>();
    private List<ImageItem> picList = new ArrayList<>();
    private ImageApproveAdapter cardAdapter;
    private ImageApproveAdapter comAdapter;
    private ImageApproveAdapter picAdapter;

    private int clickP = -1;//點擊的那個類型 1 身份證 2 營業 3 食品 4門頭照 5授权
    private int clickPItem = -1;// 點擊item位置

    private boolean isSuccess = false;
    private String path = "";

    private SkipInfoEntity skipInfoEntity=null;

    @Override
    protected Class<ApproveSuccesD> getDelegateClass() {
        return ApproveSuccesD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        initImagePicker();
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("店铺认证");
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        butOk = viewDelegate.get(R.id.but_ok);
        butOk.setOnClickListener(this);
        adapter = new RvBaseAdapter(this, list, R.layout.item_approve_top_layout, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
            }
        });
        contentView = LayoutInflater.from(context).inflate(R.layout.item_approve_bottom_layout, null);
        adapter.addFooterView(contentView);
        initVerticalRecyclerView(adapter, false, false, 1, 0);
        getApproveInfo();
    }

    @Override
    public void initData(int code, final BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            approveInfo = MJsonUtils.josnToApproveSuccessEntity(baseEntity.data);
            list.clear();
            superRecyclerView.completeRefresh();
            initApproveData();
        }
        if (code == 2) {
            viewDelegate.showSuccessHint("上传成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    refreshPic(MJsonUtils.jsonToImageItem(baseEntity.data).get(0));
                    FileUtil.deleteFile(path);
                }
            });
        }
        if (code == 3) {
            viewDelegate.showSuccessHint("提交成功，请耐心等待审核!", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    if (null!=skipInfoEntity&&skipInfoEntity.index==1){
                        setResult(Constants.SHOP_GUIDE_RE_SUCCESS);
                        finish();
                    }else {
                        getApproveInfo();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_ok) {
            postApprove();
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getApproveInfo();
    }

    /**
     * 获取认证信息
     */
    private void getApproveInfo() {
        requestId = 1;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_GET_APPROVE_INFO, params);
    }

    /**
     * 加载数据
     */
    private void initApproveData() {
        ShopInfoItemEntity shopInfo = MJsonUtils.josnToShopInfoItemEntity(MainApplication.cache.getAsString(Constants.SHOP_INFO));
        viewDelegate.showShopinfoDialog(shopInfo,"请先完善店铺基本信息");
        initList();
        TextView hintTx=ViewHolder.get(contentView,R.id.hint_TX);
        hintTx.setText(Html.fromHtml("温馨提示：1、为增加审核通过率，请保证上传证件的清晰度并能准确辨别。" +
                "2、店主与营业执照法人不一致时，必须上传取得营业执照法人授权的委托书（认证公函）。" +
                "<a href=\"http://www.zhuandan.com/down/zhuandanbao_renzheng_gonghan.doc\"><font color='#2383CD'>下载公函</font></a>"));
        hintTx.setMovementMethod(LinkMovementMethod.getInstance());  //其实就这一句是关键
        LinearLayout hintLayout = ViewHolder.get(contentView, R.id.approve_hint_layout);
        ImageView hintImg = ViewHolder.get(contentView, R.id.approve_hint_img);
        TextView hintText = ViewHolder.get(contentView, R.id.approve_hint_text);
        TextView hintReason = ViewHolder.get(contentView, R.id.approve_hint_reason);
        LinearLayout reasonLayout = ViewHolder.get(contentView, R.id.hint_reason_layout);
        RecyclerView card = ViewHolder.get(contentView, R.id.approve_card_recyclerView);
        RecyclerView com = ViewHolder.get(contentView, R.id.approve_com_recyclerView);
        final RecyclerView pic = ViewHolder.get(contentView, R.id.approve_shop_pic_recyclerView);
        TextView card_example = ViewHolder.get(contentView, R.id.approve_card_example);
        TextView com_example=ViewHolder.get(contentView,R.id.approve_com_example);
        EditText shopName = ViewHolder.get(contentView, R.id.approve_shop_name);
        EditText shopCardId = ViewHolder.get(contentView, R.id.approve_card_id);
        EditText comName = ViewHolder.get(contentView, R.id.approve_com_name);
        EditText comID = ViewHolder.get(contentView, R.id.approve_com_id);
        EditText comFoodID = ViewHolder.get(contentView, R.id.approve_food_id);
        LinearLayout foodLayout = ViewHolder.get(contentView, R.id.food_layout);
        if (StrUtils.isNotNull(approveInfo.getStatus())) {
            if (approveInfo.getStatus().equals("-1")) {//认证失败
                hintLayout.setVisibility(View.VISIBLE);
                hintImg.setImageResource(R.mipmap.img_approve_fail);
                hintText.setText(Html.fromHtml(StrUtils.setHtmlRed("您的店铺", "认证失败", "，请修改")));
                reasonLayout.setVisibility(View.VISIBLE);
                hintReason.setText(approveInfo.getReason());
            } else if (approveInfo.getStatus().equals("0")) {//待審核 但其他信息為null是 也是未提交過認證
                hintLayout.setVisibility(View.VISIBLE);
                hintImg.setImageResource(R.mipmap.img_approve_ing);
                hintText.setText(Html.fromHtml(StrUtils.setHtmlRed("您的店铺正在", "认证中", null)));
                reasonLayout.setVisibility(View.GONE);
                if (StrUtils.isNull(approveInfo.getPerson_name()) || StrUtils.isNull(approveInfo.getCard_id())) {//未提交审核
                    hintLayout.setVisibility(View.GONE);
                }
            } else if (approveInfo.getStatus().equals("1")) {
                butOk.setVisibility(View.GONE);
                hintLayout.setVisibility(View.VISIBLE);
                hintImg.setImageResource(R.mipmap.img_approve_success);
                hintText.setText(Html.fromHtml(StrUtils.setHtmlRed("您的店铺已", "认证成功", null)));
                reasonLayout.setVisibility(View.GONE);
                setClickAble(false, contentView);
                isSuccess = true;
            }
        } else {
            hintLayout.setVisibility(View.GONE);
        }
        if (approveInfo.getFood_circulation_permit_required().equals("FALSE")) {
            foodLayout.setVisibility(View.GONE);
        } else {
            foodLayout.setVisibility(View.VISIBLE);
        }
        card_example.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDelegate.goToActivity(null,ApproveHintActivity.class,0);
            }
        });
        com_example.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDelegate.goToActivity(null,ApproveHint1Activity.class,0);
            }
        });
        setClickAble(true, contentView);
        isSuccess = false;
        if (StrUtils.isNotNull(approveInfo.getPerson_name())) {
            shopName.setText(approveInfo.getPerson_name());
        }
        if (StrUtils.isNotNull(approveInfo.getCard_id())) {
            shopCardId.setText(approveInfo.getCard_id());
        }
        if (StrUtils.isNotNull(approveInfo.getBack_img())) {
            cradList.get(1).url = approveInfo.getBack_img();
        }
        if (StrUtils.isNotNull(approveInfo.getFront_img())) {
            cradList.get(0).url = approveInfo.getFront_img();
        }
        if (StrUtils.isNotNull(approveInfo.getLetter_of_application())) {
            comList.get(2).url = approveInfo.getLetter_of_application();
        }
        if (StrUtils.isNotNull(approveInfo.getCorporation_name())) {
            comName.setText(approveInfo.getCorporation_name());
        }
        if (StrUtils.isNotNull(approveInfo.getBusiness_license_id())) {
            comID.setText(approveInfo.getBusiness_license_id());
        }
        if (StrUtils.isNotNull(approveInfo.getBusiness_scanning())) {
            comList.get(0).url = approveInfo.getBusiness_scanning();
        }
        if (StrUtils.isNotNull(approveInfo.getFood_circulation_permit())) {
            comFoodID.setText(approveInfo.getFood_circulation_permit());
        }
        if (StrUtils.isNotNull(approveInfo.getFood_circulation_permit_scanning())) {
            comList.get(1).url = approveInfo.getFood_circulation_permit_scanning();
        }

        List<String> facade_photo = new ArrayList<>();
        if (null != approveInfo.getFacade_photo()) {
            facade_photo.clear();
            facade_photo.addAll(JSON.parseArray(approveInfo.getFacade_photo(), String.class));
            for (int j = 0; j < facade_photo.size(); j++) {
                picList.get(j).url = facade_photo.get(j);
            }
        }
        cardAdapter = new ImageApproveAdapter(this, cradList, cradList.size(), true);
        card.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(new ImageApproveAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickP = 1;
                clickPItem = position;
                cick(position, cardAdapter, cradList.get(position));
            }
        });
        if (approveInfo.getFood_circulation_permit_required().equals("TRUE")) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            com.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            com.setLayoutManager(layoutManager);
            comList.remove(1);
        }
        comAdapter = new ImageApproveAdapter(this, comList, comList.size(), true);
        com.setAdapter(comAdapter);
        comAdapter.setOnItemClickListener(new ImageApproveAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickP = 2;
                clickPItem = position;
                cick(position, comAdapter, comList.get(position));
            }
        });
        picAdapter = new ImageApproveAdapter(this, picList, picList.size(), true);
        pic.setAdapter(picAdapter);
        picAdapter.setOnItemClickListener(new ImageApproveAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickP = 4;
                clickPItem = position;
                cick(position, picAdapter, picList.get(position));
            }
        });
    }

    /**
     * 图片点击事件
     * @param position
     * @param adapter
     * @param img
     */
    private void cick(final int position, final ImageApproveAdapter adapter, ImageItem img) {
        if (null == img || StrUtils.isNull(img.url)) {
            if (!isSuccess) {//加载图片
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
            }
        } else {
            deteleImg(position, adapter, picList);
        }
    }

    /**
     * 设置控件是否可点
     * @param isClick
     * @param view
     */
    private void setClickAble(boolean isClick, View view) {
        ViewHolder.get(view, R.id.approve_shop_name).setEnabled(isClick);
        ViewHolder.get(view, R.id.approve_card_id).setEnabled(isClick);
        ViewHolder.get(view, R.id.approve_com_name).setEnabled(isClick);
        ViewHolder.get(view, R.id.approve_com_id).setEnabled(isClick);
        ViewHolder.get(view, R.id.approve_food_id).setEnabled(isClick);
    }

    private void initList() {
        if (null != cradList) {
            cradList.clear();
            comList.clear();
            picList.clear();
        }
        cradList = setItemImg(2);
        cradList.get(0).name = "手持身份证正面";
        cradList.get(1).name = "手持身份证反面";
        comList = setItemImg(3);
        comList.get(0).name = "营业执照";
        comList.get(1).name = "食品许可证";
        comList.get(2).name = "认证公函";
        picList = setItemImg(3);
        picList.get(0).name="店铺门头照1";
        picList.get(1).name="店铺门头照2";
        picList.get(2).name="店铺门头照3";
    }

    private List<ImageItem> setItemImg(int num) {
        List<ImageItem> list = new ArrayList<>();
        if (num == 0) {
            return list;
        }
        for (int i = 0; i < num; i++) {
            ImageItem imageItem = new ImageItem();
            if (num == 3) {
                imageItem.name = "店铺门头照";
            }
            list.add(imageItem);
        }
        return list;
    }

    /**
     * 查看  删除
     * @param position
     * @param approveAdapter
     * @param itemList
     */
    private void deteleImg(final int position, final ImageApproveAdapter approveAdapter, List<ImageItem> itemList) {
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
        if (isSuccess) {
            pcPopCibtn.setVisibility(View.GONE);
        } else {
            pcPopCibtn.setVisibility(View.VISIBLE);
        }
        pcPopPibtn.setText("查看");
        pcPopCibtn.setText("更换");
        pcPopPibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开预览
                Intent intentPreview = new Intent(ApproveActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) approveAdapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                alertDialog.dismiss();
            }
        });
        pcPopCibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent = new Intent(ApproveActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_SELECT);
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

    private String get(int itemID) {
        EditText text = ViewHolder.get(contentView, itemID);
        return text.getText().toString().trim();
    }

    /**
     * 提交信息
     */
    private void postApprove() {
        if (StrUtils.isNull(get(R.id.approve_shop_name))) {
            viewDelegate.showErrorHint("请填写店主姓名", 1, null);
            return;
        }
        if (!CardIdUtil.isCardID(get(R.id.approve_card_id))) {
            viewDelegate.showErrorHint("请填写店主身份证号", 1, null);
            return;
        }
        if (StrUtils.isNull(cradList.get(0).url)) {
            viewDelegate.showErrorHint("请上传身份证正面", 1, null);
            return;
        }
        if (StrUtils.isNull(cradList.get(1).url)) {
            viewDelegate.showErrorHint("请上传身份证反面", 1, null);
            return;
        }
        if (StrUtils.isNull(get(R.id.approve_com_name))) {
            viewDelegate.showErrorHint("请填写企业名称", 1, null);
            return;
        }
        if (!CardIdUtil.isApproveNum(get(R.id.approve_com_id))) {
            viewDelegate.showErrorHint("请输入正确的企业注册号", 1, null);
            return;
        }
        if (approveInfo.getFood_circulation_permit_required().equals("TRUE")) {
            if (StrUtils.isNull(get(R.id.approve_food_id))) {
                viewDelegate.showErrorHint("请输入食品流通许可证号码", 1, null);
                return;
            }
            if (StrUtils.isNull(comList.get(0).url)) {
                viewDelegate.showErrorHint("请上传的企业注册证件", 1, null);
                return;
            }
            if (StrUtils.isNull(comList.get(1).url)) {
                viewDelegate.showErrorHint("请上传食品许可证件", 1, null);
                return;
            }
        }else {
            if (StrUtils.isNull(comList.get(0).url)) {
                viewDelegate.showErrorHint("请上传的企业注册证件", 1, null);
                return;
            }
        }
        List<String> pic = new ArrayList<>();
        for (int i = 0; i < picList.size(); i++) {
            if (StrUtils.isNull(picList.get(i).url)) {
                viewDelegate.showErrorHint("请上传三张门头照", 1, null);
                return;
            }
            pic.add(picList.get(i).url);
        }
        requestId = 3;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("person_name", get(R.id.approve_shop_name));
        params.put("card_id", get(R.id.approve_card_id));
        params.put("front_img", cradList.get(0).url);
        params.put("back_img", cradList.get(1).url);
        params.put("corporation_name", get(R.id.approve_com_name));
        params.put("business_license_id", get(R.id.approve_com_id));
        params.put("business_scanning", comList.get(0).url);
        if (approveInfo.getFood_circulation_permit_required().equals("TRUE")) {
            params.put("food_circulation_permit_scanning", comList.get(1).url);
            params.put("food_circulation_permit", get(R.id.approve_food_id));
            if (StrUtils.isNotNull(comList.get(2).url)) {
                params.put("letter_of_application", comList.get(2).url);
            }
        } else {
            if (StrUtils.isNotNull(comList.get(1).url)) {
                params.put("letter_of_application", comList.get(1).url);
            }
        }
        params.put("facade_photo", JSON.toJSONString(pic));
        baseHttp("正在提交...", true, Constants.API_APPROVE_SHOP, params);
    }

    /**
     * 上传图片成功后刷新
     * @param img
     */
    private void refreshPic(ImageItem img) {
        if (null != img) {
            switch (clickP) {
                case 1://身份證
                    if (clickPItem != -1) {
                        cradList.get(clickPItem).url = img.url;
                        cardAdapter.setImages(cradList);
                    }
                    break;
                case 2://營業執照
                    if (clickPItem == 0) {
                        comList.get(0).url = img.url;
                        comAdapter.setImages(comList);
                    }
                    if (clickPItem == 1 && approveInfo.getFood_circulation_permit_required().equals("TRUE")) {
                        comList.get(1).url = img.url;
                        comAdapter.setImages(comList);
                    }
                    if (clickPItem == 1 && approveInfo.getFood_circulation_permit_required().equals("FALSE")) {
                        comList.get(1).url = img.url;
                        comAdapter.setImages(comList);
                    }
                    if (clickPItem == 2) {
                        comList.get(2).url = img.url;
                        comAdapter.setImages(comList);
                    }
                    break;
                case 4://門頭
                    if (clickPItem != -1) {
                        picList.get(clickPItem).url = img.url;
                        picAdapter.setImages(picList);
                    }
                    break;
            }
        }
    }

    /**
     * 上传图片
     * @param imageItem
     */
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
        params.put("picture", file);
        params.put("albname", "ShopFacePhoto");
        baseHttp("正在上传图片...", true, Constants.API_BATCH_PICTURE_UPLOAD, params);
    }

    /**
     * 图片配置信息
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new UILImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(9);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }
}
