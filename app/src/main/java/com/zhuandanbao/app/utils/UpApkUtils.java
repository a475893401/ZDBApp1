package com.zhuandanbao.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.zhuandanbao.app.activity.UpVersionActivity;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.UpVersionDataEntity;


/**
 * Created by BFTECH on 2016/12/2.
 */
public class UpApkUtils {

    public static void showUp(final Context context, final UpVersionDataEntity info, boolean isCancle) {
        final Activity activity = (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("版本更新").setMessage("更新说明：\n" + info.upgrade_desc).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                upDataVersion(activity, info);
            }
        });
        if (isCancle||info.is_upgrade == 1) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        AlertDialog dialog=builder.create();
        if (info.is_upgrade==2){
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Toast.makeText(context, "即将退出应用", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            });
        }
        dialog.show();
    }

    private static void upDataVersion(Activity activity, UpVersionDataEntity info) {
        DownloadManager downloadManager = DownloadService.getDownloadManager();
        downloadManager.setTargetFolder(StorageUtil.getApkFile(info.version+"").getPath());
        MLog.e("apkPath======="+StorageUtil.getApkFile(info.version+"").getPath());
        if (null != downloadManager.getDownloadInfo(info.upgrade_path)) {
            DownloadInfo downloadInfo = downloadManager.getDownloadInfo(info.upgrade_path);
            downloadManager.removeTask(downloadInfo.getUrl());
            if (FileUtil.deleteFile(downloadInfo.getTargetPath())) {
                GetRequest request = OkGo.get(info.upgrade_path);
                downloadManager.addTask(info.upgrade_path, info, request, null);
            }
        } else {
            GetRequest request = OkGo.get(info.upgrade_path);
            downloadManager.addTask(info.upgrade_path, info, request, null);
        }
        Intent intent = new Intent(activity, UpVersionActivity.class);
        intent.putExtra(Constants.APK_INFO, info);
        activity.startActivity(intent);
        activity.finish();
    }
}
