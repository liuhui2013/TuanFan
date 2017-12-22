package com.ro.xdroid.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ro.xdroid.kit.ToolsKit;
import com.ro.xdroid.mvp.XApp;

/**
 * Created by roffee on 2017/7/21 10:02.
 * Contact with 460545614@qq.com
 * @ToastKit:
 */
public class ToastKit {
    public static void show(String msg){
        show(XApp.getContext(), msg, false);
    }
    public static void show(Context context, String msg){
        show(context, msg, false);
    }
    public static void show(Context context, String msg, boolean isLong){
        if(context == null || ToolsKit.isEmpty(msg)) return;
        Toast.makeText(context, msg, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
    public static void showInUiThread(Activity activity, String msg){
        if(activity == null || ToolsKit.isEmpty(msg)) return;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void showSnackbar(String message) {
        if (XApp.getApp().getCurActivity() == null) {return;}
        View view = XApp.getApp().getCurActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        if(view == null){return;}
        Snackbar.make(view, "" + message, Snackbar.LENGTH_SHORT).show();
    }
    public static void showSnackbar(String message, boolean isLong) {
        if (XApp.getApp().getCurActivity() == null) {return;}
        View view = XApp.getApp().getCurActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        if(view == null){return;}
        Snackbar.make(view, "" + message, isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
    }
}
