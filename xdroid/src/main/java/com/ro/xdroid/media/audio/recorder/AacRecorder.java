package com.ro.xdroid.media.audio.recorder;

import android.media.MediaRecorder;

/**
 * Created by roffee on 2017/10/8 14:39.
 * Contact with 460545614@qq.com
 */
public class AacRecorder extends AmrRecorder {
    public AacRecorder() {
        super(AMode.AType.Aac);
    }

    @Override
    protected void setMediaRecorderFormatEncode(){
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    }
}
