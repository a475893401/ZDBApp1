package com.zhuandanbao.app.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.superlibrary.adapter.BaseViewHolder;
import com.superlibrary.adapter.SuperBaseAdapter;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.ZdbInfoActivity;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.NewsEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.AnimateFirstDisplayListener;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;

import java.util.ArrayList;
import java.util.List;

/**
 * 资讯分类
 * Created by BFTECH on 2017/2/28.
 */

public class NewsListFragment extends BaseListViewFragment<ShopD> {
    private int newTypeId;
    private List<NewsEntity> list = new ArrayList<>();
    private RvBaseAdapter adapter;

    public static NewsListFragment getInstance(int indexFragment) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, indexFragment);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initD() {
        newTypeId = getArguments().getInt(FRAGMENT_INDEX);
        loadingId = 2;
        getNewInfo();
    }

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter = new RvBaseAdapter(context, list, R.layout.item_news_list_layout, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                NewsEntity info = (NewsEntity) item;
                holder.setText(R.id.item_news_title, info.getTitle());
                holder.setText(R.id.item_news_time, info.getSource() + "   " + MUtils.getTo66TimePHP(info.getTime(), "yyyy-MM-dd"));
                ImageView imageView = holder.getView(R.id.item_news_img);
                ImageLoader.getInstance().displayImage(info.getCover(), imageView, MyImage.deployMemory(), new AnimateFirstDisplayListener());
            }
        });
        initVerticalRecyclerView(adapter, true, true, 1, 0);
        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                NewsEntity info = (NewsEntity) item;
                SkipInfoEntity infoEntity=new SkipInfoEntity();
                infoEntity.isPush=false;
                infoEntity.pushId=info.getId();
                viewDelegate.goToActivity(infoEntity, ZdbInfoActivity.class,0);
            }
        });
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            if (page == 1) {
                list.clear();
            }
            List<NewsEntity> itemList = MJsonUtils.jsonToNewsEntity(baseEntity.data);
            list.addAll(itemList);
            completeRefresh(itemList.size(), list.size(), false);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        loadingId = 0;
        getNewInfo();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page = (list.size() / page_size) + 1;
        loadingId = 0;
        getNewInfo();
    }

    /**
     * 獲取新聞信息
     */
    private void getNewInfo() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put("cate_id", newTypeId);
        params.put("page", page);
        params.put("page_size", page_size);
        baseHttp(null, true, Constants.API_ACTION_INFOMATION, params);
    }
}
