package com.ro.xdroid.net.ok;

import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.ro.xdroid.kit.FileKit;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.net.error.ErrorHandler;
import com.ro.xdroid.net.ok.callback.FileCallback;
import com.ro.xdroid.net.ok.convert.FileConvert;
import com.ro.xdroid.net.ok.taotong.TTOkListener;
import com.ro.xdroid.net.ok.taotong.TTProgress;
import com.ro.xdroid.view.dialog.ToastKit;

import java.io.File;
import java.io.Serializable;

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
 * Created by roffee on 2017/8/14 10:12.
 * Contact with 460545614@qq.com
 */
public class Okdl<K> extends XOk<K, File, Okdl<K>> implements Serializable {
    private String destFileDir;
    private String destFileName;
    private boolean isBreakpoint;
    private boolean isCover = true;
    private long lastLoadTime;
    private Disposable disposable;

    public Okdl(){
        super();
        ttResponse.progress = new TTProgress();
        isDesParam(false);
    }
    public Okdl<K> makeRequest(String url){
        return makeRequest(null, OkMode.RequestMode.Get, url);
    }
    public Okdl<K> makeRequest(K key, String url){
        return makeRequest(key, OkMode.RequestMode.Get, url);
    }
    public Okdl<K> setDestFileDir(String destFileDir){
        this.destFileDir = destFileDir;
        return this;
    }
    public Okdl<K> setDestFileName(String destFileName){
        this.destFileName = destFileName;
        return this;
    }
    public Okdl<K> isBreakpoint(boolean isBreakpoint, String destFileName){
        this.destFileName = destFileName;
        this.isBreakpoint = isBreakpoint;
        return this;
    }
    public Okdl<K> isCover(boolean isCover){
        this.isCover = isCover;
        return this;
    }
    @Override
    public Okdl<K> execute(TTOkListener<K, File> ttOkListener){
        super.execute(ttOkListener);
        disposeTTExecute();
        return this;
    }

