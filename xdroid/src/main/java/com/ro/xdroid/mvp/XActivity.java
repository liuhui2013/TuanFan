package com.ro.xdroid.mvp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ro.xdroid.R2;
import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.kit.AutoLayoutKit;
import com.ro.xdroid.kit.KnifeKit;
import com.ro.xdroid.rx.RxKit;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.view.dialog.DialogKit;
import com.ro.xdroid.view.dialog.LoadingDialog;
import com.ro.xdroid.view.dialog.ToastKit;
import com.ro.xdroid.view.widget.StateView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xstatecontroller.XStateController;

/**
 * Created by roffee on 2017/7/20 09:08.
 * Contact with 460545614@qq.com
 */
public abstract class XActivity<P extends IPresent> extends RxAppCompatActivity implements IView<P> {
    @BindView(R2.id.baseStatusBar)
    View baseStatusBar;
    @BindView(R2.id.baseLeftIcon)
    ImageView baseLeftIcon;
    @BindView(R2.id.baseLeftAction)
    AutoFrameLayout baseLeftAction;
    @BindView(R2.id.baseTitleTv)
    TextView baseTitleTv;
    @BindView(R2.id.baseRightTv)
    TextView baseRightTv;
    @BindView(R2.id.baseRightIcon)
    ImageView baseRightIcon;
    @BindView(R2.id.baseRightAction)
    AutoFrameLayout baseRightAction;
    @BindView(R2.id.baseTitleBar)
    AutoFrameLayout baseTitleBar;
    @BindView(R2.id.baseContentStateLayout)
    XStateController baseContentStateLayout;
    @BindView(R2.id.baseSwipeRefreshLayout)
    SwipeRefreshLayout baseSwipeRefreshLayout;
    @BindView(R2.id.baseHeaderLayout)
    AutoLinearLayout baseHeaderLayout;

