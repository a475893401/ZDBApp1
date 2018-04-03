package com.zhuandanbao.app.mvp.callback;

import com.lzy.okgo.request.BaseRequest;
import com.zhuandanbao.app.mvp.MCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by BFTECH on 2016/12/5.
 */
public abstract class MHttpCallback<T> extends MJsonCallback<T> {

    private MCallback mCallback;

    public MHttpCallback(MCallback mCallback) {
        this.mCallback = mCallback;
    }


    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        mCallback.onBefore(request);
    }

    @Override
    public void onAfter(T t, Exception e) {
        super.onAfter(t, e);
        mCallback.onAfter(t, e);
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        mCallback.onError(call, response, e);
    }
}
