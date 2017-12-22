package com.ro.xdroid.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ro.xdroid.R2;
import com.ro.xdroid.rx.RxKit;
import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.view.widget.StateView;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zhy.autolayout.AutoFrameLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xstatecontroller.XStateController;

/**
 * Created by roffee on 2017/7/20 15:11.
 * Contact with 460545614@qq.com
 */
public abstract class XFragment<P extends IPresent> extends RxFragment implements IView<P> {
    @BindView(R2.id.baseFrgLeftIcon)
    ImageView baseFrgLeftIcon;
    @BindView(R2.id.baseFrgLeftAction)
    AutoFrameLayout baseFrgLeftAction;
    @BindView(R2.id.baseFrgTitleTv)
    TextView baseFrgTitleTv;
    @BindView(R2.id.baseFrgRightTv)
    TextView baseFrgRightTv;
    @BindView(R2.id.baseFrgRightIcon)
    ImageView baseFrgRightIcon;
    @BindView(R2.id.baseFrgRightAction)
    AutoFrameLayout baseFrgRightAction;
    @BindView(R2.id.baseFrgTitleBar)
    AutoFrameLayout baseFrgTitleBar;
    @BindView(R2.id.baseFrgContentStateLayout)
    XStateController baseFrgContentStateLayout;
    @BindView(R2.id.baseFrgSwipeRefreshLayout)
    SwipeRefreshLayout baseFrgSwipeRefreshLayout;

    protected XActivity xActivity;
    protected LayoutInflater layoutInflater;
    private XModel xModel;
    private P p;
    private View rootView;


    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        xActivity = (XActivity) getActivity();
        layoutInflater = inflater;
        xModel = new XModel();
        initUse();
        if (rootView == null) {
            rootView = inflater.inflate(R2.layout.layout_base_frag, null);
            if (!useBaseTitleBar()) {
                baseFrgTitleBar.setVisibility(View.GONE);
            }
            int contentLayoutId = getContentLayoutId();
            if(contentLayoutId > 0){
                baseFrgContentStateLayout.addView(inflater.inflate(contentLayoutId, null));
            }
            bindV(rootView);
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLocalView();
        initView(savedInstanceState);
//        bindEvent();
    }

    @Override
    public void onStart() {
        super.onStart();
        bindEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        getvDelegate().resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getvDelegate().pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (xModel.unbinder != Unbinder.EMPTY) {
            xModel.unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }

        if (p != null) {
            p.detachV();
            p = null;
        }
        getvDelegate().destory();
        xModel = null;
    }

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

    public void setBaseLeftActionVisible(boolean visible) {
        if (useBaseTitleBar()) {
            baseFrgLeftIcon.setVisibility(visible ? View.VISIBLE : View.GONE);
            baseFrgLeftAction.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
    public void setBaseLeftIcon(int iconId) {
        if (useBaseTitleBar()) {
            baseFrgLeftIcon.setImageResource(iconId);
        }
    }

    public TextView setTitle(String text) {
        if (useBaseTitleBar()) {
            baseFrgTitleTv.setText("" + text);
        }
        return baseFrgTitleTv;
    }

    public TextView setRightActionText(String text, RxKit.RxClickListener rxClickListener) {
        if (useBaseTitleBar()) {
            baseFrgRightTv.setText("" + text);
            baseFrgRightTv.setVisibility(View.VISIBLE);
            baseFrgRightIcon.setVisibility(View.GONE);
            baseFrgRightAction.setVisibility(View.VISIBLE);
            RxKit.prevent2Click(baseFrgRightAction, rxClickListener);
        }
        return baseFrgRightTv;
    }

    public ImageView setRightActionIocn(int iconId, RxKit.RxClickListener rxClickListener) {
        if (useBaseTitleBar()) {
            baseFrgRightIcon.setImageResource(iconId);
            baseFrgRightIcon.setVisibility(View.VISIBLE);
            baseFrgRightTv.setVisibility(View.GONE);
            baseFrgRightAction.setVisibility(View.VISIBLE);
            RxKit.prevent2Click(baseFrgRightAction, rxClickListener);
        }
        return baseFrgRightIcon;
    }

    public void showContent() {
        baseFrgContentStateLayout.showContent();
    }

    public void showStateLoading() {
        baseFrgContentStateLayout.showLoading();
    }

    public void showStateErrorIcon(int iconId) {
        StateView stateView = (StateView) baseFrgContentStateLayout.getErrorView();
        stateView.setStateIcon(iconId);
    }

    public void showStateError(String error) {
        StateView stateView = (StateView) baseFrgContentStateLayout.getErrorView();
        stateView.setStateMsg(error);
        baseFrgContentStateLayout.showError();
    }

    public void showStateError(int iconId, String error) {
        StateView stateView = (StateView) baseFrgContentStateLayout.getErrorView();
        stateView.setStateIcon(iconId);
        stateView.setStateMsg(error);
        baseFrgContentStateLayout.showError();
    }

    public void setRecyStateView(XRecyclerContentLayout recStateLayout) {
        if (recStateLayout == null) {
            return;
        }
        xModel.recStateLayout = recStateLayout;
        recStateLayout.loadingView(layoutInflater.inflate( R2.layout.view_base_loading, null));
        recStateLayout.errorView(new StateView(xActivity));
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
    @Override
    public void bindV(View view) {
        xModel.unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void bindEvent() {
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
    }
    protected void onClickLeftAction() {
        getFragmentManager().popBackStack();
//        activity.getSupportFragmentManager().popBackStack();
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
            xModel.iDelegate = XDelegate.create(xActivity);
        }
        return xModel.iDelegate;
    }

    private void initLocalView() {
        if (useBaseTitleBar()) {
            RxKit.prevent2Click(baseFrgLeftAction, new RxKit.RxClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLeftAction();
                }
            });
        }
        if (useSwipeRefresh()) {
            baseFrgSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_light,
                    android.R.color.holo_red_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            baseFrgSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onSwipeRefresh();
                }
            });
        } else {
            baseFrgSwipeRefreshLayout.setClickable(false);
            baseFrgSwipeRefreshLayout.setFocusable(false);
            baseFrgSwipeRefreshLayout.setEnabled(false);
        }
        if (useStateView()) {
            baseFrgContentStateLayout.loadingView(layoutInflater.inflate(R2.layout.view_base_loading, null));
            baseFrgContentStateLayout.errorView(new StateView(xActivity));
//            baseContentStateLayout.emptyView(new StateView(this));
        }
    }
}
