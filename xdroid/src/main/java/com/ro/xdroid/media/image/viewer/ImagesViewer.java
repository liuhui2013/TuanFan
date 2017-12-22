package com.ro.xdroid.media.image.viewer;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.view.View;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.view.dialog.ToastKit;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;
import java.util.Random;

/**
 * Created by roffee on 2017/9/13 15:10.
 * Contact with 460545614@qq.com
 */
public class ImagesViewer<T> {
    private ImageViewer.Builder<T> ivBuilder;
    private ImageViewer.Formatter<T> formatter;
    private ImageOverlayView overlayView;
    private RoundingParams roundingParams;
    private Random random;
    private Context context;
    private int imagesSize;

    public static <T> ImagesViewer<T> create(Context context, T[] images) throws Throwable{
        if(context == null || ToolsKit.isEmpty(images)){
            ToastKit.show("ImageViewer参数配置为空");
            throw new Throwable("ImageViewer参数配置为空");
        }
        return new ImagesViewer<T>(context, images);
    }
    public static <T> ImagesViewer<T> create(Context context, List<T> images, ImageViewer.Formatter<T> formatter) throws Throwable{
        if(context == null || formatter == null || ToolsKit.isEmpty(images)){
            ToastKit.show("ImageViewer参数配置为空");
            throw new Throwable("ImageViewer参数配置为空");
        }
        return new ImagesViewer<T>(context, images, formatter);
    }


    public ImagesViewer(Context context, T[] images){
        this.context = context;
        imagesSize = images.length;
        ivBuilder = new ImageViewer.Builder<>(context, images);
        init();
    }
    public ImagesViewer(Context context, List<T> images, ImageViewer.Formatter<T> formatter){
        this.context = context;
        this.formatter = formatter;
        imagesSize = images.size();
        ivBuilder = new ImageViewer.Builder<>(context, images);
        init();
    }
    private void init(){
        ivBuilder.setStartPosition(0)
                .hideStatusBar(true)
                .allowSwipeToDismiss(true)
                .allowZooming(true);
        if(formatter != null) ivBuilder.setFormatter(formatter);
    }

    public ImagesViewer<T> isHideStatusBar(boolean isHideStatusBar){
        ivBuilder.hideStatusBar(isHideStatusBar);
        return this;
    }
    public ImagesViewer<T> isImageCircle(boolean isImageCircle){
        if(roundingParams == null) roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(isImageCircle);
        GenericDraweeHierarchyBuilder genericDraweeHierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                .setRoundingParams(roundingParams);
        ivBuilder.setCustomDraweeHierarchyBuilder(genericDraweeHierarchyBuilder);
        return this;
    }
    public ImagesViewer<T> isSwipeToDismiss(boolean isSwipeToDismiss){
        ivBuilder.allowSwipeToDismiss(isSwipeToDismiss);
        return this;
    }
    public ImagesViewer<T> isZoomable(boolean isZoomable){
        ivBuilder.allowZooming(isZoomable);
        return this;
    }
    public ImagesViewer<T> setStartPosition(int position){
        if(position < 0) position = 0;
        else if(position >= imagesSize) position = imagesSize - 1;
        ivBuilder.setStartPosition(position);
        return this;
    }
    public ImagesViewer<T> setContainerPadding(@DimenRes int padding){
        ivBuilder.setContainerPadding(context, padding);
        return this;
    }
    public ImagesViewer<T> setContainerPaddingPx(int padding){
        ivBuilder.setContainerPaddingPx(padding);
        return this;
    }
    public ImagesViewer<T> setContainerPaddingPx(int left, int top, int right, int bottom){
        ivBuilder.setContainerPaddingPx(left, top, right, bottom);
        return this;
    }
    public ImagesViewer<T> setImageMargin(@DimenRes int margin){
        ivBuilder.setImageMargin(context, margin);
        return this;
    }
    public ImagesViewer<T> setImageMarginPx(int margin){
        ivBuilder.setImageMarginPx(margin);
        return this;
    }
    public ImagesViewer<T> setBackgroundColorRes(@ColorRes int color){
        ivBuilder.setBackgroundColorRes(color);
        return this;
    }
    public ImagesViewer<T> setBackgroundColor(@ColorInt int color){
        ivBuilder.setBackgroundColor(color);
        return this;
    }
    public ImagesViewer<T> useRandomBackgroundColor(){
        ivBuilder.setBackgroundColor(getRandomColor());
        return this;
    }
//    public ImagesViewer<T> useGrayscaleProcessing(){
//        ivBuilder.setCustomImageRequestBuilder(
//                ImageViewer.createImageRequestBuilder()
//                        .setPostprocessor(new GrayscalePostprocessor()));
//        return this;
//    }
    public ImagesViewer<T> useOverlayView(OnImageChangeListener imageChangeListener){
        if(imageChangeListener == null) return this;
        if(overlayView == null) overlayView = new ImageOverlayView(context);
        ivBuilder.setOverlayView(overlayView);
        ivBuilder.setImageChangeListener(new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                imageChangeListener.onImageChange(position, overlayView);
            }
        });
        return this;
    }
    public ImagesViewer<T> useDefinedOverlayView(View view, ImageViewer.OnImageChangeListener imageChangeListener){
        if(view == null || imageChangeListener == null) return this;
        ivBuilder.setOverlayView(view);
        ivBuilder.setImageChangeListener(imageChangeListener);
        return this;
    }
    public ImagesViewer<T> setOnDismissListener(ImageViewer.OnDismissListener onDismissListener){
        if(onDismissListener == null) return this;
        ivBuilder.setOnDismissListener(onDismissListener);
        return this;
    }
    public ImagesViewer<T> setFormatter(ImageViewer.Formatter<T> formatter){
        if(formatter == null) return this;
        ivBuilder.setFormatter(formatter);
        return this;
    }
    public void show(){
        ivBuilder.show();
    }
    public void close(){
        ivBuilder = null;
        formatter = null;
        overlayView = null;
        roundingParams = null;
        random = null;
    }
    private int getRandomColor() {
        if(random == null) random = new Random();
        return Color.argb(255, random.nextInt(156), random.nextInt(156), random.nextInt(156));
    }
    public interface OnImageChangeListener {
        void onImageChange(int position, ImageOverlayView overlayView);
    }
}
