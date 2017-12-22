package com.taotong.tuanfan;

import android.app.Activity;
import android.content.Context;

import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.mvp.XApp;
import com.ro.xdroid.net.ok.OkConfig;
import com.ro.xdroid.net.ok.OkKit;
import com.taotong.tuanfan.Util.CommonUtils;
import com.taotong.tuanfan.Util.GlobalConstants;
import com.taotong.tuanfan.Util.IPConstants;
import com.taotong.tuanfan.Util.SpanRefreshHandler;
import com.taotong.tuanfan.Util.StatusIntegrationConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liu on 2017/12/21.
 */

public class TTApp extends XApp {
    public static final String PackageName = "com.taotong.tuanfan";
    private static TTApp _instance;
    private List<Activity> _activityList = new ArrayList<>();

    //暂时使用跨多组件异步通知更新
    public SpanRefreshHandler spanRefreshHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance=this;
        /** 暂时使用跨多组件异步通知更新*/
        spanRefreshHandler = new SpanRefreshHandler();

    }

    @Override
    protected void initXDroidConfig() {
        XDroidConfig.Debug = GlobalConstants.DEBUG;
        XDroidConfig.PackageName = PackageName;
        XDroidConfig.QUploadGainTokenUrl = IPConstants.GROUP_CLASSS_TOKEN;
        XDroidConfig.QUploadBaseUrl = IPConstants.QINIU;

        OkConfig okConfig = new OkConfig();
        okConfig.ttStatusInteragtion = StatusIntegrationConfig.getConfig();
        OkKit.setOkConfig(okConfig);
    }

    public synchronized static TTApp getInstance() {
       /* if (_instance == null){
            _instance = new TTApp();
        }*/
        return _instance;
    }

    public static Context getAppContext() {
        return _instance.getApplicationContext();
    }

    /**
     * 添加 add Activity
     */
    public void addActivity(Activity activity) {
        _activityList.add(activity);
    }

    public void deleteActivity(Activity activity) {
        if (activity == null)
            return;

        if (_activityList.contains(activity)) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            _activityList.remove(activity);
        }

    }

    public Activity getCurActivity() {
        if (!CommonUtils.isEmpty(_activityList))
            return _activityList.get(_activityList.size() - 1);
        return null;
    }

    //finish 集合里面的Activity
    public void finishActivity() {
        try {
            for (Activity activity : _activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {    //遍历List，退出每一个Activity
        try {
            for (Activity activity : _activityList) {
                if (activity != null)
                    activity.finish();
            }
//            OkgoUtils.cancleAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}
