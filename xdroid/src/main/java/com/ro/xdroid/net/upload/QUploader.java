package com.ro.xdroid.net.upload;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.SparseArray;

import com.cantrowitz.rxbroadcast.RxBroadcast;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.ro.xdroid.kit.FileKit;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.mvp.XApp;
import com.ro.xdroid.net.ok.OkKit;
import com.ro.xdroid.net.ok.Okrx;
import com.ro.xdroid.net.ok.taotong.TTOkListener;
import com.ro.xdroid.net.ok.taotong.TTResponse;
import com.ro.xdroid.net.ok.taotong.model.TTComModel;
import com.ro.xdroid.net.upload.listener.UploadListener;
import com.ro.xdroid.net.upload.listener.UploadResponse;
import com.ro.xdroid.net.upload.model.TokenModel;
import com.ro.xdroid.net.upload.model.UploadGroup;
import com.ro.xdroid.net.upload.model.UploadGroupItem;
import com.ro.xdroid.net.upload.model.UploadMode;
import com.ro.xdroid.net.upload.model.UploadResultCommitParam;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
 * Created by roffee on 2017/11/24 19:06.
 * Contact with 460545614@qq.com
 */
public class QUploader<K,IK>{
    //任务key
    protected K key;
    //当前实例是否在执行，如果在执行可以关闭，但不能让另一个实例执行此实例正在执行执行的资源
    protected boolean isInstanceRuning;
    //任务组信息
    protected UploadGroup<IK> uploadGroup;
    //任务监听
    protected UploadListener<K,IK> uploadListener;
    //任务节点响应体
    protected UploadResponse<K,IK> uploadResponse;
    //通过索引查到的当前执行项
    protected UploadGroupItem<IK> curUploadGroupItem;

    //网络广播监听rx观察者
    protected Observable<Intent> netBroadcastObservable;
    //Observable 处理句柄，用于关闭订阅服务
    protected Disposable observableDisposable;
    //过滤掉开启网络监听第一次回调
    protected boolean filterFirstNetBroadcastCallback;
    //Observable 处理句柄，用于关闭订阅服务
    protected Disposable netReconnectTimerObservableDisposable;
    //ok网络请求实例
    protected Okrx okrx;

    //控制七牛上传器是否取消上传
    protected boolean cancelQUploadManagerTask;
    //准确控制真正上传有进度时才发送节点信息，否则容易产生误差，如一调上传时就报错
    protected boolean isHasSendStatusWhenStartQUpload;
    //网络中断后，记录当前任务处理状态
    protected int recordPreStatus = -1;


