package com.zhuandanbao.app.mvp;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.classic.common.MultipleStatusView;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.mvp.presenter.BasePresenter;
import com.zhuandanbao.app.mvp.view.BaseDelegate;
import com.zhuandanbao.app.mvp.view.IFoodView;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.NetworkUtils;


/**
 * Created by BFTECH on 2017/1/25.
 */

public abstract class BaseHttpFragment<T extends BaseDelegate> extends BaseFragment<T> implements IFoodView, View.OnClickListener {
    private BasePresenter presenter = new BasePresenter(this);
    protected int requestId = 1;//请求数据标识
    protected int loadingId = 2;//show loading样式 0：无 1：Kprogress 2: MultipleStatusView 3:上传图片
    protected static String FRAGMENT_INDEX = "fragment_index";
    protected MultipleStatusView multipleStatusView;
    protected String url;
    protected boolean isPost = true;
    protected HttpParams baseParams;
    protected Toolbar toolbar;
    protected String loadingStr = null;

    @Override
    public void initView() {
        multipleStatusView = (MultipleStatusView) viewDelegate.getRootView().findViewById(R.id.main_multiplestatusview);
    }

    @Override
    public void showLoading() {
        if (null == multipleStatusView) {
            multipleStatusView = (MultipleStatusView) viewDelegate.getRootView().findViewById(R.id.main_multiplestatusview);
        }
        switch (loadingId) {
            case 1:
                MLog.e("========showLoading===========1");
                viewDelegate.showKprogress(loadingStr);
                break;
            case 2:
                viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_LOADING,null);
                break;
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        MLog.e("code=" + code + ";baseEntity" + baseEntity.data);
    }

    @Override
    public void hideLoading() {
        if(loadingId==1){
            viewDelegate.hudDismiss();
        }
        viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_CONTENT,null);
    }

    @Override
    public void showErrorMsg(int code, String errorMsg) {
        if (code==404||code==500){
            multipleStatusView.showEmpty();
            Toast.makeText(context,errorMsg,Toast.LENGTH_LONG).show();
        }else {
            viewDelegate.errorHint(context, code, errorMsg);
        }
    }

    @Override
    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

    }

    @Override
    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

    }

    protected void baseHttp(String loadingStr, boolean isPost, String url, HttpParams params) {
        this.loadingStr = loadingStr;
        this.url = url;
        this.isPost = isPost;
        this.baseParams = params;
        if (!isVisible) {
            return;
        }
        if (null == multipleStatusView) {
            multipleStatusView = (MultipleStatusView) viewDelegate.getRootView().findViewById(R.id.main_multiplestatusview);
        }
        if (!NetworkUtils.isNetworkAvailable(activity)) {
            viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_NO_NETWORK,null);
            onClickMultipleStatusView();
            return;
        }
        if (isPost) {
            presenter.loadPostData(url, requestId, params);
        } else {
            presenter.loadGetData(url, requestId, params);
        }
    }

    protected void onClickMultipleStatusView() {
        if (null == multipleStatusView) {
            MLog.e("multipleStatusView is null");
            return;
        }
        multipleStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewStatus = multipleStatusView.getViewStatus();
                switch (viewStatus) {
                    case MultipleStatusView.STATUS_CONTENT:
                        if (MLog.isLogEnable) {
                            viewDelegate.toast("您点击了重试视图 STATUS_CONTENT");
                        }
                        break;
                    case MultipleStatusView.STATUS_EMPTY:
                        if (MLog.isLogEnable) {
                            viewDelegate.toast("您点击了重试视图 STATUS_EMPTY");
                        }
                        baseHttp(loadingStr, isPost, url, baseParams);
                        break;
                    case MultipleStatusView.STATUS_ERROR:
                        if (MLog.isLogEnable) {
                            viewDelegate.toast("您点击了重试视图 STATUS_ERROR");
                        }
                        break;
                    case MultipleStatusView.STATUS_LOADING:
                        if (MLog.isLogEnable) {
                            viewDelegate.toast("您点击了重试视图 STATUS_LOADING");
                        }
                        break;
                    case MultipleStatusView.STATUS_NO_NETWORK:
                        if (MLog.isLogEnable) {
                            viewDelegate.toast("您点击了重试视图 STATUS_NO_NETWORK");
                        }
                        baseHttp(loadingStr, isPost, url, baseParams);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
