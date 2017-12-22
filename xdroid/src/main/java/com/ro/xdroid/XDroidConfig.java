package com.ro.xdroid;

import android.content.Context;

import com.ro.xdroid.kit.ToolsKit;

import java.io.File;

/**
 * Created by roffee on 2017/7/20 19:08.
 * Contact with 460545614@qq.com
 */
public class XDroidConfig {
    //#debug
    public static boolean Debug = true;
    //#app
    public static String PackageName;
    //#dir&path
    public static final String DirBase = ToolsKit.getAvailableStorage() + File.separator + "taotong" + File.separator;
    public static final String DirDownload = DirBase + "download" + File.separator;
    public static final String DirShortvideo = DirBase + "shortvideo" + File.separator;
//    public static final String DirPLPlaer = DirBase + "PLPlayer" + File.separator;
//    public static final String DirPLLive = DirBase + "PLLive" + File.separator;
    public static final String DirCamera = DirBase + "camera" + File.separator;
    public static final String DirAudio = DirBase + "audio" + File.separator;
    public static final String DirVCache = DirBase + "vcache" + File.separator;
    public static final String DirVIDEOCROP = DirBase + "VideoCrop" + File.separator;
    //#sp
    public static final String SPAppKey = "BIPAI";
    public static final int SPAppMode = Context.MODE_PRIVATE;

    public static final String SPKeyUserId = "USER_ID";
    public static final String SPKeyReqToaken = "REQ_TOKEN";

    public static final String ComParamKeyAppVersion = "app_version";
    public static final String ComParamKeyPlatformTag = "platform_tag";
    public static final String ComParamKeyAppType = "app_type";
    public static final String ComParamKeyUserId = "user_uid";
    public static final String ComParamKeyIsEntrypt = "token_entrypt";

    public static final String ReqParamKeyEntrypt = "entrypt_data";
    public static final String ReqParamKeyIsEntrypt = "is_entrypt";

    //upload
    public static String QUploadGainTokenUrl;
    public static String QUploadBaseUrl;
}
