package com.zhuandanbao.app.mvp.model;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.mvp.OnDataLoadListener;

public interface ILoadFoodData {
    void getData(String httpUrl, HttpParams params, OnDataLoadListener onDataLoadListener);

    void postData(String httpUrl, HttpParams params, OnDataLoadListener onDataLoadListener);
}
