package com.ro.xdroid.net.ok.taotong.model;

import java.io.Serializable;

/**
 * Created by roffee on 2017/8/17 10:25.
 * Contact with 460545614@qq.com
 */
public class TTTokenModel implements Serializable {

    public int status;
    public DataBean data;
    public static class DataBean {
        public String token;
//        public String remoteUrl;
    }
}
