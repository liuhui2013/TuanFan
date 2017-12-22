package com.ro.xdroid.media.player.gvp;

import com.ro.xdroid.kit.ToolsKit;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

/**
 * Created by roffee on 2017/10/13 10:16.
 * Contact with 460545614@qq.com
 */
public class GVMode {
    public static final int SmallWinWidth = (int)(160* ToolsKit.getDisplayMetrics().density);
    public static final int SmallWinHeight = (int)(120* ToolsKit.getDisplayMetrics().density);
    public enum VAspectRatio{
        Default,
        Full,//全屏
        MatchFull,//拉伸全屏
        R16x9,
        R4x3;

        public int getAspectRatio(){
            switch (this){
                case Full: return GSYVideoType.SCREEN_TYPE_FULL;
                case MatchFull: return GSYVideoType.SCREEN_MATCH_FULL;
                case R16x9: return GSYVideoType.SCREEN_TYPE_16_9;
                case R4x3: return GSYVideoType.SCREEN_TYPE_4_3;
                default: return GSYVideoType.SCREEN_TYPE_DEFAULT;
            }
        }
    }

    public enum VImageRotateTrans{
        RotateTrans,
        LeftRightTrans,
        UpDownTrans
    }
//    public enum VThumbCoverType{
//        None,
//        Url,
//        ResId,
//        File,
////        VFrame
//    }
    public enum TableType {
        Recycler,
        ListView,
    }
}
