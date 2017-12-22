package com.ro.xdroid.media.audio.recorder.listener;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by roffee on 2017/9/29 13:39.
 * Contact with 460545614@qq.com
 */
public class ARecorderResponse {
    public static final int StateNone = 0;
    public static final int StateError = 1;
    public static final int StateRecording = 2;
    public static final int StateStop = 3;


    public int state;
    public int durationChange;//s
    public String filePath;
    private SimpleDateFormat simpleDateFormat;

    public ARecorderResponse(){
        //noinspection unchecked
        simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
    }
    public String getFormatDuration(){
        if(durationChange <= 0){return "00:00";}
        return simpleDateFormat.format(durationChange*1000);
    }
}
