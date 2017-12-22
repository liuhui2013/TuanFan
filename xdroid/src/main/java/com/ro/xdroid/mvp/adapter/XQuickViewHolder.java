package com.ro.xdroid.mvp.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ro.xdroid.kit.KnifeKit;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by roffee on 2017/7/25 13:32.
 * Contact with 460545614@qq.com
 */
public class XQuickViewHolder extends BaseViewHolder{
    private boolean useAutolayout = true, bindView = false;

    public XQuickViewHolder(View view) {
        super(view);
        if(useAutolayout){AutoUtils.autoSize(view);}
        if(bindView){KnifeKit.bind(this, view);}
    }
    public XQuickViewHolder(View view, boolean useAutolayout, boolean bindView) {
        super(view);
        this.useAutolayout = useAutolayout;
        this.bindView = bindView;
        if(useAutolayout){AutoUtils.autoSize(view);}
        if(bindView){KnifeKit.bind(this, view);}
    }
}
