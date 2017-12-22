package com.ro.xdroid.media.player.gvp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.transition.Transition;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ro.xdroid.R;
import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.media.image.loader.ImageLoaderKit;
import com.ro.xdroid.media.player.gvp.model.GVSourceModel;
import com.ro.xdroid.mvp.XApp;
import com.ro.xdroid.view.dialog.ToastKit;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roffee on 2017/10/13 09:09.
 * Contact with 460545614@qq.com
 */
public class GVStylePlayer extends StandardGSYVideoPlayer {
    //styles
    public static final int Standar = 0;
    public static final int InDetail = 1;
    public static final int InDetailWithScrollable = 2;
    public static final int InTable = 3;
    public static final int NoUiConrol = 4;
    public static final int LiveStreaming = 5;

    @IntDef(flag = true,
            value = {
                    Standar,                        //屏幕只有1个视频
                    InDetail,                       //屏幕除了1个视频还有其他详情
                    InDetailWithScrollable,      //屏幕除了1个视频还有其他详情，且视频可滚动
                    InTable,                        //被用到列表、表格中，多个视频
                    LiveStreaming,                 //直播
            })
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface Styles{}

    //attrs
    protected int styleId;          //风格
    protected int ulayoutId;        //用户自定义布局引入，不设置使用默认

    //variable
    protected Activity activity;
    protected int vAspectRatioType = GSYVideoType.SCREEN_TYPE_DEFAULT;
    protected GVMode.VImageRotateTrans vImageRotateTransType = GVMode.VImageRotateTrans.RotateTrans;
    protected String vCachePath = XDroidConfig.DirVCache;
    protected List<GVSourceModel> vSources;
    protected int curPlaySourcePos = 0;
    protected OrientationUtils orientationUtils;
    protected ImageView thumbCoverView;
    protected Object thumbCoverSource;
    protected View scrollView;
    protected boolean isCache = true;
    protected boolean isShowBackButton;
    protected boolean isShowTitle = true;
    protected boolean isTransition = true;
    protected boolean isAotuPlay;
    protected boolean isShowFirstFrameAsCover;
    protected boolean isCongfigReconnect;
    protected boolean isSmall;
    protected boolean isShowSmallPlayer = true;
    protected boolean isAotuRotate = true;
    protected boolean isFullscreenWithLandscape = true;
    protected boolean isAlwaysShowSmallWhenScrollable;
    protected boolean isShowFullAnimation = true;
    protected boolean isVolumeChangeUi = true;
    protected boolean isBrightnessChangeUi = true;
    protected boolean isPrepared;
    protected Point smallWinSize = new Point(GVMode.SmallWinWidth, GVMode.SmallWinHeight);
    protected GVListener gvListener;
    protected GVResponse gvResponse;
    protected int playPos = -1;
    protected String playTag = "GVStylePlayer";
    private Matrix matrix;


