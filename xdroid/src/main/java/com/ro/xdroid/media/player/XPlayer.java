package com.ro.xdroid.media.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ro.xdroid.kit.FileKit;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.kit.UriKit;
import com.ro.xdroid.media.player.listener.XPlayerListener;
import com.ro.xdroid.media.player.listener.XPlayerResponse;
import com.ro.xdroid.view.dialog.ToastKit;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ro.xdroid.media.player.XPlayerMode.ProgressUpdateInterval;

/**
 * Created by roffee on 2017/8/29 11:42.
 * Contact with 460545614@qq.com
 */
public abstract class XPlayer<M extends XPlayer> {
    protected MediaPlayer mediaPlayer;
    protected XPlayerMode.MType mType;
    protected XPlayerMode.MSourceType mSourceType;
    protected XPlayerListener XPlayerListener;
    protected XPlayerResponse XPlayerResponse;
    protected Context context;
    protected Object destSource; //url/path/file/resouceid/uri
    protected boolean isAutoToast = true;

    protected Observable<Long> observableInterval;
    protected Disposable disposable;
    protected boolean isAutoPlay = false; //是否自动播
    protected boolean isSeekAutoPlay = true; //是否加载完播
    protected boolean isLoop = false;//是否循环播
    protected boolean isBufferingCompleted = false;
    protected boolean isPrepareOk = false;
    protected int seekType = 0; //0:manual seek  1:play complete seek
//    private int preErrorWhat, preErrorExtra;

    public XPlayer(XPlayerMode.MType mType){
        this(mType, null);
    }
    public XPlayer(XPlayerMode.MType mType, XPlayerMode.MSourceType mSourceType){
        this.mType = mType;
        this.mSourceType = mSourceType;

        mediaPlayer = new MediaPlayer();

        XPlayerResponse = new XPlayerResponse();
        XPlayerResponse.mType = mType;
        XPlayerResponse.isOk = true;
    }
    @SuppressWarnings("unchecked")
    public M setSource(String url){
        this.destSource = url;
        this.mSourceType = XPlayerMode.MSourceType.NET_STREAM;
        return (M) this;
    }
    @SuppressWarnings("unchecked")
    public M setSource(File file){
        this.destSource = file;
        this.mSourceType = XPlayerMode.MSourceType.LOCAL_FILE;
        return (M) this;
    }
    @SuppressWarnings("unchecked")
    public M setSource(int resId){
        this.destSource = resId;
        this.mSourceType = XPlayerMode.MSourceType.LOCAL_RESOURCE;
        return (M) this;
    }
    @SuppressWarnings("unchecked")
    public M setSource(Context context, Uri uri){
        this.context = context;
        this.destSource = uri;
        this.mSourceType = XPlayerMode.MSourceType.URI;
        return (M) this;
    }
//    public MediaPlay setMediaListener(MediaListener mediaListener){
//        this.mediaListener = mediaListener;
//        return this;
//    }
    @SuppressWarnings("unchecked")
    public M isAutoToast(boolean isAutoToast){
        this.isAutoToast = isAutoToast;
        return (M) this;
    }

