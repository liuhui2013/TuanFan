package com.ro.xdroid.mvp;

/**
 * Created by roffee on 2017/7/19 18:31.
 * Contact with 460545614@qq.com
 */
public interface IPresent<V> {
    void attachV(V v);
    void detachV();
}
