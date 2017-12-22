package com.taotong.tuanfan.Util;

import android.widget.Toast;

import com.ro.xdroid.net.ok.taotong.TTStatusInteragtion;
import com.taotong.tuanfan.TTApp;

/**
 * Created by android1 on 2017/9/12.
 */

public class StatusIntegrationConfig implements TTStatusInteragtion {
    private static StatusIntegrationConfig statusIntegrationConfig;

    public static StatusIntegrationConfig getConfig() {
        if (statusIntegrationConfig == null)
            statusIntegrationConfig = new StatusIntegrationConfig();
        return statusIntegrationConfig;
    }

    /**
     *
     * @param status 服务器返回的状态码
     * @param info 服务器返回的信息
     * @return true为继续进行回调，false拦截
     */
    @Override
    public boolean onTTStatusInteragtion(int status, String info) {
        switch (status) {
            case -1:
                if (SPUtils.getInt(TTApp.getAppContext(), SPConstant.LOGIN_TYPE, 0) == 1) {
                    new LoginHelper().userLogout();
                    Toast.makeText(TTApp.getAppContext(), "登陆状态异常，请重新登陆", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
//            case 1007:
//                Activity curActivity = TTApp.getInstance().getCurActivity();
//                if (curActivity == null) {
//                    new LoginHelper().userLogout();
//                    return false;
//                }
//                DialogKit.showMDialog(curActivity, "您的账号已在其它地方登陆?", "重登", "退出", new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        switch (which) {
//                            case POSITIVE:
//                                IMInitBusiness.loginIm(SPUtils.getString(TTApp.getAppContext(), SPConstant.USER_ID), SPUtils.getString(TTApp.getAppContext(), SPConstant.USER_SIG));
//                                break;
//                            case NEGATIVE:
//                                new LoginHelper().userLogout();
//                                break;
//                        }
//                    }
//                });
//                return true;
        }
        return true;
    }
}
