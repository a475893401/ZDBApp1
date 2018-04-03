package com.zhuandanbao.app.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ShopDetailsItemD;
import com.zhuandanbao.app.entity.DataEntity;
import com.zhuandanbao.app.entity.ShopDetailsEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;

/**
 * 店鋪詳情基本信息
 * Created by BFTECH on 2017/3/9.
 */
public class ShopDetailsFragment  extends BaseListViewFragment<ShopDetailsItemD> {
    private ShopDetailsEntity detailsEntity;
    private String jsonData;

    public static ShopDetailsFragment getInstance(String orderSn) {
        ShopDetailsFragment fragment = new ShopDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_INDEX, orderSn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initD() {

    }

    @Override
    protected Class<ShopDetailsItemD> getDelegateClass() {
        return ShopDetailsItemD.class;
    }

    @Override
    public void initView() {
        super.initView();
        jsonData=getArguments().getString(FRAGMENT_INDEX);
        detailsEntity= MJsonUtils.jsonToShopDetailsEntity(jsonData);

        TextView shopRzName = viewDelegate.get(R.id.shop_rz_name);
        TextView shopCxName = viewDelegate.get(R.id.shop_cx_name);
        LinearLayout shopRzImg = viewDelegate.get(R.id.bill_order_shop_list_item_approve);
        ImageView shopCxImg = viewDelegate.get(R.id.shop_list_item_good_faith);
        LinearLayout shopXyLayout = viewDelegate.get(R.id.shop_xy_layout);
        TextView shopAddress = viewDelegate.get(R.id.shop_address);
        TextView shopJianJie = viewDelegate.get(R.id.shop_jianjie);
        if ("0".equals(detailsEntity.getIs_audit())) {
            shopRzImg.setVisibility(View.GONE);
            shopRzName.setText("未认证");
        } else {
            shopRzImg.setVisibility(View.VISIBLE);
        }
        if ("0".equals(detailsEntity.getAssure_credit())) {
            shopCxName.setText("未加入");
            shopCxImg.setImageResource(R.mipmap.img_no_add_honest);
        } else if ("1".equals(detailsEntity.getAssure_credit())) {
            shopCxImg.setImageResource(R.mipmap.img_yes_add_honest);
            shopCxName.setText("已加入");
        }
        if (!TextUtils.isEmpty(detailsEntity.getCredit())) {
            DataEntity imageGradeMap = MUtils.showCountShopGrade(Long.parseLong(detailsEntity.getCredit()));
            ImageView spGradeImage;
            shopXyLayout.removeAllViews();
            switch (imageGradeMap.gradeColor) {
                case 1: // 红心
                    for (int i = 0; i < imageGradeMap.grade; i++) {
                        spGradeImage = new ImageView(getActivity());
                        spGradeImage.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        spGradeImage.setImageResource(R.mipmap.shop_grade_one);
                        shopXyLayout.addView(spGradeImage);
                    }
                    break;
                case 2:
                    for (int i = 0; i < imageGradeMap.grade; i++) {
                        spGradeImage = new ImageView(getActivity());
                        spGradeImage.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        spGradeImage.setImageResource(R.mipmap.shop_grade_two);
                        shopXyLayout.addView(spGradeImage);
                    }
                    break;
                case 3:
                    for (int i = 0; i < imageGradeMap.grade; i++) {
                        spGradeImage = new ImageView(getActivity());
                        spGradeImage.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        spGradeImage.setImageResource(R.mipmap.shop_grade_three);
                        shopXyLayout.addView(spGradeImage);
                    }
                    break;
                case 4:
                    for (int i = 0; i < imageGradeMap.grade; i++) {
                        spGradeImage = new ImageView(getActivity());
                        spGradeImage.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        spGradeImage.setImageResource(R.mipmap.shop_grade_four);
                        shopXyLayout.addView(spGradeImage);
                    }
                    break;
            }
        }
        shopAddress.setText(detailsEntity.getAreainfo()+"  "+detailsEntity.getAddress());
        shopJianJie.setText(detailsEntity.getIntroduce());
    }
}
