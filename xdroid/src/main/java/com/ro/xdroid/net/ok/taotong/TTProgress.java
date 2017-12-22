package com.ro.xdroid.net.ok.taotong;

import android.text.format.Formatter;

import com.ro.xdroid.mvp.XApp;

import java.io.Serializable;
import java.text.NumberFormat;

/**
 * Created by roffee on 2017/8/14 16:08.
 * Contact with 460545614@qq.com
 */
public class TTProgress implements Serializable{
    public static final int NONE = 0;         //无状态
    public static final int WAITING = 1;      //等待
    public static final int LOADING = 2;      //下载中
    public static final int PAUSE = 3;        //暂停
    public static final int ERROR = 4;        //错误
    public static final int FINISH = 5;       //完成

    public int status;
    public String folder;                       //保存文件夹
    public String filePath;                     //保存文件地址
    public String fileName;                     //保存的文件名
    public String tempFilePath;                 //保存临时文件地址
    public String tempFileName;                  //保存临时的文件名

    public float percent;                       //下载的进度，0-1
    public long totalSize;                      //总字节长度, byte
    public long loadSize;                       //本次下载的大小, byte
    public long speed;                           //网速，byte/s

    public boolean isSetStable;
    private NumberFormat numberFormat;

    public TTProgress(){
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
    }
    //format
    public String formatLoadSize(){
        return Formatter.formatFileSize(XApp.getContext(), loadSize);
    }
    public String formatTotalSize(){
        return Formatter.formatFileSize(XApp.getContext(), totalSize);
    }
    public String formatSpeed(){
        return Formatter.formatFileSize(XApp.getContext(), speed) + "/s";
    }
    public String formatPercent(){
        return numberFormat.format(percent);
    }
}
