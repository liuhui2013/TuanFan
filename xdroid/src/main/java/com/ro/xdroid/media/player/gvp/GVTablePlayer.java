package com.ro.xdroid.media.player.gvp;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.media.image.loader.ImageLoaderKit;
import com.ro.xdroid.media.player.gvp.model.VisableItemPos;
import com.ro.xdroid.view.dialog.ToastKit;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;

import static com.ro.xdroid.media.player.gvp.GVPlayerKit.getRecyclerViewVisibleItemPos;

/**
 * Created by roffee on 2017/10/14 15:39.
 * Contact with 460545614@qq.com
 */
public class GVTablePlayer {
    protected Context context;
    protected ViewGroup tableView;
    protected ListVideoUtil listVideoUtil;
    protected boolean isShowSmall = true;
    protected boolean isAutoCallPlayer = true;
    protected boolean isShowSmallPlayer = true;
    protected boolean isFullscreenWithLandscape = true;
    protected VisableItemPos visableItemPos;
    protected String playTag = "GVTablePlayer";
    protected Point smallWinSize = new Point(GVMode.SmallWinWidth, GVMode.SmallWinHeight);
    protected GVListener gvListener;
    protected GVResponse gvResponse;
    protected int tableHeaderViewCount = -1;

    public GVTablePlayer(Context context, ViewGroup videoFullContainer){
        this.context = context;
        visableItemPos = new VisableItemPos();
        initPlayer(videoFullContainer);
    }

    public GVTablePlayer isAutoRotate(boolean isAutoRotate){
        listVideoUtil.setAutoRotation(isAutoRotate);
        return this;
    }
    public GVTablePlayer isShowFullAnimation(boolean isShowFullAnimation){
        listVideoUtil.setShowFullAnimation(isShowFullAnimation);
        return this;
    }
    public GVTablePlayer isShowSmallPlayer(boolean isShowSmallPlayer){
        this.isShowSmallPlayer = isShowSmallPlayer;
        return this;
    }
    public GVTablePlayer isLoop(boolean isLoop){
        listVideoUtil.setLoop(isLoop);
        return this;
    }
    public GVTablePlayer isNeedLockFull(boolean isNeedLockFull){
        listVideoUtil.setNeedLockFull(isNeedLockFull);
        return this;
    }
    public GVTablePlayer isNeedShowWifiTip(boolean isNeedShowWifiTip){
        listVideoUtil.setNeedShowWifiTip(isNeedShowWifiTip);
        return this;
    }
    public GVTablePlayer isHideStatusBar(boolean isHideStatusBar){
        listVideoUtil.setHideStatusBar(isHideStatusBar);
        return this;
    }
    public GVTablePlayer isHideActionBar(boolean isHideActionBar){
        listVideoUtil.setHideActionBar(isHideActionBar);
        return this;
    }
    public GVTablePlayer isFullLandFrist(boolean isFullLandFrist){
        listVideoUtil.setFullLandFrist(isFullLandFrist);
        return this;
    }
    public GVTablePlayer isAutoCallPlayer(boolean isAutoCallPlayer){
        this.isAutoCallPlayer = isAutoCallPlayer;
        return this;
    }
//    public GVTablePlayer isFullscreenWithLandscape(boolean isFullscreenWithLandscape){
//        this.isFullscreenWithLandscape = isFullscreenWithLandscape;
//        return this;
//    }
    public GVTablePlayer setSpeed(int speed){
        listVideoUtil.setSpeed(speed);
        return this;
    }
    public GVTablePlayer setTitle(String title){
        if(!ToolsKit.isEmpty(title)) listVideoUtil.setTitle(title);
        return this;
    }
    public GVTablePlayer setRecyclerView(RecyclerView recyclerViewt){
        return setRecyclerView(recyclerViewt, -1);
    }
    public GVTablePlayer setRecyclerView(RecyclerView recyclerView, int headerViewCount){
        this.tableView = recyclerView;
        this.tableHeaderViewCount = headerViewCount;
        setScrollListener();
        return this;
    }
    public GVTablePlayer setListView(ListView listView){
        return setListView(listView, -1);
    }
    public GVTablePlayer setListView(ListView listView, int headerViewCount){
        this.tableView = listView;
        this.tableHeaderViewCount = headerViewCount;
        setScrollListener();
        return this;
    }

