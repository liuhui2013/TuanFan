package com.ro.xdroid.media.image.loader.glide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.ro.xdroid.kit.FileKit;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.media.image.loader.ImageLoaderKit;
import com.ro.xdroid.media.image.loader.Mode;
import com.ro.xdroid.media.image.loader.transformations.Transformations;
import com.ro.xdroid.mvp.XApp;
import com.ro.xdroid.view.widget.ResizableImageView;

import jp.wasabeef.glide.transformations.BitmapTransformation;

/**
 * Created by roffee on 2017/8/7 10:21.
 * Contact with 460545614@qq.com
 */
public class GlideLoader {
//    private static Glide glide = Glide.get(XApp.getContext());

    public static <W, L> void withSimpleLoader(ImageView imageView, W w, L l, int... placeErrorResId){
        withSimpleLoader(imageView, w, l, Mode.ScaleType.CenterCrop, placeErrorResId);
    }
    public static <W, L> void withSimpleLoader(ImageView imageView, W w, L l, Mode.ScaleType scaleType, int... placeErrorResId){
        if(imageView == null){return;}
        GlideRequests glideRequests = getGlideRequests(w);
        if(glideRequests == null){return;}
        if(l instanceof String && ImageLoaderKit.isGif(l.toString())){
            glideRequests.asGif();
        }
        GlideRequest<Drawable> drawableGlideRequest = glideRequests.load(l);
        if(scaleType != null){
            if(scaleType == Mode.ScaleType.CenterCrop) drawableGlideRequest.centerCrop();
            else if(scaleType == Mode.ScaleType.CenterInside) drawableGlideRequest.centerInside();
            else if(scaleType == Mode.ScaleType.CircleCrop) drawableGlideRequest.circleCrop();
            else if(scaleType == Mode.ScaleType.FitCenter) drawableGlideRequest.fitCenter();
            else if(scaleType == Mode.ScaleType.None){}
            else drawableGlideRequest.centerCrop();
        }
        if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
            drawableGlideRequest.placeholder(placeErrorResId[0])
                    .error(placeErrorResId[0]);
        }
        drawableGlideRequest.into(imageView);
        /*drawableGlideRequest.transition(DrawableTransitionOptions.withCrossFade(Mode.FadeDuration))
                .into(imageView);*/
    }
    public static <W, L> void withWrapLoader(ImageView imageView,  W w, L l, Mode.WrapType wrapType, float inScreenFDefine, int... placeErrorResId){
        if(imageView == null){return;}

        GlideRequests glideRequests = getGlideRequests(w);
        if(glideRequests == null){return;}

        GlideRequest<Bitmap> glideRequest;
        if(imageView instanceof ResizableImageView){
            glideRequest = glideRequests.asBitmap().load(l)
                    .transition(BitmapTransitionOptions.withCrossFade(Mode.FadeDuration));
            if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
                glideRequest.placeholder(placeErrorResId[0])
                        .error(placeErrorResId[0]);
            }
            glideRequest.into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    imageView.setImageBitmap(resource);
                }
            });
        }else{
            glideRequest = glideRequests.asBitmap().load(l)
//                .skipMemoryCache(true)
                    .centerCrop()
                    .transition(BitmapTransitionOptions.withCrossFade(Mode.FadeDuration));

            if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
                glideRequest.placeholder(placeErrorResId[0])
                        .error(placeErrorResId[0]);
            }
            glideRequest.into(new WrapTarget(imageView, wrapType, inScreenFDefine));
        }
    }
    public static <W, L> void withTransLoader(ImageView imageView, W w, L l, Transformations.TransType transType, Object[] transArgs, int... placeErrorResId){
        if(imageView == null){return;}

        GlideRequests glideRequests = getGlideRequests(w);
        if(glideRequests == null){return;}

        GlideRequest<Bitmap> glideRequest = glideRequests.asBitmap().load(l)
//                .skipMemoryCache(true)
                .centerCrop()
                .transition(BitmapTransitionOptions.withCrossFade(Mode.FadeDuration));

        if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
            glideRequest.placeholder(placeErrorResId[0])
                    .error(placeErrorResId[0]);
        }
        if(transType == null) return;
        if(transType == Transformations.TransType.TransCircle){
////            glideRequest.circleCrop();
            glideRequest.into(new TransTarget((Context)w, imageView, transType));
        }else{
            BitmapTransformation bitmapTransformation = Transformations.getGlideTransformation(transType, transArgs);
            if(bitmapTransformation != null) glideRequest.apply(RequestOptions.bitmapTransform(bitmapTransformation));
            glideRequest .into(imageView);
        }
