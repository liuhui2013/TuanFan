package com.ro.xdroid.net.error;

import com.google.gson.JsonParseException;
import com.lzy.okgo.exception.HttpException;
import com.ro.xdroid.kit.ToolsKit;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

/**
 * Created by roffee on 2017/8/2 11:30.
 * Contact with 460545614@qq.com
 */
public class ErrorHandler {
    public static String getError(Throwable e){
        if(e == null){return "未知错误！";}
        //http
        if (e instanceof HttpException) {
            return "网络异常";
        }else if (e instanceof ConnectException || e instanceof SocketException || e instanceof SocketTimeoutException){
            return "网络超时";
        }else if (e instanceof UnknownHostException || e instanceof UnknownServiceException){
            return "解析域名或服务器异常";
        }else if (e instanceof HttpRetryException){
            return "网络重连异常";
        }
        //数据解析
        else if (e instanceof JSONException || e instanceof JsonParseException){ //e instanceof JsonIOException
            return "网络json数据解析异常";
        }else if (e instanceof NullPointerException){
            return "空指针引用异常";
        }else if (e instanceof IOException){
            return "IO异常";
        }else if (e instanceof NoSuchFieldException){
            return "类型字段匹配异常";
        }else if (e instanceof IllegalAccessError){
            return "违法访问异常";
        } else if (e instanceof IllegalStateException){
            return "无效状态异常";
        }
        else if (e instanceof UnknownError){
            return "未知错误";
        }else{
            if(!ToolsKit.isEmpty(e.getMessage())){
                return e.getMessage();
            }else{
                return e.toString();
            }
        }
    }
}