    @SuppressWarnings("unchecked")
    public M isAutoPlay(boolean isAutoPlay){
        this.isAutoPlay = isAutoPlay;
        return (M) this;
    }
    @SuppressWarnings("unchecked")
    public M isSeekAutoPlay(boolean isSeekAutoPlay){
        this.isSeekAutoPlay = isSeekAutoPlay;
        return (M) this;
    }
    @SuppressWarnings("unchecked")
    public M isLoop(boolean isLoop){
        this.isLoop = isLoop;
        return (M) this;
    }
    @SuppressWarnings("unchecked")
    public M prePlay(XPlayerListener XPlayerListener){
        this.XPlayerListener = XPlayerListener;
        if(destSource == null){
            XPlayerResponse.isOk = false;
            XPlayerResponse.state = XPlayerMode.MState.ERROR;
            XPlayerResponse.toast = "资源参数为空！";
            if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
            if(isAutoToast)ToastKit.show(XPlayerResponse.toast);
            return (M) this;
        }
        if(!isSupportMimeType()){
            XPlayerResponse.isOk = false;
            XPlayerResponse.state = XPlayerMode.MState.ERROR;
            XPlayerResponse.toast = "音频格式不支持！";
            if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
            if(isAutoToast)ToastKit.show(XPlayerResponse.toast);
            return (M) this;
        }
        try {
            mediaPlayer.reset();
            if(mSourceType == XPlayerMode.MSourceType.NET_STREAM){
                mediaPlayer.setDataSource((String) destSource);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }else if(mSourceType == XPlayerMode.MSourceType.LOCAL_FILE){
                mediaPlayer.setDataSource(((File) destSource).getPath());
            }else if(mSourceType == XPlayerMode.MSourceType.LOCAL_RESOURCE){
                mediaPlayer.setDataSource(context, UriKit.getUriForResourceId((int) destSource));
            }else if(mSourceType == XPlayerMode.MSourceType.URI){
                mediaPlayer.setDataSource(context, (Uri)destSource);
                if(UriKit.isNetworkUri((Uri)destSource)){
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
            }
            XPlayerResponse.state = XPlayerMode.MState.PREPARE;
            setListener();
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            XPlayerResponse.isOk = false;
            XPlayerResponse.state = XPlayerMode.MState.ERROR;
            XPlayerResponse.toast = "准备播放异常！";
            if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
            if(isAutoToast)ToastKit.show(XPlayerResponse.toast);
        }
        return (M) this;
    }
    public void seekTo(int msec){
        if(!isPrepareOk){
            if(isAutoToast)ToastKit.show("加载准备中，请稍后！");
            return;
        }
        seekType = 0;
        if(msec < 0) msec = 0;
        else if(msec > mediaPlayer.getDuration()) msec = mediaPlayer.getDuration();
//        if(mediaPlayer.isPlaying()) mediaPlayer.pause();
        mediaPlayer.seekTo(msec);
    }
    public void play(){
        if(!isPrepareOk){
            if(isAutoToast)ToastKit.show("加载准备中，请稍后！");
            return;
        }
        if(mediaPlayer == null) return;
        if(mediaPlayer.isPlaying()){return;}
        mediaPlayer.start();
        startTimer();
        XPlayerResponse.state = XPlayerMode.MState.STARTPLAY;
        XPlayerResponse.playedDuration = mediaPlayer.getCurrentPosition();
        if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
    }
    public void pause() {
        if(!isPrepareOk){
//            if(isAutoToast)ToastKit.show("加载准备中，请稍后！");
            return;
        }
        cancelTimer();
        if(mediaPlayer == null) return;
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                XPlayerResponse.state = XPlayerMode.MState.PAUSE;
                XPlayerResponse.playedDuration = mediaPlayer.getCurrentPosition();
                if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public boolean isPlaying(){
        if(mediaPlayer == null) return false;
        try {
            return mediaPlayer.isPlaying();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public int getState(){
        return XPlayerResponse.state;
    }
    public MediaPlayer getMediaPlay(){ return mediaPlayer; }
//    public void stop() {
//        try {
//            if (mediaPlayer.isPlaying()) {
//                mediaPlayer.stop();
//                cancelTimer();
//            }
//            mediaPlayer.release();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void release() {
        try {
            cancelTimer();
            if (mediaPlayer != null) {
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.setOnErrorListener(null);
                    mediaPlayer.setOnPreparedListener(null);
                    mediaPlayer.setOnCompletionListener(null);
                    mediaPlayer.setOnSeekCompleteListener(null);
                    mediaPlayer.setOnBufferingUpdateListener(null);
                    mediaPlayer.reset();
                    mediaPlayer.release();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                mediaPlayer = null;
            }
            observableInterval = null;
            XPlayerListener = null;
            XPlayerResponse = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected boolean isListenerLifecycle() {
        if (XPlayerListener == null) return true;
        return false;
    }

    protected void startTimer() {
        if(observableInterval != null){return;}
        observableInterval = Observable.interval(ProgressUpdateInterval, TimeUnit.MILLISECONDS);
        observableInterval.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        try {
                            if (mediaPlayer.isPlaying()) {
                                XPlayerResponse.state = XPlayerMode.MState.PALYING;
//                                mediaResponse.totalDuration = mediaPlayer.getDuration();
                                XPlayerResponse.playedDuration = mediaPlayer.getCurrentPosition();
                                if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
                            } else {
                                cancelTimer();
                                XPlayerResponse.state = XPlayerMode.MState.PAUSE;
                                XPlayerResponse.playedDuration = mediaPlayer.getCurrentPosition();
                                if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    protected void cancelTimer() {
        if (disposable == null) return;
        disposable.dispose();
        disposable = null;
        observableInterval = null;
    }
    private void setListener(){
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                cancelTimer();
//                if(preErrorWhat == i && preErrorExtra == i1){
//                    return true;
//                }
//                preErrorWhat = i; preErrorExtra = i1;
                XPlayerResponse.state = XPlayerMode.MState.ERROR;
                XPlayerResponse.toast = "播放出错-what:" + i + "  extra:" + i1;
                if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
                if(isAutoToast)ToastKit.show(XPlayerResponse.toast);
                return true;
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                isPrepareOk = true;
                XPlayerResponse.state = XPlayerMode.MState.PREPARE;
                XPlayerResponse.totalDuration = mediaPlayer.getDuration();
                XPlayerResponse.playedDuration = 0;
                if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }

                if(isAutoPlay){
                    mediaPlayer.start();
                    startTimer();
                    XPlayerResponse.state = XPlayerMode.MState.STARTPLAY;
//                    XPlayerResponse.totalDuration = mediaPlayer.getDuration();
                    XPlayerResponse.playedDuration = mediaPlayer.getCurrentPosition();
                    if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                cancelTimer();
                seekType = 1;
                mediaPlayer.seekTo(0);
                XPlayerResponse.state = XPlayerMode.MState.COMPLETE;
                XPlayerResponse.playedDuration = mediaPlayer.getCurrentPosition();
                if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
            }
        });

        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                if(seekType == 0){
                    if(isSeekAutoPlay){
                        mediaPlayer.start();
                        startTimer();
                        XPlayerResponse.state = XPlayerMode.MState.PALYING;
                        XPlayerResponse.playedDuration = mediaPlayer.getCurrentPosition();
                        if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
                    }
                }else{
                    if(isLoop){
                        mediaPlayer.start();
                        startTimer();
                        XPlayerResponse.state = XPlayerMode.MState.PALYING;
                        XPlayerResponse.playedDuration = mediaPlayer.getCurrentPosition();
                        if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
                    }
                }

            }
        });
        if(mSourceType == XPlayerMode.MSourceType.NET_STREAM ||
                (mSourceType == XPlayerMode.MSourceType.URI && UriKit.isNetworkUri((Uri)destSource))){
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    if(isBufferingCompleted) return;
                    if(i == 100) isBufferingCompleted = true;
                    if(XPlayerResponse.state != XPlayerMode.MState.BUFFERINT) XPlayerResponse.state = XPlayerMode.MState.BUFFERINT;
                    XPlayerResponse.bufferintPercent = (float) i / 100 * 1.0f;
                    if(!isListenerLifecycle()){ XPlayerListener.onMediaListner(XPlayerResponse); }
                }
            });
        }
    }
    private boolean isSupportMimeType(){
        String suffix = null;
        if(mSourceType == XPlayerMode.MSourceType.NET_STREAM){
            suffix = FileKit.getSuffix((String) destSource).toLowerCase();
        }else if(mSourceType == XPlayerMode.MSourceType.LOCAL_FILE){
            suffix = FileKit.getSuffix(((File) destSource).getPath()).toLowerCase();
        }else if(mSourceType == XPlayerMode.MSourceType.LOCAL_RESOURCE){
            return true;
        }else if(mSourceType == XPlayerMode.MSourceType.URI){
            if(UriKit.isNetworkUri((Uri)destSource) || UriKit.isLocalFileUri((Uri)destSource)){
                suffix = FileKit.getSuffix(((Uri)destSource).getPath()).toLowerCase();
            }else{
                return true;
            }
        }
        if(ToolsKit.isEmpty(suffix)){return false;}
        for(String s : XPlayerMode.AUDIO_SUPPORT_ARRAY){
            if(s.equals(suffix)){
                return true;
            }
        }
        return false;
    }
}
