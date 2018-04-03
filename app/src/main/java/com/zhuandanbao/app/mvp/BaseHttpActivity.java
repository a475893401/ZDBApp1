package com.zhuandanbao.app.mvp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.classic.common.MultipleStatusView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.mvp.presenter.BasePresenter;
import com.zhuandanbao.app.mvp.view.BaseDelegate;
import com.zhuandanbao.app.mvp.view.IFoodView;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.NetworkUtils;
import com.zhuandanbao.app.utils.StrUtils;


/**
 * Created by BFTECH on 2017/1/18.
 */

public abstract class BaseHttpActivity<T extends BaseDelegate> extends BaseActivity<T> implements IFoodView {
    private BasePresenter presenter = new BasePresenter(this);
    protected int requestId = 1;//请求数据标识
    protected int loadingId = 2;//show loading样式 0：无 1：Kprogress 2: MultipleStatusView
    protected MultipleStatusView multipleStatusView;
    protected String url;
    protected boolean isPost = true;
    protected HttpParams baseParams;
    protected String loadingStr = null;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        multipleStatusView = (MultipleStatusView) viewDelegate.getRootView().findViewById(R.id.main_multiplestatusview);
        multipleStatusView.showContent();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        MLog.e("code=" + code + ";baseEntity" + baseEntity.data);
    }

    @Override
    public void showLoading() {
        switch (loadingId) {
            case 1:
                viewDelegate.showKprogress(loadingStr);
                break;
            case 2:
                viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_LOADING,null);
                break;
        }
    }

    @Override
    public void hideLoading() {
        if(null!=viewDelegate&&loadingId==1){
            viewDelegate.hudDismiss();
        }
        if (null!=multipleStatusView){
            viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_CONTENT,null);
        }
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
        MLog.e("baseHttp");
        this.loadingStr = loadingStr;
        this.url = url;
        this.isPost = isPost;
        this.baseParams = params;
        if (!NetworkUtils.isNetworkAvailable(activity)) {
            viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_NO_NETWORK,null);
            onClickMultipleStatusView();
            return;
        }
        MLog.e("have net");
        if (isPost) {
            MLog.e("isPost");
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

    protected void onClickMultipleStatusView(final boolean isDef) {
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
                        if (isDef){
                            emptyOnclick();
                        }
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

    protected void emptyOnclick(){}
    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (StrUtils.isNotNull(url)) {
            OkGo.getInstance().cancelTag(url);
        }
    }
}
