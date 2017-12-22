package com.ro.xdroid.net.upload.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by roffee on 2017/11/30 15:20.
 * Contact with 460545614@qq.com
 */
public class TokenModel implements Serializable {
    /**
     * token : BX3FxNDH3aFGwGSb8Yue745EgiumlqGpqthQ8x1u:q1U_rCjB5ON6h8hLzgC23dNO8pA=:eyJzY29wZSI6ImRvdXNob3ciLCJkZWFkbGluZSI6MTQ3NTA0OTA4OH0=
     */

    private DataBean data;
    /**
     * data : {"token":"BX3FxNDH3aFGwGSb8Yue745EgiumlqGpqthQ8x1u:q1U_rCjB5ON6h8hLzgC23dNO8pA=:eyJzY29wZSI6ImRvdXNob3ciLCJkZWFkbGluZSI6MTQ3NTA0OTA4OH0="}
     * status : 1
     */

    private int status;

    public static TokenModel objectFromData(String str) {

        return new Gson().fromJson(str, TokenModel.class);
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        private String token;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
