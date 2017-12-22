package com.ro.xdroid.kit;

import com.orhanobut.logger.Logger;
import com.ro.xdroid.XDroidConfig;

/**
 * Created by roffee on 2017/7/20 15:58.
 * Contact with 460545614@qq.com
 */
public class LoggerKit {
    public static void d(Object object) {
        if (!XDroidConfig.Debug) {
            return;
        }
        Logger.d(object);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        if (!XDroidConfig.Debug) {
            return;
        }
        Logger.e(throwable, message, args);
    }

    public static void d(String message, Object... args) {
        if (!XDroidConfig.Debug) {
            return;
        }
        Logger.d(message, args);
    }

    public static void i(String message, Object... args) {
        if (!XDroidConfig.Debug) {
            return;
        }
        Logger.i(message, args);
    }

    public static void w(String message, Object... args) {
        if (!XDroidConfig.Debug) {
            return;
        }
        Logger.w(message, args);
    }

    public static void json(String json) {
        if (!XDroidConfig.Debug) {
            return;
        }
        Logger.json(json);
    }

    public static void xml(String xml) {
        if (!XDroidConfig.Debug) {
            return;
        }
        Logger.xml(xml);
    }
}
