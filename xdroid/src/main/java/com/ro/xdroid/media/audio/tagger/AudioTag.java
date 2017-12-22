package com.ro.xdroid.media.audio.tagger;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;

import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.mvp.XApp;

/**
 * Created by roffee on 2017/8/30 11:17.
 * Contact with 460545614@qq.com
 */
public class AudioTag {
    //state
    public boolean isOk;
    public String error;

    //tagger ininfo
    public String title;        //名称
    public String artis;        //艺术家
    public String album;        //专辑
    public String albumArtis;  //专辑艺术家
    public String year;         //日期
    public String genre;        //风格
    public String duration;     //时长
    public long size;           //大小

    public CoverType coverType;
    public Object cover;
//    public List<Artwork> artworks;

//    public Bitmap getCover(int... index){
//        if(ToolsKit.isEmpty(artworks)){return null;}
//        try {
//            if(ToolsKit.isEmpty(index)){return (Bitmap) artworks.get(0).getImage();}
//            else{
//                int idx = index[0];
//                if(idx < 0) idx = 0;
//                else if(idx >= artworks.size()) idx = artworks.size() - 1;
//                return (Bitmap) artworks.get(idx).getImage();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    public String formatSize(){return Formatter.formatFileSize(XApp.getContext(), size);}
    public Drawable getCoverDrawable(){
        if(cover == null){return null;}
        if(coverType == CoverType.Bitmap){return ToolsKit.bitmap2Drawable((Bitmap)cover);}
        else if(coverType == CoverType.Bytes){return ToolsKit.bytes2Drawable((byte[])cover);}
        else {return null;}
    }
    public enum CoverType {
        Unknown,
        Bitmap,
        Url,
        Bytes,
        File,
    }
}