package com.zhuandanbao.app.mvp.view;


import com.zhuandanbao.app.mvp.model.BaseEntity;

/**
 * Created by 王松 on 2016/10/8.
 */

public interface IFoodView {
    void showLoading();

    void hideLoading();

    void initData(int code, BaseEntity baseEntity);

    void showErrorMsg(int code, String errorMsg);

    void upProgress(long currentSize, long totalSize, float progress, long networkSpeed);

    void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed);
}
