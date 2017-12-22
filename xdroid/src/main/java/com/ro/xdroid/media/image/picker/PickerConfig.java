package com.ro.xdroid.media.image.picker;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ro.xdroid.R;

import com.ro.xdroid.media.image.loader.glide.GlideApp;
import com.ro.xdroid.media.image.loader.glide.GlideLoader;

import java.io.File;

/**
 * Created by roffee on 2017/8/8 15:29.
 * Contact with 460545614@qq.com
 */
public class PickerConfig implements com.lzy.imagepicker.loader.ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        if(width > 0 && height > 0){
            GlideApp.with(activity)
                    .load(path)
                    .placeholder(R.drawable.ic_default_image)
                    .error(R.drawable.ic_default_image)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(width, height)
                    .into(imageView);
        }else{
            GlideApp.with(activity)
                    .load(path)
                    .placeholder(R.drawable.ic_default_image)
                    .error(R.drawable.ic_default_image)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        if(width > 0 && height > 0){
            GlideApp.with(activity)                             //配置上下文
                    .load(Uri.fromFile(new File(path)))         //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存全尺寸
                    .override(width, height)
                    .into(imageView);
        }else{
            GlideApp.with(activity)                             //配置上下文
                    .load(Uri.fromFile(new File(path)))         //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存全尺寸
                    .into(imageView);
        }
    }

    @Override
    public void clearMemoryCache() {
        GlideLoader.clearMemoryCache();
    }
}
