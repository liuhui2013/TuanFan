package com.ro.xdroid.media.camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;

import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.FileKit;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.media.camera.view.AutoFitTextureView;
import com.ro.xdroid.view.dialog.ToastKit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by roffee on 2017/9/22 08:55.
 * Contact with 460545614@qq.com
 */
@TargetApi(21)
public class XCamera2 extends XCameraBase<XCamera2> {
    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;

//    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
//    static {
//        ORIENTATIONS.append(Surface.ROTATION_0, 90);
//        ORIENTATIONS.append(Surface.ROTATION_90, 0);
//        ORIENTATIONS.append(Surface.ROTATION_180, 270);
//        ORIENTATIONS.append(Surface.ROTATION_270, 180);
//    }


    private CameraManager cameraManager;
    //    private Map<Integer, String> cameraIdMap;
    private SparseArray<String> cameraIdSparseArray;
    private ImageReader imageReader;
    private Handler backgroundHandler;
    private HandlerThread backgroundHandlerThread;
    private Size previewSize;
    private Semaphore cameraLockSemaphore;
    private CameraDevice camera2Device;
    private CaptureRequest.Builder captureRequestBuild;
    private CaptureRequest captureRequest;
    private CameraCaptureSession camera2CaptureSession;
    private int sensorOrientation;
    private int state = STATE_PREVIEW;

    public XCamera2(Activity activity, TextureView textureView) {
        super(activity, textureView);
    }
    public XCamera2(Activity activity, SurfaceView surfaceView) {
        super(activity, surfaceView);
    }


    @Override
    protected void preOpen() {
        if (!isOk) return;
        cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        if(viewType == CameraMode.ViewType.TextureView) openWithTextureView();
        else openWithSurfaceView();
    }

