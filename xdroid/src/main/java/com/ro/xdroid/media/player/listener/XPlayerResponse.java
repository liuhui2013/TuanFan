package com.ro.xdroid.media.player.listener;

import com.ro.xdroid.media.player.XPlayerMode;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by roffee on 2017/8/29 15:38.
 * Contact with 460545614@qq.com
 */
public class XPlayerResponse {
    public XPlayerMode.MType mType;
    public boolean isOk;
    public int state;
    public String toast;

    public float bufferintPercent; //0-1
//    public float playPercent; //0-1
    public int totalDuration; //ms
    public int playedDuration;

    private SimpleDateFormat simpleDateFormat;

    public XPlayerResponse(){
        //noinspection unchecked
        simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
    }
    public String formatTotalDuration(){
        if(totalDuration <= 0){return "00:00";}
        return simpleDateFormat.format(totalDuration);
    }
    public String formatPlayedDuration(){
        if(playedDuration <= 0){return "00:00";}
        return simpleDateFormat.format(playedDuration);
    }
    public float getPlayPercent(){
        return (float)playedDuration / (float) totalDuration * 1.0f;
    }
}
