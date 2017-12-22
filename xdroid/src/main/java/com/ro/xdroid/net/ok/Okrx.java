package com.ro.xdroid.net.ok;


import com.lzy.okgo.adapter.CallAdapter;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.JsonKit;
import com.ro.xdroid.kit.LoggerKit;
import com.ro.xdroid.kit.SecurityKit;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.net.error.ErrorHandler;
import com.ro.xdroid.net.ok.adapter.ObservableBodyAdpt;
import com.ro.xdroid.net.ok.adapter.ObservableResponseAdpt;
import com.ro.xdroid.net.ok.taotong.TTCommonParams;
import com.ro.xdroid.net.ok.taotong.TTConvert;
import com.ro.xdroid.net.ok.taotong.TTOkListener;
import com.ro.xdroid.net.ok.taotong.TTStatusInteragtion;
import com.ro.xdroid.view.dialog.ToastKit;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by roffee on 2017/7/27 15:39.
 * Contact with 460545614@qq.com
 */
public class Okrx<K, T> extends XOk<K, T, Okrx<K, T>> implements Serializable {
    private Observable<T> observable;
    private Observable<Response<T>> responseObservable;
    private LifecycleTransformer<T> lifecycleTransformer;
    private Disposable disposable;

    private Class<T> clazz;
    private Type type;
    private Type TType;

    protected boolean isAutoToastInfo = true;
    private TTStatusInteragtion ttStatusInteragtion;

    public Okrx(){
        this(null);
    }
    public Okrx(Class<T> clazz){
        super();
        this.clazz = clazz;
//        getRawType();
    }
    public Okrx<K, T> setClazz(Class<T> clazz){
        this.clazz = clazz;
        return this;
    }
    public Okrx<K, T> setType(Type type){
        this.type = type;
        return this;
    }
    public Okrx<K, T> bindToLifecycle(LifecycleTransformer<T>  lifecycleTransformer){
        this.lifecycleTransformer = lifecycleTransformer;
        return this;
    }
    public Okrx<K, T> isAutoToastInfo(boolean isAutoToastInfo){
        this.isAutoToastInfo = isAutoToastInfo;
        return this;
    }
    public Okrx<K, T> setStatusInteragtion(TTStatusInteragtion ttStatusInteragtion){
        this.ttStatusInteragtion = ttStatusInteragtion;
        return this;
    }
    @Override
    public Okrx<K, T> execute(TTOkListener<K, T> ttOkListener){
        super.execute(ttOkListener);
        if(ToolsKit.isEmpty(url)){
            if(!isTTListenerLifecycle()){
                ttResponse.isSuccess = false;
                ttResponse.toast = "url is null, request failed";
                try {
                    ttOkListener.onTTOkResponse(ttResponse);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    ToastKit.show(ErrorHandler.getError(throwable));
                }
                if(isAutoToast){ToastKit.show(ttResponse.toast);}
                cancel();
            }
            return this;
        }
        disposeParams();
        return this;
    }
    protected Okrx<K, T> setConverter(Converter<T> converter){
        request.converter(converter);
        return this;
    }
    protected Okrx<K, T> setConverter(TTConvert<T> converter){
        request.converter(converter);
        return this;
    }
    protected Observable<T> setAdapter(ObservableBodyAdpt<T> adapter){
        observable = request.adapt(adapter);
        return observable;
    }
    protected Observable<Response<T>> setAdapter(ObservableResponseAdpt<T> adapter){
        responseObservable = request.adapt(adapter);
        return responseObservable;
    }
    protected Observable<T> setAdapter(CallAdapter<T, Observable<T>> adapter){
        observable = request.adapt(adapter);
        return observable;
    }
    protected Observable<Response<T>> setAdapter1(CallAdapter<T, Observable<Response<T>>> adapter){
        responseObservable = request.adapt(adapter);
        return responseObservable;
    }
    protected Observable setCompose(LifecycleTransformer<T> lifecycleTransformer){
        Observable ob = (observable != null ? observable : responseObservable);
        if(lifecycleTransformer == null){return ob;}
        return ob.compose(lifecycleTransformer);
    }
    @Override
    protected boolean isTTListenerLifecycle(){
        boolean ttListenerLifecycle = super.isTTListenerLifecycle();
//        LoggerKit.d("isTTListenerLifecycle:" + ttListenerLifecycle);
        if(ttListenerLifecycle){
            cancel();
        }
        if(disposable != null && disposable.isDisposed()){
            ttListenerLifecycle = true;
        }
        return ttListenerLifecycle;
    }
    @Override
    public void cancel(){
//        LoggerKit.d("cancel");
        super.cancel();
        if(!isAutoCancel){return;}
        if(disposable != null){
            disposable.dispose();
            disposable = null;
        }
        observable = null;
        responseObservable = null;
        lifecycleTransformer = null;
    }
    public void forceCancel(){
        isAutoCancel(true);
        cancel();
    }
    private void getRawType(){
        Class aClass = getClass();
        if(!aClass.isMemberClass()){
            return;
        }
        Type genType = getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) genType).getActualTypeArguments();
        if(types == null || types.length < 2){return;}
