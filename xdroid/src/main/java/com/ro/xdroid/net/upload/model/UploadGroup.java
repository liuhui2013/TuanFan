package com.ro.xdroid.net.upload.model;

import android.util.SparseArray;

/**
 * Created by roffee on 2017/11/29 11:13.
 * Contact with 460545614@qq.com
 */
public class UploadGroup<IK>{
    //任务网络要求，默认有网即可传
    public boolean isMustWifi;
    //对于某些特殊情况，文本、已上传的直接配置好map直接提交
    public boolean isImmCommit;
    //当任务组某些资源缺失、配置出错是否继续执行其他的
    public boolean isContinueWhenSomethingError;
    //任务类型，默认支持断点续传
    public UploadMode.BreakpointType breakpointType = UploadMode.BreakpointType.SupportBreakpoint;

    //上传资源总大小
    public double sourceTotalSize;
    //综合计算已上传大小，需记录以备下次断点准确计算起始位置
    public double hadUploadSize;
    //任务组项列表
    public SparseArray<UploadGroupItem<IK>> groupItems;
    //任务计数
    public int taskCounts;
    //当前任务执行索引
    public int curExecuteIndex = -1;


    //配置是否让我提交结果，默认是ture
    public boolean isResultCommit = true;
    //如果让我提交，配置结果提交相关参数
    public UploadResultCommitParam resultCommitParam;

    //任务组扩展参数
    public Object extra;



    //上传权限token
    public String token;
    //拿到token时的时间戳
    public long tokenTimestamp;
    //记录上传进度过程上次时间戳，用于计算上传速率
    public long upProgressLastTime;
    //记录上传进度过程上次偏移，用于计算上传速率
    public double upProgressLastOffset;
    //统计已经完全上传项的上传总大小
    public int hadCompleteUploadItemsTotalSize;


    public UploadGroup(){
        curExecuteIndex = -1;
        groupItems = new SparseArray<UploadGroupItem<IK>>();
    }
}
