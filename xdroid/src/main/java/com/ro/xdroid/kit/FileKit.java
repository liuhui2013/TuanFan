package com.ro.xdroid.kit;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.ro.xdroid.net.error.ErrorHandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by roffee on 2017/8/3 09:10.
 * Contact with 460545614@qq.com
 */
public class FileKit {
    private final static int FZ_KB = 1024;
    private final static int FZ_MB = 1024 * FZ_KB;
    private final static int FZ_GB = 1024 * FZ_MB;
    private final static int FZ_PB = 1024 * FZ_GB;

    public static class FileInfo {
        public long size;
        public String filePath;
        public String fileFullName;//文件全名,如"temp.mp3"
        public String fileName;//文件名,如"temp"
        public String mimeType;//文件格式,如"mp3"
    }
    /**
     * @getFileInfo: 获取文件信息
     */
    public static FileInfo getFileInfo(String filePath){
        if(com.ro.xdroid.kit.ToolsKit.isEmpty(filePath)){
            return null;
        }
        File file = new File(filePath);
        if(file == null || !file.exists() || !file.isFile()){
            return null;
        }
        if(file.length() <= 0){
            return null;
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.filePath = filePath;
        fileInfo.size = file.length();
        fileInfo.fileFullName = file.getName();
//        String[] temp = file.getName().split("\\.");//如果使用"."、"|"、"^"等字符做分隔符时，要写成s3.split("\\^")的格式
//        if(temp != null && temp.length == 2){
//            fileInfo.fileName = temp[0];
//            fileInfo.mimeType = temp[1];
//        }

        int lastindex = fileInfo.fileFullName.lastIndexOf(".");
        if(lastindex > 0){
            fileInfo.fileName = fileInfo.fileFullName.substring(0, lastindex);
            fileInfo.mimeType = fileInfo.fileFullName.substring(lastindex+1);
        }else{
            fileInfo.fileName = fileInfo.fileFullName;
            fileInfo.mimeType = "";
        }
        return fileInfo;
    }
    public static long getFileSize(String filePath){
        if(ToolsKit.isEmpty(filePath)){
            return 0;
        }
        long size = 0;
        File file = new File(filePath);
        if(file == null || !file.exists()){return 0;}
        if(file.isDirectory()){
            size = getDirSize(file);
        }else{
            size = file.length();
        }
        return size;
    }
    public static long getFileSize(File file){
        if(file == null || !file.exists()){ return 0;}
        long size = 0;
        if(file.isDirectory()){
            size = getDirSize(file);
        }else{
            size = file.length();
        }
        return size;
    }
    private static long getDirSize(File file){
        long size = 0;
        try {
            File[] files = file.listFiles();
            for(File f : files){
                if(f.isDirectory()){
                    size += getDirSize(f);
                }else{
                    size += f.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    public static void deleteFile(String filePath) {
        if(ToolsKit.isEmpty(filePath)){return;}
        File file = new File(filePath);
        file.setWritable(true);
		file.setExecutable(true);
        if(!file.exists() || !file.isFile()){return;}
        file.delete();
    }

    /**
     * @method deleteFiles 删除方法，这里只会删除某个文件夹文件，如果是传入的directory是个文件，将不做处理 。
     * @param directory
     */
    public static void deleteFiles(File directory) {
        if(directory == null || !directory.exists() || !directory.isDirectory()){
            return;
        }

        for (File child : directory.listFiles()) {
            if (child.isDirectory()) {
                deleteFiles(child);
            }
            child.delete();
        }
    }
    /**
     * @makeDir: 创建m目录
     */
    public static boolean makeDir(String dirPath){
        if(ToolsKit.isEmpty(dirPath)){
            return false;
        }
        File file = new File(dirPath);
        if(file.exists()){
            if(file.isDirectory()){return true;}
        }
        return file.mkdirs();
    }
    /**
     * 根据文件大小格式化单位
     * @param fileLength
     * @return
     */
    public static String formatSize(long fileLength) {
        StringBuilder sb = new StringBuilder();
        if (fileLength < FZ_KB) {
            sb.append(formatDouble(fileLength, 1)).append(" B");
        } else if (fileLength <= FZ_MB) {
            sb.append(formatDouble(fileLength, FZ_KB)).append(" KB");
        } else if (fileLength <= FZ_GB) {
            sb.append(formatDouble(fileLength, FZ_MB)).append(" MB");
        } else if (fileLength <= FZ_PB) {
            sb.append(formatDouble(fileLength, FZ_GB)).append(" GB");
        } else {
            sb.append(formatDouble(fileLength, FZ_PB)).append(" PB");
        }
        return sb.toString();
    }
    public static String formatDouble(long value, int divider) {
        double result = value * 1.0 / divider;
        return String.format(Locale.getDefault(), "%.2f", result);
    }
    public static String formatSpeed(double deltaSize, double deltaMillis) {
        double speed = deltaSize * 1000 / deltaMillis / FZ_KB;
        String result = String.format(Locale.getDefault(), "%.2f KB/s", speed);
        if ((int) speed > FZ_KB) {
            result = String.format(Locale.getDefault(), "%.2f MB/s", speed
                    / FZ_KB);
        }
        return result;
    }
    public static double getSpeed(double deltaSize, double deltaMillis) {
        double speed = deltaSize * 1000 / deltaMillis / FZ_KB;
        return speed;
    }
    public static String getSuffix(String str){
        if(ToolsKit.isEmpty(str)){return "";}
        int lastindex = str.lastIndexOf(".");
        if(lastindex > 0){
            return str.substring(lastindex + 1);
        }
        return "";
    }
    public static interface OnFileSaveListener{
        void onFileSaveListener(boolean isOk, String result);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean saveFile(Object source, String targetPath){
        if(source == null || ToolsKit.isEmpty(targetPath)) {
            return false;
        };

        int sourceType = 0;
        if(source instanceof Image) sourceType = 0;
        else if(source instanceof Bitmap) sourceType = 1;
        else { return false; }

        FileOutputStream fos = null; BufferedOutputStream bos = null;
        Bitmap bitmap = null; Image image = null;
        try {
            File file = new File(targetPath);
            if(!file.canWrite()) file.setWritable(true);
            if(sourceType == 0){
                image = (Image)source;
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                fos = new FileOutputStream(file);
                fos.write(bytes);
            }else{
                bitmap = (Bitmap)source;
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if(bitmap != null){
                bitmap.recycle();
                bitmap = null;
            }
            if(image != null){
                image.close();
                image = null;
            }
            try {
                if (fos != null) fos.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fos = null;
            bos = null;
        }
        return true;
    }
    public static void saveFile(Object source, String targetPath, OnFileSaveListener onFileSaveListener){
        if(source == null || ToolsKit.isEmpty(targetPath)) {
            if(onFileSaveListener != null) onFileSaveListener.onFileSaveListener(false, "参数为空");
        };

        int sourceType = 0;
        if(source instanceof Image) sourceType = 0;
        else if(source instanceof Bitmap) sourceType = 1;
        else { if(onFileSaveListener != null) onFileSaveListener.onFileSaveListener(false, "类型不支持"); }

        Observable.just(sourceType)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        FileOutputStream fos = null; BufferedOutputStream bos = null;
                        Bitmap bitmap = null; Image image = null;
                        try {
                            File file = new File(targetPath);
                            if(!file.canWrite()) file.setWritable(true);
                            if(integer == 0){
                                image = (Image)source;
                                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                                byte[] bytes = new byte[buffer.remaining()];
                                buffer.get(bytes);
                                fos = new FileOutputStream(file);
                                fos.write(bytes);
                            }else{
                                bitmap = (Bitmap)source;
                                fos = new FileOutputStream(file);
                                bos = new BufferedOutputStream(fos);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                            }
                            if(onFileSaveListener != null) onFileSaveListener.onFileSaveListener(true, targetPath);
                        } catch (Exception e){
                            e.printStackTrace();
                            if(onFileSaveListener != null) onFileSaveListener.onFileSaveListener(false, ErrorHandler.getError(e));
                        }finally {
                            if(bitmap != null){
                                bitmap.recycle();
                                bitmap = null;
                            }
                            if(image != null){
                                image.close();
                                image = null;
                            }
                            if (fos != null) fos.close();
                            if (bos != null) bos.close();
                            fos = null;
                            bos = null;
                        }
                    }
                });
    }
}
