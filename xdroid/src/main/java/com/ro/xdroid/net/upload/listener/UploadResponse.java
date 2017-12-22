package com.ro.xdroid.net.upload.listener;

import com.ro.xdroid.net.upload.model.UploadResultCommitParam;

/**
 * Created by roffee on 2017/11/25 13:39.
 * Contact with 460545614@qq.com
 */
public class UploadResponse<K,IK> {
    //做些准备工作，如检查配置信息是否正常，如出错进入error状态，否则进入开始上传状态
    public static final int StatusPrepare = 0;
    //执行出现错误，会释放实例
    public static final int StatusError = 1;
    //执行网络中断， TimerForNetFactorTaskInterruptOuttime = 3*60s内重连，此阶段内不释放实例，如果重连失败转入error状态；此时会返回此项部分信息，避免异常中断信息丢失无法记录，so有些需记录信息保存; TaskItemInfo；此阶段不释放执行，
    public static final int StatusNetFactorTaskInterrupt = 2;
    //开始上传某项资源,此时会返回此项部分信息，避免异常中断信息丢失无法记录，so有些需记录信息保存; totalSize、TaskItemInfo
    public static final int StatusSomeItemStartUpload = 3;
    //某项执行上传中，进度进行中，返回progress
    public static final int StatusSomeItemUploading = 4;
    //某项上传完毕，统计记录此项数据
    public static final int StatusSomeItemUploaded = 5;
    //任务组执行上传都完毕后的提交工作，此状态下如果需要我提交结果并且是自定义，需你配置UploadResultCommitParam中paramMap
    public static final int StatusPrepareResultCommit = 6;
    //任务组执行完毕，此任务组本地记录可以清除了，会释放实例
    public static final int StatusCompleted = 7;

    //任务key
    public K key;
    //任务状态
    public int status;
    //错误码值
    public int errorCode;


    //任务组资源总大小，需记录，后续设置使用；任务组开始执行上传时返回StatusSomeItemStartUpload
    public double sourceTotalSize;
    //综合计算已上传大小，需记录以备下次断点准确计算起始位置；上传进度时实时返回，StatusSomeItemUploading
    public double hadUploadSize;
    //任务项执行信息，需记录
    public TaskItemInfo taskItemInfo;
    //进度信息
    public Progress progress;
    //额外附属参数
    public Object extra;

    //配置是否让我提交结果，默认是ture
    public boolean isResultCommit;
    //StatusPrepareResultCommit状态下如果需要我提交结果并且是自定义返回此值，需你配置UploadResultCommitParam中paramMap
    public UploadResultCommitParam uploadResultCommitParam;
    //配置是否让我提交结果Data
    public Object resultCommitData;

    public UploadResponse(){
        taskItemInfo = new TaskItemInfo();
        progress = new Progress();
    }

    public class Progress {
        public double percent;
        public double speed;
    }
    //此结构信息根据对应状态需本地记录，供下次使用
    public class TaskItemInfo {
        //项key
        public IK ikey;
        //执行某项上传任务时，此值会返回，pos位置和添加顺序一致
        public int positon;
        //执行某项上传任务时，此值会返回；当和你对应项状态不一致时更新此值本地记录，续传重启时需要此值；
        public int taskExecuteStep;
        //执行某项上传任务时，此值会返回（任务组某项数据源（目前String类型即可满足）:路径、文本）；续传重启时需要此值，结果提交不需要此值
        public String source;
        //执行某项上传任务时，此值会返回；其他项已上传完毕，续传重启时需要此值，正确计算比例；StepUplodComplete
        public double sourceSize;
        //执行某项上传任务时，此值会返回；以备此项继续续传；StepUplodPart
        public String qUploadKey;
        //此项上传完毕状态下七牛返回的url；StepUplodComplete状态下记录
        public String qResultUrl;
//        //上传结果拼接的url putmap中对应的key值；如"video_url"、"group_url"、"cover_url"；要求我来提交结果时必填记录
//        public String commitParamMapKey;
    }
}
