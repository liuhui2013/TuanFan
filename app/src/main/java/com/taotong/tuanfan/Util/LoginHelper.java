package com.taotong.tuanfan.Util;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ro.xdroid.net.ok.OkKit;
import com.taotong.tuanfan.TTApp;
import com.taotong.tuanfan.listener.OnOkgoResponseListener;
import com.taotong.tuanfan.model.MySelfInfo;
import com.taotong.tuanfan.model.Respond;
import com.taotong.tuanfan.model.UserInformationModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by android1 on 2017/1/12.
 */

public class LoginHelper implements OnOkgoResponseListener {
    private Context context = TTApp.getInstance().getApplicationContext();
    public static final int USER_LOGOUT = 99;
    private final int UNLOGIN_USERINFO = 98;

    public int parsJson(Object data) {
        UserInformationModel loginModel = (UserInformationModel) data;
        int status = loginModel.getStatus();
        String info = loginModel.getInfo();
        if (status == 1) {
            SPUtils.putInt(context, SPConstant.LOGIN_TYPE, 1);
            MySelfInfo dataBean = loginModel.getData();
            String sig = dataBean.getSig();
            String uid = dataBean.getUid();
            //TODO IM登陆
//            if (sig != null && !"".equalsIgnoreCase(sig)) {
//                IMInitBusiness.loginIm(uid, sig);
//            } else {
//                IMInitBusiness.getSig();
//            }
            dataBean.writeToCache(context);
//                edit.putString(SPConstant.USER_ID, uid).commit();
        } else {
            if (status == -1) {
                userLogout();
            }
            Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        Map map = new HashMap();
        map.put("uid", SPUtils.getString(context, SPConstant.USER_ID));
     //   OkgoUtils.postRequest(UNLOGIN_USERINFO, IPConstants.USER_INFO, map, null, this, OkgoUtils.FLAG_PARAMS_ENCRYPT | OkgoUtils.FLAG_SHOW_ERROR);

    }

    /**
     * 退出登录
     */
    public void userLogout() {
        OkKit.cancelAll();
        SPUtils.clearSP(context);
    //    OkgoUtils.postRequest(USER_LOGOUT, IPConstants.LOGOUT, new HashMap<String, String>(), null, this, OkgoUtils.FLAG_PARAMS_ENCRYPT | OkgoUtils.FLAG_SHOW_ERROR);

    }

    public void hanldeLogOut() {
        //IM登陆
        //有账户登录直接IM登录
     //   IMInitBusiness.logout();

        TTApp.getInstance().finishActivity();
        //TODO 暂时隐藏
//        Intent intent = new Intent();
//        intent.setClass(context, LoginActivity2.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //注意本行的FLAG设置
//        context.startActivity(intent);
    }

    @Override
    public void OnOkgoSuccess(Respond respond) {
        switch (respond.key) {
            case USER_LOGOUT:
                hanldeLogOut();
                break;
            case UNLOGIN_USERINFO:
                if (respond.data != null) {
                    Gson gson = new Gson();
                    UserInformationModel loginModel = gson.fromJson((String) respond.data, UserInformationModel.class);
                    parsJson(loginModel);
                }
                break;
        }
    }

    @Override
    public void OnOkgoFailed(Respond respond) {
        switch (respond.key) {
            case USER_LOGOUT:
                hanldeLogOut();
                break;
        }
    }
}
