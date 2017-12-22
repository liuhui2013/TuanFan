package com.ro.xdroid.media.audio.recorder.mp3;

/**
 * Created by roffee on 2017/10/8 11:01.
 * Contact with 460545614@qq.com
 */
public class RawData {
    public short[] data;
    public int size;

    public RawData(short[] data, int size){
        this.data = data.clone();
        this.size = size;
    }
}
