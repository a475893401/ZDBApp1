package com.zhuandanbao.app.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.mvp.OnDataLoadListener;
import com.zhuandanbao.app.mvp.callback.MvpHttpCallback;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.mvp.model.ILoadFoodData;
import com.zhuandanbao.app.mvp.view.IFoodView;

import okhttp3.Call;
import okhttp3.Response;

public class BasePresenter implements ILoadFoodData {
    private IFoodView iFoodView;

    public BasePresenter(IFoodView iFoodView) {
        this.iFoodView = iFoodView;
    }

    public void loadPostData(String url, final int code, HttpParams params) {
        iFoodView.showLoading();
        postData(url, params, new OnDataLoadListener() {
            @Override
            public void onSuccess(BaseEntity baseEntity) {
                iFoodView.hideLoading();
                iFoodView.initData(code, baseEntity);
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                iFoodView.hideLoading();
                iFoodView.showErrorMsg(code, errorMsg);
            }

            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                iFoodView.upProgress(currentSize,totalSize,progress,networkSpeed);
            }

            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                iFoodView.downloadProgress(currentSize,totalSize,progress,networkSpeed);
            }
        });
    }

    public void loadGetData(String url, final int code, HttpParams params) {
        iFoodView.showLoading();
        getData(url, params, new OnDataLoadListener() {
            @Override
            public void onSuccess(BaseEntity baseEntity) {
                iFoodView.hideLoading();
                iFoodView.initData(code, baseEntity);
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                iFoodView.hideLoading();
                iFoodView.showErrorMsg(code, errorMsg);
            }

            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

            }

            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

            }
        });
    }

    @Override
    public void getData(String httpUrl, HttpParams params, final OnDataLoadListener onDataLoadListener) {
        OkGo.get(httpUrl)
                .tag(httpUrl)
                .params(params)
                .execute(new MvpHttpCallback<BaseEntity>(onDataLoadListener) {
                    @Override
                    public void onSuccess(BaseEntity baseEntity, Call call, Response response) {
                        if (baseEntity.code == 0) {
                            onDataLoadListener.onSuccess(baseEntity);
                        } else {
                            onDataLoadListener.onFailed(baseEntity.code, baseEntity.message);
                        }
                    }
                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);
                        onDataLoadListener.upProgress(currentSize,totalSize,progress,networkSpeed);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        onDataLoadListener.downloadProgress(currentSize,totalSize,progress,networkSpeed);
                    }
                });
    }

    @Override
    public void postData(String httpUrl, HttpParams params, final OnDataLoadListener onDataLoadListener) {
        OkGo.post(httpUrl)
                .tag(httpUrl)
                .params(params)
                .execute(new MvpHttpCallback<BaseEntity>(onDataLoadListener) {
                    @Override
                    public void onSuccess(BaseEntity baseEntity, Call call, Response response) {
                        if (baseEntity.code == 0) {
                            onDataLoadListener.onSuccess(baseEntity);
                        } else {
                            onDataLoadListener.onFailed(baseEntity.code, baseEntity.message);
                        }
                    }
                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);
                        onDataLoadListener.upProgress(currentSize,totalSize,progress,networkSpeed);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        onDataLoadListener.downloadProgress(currentSize,totalSize,progress,networkSpeed);
                    }
                });
    }


}
