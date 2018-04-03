package com.zhuandanbao.app.mvp.view;

/**
 * Created by BFTECH on 2016/12/8.
 */
public interface HttpCallback {
    void onSuccess(String data);

    void onError(int code, String message);
}
