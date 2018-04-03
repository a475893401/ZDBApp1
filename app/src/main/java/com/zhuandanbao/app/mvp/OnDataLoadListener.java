package com.zhuandanbao.app.mvp;


import com.zhuandanbao.app.mvp.model.BaseEntity;

/**
 * Created by 王松 on 2016/10/8.
 */

public interface OnDataLoadListener {
    void onSuccess(BaseEntity baseEntity);
    void onFailed(int code, String errorMsg);
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
