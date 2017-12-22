package com.ro.xdroid.net.ok.convert;

import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.utils.HttpUtils;
import com.ro.xdroid.kit.FileKit;
import com.ro.xdroid.net.ok.OkMode;
import com.ro.xdroid.net.ok.XOk;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by roffee on 2017/8/15 09:23.
 * Contact with 460545614@qq.com
 */
public class FileConvert implements Converter<File> {
    private static final int DL_BUFFER_LEN = 8*1024;
    private static final int DL_PROGRESS_INTERVAL = 20;
    private static final int DL_PROGRESS_INTERVAL_MINI = 1024*512;
    public static int convertControl;

    private XOk xOk;
    private String folder;                  //目标文件存储的文件夹路径
    private String fileName;                //目标文件存储的文件名
    private String tempFileName;           //下载为完成时临时文件
    private Callback<File> callback;        //下载回调
    private boolean isCover;

    public FileConvert() {
        this(null);
    }

    public FileConvert(String fileName) {
        this(OkMode.LoadMode.DLFolderDir, fileName, null);
    }

    public FileConvert(String folder, String fileName, String tempFileName) {
        this(null, OkMode.LoadMode.DLFolderDir, fileName, null, false);
    }
    public FileConvert(XOk xOk, String folder, String fileName, String tempFileName, boolean isCover) {
        this.xOk = xOk;
        this.isCover = isCover;
        convertControl = 0;
        if (TextUtils.isEmpty(folder)){ this.folder = OkMode.LoadMode.DLFolderDir;}
        else{this.folder = folder;}
        FileKit.makeDir(folder);
        this.tempFileName = tempFileName;
        this.fileName = fileName;
    }

    public void setCallback(Callback<File> callback) {
        this.callback = callback;
    }
    public static void setConvertControl(int control) {
        convertControl = control;
    }
    @Override
    public File convertResponse(Response response) throws Throwable {
        if(convertControl != OkMode.LoadMode.DL_CONVERT_NONE){
//                onError(com.lzy.okgo.model.Response.error(false, null, response, TTException.getException(convertControl)));
            throw getThrowable();
        }
        return disposeDLFile(response);
    }
    private void onProgress(final Progress progress) {
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.downloadProgress(progress);   //进度回调的方法
            }
        });
    }
    private void onError(final com.lzy.okgo.model.Response response) {
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onError(response);   //进度回调的方法
            }
        });
    }
    private void postDelete() {
        OkGo.getInstance().getDelivery().postDelayed(new Runnable() {
            @Override
            public void run() {
                FileKit.deleteFile(folder + fileName);
                FileKit.deleteFile(folder + tempFileName);
            }
        }, 1000);
    }
    private File disposeDLFile(Response response) throws Throwable {
        File tmpFile = null;
        InputStream bodyStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            String prefix = null, suffix;
            String url = response.request().url().toString();
            if (TextUtils.isEmpty(fileName)) fileName = HttpUtils.getNetFileName(response, url);
            File destFile = new File(folder, fileName);
            if(isCover){
                if(destFile.exists() && destFile.isFile()){
                    destFile.delete();
                }
            }else{
                if(destFile.exists() && destFile.isFile()){
                    int lastindex = fileName.lastIndexOf(".");
                    long now = System.currentTimeMillis();
                    if(lastindex > 0){
                        prefix = fileName.substring(0, lastindex);
                        suffix = fileName.substring(lastindex + 1);
                        fileName = prefix + "_" + now + "." + suffix;
                    }else{
                        fileName += now;
                    }
                    destFile = new File(folder, fileName);
                }
            }
            if (TextUtils.isEmpty(tempFileName)){
//                tempFileName = prefix + "_" + now +  OkMode.LoadMode.DLTempSuffix;
                tempFileName = OkMode.LoadMode.getDestTempFileName(fileName);
            }

            tmpFile = new File(folder, tempFileName);
            if(!tmpFile.exists()){tmpFile.createNewFile();}

            ResponseBody body = response.body();
            if (body == null) return null;

            bodyStream = body.byteStream();
            Progress progress = new Progress();
            progress.folder = folder;
            progress.fileName = fileName;
            progress.filePath = folder + fileName;
            progress.extra2 = tempFileName;
            progress.extra3 = tmpFile.getAbsolutePath();
            progress.status = Progress.LOADING;
            progress.url = url;
            progress.tag = url;

//            FileWriter fileWriter = new FileWriter();
            randomAccessFile = new RandomAccessFile(tmpFile, "rw");
            long startPos = randomAccessFile.length();
            randomAccessFile.seek(startPos);
            progress.totalSize = body.contentLength() + startPos;
            progress.currentSize = startPos;

            if(xOk != null){
                xOk.ttResponse.progress.isSetStable = true;
                xOk.ttResponse.progress.folder = folder;//progress.folder;
                xOk.ttResponse.progress.filePath = progress.filePath;
                xOk.ttResponse.progress.fileName = progress.fileName;
                xOk.ttResponse.progress.tempFileName = tempFileName;
                xOk.ttResponse.progress.tempFilePath = tmpFile.getAbsolutePath();
                xOk.ttResponse.progress.totalSize = progress.totalSize;
                xOk.ttResponse.progress.status = Progress.LOADING;
            }

            byte[] buffer = new byte[DL_BUFFER_LEN];
            long len, interval = 0, incLen = 0;
            while ((len = bodyStream.read(buffer)) != -1) {
                if(convertControl != OkMode.LoadMode.DL_CONVERT_NONE){
                    break;
                }
                randomAccessFile.write(buffer, 0, (int)len);
                if (callback == null) continue;
                incLen += len;
                if(progress.totalSize > DL_PROGRESS_INTERVAL_MINI){
                    if(interval % DL_PROGRESS_INTERVAL == 0){
                        Progress.changeProgress(progress, incLen, new Progress.Action() {
                            @Override
                            public void call(Progress progress) {
                                onProgress(progress);
                            }
                        });
                        incLen = 0;
                    }
                }else{
                    Progress.changeProgress(progress, incLen, new Progress.Action() {
                        @Override
                        public void call(Progress progress) {
                            onProgress(progress);
                        }
                    });
                    incLen = 0;
                }
                interval++;
            }
            if(convertControl != OkMode.LoadMode.DL_CONVERT_NONE){
                progress.status = Progress.PAUSE;
                Progress.changeProgress(progress, incLen, new Progress.Action() {
                    @Override
                    public void call(Progress progress) {
                        onProgress(progress);
                    }
                });
                throw getThrowable();
            }
//            IOUtils.delFileOrFolder(destFile);
            tmpFile.renameTo(destFile);
            progress.status = Progress.FINISH;
            progress.extra1 = true;
            if(progress.totalSize > DL_PROGRESS_INTERVAL_MINI && progress.currentSize < progress.totalSize){
                Progress.changeProgress(progress, incLen, new Progress.Action() {
                    @Override
                    public void call(Progress progress) {
                        onProgress(progress);
                    }
                });
            }

            return destFile;
        } finally {
            if(bodyStream != null){
                bodyStream.close();
            }
            if(randomAccessFile != null){
                randomAccessFile.close();
            }
        }
    }
    public Throwable getThrowable(){
        if(convertControl == OkMode.LoadMode.DL_CONVERT_PAUSE){
            return  new Throwable("暂停下载");
        }
//        else if(convertControl == OkMode.LoadMode.DL_CONVERT_DELETE){
//            return  new Throwable("删除下载");
//        }
        return  new Throwable("停止下载");
    }
}
