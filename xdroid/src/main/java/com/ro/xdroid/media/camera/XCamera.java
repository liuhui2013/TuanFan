package com.ro.xdroid.media.camera;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.util.SparseBooleanArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;

import com.ro.xdroid.kit.ToolsKit;

import java.io.IOException;
import java.util.List;

import static android.hardware.Camera.getCameraInfo;
import static android.hardware.Camera.open;

/**
 * Created by roffee on 2017/9/22 08:47.
 * Contact with 460545614@qq.com
 */
public class XCamera extends XCameraBase<XCamera>{
    private Camera camera;
    private SparseBooleanArray cameraIdSparseArray;

    public XCamera(Activity activity, TextureView textureView) {
        super(activity, textureView);
    }
    public XCamera(Activity activity, SurfaceView surfaceView) {
        super(activity, surfaceView);
    }

    @Override
    protected void preOpen() {
        if (!isOk) return;
        if(viewType == CameraMode.ViewType.TextureView) openWithTextureView();
        else openWithSurfaceView();
    }
    @Override
    protected void openCamera(int width, int height) {
        super.openCamera(width, height);
        if(viewType == CameraMode.ViewType.TextureView){
            starPriview();
            cofigPriview(width, height);
        }
    }
    @Override
    protected void closeCamera() {
        super.closeCamera();
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if(viewType == CameraMode.ViewType.TextureView) closeCamera();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!isOk) return;
        if(!isClose){
            if (viewType == CameraMode.ViewType.TextureView) openWithTextureView();
//            else openWithSurfaceView();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeCamera();
        if(cameraIdSparseArray != null){
            cameraIdSparseArray.clear();
            cameraIdSparseArray = null;
        }
    }
    @Override
    public void switchCamera(){
        if (!isOk) return;
        closeCamera();
        isFaceFront = !isFaceFront;
        if (viewType == CameraMode.ViewType.TextureView) openWithTextureView();
        else starPriview();
        super.switchCamera();
    }
    private void openWithTextureView(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,   WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//            textureView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if(textureView.isAvailable()) openCamera(textureView.getWidth(), textureView.getHeight());
        else textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                starPriview();
                cofigPriview(textureView.getWidth(), textureView.getHeight());
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
                cofigPriview(width, height);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                closeCamera();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }
    private void openWithSurfaceView(){
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                starPriview();
//                cofigPriview(textureView.getWidth(), textureView.getHeight());
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
//                cofigPriview(width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                closeCamera();
            }
        });
    }
    private void starPriview(){
        if(ToolsKit.isEmpty(cameraIdSparseArray)){
            cameraIdSparseArray = new SparseBooleanArray();
            int numberOfCameras = Camera.getNumberOfCameras();
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            for(int i = 0; i < numberOfCameras; i++){
                getCameraInfo(i, cameraInfo);
                if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                    cameraIdSparseArray.append(Camera.CameraInfo.CAMERA_FACING_BACK, true);
                }else if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                    cameraIdSparseArray.append(Camera.CameraInfo.CAMERA_FACING_FRONT, true);
                }
            }
        }
        if(isFaceFront && cameraIdSparseArray.get(Camera.CameraInfo.CAMERA_FACING_FRONT)) camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        else if(cameraIdSparseArray.get(Camera.CameraInfo.CAMERA_FACING_BACK)) camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        else camera = open();
        try {
            if(viewType == CameraMode.ViewType.TextureView){
                textureView.setKeepScreenOn(true);
                camera.setPreviewTexture(textureView.getSurfaceTexture());
            }else{
                surfaceView.getHolder().setKeepScreenOn(true);
                surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                cofigPriview(surfaceView.getWidth(), surfaceView.getHeight());
                camera.setPreviewDisplay(surfaceView.getHolder());
            }
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void cofigPriview(int width, int height){
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);//isFlash ? "on" : "off");
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//        parameters.setPreviewFormat(imageFormatType);

//        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
//        Camera.Size optionSize = getOptimalPreviewSize(sizeList, width, height);
////        parameters.setPictureSize(optionSize.width, optionSize.height);
//        parameters.setPreviewSize(optionSize.width, optionSize.height);
        camera.setParameters(parameters);

        int rotation = activity.getResources().getConfiguration().orientation;
        if (rotation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
//            if(viewType == CameraMode.ViewType.TextureView) textureView.setRotation(90.0f);
        } else{
            camera.setDisplayOrientation(0);
//            if(viewType == CameraMode.ViewType.TextureView) textureView.setRotation(0.0f);
        }
//        configTransform(rotation, optionSize, width, height);
    }
    private void configTransform(int rotation, Camera.Size optionSize, int width, int height){
        if(viewType != CameraMode.ViewType.TextureView) return;
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, width, height);
        RectF bufferRect = new RectF(0, 0, optionSize.width, optionSize.height);
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) height / optionSize.height,
                    (float) width / optionSize.width);
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
