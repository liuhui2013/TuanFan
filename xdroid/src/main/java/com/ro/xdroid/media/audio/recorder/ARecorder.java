package com.ro.xdroid.media.audio.recorder;

import android.support.annotation.NonNull;

import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.FileKit;
import com.ro.xdroid.media.audio.recorder.listener.ARecorderListener;
import com.ro.xdroid.media.audio.recorder.listener.ARecorderResponse;
import com.ro.xdroid.view.dialog.ToastKit;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ro.xdroid.media.audio.recorder.AMode.DefaultMaxDuration;
import static com.ro.xdroid.media.audio.recorder.AMode.DefaultMinDuration;
import static com.ro.xdroid.media.audio.recorder.AMode.EnableMinDuration;

/**
 * Created by roffee on 2017/9/29 11:28.
 * Contact with 460545614@qq.com
 */
public abstract class ARecorder <R extends ARecorder>{
    protected AMode.AType aType;
    protected AMode.ASource aSource = AMode.ASource.Mic;
    protected AMode.AChannel aChannel = AMode.AChannel.Mono;
    protected AMode.ASampleRate aSampleRate = AMode.ASampleRate.HZ_44100;
    protected AMode.APcmFormat aPcmFormat = AMode.APcmFormat.PCM_16BIT;
    protected ARecorderListener aRecorderListener;
    protected ARecorderResponse aRecorderResponse;
    protected int minSecond = DefaultMinDuration, maxSecond = DefaultMaxDuration;
    protected boolean isConfigOk, isRecording, isTempFile = true;
    protected String filePath;

    protected Observable<Long> observableInterval;
    protected Disposable disposable;
    protected int curDuration;

    protected abstract boolean start();
    protected abstract boolean stop();
    protected abstract int durationChange(int curDuration);

    public ARecorder(AMode.AType aType){
        this.aType = aType;
        aRecorderResponse = new ARecorderResponse();
        disposeFile();
    }

    @SuppressWarnings("unchecked")
    public R setSource(AMode.ASource aSource){
        this.aSource = aSource;
        return (R)this;
    }
    @SuppressWarnings("unchecked")
    public R setChannel(AMode.AChannel aChannel){
        this.aChannel = aChannel;
        return (R)this;
    }
    @SuppressWarnings("unchecked")
    public R setSampleRate(AMode.ASampleRate aSampleRate){
        this.aSampleRate = aSampleRate;
        return (R)this;
    }
    @SuppressWarnings("unchecked")
    public R setMinDuration(int minSecond){
        this.minSecond = minSecond;
        return (R)this;
    }
    @SuppressWarnings("unchecked")
    public R setMaxDuration(int maxSecond){
        this.maxSecond = maxSecond;
        return (R)this;
    }
    @SuppressWarnings("unchecked")
    public R isTempFile(boolean isTempFile){
        this.isTempFile = isTempFile;
        return (R)this;
    }

    @SuppressWarnings("unchecked")
    public R prepaerRecord(ARecorderListener aRecorderListener){
        this.aRecorderListener = aRecorderListener;
        if(aSource == null) aSource = AMode.ASource.Mic;
        if(aChannel == null) aChannel = AMode.AChannel.Mono;
        if(aSampleRate == null) aSampleRate = AMode.ASampleRate.HZ_44100;
        if(aPcmFormat == null) aPcmFormat = AMode.APcmFormat.PCM_16BIT;
        if(minSecond < EnableMinDuration) {
            if(isListenerOk()){
                aRecorderResponse.state = ARecorderResponse.StateError;
                aRecorderListener.onAudioRecorder(aRecorderResponse);
                ToastKit.show("配置最小时长太短");
                return (R)this;
            }
        }
        if(maxSecond <= minSecond) {
            if(isListenerOk()){
                aRecorderResponse.state = ARecorderResponse.StateError;
                aRecorderListener.onAudioRecorder(aRecorderResponse);
                ToastKit.show("配置最大时长小于最小时长");
                return (R)this;
            }
        }
        aRecorderResponse.filePath = filePath;
        isConfigOk = true;
        return (R)this;
    }
    public boolean startRecord(){
        if(!isConfigOk) {
            ToastKit.show("配置出错");
            return false;
        }
        return start();
    }
    public boolean stopRecord(){
        if(!isConfigOk) {
            ToastKit.show("配置出错");
            return false;
        }
        return stop();
    }
    protected boolean isListenerOk(){
        if(aRecorderListener == null) return false;
        return true;
    }
    protected void disposeFile(){
        FileKit.makeDir(XDroidConfig.DirAudio);
        if(isTempFile) filePath = XDroidConfig.DirAudio + aType.getTempName();
    }
    protected String getRealFilePath(){
        filePath = XDroidConfig.DirAudio + aType.getRealName();
        return filePath;
    }
    protected void startTimer() {
        if(observableInterval != null){return;}
        curDuration = 0;
        observableInterval = Observable.interval(1, TimeUnit.SECONDS);
        observableInterval.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if(curDuration < maxSecond){
                            curDuration++;
                            durationChange(curDuration);
                        }else{
                            ToastKit.show("录音达到最大时限");
                            stopRecord();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    protected void stopTimer() {
        if (disposable == null) return;
        disposable.dispose();
        disposable = null;
        observableInterval = null;
    }
}
