package com.zhuandanbao.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.entity.WorkOrderTypeEntity;

import java.util.List;

/**
 * Created by BFTECH on 2016/5/26.
 */
public class WorkSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<WorkOrderTypeEntity> list;
    private SpItem spItem;

    public WorkSpinnerAdapter(Context context, List<WorkOrderTypeEntity> list) {
        this.context = context;
        this.list = list;
    }

    public TextView getSpItemView() {
        return spItem.name;
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            spItem = new SpItem();
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, null);
            spItem.name = (TextView) convertView.findViewById(R.id.sp_item);
            convertView.setTag(spItem);
        } else {
            spItem = (SpItem) convertView.getTag();
        }
        spItem.name.setTextSize(16);
        spItem.name.setText(list.get(position).getName());
        spItem.name.setTag(list.get(position).getId());
        return convertView;
    }

    class SpItem {
        TextView name;
    }
}
