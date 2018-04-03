package com.zhuandanbao.app.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 判断图片是否是第一次加载
 * Created by BFTECH on 2015/8/28.
 */
public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
    static final List<String> displayedImages = Collections
            .synchronizedList(new LinkedList<String>());

    @Override
    public void onLoadingComplete(String imageUri, View view,
                                  Bitmap loadedImage) {
        if (loadedImage != null) {
            ImageView imageView = (ImageView) view;
            // 是否第一次显示
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                // 图片谈入效果
                FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
            }
        }
    }
}



