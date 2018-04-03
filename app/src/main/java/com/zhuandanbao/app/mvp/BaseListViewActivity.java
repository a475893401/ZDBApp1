package com.zhuandanbao.app.mvp;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.classic.common.MultipleStatusView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.superlibrary.recycleview.ProgressStyle;
import com.superlibrary.recycleview.SuperRecyclerView;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.mvp.view.BaseDelegate;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.MUtils;

import java.util.List;

import app.zhuandanbao.com.reslib.widget.listview.LJListView;


/**
 * Created by BFTECH on 2017/1/25.
 */

public abstract class BaseListViewActivity<T extends BaseDelegate> extends BaseHttpActivity<T> implements SuperRecyclerView.LoadingListener, LJListView.IXListViewListener {
    protected SuperRecyclerView superRecyclerView;
    protected int page = 1;
    protected int page_size = 10;
    protected boolean isRefresh;
    private RvBaseAdapter baseAdapter;

    protected LJListView baseListview;
    protected NBaseAdapter nBaseAdapter;

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
            new Throwable("SuperRecyclerView is null");
            return;
        }
        baseAdapter = adapter;
        if (mobe == 1) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            superRecyclerView.setLayoutManager(layoutManager);
        } else if (mobe == 2) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            superRecyclerView.setLayoutManager(layoutManager);
        } else if (mobe == 3) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, num);
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


    protected <T> NBaseAdapter setBaseAdapter(List<T> listData) {
        NBaseAdapter baseAdapter = new NBaseAdapter(listData);
        return baseAdapter;
    }

    /**
     * c初始化ljlistview
     * @param isRefresh
     * @param iSLoadMore
     */
    protected void setBaseListview(boolean isRefresh, boolean iSLoadMore) {
        if (null != baseListview) {
            baseListview.setXListViewListener(this);
            baseListview.setPullRefreshEnable(isRefresh);
            baseListview.setPullLoadEnable(iSLoadMore, "");
            baseListview.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        }
    }

    /**
     * ljlistview
     * @param count
     * @param isShowCount
     */
    protected void isLoadMore(int count, boolean isShowCount) {
        if (count == 0) {
            multipleStatusView.showEmpty();
            baseListview.setVisibility(View.GONE);
        } else {
            multipleStatusView.showContent();
            baseListview.setVisibility(View.VISIBLE);
        }
        if (count % page_size == 0) {//加载更多
            baseListview.setPullLoadEnable(true, "");
        } else {//隐藏加载更多
            if (isShowCount) {
                baseListview.setPullLoadEnable(false, "共 " + count + " 条数据");
            } else {
                baseListview.setPullLoadEnable(false, "");
            }
        }
        LJcompleteRefresh();
        baseAdapter.notifyDataSetChanged();
    }

    /**
     * 完成刷新 ljlistview
     */
    protected void LJcompleteRefresh() {
        if (null != baseListview) {
            baseListview.stopLoadMore();
            baseListview.stopRefresh();
            baseListview.setRefreshTime(MUtils.getTo66TimePHP(System.currentTimeMillis() + "", "yyyy-MM-dd HH:mm"));
        }
    }

    public class NBaseAdapter<T extends Object> extends BaseAdapter {
        private List<T> listData;

        public NBaseAdapter(List<T> listData) {
            this.listData = listData;
        }

        @Override
        public int getCount() {
            return null == listData ? 0 : listData.size();
        }

        @Override
        public Object getItem(int i) {
            return listData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return getItemView(i, view, viewGroup);
        }
    }

    public View getItemView(int i, View view, ViewGroup viewGroup) {
        return view;
    }

    @Override
    public void onRefresh() {
        page = 1;
        isRefresh = true;
        loadingId = 0;
    }

    @Override
    public void onLoadMore() {
        loadingId = 0;
        isRefresh = false;
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
        if (null == superRecyclerView) {
            return;
        }
        if (count == 0) {
            viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_EMPTY,null);
            onClickMultipleStatusView();
        }
        if (isRefresh) {
            superRecyclerView.completeRefresh();
            baseAdapter.notifyDataSetChanged();
        } else {
            superRecyclerView.completeLoadMore();
        }
        if (count % page_size == 0) {//整除  加载更多
            superRecyclerView.setLoadMoreEnabled(true);
        } else {
            superRecyclerView.setLoadMoreEnabled(false);
            superRecyclerView.setNoMore(true);
        }
        if (count > 0 && itemCount == 0) {
            superRecyclerView.setLoadMoreEnabled(false);
            superRecyclerView.setNoMore(true);
        }
    }

    /**
     * 无数据时需要去添加
     * @param count
     */
    protected void completeRefreshBut(int count,String emptyStr) {
        if (count==0){
            if (null!=multipleStatusView){
                viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_EMPTY,emptyStr);
                MLog.e("getViewStatus="+multipleStatusView.getViewStatus()+";STATUS_EMPTY="+MultipleStatusView.STATUS_EMPTY);
                onClickMultipleStatusView(true);
            }
        }
        superRecyclerView.completeRefresh();
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onlJLoadMore() {

    }

    @Override
    public void onlJRefresh() {

    }
}
