package com.ro.xdroid.net.upload.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roffee on 2017/11/29 14:08.
 * Contact with 460545614@qq.com
 */
public class UploadResultCommitParam {
    public String commitUrl;                //上传结果提交的ip

    public boolean isUdefineParamMap;       //是否调用者自定义map参数，如果自定义则不需要设置resUrlKey值，否则需要设置内部处理
//    public String commitUrlMapKey;          //上传结果拼接的url putmap中对应的key值；如"video_url"、"group_url"、"cover_url"
    public Map paramMap;                    //上传结果提交网络请求参数

    public UploadResultCommitParam(){
        paramMap = new HashMap();
    }
}
