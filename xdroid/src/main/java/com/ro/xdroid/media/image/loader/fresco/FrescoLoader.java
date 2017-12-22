package com.ro.xdroid.media.image.loader.fresco;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.media.image.loader.Mode;
import com.ro.xdroid.media.image.loader.transformations.Transformations;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Created by roffee on 2017/8/4 17:04.
 * Contact with 460545614@qq.com
 */
public class FrescoLoader {
    public static void init(Context context){
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(context, new OkHttpClient())
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .setMemoryTrimmableRegistry(getMemoryTrimmableRegistry())
//                .setBitmapMemoryCacheParamsSupplier(new BitmapMemoryCacheParamsSupplier(context))
                .build();
        Fresco.initialize(context, imagePipelineConfig);
    }

    public static <L> void withSimpleLoader(SimpleDraweeView draweeView, L l, int... placeErrorResId){
        withSimpleLoader(draweeView, l, Mode.ScaleType.CenterCrop, placeErrorResId);
    }
    public static <L> void withSimpleLoader(SimpleDraweeView draweeView, L l, Mode.ScaleType scaleType, int... placeErrorResId){
        if(draweeView == null){return;}

        Uri uri = getUri(l);

        GenericDraweeHierarchy draweeHierarchy = draweeView.getHierarchy();
        draweeHierarchy.setFadeDuration(Mode.FadeDuration);
        ScaleType frescosScaleType = null;
        if(scaleType != null) frescosScaleType = scaleType.getScaleType();
        if(frescosScaleType != null){
            draweeHierarchy.setActualImageScaleType(frescosScaleType);
            if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
                draweeHierarchy.setPlaceholderImage(placeErrorResId[0], frescosScaleType);
                draweeHierarchy.setFailureImage(placeErrorResId[0], frescosScaleType);
            }
        }else{
            if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
                draweeHierarchy.setPlaceholderImage(placeErrorResId[0]);
                draweeHierarchy.setFailureImage(placeErrorResId[0]);
            }
        }

        if(uri != null) draweeView.setImageURI(uri);
    }
    public static <L> void withWrapLoader(SimpleDraweeView draweeView, L l, final Mode.WrapType wrapType, float inScreenFDefine, int... placeErrorResId){
        if(draweeView == null){return;}

        Uri uri = getUri(l);

        GenericDraweeHierarchy draweeHierarchy = draweeView.getHierarchy();
        draweeHierarchy.setFadeDuration(Mode.FadeDuration);
        draweeHierarchy.setActualImageScaleType(ScaleType.CENTER_CROP);
        if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
            draweeHierarchy.setPlaceholderImage(placeErrorResId[0], ScaleType.CENTER_CROP);
            draweeHierarchy.setFailureImage(placeErrorResId[0], ScaleType.CENTER_CROP);
        }
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(new BaseControllerListener<ImageInfo>(){
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo  imageInfo, @Nullable Animatable animatable) {
                        if (imageInfo == null || wrapType == null) return;
                        ViewGroup.LayoutParams layoutParams = draweeView.getLayoutParams();
                        float scale;
                        if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenF1){
                            scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f)/(imageInfo.getWidth()*1.0f);
                            layoutParams.height = (int)(imageInfo.getHeight()*scale);
                        }else if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenF2){
                            scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f/2.0f)/(imageInfo.getWidth()*1.0f);
                            layoutParams.height = (int)(imageInfo.getHeight()*scale);
                        }else if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenF3){
                            scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f/3.0f)/(imageInfo.getWidth()*1.0f);
                            layoutParams.height = (int)(imageInfo.getHeight()*scale);
                        }else if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenF4){
                            scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f/4.0f)/(imageInfo.getWidth()*1.0f);
                            layoutParams.height = (int)(imageInfo.getHeight()*scale);
                        }else if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenFDefine){
                            scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f*inScreenFDefine)/(imageInfo.getWidth()*1.0f);
                            layoutParams.height = (int)(imageInfo.getHeight()*scale);
                        } else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenF1){
                            scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f)/(imageInfo.getHeight()*1.0f);
                            layoutParams.width = (int)(imageInfo.getWidth()*scale);;
                        }else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenF2){
                            scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f/2.0f)/(imageInfo.getHeight()*1.0f);
                            layoutParams.width = (int)(imageInfo.getWidth()*scale);;
                        }else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenF3){
                            scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f/3.0f)/(imageInfo.getHeight()*1.0f);
                            layoutParams.width = (int)(imageInfo.getWidth()*scale);;
                        }else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenF4){
                            scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f/4.0f)/(imageInfo.getHeight()*1.0f);
                            layoutParams.width = (int)(imageInfo.getWidth()*scale);;
                        }else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenFDefine){
                            scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f)/(imageInfo.getHeight()*1.0f);
                            layoutParams.width = (int)(imageInfo.getWidth()*scale);;
                        }else if(wrapType ==  Mode.WrapType.BothWrap){
                            if(imageInfo.getWidth() > ToolsKit.getDisplayMetrics().widthPixels) layoutParams.width = ToolsKit.getDisplayMetrics().widthPixels;
                            else layoutParams.width = imageInfo.getWidth();

                            if(imageInfo.getWidth() > ToolsKit.getDisplayMetrics().heightPixels) layoutParams.height = ToolsKit.getDisplayMetrics().heightPixels;
                            else layoutParams.height = imageInfo.getHeight();
                        }
                        draweeView.setLayoutParams(layoutParams);
                    }
                })
                .setUri(uri)
                .build();

        draweeView.setController(controller);
    }

    public static <W, L> void withTransLoader(SimpleDraweeView draweeView, W w, L l, Transformations.TransType transType, Object[] transArgs, int... placeErrorResId){
        if(draweeView == null){return;}
        if(transType == null){
            withSimpleLoader(draweeView, l, placeErrorResId);
        }else{
            Uri uri = getUri(l);

            GenericDraweeHierarchy draweeHierarchy = draweeView.getHierarchy();
            draweeHierarchy.setFadeDuration(Mode.FadeDuration);
            draweeHierarchy.setActualImageScaleType(ScaleType.CENTER_CROP);
            if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
                draweeHierarchy.setPlaceholderImage(placeErrorResId[0], ScaleType.CENTER_CROP);
                draweeHierarchy.setFailureImage(placeErrorResId[0], ScaleType.CENTER_CROP);
            }
            ImageRequest request = null;
//            if(transType == Transformations.TransType.TransBlur){
//                int iterations = 1; int blurRadius = 5;
//                if(!ToolsKit.isEmpty(transArgs)){
//                    if(transArgs.length > 0) iterations = (int)transArgs[0];
//                    if(transArgs.length > 1) blurRadius = (int)transArgs[0];
//                }
//                request = ImageRequestBuilder.newBuilderWithSource(uri)
//                        .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
//                        .build();
//            }else{
                BasePostprocessor postprocessor = Transformations.getFrescoPostprocessor((Context) w, transType, transArgs);
                request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setPostprocessor(postprocessor)
                        .build();
//            }
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        }
    }
    public static <L> void withTransLoader(SimpleDraweeView draweeView, L l, BasePostprocessor postprocessor, int... placeErrorResId){
        if(draweeView == null){return;}

        Uri uri = getUri(l);

        GenericDraweeHierarchy draweeHierarchy = draweeView.getHierarchy();
        draweeHierarchy.setFadeDuration(Mode.FadeDuration);
        draweeHierarchy.setActualImageScaleType(ScaleType.CENTER_CROP);
        if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
            draweeHierarchy.setPlaceholderImage(placeErrorResId[0], ScaleType.CENTER_CROP);
            draweeHierarchy.setFailureImage(placeErrorResId[0], ScaleType.CENTER_CROP);
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(postprocessor)
                .build();
        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();
        draweeView.setController(controller);
    }
    public static Builder with(SimpleDraweeView draweeView){
        if(draweeView == null){return null;}
        return new Builder(draweeView);
    }
    public static class Builder{
        private SimpleDraweeView draweeView;
        private GenericDraweeHierarchy draweeHierarchy;
        private PipelineDraweeControllerBuilder draweeControllerBuilder;
        private Resources resources;
        private ImageRequestBuilder imageRequestBuilder;
        private BasePostprocessor postprocessor;
        private ControllerListener<ImageInfo> controllerListener;
        private int width, height;
        private boolean progressiveEnable = true;

        public Builder(SimpleDraweeView draweeView){
            this.draweeView = draweeView;
            resources = draweeView.getContext().getResources();
            draweeHierarchy = draweeView.getHierarchy();
            draweeControllerBuilder = Fresco.newDraweeControllerBuilder();
        }
        public Builder setPlaceholderImage(int resourceId){
            draweeHierarchy.setPlaceholderImage(resourceId);
            return this;
        }
        public Builder setPlaceholderImage(int resourceId, ScaleType scaleType){
            if(scaleType != null) draweeHierarchy.setPlaceholderImage(resourceId, scaleType);
            else draweeHierarchy.setPlaceholderImage(resourceId);
            return this;
        }
        public Builder setFailureImage(int resourceId){
            draweeHierarchy.setFailureImage(resourceId);
            return this;
        }
        public Builder setFailureImage(int resourceId, ScaleType scaleType){
            if(scaleType != null) draweeHierarchy.setFailureImage(resourceId, scaleType);
            else draweeHierarchy.setFailureImage(resourceId);
            return this;
        }
        public Builder setBackgroundImage(int resourceId){
            draweeHierarchy.setBackgroundImage(resources.getDrawable(resourceId));
            return this;
        }
        public Builder setProgressBarImage(int resourceId){
            draweeHierarchy.setProgressBarImage(resourceId);
            return this;
        }
        public Builder setProgressBarImage(int resourceId, ScaleType scaleType){
            if(scaleType != null) draweeHierarchy.setProgressBarImage(resourceId, scaleType);
            else draweeHierarchy.setProgressBarImage(resourceId);
            return this;
        }
        public Builder setRetryImage(int resourceId){
            draweeHierarchy.setRetryImage(resourceId);
            return this;
        }
        public Builder setRetryImage(int resourceId, ScaleType scaleType){
            if(scaleType != null) draweeHierarchy.setRetryImage(resourceId, scaleType);
            else draweeHierarchy.setRetryImage(resourceId);
            return this;
        }
        public Builder setOverlayImage(int resourceId){
            draweeHierarchy.setOverlayImage(resources.getDrawable(resourceId));
            return this;
        }
        public Builder setActualImageScaleType(ScaleType scaleType){
            if(scaleType != null) draweeHierarchy.setActualImageScaleType(scaleType);
            return this;
        }
        public Builder setFadeDuration(int durationMs){
            draweeHierarchy.setFadeDuration(durationMs);
            return this;
        }
        public  Builder setProgressiveEnable(Boolean progressiveEnable){
            this.progressiveEnable = progressiveEnable;
            return this;
        }
        public Builder setRoundingParams(){
            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);
            draweeHierarchy.setRoundingParams(roundingParams);
            return this;
        }
        public Builder setRoundingParams(RoundingParams roundingParams){
            draweeHierarchy.setRoundingParams(roundingParams);
            return this;
        }
        public Builder setResize(int width, int height){
            this.width = width;
            this.height = height;
            return this;
        }
        public Builder setPostprocessor(BasePostprocessor postprocessor){
            this.postprocessor = postprocessor;
            return this;
        }
        public Builder setControllerListener(ControllerListener<ImageInfo> controllerListener){
            this.controllerListener = controllerListener;
            return this;
        }
        public void loader(String url){
            if(url == null){return;}
            load(Uri.parse(url));
        }
        public void loader(File file){
            if(file == null){return;}
            load(UriUtil.getUriForFile(file));
        }
        public void loader(int resourceId){
            if(resourceId < 1){return;}
            load(UriUtil.getUriForResourceId(resourceId));
        }
        private void load(Uri uri){
            if(draweeView == null){return;}
            imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
            imageRequestBuilder.setRotationOptions(RotationOptions.autoRotate())
                    .setProgressiveRenderingEnabled(progressiveEnable);

            if(width > 0 && height > 0){
                imageRequestBuilder.setResizeOptions(new ResizeOptions(width, height));
            }
            if(UriUtil.isLocalFileUri(uri)){
                imageRequestBuilder.setLocalThumbnailPreviewsEnabled(true);
            }
            if (postprocessor != null) {
                imageRequestBuilder.setPostprocessor(postprocessor);
            }
            imageRequestBuilder.build();
            draweeControllerBuilder.setOldController(draweeView.getController())
                    .setImageRequest(imageRequestBuilder.build())
                    .setControllerListener(controllerListener)
                    .setTapToRetryEnabled(true)
                    .setAutoPlayAnimations(true);
            draweeView.setController(draweeControllerBuilder.build());
        }
    }

    public static void pause() {
        try {
            Fresco.getImagePipeline().pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void resume() {
        try {
            Fresco.getImagePipeline().resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static long getDiskCacheSize() {
        return Fresco.getImagePipelineFactory().getMainFileCache().getSize()
                + Fresco.getImagePipelineFactory().getSmallImageFileCache().getSize();
    }
    public static void clearDiskCache(){
        Fresco.getImagePipeline().clearDiskCaches();
    }
    public static void clearAll(){
        Fresco.getImagePipeline().clearMemoryCaches();
        Fresco.getImagePipeline().clearDiskCaches();
    }
    public static void onLowMemory(){
        Fresco.getImagePipeline().clearMemoryCaches();
    }
    public static void onTrimMemory(){
        onLowMemory();
    }

    private static <L> Uri getUri(L l){
        Uri uri = null;

        try {
            if(l == null) {
                uri = Uri.parse("");
                return uri;
            };

            if(l instanceof String){
                String ls = (String) l;
                if(ls.startsWith("http") || ls.startsWith("https") || ls.startsWith("HTTP")){
                    uri = Uri.parse(ls);
                }else{
                    File file = new File(ls);
                    if(file != null){uri = UriUtil.getUriForFile(file);}
                }
            }else if(l instanceof Integer){
                int li = (Integer)l;
                if(li > 0){uri = UriUtil.getUriForResourceId(li);}
            }else if(l instanceof File){
                File lf = (File)l;
                if(lf.exists() && lf.isFile()){ uri = UriUtil.getUriForFile(lf);}
                uri = UriUtil.getUriForFile((File) l);
            }else{
                uri = Uri.parse(l.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            uri = Uri.parse("");
        }
        return uri;
    }



    private static MemoryTrimmableRegistry getMemoryTrimmableRegistry() {
        MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
        memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    // 清除内存缓存
                    Fresco.getImagePipeline().clearMemoryCaches();
                }
            }
        });
        return memoryTrimmableRegistry;
    }
    private static class BitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {
        private static final int MAX_CACHE_ENTRIES = 56;
        private static final int MAX_CACHE_ASHM_ENTRIES = 128;
        private static final int MAX_CACHE_EVICTION_SIZE = 5;
        private static final int MAX_CACHE_EVICTION_ENTRIES = 5;

        Context context;
        ActivityManager activityManager;
        public BitmapMemoryCacheParamsSupplier(Context context){
            this.context = context;
            activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        @Override
        public MemoryCacheParams get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return new MemoryCacheParams(getMaxCacheSize(),         // 内存缓存中总图片的最大大小,以字节为单位。
                        MAX_CACHE_ENTRIES,                             // 内存缓存中图片的最大数量。
                        MAX_CACHE_EVICTION_SIZE,                      // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                        MAX_CACHE_EVICTION_ENTRIES,                   // 内存缓存中准备清除的总图片的最大数量。
                        1);                                               // 内存缓存中单个图片的最大大小。
            } else {
                return new MemoryCacheParams(
                        getMaxCacheSize(),
                        MAX_CACHE_ASHM_ENTRIES,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE);
            }
        }

        private int getMaxCacheSize() {
//            long maxHeapSize = Runtime.getRuntime().maxMemory();
//            return maxHeapSize / 3;
            final int maxMemory = Math.min(activityManager.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
            if (maxMemory < 32 * ByteConstants.MB) {
                return 4 * ByteConstants.MB;
            } else if (maxMemory < 64 * ByteConstants.MB) {
                return 6 * ByteConstants.MB;
            } else {
                // We don't want to use more ashmem on Gingerbread for now, since it doesn't respond well to
                // native memory pressure (doesn't throw exceptions, crashes app, crashes phone)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    return 8 * ByteConstants.MB;
                } else {
                    return maxMemory / 5;
                }
            }
        }
    }
}
