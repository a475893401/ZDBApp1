package com.zhuandanbao.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhuandanbao.app.R;

/**
 * Created by BFTECH on 2017/2/17.
 */

public class MyImage {

    //显示图片配置
    public static DisplayImageOptions deploy() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.app_defant)
                .showImageOnLoading(R.mipmap.app_defant)
                .showImageOnFail(R.mipmap.app_defant)
                .cacheInMemory(true).cacheOnDisk(true)
                .delayBeforeLoading(100)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置 图片的解码类型//
                .resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(0)).build();
        return options;
    }

    public static DisplayImageOptions deployMemory() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.app_defant)
                .showImageOnLoading(R.drawable.zdb_loading)
                .showImageOnFail(R.mipmap.app_defant)
                .cacheInMemory(false).cacheOnDisk(true)
                .delayBeforeLoading(100)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置 图片的解码类型//
                .resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(0)).build();
        return options;
    }

    public static DisplayImageOptions deployMemoryNoLoading() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.app_defant)
                .showImageOnLoading(R.mipmap.app_defant)
                .showImageOnFail(R.mipmap.app_defant)
                .cacheInMemory(false).cacheOnDisk(true)
                .delayBeforeLoading(100)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置 图片的解码类型//
                .resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(0)).build();
        return options;
    }

    /**
     * 注意： 作图时最好选择尺寸较大图！
     * 例如，一般屏幕的宽度最大也就是1080，只是魅族有些奇葩会是1152，
     * 如果担心这个问题的话，请美工给图的时候，直接按照1152的宽来就好了。
     *
     * @param context
     * @param view
     * @param drawableResId
     */
    public static void scaleImageRes(final Context context, final ImageView view, int drawableResId) {
        Activity activity = (Activity) context;
        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);

        // 解析将要被处理的图片
        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);

        if (resourceBitmap == null) {
            return;
        }
        // 开始对图片进行拉伸或者缩放
        // 使用图片的缩放比例计算将要放大的图片的高度
        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());
        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这里防止图像的重复创建，避免申请不必要的内存空间
                if (scaledBitmap.isRecycled())
                    //必须返回true
                    return true;

                // 当UI绘制完毕，我们对图片进行处理
                int viewHeight = view.getMeasuredHeight();

                // 计算将要裁剪的图片的顶部以及底部的偏移量
                int offset = (scaledBitmap.getHeight() - viewHeight) / 2;
                MLog.e("offset="+offset);
                if (offset<0){
                    offset=20;
                }
                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
                Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(),
                        scaledBitmap.getHeight() - offset * 2);
                if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收
                    scaledBitmap.recycle();
                    System.gc();
                }

                // 设置图片显示
                view.setImageDrawable(new BitmapDrawable(context.getResources(), finallyBitmap));
                return true;
            }
        });
    }


    /**
     * 注意： 作图时最好选择尺寸较大图！
     * 例如，一般屏幕的宽度最大也就是1080，只是魅族有些奇葩会是1152，
     * 如果担心这个问题的话，请美工给图的时候，直接按照1152的宽来就好了。
     *
     * @param context
     * @param view
     */
    public static void scaleImageBitmap(final Context context, final ImageView view, Bitmap bitmap) {
        // 解析将要被处理的图片
        Bitmap resourceBitmap = bitmap;
        if (resourceBitmap == null) {
            return;
        }
        Activity activity = (Activity) context;
        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);
        // 开始对图片进行拉伸或者缩放
        // 使用图片的缩放比例计算将要放大的图片的高度
        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());
        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这里防止图像的重复创建，避免申请不必要的内存空间
                if (scaledBitmap.isRecycled()) {
                    //必须返回true
                    return true;
                }
                // 当UI绘制完毕，我们对图片进行处理
                int viewHeight = view.getMeasuredHeight();
                // 计算将要裁剪的图片的顶部以及底部的偏移量
                int offset = (scaledBitmap.getHeight() - viewHeight) / 2;
                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
                Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(),
                        scaledBitmap.getHeight() - offset * 2);
                if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收
                    scaledBitmap.recycle();
                    System.gc();
                }
                // 设置图片显示
                view.setImageDrawable(new BitmapDrawable(context.getResources(), finallyBitmap));
                return true;
            }
        });
    }
}
