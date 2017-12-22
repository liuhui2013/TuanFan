package com.ro.xdroid.net.ok.adapter;

import com.lzy.okgo.adapter.AdapterParam;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.adapter.CallAdapter;
import com.lzy.okgo.model.Response;

import io.reactivex.Observable;

/**
 * Created by roffee on 2017/7/31 18:07.
 * Contact with 460545614@qq.com
 */
public class ObservableResponseAdpt<T> implements CallAdapter<T, Observable<Response<T>>> {
    private AdapterParam param;

    public ObservableResponseAdpt(){
        param = new AdapterParam();
        param.isAsync = true;
    }
    public void isAsync(boolean isAsync){param.isAsync = isAsync;}

    @Override
    public Observable<Response<T>> adapt(Call<T> call, AdapterParam param) {
        return AnalysisParams.analysis(call, this.param);
    }
}
