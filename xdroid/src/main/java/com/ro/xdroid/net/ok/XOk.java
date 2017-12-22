package com.ro.xdroid.net.ok;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.Fragment;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.request.base.Request;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.net.error.ErrorHandler;
import com.ro.xdroid.net.ok.taotong.TTOkListener;
import com.ro.xdroid.net.ok.taotong.TTResponse;
import com.ro.xdroid.view.dialog.ToastKit;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by roffee on 2017/8/2 15:13.
 * Contact with 460545614@qq.com
 */
public abstract class XOk<K, T, O extends XOk> implements Serializable{

    protected static final long CACHE_TIME_TNEVER_EXPIRE = -1;        //缓存永不过期
    protected Request<T, ? extends Request> request;
    protected Map<String, String> requestParams;
    protected K key;
    protected String url;
    protected TTOkListener<K, T> ttOkListener;
    public TTResponse<K, T> ttResponse;
    protected boolean isAutoCancel = true;
    protected boolean isDesParams = true;
    protected boolean isUseCommonParams = true;
    protected boolean isAutoToast = true;
    protected boolean isBindToLifecycle = true;
    protected boolean isMustWifi;
//    protected boolean isCallBackWhenStatusAbnormal;
    protected Object reserve;
    protected Object extraReserve;

    public XOk() {
        requestParams = new LinkedHashMap<>();
        ttResponse = new TTResponse<>();
    }
    @SuppressWarnings("unchecked")
    public O makeRequest(String url){
        return makeRequest(null, OkMode.RequestMode.Post, url);
    }
    @SuppressWarnings("unchecked")
    public O makeRequest(K key, String url){
        return makeRequest(key, OkMode.RequestMode.Post, url);
    }
    @SuppressWarnings("unchecked")
    public O makeRequest(K key, OkMode.RequestMode requestMode, String url){
        this.key = key;
        this.url = url;
        if (requestMode == OkMode.RequestMode.Get) request = OkGo.get(url);
        else if (requestMode == OkMode.RequestMode.Post) request =  OkGo.post(url);
        else if (requestMode == OkMode.RequestMode.Put) request =  OkGo.put(url);
        else if (requestMode == OkMode.RequestMode.Delete) request =  OkGo.delete(url);
        else if (requestMode == OkMode.RequestMode.Head) request =  OkGo.head(url);
        else if (requestMode == OkMode.RequestMode.Patch) request =  OkGo.patch(url);
        else if (requestMode == OkMode.RequestMode.Option) request =  OkGo.options(url);
        else if (requestMode == OkMode.RequestMode.Trace) request =  OkGo.trace(url);
        else request =  OkGo.post(url);
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O setCacheMode(OkMode.CacheMode cacheMode){
        if(cacheMode == null){
            request.cacheMode(CacheMode.DEFAULT);
            return (O)this;
        }
        request.cacheMode(cacheMode.getCacheMode());
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O setCacheKey(String cacheKey){
        if(ToolsKit.isEmpty(cacheKey)){return (O)this;}
        request.cacheKey(cacheKey);
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O setCacheTime(long cacheTime){
        request.cacheTime(cacheTime);
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O setReserve(Object reserve){
        this.reserve = reserve;
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O setExtraReserve(Object extraReserve){
        this.extraReserve = extraReserve;
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O putParams(Map<String, String> params){
        if(ToolsKit.isEmpty(params)){return (O)this;}
//        requestParams.putAll(params);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestParams.put(entry.getKey(), entry.getValue());
        }
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public <V> O putParams(String key, V value){
        if(ToolsKit.isEmpty(key) || ToolsKit.isEmpty(value)){return (O)this;}
        if(value instanceof String) requestParams.put(key, (String) value);
        else if(value instanceof Integer) requestParams.put(key, String.valueOf(value));
        else if(value instanceof Long) requestParams.put(key, String.valueOf(value));
        else if(value instanceof Float) requestParams.put(key, String.valueOf(value));
        else if(value instanceof Double) requestParams.put(key, String.valueOf(value));
        else if(value instanceof Character) requestParams.put(key, String.valueOf(value));
        else if(value instanceof Boolean) requestParams.put(key, String.valueOf(value));
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O isAutoCancel(boolean isAutoCancel){
        this.isAutoCancel = isAutoCancel;
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O isMustWifi(boolean isMustWifi){
        this.isMustWifi = isMustWifi;
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O isDesParam(boolean isDesParams){
        this.isDesParams = isDesParams;
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O isUseCommonParams(boolean isUseCommonParams){
        this.isUseCommonParams = isUseCommonParams;
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O isAutoToast(boolean isAutoToast){
        this.isAutoToast = isAutoToast;
        return (O)this;
    }

//    @SuppressWarnings("unchecked")
//    public O isCallBackWhenStatusAbnormal(boolean isCallBackWhenStatusAbnormal){
//        this.isCallBackWhenStatusAbnormal = isCallBackWhenStatusAbnormal;
//        return (O)this;
//    }
    @SuppressWarnings("unchecked")
    public O isBindToLifecycle(boolean isBindToLifecycle){
        this.isBindToLifecycle = isBindToLifecycle;
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    public O execute(TTOkListener<K, T> ttOkListener){
        this.ttOkListener = ttOkListener;
        ttResponse.key = key;
        ttResponse.isAutoToast = isAutoToast;
        if(reserve != null) ttResponse.reserve = reserve;
        if(extraReserve != null) ttResponse.extraReserve = extraReserve;
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    protected O setTag(Object tag){
        request.tag(tag);
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    protected O setHeaders(OkMode.HttpHeaders headers){
        if(headers == null) return (O)this;
        request.headers(headers);
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    protected O setHeaders(String key, String value){
        if(ToolsKit.isEmpty(key) || ToolsKit.isEmpty(value)) return (O)this;
        request.headers(key, value);
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    protected O setParams(OkMode.HttpParams params){
        if(params == null) return (O)this;
        request.params(params);
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    protected O setParams(Map<String, String> params, boolean... isReplace){
        if(ToolsKit.isEmpty(params)) return (O)this;
        request.params(params, isReplace);
        return (O)this;
    }
    @SuppressWarnings("unchecked")
    protected <V> O setParams(String key, V value, boolean... isReplace){
        if(value instanceof String) request.params(key, (String) value, isReplace);
        else if(value instanceof Integer) request.params(key, (Integer) value, isReplace);
        else if(value instanceof Long) request.params(key, (Long) value, isReplace);
        else if(value instanceof Float) request.params(key, (Float) value, isReplace);
        else if(value instanceof Double) request.params(key, (Double) value, isReplace);
        else if(value instanceof Character) request.params(key, (Character) value, isReplace);
        else if(value instanceof Boolean) request.params(key, (Boolean) value, isReplace);
        return (O)this;
    }
    @TargetApi(17)
    protected boolean isTTListenerLifecycle(){
        if(ttOkListener == null){return true;}
        if(!isBindToLifecycle){return false;}
        boolean isLifecycle = false;
        if(ttOkListener instanceof Activity){
            Activity activity = (Activity)ttOkListener;
            if(activity.isFinishing() || activity.isDestroyed()){
                isLifecycle = true;
            }
        }else if(ttOkListener instanceof Fragment){
            Fragment fragment = (Fragment)ttOkListener;
            if(fragment.isRemoving() || fragment.isDetached()){
                isLifecycle = true;
            }
        }else if(ttOkListener instanceof android.app.Fragment){
            android.app.Fragment afragment = (android.app.Fragment)ttOkListener;
            if(afragment.isRemoving() || afragment.isDetached()){
                isLifecycle = true;
            }
        }
//        if(isLifecycle){
//            LoggerKit.d("isTTListenerLifecycle--isLifecycle--ture");
//            cancel();
//        }
        return isLifecycle;
    }
    protected boolean isNetworkAvailable(){
        if(isMustWifi){
            if (!ToolsKit.isWifiAvailable(isAutoToast)) {
                if(!isTTListenerLifecycle()){
                    ttResponse.isSuccess = false;
                    ttResponse.toast = "当前wifi不可用，请检查网络！";
                    try {
                        ttOkListener.onTTOkResponse(ttResponse);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        ToastKit.show(ErrorHandler.getError(throwable));
                    }
                    return false;
                }
            }
        }else {
            if (!ToolsKit.isNetworkAvailable(isAutoToast)) {
                if(!isTTListenerLifecycle()){
                    ttResponse.isSuccess = false;
                    ttResponse.toast = "当前网络不可用，请检查网络！";
                    try {
                        ttOkListener.onTTOkResponse(ttResponse);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    return false;
                }
            }
        }
        return true;
    }
    public void cancel(){
        if(!isAutoCancel){return;}
        if(requestParams != null){
            requestParams.clear();
            requestParams = null;
        }
        request = null;
        ttOkListener = null;
        ttResponse = null;
    }
    public void forceCancel(){
        isAutoCancel = true;
        cancel();
    }
}
