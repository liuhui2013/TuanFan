package com.ro.xdroid.mvp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.cache.CacheKit;
import com.ro.xdroid.media.image.loader.ImageLoaderKit;
import com.ro.xdroid.net.ok.OkKit;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by roffee on 2017/7/20 11:43.
 * Contact with 460545614@qq.com
 */
public abstract class XApp extends Application{
//        MultiDexApplication {
    protected static XApp xApp;
    protected Activity curActivity;
    protected RefWatcher refWatcher;
//    protected Handler handler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 将MultiDex注入到项目中
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        xApp = this;
//        handler = new Handler();
        initXDroidConfig();
        init();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ImageLoaderKit.onTrimMemory(level);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        ImageLoaderKit.onLowMemory();
    }

    protected abstract void initXDroidConfig();
    private void init(){
        AutoLayoutConifg.getInstance().useDeviceSize().init(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        OkKit.initTaotong(this);
        ImageLoaderKit.init(this);
//        refWatcher = XDroidConfig.Debug ? LeakCanary.install(this) : RefWatcher.DISABLED;
        refWatcher = XDroidConfig.Debug ? LeakCanary.install(this) : null;
        CacheKit.clearVCache();
    }
    public static XApp getApp(){return xApp;}
    public static Context getContext() {
        return xApp.getApplicationContext();
    }
    public void setCurActivity(Activity curActivity){this.curActivity = curActivity;}
    public Activity getCurActivity(){return curActivity;}
    public void leakCanaryWatcher(Object watchedReference){
        if(refWatcher == null){return;}
        refWatcher.watch(watchedReference);
    }
    public Handler getHandler(){return OkKit.getHandler();}
}
