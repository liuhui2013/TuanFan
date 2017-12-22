package com.ro.xdroid.net.upload.model;

import com.qiniu.android.http.ResponseInfo;
import com.ro.xdroid.XDroidConfig;

import java.io.File;

/**
 * Created by roffee on 2017/11/25 09:14.
 * Contact with 460545614@qq.com
 */
public class UploadMode {
    public enum BreakpointType{
        SupportBreakpoint,
        NoneBreakpoint
    }
    //任务源类型
    public enum SourceType{
        //需要上传
        File,
        //不需要上传，直接进入提交
        Text
    }
    public static class Constant{
        //工程路径
        public static final String BasePath = "/data/data/com.doubihai.taotong/";
        //七牛上传断点续传记录器路径
        public static final String QUploadRecoderPath = BasePath + "qupload/";

        //获取token ip root
        public static final String QGainTokenUrl = XDroidConfig.QUploadGainTokenUrl;
        //七牛上传结果提交ip root
        public static final String QUploadBaseUrl = XDroidConfig.QUploadBaseUrl;

        //ms--1 h存储服务token默认过期时间，单位ms
        public static final long TokenExpire = 3600*1000;
        //网络原因任务中断，实例等待重连超时时间；s
        public static final long TimerForNetFactorTaskInterruptOuttime = 3*60;
        //任务阶段异常后最大重试次数，如获取token、请求上传等
        public static final int MaxRetry = 1;
        //七牛上传器内部错误码增量，避免和内部错误码冲突
        public static final int QUploadManagerErrorIncr = -1000;
        //控制上传进度回调刷新的时间间隔，单位ms
        public static final int UploadProgressUpdateInterval = 500;
    }
    //执行进展阶段
    public static class TaskExecuteStep{
        //还未处理
        public static final int StepUploadNone = 0;
        //执行到上传部分
        public static final int StepUplodPart = 1;
        //执行到上传完毕
        public static final int StepUplodComplete = 2;
    }
    public static class FileInfo {
        //文件大小
        public double size;
        //文件路径
//        public String filePath;
        //文件全名,如"temp.mp3"
        public String fileFullName;
        //文件名,如"temp"
        public String fileName;
        //文件格式,如"mp3"
        public String mimeType;

        public static FileInfo getFileInfo(String filePath){
            if(filePath == null) return null;
            File file = new File(filePath);
            if(file == null || !file.exists() || !file.isFile()){
                return null;
            }
            if(file.length() <= 0){
                return null;
            }
            FileInfo fileInfo = new FileInfo();
//            fileInfo.filePath = filePath;
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
    }

    //错误码
    public static class ErrorCode {
        //正常
        public static final int EOk = 0;

        //上传任务组列表为空
        public static final int EGroupTasksIsEmpty = -1;
        //某项上传资源路径为空
        public static final int EGroupSomeItemSourcePathIsNull = -2;
        //任务上传结果提交参数出错
        public static final int EUploadResultCommitParamError = -3;
        //某项上传资源结果提交参数key为空
        public static final int EGroupSomeItemCommitParamKeyIsNull = -4;
        //某项上传资源不存在
        public static final int EGroupSomeItemSourceInexistence = -5;
        //已上传部分的qUploadKey没有设置，则无法续传
        public static final int EGroupUploadPartItemUploadKeyInexistence = -6;
        //处理开始执行任务项的索引出粗
        public static final int EGroupUpItemExecuteIndexError = -7;
        //上传某项任务时获取上传token出错
        public static final int EGroupSomeItemUploadGetTokenFailed = -8;
        //创建七牛上传器时io error
        public static final int ECreateQuploaderIOError = -9;
        //配置的已上传信息出错
        public static final int EConfigHadUploadInifoError = -10;
        //全局配置的log路径或token url或提交rul出错
        public static final int EGlobalConfigPathAndUrlError = -11;
        //上传结果提交失败
        public static final int EUploadResultCommitFailed = -12;
        //网络原因任务中断，但实例没释放
        public static final int EUploadNetFactorTaskInterrupt = -13;
        //网络中断后网络重连超时
        public static final int ENetReconnectOutTime = -14;
        //配置资源类型为空
        public static final int ESourceTypeIsNull = -15;


        //wifi不可用
        public static final int EWifiUnavailable = -20;
        //网络不可用
        public static final int ENetworkUnavailable = -21;
        //逻辑出错
        public static final int ELogicalException = -22;
        //逻辑出错
        public static final int EAbnormalExit = -100;


