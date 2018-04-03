package com.zhuandanbao.app.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

import static com.zhuandanbao.app.utils.AppUtil.sApp;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/12/16 上午10:48
 * 描述:应用升级工具类
 */
public class BGAUpgradeUtil {
    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";

    private BGAUpgradeUtil() {
    }
    /**
     * 安装 apk 文件
     *
     * @param apkFile
     */
    public static void installApk(File apkFile) {
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.M) {
            MLog.e("============================M");
            installApkIntent.setDataAndType(FileProvider.getUriForFile(sApp, PermissionUtil.getFileProviderAuthority(), apkFile), MIME_TYPE_APK);
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            MLog.e("======M======================");
            installApkIntent.setDataAndType(Uri.fromFile(apkFile), MIME_TYPE_APK);
        }
        if (sApp.getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            sApp.startActivity(installApkIntent);
        }
    }

    /**
     * 删除之前升级时下载的老的 apk 文件
     */
    public static void deleteOldApk() {
        File apkDir = StorageUtil.getApkFileDir();
        if (apkDir == null || apkDir.listFiles() == null || apkDir.listFiles().length == 0) {
            return;
        }
        // 删除文件
        StorageUtil.deleteFile(apkDir);
    }
}
