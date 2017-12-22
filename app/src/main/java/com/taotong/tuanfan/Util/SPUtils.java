
package com.taotong.tuanfan.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ro.xdroid.net.ok.taotong.TTCommonParams;

public class SPUtils {

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        return editor.commit();
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SPConstant.KEY_BIPAI,
                Context.MODE_PRIVATE);
        return settings.edit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(SPConstant.KEY_BIPAI,
                Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(key, value);
        return editor.commit();
    }

    public static boolean remove(Context context, String key) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(key);
        return editor.commit();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(SPConstant.KEY_BIPAI,
                Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(SPConstant.KEY_BIPAI,
                Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(SPConstant.KEY_BIPAI,
                Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static void clearSP(Context context) {
        try {
            String avatar = SPUtils.getString(context, SPConstant.USER_AVATAR);
            int teach_job_hint = SPUtils.getInt(context, SPConstant.TEACH_JOB_HINT, 0);
            SharedPreferences settings = context.getSharedPreferences(SPConstant.KEY_BIPAI,
                    Context.MODE_PRIVATE);
            settings.edit().clear().commit();
            SPUtils.putInt(context, SPConstant.LOGIN_TYPE, 0);
            SPUtils.putString(context, SPConstant.USER_AVATAR, avatar);
          //  SPUtils.putInt(context, SPConstant.TEACH_JOB_HINT, teach_job_hint);
            TTCommonParams.autoUpdateCommonParams();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(SPConstant.KEY_BIPAI,
                Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    /*********************************************************************************
     *TODO 以下为次SP相关方法，SP的key为-----SPConstant.KEY_SECONDARY
     * 次SP退出登录后不清空，主要用来处理引导图等操作
     * ***************************************************************************/
    private static SharedPreferences getSecondSP(Context context) {
        return context.getSharedPreferences(SPConstant.KEY_SECONDARY, Context.MODE_PRIVATE);
    }

    public static int getSecondInt(Context context, String key, int defaultValue) {
        return getSecondSP(context).getInt(key, defaultValue);
    }

    public static boolean putSecondInt(Context context, String key, int value) {
        return getSecondSP(context).edit().putInt(key, value).commit();
    }

    public static boolean getSecondBoolean(Context context, String key, boolean defaultValue) {
        return getSecondSP(context).getBoolean(key, defaultValue);
    }

    public static boolean putSecondBoolean(Context context, String key, boolean value) {
        return getSecondSP(context).edit().putBoolean(key, value).commit();
    }

    public static String getSecondString(Context context, String key, String defaultValue) {
        return getSecondSP(context).getString(key, defaultValue);
    }

    public static boolean putSecondString(Context context, String key, String value) {
        return getSecondSP(context).edit().putString(key, value).commit();
    }

    public static boolean removeSecond(Context context, String key) {
        return getSecondSP(context).edit().remove(key).commit();
    }

    public static void clearSecondSP(Context context) {
        getSecondSP(context).edit().clear().apply();
    }
}
