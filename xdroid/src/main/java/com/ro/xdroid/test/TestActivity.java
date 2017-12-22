package com.ro.xdroid.test;

import android.os.Bundle;

import com.ro.xdroid.mvp.IDelegate;
import com.ro.xdroid.mvp.XActivity;

/**
 * Created by roffee on 2017/7/24 16:50.
 * Contact with 460545614@qq.com
 */
public class TestActivity extends XActivity {
    @Override
    public int getContentLayoutId() {
        return 0;
    }

    @Override
    public void initUse() {
        useAutoLayout(false);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }
    @Override
    public Object p() {
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected IDelegate getvDelegate() {
        return super.getvDelegate();
    }
}
