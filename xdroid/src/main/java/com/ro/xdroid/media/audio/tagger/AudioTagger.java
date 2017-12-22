package com.ro.xdroid.media.audio.tagger;

import android.graphics.Bitmap;

import com.ro.xdroid.kit.ToolsKit;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

/**
 * Created by roffee on 2017/8/30 10:15.
 * Contact with 460545614@qq.com
 */
public class AudioTagger {
    private static String[] SupportAudio = new String[]{"mp3", "mp4", "ogg", "aac"};
    private String audioPath;
    private AudioFile audioFile;
    private String error;


    public AudioTagger(String audioPath){
        this.audioPath = audioPath;
        if(ToolsKit.isEmpty(audioPath)){
            error = "路径为空！";
            return;
        }
        if(!determineSuffixSupport(audioPath)){
            error = "类型不支持！";
            return;
        }
        try {
            audioFile = AudioFileIO.read(new File(audioPath));
        } catch (CannotReadException e) {
            e.printStackTrace();
            error = "文件读取异常！";
        } catch (IOException e) {
            e.printStackTrace();
            error = "文件IO异常！";
        } catch (TagException e) {
            e.printStackTrace();
            error = "读取Tag异常！";
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
            error = "文件读取异常！";
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
            error = "音频帧异常！";
        }
    }
    public AudioTag reader(){
        AudioTag audioTag = new AudioTag();
        if(audioFile == null){
            audioTag.error = error;
            return audioTag;
        }
        audioTag.title = audioFile.getTag().getFirst(FieldKey.TITLE);
        audioTag.artis = audioFile.getTag().getFirst(FieldKey.ARTIST);
        audioTag.album = audioFile.getTag().getFirst(FieldKey.ALBUM);
        audioTag.albumArtis = audioFile.getTag().getFirst(FieldKey.ALBUM_ARTIST);
        audioTag.year = audioFile.getTag().getFirst(FieldKey.YEAR);
        audioTag.genre = audioFile.getTag().getFirst(FieldKey.GENRE);
        audioTag.duration = audioFile.getTag().getFirst(FieldKey.TRACK_TOTAL);
        audioTag.size = audioFile.getFile().length();
        try {
            Object o = audioFile.getTag().getFirstArtwork().getImage();
            if(o instanceof Bitmap){
                audioTag.coverType = AudioTag.CoverType.Bitmap;
            }else if(o instanceof File){
                audioTag.coverType = AudioTag.CoverType.File;
            }else if(o instanceof String){
                audioTag.coverType = AudioTag.CoverType.Url;
            }else if(o instanceof byte[]){
                audioTag.coverType = AudioTag.CoverType.Bytes;
            }else{
                audioTag.coverType = AudioTag.CoverType.Unknown;
            }
            audioTag.cover = o;
            audioTag.isOk = true;
        } catch (IOException e) {
            e.printStackTrace();
            audioTag.coverType = AudioTag.CoverType.Unknown;
        }
        return audioTag;
    }
    public boolean writer(AudioTag audioTag){
        if(audioFile == null || audioTag == null){return false;}
        try {
            if(!ToolsKit.isEmpty(audioTag.title)){
                audioFile.getTag().setField(FieldKey.TITLE, audioTag.title);
            }
            if(!ToolsKit.isEmpty(audioTag.artis)){
                audioFile.getTag().setField(FieldKey.ARTIST, audioTag.artis);
            }
            if(!ToolsKit.isEmpty(audioTag.album)){
                audioFile.getTag().setField(FieldKey.ALBUM, audioTag.album);
            }
            if(!ToolsKit.isEmpty(audioTag.year)){
                audioFile.getTag().setField(FieldKey.YEAR, audioTag.year);
            }
            if(!ToolsKit.isEmpty(audioTag.genre)){
                audioFile.getTag().setField(FieldKey.GENRE, audioTag.genre);
            }
            if(!ToolsKit.isEmpty(audioTag.cover)){
                if(audioTag.cover == AudioTag.CoverType.Bitmap){
                    audioFile.getTag().getFirstArtwork().setBinaryData(ToolsKit.bitmap2Bytes((Bitmap) audioTag.cover));
                }else if(audioTag.cover == AudioTag.CoverType.Url){
                    audioFile.getTag().getFirstArtwork().setImageUrl((String) audioTag.cover);
                }else if(audioTag.cover == AudioTag.CoverType.Bytes){
                    audioFile.getTag().getFirstArtwork().setBinaryData((byte[])audioTag.cover);
                }else if(audioTag.cover == AudioTag.CoverType.File){
                    audioFile.getTag().getFirstArtwork().setFromFile((File)audioTag.cover);
                }
            }
            audioFile.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean determineSuffixSupport(String str) {
        if (ToolsKit.isEmpty(str)){
            return false;
        }

        int lastindex = str.lastIndexOf(".");
        String suffix;
        if (lastindex > 0) {
            suffix = str.substring(lastindex + 1);
        } else {
            suffix = "";
        }
        for (String s : SupportAudio) {
            if (s.equals(suffix))
                return true;
        }
        return false;
    }
}