//        GlideLoader.withTransLoader(w, l, imageView, Transformations.getGlideTransformation(transType, transArgs), placeErrorResId);
    }
    public static <W, L> void withTransLoader(ImageView imageView, W w, L l, BitmapTransformation bitmapTransformation, int... placeErrorResId){
        if(imageView == null){return;}

        GlideRequests glideRequests = getGlideRequests(w);
        if(glideRequests == null){return;}

        GlideRequest<Bitmap> glideRequest = glideRequests.asBitmap().load(l)
//                .skipMemoryCache(true)
                .centerCrop()
                .transition(BitmapTransitionOptions.withCrossFade(Mode.FadeDuration));

        if(!ToolsKit.isEmpty(placeErrorResId) && placeErrorResId[0] > 0){
            glideRequest.placeholder(placeErrorResId[0])
                    .error(placeErrorResId[0]);
        }
        if(bitmapTransformation != null) glideRequest.apply(RequestOptions.bitmapTransform(bitmapTransformation));
        glideRequest.into(imageView);
    }

    public static <W> GlideLoader.Builder with(W w){
        if(w == null){return null;}
        return new Builder(w);
    }
    public static class Builder<W>{
        private W w;
        private GlideRequests glideRequests;
        private GlideRequest glideRequest;
        private Mode.TranscodeType transcodeType = Mode.TranscodeType.Drawable;
        private boolean isSkipMemoryCache = false;
        private GlideReq glideReq;

        public Builder(W w){
            if(w instanceof FragmentActivity){
                glideRequests = GlideApp.with((FragmentActivity) w);
            }else if(w instanceof Activity){
                glideRequests = GlideApp.with((Activity) w);
            } else if(w instanceof Fragment){
                glideRequests = GlideApp.with((Fragment) w);
            }else if(w instanceof android.support.v4.app.Fragment){
                glideRequests = GlideApp.with((android.support.v4.app.Fragment) w);
            }else if(w instanceof Context){
                glideRequests = GlideApp.with((Context) w);
            }else if(w instanceof View){
                glideRequests = GlideApp.with((View) w);
            }else{
                throw new IllegalStateException("不支持此类型");
            }
        }
        public Builder asBitmap(){
            glideRequests.asBitmap();
            transcodeType = Mode.TranscodeType.Bitmap;
            return this;
        }
        public Builder asGif(){
            glideRequests.asGif();
            transcodeType = Mode.TranscodeType.Drawable;
            return this;
        }
        public Builder asDrawable(){
            glideRequests.asDrawable();
            transcodeType = Mode.TranscodeType.Drawable;
            return this;
        }
        public Builder isSkipMemoryCache(boolean isSkipMemoryCache){
            this.isSkipMemoryCache = isSkipMemoryCache;
            return this;
        }
        public GlideReq loader(Object l){
            if(l == null){l = "";}
            if(ImageLoaderKit.isGif(l.toString())){
                glideRequests.asGif();
            }
            glideRequest = glideRequests.load(l);
            if(isSkipMemoryCache){
                glideRequest.skipMemoryCache(true);
            }
            glideReq = new GlideReq(glideRequest, transcodeType);
            return glideReq;
        }
        public static class GlideReq{
            private GlideRequest glideRequest;
            private Mode.TranscodeType transcodeType;
            public GlideReq(GlideRequest glideRequest, Mode.TranscodeType transcodeType){
                this.glideRequest = glideRequest;
                this.transcodeType = transcodeType;
            }
            public GlideReq scaleType(Mode.ScaleType scaleType){
                if(scaleType == null){glideRequest.centerCrop(); return this;}
                if(scaleType == Mode.ScaleType.CenterCrop){glideRequest.centerCrop();}
                else if(scaleType == Mode.ScaleType.CenterInside){glideRequest.centerInside();}
                else if(scaleType == Mode.ScaleType.CircleCrop){glideRequest.circleCrop();}
                else if(scaleType == Mode.ScaleType.FitCenter){glideRequest.fitCenter();}
                return this;
            }
            public GlideReq placeholder(int resourceId){
                glideRequest.placeholder(resourceId);
                return this;
            }
            public GlideReq placeholder(Drawable drawable){
                if(drawable != null){
                    glideRequest.placeholder(drawable);
                }
                return this;
            }
            public GlideReq error(int resourceId){
                glideRequest.error(resourceId);
                return this;
            }
            public GlideReq error(Drawable drawable){
                if(drawable != null){
                    glideRequest.error(drawable);
                }
                return this;
            }
            public GlideReq fallback(int resourceId){
                glideRequest.fallback(resourceId);
                return this;
            }
            public GlideReq fallback(Drawable drawable){
                if(drawable != null){
                    glideRequest.fallback(drawable);
                }
                return this;
            }
            @SuppressWarnings("unchecked")
            public GlideReq fadeDuration(int fadeDuration){
                if(transcodeType == Mode.TranscodeType.Drawable){
                    glideRequest.transition(DrawableTransitionOptions.withCrossFade(fadeDuration));
                }else{
                    glideRequest.transition(BitmapTransitionOptions.withCrossFade(fadeDuration));
                }
                return this;
            }
            public GlideReq reSize(int size){
                glideRequest.override(size);
                return this;
            }
            public GlideReq reSize(int width, int height){
                glideRequest.override(width, height);
                return this;
            }
            //        public Builder apply(RequestOptions requestOptions){
//            if(requestOptions != null){
//                glideRequest.apply(requestOptions );
//            }
//            return this;
//        }
            public GlideReq thumbnail(float sizeMultiplier){
                glideRequest.thumbnail(sizeMultiplier);
                return this;
            }
            @SuppressWarnings("unchecked")
            public GlideReq requestListener(RequestListener requestListener){
                if(requestListener != null){
                    glideRequest.listener(requestListener);
                }
                return this;
            }
            public GlideReq requestListener(Transformation<Bitmap> transformation){
                if(transformation != null && transcodeType == Mode.TranscodeType.Bitmap){
                    glideRequest.transform(transformation);
                }
                return this;
            }
            public void into(ImageView imageView){
                if(imageView == null){return;}
                into(imageView);
            }
            public void into(Target targetView){
                if(targetView == null){return;}
                into(targetView);
            }
            private void into(Object in){
                if(in instanceof ImageView){
                    glideRequest.into((ImageView) in);
                }else{
                    glideRequest.into((Target) in);
                }
            }
        }

    }
    public static void resumeRequests(Object w){
        try {
            GlideRequests glideRequests = getGlideRequests(w);
            if(glideRequests != null) glideRequests.resumeRequests();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public static void pauseRequests(Object w){
        try {
            GlideRequests glideRequests = getGlideRequests(w);
            if(glideRequests != null) glideRequests.pauseRequests();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public static long getDiskCacheSize(){
        return FileKit.getFileSize(GlideApp.getPhotoCacheDir(XApp.getContext()));
    }
    public static void clearMemoryCache(){
        Glide.get(XApp.getContext()).clearMemory();
    }
    public static void clearDiskCache(){
        Glide.get(XApp.getContext()).clearDiskCache();
    }
    public static void clearAll(){
        Glide.get(XApp.getContext()).clearMemory();
        Glide.get(XApp.getContext()).clearDiskCache();
    }
    public static void onLowMemory(){
        Glide.get(XApp.getContext()).onLowMemory();
    }
    public static void onTrimMemory(int level){
        Glide.get(XApp.getContext()).onTrimMemory(level);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static <W> GlideRequests getGlideRequests(W w){
        if(w == null) return null;
        GlideRequests glideRequests = null;
        if(w instanceof FragmentActivity){
            FragmentActivity fragmentActivity = (FragmentActivity) w;
            if(fragmentActivity.isFinishing() || fragmentActivity.isDestroyed()) return null;
            glideRequests = GlideApp.with(fragmentActivity);
        }else if(w instanceof Activity){
            Activity activity = (Activity) w;
            if(activity.isFinishing() || activity.isDestroyed()) return null;
            glideRequests = GlideApp.with(activity);
        } else if(w instanceof Fragment){
            Fragment fragment = (Fragment) w;
            if(fragment.isRemoving() || fragment.isDetached()) return null;
            glideRequests = GlideApp.with(fragment);
        }else if(w instanceof android.support.v4.app.Fragment){
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) w;
            if(fragment.isRemoving() || fragment.isDetached()) return null;
            glideRequests = GlideApp.with(fragment);
        }else if(w instanceof Context){
            Context context = (Context) w;
            glideRequests = GlideApp.with(context);
        }else if(w instanceof View){
            View view = (View) w;
            glideRequests = GlideApp.with(view);
        }
        return glideRequests;
    }
    private static class WrapTarget extends SimpleTarget<Bitmap>{
        Mode.WrapType wrapType;
        ImageView imageView;
        float inScreenFDefine;

        public WrapTarget(ImageView imageView, Mode.WrapType wrapType, float inScreenFDefine){
            this.imageView = imageView;
            this.wrapType = wrapType;
            this.inScreenFDefine = inScreenFDefine;
        }
        @Override
        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            if(resource == null || wrapType == null || imageView == null){
                return;
            }
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            float scale;
            if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenF1){
                scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f)/(resource.getWidth()*1.0f);
                layoutParams.height = (int)(resource.getHeight()*scale);
            }else if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenF2){
                scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f/2.0f)/(resource.getWidth()*1.0f);
                layoutParams.height = (int)(resource.getHeight()*scale);
            }else if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenF3){
                scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f/3.0f)/(resource.getWidth()*1.0f);
                layoutParams.height = (int)(resource.getHeight()*scale);
            }else if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenF4){
                scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f/4.0f)/(resource.getWidth()*1.0f);
                layoutParams.height = (int)(resource.getHeight()*scale);
            }else if(wrapType ==  Mode.WrapType.HeightWrapAndWidthMatchInScreenFDefine){
                scale = (ToolsKit.getDisplayMetrics().widthPixels*1.0f*inScreenFDefine)/(resource.getWidth()*1.0f);
                layoutParams.height = (int)(resource.getHeight()*scale);
            } else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenF1){
                scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f)/(resource.getHeight()*1.0f);
                layoutParams.width = (int)(resource.getWidth()*scale);;
            }else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenF2){
                scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f/2.0f)/(resource.getHeight()*1.0f);
                layoutParams.width = (int)(resource.getWidth()*scale);;
            }else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenF3){
                scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f/3.0f)/(resource.getHeight()*1.0f);
                layoutParams.width = (int)(resource.getWidth()*scale);;
            }else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenF4){
                scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f/4.0f)/(resource.getHeight()*1.0f);
                layoutParams.width = (int)(resource.getWidth()*scale);;
            }else if(wrapType ==  Mode.WrapType.WidthWrapAndHeightMatchInScreenFDefine){
                scale = (ToolsKit.getDisplayMetrics().heightPixels*1.0f)/(resource.getHeight()*1.0f);
                layoutParams.width = (int)(resource.getWidth()*scale);;
            }else if(wrapType ==  Mode.WrapType.BothWrap){
                if(resource.getWidth() > ToolsKit.getDisplayMetrics().widthPixels) layoutParams.width = ToolsKit.getDisplayMetrics().widthPixels;
                else layoutParams.width = resource.getWidth();

                if(resource.getWidth() > ToolsKit.getDisplayMetrics().heightPixels) layoutParams.height = ToolsKit.getDisplayMetrics().heightPixels;
                else layoutParams.height = resource.getHeight();
            }
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(resource);
        }
    }
    private static class TransTarget extends SimpleTarget<Bitmap>{
        Context context;
        Transformations.TransType transType;
        ImageView imageView;
        public TransTarget(Context context, ImageView view, Transformations.TransType transType) {
            this.context = context;
            this.imageView = view;
            this.transType = transType;
        }
        @Override
        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            if(transType == Transformations.TransType.TransCircle){
                RoundedBitmapDrawable roundedBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                roundedBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(roundedBitmapDrawable);
            }
        }
    }
    private static class TransViewTarget extends BitmapImageViewTarget{
        Context context;
        Transformations.TransType transType;
        ImageView imageView;
        public TransViewTarget(Context context, ImageView view, Transformations.TransType transType) {
            super(view);
            this.context = context;
            this.imageView = view;
            this.transType = transType;
        }
        @Override
        protected void setResource(Bitmap resource) {
            if(transType == Transformations.TransType.TransCircle){
                RoundedBitmapDrawable roundedBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                roundedBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(roundedBitmapDrawable);
            }
        }
    }
}
