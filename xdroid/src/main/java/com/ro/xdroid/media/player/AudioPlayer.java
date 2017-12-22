package com.ro.xdroid.media.player;

/**
 * Created by roffee on 2017/8/29 13:53.
 * Contact with 460545614@qq.com
 */
public class AudioPlayer extends XPlayer<AudioPlayer> {
//    private static AudioPlayer audioPlay;

    public AudioPlayer() {
        super(XPlayerMode.MType.AUDIO);
    }
//    public static AudioPlayer getPlayer(){
//        if(audioPlay == null) audioPlay = new AudioPlayer();
//        return audioPlay;
//    }
    public static AudioPlayer newPlayer(){
        return new AudioPlayer();
    }
    @Override
    public void release(){
        super.release();
//        audioPlay = null;
    }

    /*
    test use
    private void test(){
        AudioPlay.getPlayer().setSource("").prePlay(new MediaListener() {
            @Override
            public void onMediaListner(MediaResponse mediaResponse) {

            }
        });
        AudioPlay.getPlayer().release();
    }
     */
}
