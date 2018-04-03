package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ImagePickerAdapter;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ApproveSuccesD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.OrderInfoEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.SignInfoEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 订单评价
 * Created by BFTECH on 2017/3/20.
 */

public class OrderCommentActivity extends BaseListViewActivity<ApproveSuccesD> {
    private List<OrderItemEntity> list = new ArrayList<>();
    private RvBaseAdapter adapter;
    private View topView;
    private View bottomView;
    private SkipInfoEntity skipInfoEntity;
    private OrderInfoEntity infoEntity;
    private OrderDataEntity dataEntity;
    private SignInfoEntity signInfoEntity;
    private Button button;

    private int composite = 1;
    private float consonant = 5;
    private float manner = 5;
    private float speed = 5;

    private EditText commentOrderContent;
    private RadioButton commentOrderGood;
    private RadioButton commentOrderMid;
    private RadioButton commentOrderBad;

    private SeekBar commentOrderConsonant;
    private SeekBar commentOrderManner;
    private SeekBar commentOrderSpeed;

    @Override
    protected Class<ApproveSuccesD> getDelegateClass() {
        return ApproveSuccesD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            infoEntity = (OrderInfoEntity) skipInfoEntity.data;
            dataEntity = JSON.parseObject(infoEntity.getOrderData(), OrderDataEntity.class);
            signInfoEntity = JSON.parseObject(infoEntity.getSignInfo(), SignInfoEntity.class);
            list.addAll(JSON.parseArray(dataEntity.getOrder_items(), OrderItemEntity.class));
        }
        viewDelegate.setTitle("订单点评");
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        button = viewDelegate.get(R.id.but_ok);
        button.setText("立即点评");
        button.setOnClickListener(this);
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, list, R.layout.item_order_goods, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                OrderItemEntity itemEntity = (OrderItemEntity) item;
                if (StrUtils.isNotNull(itemEntity.getItem_name())) {
                    holder.setText(R.id.item_goods_name, itemEntity.getItem_name());
                    holder.setText(R.id.item_goods_dsc, itemEntity.getItem_remarks());
                } else {
                    holder.setText(R.id.item_goods_name, itemEntity.getItem_remarks());
                }
                holder.setText(R.id.item_goods_num, "x " + itemEntity.getItem_total());