    public GVStylePlayer(Context context) {
        super(context);
    }
    public GVStylePlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }
    public GVStylePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        styleInit();
        super.init(context);
        getTitleTextView().setVisibility(View.GONE);
        getBackButton().setVisibility(View.GONE);
        vSources = new ArrayList<>();
    }
    protected void styleInit(){
        TypedArray typedArray = getContext().obtainStyledAttributes(R.styleable.GVStylePlayer);
        styleId = typedArray.getInt(R.styleable.GVStylePlayer_styles, Standar);
        switch (styleId) {
            case Standar:
            case InDetail:
                ulayoutId = typedArray.getResourceId(R.styleable.GVStylePlayer_ulayout, R.layout.video_layout_standard);
            case InDetailWithScrollable:
            case InTable:
                ulayoutId = typedArray.getResourceId(R.styleable.GVStylePlayer_ulayout, R.layout.video_layout_normal);
//            case NoUiConrol:
//                ulayoutId = typedArray.getResourceId(R.styleable.GVStylePlayer_ulayout, R.layout.gvp_empty_control_layout);
            case LiveStreaming:
                ulayoutId = typedArray.getResourceId(R.styleable.GVStylePlayer_ulayout, R.layout.video_layout_standard);
            default:
                ulayoutId = typedArray.getResourceId(R.styleable.GVStylePlayer_ulayout, R.layout.video_layout_standard);
        }
    }
    @Override
    public int getLayoutId() { return ulayoutId; }
    public GVStylePlayer addPlaySource(String vUrl){
        return addPlaySource(vUrl, null);
    }
    public GVStylePlayer addPlaySource(String vUrl, String vTitle){
        if(!ToolsKit.isEmpty(vUrl)){
            GVSourceModel gvSourceModel = new GVSourceModel(vUrl, vTitle + "");
            vSources.add(gvSourceModel);
        }
        return this;
    }
    public GVStylePlayer addPlaySource(List<GVSourceModel> vSources){
        if(!ToolsKit.isEmpty(vSources)){
            this.vSources.addAll(vSources);
        }
        return this;
    }
    public GVStylePlayer isCache(boolean isCache){
        this.isCache = isCache;
        return this;
    }
    public GVStylePlayer isShowBackButton(boolean isShowBackButton){
        this.isShowBackButton = isShowBackButton;
        return this;
    }
    public GVStylePlayer isShowSmallPlayer(boolean isShowSmallPlayer){
        this.isShowSmallPlayer = isShowSmallPlayer;
        return this;
    }
    public GVStylePlayer isShowTitle(boolean isShowTitle){
        this.isShowTitle = isShowTitle;
        return this;
    }
    public GVStylePlayer isTransition(boolean isTransition){
        this.isTransition = isTransition;
        return this;
    }
    public GVStylePlayer isCacheVedio(boolean isCacheVedio){
        this.isCache = isCacheVedio;
        return this;
    }
    public GVStylePlayer isAotuPlay(boolean isAotuPlay){
        this.isAotuPlay = isAotuPlay;
        return this;
    }
    public <L> GVStylePlayer setThumbCoverSource(L coverSource){
        thumbCoverSource = coverSource;
        return this;
    }
    public GVStylePlayer isShowFirstFrameAsCover(boolean isShowFirstFrameAsCover){
        this.isShowFirstFrameAsCover = isShowFirstFrameAsCover;
        return this;
    }
    public GVStylePlayer isLoop(boolean isLoop){
        mLooping = isLoop;
        return this;
    }
    public GVStylePlayer isCongfigReconnect(boolean isCongfigReconnect){
        this.isCongfigReconnect = isCongfigReconnect;
        return this;
    }
    public GVStylePlayer isAotuRotate(boolean isAotuRotate){
        this.isAotuRotate = isAotuRotate;
        return this;
    }
    public GVStylePlayer isFullscreenWithLandscape(boolean isFullscreenWithLandscape){
        this.isFullscreenWithLandscape = isFullscreenWithLandscape;
        return this;
    }
    public GVStylePlayer isAlwaysShowSmallWhenScrollable(boolean isAlwaysShowSmallWhenScrollable){
        this.isAlwaysShowSmallWhenScrollable = isAlwaysShowSmallWhenScrollable;
        return this;
    }
    public GVStylePlayer isShowFullAnimation(boolean isShowFullAnimation){
        this.isShowFullAnimation = isShowFullAnimation;
        return this;
    }
    public GVStylePlayer isSpeedEnable(boolean isSpeedEnable){
        mChangePosition = isSpeedEnable;
        return this;
    }
    public GVStylePlayer isVolumeEnable(boolean isVolumeEnable){
        mChangeVolume = isVolumeEnable;
        return this;
    }
    public GVStylePlayer isBrightnessEnable(boolean isBrightnessEnable){
        mBrightness = isBrightnessEnable;
        return this;
    }
    public GVStylePlayer isVolumeChangeUi(boolean isVolumeChangeUi){
        this.isVolumeChangeUi = isVolumeChangeUi;
        return this;
    }
    public GVStylePlayer isBrightnessChangeUi(boolean isBrightnessChangeUi){
        this.isBrightnessChangeUi = isBrightnessChangeUi;
        return this;
    }
    public GVStylePlayer setSrollView(View scrollView){
        this.scrollView = scrollView;
        return this;
    }
    public GVStylePlayer setSmallWinSize(int widthDip, int heightDip){
        smallWinSize.x = (int)(widthDip* ToolsKit.getDisplayMetrics().density);;
        smallWinSize.y = (int)(heightDip* ToolsKit.getDisplayMetrics().density);;
        return this;
    }
    public GVStylePlayer setPosition(int playPos){
        this.playPos = playPos;
        return this;
    }
    public GVStylePlayer setTag(String playTag){
        this.playTag = playTag;
        return this;
    }
    public void preparePlayer(Activity activity){
        preparePlayer(activity, null);
    }
    public void preparePlayer(Activity activity, GVListener gvListener){
        if(activity == null || ToolsKit.isEmpty(vSources)) {
            ToastKit.show("activity or url param is invalide");
            return;
        }
        this.activity = activity;
        this.gvListener = gvListener;
        if(gvListener != null && gvResponse == null) gvResponse = new GVResponse();
        orientationUtils = new OrientationUtils(activity, this);
        configPlayer();
        setTransition();
    }
    protected boolean isListenerOk(){
        if(gvListener == null) return false;
        return true;
    }
    /**
     * 全屏时将对应处理参数逻辑赋给全屏播放器
     */
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
//        super.startWindowFullscreen()
        GVStylePlayer gvStylePlayer = (GVStylePlayer) super.startWindowFullscreen(context, false, false);
        gvStylePlayer.activity = activity;
        gvStylePlayer.vAspectRatioType = vAspectRatioType;
        gvStylePlayer.vImageRotateTransType = vImageRotateTransType;
        gvStylePlayer.vSources = vSources;
        gvStylePlayer.isCache = isCache;
        gvStylePlayer.resolveAspectRatio();
        return gvStylePlayer;
    }

    /**
     * 推出全屏时将对应处理参数逻辑返回给非播放器
     */
    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
        if(gsyVideoPlayer == null) return;
        GVStylePlayer gvStylePlayer = (GVStylePlayer) gsyVideoPlayer;
        activity = gvStylePlayer.activity;
        vAspectRatioType = gvStylePlayer.vAspectRatioType;
        vImageRotateTransType = gvStylePlayer.vImageRotateTransType;
        vSources = gvStylePlayer.vSources;
        isCache = gvStylePlayer.isCache;
        setUpSource();
        resolveAspectRatio();
    }

    /**
     * 需要在尺寸发生变化的时候重新处理
     */
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        super.onSurfaceTextureSizeChanged(surface, width, height);
        resolveImageRotate();
    }
    /**
     * 处理显示逻辑
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        super.onSurfaceTextureAvailable(surface, width, height);
        resolveVedioRotate();
        resolveImageRotate();
    }
    @Override
    protected void updateStartImage() {
        super.updateStartImage();
//        if(mStartButton instanceof ImageView) {
//            ImageView imageView = (ImageView) mStartButton;
//            if (mCurrentState == CURRENT_STATE_PLAYING) {
//                imageView.setImageResource(R.drawable.video_click_pause_selector);
//            } else if (mCurrentState == CURRENT_STATE_ERROR) {
//                imageView.setImageResource(R.drawable.video_click_play_selector);
//            } else {
//                imageView.setImageResource(R.drawable.video_click_play_selector);
//            }
//        }
    }
//    @Override
//    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
//        if(styleId == NoUiConrol){
//            super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
//        }
//    }
    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {
        if(isVolumeChangeUi) super.showVolumeDialog(deltaY, volumePercent);
    }
    @Override
    protected void showBrightnessDialog(float percent){
        if(isBrightnessChangeUi) super.showBrightnessDialog(percent);
    }
    @Override
    protected void onClickUiToggle() {
        if(styleId == LiveStreaming) {
            if(mIfCurrentIsFullscreen) {
                resolveLivestreamingUi();
                super.onClickUiToggle();
            }else resolveLivestreamingUi();
        }else super.onClickUiToggle();// && mIfCurrentIsFullscreen) super.onClickUiToggle();
    }
    public void onPause() {
        onVideoPause();
    }
    public void onResume() {
        onVideoResume();
    }
    public void onDestroy() {
        if (orientationUtils != null){
            orientationUtils.releaseListener();
            orientationUtils = null;
        }
        setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        if(vSources != null){
            vSources.clear();
            vSources = null;
        }
    }
    public boolean onBackPressed(){
        //先返回正常状态
        try {
            if(styleId < InTable){
//                if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                    getFullscreenButton().performClick();
//                    return false;
//                }
                if(orientationUtils != null) orientationUtils.backToProtVideo();
                if (StandardGSYVideoPlayer.backFromWindowFull(activity)) return false;
                //释放所有
//                setStandardVideoAllCallBack(null);
//                GSYVideoPlayer.releaseAllVideos();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                    XApp.getApp().getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                        }
                    }, 500);
                    return false;
                }
            }else{
                if(orientationUtils != null) orientationUtils.backToProtVideo();
                if (StandardGSYVideoPlayer.backFromWindowFull(activity)) return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public void onWinConfigurationChanged(Configuration newConfig){
        if(isPlaying() && !isSmall) onConfigurationChanged(activity, newConfig, orientationUtils);
    }

    public boolean isPrepared(){
        return isPrepared;
    }
    public boolean isPlaying(){
//        return isPlaying;
        if (getCurrentState() == GSYVideoPlayer.CURRENT_STATE_PLAYING) return true;
        return false;
    }
    public void startPlay(){
        if(isPrepared) clickStartIcon();
    }
    public void pasuePlay(){
        if(isPrepared) clickStartIcon();
    }
    public void switchToFullScreen(){
        if(!isPrepared) return;
        if(isFullscreenWithLandscape) orientationUtils.resolveByClick();
        startWindowFullscreen(activity, false, false);
    }
    public boolean QuitFromFullScreen(){
        if(!isPrepared) return false;
        if(orientationUtils != null) orientationUtils.backToProtVideo();
        return StandardGSYVideoPlayer.backFromWindowFull(activity);
    }
    public boolean switchAspectRatio(GVMode.VAspectRatio vAspectRatio){
        if(vAspectRatio == null) return false;
        if (!mHadPlay || mTextureView == null)  return false;
        vAspectRatioType = vAspectRatio.getAspectRatio();
        resolveAspectRatio();
        return true;
    }
    public boolean switchVedioRotate(float rotate){
        if (!mHadPlay || mTextureView == null) return false;
        mTextureView.setRotation(rotate);
        mTextureView.requestLayout();
        return true;
    }
    public boolean switchImageRotate(GVMode.VImageRotateTrans vImageRotateTrans){
        if (!mHadPlay || mTextureView == null) return false;
        vImageRotateTransType = vImageRotateTrans;
        resolveImageRotate();
        return true;
    }
    public void switchSpeed(float speed){
        setSpeedPlaying(speed, true);
    }
    public void switchPlayerSource(String url){
        switchPlayerSource(url, null);
    }
    public void switchPlayerSource(String vUrl, String vTitle){
        if(ToolsKit.isEmpty(vUrl)) {
            ToastKit.show("url is invalide");
            return;
        }
        GVSourceModel gvSourceModel = new GVSourceModel(vUrl, vTitle  + "");
        vSources.clear();
        vSources.add(gvSourceModel);
        curPlaySourcePos = 0;

        release();
        configPlayer();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startPlayLogic();
            }
        }, 1000);
    }
    public void switchPlayerSource(List<GVSourceModel> vSources){
        if(ToolsKit.isEmpty(vSources)) {
            ToastKit.show("urls is invalide");
            return;
        }
        this.vSources.clear();
        this.vSources.addAll(vSources);
        curPlaySourcePos = 0;

        release();
        configPlayer();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startPlayLogic();
            }
        }, 1000);
    }

    public void reset(){
        getTitleTextView().setVisibility(View.GONE);
        getBackButton().setVisibility(View.GONE);
        if(vSources != null) vSources.clear();
    }
    private boolean configPlayer(){
        if(styleId < NoUiConrol){
            if(isShowTitle) getTitleTextView().setVisibility(ToolsKit.isEmpty(vSources.get(curPlaySourcePos).title) ? View.VISIBLE : View.GONE);
            getBackButton().setVisibility(isShowBackButton && (styleId != InTable) ? View.VISIBLE : View.GONE);
            if(thumbCoverSource != null || isShowFirstFrameAsCover){
                if(thumbCoverView == null) thumbCoverView = new ImageView(activity);
                if(thumbCoverSource != null) ImageLoaderKit.withImageViewLoader(thumbCoverView, activity, thumbCoverSource);
                if(isShowFirstFrameAsCover) loadFirstFrameCover();
                setThumbImageView(thumbCoverView);
            }

            orientationUtils.setEnable(false);
            setIsTouchWiget(true);
            setRotateViewAuto(false);
            setLockLand(false);
            setShowFullAnimation(isShowFullAnimation);
            setNeedLockFull(true);
            setSeekRatio(1);
            setLooping(mLooping);
            setRotateViewAuto(isAotuRotate);
            if(playPos > -1) setPlayPosition(playPos);
            setPlayTag(playTag);

            getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isFullscreenWithLandscape) orientationUtils.resolveByClick();
                    startWindowFullscreen(activity, false, false);
                }
            });
            getBackButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                onBackPressed();
                    if(isListenerOk()){
                        if(isListenerOk()){
                            gvResponse.state = GVResponse.onClickBackButton;
                            gvListener.onGvListener(gvResponse);
                        }
                    }
                }
            });
            setLockClickListener(new LockClickListener() {
                @Override
                public void onClick(View view, boolean lock) {
                    if (orientationUtils != null) {
                        //配合下方的onConfigurationChanged
                        orientationUtils.setEnable(!lock);
                    }
                }
            });
            setStandardVideoAllCallBack(standardVideoAllCallBack);
            setSrollViewListener();
        }else if(styleId == LiveStreaming){
            resolveLivestreamingUi();

            if(thumbCoverSource != null || isShowFirstFrameAsCover){
                if(thumbCoverView == null) thumbCoverView = new ImageView(activity);
                if(thumbCoverSource != null) ImageLoaderKit.withImageViewLoader(thumbCoverView, activity, thumbCoverSource);
                if(isShowFirstFrameAsCover) loadFirstFrameCover();
                setThumbImageView(thumbCoverView);
            }

            mBottomContainer.setEnabled(false);
            mBottomContainer.setVisibility(GONE);
            mBottomProgressBar.setEnabled(false);
            mBottomProgressBar.setVisibility(GONE);
            mProgressBar.setEnabled(false);
            mProgressBar.setVisibility(GONE);

            mStartButton.setClickable(false);
            mStartButton.setEnabled(false);
            mStartButton.setVisibility(GONE);

            orientationUtils.setEnable(false);
            setIsTouchWiget(true);
            setRotateViewAuto(false);
            setLockLand(false);
            setShowFullAnimation(isShowFullAnimation);
            setNeedLockFull(true);
            setSeekRatio(1);
            setLooping(false);
            setRotateViewAuto(isAotuRotate);
            if(playPos > -1) setPlayPosition(playPos);
            setPlayTag(playTag);

            mChangePosition = false;

            setLockClickListener(new LockClickListener() {
                @Override
                public void onClick(View view, boolean lock) {
                    if (orientationUtils != null) {
                        //配合下方的onConfigurationChanged
                        orientationUtils.setEnable(!lock);
                    }
                }
            });
            setStandardVideoAllCallBack(standardVideoAllCallBack);
            setSrollViewListener();
        }
        boolean isSetSrcOk = setUpSource();
        if(!isSetSrcOk){
            ToastKit.show("url set failed");
        }
//        resolveAspectRatio();
        return isSetSrcOk;
    }
    private boolean setUpSource(){
        if(isCache) return setUp(vSources.get(curPlaySourcePos).url, isCache, new File(vCachePath), vSources.get(curPlaySourcePos).title);
        else return setUp(vSources.get(curPlaySourcePos).url, isCache, vSources.get(curPlaySourcePos).title);
    }
    private void setTransition(){
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.postponeEnterTransition();
            ViewCompat.setTransitionName(this, "transition");
            Transition transition = activity.getWindow().getSharedElementEnterTransition();
            if (transition != null) {
                transition.addListener(new Transition.TransitionListener(){
                    @Override
                    public void onTransitionStart(Transition transition) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        if(isAotuPlay) startPlayLogic();
                        transition.removeListener(this);
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });
            }
            activity.startPostponedEnterTransition();
        } else {
            if(isAotuPlay) startPlayLogic();
        }
    }
    private void resolveAspectRatio(){
        if (!mHadPlay) return;
        switch (vAspectRatioType){
            case GSYVideoType.SCREEN_TYPE_DEFAULT:  GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT); break;
            case GSYVideoType.SCREEN_TYPE_FULL:  GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL); break;
            case GSYVideoType.SCREEN_MATCH_FULL:  GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL); break;
            case GSYVideoType.SCREEN_TYPE_16_9:  GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9); break;
            case GSYVideoType.SCREEN_TYPE_4_3:  GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3); break;
            default: GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT); break;
        }
        changeTextureViewShowType();
        if(mTextureView != null) mTextureView.requestLayout();
    }
    private void resolveVedioRotate(){
        if (!mHadPlay || mTextureView == null) return;
        mTextureView.setRotation(mRotate);
        mTextureView.requestLayout();
    }
    private void resolveImageRotate(){
        if (!mHadPlay) return;
        if(matrix == null) matrix = new Matrix();
        if(vImageRotateTransType == GVMode.VImageRotateTrans.RotateTrans){
            matrix.setScale(1, 1, mTextureView.getWidth() / 2, 0);
        }else if(vImageRotateTransType == GVMode.VImageRotateTrans.LeftRightTrans){
            matrix.setScale(-1, 1, mTextureView.getWidth() / 2, 0);
        }else{
            matrix.setScale(1, -1, 0, mTextureView.getHeight() / 2);
        }
        mTextureView.setTransform(matrix);
        mTextureView.invalidate();
    }
    private void resolveLivestreamingUi(){
        if(mIfCurrentIsFullscreen){
            mTopContainer.setEnabled(true);
            mTopContainer.setVisibility(VISIBLE);

            if(isShowTitle) getTitleTextView().setVisibility(ToolsKit.isEmpty(vSources.get(curPlaySourcePos).title) ? View.VISIBLE : View.GONE);
            getBackButton().setVisibility(isShowBackButton && (styleId != InTable) ? View.VISIBLE : View.GONE);

        }else{
            mTopContainer.setEnabled(false);
            mTopContainer.setVisibility(GONE);

            getTitleTextView().setVisibility(View.GONE);
            getBackButton().setVisibility(View.GONE);
        }
    }
    private void loadFirstFrameCover() {
        if(thumbCoverView == null) return;
        Glide.with(activity)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop())
                .load(vSources.get(curPlaySourcePos).url)
                .into(thumbCoverView);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setSrollViewListener(){
        if(scrollView == null) return;
        if(scrollView instanceof NestedScrollView){
            ((NestedScrollView)scrollView).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (!isIfCurrentIsFullscreen() && scrollY >= 0 && (isPlaying() || isAlwaysShowSmallWhenScrollable)) {
                        if (scrollY > getHeight()) {
                            if(!isShowSmallPlayer){
                                onPause();
                                return;
                            }
                            if (!isSmall) {
                                isSmall = true;
                                showSmallVideo(smallWinSize, false, false);
                                orientationUtils.setEnable(false);
                            }
                        } else {
                            if (isSmall) {
                                isSmall = false;
                                orientationUtils.setEnable(true);
                                //必须
                                postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideSmallVideo();
                                    }
                                }, 100);
                            }
                        }
//                        setTranslationY((scrollY <= getHeight()) ? -scrollY : - getHeight());
                    }
                }
            });
        }else if(scrollView instanceof ScrollView){
            ((ScrollView)scrollView).setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (!isIfCurrentIsFullscreen() && scrollY >= 0 && (isPlaying() || isAlwaysShowSmallWhenScrollable)) {
                        if (scrollY > getHeight()) {
                            if(!isShowSmallPlayer){
                                onPause();
                                return;
                            }
                            if (!isSmall) {
                                isSmall = true;
                                showSmallVideo(smallWinSize, false, false);
                                orientationUtils.setEnable(false);
                            }
                        } else {
                            if (isSmall) {
                                isSmall = false;
                                orientationUtils.setEnable(true);
                                //必须
                                postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideSmallVideo();
                                    }
                                }, 100);
                            }
                        }
//                        setTranslationY((scrollY <= getHeight()) ? -scrollY : - getHeight());
                    }
                }
            });
        }
    }
    public void OnScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY){
        if (!isIfCurrentIsFullscreen() && scrollY >= 0 && (isPlaying() || isAlwaysShowSmallWhenScrollable)) {
            if (scrollY > getHeight()) {
                if(!isShowSmallPlayer){
                    onPause();
                    return;
                }
                if (!isSmall) {
                    isSmall = true;
//                    if(mSmallClose != null){
//                        mSmallClose.setPadding(0, 0, 0, 0);
//                        mSmallClose.setLeft(10);
//                        mSmallClose.setTop(10);
//                    }
                    showSmallVideo(smallWinSize, false, false);
                    orientationUtils.setEnable(false);
                }
            } else {
                if (isSmall) {
                    isSmall = false;
                    orientationUtils.setEnable(true);
                    //必须
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideSmallVideo();
                        }
                    }, 100);
                }
            }
//                        setTranslationY((scrollY <= getHeight()) ? -scrollY : - getHeight());
        }
    }
    private StandardVideoAllCallBack standardVideoAllCallBack = new StandardVideoAllCallBack() {
        @Override
        public void onPrepared(String url, Object... objects) {
            isPrepared = true;
            orientationUtils.setEnable(true);
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
            if (orientationUtils != null) orientationUtils.backToProtVideo();
        }

        @Override
        public void onQuitSmallWidget(String url, Object... objects) {

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
    };
}
