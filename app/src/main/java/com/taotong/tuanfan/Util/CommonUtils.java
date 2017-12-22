package com.taotong.tuanfan.Util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.ro.xdroid.kit.ToolsKit;
import com.taotong.tuanfan.TTApp;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * Created by Administrator on 2016/8/13 0013.
 * 项目中共同用到的方法
 */
public class CommonUtils {
    public static String getStringWeek() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWeek = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWeek)) {
            mWeek = "日";
        } else if ("2".equals(mWeek)) {
            mWeek = "一";
        } else if ("3".equals(mWeek)) {
            mWeek = "二";
        } else if ("4".equals(mWeek)) {
            mWeek = "三";
        } else if ("5".equals(mWeek)) {
            mWeek = "四";
        } else if ("6".equals(mWeek)) {
            mWeek = "五";
        } else if ("7".equals(mWeek)) {
            mWeek = "六";
        }
        return "星期" + mWeek;
    }

    /**
     * 判断SDCard是否存在,并可写
     *
     * @return
     */
    public static boolean checkSDCard() {
        String flag = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(flag)) {
            return true;
        }
        return false;
    }

    /**
     * 获取屏幕显示信息对象
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * dp转pixel
     */
    public static float dpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * dip转px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String result = "ERROR";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(TTApp.PackageName, 0);
            result = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @isEmpty: 判断常见数据容器是否为null或empty
     */
    public static <T> boolean isEmpty(T t) {
        return ToolsKit.isEmpty(t);
    }

    /**
     * @showToast: show toast方法
     * @content: toast显示的内容string
     */
    public static void showToast(String content) {
        if (StringUtils.isNULL(content)) {
            return;
        }
        Toast.makeText(TTApp.getInstance().getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }
}
