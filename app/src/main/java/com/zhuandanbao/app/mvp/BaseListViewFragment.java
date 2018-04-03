package com.zhuandanbao.app.mvp;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.classic.common.MultipleStatusView;
import com.superlibrary.recycleview.ProgressStyle;
import com.superlibrary.recycleview.SuperRecyclerView;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.mvp.view.BaseDelegate;
import com.zhuandanbao.app.utils.MLog;


/**
 * Created by BFTECH on 2017/1/25.
 */

public abstract class BaseListViewFragment<T extends BaseDelegate> extends BaseHttpFragment<T> implements SuperRecyclerView.LoadingListener {
    protected SuperRecyclerView superRecyclerView;
    protected int page = 1;
    protected int page_size = 10;
    protected boolean isRefresh;
    private RvBaseAdapter baseAdapter;
    protected int fragmentIndex = 0;
    protected String tag = "";
    protected String search_str = "";

    /**
     * 初始化superRecyclerView
     *
     * @param isRefresh 是否刷新
     * @param isMore    是否加载更多
     * @param mobe      排序 1：竖直 2：横向 3：grid
     * @param num
     */
    protected void initVerticalRecyclerView(RvBaseAdapter adapter, boolean isRefresh, boolean isMore, int mobe, int num) {
        if (null == superRecyclerView) {
            if (MLog.isLogEnable) {
                viewDelegate.toast("SuperRecyclerView is null");
            }
            return;
        }
        baseAdapter = adapter;
        if (mobe == 1) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            superRecyclerView.setLayoutManager(layoutManager);
        } else if (mobe == 2) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            superRecyclerView.setLayoutManager(layoutManager);
        } else if (mobe == 3) {
            GridLayoutManager layoutManager = new GridLayoutManager(context, num);
            superRecyclerView.setLayoutManager(layoutManager);
        }
        superRecyclerView.setRefreshEnabled(isRefresh);//可以定制是否开启下拉刷新
        superRecyclerView.setLoadMoreEnabled(isMore);//可以定制是否开启加载更多
        superRecyclerView.setLoadingListener(this);//下拉刷新，上拉加载的监听
        superRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);//下拉刷新的样式
        superRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);//上拉加载的样式
        superRecyclerView.setArrowImageView(R.mipmap.xlistview_arrow);//设置下拉箭头
        superRecyclerView.setAdapter(adapter);
        MLog.e("Height==" + superRecyclerView.getMeasuredHeight());
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        loadingId = 0;
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        loadingId = 0;
    }

    @Override
    public void isLastIndex(int isLastIndex) {

    }

    /**
     * 完成刷新或者加載更多
     *
     * @param itemCount 当前请求的条数
     * @param count     请求的总条数
     * @param isShow
     */
    protected void completeRefresh(int itemCount, int count, boolean isShow) {
        MLog.e("itemCount="+itemCount+";count="+count+";isRefresh="+isRefresh);
        if (null == superRecyclerView) {
            return;
        }
        if (count == 0) {
            viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_EMPTY,null);
            onClickMultipleStatusView();
        }
        if (isRefresh) {
            superRecyclerView.completeRefresh();
        } else {
            superRecyclerView.completeLoadMore();
        }
        baseAdapter.notifyDataSetChanged();
        if (count % page_size == 0) {//整除  加载更多
            superRecyclerView.setLoadMoreEnabled(true);
        } else {
            superRecyclerView.setNoMore(true);
        }
        if (count > 0 && itemCount == 0) {
            superRecyclerView.setNoMore(true);
        }
    }
}
