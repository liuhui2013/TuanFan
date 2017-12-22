package com.ro.xdroid.net.ok.taotong;

/**
 * Created by roffee on 2017/8/1 10:10.
 * Contact with 460545614@qq.com
 */
public interface TTOkListener<K, T>{
    void onTTOkResponse(TTResponse<K, T> ttResponse) throws Throwable;
}
