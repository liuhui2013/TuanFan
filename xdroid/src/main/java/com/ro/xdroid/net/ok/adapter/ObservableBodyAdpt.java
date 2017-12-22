package com.ro.xdroid.net.ok.adapter;

import com.lzy.okgo.adapter.AdapterParam;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.adapter.CallAdapter;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.observable.BodyObservable;

import io.reactivex.Observable;

/**
 * Created by roffee on 2017/7/31 17:31.
 * Contact with 460545614@qq.com
 */
public class ObservableBodyAdpt<T> implements CallAdapter<T, Observable<T>> {
    private AdapterParam param;

    public ObservableBodyAdpt(){
        param = new AdapterParam();
        param.isAsync = true;
    }
    public void isAsync(boolean isAsync){param.isAsync = isAsync;}
    @Override
    public Observable<T> adapt(Call<T> call, AdapterParam param) {
        Observable<Response<T>> responseObservable = AnalysisParams.analysis(call, this.param);
        return  new BodyObservable<>(responseObservable);
    }
}
