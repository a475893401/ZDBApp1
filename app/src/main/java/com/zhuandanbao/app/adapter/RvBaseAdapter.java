package com.zhuandanbao.app.adapter;

import android.content.Context;

import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;

/**
 * Created by BFTECH on 2017/1/25.
 */

public class RvBaseAdapter<T> extends SuperBaseAdapter<T> {
    private int layoutId=-1;
    private RefreshAdapterImpl refreshAdapter;

    public RvBaseAdapter(Context context, List<T> data,int layoutId,RefreshAdapterImpl refreshAdapter) {
        super(context, data);
        this.layoutId=layoutId;
        this.refreshAdapter=refreshAdapter;
    }

    @Override
    protected void convert(BaseViewHolder holder, T item, int position) {
        refreshAdapter.convertImp(holder,item,position);
    }

    @Override
    protected int getItemViewLayoutId(int position, T item) {
        return layoutId;
    }
    public interface RefreshAdapterImpl<T>{
        void convertImp(BaseViewHolder holder, T item, int position);
    }

}
