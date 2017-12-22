package com.taotong.tuanfan.model;

/**
 * Created by roffee on 2017/7/17 08:49.
 * Contact with 460545614@qq.com
 */
public class Respond<T, E, R> {
    public int key;
    public int errorCode;

    public String toast;
    public boolean isAutoToast;

    public boolean isFromCache;
    public T data;

    public E extraOkgokey;
    public R reserve;

    public Respond(int key, String toast, boolean isAutoToastm, R reserve){
        this.key = key;
        this.toast = toast;
        this.isAutoToast = isAutoToastm;
        this.reserve = reserve;
    }
    public Respond(int key, T data, R reserve){
        this.key = key;
        this.data = data;
        this.reserve = reserve;
    }
    public Respond(int key, int errorCode, String toast, T data, E extraOkgokey, R reserve){
        this.key = key;
        this.errorCode = errorCode;
        this.toast = toast;
        this.data = data;
        this.extraOkgokey = extraOkgokey;
        this.reserve = reserve;
    }
}
