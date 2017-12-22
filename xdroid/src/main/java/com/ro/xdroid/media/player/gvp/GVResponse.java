package com.ro.xdroid.media.player.gvp;

/**
 * Created by roffee on 2017/10/19 18:57.
 * Contact with 460545614@qq.com
 */

public class GVResponse {
    public static final int None = 0;
    public static final int onPrepared = 1;
    public static final int onClickStartIcon = 2;
    public static final int onClickStop = 3;
    public static final int onClickStopFullscreen = 4;
    public static final int onClickResume = 5;
    public static final int onClickResumeFullscreen = 6;
    public static final int onClickSeekbar = 7;
    public static final int onClickSeekbarFullscreen = 8;
    public static final int onAutoComplete = 9;
    public static final int onEnterFullscreen = 10;
    public static final int onQuitFullscreen = 11;
    public static final int onQuitSmallWidget = 12;
    public static final int onEnterSmallWidget = 13;
    public static final int onTouchScreenSeekVolume = 14;
    public static final int onTouchScreenSeekPosition = 15;
    public static final int onPlayError = 16;
    public static final int onClickStartThumb = 17;
    public static final int onClickBlank = 18;
    public static final int onClickBlankFullscreen = 19;

    public static final int onClickBackButton = 30;

    public int state;
    public Object[] results;
}
