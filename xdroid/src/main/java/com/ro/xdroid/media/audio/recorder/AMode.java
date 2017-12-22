package com.ro.xdroid.media.audio.recorder;

import android.media.AudioFormat;
import android.media.MediaRecorder;

import com.ro.xdroid.media.audio.recorder.mp3.PCMFormat;

/**
 * Created by roffee on 2017/9/29 11:34.
 * Contact with 460545614@qq.com
 */
public class AMode {
    public static final int EnableMinDuration = 3;      //3second
    public static final int DefaultMinDuration = 5;     //5second
    public static final int DefaultMaxDuration = Integer.MAX_VALUE;  //5min


    public enum AType {
        Mp3, Aac, Amr, Wav; //Wav2Mp3;

        public String getSuffix(){
            switch (this) {
                case Mp3:
                    return ".mp3";
                case Aac:
                    return ".acc";
                case Amr:
                    return ".amr";
                case Wav:
                    return ".wav";
                default:
                    return ".mp3";
            }
        }
        public String getTempName(){
            switch (this) {
                case Mp3:
                    return "audiotemp.mp3";
                case Aac:
                    return "audiotemp.acc";
                case Amr:
                    return "audiotemp.amr";
                case Wav:
                    return "audiotemp.wav";
                default:
                    return "audiotemp.mp3";
            }
        }
        public String getRealName(){
            long curT = System.currentTimeMillis();
            switch (this) {
                case Mp3:
                    return "audio" + curT + ".mp3";
                case Aac:
                    return "audio" + curT + ".acc";
                case Amr:
                    return "audio" + curT + ".amr";
                case Wav:
                    return "audio" + curT + ".wav";
                default:
                    return "audio" + curT + ".mp3";
            }
        }
    }

    public enum ASource {
        Default,
        Mic,
        Camcorder;

        public int getSource() {
            switch (this) {
                case Default:
                    return MediaRecorder.AudioSource.DEFAULT;
                case Mic:
                    return MediaRecorder.AudioSource.MIC;
                case Camcorder:
                    return MediaRecorder.AudioSource.CAMCORDER;
                default:
                    return MediaRecorder.AudioSource.MIC;
            }
        }
    }

    public enum AChannel {
        Default,
        Mono,
        Stereo;

        public int getChannel(){
            switch (this){
                case Default:
                    return AudioFormat.CHANNEL_IN_DEFAULT;
                case Mono:
                    return AudioFormat.CHANNEL_IN_MONO;
                case Stereo:
                    return AudioFormat.CHANNEL_IN_STEREO;
                default:
                    return AudioFormat.CHANNEL_IN_STEREO;
            }
        }
    }

    public enum ASampleRate {
        HZ_48000,
        HZ_44100,
        HZ_32000,
        HZ_22050,
        HZ_16000,
        HZ_11025,
        HZ_8000;

        public int getSampleRate() {
            return Integer.parseInt(name().replace("HZ_", ""));
        }
    }
    public enum APcmFormat{
        PCM_8BIT,
        PCM_16BIT;

        public PCMFormat getPcmFormat(){
            switch (this){
                case PCM_8BIT:
                    return PCMFormat.PCM_8BIT;
                case PCM_16BIT:
                    return PCMFormat.PCM_16BIT;
                default:
                    return PCMFormat.PCM_16BIT;
            }
        }
    }
}