    @Override
    protected void openCamera(int width, int height) {
        super.openCamera(width, height);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startBackgroundThread();
        setCameraOutputs(width, height);
        configTransform(width, height);

        try {
            cameraLockSemaphore = new Semaphore(1);
            if (!cameraLockSemaphore.tryAcquire(3000, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            cameraManager.openCamera(curCameraId, stateCallback, backgroundHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void closeCamera() {
        super.closeCamera();
        stopBackgroundThread();
        try {
            if(cameraLockSemaphore != null) {
                cameraLockSemaphore.acquire();
            }
            if (camera2CaptureSession != null) {
                camera2CaptureSession.close();
                camera2CaptureSession = null;
            }
            if (camera2Device != null) {
                camera2Device.close();
                camera2Device = null;
            }
            if(imageReader != null) {
                imageReader.close();
                imageReader = null;
            }
//            if(textureView != null){
//                textureView.setSurfaceTextureListener(null);
//            }
//            if(surfaceView != null){
//                surfaceView.getHolder().addCallback(null);
//            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            if(cameraLockSemaphore != null) {
                cameraLockSemaphore.release();
                cameraLockSemaphore = null;
            }
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        closeCamera();
    }
    @Override
    public void onResume(){
        super.onResume();
        if (!isOk) return;
        if(isClose){
            if (viewType == CameraMode.ViewType.TextureView) openWithTextureView();
            else openWithSurfaceView();
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        closeCamera();
        cameraManager = null;
        if(cameraIdSparseArray != null){
            cameraIdSparseArray.clear();
            cameraIdSparseArray = null;
        }
    }
    public void takePicture() {
        if(!isOk) return;
        try {
            // This is how to tell the camera to lock focus.
            captureRequestBuild.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            state = STATE_WAITING_LOCK;
            camera2CaptureSession.capture(captureRequestBuild.build(), captureCallback,
                    backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void switchCamera(){
        if(!isOk) return;
        closeCamera();
        isFaceFront = !isFaceFront;
        if (viewType == CameraMode.ViewType.TextureView) openWithTextureView();
        else openCamera(surfaceView.getWidth(), surfaceView.getHeight());
        super.switchCamera();
    }
    private void openWithTextureView(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,   WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//            textureView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        textureView.setKeepScreenOn(true);
        if(textureView.isAvailable()) openCamera(textureView.getWidth(), textureView.getHeight());
        else textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                openCamera(width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
                configTransform(width, height);
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
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                openCamera(surfaceView.getWidth(), surfaceView.getHeight());
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
//                configTransform(width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                closeCamera();
            }
        });
    }

    private void setCameraOutputs(int width, int height) {
        checkFaceSuppots();

        if(isFaceFront && !ToolsKit.isEmpty(cameraIdSparseArray.get(CameraMode.FaceSupports.FaceFront))) curLensFace = CameraMode.FaceSupports.FaceFront;
        else if(!ToolsKit.isEmpty(cameraIdSparseArray.get(CameraMode.FaceSupports.FaceBack))) curLensFace = CameraMode.FaceSupports.FaceBack;
        else if(!ToolsKit.isEmpty(cameraIdSparseArray.get(CameraMode.FaceSupports.Face3RD))) curLensFace = CameraMode.FaceSupports.Face3RD;
        else curLensFace = CameraMode.FaceSupports.FaceNone;
        curCameraId = cameraIdSparseArray.get(curLensFace);

        try {
            CameraCharacteristics characteristics  = cameraManager.getCameraCharacteristics(curCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if(map == null) return;

            Size largest = Collections.max(Arrays.asList(map.getOutputSizes(imageFormatType)), new CompareSizesByArea());
            imageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), imageFormatType, 2);
            imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    String path = XDroidConfig.DirCamera + System.currentTimeMillis() + ".jpg";
                    FileKit.saveFile(imageReader.acquireNextImage(), path,
                            new FileKit.OnFileSaveListener() {
                                @Override
                                public void onFileSaveListener(boolean isOk, String result) {
                                    if(isOk) ToastKit.showInUiThread(activity, "capture picture ok: " + path);
                                    else ToastKit.show(activity, "capture picture failed");
                                }
                            });
                }
            }, backgroundHandler);

            int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            //noinspection ConstantConditions
            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            boolean swappedDimensions = false;
            switch (displayRotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180:
                    if (sensorOrientation == 90 || sensorOrientation == 270) swappedDimensions = true;
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    if (sensorOrientation == 0 || sensorOrientation == 180) swappedDimensions = true;
                    break;
                default:
                    break;
            }

            Point point = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(point);
            int rotatedPreviewWidth = width;
            int rotatedPreviewHeight = height;
            int maxPreviewWidth = point.x;
            int maxPreviewHeight = point.y;
            if (swappedDimensions) {
                rotatedPreviewWidth = height;
                rotatedPreviewHeight = width;
                maxPreviewWidth = point.y;
                maxPreviewHeight = point.x;
            }
            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) maxPreviewWidth = MAX_PREVIEW_WIDTH;
            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT)  maxPreviewHeight = MAX_PREVIEW_HEIGHT;

            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                    maxPreviewHeight, largest);
            if(viewType == CameraMode.ViewType.TextureView){
                if(textureView instanceof AutoFitTextureView){
                    int orientation = activity.getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        ((AutoFitTextureView)textureView).setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
                    } else {
                        ((AutoFitTextureView)textureView).setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
                    }
                }
            }
            Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            isSupportFlash = available == null ? false : available;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void configTransform(int viewWidth, int viewHeight){
        if (previewSize == null) return;
        if(viewType != CameraMode.ViewType.TextureView) return;

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / previewSize.getHeight(),
                    (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }
    private void checkFaceSuppots(){
        if(!ToolsKit.isEmpty(cameraIdSparseArray) ) return;
        try {
            if(cameraIdSparseArray == null) cameraIdSparseArray = new SparseArray<>();
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT){
//                    FlagBitKit.addFlagBit(faceSupports, CameraMode.FaceSupports.FaceFront);
                    cameraIdSparseArray.append(CameraMode.FaceSupports.FaceFront, cameraId);
                }else if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
//                    FlagBitKit.addFlagBit(faceSupports, CameraMode.FaceSupports.FaceBack);
                    cameraIdSparseArray.append(CameraMode.FaceSupports.FaceBack, cameraId);
                }else if (facing != null && facing == CameraCharacteristics.LENS_FACING_EXTERNAL) {
//                    FlagBitKit.addFlagBit(faceSupports, CameraMode.FaceSupports.Face3RD);
                    cameraIdSparseArray.append(CameraMode.FaceSupports.Face3RD, cameraId);
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }
    private void startBackgroundThread() {
        if(backgroundHandler != null && backgroundHandlerThread != null) return;
        else stopBackgroundThread();

        backgroundHandlerThread = new HandlerThread("CameraBackground");
        backgroundHandlerThread.start();
        backgroundHandler = new Handler(backgroundHandlerThread.getLooper());
    }
    private void stopBackgroundThread() {
        try {
            if(backgroundHandlerThread != null){
                backgroundHandlerThread.quitSafely();
                backgroundHandlerThread.join();
                backgroundHandlerThread = null;
            }
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void createCameraPreviewSession() {
        if (null == camera2Device) return;
        try {
            Surface surface;
            if(viewType == CameraMode.ViewType.TextureView) {
                SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
                if(surfaceTexture == null) return;
                surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
                surface = new Surface(surfaceTexture);
            }else surface = surfaceView.getHolder().getSurface();
            if(surface == null) return;

            captureRequestBuild = camera2Device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuild.addTarget(surface);

//            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//            captureRequestBuild.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            camera2Device.createCaptureSession(Arrays.asList(surface, imageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            if(camera2Device == null) return;

                            camera2CaptureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                captureRequestBuild.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
                                setAutoFlash(captureRequestBuild);

                                // Finally, we start displaying the camera preview.
                                captureRequest = captureRequestBuild.build();
                                camera2CaptureSession.setRepeatingRequest(captureRequest, captureCallback, backgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                            ToastKit.showInUiThread(activity, "ConfigureFailed");
                        }
                    }, backgroundHandler
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (isSupportFlash) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }
    private void captureStillPicture() {
        if (camera2Device == null) return;
        try {
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    camera2Device.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback ccsCaptureCallback = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    unlockFocus();
                }
            };

            camera2CaptureSession.stopRepeating();
            camera2CaptureSession.capture(captureBuilder.build(), ccsCaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            captureRequestBuild.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            state = STATE_WAITING_PRECAPTURE;
            camera2CaptureSession.capture(captureRequestBuild.build(), captureCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private int getOrientation(int rotation) {
        return (CameraMode.OrientationsArray[rotation] + sensorOrientation + 270) % 360;
    }
    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            captureRequestBuild.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            setAutoFlash(captureRequestBuild);
            camera2CaptureSession.capture(captureRequestBuild.build(), captureCallback, backgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            state = STATE_PREVIEW;
            camera2CaptureSession.setRepeatingRequest(captureRequest, captureCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }

    }
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            cameraLockSemaphore.release();
            camera2Device = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraLockSemaphore.release();
            cameraDevice.close();
            camera2Device = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            cameraLockSemaphore.release();
            cameraDevice.close();
            camera2Device = null;
        }
    };
    private final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (state) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            state = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        state = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        state = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            process(result);
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }

        @Override
        public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
            super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
        }

        @Override
        public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
            super.onCaptureSequenceAborted(session, sequenceId);
        }

        @Override
        public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
            super.onCaptureBufferLost(session, request, target, frameNumber);
        }
    };
}
