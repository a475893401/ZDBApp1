package com.zhuandanbao.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.ViewHolder;

import java.util.List;


/**
 * Created by BFTECH on 2016/11/11.
 */
public class ItemAdapter extends MBaseAdapter {

    private Context context;
    private List<OrderItemEntity> list;
    private int index = -1;


    public ItemAdapter(List<OrderItemEntity> list, Context context) {
        super(list, context);
        this.list = list;
        this.context = context;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_order_info_img, null);
        }
        ImageView imageView = ViewHolder.get(view, R.id.img);
        LinearLayout layout = ViewHolder.get(view, R.id.layout);
        ImageView showImg = ViewHolder.get(view, R.id.show_img);
        if (position == index) {
            layout.setBackgroundResource(R.drawable.more_sku_check_shape);
            showImg.setVisibility(View.VISIBLE);
        } else {
            layout.setBackgroundResource(R.drawable.more_sku_no_check_shape);
            showImg.setVisibility(View.GONE);
        }
        if (list.size()==0){
            imageView.setImageResource(R.mipmap.app_defant);
        }
        List<String> itemList= JSON.parseArray(list.get(position).getItem_img(),String.class);
        if (itemList.size()>0) {
            ImageLoader.getInstance().displayImage(itemList.get(0), imageView, MyImage.deployMemory());
        }
        return view;
    }
}
