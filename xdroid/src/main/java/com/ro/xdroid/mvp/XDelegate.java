package com.ro.xdroid.mvp;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by roffee on 2017/7/20 14:38.
 * Contact with 460545614@qq.com
 */
public class XDelegate implements IDelegate{
    private Context context;
    protected CompositeDisposable compositeDisposable;

    private XDelegate(Context context){this.context = context;}
    public static IDelegate create(Context context){return new XDelegate(context);}
    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destory() {
        dispose();
    }

    protected void addDispose(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    protected void dispose() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }
}
