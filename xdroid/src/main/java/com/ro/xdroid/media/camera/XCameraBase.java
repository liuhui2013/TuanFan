package com.ro.xdroid.media.camera;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.view.SurfaceView;
import android.view.TextureView;

import com.ro.xdroid.mvp.XApp;
import com.ro.xdroid.view.dialog.ToastKit;

/**
 * Created by roffee on 2017/9/22 09:08.
 * Contact with 460545614@qq.com
 */
public abstract class XCameraBase<C extends XCameraBase> {
    protected static final int MAX_PREVIEW_WIDTH = XApp.getContext().getResources().getDisplayMetrics().widthPixels;
    protected static final int MAX_PREVIEW_HEIGHT = XApp.getContext().getResources().getDisplayMetrics().heightPixels;

    protected Activity activity;
    protected TextureView textureView;
    protected SurfaceView surfaceView;
    protected CameraMode.ViewType viewType;
    protected boolean isFaceFront = true;
    protected boolean isCapture = true;
    protected boolean isOk;
    protected boolean isSupportFlash;
    protected boolean isFlash;
    protected int faceSupports;
    protected int curLensFace;
    protected int imageFormatType = ImageFormat.JPEG;
    protected String curCameraId;
    protected boolean isClose;

    protected abstract void preOpen();

    public XCameraBase(Activity activity, TextureView textureView){
        this.activity = activity;
        this.textureView = textureView;
        this.viewType = CameraMode.ViewType.TextureView;
    }
    public XCameraBase(Activity activity, SurfaceView surfaceView){
        this.activity = activity;
        this.surfaceView = surfaceView;
        this.viewType = CameraMode.ViewType.SurfaceView;
    }
    @SuppressWarnings("unchecked")
    public C isFaceFront(boolean isFaceBack){
        this.isFaceFront = isFaceFront;
        return (C)this;
    }
    @SuppressWarnings("unchecked")
    public C isFlash(boolean isFlash){
        this.isFlash = isFlash;
        return (C)this;
    }
    @SuppressWarnings("unchecked")
    public C prepareView(){
        if(activity == null) {
            ToastKit.show("context is null");
            return (C)this;
        }
        if(viewType == CameraMode.ViewType.TextureView){
            if(textureView == null){
                ToastKit.show("TextureView is null");
                return (C)this;
            }
        }else{
            if(surfaceView == null){
                ToastKit.show("surfaceView is null");
                return (C)this;
            }
        }
        isOk = true;
        preOpen();
        return (C)this;
    }
    public boolean getIsFaceFront(){
        return isFaceFront;
    }
    protected void openCamera(int width, int height){}
    protected void closeCamera(){ isClose = true; }
    public void switchCamera(){ isClose  = false; }
    public void onPause(){}
    public void onResume(){}
    public void onDestroy(){}
}
