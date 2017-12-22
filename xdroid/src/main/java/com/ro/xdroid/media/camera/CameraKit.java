package com.ro.xdroid.media.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.SurfaceView;
import android.view.TextureView;

/**
 * Created by roffee on 2017/9/21 16:47.
 * Contact with 460545614@qq.com
 */
public class CameraKit {
    public static XCameraBase makeCamera(Activity activity, TextureView textureView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) return new XCamera2(activity, textureView);
        else return new XCamera(activity, textureView);
    }

    public static XCameraBase makeCamera(Activity activity, SurfaceView surfaceView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) return new XCamera2(activity, surfaceView);
        else return new XCamera(activity, surfaceView);
    }

    public static boolean hasCamera(Context context){
        if(context == null) return false;
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
