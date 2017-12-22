package com.ro.xdroid.net.ok;

import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.ToolsKit;

import java.io.File;

/**
 * Created by roffee on 2017/7/31 09:29.
 * Contact with 460545614@qq.com
 */
public class OkMode {
    public enum XMode {
        Normal,
        Rx,
    }
    public enum RequestMode {
        Get,

        Post,

        Put,

        Delete,

        Head,

        Patch,

        Option,

        Trace,
    }
    public enum CacheMode {
        /** 按照HTTP协议的默认缓存规则，例如有304响应头时缓存 */
        Default,

        /** 不使用缓存 */
        NoCache,

        /** 请求网络失败后，读取缓存 */
        RequestFailedReadCache,

        /** 如果缓存不存在才请求网络，否则使用缓存 */
        IfNoCacheRequest,

        /** 先使用缓存，不管是否存在，仍然请求网络 */
        FirstCacheThenRequest;

        public com.lzy.okgo.cache.CacheMode getCacheMode() {
            switch (this) {
                case Default:
                    return com.lzy.okgo.cache.CacheMode.DEFAULT;
                case NoCache:
                    return com.lzy.okgo.cache.CacheMode.NO_CACHE;
                case RequestFailedReadCache:
                    return com.lzy.okgo.cache.CacheMode.REQUEST_FAILED_READ_CACHE;
                case IfNoCacheRequest:
                    return com.lzy.okgo.cache.CacheMode.IF_NONE_CACHE_REQUEST;
                case FirstCacheThenRequest:
                    return com.lzy.okgo.cache.CacheMode.FIRST_CACHE_THEN_REQUEST;
                default:
                    return com.lzy.okgo.cache.CacheMode.DEFAULT;
            }
        }
    }

    public static class HttpHeaders extends com.lzy.okgo.model.HttpHeaders{
        public HttpHeaders(){
            super();
        }
        public HttpHeaders(String key, String value){
            super(key, value);
        }
    }
    public static class HttpParams extends com.lzy.okgo.model.HttpParams{
        public HttpParams(){
            super();
        }
        public HttpParams(String key, String value){
            super(key, value);
        }
        public HttpParams(String key, File file){
            super(key, file);
        }
    }
    public static class LoadMode{
        public static final String DLFolderDir = XDroidConfig.DirDownload;
        public static final String DLTempSuffix = ".dltmp";

        public static final int DL_CONVERT_NONE  = 0;
        public static final int DL_CONVERT_PAUSE  = 1;
//        public static final int DL_CONVERT_DELETE  = 2;

        //tt
        public static final long TOKEN_EXPIRE = 3600*3*1000;

        public static String getDestTempFileName(String destFileName){
            if(ToolsKit.isEmpty(destFileName)){return null;}
            int lastindex = destFileName.lastIndexOf(".");
            String prefix = null;
            if(lastindex > 0){
                prefix = destFileName.substring(0, lastindex);
            }else{
                prefix = destFileName;
            }
            return prefix + OkMode.LoadMode.DLTempSuffix;
        }
    }
    public static class ProgressMode{
        public static final int ProgressTimeInterval = 300;
    }
}