//        ParameterizedType parameterizedType = (ParameterizedType)types[1];
        TType = ((ParameterizedType)types[1]).getRawType();
    }

    private void autoBindLifercycle(Observable<Response<T>> responseObservable){
        if(isBindToLifecycle){
            if(lifecycleTransformer != null){
                //noinspection unchecked
                ((Observable<T>)responseObservable).compose(lifecycleTransformer);
            }else{
                //rx方式不用设置tag
//                    setTag(ttOkListener);
                if(ttOkListener instanceof RxAppCompatActivity){
                    RxAppCompatActivity rxAppCompatActivity = (RxAppCompatActivity)ttOkListener;
                    responseObservable.compose(rxAppCompatActivity.<Response<T>>bindToLifecycle());
                }else if(ttOkListener instanceof RxFragment){
                    RxFragment rxFragment = (RxFragment)ttOkListener;
                    responseObservable.compose(rxFragment.<Response<T>>bindToLifecycle());
                }else if(ttOkListener instanceof com.trello.rxlifecycle2.components.RxFragment){
                    com.trello.rxlifecycle2.components.RxFragment arxFragment = (com.trello.rxlifecycle2.components.RxFragment)ttOkListener;
                    responseObservable.compose(arxFragment.<Response<T>>bindToLifecycle());
                }
            }
        }
    }
    private void disposeParams(){
        if(!isNetworkAvailable()){return;}
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                try{
                    putParams(TTCommonParams.getTTCommonParams(isUseCommonParams));
                    if(isDesParams){
                        try{
                            String jsonParams = JsonKit.toJson(requestParams);
                            String desParams = SecurityKit.desEncode(jsonParams); //TODO 加密3des
                            setParams(XDroidConfig.ReqParamKeyEntrypt, desParams)
                                    .setParams(XDroidConfig.ReqParamKeyIsEntrypt, 1);
                        }catch (Exception e){
                            e.printStackTrace();

                            return;
                        }
                    }else{
                        setParams(requestParams);
                    }
                    emitter.onNext(true);
                }catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(new Throwable("请求参数加密失败"));
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        disposeTTExecute();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(!isTTListenerLifecycle()){
                            ttResponse.isSuccess = false;
                            ttResponse.toast = "请求参数加密失败";
                            try {
                                ttOkListener.onTTOkResponse(ttResponse);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                                ToastKit.show(ErrorHandler.getError(throwable));
                            }
                            if(isAutoToast){ToastKit.show(ttResponse.toast);}
                            cancel();
                        }
                    }

                    @Override
                    public void onComplete() {}
                });
    }
    private void disposeTTExecute(){
        if(ToolsKit.isEmpty(request.getCacheKey()) &&
                (request.getCacheMode() != CacheMode.DEFAULT
                        || request.getCacheMode() != CacheMode.NO_CACHE)){
            setCacheKey(request.getUrl());
        }
        setConverter(new TTConvert<T>(isDesParams, clazz, type))
                .setAdapter1(new ObservableResponse<T>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        autoBindLifercycle(responseObservable);
        responseObservable.subscribe(new Observer<Response<T>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
                if(isBindToLifecycle){OkDisposable.addDisposable(d);}
            }

            @Override
            public void onNext(@NonNull Response<T> tResponse) {
                if(!isTTListenerLifecycle()){
                    ttResponse.isSuccess = true;
                    ttResponse.isFromCache = tResponse.isFromCache();
                    ttResponse.data = tResponse.body();

                    try {
                        integrationStatus();
                        if(!ttResponse.isInterruptResponse)ttOkListener.onTTOkResponse(ttResponse);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        if(!isTTListenerLifecycle() && ttResponse.isSuccess) {
                            ttResponse.isSuccess = false;
                            ttResponse.toast = ErrorHandler.getError(throwable);
                            try {
                                ttOkListener.onTTOkResponse(ttResponse);
                            } catch (Throwable throwable1) {
                                throwable1.printStackTrace();
                                ToastKit.show(ErrorHandler.getError(throwable1));
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if(!isTTListenerLifecycle()){
                    ttResponse.isSuccess = false;
                    ttResponse.toast = ErrorHandler.getError(e);
                    try {
                        ttOkListener.onTTOkResponse(ttResponse);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    if(isAutoToast){ToastKit.show(ttResponse.toast);}
                }
                cancel();
            }

            @Override
            public void onComplete() {
                cancel();
            }
        });
    }

    private void integrationStatus() throws Throwable{
        if(ttResponse.isFromCache && request.getCacheMode() != CacheMode.IF_NONE_CACHE_REQUEST) ttResponse.isInterruptResponse = true;
        if(ttResponse.data == null) {
            ttResponse.isSuccess = false;
            ttResponse.toast = "响应数据为空";
            if(!ttResponse.isInterruptResponse)ttOkListener.onTTOkResponse(ttResponse);
            if(isAutoToast && !ttResponse.isFromCache){ToastKit.show(ttResponse.toast);}
            return;
        }
        Class aClass = ttResponse.data.getClass();
        if(aClass == null) return;
//        Field statusField = aClass.getDeclaredField("status");
        Field[] fields = aClass.getDeclaredFields();
        if(fields == null) return;
        boolean hasStatus = false;
        int statusValue = 0;
        Object infoValue = null;
        for(Field field : fields){
            field.setAccessible(true);
            if(field.getName().equals("status")){
                hasStatus = true;
                statusValue = field.getInt(ttResponse.data);
            }else if(field.getName().equals("info")){
                infoValue = field.get(ttResponse.data);
            }
        }
        if(hasStatus){
            if(statusValue != 1 && statusValue != 1056){
                if(ttResponse.isFromCache) return;

                String info;
                if(infoValue != null) info = infoValue.toString();
                else info = "";

                ttResponse.toast = info;
                LoggerKit.d("integrationStatus-status:" + statusValue + "   info:" + info);
                if(isAutoToastInfo && infoValue != null) ToastKit.show(info);

                ttResponse.isSuccess = false;
                if(ttStatusInteragtion != null) {
                    ttResponse.isInterruptRequest = !ttStatusInteragtion.onTTStatusInteragtion(statusValue, info);
                    if(ttResponse.isInterruptRequest) ttResponse.isInterruptResponse = true;
                }
            }else{
                ttResponse.isInterruptResponse = false;
            }
        }
    }
}
