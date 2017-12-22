package com.ro.xdroid.media.image.loader.transformations;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;

import com.facebook.imagepipeline.request.BasePostprocessor;
import com.ro.xdroid.R;
import com.ro.xdroid.kit.ToolsKit;

import jp.wasabeef.fresco.processors.BlurPostprocessor;
import jp.wasabeef.fresco.processors.ColorFilterPostprocessor;
import jp.wasabeef.fresco.processors.GrayscalePostprocessor;
import jp.wasabeef.fresco.processors.MaskPostprocessor;
import jp.wasabeef.fresco.processors.gpu.BrightnessFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.ContrastFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.InvertFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.KuawaharaFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.PixelationFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.SepiaFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.SwirlFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.ToonFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.VignetteFilterPostprocessor;
import jp.wasabeef.glide.transformations.BitmapTransformation;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

/**
 * Created by roffee on 2017/9/20 16:13.
 * Contact with 460545614@qq.com
 */
public class Transformations {
    public enum TransType{//支持处理图像图形变换的类型
        NONE,
        TransCrop,                      //裁剪处理
        TransCircle,                    //圆图处理
        TransSquare,                    //正方形处理
        TransRoundedCorners,           //圆角处理
        TransGrayscale,                 //灰度处理
        TransBlur,                      //高斯模糊处理
        TransMask,                      //马赛克处理
//        TransCombine,

        TransColorFilter,               //颜色
        TransBrightnessFilter,         //亮度
        TransContrastFilter,            //对比度
//        TransGpuFilter,                 //gpu
        TransInvertFilter,              //反转
        TransKuwaharaFilter,            //沙漠
        TransPixelationFilter,          //像素
        TransSepiaFilter,                //深度
        TransSketchFilter,               //素描
        TransSwirlFilter,                //旋涡
        TransToonFilter,                 //木心
        TransVignetteFilter,            //晕映
    }

    public static BitmapTransformation getGlideTransformation(TransType transType, Object... args){
        if(transType == null){return null;}
        BitmapTransformation bitmapTransformation = null;
        try {
            if(transType == TransType.TransCrop) {//裁剪处理
                if(!ToolsKit.isEmpty(args) && args.length == 3){
                    bitmapTransformation = new CropTransformation((int)args[0], (int)args[1], (CropTransformation.CropType)args[2]);
                }else{
                    bitmapTransformation = new CropTransformation(100, 100, CropTransformation.CropType.CENTER);
                }
            }else if(transType == TransType.TransCircle){//圆形处理
                bitmapTransformation = new CropCircleTransformation();
            } else if(transType == TransType.TransSquare){
                bitmapTransformation = new CropSquareTransformation();
            }else if (transType == TransType.TransRoundedCorners){//圆角处理
                if(!ToolsKit.isEmpty(args) && args.length == 3){//设置提供参数
                    bitmapTransformation = new RoundedCornersTransformation((int)args[0], (int)args[1], (RoundedCornersTransformation.CornerType)args[2]);
                }else{//设置默认参数
                    bitmapTransformation = new RoundedCornersTransformation(24, 0, RoundedCornersTransformation.CornerType.ALL);
                }
            }else if(transType == TransType.TransGrayscale){
                bitmapTransformation = new GrayscaleTransformation();
            }else if(transType == TransType.TransBlur){//高斯处理
                if(!ToolsKit.isEmpty(args) && args.length == 2){//设置提供参数
                    bitmapTransformation = new BlurTransformation((int)args[0], (int)args[1]);
                }else{//设置默认参数
                    bitmapTransformation = new BlurTransformation();
                }
            }else if(transType == TransType.TransMask){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    bitmapTransformation = new MaskTransformation((int)args[0]);
                }else{
                    bitmapTransformation = new MaskTransformation(99);
                }
            }
            //filter
            else if(transType == TransType.TransColorFilter){//颜色过滤
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    bitmapTransformation = new ColorFilterTransformation((int)args[0]);
                }else{
                    bitmapTransformation = new ColorFilterTransformation(Color.parseColor("#ffffff"));
                }
            }else if(transType == TransType.TransBrightnessFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    bitmapTransformation = new BrightnessFilterTransformation((float)args[0]);
                }else{
                    bitmapTransformation = new BrightnessFilterTransformation(0.5f);
                }
            }else if(transType == TransType.TransContrastFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    bitmapTransformation = new ContrastFilterTransformation((float)args[0]);
                }else{
                    bitmapTransformation = new ContrastFilterTransformation();
                }
            }
