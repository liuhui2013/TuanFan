package com.ro.xdroid.media.image.picker;

import android.app.Activity;
import android.content.Intent;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.view.dialog.ToastKit;

import java.util.ArrayList;

/**
 * Created by roffee on 2017/9/7 09:25.
 * Contact with 460545614@qq.com
 */
public class PhotoPicker {
    public static final int REQUEST_CODE_PICK = 900;
    public static final int REQUEST_CODE_PHOTO = 901;
    public static final int REQUEST_CODE_PREVIEW = 902;
    public static final int RESULT_CODE_ITEMS = ImagePicker.RESULT_CODE_ITEMS;
    public static final int RESULT_CODE_BACK = ImagePicker.RESULT_CODE_BACK;
    public static final String EXTRA_RESULT_ITEMS = ImagePicker.EXTRA_RESULT_ITEMS;

    public enum CropStyle {
        Rectrangle, Circle;

        public CropImageView.Style getCropStyle(){
            switch (this){
                case Circle: return CropImageView.Style.CIRCLE;
                default: return CropImageView.Style.RECTANGLE;
            }
        }
    }

    private Activity activity;
    private ImagePicker imagePicker;
//    private int requestCode = REQUEST_CODE_SELECT;
//    private boolean isMultiSelect = true;
//    private int multiSelectNum = 9;

    public static PhotoPicker getInst(Activity activity){
        return new PhotoPicker(activity);
    }
    public PhotoPicker(Activity activity){
        this.activity = activity;
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PickerConfig());
    }
    public PhotoPicker isMultiSelect(boolean isMultiSelect){
        imagePicker.setMultiMode(isMultiSelect);
        return this;
    }
    public PhotoPicker setMultiSelectNumber(int multiSelectNum){
        if(multiSelectNum > 15){
            multiSelectNum = 15;
            ToastKit.show("多选不能超过15张");
        }
        if(multiSelectNum < 2){
            imagePicker.setMultiMode(false);
        }else{
            imagePicker.setMultiMode(true);
        }
        imagePicker.setSelectLimit(multiSelectNum);
        return this;
    }
    public PhotoPicker isShowCamera(boolean isShowCamera){
        imagePicker.setShowCamera(isShowCamera);
        return this;
    }
    public PhotoPicker isCropPhoto(boolean isCropPhoto){
        imagePicker.setCrop(isCropPhoto);
        return this;
    }
    public PhotoPicker isSaveRectangle(boolean isSaveRectangle){
        imagePicker.setSaveRectangle(isSaveRectangle);
        return this;
    }
    public PhotoPicker setCropStyle(CropStyle cropStyle){
        if(cropStyle != null) imagePicker.setStyle(cropStyle.getCropStyle());
        return this;
    }
    public PhotoPicker setCropRectangleSize(int width, int height){
        if(width < 0 || width > activity.getResources().getDisplayMetrics().widthPixels ||
                height < 0 || height > activity.getResources().getDisplayMetrics().heightPixels) return this;
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
        return this;
    }
    public PhotoPicker setCropCircleRadius(int radius){
        if(radius < 0 || radius*2 > activity.getResources().getDisplayMetrics().widthPixels) return this;
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        imagePicker.setFocusWidth(radius*2);
        imagePicker.setFocusHeight(radius*2);
        return this;
    }
    public PhotoPicker setPhotoExportSize(int width, int height){
        if(width < 0 || height < 0) return this;
        imagePicker.setOutPutX(width);
        imagePicker.setOutPutY(height);
        return this;
    }
//    public PhotoPicker setRequestCode(int requestCode){
//        this.requestCode = requestCode;
//        return this;
//    }
    public PhotoPicker takePicker(ArrayList<PhotoItem> selectedPhotos){
        Intent intent = new Intent(activity, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, selectedPhotos);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK);
        return this;
    }
    /**
     * receive makePicker selected photo
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if (resultCode == PhotoPicker.RESULT_CODE_ITEMS) {
     if (data != null && requestCode == PhotoPicker.REQUEST_CODE_PICK) {
     ArrayList<PhotoItem> selectedPhotos = (ArrayList<PhotoItem>) data.getSerializableExtra(PhotoPicker.EXTRA_RESULT_ITEMS);
     } else {
     ToastKit.show("没有数据");
     }
     }
     }
     */

    public PhotoPicker takePhoto(){
        Intent intent = new Intent(activity, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        activity.startActivityForResult(intent, REQUEST_CODE_PHOTO);
        return this;
    }
    /**
     *receive takePhoto&preview @del photo

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if (resultCode == PhotoPicker.RESULT_CODE_ITEMS) {
     //添加图片返回
     if (data != null && requestCode == PhotoPicker.REQUEST_CODE_PHOTO) {
     ArrayList<PhotoItem> photos = (ArrayList<PhotoItem>) data.getSerializableExtra(PhotoPicker.EXTRA_RESULT_ITEMS);
     }
     } else if (resultCode == PhotoPicker.RESULT_CODE_BACK) {
     //预览图片返回
     if (data != null && requestCode == PhotoPicker.REQUEST_CODE_PREVIEW) {
     ArrayList<PhotoItem> photos = (ArrayList<PhotoItem>) data.getSerializableExtra(PhotoPicker.EXTRA_IMAGE_ITEMS);
     }
     }
     }
     */


    public static void previewPhoto(Activity activity, ArrayList<PhotoItem> previewphotos){
        if(ToolsKit.isEmpty(previewphotos)){
            ToastKit.show("要预览的图片列表为空");
            return;
        }
        Intent intentPreview = new Intent(activity, ImagePreviewDelActivity.class);
        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, previewphotos);
        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
        activity.startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
    }


    //单/多图浏览，支持大图、缩放
    public static void imagePreview(Activity activity, String filePath){
        if(ToolsKit.isEmpty(filePath)){
            ToastKit.show("图片路径为空");
            return;
        }
        PhotoItem photoItem = new PhotoItem();
        photoItem.path = filePath;
        ArrayList<PhotoItem> photoItems = new ArrayList<>();
        photoItems.add(photoItem);
        imagePreview(activity, photoItems);
    }
    public static void imagePreview(Activity activity, String... filePaths){
        if(ToolsKit.isEmpty(filePaths)){
            ToastKit.show("图片路径为空");
            return;
        }
        ArrayList<PhotoItem> photoItems = new ArrayList<>();
        for(String s : filePaths){
            PhotoItem photoItem = new PhotoItem();
            photoItem.path = s;
            photoItems.add(photoItem);
        }
        imagePreview(activity, photoItems);
    }
    public static void imagePreview(Activity activity, ArrayList<PhotoItem> imageItems){
        if(ToolsKit.isEmpty(imageItems)){
            ToastKit.show("图片列表为空");
            return;
        }
        PhotoPicker.previewPhoto(activity, imageItems);
    }
}
