package com.zhuandanbao.app.fragment;

import android.os.Bundle;
import android.view.View;

import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.ShopTouchItemEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.utils.CallPhone;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.StrUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 店鋪详情店铺联系人
 * Created by BFTECH on 2017/3/9.
 */

public class ShopDetailsTouchFragment extends BaseListViewFragment<ShopD> {

    private RvBaseAdapter adapter;
    private List<ShopTouchItemEntity> list = new ArrayList<>();
    private String josnData;

    public static ShopDetailsTouchFragment getInstance(String orderSn) {
        ShopDetailsTouchFragment fragment = new ShopDetailsTouchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_INDEX, orderSn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initD() {

    }

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    public void initView() {
        super.initView();
        josnData = getArguments().getString(FRAGMENT_INDEX);
        try {
            JSONObject object = new JSONObject(josnData);
            ShopInfoItemEntity shopInfo=MJsonUtils.josnToShopInfoItemEntity(object.getString("shopInfo"));
            list.addAll(MJsonUtils.jsonToShopTouchItemEntity(object.optString("contacts"),shopInfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, list, R.layout.item_touch_layout, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                final ShopTouchItemEntity info = (ShopTouchItemEntity) item;
                holder.setText(R.id.name, info.getUser());
                holder.setText(R.id.mobile, info.getMobile());
                holder.setText(R.id.position, "【 " + info.getPosition() + " 】");
                if (position == 0) {
                    holder.setImageResource(R.id.img, R.mipmap.shopkeeper_img);
                } else {
                    holder.setImageResource(R.id.img, R.mipmap.salesclerk_img);
                }
                if (StrUtils.isNotNull(info.getPhone())) {
                    holder.setVisible(R.id.phone_layout, true);
                    holder.setText(R.id.phone, info.getPhone());
                    if (StrUtils.isMobileNum(info.getPhone())) {
                        holder.setImageResource(R.id.mobile_img, R.mipmap.phone);
                    } else {
                        holder.setImageResource(R.id.mobile_img, R.mipmap.img_mobile);
                    }
                } else {
                    holder.setVisible(R.id.phone_layout, false);
                }
                holder.setOnClickListener(R.id.mobile, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CallPhone.callPhone(getActivity(), info.getMobile());
                    }
                });
                holder.setOnClickListener(R.id.phone_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CallPhone.callPhone(getActivity(), info.getPhone());
                    }
                });
            }
        });
        initVerticalRecyclerView(adapter, false, false, 1, 0);
    }
}
