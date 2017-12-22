package com.ro.xdroid.net.ok.taotong;

/**
 * Created by roffee on 2017/8/1 10:12.
 * Contact with 460545614@qq.com
 */
public class TTResponse<K, T> {
    public boolean isSuccess;
    public boolean isAutoToast;
    public String toast;

    public K key;

    public boolean isFromCache;
    public T data;

    public TTProgress progress;

    public Object reserve;
    public Object extraReserve;

    //外部暂使用不到
    public boolean isInterruptRequest;
    public boolean isInterruptResponse;
}
