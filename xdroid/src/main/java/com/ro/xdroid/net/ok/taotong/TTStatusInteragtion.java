package com.ro.xdroid.net.ok.taotong;

/**
 * Created by roffee on 2017/9/12 14:50.
 * Contact with 460545614@qq.com
 */
public interface TTStatusInteragtion {
    //return true:继续网络请求后续执行  return false:终止网络请求后续执行
    boolean onTTStatusInteragtion(int status, String info);
}