        //！！！以下为七牛上传器反馈code
        //上传token过期
        public static final int EQCode401 = 401 + UploadMode.Constant.QUploadManagerErrorIncr;
        //key doesn't match with scope
        public static final int EQCode403 = 403 + UploadMode.Constant.QUploadManagerErrorIncr;
        //上传网络请求超时
        public static final int EQCodeTimedOut = ResponseInfo.TimedOut + UploadMode.Constant.QUploadManagerErrorIncr;
        //上传未知服务器
        public static final int EQCodeUnknownHost = ResponseInfo.UnknownHost + UploadMode.Constant.QUploadManagerErrorIncr;
        //上传无法连接服务器
        public static final int EQCodeCannotConnectToHost = ResponseInfo.CannotConnectToHost + UploadMode.Constant.QUploadManagerErrorIncr;
        //上传网络连接断开
        public static final int EQCodeNetworkConnectionLost = ResponseInfo.NetworkConnectionLost + UploadMode.Constant.QUploadManagerErrorIncr;
        //网络出错
        public static final int EQCodeNetworkError = ResponseInfo.NetworkError + UploadMode.Constant.QUploadManagerErrorIncr;
        //上传文件为空文件
        public static final int EQCodeZeroSizeFile = ResponseInfo.ZeroSizeFile + UploadMode.Constant.QUploadManagerErrorIncr;
        //上传token不可用
        public static final int EQCodeInvalidToken = ResponseInfo.InvalidToken + UploadMode.Constant.QUploadManagerErrorIncr;
        //上传参数无效
        public static final int EQCodeInvalidArgument = ResponseInfo.InvalidArgument + UploadMode.Constant.QUploadManagerErrorIncr;
        //上传文件无效
        public static final int EQCodeInvalidFile = ResponseInfo.InvalidFile + UploadMode.Constant.QUploadManagerErrorIncr;
        //上传被取消
        public static final int EQCodeCancelled = ResponseInfo.Cancelled + UploadMode.Constant.QUploadManagerErrorIncr;


        public static String getErrorToast(int errorCode) {
            switch (errorCode) {
                //quploader
                case EOk:return "";
                case EGroupTasksIsEmpty: return "上传任务组数据列表为空";
                case EGroupSomeItemSourcePathIsNull: return "上传某项资源路径为空";
                case EUploadResultCommitParamError: return "上传某项结果提交参数出错";
                case EGroupSomeItemCommitParamKeyIsNull: return "上传某项资源结果提交参数key为空";
                case EGroupUploadPartItemUploadKeyInexistence: return "上传某项已上传部分的qUploadKey没有设置";
                case EGroupUpItemExecuteIndexError: return "上传开始执行任务项索引出错";
                case EGroupSomeItemUploadGetTokenFailed: return "上传某项任务时获取上传token失败";
                case ECreateQuploaderIOError: return "创建上传器时IO异常";
                case EConfigHadUploadInifoError: return "配置的已上传信息出错";
                case EGlobalConfigPathAndUrlError: return "全局配置信息出错";
                case EUploadResultCommitFailed: return "上传结果提交失败";
                case EUploadNetFactorTaskInterrupt: return "网络原因任务中断";
                case ENetReconnectOutTime: return "网络重连超时";
                case ESourceTypeIsNull: return "配置资源类型为空";

                case EWifiUnavailable: return "wifi不可用";
                case ENetworkUnavailable: return "网络不可用";
                case ELogicalException: return "逻辑出错";


                //qiniu code
                case EQCode401: return "上传token过期";
                case EQCode403: return "key doesn't match with scope";

                case EQCodeTimedOut: return "上传网络请求超时";
                case EQCodeUnknownHost: /*return "上传未知服务器";*/
                case EQCodeCannotConnectToHost: /*return "上传无法连接服务器";*/
                case EQCodeNetworkConnectionLost: return "上传网络连接断开";
                case EQCodeNetworkError: return "上传网络出错";

                case EQCodeZeroSizeFile: return "上传文件为空文件";
                case EQCodeInvalidToken: return "上传token无效";
                case EQCodeInvalidArgument: return "上传参数无效";
                case EQCodeInvalidFile: return "上传文件无效";
                case EQCodeCancelled: return "上传被取消";

                default: return "未知错误";
            }
        }
    }
}
