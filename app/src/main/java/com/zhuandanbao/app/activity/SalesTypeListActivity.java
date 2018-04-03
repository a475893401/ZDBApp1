package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SalesTypeEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 售后类型选择
 * Created by BFTECH on 2017/3/15.
 */

public class SalesTypeListActivity extends BaseListViewActivity<ShopD> {

    private List<SalesTypeEntity> list=new ArrayList<>();
    private RvBaseAdapter adapter;
    private SkipInfoEntity skipInfoEntity;
    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            list.clear();
            list.addAll((Collection<? extends SalesTypeEntity>) skipInfoEntity.data);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("售后类型");
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        adapter=new RvBaseAdapter(context, list, R.layout.item_sales_type, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                SalesTypeEntity info= (SalesTypeEntity) item;
                holder.setText(R.id.show_name,info.getName());
                if (position==skipInfoEntity.index){
                    holder.setVisible(R.id.img_is_show,true);
                }else {
                    holder.setVisible(R.id.img_is_show,false);
                }
            }
        });
        initVerticalRecyclerView(adapter,false,false,1,0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                skipInfoEntity.index=position;
                adapter.notifyDataSetChanged();
                Intent intent=new Intent();
                intent.putExtra(Constants.SKIP_INFO_ID,position);
                setResult(6666,intent);
                finish();
            }
        });
    }
}
