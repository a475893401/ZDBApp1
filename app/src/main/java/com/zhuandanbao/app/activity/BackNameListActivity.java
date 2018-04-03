package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ApproveSuccesD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.BackNameEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MyImage;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择银行卡
 * Created by BFTECH on 2017/3/14.
 */

public class BackNameListActivity extends BaseListViewActivity<ApproveSuccesD> {

    private List<BackNameEntity> list=new ArrayList<>();
    private RvBaseAdapter adapter;
    private Button button;
    private SkipInfoEntity skipInfoEntity;
    private int index=-1;
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
        viewDelegate.setTitle("选择银行卡");
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        superRecyclerView=viewDelegate.get(R.id.recycler_view);
        button=viewDelegate.get(R.id.but_ok);
        button.setText("确认");
        button.setOnClickListener(this);
        adapter=new RvBaseAdapter(context, list, R.layout.item_back_name_list, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, final int position) {
                final BackNameEntity info= (BackNameEntity) item;
                ImageView img=holder.getView(R.id.img);
                ImageLoader.getInstance().displayImage(info.getLogo(),img, MyImage.deployMemoryNoLoading());
                holder.setText(R.id.name,info.getName());
                final CheckBox checkBox=holder.getView(R.id.checkBox);
                checkBox.setChecked(info.isCheck());
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean b=info.isCheck();
                        setCheck(false);
                        info.setCheck(!b);
                        if (!b){
                            index=position;
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        initVerticalRecyclerView(adapter,false,false,1,0);
        loadingId=2;
        getBackList();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            list.clear();
            list.addAll(MJsonUtils.jsonToBackNameEntity(baseEntity.data,skipInfoEntity.pushId));
            superRecyclerView.completeRefresh();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.but_ok){
            if (index==-1){
                viewDelegate.showErrorHint("请先选择银行卡",1,null);
                return;
            }
            BackNameEntity nameEntity=list.get(index);
            Intent intent=new Intent();
            intent.putExtra(Constants.SKIP_INFO_ID,nameEntity);
            setResult(6666,intent);
            finish();
        }
    }

    /**
     * 获取系统支持的银行
     */
    private void getBackList(){
        requestId=1;
        loadingId=0;
        HttpParams params=new HttpParams();
        baseHttp(null,true, Constants.API_GET_FINANCE_BANK_CARD_LISTS,params);
    }


    private void setCheck(boolean isCheck){
        for (BackNameEntity info:list){
            info.setCheck(isCheck);
        }
    }
}
