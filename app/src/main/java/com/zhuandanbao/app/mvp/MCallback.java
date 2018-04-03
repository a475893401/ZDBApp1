package com.zhuandanbao.app.mvp;

import android.support.annotation.Nullable;

import com.lzy.okgo.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by BFTECH on 2016/12/8.
 */
public interface MCallback {
    /**
     * 无网络
     */
    void noneNetworks();

    /**
     * 有网络
     */
    void haveNetworks();

    /**
     * 请求网络开始前，UI线程
     */
    void onBefore(BaseRequest request);

    /**
     * 对返回数据进行操作的回调， UI线程
     */
    <T> void onSuccess(T t, Call call, Response response);

    /**
     * 缓存成功的回调,UI线程
     */
    <T> void onCacheSuccess(T t, Call call);

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
     */
    void onError(Call call, Response response, Exception e);

    /**
     * 缓存失败的回调,UI线程
     */
    void onCacheError(Call call, Exception e);

    /**
     * 网络失败结束之前的回调
     */
    void parseError(Call call, Exception e);

    /**
     * 请求网络结束后，UI线程
     */
    <T> void onAfter(@Nullable T t, @Nullable Exception e);

    /**
     * Post执行上传过程中的进度回调，get请求不回调，UI线程
     *
     * @param currentSize  当前上传的字节数
     * @param totalSize    总共需要上传的字节数
     * @param progress     当前上传的进度
     * @param networkSpeed 当前上传的速度 字节/秒
     */
    void upProgress(long currentSize, long totalSize, float progress, long networkSpeed);

    /**
     * 执行下载过程中的进度回调，UI线程
     *
     * @param currentSize  当前下载的字节数
     * @param totalSize    总共需要下载的字节数
     * @param progress     当前下载的进度
     * @param networkSpeed 当前下载的速度 字节/秒
     */
    void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed);
}
