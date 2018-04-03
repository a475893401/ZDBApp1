package com.zhuandanbao.app.mvp.callback;

import com.lzy.okgo.request.BaseRequest;
import com.zhuandanbao.app.mvp.OnDataLoadListener;

import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by BFTECH on 2017/1/16.
 */

public abstract class MvpHttpCallback<T> extends MJsonCallback<T> {

    private OnDataLoadListener loadListener;

    public MvpHttpCallback(OnDataLoadListener loadListener) {
        this.loadListener = loadListener;
    }
    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);

    }
    @Override
    public void onAfter(T t, Exception e) {
        super.onAfter(t, e);
        if (e instanceof TimeoutException){
            loadListener.onFailed(900,"请检查网络");
        }
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        loadListener.onFailed(404,e.getMessage());
    }
}
