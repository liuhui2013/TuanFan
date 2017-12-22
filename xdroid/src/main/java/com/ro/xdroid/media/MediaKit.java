package com.ro.xdroid.media;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.ro.xdroid.view.dialog.ToastKit;

/**
 * Created by roffee on 2017/9/7 08:57.
 * Contact with 460545614@qq.com
 */
public class MediaKit {
    public static final int REQUEST_CODE_PICK_VIDEO = 100;
    public static final int REQUEST_CODE_PICK_AUDIO = 101;
    public static final int REQUEST_CODE_PICK_IMAGE = 102;

    public static void videoSysPicker(Activity activity) {
        if(activity == null){
            ToastKit.show("参数为空");
            return;
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("video/*");
        }
        activity.startActivityForResult(Intent.createChooser(intent, "选择视频文件"), REQUEST_CODE_PICK_VIDEO);
    }

    public static void audioSysPicker(Activity activity) {
        if(activity == null){
            ToastKit.show("参数为空");
            return;
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
        }
        activity.startActivityForResult(Intent.createChooser(intent, "选择音频文件："), REQUEST_CODE_PICK_AUDIO);
    }

    public static void imageSysPicker(Activity activity) {
        if(activity == null){
            ToastKit.show("参数为空");
            return;
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
        }
        activity.startActivityForResult(Intent.createChooser(intent, "选择图片文件："), REQUEST_CODE_PICK_IMAGE);
    }
    /**
     * receive systerm pick files
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if(requestCode == MediaKit.REQUEST_CODE_PICK_VIDEO){
     if (resultCode == Activity.RESULT_OK) {
     String selectedFilepath = UriKit.getPath(this, data.getData());
     }
     }else if(requestCode == MediaKit.REQUEST_CODE_PICK_AUDIO){
     if (resultCode == Activity.RESULT_OK) {
     String selectedFilepath = UriKit.getPath(this, data.getData());
     }
     }
     }
     */
    private void test(){

    }
}
