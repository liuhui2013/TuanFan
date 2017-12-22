package com.ro.xdroid.net.ok.adapter;

import com.lzy.okgo.adapter.AdapterParam;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.observable.CallEnqueueObservable;
import com.lzy.okrx2.observable.CallExecuteObservable;

import io.reactivex.Observable;

/**
 * Created by roffee on 2017/8/3 13:36.
 * Contact with 460545614@qq.com
 */
public class AnalysisParams {
    static <T> Observable<Response<T>> analysis(Call<T> call, AdapterParam param) {
        Observable<Response<T>> observable;
        if (param == null) param = new AdapterParam();
        if (param.isAsync) {
            observable = new CallEnqueueObservable<>(call);
        } else {
            observable = new CallExecuteObservable<>(call);
        }
        return observable;
    }
}
