package com.ro.xdroid.mvp;

import android.os.Bundle;
import android.view.View;

/**
 * Created by roffee on 2017/7/19 18:30.
 * Contact with 460545614@qq.com
 */
public interface IView<P> {
    void bindV(View v);
    void bindEvent();

    int getContentLayoutId();
//    int getOptionsMenuId();
    void initUse();
    void initView(Bundle savedInstanceState);

    boolean useAutoLayout(boolean... isUseAutolayout);
    boolean useEventBus(boolean... isUseEventBus);
    boolean useBaseTitleBar(boolean... isUseBaseTitleBar);
    boolean useStateView(boolean... isUseStateView);
    boolean useSwipeRefresh(boolean... isUseSwipeRefresh);

    P p();
}
