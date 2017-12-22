package com.ro.xdroid.net.ok.taotong;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.net.error.ErrorHandler;
import com.ro.xdroid.net.ok.OkKit;
import com.ro.xdroid.net.ok.OkMode;
import com.ro.xdroid.net.ok.Okdl;
import com.ro.xdroid.net.ok.taotong.model.TTTokenModel;
import com.ro.xdroid.view.dialog.ToastKit;

import java.io.File;

/**
 * Created by roffee on 2017/8/17 09:45.
 * Contact with 460545614@qq.com
 */
public class TTDL<K> {
    private static final long QiniuTokenDefualtExpires = 3600*3*1000;//OkMode.LoadMode.TOKEN_EXPIRE;
    private Okdl<K> okdl;
    private K key;
    private String url;
    private String tokenUrl;
    private TTResponse<K, File> fileTTResponse;

    private String destFileName;
    private boolean isAutoCancel = false;
    private boolean isBreakpoint;
    private boolean isCover = true;
    private boolean isBindToLifecycle = false;
    private boolean isResume;
    private TTOkListener<K, File> ttOkListener;


    private long tokenTimestamp;//拿到token时的时间戳
    private TTTokenModel ttTokenModel;

    public TTDL(){
        okdl =  new Okdl<>();
        fileTTResponse = new TTResponse<>();
    }
    public TTDL<K> makeRequest(K key, String url, String tokenUrl){
        this.key = key;
        this.url = url;
        this.tokenUrl = tokenUrl;
        return this;
    }
    public TTDL<K> isAutoCancel(boolean isAutoCancel){
        this.isAutoCancel = isAutoCancel;
        return this;
    }
    public TTDL<K> isBreakpoint(boolean isBreakpoint, String destFileName){
        this.isBreakpoint = isBreakpoint;
        this.destFileName = destFileName;
        return this;
    }
    public TTDL<K> isBindToLifecycle(boolean isBindToLifecycle){
        this.isBindToLifecycle = isBindToLifecycle;
        return this;
    }
    public TTDL<K> isCover(boolean isCover){
        this.isCover = isCover;
        return this;
    }
    public TTDL<K> execute(TTOkListener<K, File> ttOkListener){
        this.ttOkListener = ttOkListener;
        fileTTResponse.key = key;
        getToken();
        return this;
    }
    public void pause(){
        if(okdl == null){return;}
        isResume = false;
        okdl.pause();
    }
    public void resume(){
        if(okdl == null){return;}
        isResume = true;
        if(System.currentTimeMillis() - tokenTimestamp < OkMode.LoadMode.TOKEN_EXPIRE
                && ttTokenModel != null && ttTokenModel.status == 1){
            okdl.resume();
        }else{
            getToken();
        }
    }
    public void delete(String... destFileName){
        if(okdl == null){return;}
        okdl.delete(destFileName);
    }
    public void close(){
        if(okdl != null){okdl.forceCancel();}
        okdl = null;
        fileTTResponse = null;
        ttOkListener = null;
        ttTokenModel = null;
    }
    private void getToken(){
        OkKit.<K, TTTokenModel>request(key, tokenUrl, TTTokenModel.class)
                .execute(new TTOkListener<K, TTTokenModel>() {
                    @Override
                    public void onTTOkResponse(TTResponse<K, TTTokenModel> ttResponse) {
                        if(ttResponse.isSuccess){
                            ttTokenModel = ttResponse.data;
                            if(ttTokenModel.status == 1){
                                tokenTimestamp = System.currentTimeMillis();
                                startLoad();
                            }else{
                                ttTokenModel = null;
                                if(ttOkListener != null){
                                    fileTTResponse.isSuccess = false;
                                    fileTTResponse.toast = "获取token失败！";
                                    try {
                                        ttOkListener.onTTOkResponse(fileTTResponse);
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                        ToastKit.show(ErrorHandler.getError(throwable));
                                    }
                                }
                            }
                        }
                    }
                });
    }
    private void startLoad(){
        if(okdl == null){return;}
        if(isResume){
            resumeLoad();
            return;
        }
        okdl.makeRequest(key, formatUrlWithToken())
                .isAutoCancel(isAutoCancel)
                .isBindToLifecycle(isBindToLifecycle)
                .isBreakpoint(isBreakpoint, destFileName)
                .isCover(isCover)
                .execute(new TTOkListener<K, File>() {
                    @Override
                    public void onTTOkResponse(TTResponse<K, File> ttResponse) {
                        if(ttOkListener != null){
                            fileTTResponse = ttResponse;
                            try {
                                ttOkListener.onTTOkResponse(ttResponse);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                                ToastKit.show(ErrorHandler.getError(throwable));
                            }
                        }
                    }
                });
    }
    private void resumeLoad() {
        if (okdl == null || !isResume) {
            return;
        }
        okdl.resume(formatUrlWithToken());
    }
    private String formatUrlWithToken(){
        if(ttTokenModel == null || ToolsKit.isEmpty(ttTokenModel.data.token)){return url;}
        return url + "?e=" + QiniuTokenDefualtExpires + "&token=" + ttTokenModel.data.token;
        //http://<domain>/<key>?e=<deadline>&token=<downloadToken>
    }
}