    protected Activity activity;
    private XModel xModel;
    private P p;
    private int setStatusBarTintColor = getResources().getColor(R2.color.headerLayoutBg);// = Color.parseColor("#24262e");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        XApp.getApp().setCurActivity(this);
        xModel = new XModel();
        setContentView(R2.layout.layout_base_act);
        initUse();
        setTranslucentStatus();
        if (!useBaseTitleBar()) {
            baseTitleBar.setVisibility(View.GONE);
        }
        int contentLayoutId = getContentLayoutId();
        if (contentLayoutId > 0) {
            baseContentStateLayout.addView(View.inflate(this, contentLayoutId, null));
        }
        bindV(null);
        initLocalView();
        initView(savedInstanceState);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (useAutoLayout()) {
            view = AutoLayoutKit.convertAutoView(name, context, attrs);
        }
        return view == null ? super.onCreateView(name, context, attrs) : view;
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        XApp.getApp().setCurActivity(this);
        getvDelegate().resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getvDelegate().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }

        if (xModel.unbinder != Unbinder.EMPTY) {
            xModel.unbinder.unbind();
        }

        if (p != null) {
            p.detachV();
            p = null;
        }
        getvDelegate().destory();
        xModel = null;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (getOptionsMenuId() > 0) {
//            getMenuInflater().inflate(getOptionsMenuId(), menu);
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean useAutoLayout(boolean... isUseAutolayout) {
        if(ToolsKit.isEmpty(isUseAutolayout)){xModel.isUseAutolayout = isUseAutolayout[0];}
        return xModel.isUseAutolayout;
    }

    @Override
    public boolean useEventBus(boolean... isUseEventBus) {
        if(ToolsKit.isEmpty(isUseEventBus)){xModel.isUseEventBus = isUseEventBus[0];}
        return xModel.isUseEventBus;
    }
    public boolean useTranslucentStatusBar(boolean... isUseTranslucentStatusBar) {
        if(ToolsKit.isEmpty(isUseTranslucentStatusBar)){xModel.isUseTranslucentStatusBar = isUseTranslucentStatusBar[0];}
        return xModel.isUseTranslucentStatusBar;
    }
    @Override
    public boolean useBaseTitleBar(boolean... isUseBaseTitleBar) {
        if(ToolsKit.isEmpty(isUseBaseTitleBar)){xModel.isUseBaseTitleBar = isUseBaseTitleBar[0];}
        return xModel.isUseBaseTitleBar;
    }
    @Override
    public boolean useStateView(boolean... isStateView) {
        if(ToolsKit.isEmpty(isStateView)){xModel.isStateView = isStateView[0];}
        return xModel.isStateView;
    }
    @Override
    public boolean useSwipeRefresh(boolean... isSwipeRefresh) {
        if(ToolsKit.isEmpty(isSwipeRefresh)){xModel.isSwipeRefresh = isSwipeRefresh[0];}
        return xModel.isSwipeRefresh;
    }

    public void setStatusBarTintColorId(int colorId) {
        setStatusBarTintColor = colorId;
    }

    public void setBaseLeftActionVisible(boolean visible) {
        if (useBaseTitleBar()) {
            baseLeftIcon.setVisibility(visible ? View.VISIBLE : View.GONE);
            baseLeftAction.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
    public void setBaseLeftIcon(int iconId) {
        if (useBaseTitleBar()) {
            baseLeftIcon.setImageResource(iconId);
        }
    }

    public TextView setTitle(String text) {
        if (useBaseTitleBar()) {
            baseTitleTv.setText("" + text);
        }
        return baseTitleTv;
    }

    public TextView setRightActionText(String text, RxKit.RxClickListener rxClickListener) {
        if (useBaseTitleBar()) {
            baseRightTv.setText("" + text);
            baseRightTv.setVisibility(View.VISIBLE);
            baseRightIcon.setVisibility(View.GONE);
            baseRightAction.setVisibility(View.VISIBLE);
            RxKit.prevent2Click(baseRightAction, rxClickListener);
        }
        return baseRightTv;
    }

    public ImageView setRightActionIocn(int iconId, RxKit.RxClickListener rxClickListener) {
        if (useBaseTitleBar()) {
            baseRightIcon.setImageResource(iconId);
            baseRightIcon.setVisibility(View.VISIBLE);
            baseRightTv.setVisibility(View.GONE);
            baseRightAction.setVisibility(View.VISIBLE);
            RxKit.prevent2Click(baseRightAction, rxClickListener);
        }
        return baseRightIcon;
    }

    public void showContent() {
        baseContentStateLayout.showContent();
    }

    public void showStateLoading() {
        baseContentStateLayout.showLoading();
    }

    public void showStateErrorIcon(int iconId) {
        StateView stateView = (StateView) baseContentStateLayout.getErrorView();
        stateView.setStateIcon(iconId);
    }
    public void showStateError(String error) {
        StateView stateView = (StateView) baseContentStateLayout.getErrorView();
        stateView.setStateMsg(error);
        baseContentStateLayout.showError();
    }

    public void showStateError(int iconId, String error) {
        StateView stateView = (StateView) baseContentStateLayout.getErrorView();
        stateView.setStateIcon(iconId);
        stateView.setStateMsg(error);
        baseContentStateLayout.showError();
    }

    public void setRecyStateView(XRecyclerContentLayout recStateLayout) {
        if (recStateLayout == null) {
            return;
        }
        xModel.recStateLayout = recStateLayout;
        recStateLayout.loadingView(View.inflate(this, R2.layout.view_base_loading, null));
        recStateLayout.errorView(new StateView(this));
//        recyclerContentLayout.emptyView(new StateView(this));
    }

    public void showRecyContent() {
        if (xModel.recStateLayout == null) {
            return;
        }
        xModel.recStateLayout.showContent();
    }

    public void showRecyStateLoading() {
        if (xModel.recStateLayout == null) {
            return;
        }
        xModel.recStateLayout.showLoading();
    }

    public void showRecyStateErrorIcon(int iconId) {
        if (xModel.recStateLayout == null) {
            return;
        }
        StateView stateView = (StateView) xModel.recStateLayout.getErrorView();
        stateView.setStateIcon(iconId);
    }
    public void showRecyStateError(String error) {
        if (xModel.recStateLayout == null) {
            return;
        }
        StateView stateView = (StateView) xModel.recStateLayout.getErrorView();
        stateView.setStateMsg(error);
        xModel.recStateLayout.showError();
    }

    public void showRecyStateError(int iconId, String error) {
        if (xModel.recStateLayout == null) {
            return;
        }
        StateView stateView = (StateView) xModel.recStateLayout.getErrorView();
        stateView.setStateIcon(iconId);
        stateView.setStateMsg(error);
        xModel.recStateLayout.showError();
    }

    public LoadingDialog showLoading() {
        return DialogKit.showLoading(this);
    }
    public void dimissLoading() {
        DialogKit.dismissLoading();
    }
    public void showToast(String msg){
        ToastKit.show(this, msg);
    }
    public void showSnackbar(String msg){
        ToastKit.showSnackbar(msg);
    }
    protected void onClickLeftAction() {
        finish();
    }
    protected void onSwipeRefresh() {
    }

    protected P getP() {
        if (p == null) {
            p = p();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }

    protected IDelegate getvDelegate() {
        if (xModel.iDelegate == null) {
            xModel.iDelegate = XDelegate.create(this);
        }
        return xModel.iDelegate;
    }

    protected RxPermissions getRxPermissions() {
        if (xModel.rxPermissions == null) {
            xModel.rxPermissions = new RxPermissions(this);
            xModel.rxPermissions.setLogging(XDroidConfig.Debug);
        }
        return xModel.rxPermissions;
    }

    @Override
    public void bindV(View view) {
        xModel.unbinder = KnifeKit.bind(this);
    }

    @Override
    public void bindEvent() {
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    private void setTranslucentStatus() {
        if (!useTranslucentStatusBar()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        // 设置一个状态栏颜色
        tintManager.setStatusBarTintResource(setStatusBarTintColor);
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @TargetApi(9)
    private void initLocalView() {
        if (useBaseTitleBar()) {
            RxKit.prevent2Click(baseLeftAction, new RxKit.RxClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLeftAction();
                }
            });
        }
        if (useSwipeRefresh()) {
            baseSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_light,
                    android.R.color.holo_red_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            baseSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onSwipeRefresh();
                }
            });
        } else {
            baseSwipeRefreshLayout.setClickable(false);
            baseSwipeRefreshLayout.setFocusable(false);
            baseSwipeRefreshLayout.setEnabled(false);
        }
        if (useStateView()) {
            baseContentStateLayout.loadingView(View.inflate(this, R2.layout.view_base_loading, null));
            baseContentStateLayout.errorView(new StateView(this));
//            baseContentStateLayout.emptyView(new StateView(this));
        }
    }
}
