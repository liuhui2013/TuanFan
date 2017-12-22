package com.ro.xdroid.net.upload.listener;

/**
 * Created by roffee on 2017/11/25 13:37.
 * Contact with 460545614@qq.com
 */
public interface UploadListener<K,IK>{
    void onUploadRespose(UploadResponse<K,IK> response);
}
