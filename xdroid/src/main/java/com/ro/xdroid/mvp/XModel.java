package com.ro.xdroid.mvp;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.Unbinder;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;

/**
 * Created by roffee on 2017/8/4 15:12.
 * Contact with 460545614@qq.com
 */
public class XModel {
    public boolean isUseAutolayout = true;
    public boolean isUseEventBus = false;
    public boolean isUseTranslucentStatusBar = true;
    public boolean isUseBaseTitleBar = true;
    public boolean isStateView = true;
    public boolean isSwipeRefresh = true;
    public Unbinder unbinder;
    public IDelegate iDelegate;
    public RxPermissions rxPermissions;
    public XRecyclerContentLayout recStateLayout;
}
