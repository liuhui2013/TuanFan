package com.ro.xdroid.media.image.loader;

import com.facebook.drawee.drawable.ScalingUtils;

/**
 * Created by roffee on 2017/8/8 11:09.
 * Contact with 460545614@qq.com
 */
public class Mode {
    public static final int FadeDuration = 300; //ms

    public enum ScaleType{
        None,
        Center,
        CenterCrop,
        CenterInside,
        FocusCrop,
        CircleCrop,
        FitXY,
        FitStart,
        FitEnd,
        FitCenter,
        FitBottomStart;

        public ScalingUtils.ScaleType getScaleType(){
            switch (this){
                case None: return null;
                case Center: return ScalingUtils.ScaleType.CENTER;
                case CenterCrop: return ScalingUtils.ScaleType.CENTER_CROP;
                case CenterInside: return ScalingUtils.ScaleType.CENTER_INSIDE;
                case FocusCrop: return ScalingUtils.ScaleType.FOCUS_CROP;
                case FitXY: return ScalingUtils.ScaleType.FIT_XY;
                case FitStart: return ScalingUtils.ScaleType.FIT_START;
                case FitEnd: return ScalingUtils.ScaleType.FIT_END;
                case FitCenter: return ScalingUtils.ScaleType.FIT_CENTER;
                case FitBottomStart: return ScalingUtils.ScaleType.FIT_BOTTOM_START;
                default:return ScalingUtils.ScaleType.CENTER_CROP;
            }
        }
    }
    public enum TranscodeType{
        Drawable,
        Bitmap
    }
    public enum WrapType{
        HeightWrapAndWidthMatchInScreenF1,
        HeightWrapAndWidthMatchInScreenF2,
        HeightWrapAndWidthMatchInScreenF3,
        HeightWrapAndWidthMatchInScreenF4,
        HeightWrapAndWidthMatchInScreenFDefine,
        WidthWrapAndHeightMatchInScreenF1,
        WidthWrapAndHeightMatchInScreenF2,
        WidthWrapAndHeightMatchInScreenF3,
        WidthWrapAndHeightMatchInScreenF4,
        WidthWrapAndHeightMatchInScreenFDefine,
        BothWrap
    }
}
