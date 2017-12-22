package com.ro.xdroid.net.ok.taotong;

import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.mvp.XApp;

import java.util.HashMap;

/**
 * Created by roffee on 2017/7/31 15:34.
 * Contact with 460545614@qq.com
 */
public class TTCommonParams {
    private static final String appVersionName = ToolsKit.getVersionName(XApp.getContext());
    public static HashMap<String, String> commonParams;

    private static void create(){if(commonParams == null){commonParams = new HashMap<>();}}
    public static void initTTCommonParams(){
        create();
        commonParams.put(XDroidConfig.ComParamKeyAppVersion, appVersionName);
        commonParams.put(XDroidConfig.ComParamKeyPlatformTag, "2");
        commonParams.put(XDroidConfig.ComParamKeyAppType, "2");
    }
    public static void updateCommonParams(String key, String param){
        if(ToolsKit.isEmpty(key)) return;

        create();
        synchronized (commonParams){
            if(ToolsKit.isEmpty(param)){
                if(commonParams.containsKey(key)) commonParams.remove(key);
            }else commonParams.put(key, param);

            if(!commonParams.containsKey(XDroidConfig.ComParamKeyAppVersion)) commonParams.put(XDroidConfig.ComParamKeyAppVersion, appVersionName);
            if(!commonParams.containsKey(XDroidConfig.ComParamKeyPlatformTag)) commonParams.put(XDroidConfig.ComParamKeyPlatformTag, "2");
            if(!commonParams.containsKey(XDroidConfig.ComParamKeyAppType)) commonParams.put(XDroidConfig.ComParamKeyAppType, "2");
        }
    }
    public static void autoUpdateCommonParams(){
        create();
        synchronized (commonParams){
            if(!commonParams.containsKey(XDroidConfig.ComParamKeyAppVersion)) commonParams.put(XDroidConfig.ComParamKeyAppVersion, appVersionName);
            if(!commonParams.containsKey(XDroidConfig.ComParamKeyPlatformTag)) commonParams.put(XDroidConfig.ComParamKeyPlatformTag, "2");
            if(!commonParams.containsKey(XDroidConfig.ComParamKeyAppType)) commonParams.put(XDroidConfig.ComParamKeyAppType, "2");

            String userId = ToolsKit.getSPString(XDroidConfig.SPKeyUserId);
            if(ToolsKit.isEmpty(userId))commonParams.remove(XDroidConfig.ComParamKeyUserId);
            else commonParams.put(XDroidConfig.ComParamKeyUserId, userId);

//            String reqToken = ToolsKit.getSPString(XDroidConfig.SPKeyReqToaken);
//            if(ToolsKit.isEmpty(reqToken)) commonParams.remove(XDroidConfig.ComParamKeyIsEntrypt);
//            else commonParams.put(XDroidConfig.ComParamKeyIsEntrypt, reqToken);
        }
    }
    public static HashMap<String, String> getTTCommonParams(boolean isUseCommonParams){
        create();
        synchronized (commonParams){
            if(isUseCommonParams){
                if(ToolsKit.isEmpty(commonParams)){
                    commonParams.put(XDroidConfig.ComParamKeyAppVersion, appVersionName);
                    commonParams.put(XDroidConfig.ComParamKeyPlatformTag, "2");
                    commonParams.put(XDroidConfig.ComParamKeyAppType, "2");

                    String userId = ToolsKit.getSPString(XDroidConfig.SPKeyUserId);
                    if(!ToolsKit.isEmpty(userId))commonParams.put(XDroidConfig.ComParamKeyUserId, userId);
//                    String reqToken = ToolsKit.getSPString(XDroidConfig.SPKeyReqToaken);
//                    if(!ToolsKit.isEmpty(reqToken))commonParams.put(XDroidConfig.ComParamKeyIsEntrypt, reqToken);
                }
            }else{
                if(!ToolsKit.isEmpty(commonParams)) commonParams.clear();
            }
        }
        return commonParams;
    }
}
