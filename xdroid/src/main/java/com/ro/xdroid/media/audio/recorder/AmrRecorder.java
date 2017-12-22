package com.ro.xdroid.media.audio.recorder;

import android.media.MediaRecorder;

import com.ro.xdroid.media.audio.recorder.listener.ARecorderResponse;
import com.ro.xdroid.view.dialog.ToastKit;

import java.io.File;
import java.io.IOException;

/**
 * Created by roffee on 2017/10/8 14:39.
 * Contact with 460545614@qq.com
 */
public class AmrRecorder extends ARecorder<AmrRecorder> {
    protected MediaRecorder mediaRecorder;

    public AmrRecorder() {
        super(AMode.AType.Amr);
    }
    public AmrRecorder(AMode.AType aType) {
        super(aType);
    }
    @Override
    protected boolean start() {
        try {
            initRecorder();
            stopTimer();
        } catch (IOException e) {
            e.printStackTrace();
            if(isListenerOk()){
                aRecorderResponse.state = ARecorderResponse.StateError;
                aRecorderListener.onAudioRecorder(aRecorderResponse);
                ToastKit.show("amr init recorder error");
                return false;
            }
        }
        startTimer();
        if(isListenerOk()){
            aRecorderResponse.state = ARecorderResponse.StateRecording;
            aRecorderResponse.durationChange = 0;
            aRecorderListener.onAudioRecorder(aRecorderResponse);
        }
        return true;
    }

    @Override
    protected boolean stop() {
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        stopTimer();
        if(isListenerOk()){
            if(isListenerOk()){
                aRecorderResponse.state = ARecorderResponse.StateStop;
                if(!isTempFile) aRecorderResponse.filePath = filePath;
                aRecorderListener.onAudioRecorder(aRecorderResponse);
            }
        }
        return true;
    }

    @Override
    protected int durationChange(int curDuration) {
        if(isListenerOk()){
//            aRecorderResponse.state = ARecorderResponse.StateRecording;
            aRecorderResponse.durationChange = curDuration;
            aRecorderListener.onAudioRecorder(aRecorderResponse);
        }
        return curDuration;
    }
    protected void setMediaRecorderFormatEncode(){
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }
    private void initRecorder() throws IOException {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(aSource.getSource());
        mediaRecorder.setAudioSamplingRate(aSampleRate.getSampleRate());
        mediaRecorder.setAudioChannels(aChannel.getChannel());
        setMediaRecorderFormatEncode();

        mediaRecorder.setMaxDuration(maxSecond*1000);

        if(!isTempFile) getRealFilePath();
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.prepare();
        mediaRecorder.start();
    }
}