    public GVTablePlayer setTableHeaderViewCount(int tableHeaderViewCount){
        this.tableHeaderViewCount = tableHeaderViewCount;
        return this;
    }
    public GVTablePlayer setSmallWinSize(int widthDip, int heightDip){
        smallWinSize.x = (int)(widthDip* ToolsKit.getDisplayMetrics().density);;
        smallWinSize.y = (int)(heightDip* ToolsKit.getDisplayMetrics().density);;
        return this;
    }
    public GVTablePlayer setGVListener(GVListener gvListener){
        this.gvListener = gvListener;
        if(gvResponse == null) gvResponse = new GVResponse();
        return this;
    }

    public <L> void bind2AddVideoPlayer(int pos, ViewGroup itemContainer, View itemPlayBtn, String playUrl, ImageView itemCoverView, L itemCoverSource) {
        bind2AddVideoPlayer(pos, itemContainer, itemPlayBtn, playUrl, null, itemCoverView, itemCoverSource);
    }
    public <L> void bind2AddVideoPlayer(int pos, ViewGroup itemContainer, View itemPlayBtn, String playUrl, String playTitle, ImageView itemCoverView, L  itemCoverSource) {
        ImageLoaderKit.withImageViewLoader(itemCoverView, context, itemCoverSource);
        listVideoUtil.addVideoPlayer(pos, itemCoverView, playTag, itemContainer, itemPlayBtn);
        if(itemPlayBtn != null){
            itemPlayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isAutoCallPlayer) {
                        if(ToolsKit.isEmpty(playUrl)){
                            ToastKit.show("play url is invalide");
                            return;
                        }
                        if(tableView != null){
                            if(tableView instanceof RecyclerView){
                                ((RecyclerView)tableView).getAdapter().notifyDataSetChanged();
                            }else if(tableView instanceof AbsListView){
                                ((BaseAdapter)((AbsListView)tableView).getAdapter()).notifyDataSetChanged();
                            }
                        }
                        if(isListenerOk()){
                            gvResponse.state = GVResponse.onClickStartIcon;
                            gvListener.onGvListener(gvResponse);
                        }
                        listVideoUtil.setPlayPositionAndTag(pos, playTag);
                        if(!ToolsKit.isEmpty(playTitle)) listVideoUtil.setTitle(playTitle);
                        listVideoUtil.startPlay(playUrl);
                    }
                }
            });
        }
    }
    public void manualCallItemStartPlay(int pos, String url, String...playTitle){
        if(isAutoCallPlayer) return;
        if(ToolsKit.isEmpty(url)){
            ToastKit.show("play url is invalide");
            return;
        }

        if(tableView != null){
            if(tableView instanceof RecyclerView){
                ((RecyclerView)tableView).getAdapter().notifyDataSetChanged();
            }else if(tableView instanceof AbsListView){
                ((BaseAdapter)((AbsListView)tableView).getAdapter()).notifyDataSetChanged();
            }
        }
        if(isListenerOk()){
            gvResponse.state = GVResponse.onClickStartIcon;
            gvListener.onGvListener(gvResponse);
        }

        listVideoUtil.setPlayPositionAndTag(pos, playTag);
        if(!ToolsKit.isEmpty(playTitle)) listVideoUtil.setTitle(playTitle[0]);
        listVideoUtil.startPlay(url);
    }
    public boolean onBackPressed(){
        if(listVideoUtil != null){
            if (listVideoUtil.backFromFull()) {
                return false;
            }
        }
        return true;
    }
    public void onPause() {
        if(listVideoUtil != null) {
            listVideoUtil.getGsyVideoPlayer().onVideoPause();
        }
    }
    public void onResume() {
        if(listVideoUtil != null) {
            listVideoUtil.getGsyVideoPlayer().onVideoResume();
        }
    }
    public void onDestroy() {
        if(listVideoUtil != null){
            listVideoUtil.releaseVideoPlayer();
            listVideoUtil = null;
        }
        GSYVideoPlayer.releaseAllVideos();
    }
    protected boolean isListenerOk(){
        if(gvListener == null) return false;
        return true;
    }
    protected void initPlayer(ViewGroup videoFullContainer){
        listVideoUtil = new ListVideoUtil(context);
        listVideoUtil.setFullViewContainer(videoFullContainer);
        listVideoUtil.setFullLandFrist(true);
        listVideoUtil.setShowFullAnimation(true);
        listVideoUtil.setAutoRotation(true);
        listVideoUtil.setHideStatusBar(true);
        listVideoUtil.setHideActionBar(true);
        listVideoUtil.setCachePath(new File(XDroidConfig.DirVCache));

//        listVideoUtil.getGsyVideoPlayer().getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isFullscreenWithLandscape) listVideoUtil.getGsyVideoPlayer().geto;
//                listVideoUtil.getGsyVideoPlayer().startWindowFullscreen(context, false, false);
//            }
//        });
        listVideoUtil.setVideoAllCallBack(new StandardVideoAllCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                if(isListenerOk()){
                    gvResponse.state = GVResponse.onPrepared;
                    gvResponse.results = objects;
                    gvListener.onGvListener(gvResponse);
                }
            }

            @Override
            public void onClickStartIcon(String url, Object... objects) {
                if(isListenerOk()){
                    gvResponse.state = GVResponse.onClickStartIcon;
                    gvResponse.results = objects;
                    gvListener.onGvListener(gvResponse);
                }
            }

            @Override
            public void onClickStartError(String url, Object... objects) {

            }

            @Override
            public void onClickStop(String url, Object... objects) {

            }

            @Override
            public void onClickStopFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickResume(String url, Object... objects) {
                if(isListenerOk()){
                    gvResponse.state = GVResponse.onClickResume;
                    gvResponse.results = objects;
                    gvListener.onGvListener(gvResponse);
                }
            }

            @Override
            public void onClickResumeFullscreen(String url, Object... objects) {
                if(isListenerOk()){
                    gvResponse.state = GVResponse.onClickResumeFullscreen;
                    gvResponse.results = objects;
                    gvListener.onGvListener(gvResponse);
                }
            }

            @Override
            public void onClickSeekbar(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbarFullscreen(String url, Object... objects) {

            }

            @Override
            public void onAutoComplete(String url, Object... objects) {

            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {
                //大于0说明有播放,//对应的播放列表TAG
                if (listVideoUtil.getPlayPosition() >= 0 && listVideoUtil.getPlayTAG().equals(playTag)) {
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < visableItemPos.firstPos || position > visableItemPos.lastPos)) {
                        //释放掉视频
                        listVideoUtil.releaseVideoPlayer();
                        if(tableView == null) return;
                        if(tableView instanceof RecyclerView){
                            ((RecyclerView)tableView).getAdapter().notifyDataSetChanged();
                        }else if(tableView instanceof AbsListView){
                            ((BaseAdapter)((AbsListView)tableView).getAdapter()).notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onEnterSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekVolume(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekPosition(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekLight(String url, Object... objects) {

            }

            @Override
            public void onPlayError(String url, Object... objects) {

            }

            @Override
            public void onClickStartThumb(String url, Object... objects) {

            }

            @Override
            public void onClickBlank(String url, Object... objects) {

            }

            @Override
            public void onClickBlankFullscreen(String url, Object... objects) {

            }
        });
    }

    public void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy) {
        if(tableView == null) tableView = recyclerView;
        visableItemPos = GVPlayerKit.getRecyclerViewVisibleItemPos(recyclerView, tableHeaderViewCount);
        //当前播放的位置
        int position = listVideoUtil.getPlayPosition();
        //大于0说明有播放,//对应的播放列表TAG
        if (position >= 0 && listVideoUtil.getPlayTAG().equals(playTag)) {
            //不可视的是时候
            if ((position < visableItemPos.firstPos || position > visableItemPos.lastPos)) {
                //如果是小窗口就不需要处理
                if (!listVideoUtil.isSmall() && !listVideoUtil.isFull()) {
                    if(!isShowSmallPlayer){
                        GSYVideoPlayer.releaseAllVideos();
                    }
                    //小窗口
//                                int size = CommonUtil.dip2px(context, 150);
                    //actionbar为true才不会掉下面去
                    listVideoUtil.showSmallVideo(smallWinSize, false, false);
                }
            } else {
                if (listVideoUtil.isSmall()) {
                    listVideoUtil.smallVideoToNormal();
                }
            }
        }
    }
    public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(tableView == null) tableView = view;
        visableItemPos.firstPos = firstVisibleItem;
        visableItemPos.lastPos = firstVisibleItem + visibleItemCount;
        //当前播放的位置
        int position = listVideoUtil.getPlayPosition();
        //大于0说明有播放,//对应的播放列表TAG
        if (position >= 0 && listVideoUtil.getPlayTAG().equals(playTag)) {
            //不可视的是时候
            if ((position < visableItemPos.firstPos || position > visableItemPos.lastPos)) {
                //如果是小窗口就不需要处理
                if (!listVideoUtil.isSmall() && !listVideoUtil.isFull()) {
                    if(!isShowSmallPlayer){
                        GSYVideoPlayer.releaseAllVideos();
                    }
                    //小窗口
//                                int size = CommonUtil.dip2px(context, 150);
                    //actionbar为true才不会掉下面去
                    listVideoUtil.showSmallVideo(smallWinSize, true, true);
                }
            } else {
                if (listVideoUtil.isSmall()) {
                    listVideoUtil.smallVideoToNormal();
                }
            }
        }
    }
    private void setScrollListener(){
        if(tableView == null) return;
        if(tableView instanceof RecyclerView){
            ((RecyclerView)tableView).addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    visableItemPos = getRecyclerViewVisibleItemPos(recyclerView, tableHeaderViewCount);
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //大于0说明有播放,//对应的播放列表TAG
                    if (position >= 0 && listVideoUtil.getPlayTAG().equals(playTag)) {
                        //不可视的是时候
                        if ((position < visableItemPos.firstPos || position > visableItemPos.lastPos)) {
                            //如果是小窗口就不需要处理
                            if (!listVideoUtil.isSmall() && !listVideoUtil.isFull()) {
                                if(!isShowSmallPlayer){
                                    GSYVideoPlayer.releaseAllVideos();
                                }
                                //小窗口
//                                int size = CommonUtil.dip2px(context, 150);
                                //actionbar为true才不会掉下面去
                                listVideoUtil.showSmallVideo(smallWinSize, false, false);
                            }
                        } else {
                            if (listVideoUtil.isSmall()) {
                                listVideoUtil.smallVideoToNormal();
                            }
                        }
                    }
                }
            });
        }else{
            ((AbsListView)tableView).setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    visableItemPos.firstPos = firstVisibleItem;
                    visableItemPos.lastPos = firstVisibleItem + visibleItemCount;
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //大于0说明有播放,//对应的播放列表TAG
                    if (position >= 0 && listVideoUtil.getPlayTAG().equals(playTag)) {
                        //不可视的是时候
                        if ((position < visableItemPos.firstPos || position > visableItemPos.lastPos)) {
                            //如果是小窗口就不需要处理
                            if (!listVideoUtil.isSmall() && !listVideoUtil.isFull()) {
                                if (!isShowSmallPlayer) {
                                    GSYVideoPlayer.releaseAllVideos();
                                }
                                //小窗口
//                                int size = CommonUtil.dip2px(context, 150);
                                //actionbar为true才不会掉下面去
                                listVideoUtil.showSmallVideo(smallWinSize, true, true);
                            }
                        } else {
                            if (listVideoUtil.isSmall()) {
                                listVideoUtil.smallVideoToNormal();
                            }
                        }
                    }
                }
            });
        }
    }
}
