package com.zhuandanbao.app.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by BFTECH on 2016/11/11.
 */
public abstract class MBaseAdapter<T> extends BaseAdapter {

    private List<T> list;
    private Context context;

    public MBaseAdapter(List<T> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