//            else if(transType == TransType.TransGpuFilter){
//                if(!ToolsKit.isEmpty(args) && args.length == 1){
//                    bitmapTransformation = new GPUFilterTransformation((GPUImageFilter)args[0]);
//                }
//            }
            else if(transType == TransType.TransInvertFilter){
                bitmapTransformation = new InvertFilterTransformation();
            }else if(transType == TransType.TransKuwaharaFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    bitmapTransformation = new KuwaharaFilterTransformation((int)args[0]);
                }else{
                    bitmapTransformation = new ColorFilterTransformation(25);
                }
            }else if(transType == TransType.TransPixelationFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    bitmapTransformation = new PixelationFilterTransformation((float)args[0]);
                }else{
                    bitmapTransformation = new ColorFilterTransformation(20);
                }
            }else if(transType == TransType.TransSepiaFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    bitmapTransformation = new SepiaFilterTransformation((float)args[0]);
                }else{
                    bitmapTransformation = new SepiaFilterTransformation();
                }
            }else if(transType == TransType.TransSwirlFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 3){
                    bitmapTransformation = new SwirlFilterTransformation((float)args[0], (float)args[1], (PointF)args[2]);
                }else{
                    bitmapTransformation = new SwirlFilterTransformation();
                }
            }else if(transType == TransType.TransToonFilter){
                    if(!ToolsKit.isEmpty(args) && args.length == 2){
                        bitmapTransformation = new ToonFilterTransformation((float)args[0], (float)args[1]);
                }else{
                    bitmapTransformation = new ToonFilterTransformation();
                }
            }else if(transType == TransType.TransVignetteFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 4){
                    bitmapTransformation = new VignetteFilterTransformation((PointF)args[0], (float[])args[1], (float)args[2], (float)args[3] );
                }else{
                    bitmapTransformation = new VignetteFilterTransformation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmapTransformation;
    }
    public static BasePostprocessor getFrescoPostprocessor(Context context, TransType transType, Object... args){
        if(context == null || transType == null){return null;}
        BasePostprocessor basePostprocessor = null;
        try {
            if(transType == TransType.TransGrayscale){
                basePostprocessor = new GrayscalePostprocessor();
            }else if(transType == TransType.TransBlur){//高斯处理
                if(!ToolsKit.isEmpty(args) && args.length == 2){//设置提供参数
                    basePostprocessor = new BlurPostprocessor(context, (int)args[0], (int)args[1]);
                }else{//设置默认参数
                    basePostprocessor = new BlurPostprocessor(context);
                }
            }else if(transType == TransType.TransMask){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    basePostprocessor = new MaskPostprocessor(context, (int)args[0]);
                }else{
                    basePostprocessor = new MaskPostprocessor(context, R.mipmap.ic_failed_face);
                }
            }
            //filter
            else if(transType == TransType.TransColorFilter){//颜色过滤
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    basePostprocessor = new ColorFilterPostprocessor((int)args[0]);
                }else{
                    basePostprocessor = new ColorFilterPostprocessor(Color.parseColor("#ffffff"));
                }
            }else if(transType == TransType.TransBrightnessFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    basePostprocessor = new BrightnessFilterPostprocessor(context, (float)args[0]);
                }else{
                    basePostprocessor = new BrightnessFilterPostprocessor(context, 0.5f);
                }
            }else if(transType == TransType.TransContrastFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    basePostprocessor = new ContrastFilterPostprocessor(context, (float)args[0]);
                }else{
                    basePostprocessor = new ContrastFilterPostprocessor(context);
                }
            }else if(transType == TransType.TransInvertFilter){
                basePostprocessor = new InvertFilterPostprocessor(context);
            }else if(transType == TransType.TransKuwaharaFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    basePostprocessor = new KuawaharaFilterPostprocessor(context, (int)args[0]);
                }else{
                    basePostprocessor = new KuawaharaFilterPostprocessor(context);
                }
            }else if(transType == TransType.TransPixelationFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    basePostprocessor = new PixelationFilterPostprocessor(context, (float)args[0]);
                }else{
                    basePostprocessor = new PixelationFilterPostprocessor(context, 20);
                }
            }else if(transType == TransType.TransSepiaFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 1){
                    basePostprocessor = new SepiaFilterPostprocessor(context, (float)args[0]);
                }else{
                    basePostprocessor = new SepiaFilterPostprocessor(context);
                }
            }else if(transType == TransType.TransSwirlFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 3){
                    basePostprocessor = new SwirlFilterPostprocessor(context, (float)args[0], (float)args[1], (PointF)args[2]);
                }else{
                    basePostprocessor = new SwirlFilterPostprocessor(context);
                }
            }else if(transType == TransType.TransToonFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 2){
                    basePostprocessor = new ToonFilterPostprocessor(context, (float)args[0], (float)args[1]);
                }else{
                    basePostprocessor = new ToonFilterPostprocessor(context);
                }
            }else if(transType == TransType.TransVignetteFilter){
                if(!ToolsKit.isEmpty(args) && args.length == 4){
                    basePostprocessor = new VignetteFilterPostprocessor(context, (PointF)args[0], (float[])args[1], (float)args[2], (float)args[3] );
                }else{
                    basePostprocessor = new VignetteFilterPostprocessor(context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return basePostprocessor;
    }
}
