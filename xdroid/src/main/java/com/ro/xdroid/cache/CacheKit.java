package com.ro.xdroid.cache;

import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.FileKit;
import com.ro.xdroid.media.image.loader.fresco.FrescoLoader;
import com.ro.xdroid.media.image.loader.glide.GlideLoader;
import com.ro.xdroid.net.ok.OkKit;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by roffee on 2017/8/9 14:21.
 * Contact with 460545614@qq.com
 */
public class CacheKit {
    public static void getDiskCacheSize(final CacheListener.OnCacheSizeListener onCacheSizeListener){
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                long tSize = 0;
                try{
                    tSize = FrescoLoader.getDiskCacheSize();
                    tSize += GlideLoader.getDiskCacheSize();
                    tSize += OkKit.getCacheSize();
                    emitter.onNext(tSize);
                }catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(new Throwable("calculate size error"));
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if(onCacheSizeListener != null){onCacheSizeListener.onCacheSize(aLong);}
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(onCacheSizeListener != null){onCacheSizeListener.onCacheSize(0);}
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public static long getMemoryCacheSize(){
        return 0;
    }
    public static void clearDiskCache(final CacheListener.OnCacheClearListener onCacheClearListener){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                long tSize = 0;
                try{
                    FrescoLoader.clearDiskCache();
                    GlideLoader.clearDiskCache();
                    OkKit.clearCache();
                    emitter.onNext(true);
                }catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(new Throwable("calculate size error"));
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        if(onCacheClearListener != null){onCacheClearListener.onCacheClear(true);}
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(onCacheClearListener != null){onCacheClearListener.onCacheClear(false);}
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void clearVCache(){
        Observable.just(1)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        FileKit.deleteFiles(new File(XDroidConfig.DirVCache));
                    }
                });
    }
}
