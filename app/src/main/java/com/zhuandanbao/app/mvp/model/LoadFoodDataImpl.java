package com.zhuandanbao.app.mvp.model;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.mvp.OnDataLoadListener;
import com.zhuandanbao.app.mvp.callback.MvpHttpCallback;

import okhttp3.Call;
import okhttp3.Response;

public class LoadFoodDataImpl implements ILoadFoodData {

    @Override
    public void getData(String httpUrl, HttpParams params, final OnDataLoadListener onDataLoadListener) {
        OkGo.post(httpUrl)
                .params("mobile", "15123166707")
                .params("logintype", "passlogin")
                .params("password", "654321")
                .execute(new MvpHttpCallback<BaseEntity>(onDataLoadListener) {
                    @Override
                    public void onSuccess(BaseEntity testEntity, Call call, Response response) {
                        if (testEntity.code == 0) {
                            onDataLoadListener.onSuccess(testEntity);
                        } else {
                            onDataLoadListener.onFailed(testEntity.code, testEntity.message);
                        }
                    }
                });
    }

    @Override
    public void postData(String httpUrl, HttpParams params, final OnDataLoadListener onDataLoadListener) {
        OkGo.post(httpUrl)
                .params("mobile", "15123166707")
                .params("logintype", "passlogin")
                .params("password", "654321")
                .execute(new MvpHttpCallback<BaseEntity>(onDataLoadListener) {
                    @Override
                    public void onSuccess(BaseEntity testEntity, Call call, Response response) {
                        if (testEntity.code == 0) {
                            onDataLoadListener.onSuccess(testEntity);
                        } else {
                            onDataLoadListener.onFailed(testEntity.code, testEntity.message);
                        }
                    }
                });
    }
}