    @Override
    public void cancel(){
//        LoggerKit.d("cancel");
        if(isAutoCancel && ttResponse != null &&
                ttResponse.progress.status == TTProgress.LOADING){
            pause();
            OkKit.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Okdl.super.cancel();
                    if(!isAutoCancel){return;}
                    if(disposable != null){
                        disposable.dispose();
                        disposable = null;
                    }
                }
            }, 100);
            return;
        }

        super.cancel();
        if(!isAutoCancel){return;}
        if(disposable != null){
            disposable.dispose();
            disposable = null;
        }
    }
    public void forceCancel(){
        pause();
        OkKit.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isAutoCancel(true);
                cancel();
            }
        }, 1000);

    }
    public void pause(){
        if(ttResponse == null || disposable == null){return;}
        if(ttResponse.progress.status != TTProgress.LOADING || disposable.isDisposed()){return;}
        FileConvert.setConvertControl(OkMode.LoadMode.DL_CONVERT_PAUSE);
    }
    public void resume(String... url){
        if(isAutoCancel){return;}
        if(ttResponse.progress.status == TTProgress.LOADING){return;}
        if(ToolsKit.isEmpty(url)){makeRequest(key, this.url);}
        else{makeRequest(key, url[0]);}
        setDestFileName(ttResponse.progress.fileName);
        disposeTTExecute();
    }
    public void delete(final String... destFileName){
        pause();
        OkKit.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String dfn = null;
                if(!ToolsKit.isEmpty(destFileName)){
                    dfn = destFileName[0];
                }else{
                    dfn = Okdl.this.destFileName;
                }
                if(dfn != null && destFileDir != null){
                    FileKit.deleteFile(destFileDir + dfn);
                    FileKit.deleteFile(destFileDir + OkMode.LoadMode.getDestTempFileName(dfn));
                }
                forceCancel();
            }
        }, 1000);
    }
    private void disposeTTExecute() {
        if (!isNetworkAvailable()) {return;}
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(final ObservableEmitter<Progress> emitter) throws Exception {
//                setParams(requestParams);
                if(ToolsKit.isEmpty(destFileDir)) destFileDir = OkMode.LoadMode.DLFolderDir;
//                if(!ToolsKit.isEmpty(destFileName)) FileKit.deleteFile(destFileName);
                String destTempFileName = OkMode.LoadMode.getDestTempFileName(destFileName);
                if(isBreakpoint){
                    if(!ToolsKit.isEmpty(destTempFileName)){
                        long startPosition = FileKit.getFileSize(destFileDir + destTempFileName);
                        setHeaders(HttpHeaders.HEAD_KEY_RANGE, "bytes=" + startPosition + "-");
//                    setHeaders(HttpHeaders.HEAD_KEY_CONTENT_RANGE, "bytes=" + tempSize + "-" + totalSize + "/" + totalSize);
                    }
                }else{
                    if(!ToolsKit.isEmpty(destTempFileName)){
                        FileKit.deleteFile(destFileDir + destTempFileName);
                    }
                }
                request.execute(new FileCallback(Okdl.this, destFileDir, destFileName, destTempFileName, isCover) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        emitter.onComplete();
                    }
                    @Override
                    public void onError(Response<File> response) {
                        if(disposable == null || disposable.isDisposed()){return;}
                        if(ToolsKit.isEmpty(response.getException())){
                            emitter.onError(new Exception("停止下载"));
                        }else{
                            emitter.onError(response.getException());
                        }
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        emitter.onNext(progress);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //ready downloading
                        lastLoadTime = System.currentTimeMillis();
                        ttResponse.isSuccess = true;
                        ttResponse.progress.status = Progress.LOADING;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                        if(isBindToLifecycle){OkDisposable.addDisposable(d);}
                    }

                    @Override
                    public void onNext(@NonNull Progress progress) {
                        if(!ttResponse.progress.isSetStable){
                            ttResponse.progress.isSetStable = true;
                            ttResponse.isSuccess = true;
                            ttResponse.toast = "下载中";
                            ttResponse.progress.folder = OkMode.LoadMode.DLFolderDir;//progress.folder;
                            ttResponse.progress.filePath = progress.filePath;
                            ttResponse.progress.fileName = progress.fileName;
                            ttResponse.progress.tempFileName = (String) progress.extra2;
                            ttResponse.progress.tempFilePath = (String) progress.extra3;
                            ttResponse.progress.totalSize = progress.totalSize;
                            ttResponse.progress.status = Progress.LOADING;
                            if(destFileName == null){destFileName = progress.fileName;}
                        }

                        long nowTime = System.currentTimeMillis();
                        if((nowTime - lastLoadTime) < OkMode.ProgressMode.ProgressTimeInterval && progress.fraction != 1){
                            return;
                        }
                        lastLoadTime = nowTime;
                        ttResponse.progress.percent = progress.fraction;
                        ttResponse.progress.loadSize = progress.currentSize;
                        ttResponse.progress.speed = progress.speed;
                        if(!isTTListenerLifecycle()){
                            try {
                                ttOkListener.onTTOkResponse(ttResponse);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                                if(!isTTListenerLifecycle()) {
                                    ttResponse.isSuccess = false;
                                    ttResponse.progress.status = TTProgress.ERROR;
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
                            ttResponse.progress.status = TTProgress.ERROR;
                            try {
                                ttOkListener.onTTOkResponse(ttResponse);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                                ToastKit.show(ErrorHandler.getError(throwable));
                            }
                            if(isAutoToast){
                                ToastKit.show(ttResponse.toast);}
                        }
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        if(!isTTListenerLifecycle()){
                            ttResponse.isSuccess = true;
                            ttResponse.toast = "下载完成";
                            ttResponse.progress.status = TTProgress.FINISH;
                            ttResponse.progress.percent = 1;
                            ttResponse.progress.loadSize = ttResponse.progress.totalSize;
                            try {
                                ttOkListener.onTTOkResponse(ttResponse);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                                ToastKit.show(ErrorHandler.getError(throwable));
                            }
                            if(isAutoToast){
                                ToastKit.show(ttResponse.toast);}
                        }
                        cancel();
                    }
                });
    }

    public Request<File, ? extends Request> getRequest(){
        return request;
    }
}
