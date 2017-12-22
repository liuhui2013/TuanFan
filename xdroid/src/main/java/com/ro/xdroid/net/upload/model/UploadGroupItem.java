package com.ro.xdroid.net.upload.model;

/**
 * Created by roffee on 2017/11/29 11:01.
 * Contact with 460545614@qq.com
 */
public class UploadGroupItem<IK> {
    //！！！以下为可外部配置项
    //项key
    public IK ikey;
    //当前项执行阶段，根据执行、记录情况如实配置；必填
    public int taskExecuteStep;
    //任务源类型
    public UploadMode.SourceType sourceType = UploadMode.SourceType.File;
    //任务组某项数据源（目前String类型即可满足）:路径、文本，File:StepUploadNone、StepUplodPart状态下必填,Text:配置StepUplodComplete(内部自动配置)
    public String source;
    //StepUplodComplete状态下必填，对应response中项sourceSize
    public double hadCompleteUploadsSurceSize;
    //此项上传部分配置项；StepUplodPart状态下必填
    public String qUploadKey;
    //此项上传完毕状态下七牛返回的url；StepUplodComplete状态下必填；text下可设置字符串："text"(内部自动配置)
    public String qResultUrl;
    //上传结果拼接的url putmap中对应的key值；如"video_url"、"group_url"、"cover_url"；要求我来提交结果时必填
    public String commitParamMapKey;
    //针对图片上传后提交扩展名key，根据是否需要填写
    public String appendNameKey;



    //！！！以下为内部配置项
    //标记项加入队列的顺利，即以索引为key
    public int indexKey;
    //此项出错忽视此项上传
    public boolean isIgnore;
    //资源文件信息
    public UploadMode.FileInfo fileInfo;
}
