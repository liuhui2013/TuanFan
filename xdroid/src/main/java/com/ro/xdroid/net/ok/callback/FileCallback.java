package com.ro.xdroid.net.ok.callback;

import com.lzy.okgo.callback.AbsCallback;
import com.ro.xdroid.net.ok.XOk;
import com.ro.xdroid.net.ok.convert.FileConvert;

import java.io.File;

/**
 * Created by roffee on 2017/8/14 11:01.
 * Contact with 460545614@qq.com
 */
public abstract class FileCallback extends AbsCallback<File> {
//    private String destFileDir;
//    private String destFileName;
    private FileConvert convert;

    public FileCallback() {
        this(null);
    }

    public FileCallback(String destFileName) {
        this(null, destFileName, null);
    }

    public FileCallback(String destFileDir, String destFileName, String tempFileName) {
        this(null, null, destFileName, null, false);
    }
    public FileCallback(XOk xOk, String destFileDir, String destFileName, String tempFileName, boolean isCover) {
        convert = new FileConvert(xOk, destFileDir, destFileName, tempFileName, isCover);
        convert.setCallback(this);
    }

    @Override
    public File convertResponse(okhttp3.Response response) throws Throwable {
        File file = convert.convertResponse(response);
        response.close();
        return file;
    }
}
