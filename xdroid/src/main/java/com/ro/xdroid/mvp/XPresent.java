package com.ro.xdroid.mvp;

/**
 * Created by roffee on 2017/7/20 16:12.
 * Contact with 460545614@qq.com
 */
public class XPresent <V extends IView> implements IPresent<V>{
    private V v;

    @Override
    public void attachV(V v) {this.v = v;}

    @Override
    public void detachV() {v = null;}

    protected V getV() {
        if (v == null) {
            throw new IllegalStateException("v can not be null");
        }
        return v;
    }
}