                BGABanner bgaBanner=holder.getView(R.id.item_img);
                if (StrUtils.isNotNull(itemEntity.getItem_img())){
                    final List<String> imgList=JSON.parseArray(itemEntity.getItem_img(),String.class);
                    if (imgList.size()>0){
                        bgaBanner.setAdapter(new BGABanner.Adapter() {
                            @Override
                            public void fillBannerItem(BGABanner banner, View itemView, Object model, final int position) {
                                String picpath = (String) model;
                                ImageLoader.getInstance().displayImage(picpath, (ImageView) itemView, MyImage.deployMemoryNoLoading());
                                itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //打开预览
                                        Intent intentPreview = new Intent(activity, ImagePreviewDelActivity.class);
                                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>)  MUtils.itemToString(imgList));
                                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                                        startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                                    }
                                });
                            }
                        });
                        bgaBanner.setData(imgList, null);
                    }
                }
            }
        });
        topView = LayoutInflater.from(context).inflate(R.layout.activity_comment_top, null);
        bottomView = LayoutInflater.from(context).inflate(R.layout.activity_comment_bottom, null);
        adapter.addHeaderView(topView);
        adapter.addFooterView(bottomView);
        initVerticalRecyclerView(adapter, false, false, 1, 0);
        init();
    }

    private void init() {
        TextView orderSn = ViewHolder.get(topView, R.id.order_sn);
        TextView jiedan = ViewHolder.get(topView, R.id.order_jiedan);
        TextView orderAmount = ViewHolder.get(topView, R.id.order_amount);
        orderSn.setText(Html.fromHtml(StrUtils.setHtml999(null, "订单编号：", dataEntity.getOrder_sn())));
        jiedan.setText(Html.fromHtml(StrUtils.setHtml999(null, "接单店铺：", dataEntity.getReceive_shop_name())));
        jiedan.setOnClickListener(this);
        orderAmount.setText(Html.fromHtml(StrUtils.setHtml999(null, "交易金额：", "￥ " + dataEntity.getOrder_amount())));
        LinearLayout signLayout = ViewHolder.get(bottomView, R.id.sign_layout);
        TextView signName = ViewHolder.get(bottomView, R.id.order_info_sign_info);
        RecyclerView recyclerView = ViewHolder.get(bottomView, R.id.sign_recyclerView);
        signLayout.setVisibility(View.VISIBLE);
        signName.setText(signInfoEntity.getSign_user() + " | " + signInfoEntity.getSign_time_format());
        if (StrUtils.isNotNull(signInfoEntity.getSign_voucher())) {
            List<ImageItem> items = new ArrayList<>();
            List<String> signList = JSON.parseArray(signInfoEntity.getSign_voucher(), String.class);
            if (signList.size() > 0) {
                for (String s : signList) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.url = s;
                    items.add(imageItem);
                }
            }
            final ImagePickerAdapter adapter = new ImagePickerAdapter(context, items, items.size(), true);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intentPreview = new Intent(context, ImagePreviewDelActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                    startActivityForResult(intentPreview, Constants.REQUEST_CODE_PREVIEW);
                }
            });
        }
        commentOrderGood = ViewHolder.get(bottomView, R.id.comment_good);
        commentOrderMid = ViewHolder.get(bottomView, R.id.comment_mid);
        commentOrderBad = ViewHolder.get(bottomView, R.id.comment_bad);
        commentOrderConsonant = ViewHolder.get(bottomView, R.id.comment_goods_consonant);
        commentOrderManner = ViewHolder.get(bottomView, R.id.comment_goods_manner);
        commentOrderSpeed = ViewHolder.get(bottomView, R.id.comment_goods_speed);
        commentOrderContent = ViewHolder.get(bottomView, R.id.comment_content);
        commentOrderConsonant.setMax(5);
        commentOrderConsonant.setProgress(5);
        commentOrderManner.setMax(5);
        commentOrderManner.setProgress(5);
        commentOrderSpeed.setMax(5);
        commentOrderSpeed.setProgress(5);
        commentOrderGood.setOnClickListener(this);
        commentOrderMid.setOnClickListener(this);
        commentOrderBad.setOnClickListener(this);
        initOnclick();
    }

    private void initOnclick() {
        commentOrderConsonant.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                consonant = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        commentOrderManner.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                manner = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        commentOrderSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            viewDelegate.showSuccessHint("点评成功", new KProgressDismissClickLister() {
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
        if (view.getId() == R.id.but_ok) {
            if (StrUtils.isNull(commentOrderContent.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入点评内容", 1, null);
                return;
            }
            requestId = 1;
            loadingId = 1;
            HttpParams params = new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("orderSn",dataEntity.getOrder_sn());
            params.put("composite_score", composite);
            params.put("delivery_score", speed);
            params.put("service_score", manner);
            params.put("goods_score", consonant);
            params.put("content", commentOrderContent.getText().toString().trim());
            baseHttp("正在提交...", true, Constants.API_ORDER_COMMENT, params);
        }
        if (view.getId() == R.id.order_jiedan) {
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            skipInfoEntity.pushId = dataEntity.getReceive_sid();
            viewDelegate.goToActivity(skipInfoEntity, ShopDetailsActivity.class, 0);
        }
        if (view.getId() == R.id.comment_good) {
            composite = 1;
            commentOrderGood.setChecked(true);
            commentOrderMid.setChecked(false);
            commentOrderBad.setChecked(false);
        }
        if (view.getId() == R.id.comment_mid) {
            composite = 0;
            commentOrderGood.setChecked(false);
            commentOrderMid.setChecked(true);
            commentOrderBad.setChecked(false);
        }
        if (view.getId() == R.id.comment_bad) {
            composite = -1;
            commentOrderGood.setChecked(false);
            commentOrderMid.setChecked(false);
            commentOrderBad.setChecked(true);
        }
    }
}
