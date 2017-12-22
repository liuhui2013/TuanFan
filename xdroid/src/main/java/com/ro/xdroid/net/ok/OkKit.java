package com.ro.xdroid.net.ok;

import android.app.Application;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.db.CacheManager;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.ro.xdroid.kit.FileKit;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.net.ok.taotong.TTDL;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
/**
 * Created by roffee on 2017/7/27 14:51.
 * Contact with 460545614@qq.com
 */
public class OkKit {
    private static OkConfig okConfig;
    /***************************init for taotong*******************************/
    public static void initTaotong(@NonNull Application application, OkConfig... okConfig){
        if(!ToolsKit.isEmpty(okConfig)) OkKit.okConfig = okConfig[0];
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //log相关
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(loggingInterceptor);

            //超时时间设置，默认60秒
            builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
            builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
            builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

            builder.cookieJar(new CookieJarImpl(new DBCookieStore(application)));
            OkGo.getInstance().init(application)                           //必须调用初始化
                    .setOkHttpClient(builder.build())               //设置OkHttpClient
                    .setCacheMode(CacheMode.DEFAULT)               //全局统一缓存模式，默认不使用缓存，可以不传
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                    .setRetryCount(1);                             //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                    .addCommonHeaders(headers)                      //全局公共头
//                    .addCommonParams(params);                       //全局公共参数
        }catch (Exception e){e.printStackTrace();}
    }
    public static void setOkConfig(OkConfig okConfig){
        OkKit.okConfig = okConfig;
    }
    private static OkConfig getOkConfig(){
        if(okConfig == null) okConfig = new OkConfig();
        return okConfig;
    }
    /***************************open request api*******************************/
    public static <K, T> Okrx<K, T> request(String url, Class<T> clazz){
        return new Okrx<K, T>().makeRequest(url)
                .setClazz(clazz)
                .setStatusInteragtion(getOkConfig().ttStatusInteragtion);
    }
    public static <K, T> Okrx<K, T> request(K key, String url, Class<T> clazz){
        return new Okrx<K, T>().makeRequest(key, url)
                .setClazz(clazz)
                .setStatusInteragtion(getOkConfig().ttStatusInteragtion);
    }
    public static <K, T> Okrx<K, T> request(K key, OkMode.RequestMode requestMode, String url, Class<T> clazz){
        return new Okrx<K, T>().makeRequest(key, requestMode, url)
                .setClazz(clazz)
                .setStatusInteragtion(getOkConfig().ttStatusInteragtion);
    }
    public static <K, T> Okrx<K, T> request(String url, Type type){
        return new Okrx<K, T>().makeRequest(url)
                .setType(type)
                .setStatusInteragtion(getOkConfig().ttStatusInteragtion);
    }
    public static <K, T> Okrx<K, T> request(K key, String url, Type type){
        return new Okrx<K, T>().makeRequest(key, url)
                .setType(type)
                .setStatusInteragtion(getOkConfig().ttStatusInteragtion);
    }
    public static <K> Okdl<K> DLRequest(String url){
        return new Okdl<K>().makeRequest(url);
    }
    public static <K> Okdl<K> DLRequest(K key, String url){
        return new Okdl<K>().makeRequest(key, url);
    }
    public static <K> TTDL<K> TTDLRequest(String url, String tokenUrl){
        return new TTDL<K>().makeRequest(null, url, tokenUrl);
    }
    public static <K> TTDL<K> TTDLRequest(K key, String url, String tokenUrl){
        return new TTDL<K>().makeRequest(key, url, tokenUrl);
    }
    public static void cancel(Object tag){
        if(tag instanceof Disposable){
            OkDisposable.dispose((Disposable) tag);
        }else{
            OkGo.getInstance().cancelTag(tag);
        }
    }
    public static void cancelInLifecycle(Object... tag){
        OkDisposable.dispose();
        if(!ToolsKit.isEmpty(tag)){
            OkGo.getInstance().cancelTag(tag);
        }
    }
    public static void cancelAll(){
        cancelInLifecycle();
        OkGo.getInstance().cancelAll();
    }
    public static long getCacheSize(){
        return FileKit.getFileSize(CacheManager.getInstance().openReader().getPath());
    }
    public static void clearCache(){
        CacheManager.getInstance().clear();
    }
    public static Handler getHandler(){return OkGo.getInstance().getDelivery();}
}
