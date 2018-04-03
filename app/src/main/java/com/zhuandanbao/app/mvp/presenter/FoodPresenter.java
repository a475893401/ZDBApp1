package com.zhuandanbao.app.mvp.presenter;

import android.os.Handler;

import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.mvp.OnDataLoadListener;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.mvp.model.ILoadFoodData;
import com.zhuandanbao.app.mvp.model.LoadFoodDataImpl;
import com.zhuandanbao.app.mvp.view.IFoodView;


public class FoodPresenter {
    private IFoodView iFoodView;
    private ILoadFoodData iLoadFoodData;
    private Handler mHandler = new Handler();

    public FoodPresenter(IFoodView iFoodView) {
        this.iFoodView = iFoodView;
        iLoadFoodData = new LoadFoodDataImpl();
    }

    public void loadData() {
        iFoodView.showLoading();
        iLoadFoodData.postData(Constants.BASE_HTTP_API,null, new OnDataLoadListener() {
            @Override
            public void onSuccess(BaseEntity baseEntity) {
                iFoodView.hideLoading();
                iFoodView.initData(0,baseEntity);
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                iFoodView.hideLoading();
                iFoodView.showErrorMsg(code,errorMsg);
            }

            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

            }

            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

            }
        });
    }
}
