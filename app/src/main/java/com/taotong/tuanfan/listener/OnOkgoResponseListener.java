package com.taotong.tuanfan.listener;

import com.taotong.tuanfan.model.Respond;

/**
 * Created by roffee on 2017/5/22 09:01.
 * Contact with 460545614@qq.com
 * OnOkgoResponseListener: 网络请求回调接口
 */

public interface OnOkgoResponseListener {
    /**
     * OnOkgoSuccess: 网络请求成功回调此接口
     * @param  respond: 网络请求任务响应，此处涉及主要参数int key, T data, R reserve
     */
    void OnOkgoSuccess(Respond respond);
    /**
     *OnOkgoFailed: 网络请求失败回调此接口
     * @param  respond: 网络请求任务响应，此处涉及主要参数int key, String toast, boolean isAutoToastm, R reserve
     */
    void OnOkgoFailed(Respond respond);
}
