package com.zhuandanbao.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okserver.listener.DownloadListener;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.UpVersionD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.UpVersionDataEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.utils.ApkUtils;
import com.zhuandanbao.app.utils.BGAUpgradeUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 更新版本
 * Created by BFTECH on 2016/12/2.
 */
public class UpVersionActivity extends BaseHttpActivity<UpVersionD> {
    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private TextView downloadSize;
    private TextView tvProgress;
    private TextView netSpeed;
    private ProgressBar pbProgress;
    private Button download;
    private Button remove;
    private Button restart;
    private MyListener listener;
    private DownloadInfo downloadInfo;
    private UpVersionDataEntity apkInfo;
    private DownloadManager downloadManager;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        viewDelegate.setTitle("版本更新");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                initView();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        }
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initView();
            } else {
                showToast("文件读写权限被禁止，无法下载，请手动设置权限！");
            }
        }
    }

    public void showToast(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
    }

    protected void initView() {
        apkInfo = (UpVersionDataEntity) getIntent().getSerializableExtra(Constants.APK_INFO);
        downloadManager = DownloadService.getDownloadManager();
        TextView name = viewDelegate.get(R.id.name);
        downloadSize = viewDelegate.get(R.id.downloadSize);
        tvProgress = viewDelegate.get(R.id.tvProgress);
        netSpeed = viewDelegate.get(R.id.netSpeed);
        pbProgress = viewDelegate.get(R.id.pbProgress);
        download = viewDelegate.get(R.id.start);
        remove = viewDelegate.get(R.id.remove);
        restart = viewDelegate.get(R.id.restart);
        name.setText(apkInfo.upgrade_desc);
        download.setOnClickListener(this);
        remove.setOnClickListener(this);
        restart.setOnClickListener(this);
        listener = new MyListener();
        downloadInfo = downloadManager.getDownloadInfo(apkInfo.upgrade_path);
        if (downloadInfo != null) {
            //如果任务存在，把任务的监听换成当前页面需要的监听
            downloadInfo.setListener(listener);
            //需要第一次手动刷一次，因为任务可能处于下载完成，暂停，等待状态，此时是不会回调进度方法的
            refreshUi(downloadInfo);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (downloadInfo != null) refreshUi(downloadInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadInfo != null) downloadInfo.removeListener();
    }

    @Override
    protected Class<UpVersionD> getDelegateClass() {
        return UpVersionD.class;
    }

    @Override
    public void onClick(View v) {
        //每次点击的时候，需要更新当前对象
        downloadInfo = downloadManager.getDownloadInfo(apkInfo.upgrade_path);
        if (v.getId() == download.getId()) {
            if (downloadInfo == null) {
                downloadManager.addTask(apkInfo.upgrade_path, downloadInfo.getRequest(), listener);
                return;
            }
            switch (downloadInfo.getState()) {
                case DownloadManager.PAUSE:
                case DownloadManager.NONE:
                case DownloadManager.ERROR:
                    downloadManager.addTask(downloadInfo.getUrl(), downloadInfo.getRequest(), listener);
                    break;
                case DownloadManager.DOWNLOADING:
                    downloadManager.pauseTask(downloadInfo.getUrl());
                    break;
                case DownloadManager.FINISH:
                    if (ApkUtils.isAvailable(this, new File(downloadInfo.getTargetPath()))) {
                        ApkUtils.uninstall(this, ApkUtils.getPackageName(this, downloadInfo.getTargetPath()));
                    } else {
//                        ApkUtils.installApk(this, downloadInfo.getTargetPath());
                        BGAUpgradeUtil.installApk(new File(downloadInfo.getTargetPath()));
                    }
                    break;
            }
        } else if (v.getId() == remove.getId()) {
            if (downloadInfo == null) {
                Toast.makeText(this, "请先下载任务", Toast.LENGTH_SHORT).show();
                return;
            }
            downloadManager.removeTask(downloadInfo.getUrl());
            downloadSize.setText("--M/--M");
            netSpeed.setText("---/s");
            tvProgress.setText("--.--%");
            pbProgress.setProgress(0);
            download.setText("下载");
        } else if (v.getId() == restart.getId()) {
            if (downloadInfo == null) {
                Toast.makeText(this, "请先下载任务", Toast.LENGTH_SHORT).show();
                return;
            }
            downloadManager.restartTask(downloadInfo.getUrl());
        }
    }

    private class MyListener extends DownloadListener {

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            refreshUi(downloadInfo);
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            System.out.println("onFinish");
            Toast.makeText(UpVersionActivity.this, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_LONG).show();
//            ApkUtils.installApk(UpVersionActivity.this, downloadInfo.getTargetPath());
            BGAUpgradeUtil.installApk(new File(downloadInfo.getTargetPath()));
            finish();
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            System.out.println("onError=" + errorMsg);
            downloadManager.restartTask(downloadInfo.getUrl());
        }
    }

    private void refreshUi(DownloadInfo downloadInfo) {
        String downloadLength = Formatter.formatFileSize(UpVersionActivity.this, downloadInfo.getDownloadLength());
        String totalLength = Formatter.formatFileSize(UpVersionActivity.this, downloadInfo.getTotalLength());
        downloadSize.setText(downloadLength + "/" + totalLength);
        String networkSpeed = Formatter.formatFileSize(UpVersionActivity.this, downloadInfo.getNetworkSpeed());
        netSpeed.setText(networkSpeed + "/s");
        tvProgress.setText((Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100) + "%");
        pbProgress.setMax((int) downloadInfo.getTotalLength());
        pbProgress.setProgress((int) downloadInfo.getDownloadLength());
        switch (downloadInfo.getState()) {
            case DownloadManager.NONE:
                download.setText("下载");
                break;
            case DownloadManager.DOWNLOADING:
                download.setText("暂停");
                break;
            case DownloadManager.PAUSE:
                download.setText("继续");
                break;
            case DownloadManager.WAITING:
                download.setText("等待");
                break;
            case DownloadManager.ERROR:
                download.setText("出错");
                break;
            case DownloadManager.FINISH:
                if (ApkUtils.isAvailable(UpVersionActivity.this, new File(downloadInfo.getTargetPath()))) {
                    download.setText("卸载");
                } else {
                    download.setText("安装");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            exitBy2Click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
            // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            this.getApplication().onTerminate();
            finish();
        }
    }
}
