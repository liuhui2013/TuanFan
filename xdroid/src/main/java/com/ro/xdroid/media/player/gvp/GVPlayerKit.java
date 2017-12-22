package com.ro.xdroid.media.player.gvp;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.media.player.gvp.model.VisableItemPos;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.FileUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;

/**
 * Created by roffee on 2017/10/20 10:27.
 * Contact with 460545614@qq.com
 */
public class GVPlayerKit {
    public static VisableItemPos visableItemPos;

    public static void onGVPlayerPause(){ GSYVideoManager.onPause(); }
    public static void onGVPlayerResume(){ GSYVideoManager.onResume(); }
    public static void releaseAllGVPlayer(){ GSYVideoPlayer.releaseAllVideos(); }

    public void clearPlayerCache(){ FileUtils.deleteFiles(new File(XDroidConfig.DirVCache));}

    public static void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy, String tag) {
        if(ToolsKit.isEmpty(tag)) return;
        VisableItemPos visableItemPos = getRecyclerViewVisibleItemPos(recyclerView);
        int position = GSYVideoManager.instance().getPlayPosition();
        if (position >= 0 && GSYVideoManager.instance().getPlayTag().equals(tag)) {
            //对应的播放列表TAG
            if (position < visableItemPos.firstPos || position > visableItemPos.lastPos) {
                GSYVideoPlayer.releaseAllVideos();
//                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    public static VisableItemPos getRecyclerViewVisibleItemPos(RecyclerView recyclerView, int headerCount) {
        if(visableItemPos == null) visableItemPos = new VisableItemPos();
        if (recyclerView == null) return visableItemPos;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(headerCount < 0){
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if(adapter instanceof BaseQuickAdapter){
                headerCount = ((BaseQuickAdapter)adapter).getHeaderLayoutCount();
            }else{
                headerCount = 0;
            }
        }else{
            headerCount = 0;
        }
        if (layoutManager instanceof GridLayoutManager) {
            visableItemPos.firstPos = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition() - headerCount;
            visableItemPos.lastPos = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() - headerCount;
        }  if (layoutManager instanceof LinearLayoutManager) {
            visableItemPos.firstPos = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition() - headerCount;
            visableItemPos.lastPos = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() - headerCount;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] ints = null;
            ints = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            if (ints != null) visableItemPos.firstPos = ints[0] - headerCount;
            ints = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            if (ints != null) visableItemPos.lastPos = ints[ints.length - 1] - headerCount;
        }
        return visableItemPos;
    }
    public static VisableItemPos getRecyclerViewVisibleItemPos(RecyclerView recyclerView) {
        return getRecyclerViewVisibleItemPos(recyclerView, -1);
    }
}
