package com.ro.xdroid.integrate;

import android.app.Activity;

/**
 * Created by roffee on 2017/7/21 10:14.
 * Contact with 460545614@qq.com
 */
public class AppManager {
    private Activity curActivity;

    public Activity getCurActivity() {
        return curActivity;
    }

    public void setCurActivity(Activity curActivity) {
        this.curActivity = curActivity;
    }
}