    public static <K,IK> QUploader<K,IK> makeQUploader(boolean... isDeleBreakpointLog){
        return new QUploader<K,IK>(isDeleBreakpointLog);
    }
    public static <K,IK> QUploader<K,IK> makeQUploader(K key, boolean... isDeleBreakpointLog){
        return new QUploader<K,IK>(isDeleBreakpointLog)
                .setKey(key);
    }
    //如果还有文件需要续传请不要设置把断点记录清除，否则将无法断点续传
    public QUploader(boolean... isDeleBreakpointLog){
        isInstanceRuning = true;
        if(!ToolsKit.isEmpty(isDeleBreakpointLog)){
            if(isDeleBreakpointLog[0]){
                FileKit.deleteFiles(new File(UploadMode.Constant.QUploadRecoderPath));
            }
        }

        uploadGroup = new UploadGroup();
        uploadResponse = new UploadResponse<>();
    }
    //配置任务key
    public QUploader<K,IK> setKey(K key){
        this.key = key;
        return this;
    }
    //获取配置任务key，如果未设置返回为空
    public K getKey(){
        return key;
    }
    //配置任务组是否必须wifi
    public QUploader<K,IK> isGroupTasksMustWifi(boolean isMustWifi){
        this.uploadGroup.isMustWifi = isMustWifi;
        return this;
    }
    ////对于某些特殊情况，文本、已上传的直接配置好map直接提交
    public QUploader<K,IK> isGroupTaskImmCommit(boolean isImmCommit){
        this.uploadGroup.isImmCommit = isImmCommit;
        uploadGroup.isResultCommit = true;
        return this;
    }
    //配置任务组是否某些资源缺失出错是否继续执行其他的
//    public QUploader<K,IK> isGroupContinueWhenSomethingError(boolean isContinueWhenSomethingError){
//        this.uploadGroup.isContinueWhenSomethingError = isContinueWhenSomethingError;
//        return this;
//    }
    //配置任务组资源大小，如果第一次传不用设置，如果已经本地信息记录可传入，避免耗时计算；纯文本情况下可以不设置或者设置0
    public QUploader<K,IK> setGroupSourceTotalAndoHadUploadSize(double sourceTotalSize, double hadUploadSize){
        uploadGroup.sourceTotalSize = sourceTotalSize;
        uploadGroup.hadUploadSize = hadUploadSize;
        return this;
    }
    //配置添加任务组单项信息，根据上传状态配置对应项信息
    public QUploader<K,IK> addTaskItemToGroup(UploadGroupItem<IK> uploadGroupItem){
        if(uploadGroupItem != null){
            if(uploadGroupItem.sourceType != null && uploadGroupItem.sourceType == UploadMode.SourceType.Text){
                uploadGroupItem.taskExecuteStep = UploadMode.TaskExecuteStep.StepUplodComplete;
                uploadGroupItem.qResultUrl = "text";
            }
            uploadGroupItem.indexKey = uploadGroup.taskCounts;
            uploadGroup.groupItems.put(uploadGroup.taskCounts, uploadGroupItem);
            uploadGroup.taskCounts += 1;
        }

        return this;
    }
    //配置添加任务组多项信息，根据上传状态配置对应项信息
    public QUploader<K,IK> addTaskItemToGroup(List<UploadGroupItem<IK>> uploadGroupItems){
        if(ToolsKit.isEmpty(uploadGroupItems)) return this;
        for (UploadGroupItem<IK> uploadGroupItem : uploadGroupItems){
            if(uploadGroupItem.sourceType != null && uploadGroupItem.sourceType == UploadMode.SourceType.Text){
                uploadGroupItem.taskExecuteStep = UploadMode.TaskExecuteStep.StepUplodComplete;
                uploadGroupItem.qResultUrl = "text";
            }
            uploadGroupItem.indexKey = uploadGroup.taskCounts;
            uploadGroup.groupItems.put(uploadGroup.taskCounts, uploadGroupItem);
            uploadGroup.taskCounts += 1;
        }

        return this;
    }
    //配置添加任务组多项信息，根据上传状态配置对应项信息
    public QUploader<K,IK> addTaskItemToGroup(SparseArray<UploadGroupItem<IK>> uploadGroupItems){
        if(ToolsKit.isEmpty(uploadGroupItems)) return this;
        for (int i = 0; i < uploadGroupItems.size(); i++){
            UploadGroupItem<IK> uploadGroupItem = uploadGroupItems.get(i);
            if(uploadGroupItem.sourceType != null && uploadGroupItem.sourceType == UploadMode.SourceType.Text){
                uploadGroupItem.taskExecuteStep = UploadMode.TaskExecuteStep.StepUplodComplete;
                uploadGroupItem.qResultUrl = "text";
            }
            uploadGroupItems.get(i).indexKey = uploadGroup.taskCounts;
            uploadGroup.groupItems.put(uploadGroup.taskCounts, uploadGroupItems.get(i));
            uploadGroup.taskCounts += 1;
        }

        return this;
    }
    //配置任务组是否让我帮你上传结果，否则将把节点上传信息告诉你你来提交结果
    public QUploader<K,IK> isGroupResultCommit(boolean isResultCommit){
        uploadGroup.isResultCommit = isResultCommit;
        return this;
    }
    //配置任务组让我帮你上传结果，需配置结果上传相关参数
    public QUploader<K,IK> setGroupResultCommitParam(UploadResultCommitParam uploadResultCommitParam){
        uploadGroup.resultCommitParam = uploadResultCommitParam;
        return this;
    }
    //配置任务组是否有额外附属参数
    public QUploader<K,IK> setGroupExtra(Object extra){
        uploadGroup.extra = extra;
        return this;
    }
    //此任务组配置完毕后可调此接口开始执行任务，掉此接口后不可再配置执行参数，否则出错
    public QUploader<K,IK> executeGroupTasks(UploadListener<K,IK> uploadListener){
        this.uploadListener = uploadListener;
        if(!isListenerOk()) return this;
        uploadResponse.key = key;
        uploadResponse.extra = uploadGroup.extra;

        prepareExecuteTask();
        return this;
    }

