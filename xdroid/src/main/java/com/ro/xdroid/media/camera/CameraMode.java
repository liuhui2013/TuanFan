package com.ro.xdroid.media.camera;

/**
 * Created by roffee on 2017/9/22 09:17.
 * Contact with 460545614@qq.com
 */
public class CameraMode {
    public enum ViewType{
        TextureView,
        SurfaceView
    }
//    static {
//        ORIENTATIONS.append(Surface.ROTATION_0, 90);
//        ORIENTATIONS.append(Surface.ROTATION_90, 0);
//        ORIENTATIONS.append(Surface.ROTATION_180, 270);
//        ORIENTATIONS.append(Surface.ROTATION_270, 180);
//    }
    public static final int[] OrientationsArray = {90, 0, 270, 180};
    public static class FaceSupports{
//        public static final int FaceNone = 1;
//        public static final int FaceFront = 1<<1;
//        public static final int FaceBack = 1<<2;
//        public static final int Face3RD = 1<<3;
        public static final int FaceNone = 0;
        public static final int FaceFront = 1;
        public static final int FaceBack = 2;
        public static final int Face3RD = 3;
    }
}
