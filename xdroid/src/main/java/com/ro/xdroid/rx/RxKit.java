package com.ro.xdroid.rx;

import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by roffee on 2017/7/24 14:21.
 * Contact with 460545614@qq.com
 */
public class RxKit {
    private static Disposable disposable;
    public interface RxClickListener{
        void onClick(View view);
    }
    public static void prevent2Click(final View view, final RxClickListener rxClickListener){
        if(view == null || rxClickListener == null){return;}
        RxView.clicks(view)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        rxClickListener.onClick(view);
                    }
                });
//                .subscribe(new Observer<Object>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(@NonNull Object o) {
//                        if(disposable != null){disposable.dispose();}
//                        rxClickListener.onClick(view);
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }
//    public static void setErrorHandler(){
//        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                if (throwable instanceof UndeliverableException) {
//                    throwable = throwable.getCause();
//                }
//            }
//        });
//    }

}