    public void manualCancelTasks(){
        cancelTasks(true);
    }
    //取消任务执行，认为取消请设置ture
    protected void cancelTasks(boolean...isManual){
        cancelQUploadManagerTask = true;
        closeNetworkReconnectTimer();
        closeNetBroadcastListener();
        if(okrx != null){
            okrx.forceCancel();
            okrx = null;
        }
        Observable.just(1)
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        isInstanceRuning = false;
                        if(uploadGroup != null){
                            if(uploadGroup.groupItems != null){
                                uploadGroup.groupItems.clear();
                                uploadGroup.groupItems = null;
                            }
                            uploadGroup.resultCommitParam = null;
                            uploadGroup.extra = null;
                            uploadGroup = null;
                        }
                        uploadListener = null;
//                        uploadResponse = null;
                    }
                });
    }
    //当前实例是否在执行，如果在执行可以关闭，但不能让另一个实例执行此实例正在执行执行的资源
    public boolean getInstanceIsRuning(){
        return isInstanceRuning;
    }
    //静态方法，清除路径下断点续传记录文件，可以在草稿箱中及没有在执行的任务的情况下调用，否则将影响断点续传的正常执行
    public static void clearBreakpointLog(){
        Observable.just(1)
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        FileKit.deleteFiles(new File(UploadMode.Constant.QUploadRecoderPath));
                    }
                });
    }
    protected boolean isListenerOk(){
        if(uploadListener == null) {
            cancelTasks();
            return false;
        }
        return true;
    }
    protected void postListenerResponse(UploadResponse<K,IK> uploadResponse, boolean isCancelTask){
        if(isListenerOk()) uploadListener.onUploadRespose(uploadResponse);
        if(isCancelTask) cancelTasks();
    }
    protected String getQUploadKey(@android.support.annotation.NonNull String fileFullName){
        return System.currentTimeMillis() + "_" + fileFullName;
    }
    protected String getQUploadKey(UploadMode.FileInfo fileInfo, String appendNameKey){
        if(ToolsKit.isEmpty(appendNameKey)){
            return System.currentTimeMillis() + "_" + fileInfo.fileFullName;
        }else{
            return System.currentTimeMillis() + "_" + fileInfo.fileName + "/" + appendNameKey + "." + fileInfo.mimeType;
        }
    }
    protected String getgetQUploadKeyResultUrl(){
        return UploadMode.Constant.QUploadBaseUrl + curUploadGroupItem.qUploadKey;
    }
    protected String getgetQUploadKeyResultUrl(String key){
        return UploadMode.Constant.QUploadBaseUrl + key;
    }
    protected void prepareExecuteTask(){
        //检查全局配置
        if(ToolsKit.isEmpty(UploadMode.Constant.QUploadRecoderPath)
                || ToolsKit.isEmpty(UploadMode.Constant.QGainTokenUrl)
                || ToolsKit.isEmpty(UploadMode.Constant.QUploadBaseUrl)){
            uploadResponse.status = UploadResponse.StatusError;
            uploadResponse.errorCode = UploadMode.ErrorCode.EGlobalConfigPathAndUrlError;
            postListenerResponse(uploadResponse,true);
            return ;
        }
        //检查网络
        if(uploadGroup.isMustWifi){
            if(!ToolsKit.isWifiAvailable(false)){
                uploadResponse.status = UploadResponse.StatusError;
                uploadResponse.errorCode = UploadMode.ErrorCode.EWifiUnavailable;
                postListenerResponse(uploadResponse,true);
                return ;
            }
        }else{
            if(!ToolsKit.isNetworkAvailable(false)){
                uploadResponse.status = UploadResponse.StatusError;
                uploadResponse.errorCode = UploadMode.ErrorCode.ENetworkUnavailable;
                postListenerResponse(uploadResponse, true);
                return ;
            }
        }
        //拦截uploadGroup.isImmCommit立即提交任务,拦截到后直接提交，过滤掉后面执行步骤
        if(uploadGroup.isImmCommit){
            if(uploadGroup.resultCommitParam == null || uploadGroup.resultCommitParam.commitUrl == null){
                uploadResponse.status = UploadResponse.StatusError;
                uploadResponse.errorCode = UploadMode.ErrorCode.EUploadResultCommitParamError;
                postListenerResponse(uploadResponse, true);
                return ;
            }
            resultCommitTask();
            return;
        }
        //判断上传完成后结果提交参数配置是否ok
        if(uploadGroup.isResultCommit){
            if(uploadGroup.resultCommitParam == null || uploadGroup.resultCommitParam.commitUrl == null){
                uploadResponse.status = UploadResponse.StatusError;
                uploadResponse.errorCode = UploadMode.ErrorCode.EUploadResultCommitParamError;
                postListenerResponse(uploadResponse, true);
                return ;
            }
        }
        uploadGroup.taskCounts = uploadGroup.groupItems.size();
        if(uploadGroup.taskCounts < 1){
            uploadResponse.status = UploadResponse.StatusError;
            uploadResponse.errorCode = UploadMode.ErrorCode.EGroupTasksIsEmpty;
            postListenerResponse(uploadResponse, true);
            return ;
        }
        //判断上传文件资源是否ok
        Observable.create(new ObservableOnSubscribe<UploadResponse<K,IK>>() {
            @Override
            public void subscribe(ObservableEmitter<UploadResponse<K,IK>> emitter) throws Exception {
                try {
                    boolean isNeedCalculateTotalSize = false, hasError = false, isImmCommit = true;
                    if(uploadGroup.sourceTotalSize <= 0) isNeedCalculateTotalSize = true;

                    for(int i = 0; i < uploadGroup.taskCounts; i++){
                        UploadGroupItem<IK> uploadGroupItem = uploadGroup.groupItems.get(i);

                        //先判断让我提交结果时对应提交key是否配置
                        if(uploadGroup.isResultCommit){
                            if(!uploadGroup.resultCommitParam.isUdefineParamMap && ToolsKit.isEmpty(uploadGroupItem.commitParamMapKey)){
                                uploadResponse.status = UploadResponse.StatusError;
                                uploadResponse.errorCode = UploadMode.ErrorCode.EGroupSomeItemCommitParamKeyIsNull;
                                uploadResponse.taskItemInfo.ikey = uploadGroupItem.ikey;
                                uploadResponse.taskItemInfo.positon = i;
                                hasError = true;
                                break;
                            }

                        }
                        //判断sourceType是否为空
                        if(uploadGroupItem.sourceType == null){
                            uploadResponse.status = UploadResponse.StatusError;
                            uploadResponse.errorCode = UploadMode.ErrorCode.ESourceTypeIsNull;
                            uploadResponse.taskItemInfo.ikey = uploadGroupItem.ikey;
                            uploadResponse.taskItemInfo.positon = i;
                            hasError = true;
                        }
                        //判断上传资源是否可用
                        if(uploadGroupItem.taskExecuteStep == UploadMode.TaskExecuteStep.StepUploadNone){
                            //判断执行为未处理状态的任务项
                            isImmCommit = false;
                            UploadMode.FileInfo fileInfo = UploadMode.FileInfo.getFileInfo(uploadGroupItem.source);
                            if(fileInfo == null){
                                uploadResponse.status = UploadResponse.StatusError;
                                uploadResponse.errorCode = UploadMode.ErrorCode.EGroupSomeItemSourceInexistence;
                                uploadResponse.taskItemInfo.ikey = uploadGroupItem.ikey;
                                uploadResponse.taskItemInfo.positon = i;
                                hasError = true;
                                break;
                            }else{
                                uploadGroupItem.fileInfo = fileInfo;
                                if(isNeedCalculateTotalSize) uploadGroup.sourceTotalSize += fileInfo.size;

                                uploadGroupItem.qUploadKey = getQUploadKey(fileInfo, uploadGroupItem.appendNameKey);

                                if(uploadGroup.curExecuteIndex < 0) uploadGroup.curExecuteIndex = i;
                            }
                        }else if(uploadGroupItem.taskExecuteStep == UploadMode.TaskExecuteStep.StepUplodPart){
                            //判断执行为上传一部分状态的任务项参数配置
                            isImmCommit = false;
                            if(ToolsKit.isEmpty(uploadGroupItem.qUploadKey) || uploadGroup.hadUploadSize < 0){
                                uploadResponse.status = UploadResponse.StatusError;
                                uploadResponse.errorCode = UploadMode.ErrorCode.EConfigHadUploadInifoError;
                                uploadResponse.taskItemInfo.ikey = uploadGroupItem.ikey;
                                uploadResponse.taskItemInfo.positon = i;
                                hasError = true;
                                break;
                            }
                            UploadMode.FileInfo fileInfo = UploadMode.FileInfo.getFileInfo(uploadGroupItem.source);
                            if(fileInfo == null){
                                uploadResponse.status = UploadResponse.StatusError;
                                uploadResponse.errorCode = UploadMode.ErrorCode.EGroupSomeItemSourceInexistence;
                                uploadResponse.taskItemInfo.ikey = uploadGroupItem.ikey;
                                uploadResponse.taskItemInfo.positon = i;
                                hasError = true;
                                break;
                            }else{
                                uploadGroupItem.fileInfo = fileInfo;
                                if(isNeedCalculateTotalSize) uploadGroup.sourceTotalSize += fileInfo.size;

                                if(uploadGroup.curExecuteIndex < 0) uploadGroup.curExecuteIndex = i;
                            }
                        }else{
                            //判断执行为上传完毕状态的任务项参数配置}
//                            if(ToolsKit.isEmpty(uploadGroupItem.qResultUrl) || uploadGroupItem.hadCompleteUploadsSurceSize <= 0 || uploadGroup.hadUploadSize <= 0){
                            //如果纯文本时，不用设置size
                            if(ToolsKit.isEmpty(uploadGroupItem.qResultUrl)){
                                uploadResponse.status = UploadResponse.StatusError;
                                uploadResponse.errorCode = UploadMode.ErrorCode.EConfigHadUploadInifoError;
                                uploadResponse.taskItemInfo.ikey = uploadGroupItem.ikey;
                                uploadResponse.taskItemInfo.positon = i;
                                hasError = true;
                                break;
                            }else{
                                uploadGroup.hadCompleteUploadItemsTotalSize += uploadGroupItem.hadCompleteUploadsSurceSize;
                            }
                        }
                    }
                    if(!hasError){
                        if(isImmCommit){
                            uploadResponse.status = UploadResponse.StatusPrepareResultCommit;
                            uploadResponse.errorCode = UploadMode.ErrorCode.EOk;
                        }else{
                            if(uploadGroup.curExecuteIndex < 0 || uploadGroup.curExecuteIndex >= uploadGroup.taskCounts){
                                uploadResponse.status = UploadResponse.StatusError;
                                uploadResponse.errorCode = UploadMode.ErrorCode.EGroupUpItemExecuteIndexError;
                            }else{
                                uploadResponse.status = UploadResponse.StatusSomeItemStartUpload;
                                uploadResponse.errorCode = UploadMode.ErrorCode.EOk;
                            }
                        }
                    }

                    emitter.onNext(uploadResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(new Throwable("Exception"));
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UploadResponse<K,IK>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull UploadResponse<K,IK> uploadResponse) {
                        if(uploadResponse.status == UploadResponse.StatusError){
                            if(isListenerOk()){
                                postListenerResponse(uploadResponse, true);
                            }
                        }else if(uploadResponse.status == UploadResponse.StatusSomeItemStartUpload){
                            if(!getCurUploadGroupItem(uploadGroup.curExecuteIndex, false)) return;
                            openNetBroadcastListener();
                            gainUploadToken();
//                            startUploadTask();
                        }else if(uploadResponse.status == UploadResponse.StatusPrepareResultCommit){
                            openNetBroadcastListener();
                            resultCommitTask();
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

    protected void gainUploadToken(){
        okrx = OkKit.request(0, UploadMode.Constant.QGainTokenUrl, TokenModel.class)
                .isAutoToast(false)
                .isBindToLifecycle(false)
                .execute(new TTOkListener<Integer, TokenModel>() {
                    @Override
                    public void onTTOkResponse(TTResponse<Integer, TokenModel> ttResponse) throws Throwable {
                        okrx = null;
                        if(ttResponse.isSuccess){
                            TokenModel tokenModel = ttResponse.data;
                            if(tokenModel.getStatus() == 1){
                                uploadGroup.tokenTimestamp = System.currentTimeMillis();
                                uploadGroup.token = tokenModel.getData().getToken();
                                qUploadTask();
                            }else{
                                uploadResponse.status = UploadResponse.StatusError;
                                uploadResponse.errorCode = UploadMode.ErrorCode.EGroupSomeItemUploadGetTokenFailed;
                                uploadResponse.taskItemInfo.ikey = curUploadGroupItem.ikey;
                                uploadResponse.taskItemInfo.positon = uploadGroup.curExecuteIndex;
                                postListenerResponse(uploadResponse, true);
                            }
                        }else{
                            uploadResponse.status = UploadResponse.StatusError;
                            uploadResponse.errorCode = UploadMode.ErrorCode.EGroupSomeItemUploadGetTokenFailed;
                            uploadResponse.taskItemInfo.positon = uploadGroup.curExecuteIndex;
                            postListenerResponse(uploadResponse, true);
                        }
                    }
                });
    }
    protected boolean getCurUploadGroupItem(int excuteIndex, boolean isLoop){
        boolean isOk = true;
        curUploadGroupItem = null;
        try {
            if(isLoop && excuteIndex >= uploadGroup.groupItems.size() ){
                excuteIndex = uploadGroup.groupItems.size() - excuteIndex;
            }
            curUploadGroupItem = uploadGroup.groupItems.get(excuteIndex);
        } catch (Exception e) {
            e.printStackTrace();
            isOk = false;
        }
        if(curUploadGroupItem == null || !isOk){
            uploadResponse.status = UploadResponse.StatusError;
            uploadResponse.errorCode = UploadMode.ErrorCode.EGroupUpItemExecuteIndexError;
            postListenerResponse(uploadResponse, true);
            return false;
        }
        return true;
    }
    protected void qUploadTask(){
//        if(uploadRespose.status == UploadRespose.StatusStartUpload)
        UploadManager uploadManager;
        //创建上传器
        try {
            uploadManager = new UploadManager(new FileRecorder(UploadMode.Constant.QUploadRecoderPath));
        } catch (IOException e) {
            e.printStackTrace();
            uploadResponse.status = UploadResponse.StatusError;
            uploadResponse.errorCode = UploadMode.ErrorCode.ECreateQuploaderIOError;
            uploadResponse.taskItemInfo.ikey = curUploadGroupItem.ikey;
            uploadResponse.taskItemInfo.positon = uploadGroup.curExecuteIndex;
            postListenerResponse(uploadResponse, true);
            return;
        }
        //开始执行上传
        uploadResponse.status = UploadResponse.StatusSomeItemStartUpload;
        uploadResponse.errorCode = UploadMode.ErrorCode.EOk;
        uploadResponse.sourceTotalSize = uploadGroup.sourceTotalSize;
        uploadResponse.hadUploadSize = uploadGroup.hadUploadSize;
        uploadResponse.taskItemInfo.ikey = curUploadGroupItem.ikey;
        uploadResponse.taskItemInfo.positon = uploadGroup.curExecuteIndex;
        uploadResponse.taskItemInfo.taskExecuteStep = curUploadGroupItem.taskExecuteStep = UploadMode.TaskExecuteStep.StepUplodPart;
        uploadResponse.taskItemInfo.source = curUploadGroupItem.source;
        uploadResponse.taskItemInfo.qUploadKey = curUploadGroupItem.qUploadKey;
        uploadResponse.taskItemInfo.sourceSize = curUploadGroupItem.fileInfo.size;
        postListenerResponse(uploadResponse, false);

        uploadGroup.upProgressLastTime = System.currentTimeMillis();
        uploadManager.put(curUploadGroupItem.source, curUploadGroupItem.qUploadKey, uploadGroup.token,
                new UpCompletionHandler(){
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        Observable.just(info.isOK())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        if (aBoolean) {
                                            //此项资源上传完毕
                                            uploadResponse.status = UploadResponse.StatusSomeItemUploaded;
                                            uploadResponse.hadUploadSize = uploadGroup.hadCompleteUploadItemsTotalSize + curUploadGroupItem.fileInfo.size;
                                            uploadResponse.taskItemInfo.ikey = curUploadGroupItem.ikey;
                                            uploadResponse.taskItemInfo.positon = uploadGroup.curExecuteIndex;
                                            uploadResponse.taskItemInfo.taskExecuteStep = curUploadGroupItem.taskExecuteStep = UploadMode.TaskExecuteStep.StepUplodComplete;
                                            uploadResponse.taskItemInfo.source = curUploadGroupItem.source;
                                            uploadResponse.taskItemInfo.sourceSize = curUploadGroupItem.hadCompleteUploadsSurceSize = curUploadGroupItem.fileInfo.size;
                                            uploadResponse.taskItemInfo.qResultUrl = curUploadGroupItem.qResultUrl = getgetQUploadKeyResultUrl(key);
                                            postListenerResponse(uploadResponse, false);

                                            //统计数据，检测是否有下一项要上传或要结果提交
                                            checkExecuteNext();
                                        }else{
                                            uploadResponse.status = UploadResponse.StatusError;
                                            uploadResponse.errorCode = info.statusCode + UploadMode.Constant.QUploadManagerErrorIncr;
                                            uploadResponse.taskItemInfo.ikey = curUploadGroupItem.ikey;
                                            if (uploadGroup!=null) {
                                                uploadResponse.taskItemInfo.positon = uploadGroup.curExecuteIndex;
                                            }
                                            if(info.statusCode == ResponseInfo.NetworkError
                                                    || (info.statusCode >= ResponseInfo.NetworkConnectionLost && info.statusCode <= ResponseInfo.TimedOut)){
                                                postListenerResponse(uploadResponse, false);
                                            }else{
                                                postListenerResponse(uploadResponse, true);
                                            }
                                        }
                                    }
                                });
                    }
                },
                new UploadOptions(null, null, false, new UpProgressHandler(){
                    @Override
                    public void progress(String key, double percent) {
                        disposeProgress(percent);
                    }
                },
                        new UpCancellationSignal(){
                            @Override
                            public boolean isCancelled() {
                                //是否暂停任务
                                return cancelQUploadManagerTask;
                            }
                        })
        );
    }
    protected void checkExecuteNext(){
        int startIndex = uploadGroup.curExecuteIndex + 1;
        boolean isHasItemNoneUpload = false;
        for(int i = startIndex; i < startIndex + uploadGroup.taskCounts; i++){
             getCurUploadGroupItem(startIndex, true);
             if(curUploadGroupItem == null) return;
            if(curUploadGroupItem.taskExecuteStep == UploadMode.TaskExecuteStep.StepUploadNone ||
                    curUploadGroupItem.taskExecuteStep == UploadMode.TaskExecuteStep.StepUplodPart){
                isHasItemNoneUpload = true;
                uploadGroup.curExecuteIndex = curUploadGroupItem.indexKey;
                break;
            }
        }
        if(isHasItemNoneUpload){
            gainUploadToken();
        }else{
            resultCommitTask();
        }
    }
    protected void resultCommitTask(){
        if(uploadGroup.isResultCommit){
            uploadResponse.status = UploadResponse.StatusPrepareResultCommit;
            uploadResponse.isResultCommit = true;
            uploadResponse.uploadResultCommitParam = uploadGroup.resultCommitParam;
            postListenerResponse(uploadResponse, false);

            if(!uploadGroup.resultCommitParam.isUdefineParamMap && !uploadGroup.isImmCommit){
                for(int i = 0; i < uploadGroup.taskCounts; i++){
                    UploadGroupItem uploadGroupItem = uploadGroup.groupItems.get(i);
                    if(uploadGroupItem != null){
                        uploadGroup.resultCommitParam.paramMap.put(uploadGroupItem.commitParamMapKey, uploadGroupItem.qResultUrl);
                    }
                }
            }
            okrx = OkKit.request(1, uploadGroup.resultCommitParam.commitUrl, TTComModel.class)
                    .isAutoToastInfo(false)
                    .isBindToLifecycle(false)
                    .putParams(uploadGroup.resultCommitParam.paramMap)
                    .execute(new TTOkListener<Integer, TTComModel>() {
                        @Override
                        public void onTTOkResponse(TTResponse<Integer, TTComModel> ttResponse) throws Throwable {
                            okrx = null;
                            if(ttResponse.isSuccess){
                                TTComModel ttComModel = ttResponse.data;
                                if(ttComModel.status == 1){
                                    uploadResponse.status = UploadResponse.StatusCompleted;
                                    uploadResponse.errorCode = UploadMode.ErrorCode.EOk;
                                    uploadResponse.isResultCommit = true;
                                    uploadResponse.resultCommitData = ttComModel;
                                    postListenerResponse(uploadResponse, true);
                                }else{
                                    uploadResponse.status = UploadResponse.StatusError;
                                    uploadResponse.errorCode = UploadMode.ErrorCode.EUploadResultCommitFailed;
                                    uploadResponse.isResultCommit = true;
                                    postListenerResponse(uploadResponse, true);
                                }
                            }else{
                                uploadResponse.status = UploadResponse.StatusError;
                                uploadResponse.errorCode = UploadMode.ErrorCode.EUploadResultCommitFailed;
                                uploadResponse.isResultCommit = true;
                                postListenerResponse(uploadResponse, true);
                            }
                        }
                    });

        }else{
            uploadResponse.status = UploadResponse.StatusCompleted;
            uploadResponse.isResultCommit = false;
            postListenerResponse(uploadResponse, true);
        }
    }

    protected void disposeProgress(double percent){
        if(!isListenerOk()) return;
        long now = System.currentTimeMillis();
        long deltaTime = now - uploadGroup.upProgressLastTime;
        if (deltaTime < UploadMode.Constant.UploadProgressUpdateInterval && percent != 1) {
            return;
        }
        double curPartUpSize = percent*curUploadGroupItem.fileInfo.size;
        double wholePercent = (curPartUpSize + uploadGroup.hadCompleteUploadItemsTotalSize)*1.00f/uploadGroup.sourceTotalSize*1.00f;
        double wholePartOffset = wholePercent*uploadGroup.sourceTotalSize;
        double deltaSize = curPartUpSize - uploadGroup.upProgressLastOffset;
        double speed = FileKit.getSpeed(deltaSize, deltaTime);
        // update
        uploadGroup.upProgressLastTime = now;
        uploadGroup.upProgressLastOffset = curPartUpSize;
        //set progress

        uploadResponse.status = UploadResponse.StatusSomeItemUploading;
        uploadResponse.hadUploadSize = wholePartOffset;
        uploadResponse.progress.percent = wholePercent;
        uploadResponse.progress.speed = speed;
        postListenerResponse(uploadResponse, false);
    }
    //开启网络监听
    protected void openNetBroadcastListener(){
        if(netBroadcastObservable != null){
            return;
        }
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

        netBroadcastObservable = RxBroadcast.fromBroadcast(XApp.getContext(), intentFilter);
        netBroadcastObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Intent>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                observableDisposable = d;
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Intent intent) {
                if(!filterFirstNetBroadcastCallback){
                    filterFirstNetBroadcastCallback = true;
                    return;
                }

                boolean hasNet = false;
                if(uploadGroup.isMustWifi){
                    hasNet = ToolsKit.isWifiAvailable(false);
                }else{
                    hasNet = ToolsKit.isNetworkAvailable(false);
                }
                cancelQUploadManagerTask = !hasNet;

                if(cancelQUploadManagerTask){
                    if(netReconnectTimerObservableDisposable != null) return;
                    if(recordPreStatus < 0) recordPreStatus = uploadResponse.status;
                    //任务暂停，进行重连
                    uploadResponse.status = UploadResponse.StatusNetFactorTaskInterrupt;
                    uploadResponse.errorCode = UploadMode.ErrorCode.EUploadNetFactorTaskInterrupt;
                    postListenerResponse(uploadResponse, false);

                    //网络中断，启动定时器
                    Observable.just(1)
                            .delay(UploadMode.Constant.TimerForNetFactorTaskInterruptOuttime, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    netReconnectTimerObservableDisposable = d;
                                }

                                @Override
                                public void onNext(Integer integer) {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                    closeNetworkReconnectTimer();
                                    recordPreStatus = -1;
                                    uploadResponse.status = UploadResponse.StatusError;
                                    uploadResponse.errorCode = UploadMode.ErrorCode.ENetReconnectOutTime;
                                    postListenerResponse(uploadResponse, true);
                                }
                            });
                }else{
                    //任务继续
                    closeNetworkReconnectTimer();
                    if(uploadResponse.status > UploadResponse.StatusNetFactorTaskInterrupt || recordPreStatus < 0) return;
                    if(recordPreStatus == UploadResponse.StatusSomeItemStartUpload || recordPreStatus == UploadResponse.StatusSomeItemUploading){
                        if((System.currentTimeMillis() - uploadGroup.tokenTimestamp) < UploadMode.Constant.TokenExpire){
                            //toaken过去,需重新获取
                            qUploadTask();
                        }else{
                            gainUploadToken();
                        }
                    }else if(recordPreStatus == UploadResponse.StatusSomeItemUploaded){
                        checkExecuteNext();
                    }else if(recordPreStatus == UploadResponse.StatusPrepareResultCommit){
                        resultCommitTask();
                    }else if(recordPreStatus == UploadResponse.StatusError){
                        checkExecuteNext();
                    }
//                    else{
//                        uploadResponse.status = UploadResponse.StatusError;
//                        uploadResponse.errorCode = UploadMode.ErrorCode.ELogicalException;
//                        postListenerResponse(uploadResponse, true);
//                    }
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    //关闭网络状态监听服务
    protected void closeNetBroadcastListener(){
        try {
            if(observableDisposable != null){
                observableDisposable.dispose();
                observableDisposable = null;
            }
            netBroadcastObservable = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void closeNetworkReconnectTimer(){
        if(netReconnectTimerObservableDisposable != null){
            netReconnectTimerObservableDisposable.dispose();
            netReconnectTimerObservableDisposable = null;
        }
    }
}
