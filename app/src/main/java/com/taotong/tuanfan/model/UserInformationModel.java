package com.taotong.tuanfan.model;

import java.io.Serializable;

/**
 * Created by liuhui on 2017/4/13.
 * 新版获取当前登录用户信息
 */

public class UserInformationModel implements Serializable {
    /**
     * status : 1
     * info : 获取用户信息
     * data : {"uid":"1690","nickname":"用户_1690","avatar":"http://vod.doushow.com/dbh_avatar_default.png","signature":"这家伙很懒，什么签名都没写~","sex":"0","birthday":"","status":"1","works":"0","likes":"0","follows":"0","fans":"0","anchor_status":"1","total_coins":0}
     */

    private int status;
    private String info;
    private MySelfInfo data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public MySelfInfo getData() {
        return data;
    }

    public void setData(MySelfInfo data) {
        this.data = data;
    }

}
