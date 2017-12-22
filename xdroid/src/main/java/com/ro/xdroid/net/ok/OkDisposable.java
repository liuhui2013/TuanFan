package com.ro.xdroid.net.ok;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by roffee on 2017/8/3 09:50.
 * Contact with 460545614@qq.com
 */
public class OkDisposable {
    private static CompositeDisposable compositeDisposable;

    public static void addDisposable(Disposable d){
        if(d == null){return;}
        if(compositeDisposable == null){compositeDisposable = new CompositeDisposable();}
        compositeDisposable.add(d);
    }
    public static void dispose(Disposable d){
        if(compositeDisposable == null || d == null){return;}
        compositeDisposable.remove(d);
    }
    public static void dispose(){
        if(compositeDisposable == null){return;}
        compositeDisposable.dispose();
        compositeDisposable.clear();
        compositeDisposable = null;
    }
}
