package com.ro.xdroid.media.player;

/**
 * Created by roffee on 2017/8/29 11:49.
 * Contact with 460545614@qq.com
 */
public class XPlayerMode {
    public static final int ProgressUpdateInterval = 300; //ms
    public static final String[] AUDIO_SUPPORT_ARRAY = {
            "mp3", "aac", "amr", "midi", "flac", "pcm"
    };

    public enum MType {
        AUDIO,
        VEDIO,
    }

    public enum MSourceType {
        NET_STREAM,
        LOCAL_FILE,
        LOCAL_RESOURCE,
        URI
    }

    public static class MState {
        public static final int NOTE = 0;
        public static final int ERROR = 1;
        public static final int BUFFERINT = 2;
        public static final int PREPARE = 3;
        public static final int STARTPLAY = 4;
        public static final int PALYING = 5;
        public static final int PAUSE = 6;
        public static final int STOP = 7;
        public static final int COMPLETE = 8;
    }
}
