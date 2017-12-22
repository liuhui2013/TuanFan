package com.ro.xdroid.media.image.loader;

import android.content.Context;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.media.image.loader.fresco.FrescoLoader;
import com.ro.xdroid.media.image.loader.glide.GlideLoader;
import com.ro.xdroid.media.image.loader.transformations.Transformations;

import jp.wasabeef.glide.transformations.BitmapTransformation;

/**
 * Created by roffee on 2017/8/4 11:44.
 * Contact with 460545614@qq.com
 */
public class ImageLoaderKit {

    public static void init(Context context){FrescoLoader.init(context);}

    public static <W, L> void withSimpleLoader(@NonNull View view, @NonNull W w, L l, @AnyRes int... placeErrorResId){
        if(view instanceof SimpleDraweeView) FrescoLoader.withSimpleLoader((SimpleDraweeView) view, l, placeErrorResId);
        else if(view instanceof ImageView) GlideLoader.withSimpleLoader((ImageView) view, w, l, placeErrorResId);
        else {
            try {
                ImageView imageView = (ImageView) view;
                GlideLoader.withSimpleLoader(imageView, w, l, placeErrorResId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static <L> void withDraweeViewLoader(@NonNull SimpleDraweeView draweeView, L l, @AnyRes int... placeErrorResId){
        FrescoLoader.withSimpleLoader(draweeView, l, placeErrorResId);
    }
    public static <L> void withDraweeViewLoader(@NonNull SimpleDraweeView draweeView, L l, Mode.ScaleType scaleType, @AnyRes int... placeErrorResId){
        FrescoLoader.withSimpleLoader(draweeView, l, scaleType, placeErrorResId);
    }
    public static <W, L> void withImageViewLoader(@NonNull ImageView imageView, @NonNull W w, L l, @AnyRes int... placeErrorResId){
        GlideLoader.withSimpleLoader(imageView, w, l, placeErrorResId);
    }
    public static <W, L> void withImageViewLoader(@NonNull ImageView imageView, @NonNull W w, L l, Mode.ScaleType scaleType, @AnyRes int... placeErrorResId){
        GlideLoader.withSimpleLoader(imageView, w, l, scaleType, placeErrorResId);
    }
    public static <W, L> void withWrapLoader(@NonNull ImageView imageView, @NonNull W w, L l,  Mode.WrapType wrapType, float inScreenFDefine, @AnyRes int... placeErrorResId){
        GlideLoader.withWrapLoader(imageView, w, l, wrapType, inScreenFDefine, placeErrorResId);
    }
    public static <L> void withWrapLoader(@NonNull SimpleDraweeView draweeView, L l,  Mode.WrapType wrapType, float inScreenFDefine, @AnyRes int... placeErrorResId){
        FrescoLoader.withWrapLoader(draweeView, l, wrapType, inScreenFDefine, placeErrorResId);
    }
    public static <W, L> void withTransLoader(@NonNull ImageView imageView, @NonNull W w, L l, Transformations.TransType transType, @AnyRes int... placeErrorResId){
        withTransLoader(imageView, w, l, transType, null, placeErrorResId);
    }
    public static <W, L> void withTransLoader(@NonNull ImageView imageView, @NonNull W w, L l, Transformations.TransType transType, Object[] transArgs, @AnyRes int... placeErrorResId){
        if(imageView instanceof SimpleDraweeView){
            FrescoLoader.withTransLoader((SimpleDraweeView)imageView, w, l, transType, transArgs, placeErrorResId);
        }else{
            GlideLoader.withTransLoader(imageView, w, l, transType, transArgs, placeErrorResId);
        }
    }
    public static <W, L> void withTransLoader(@NonNull ImageView imageView, @NonNull W w, L l, BitmapTransformation bitmapTransformation, @AnyRes int... placeErrorResId){
        GlideLoader.withTransLoader(imageView, w, l, bitmapTransformation, placeErrorResId);
    }
    public static <L> void withTransLoader(@NonNull SimpleDraweeView draweeView, L l, BasePostprocessor postprocessor, @AnyRes int... placeErrorResId){
        FrescoLoader.withTransLoader(draweeView, l, postprocessor, placeErrorResId);
    }
    public static FrescoLoader.Builder with(@NonNull SimpleDraweeView draweeView){ return FrescoLoader.with(draweeView);}
    public static <W> GlideLoader.Builder with(@NonNull W w){
        return GlideLoader.with(w);
    }
    public static void pause(Object... w) {
        FrescoLoader.pause();
        if(!ToolsKit.isEmpty(w)){ GlideLoader.pauseRequests(w[0]);}
    }
    public static void resume(Object... w) {
        FrescoLoader.resume();
        if(!ToolsKit.isEmpty(w)){ GlideLoader.resumeRequests(w[0]);}
    }
    public static void onLowMemory(){
        try {
            FrescoLoader.onLowMemory();
            GlideLoader.onLowMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void onTrimMemory(int level){
        try {
            FrescoLoader.onTrimMemory();
            GlideLoader.onTrimMemory(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static long getDiskCacheSize() {
        return FrescoLoader.getDiskCacheSize() + GlideLoader.getDiskCacheSize();
    }
    public static void clearDiskCache(){
        FrescoLoader.clearDiskCache();
        GlideLoader.clearDiskCache();
    }
    //url-filepath-fullname
    public static boolean isGif(String fullname){
        if(!ToolsKit.isEmpty(fullname)){return false;}
        String[] strings = fullname.split("\\.");
        if(strings.length > 0){
            if(strings[strings.length - 1].toLowerCase().equals("gif")){
                return true;
            }
        }
        return false;
    }
}
