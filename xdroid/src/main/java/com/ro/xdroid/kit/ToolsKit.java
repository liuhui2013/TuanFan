package com.ro.xdroid.kit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import com.ro.xdroid.XDroidConfig;
import com.ro.xdroid.mvp.XApp;
import com.ro.xdroid.view.dialog.ToastKit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by roffee on 2017/7/21 09:40.
 * Contact with 460545614@qq.com
 * @ToolsKit：通用工具类，提供String处理、screen & dev、SharedPreferences.....api
 */
public class ToolsKit {
    /***************************null & empty & string trans*******************************/
    /**
     * isEmpty 常用数据类型变量判空 null & empty
     * @param t 变量
     * @param <T> 泛型类型
     * @return boolean
     */
    public static <T> boolean isEmpty(T t) {
        if(t == null){return true;}

        if (t instanceof String) {
            return ((String) t).length() < 1;
        } else if (t instanceof List) {
            return ((List) t).isEmpty();
        } else if (t instanceof Map) {
            return ((Map) t).isEmpty();
        } else if (t instanceof String[]) {
            return ((String[])t).length < 1;
        }else if (t instanceof int[]) {
            return ((int[])t).length < 1;
        }else if (t instanceof long[]) {
            return ((long[])t).length < 1;
        }else if (t instanceof boolean[]) {
            return ((boolean[])t).length < 1;
        }else if (t instanceof float[]) {
            return ((float[])t).length < 1;
        }else if (t instanceof byte[]) {
            return ((byte[])t).length < 1;
        }else if (t instanceof Array[]) {
            return ((Array[])t).length < 1;
        }else if (t instanceof Object[]) {
            return ((Object[])t).length < 1;
        }else if (t instanceof SparseArray) {
            return ((SparseArray)t).size() < 1;
        }else if (t instanceof SparseBooleanArray) {
            return ((SparseBooleanArray)t).size() < 1;
        }else if (t instanceof SparseIntArray) {
            return ((SparseIntArray)t).size() < 1;
        }else if (t instanceof SparseLongArray) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return ((SparseLongArray)t).size() < 1;
            }
        }
        return false;
    }
    public static String ToSBC(String s){
        //半角转全角
        if(isEmpty(s)) return s;
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = s.toCharArray();
        for(char c : chars){
            if(c == ' '){ //32
                // 如果是半角空格，直接用全角空格替代
                stringBuilder.append((char) 12288);
            }else if(c >= 33 && c <= 126){
                // 字符是!到~之间的可见字符
                stringBuilder.append((char) (c + 65248));
            }else{
                // 不对空格以及ascii表中其他可见字符之外的字符做任何处理
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
    public static String ToDBC(String s) {
        //全角转半角
        if(isEmpty(s)) return s;
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = s.toCharArray();
        for(char c : chars){
            if(c == 12288){
                // 如果是全角空格
                stringBuilder.append(' ');
            }else if(c >= 65281 && c <= 65374){
                // 如果位于全角！到全角～区间内
                stringBuilder.append((char)(c + 65248));
            }else{
                // 不处理全角空格，全角！到全角～区间外的字符
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    /***************************packaget*******************************/
    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String result = "ERROR";
        if(context == null){return result;}
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            result = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
    /***************************screen & dev*******************************/
//    private static WindowManager windowManager; private static DisplayMetrics displayMetrics;
    public static DisplayMetrics getDisplayMetrics() {
//        if(windowManager == null) windowManager  = (WindowManager) XApp.getContext().getSystemService(Context.WINDOW_SERVICE);
//        if(displayMetrics == null) displayMetrics = new DisplayMetrics();
//        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return XApp.getContext().getResources().getDisplayMetrics();
    }
    public static int dip2px(float dipValue) {
        float density = getDisplayMetrics().density;
        return (int) (dipValue * density + 0.5f);
    }
    public static int px2dip(float pxValue) {
        final float density = getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }
    /***************************SharedPreferences*******************************/
    public static SharedPreferences getSP() {
        return XApp.getContext().getSharedPreferences(XDroidConfig.SPAppKey, XDroidConfig.SPAppMode);
    }
    public static SharedPreferences getSP(Context context) {
        if(context == null){return null;}
        return context.getSharedPreferences(XDroidConfig.SPAppKey, XDroidConfig.SPAppMode);
    }
    public static SharedPreferences.Editor getSPEditor() {
        return XApp.getContext().getSharedPreferences(XDroidConfig.SPAppKey, XDroidConfig.SPAppMode).edit();
    }
    public static SharedPreferences.Editor getSPEditor(Context context) {
        if(context == null){return null;}
        return context.getSharedPreferences(XDroidConfig.SPAppKey, XDroidConfig.SPAppMode).edit();
    }
    public static <T> boolean putSPValue(String key, T value){
        if(isEmpty(key)){return false;}
        SharedPreferences.Editor editor = getSPEditor();
        if(editor == null){return false;}
        if(value instanceof String) editor.putString(key, (String) value);
        else if(value instanceof Set<?>) editor.putStringSet(key, (Set<String>) value);
        else if(value instanceof Integer) editor.putInt(key, (Integer) value);
        else if(value instanceof Long) editor.putLong(key, (Long) value);
        else if(value instanceof Float) editor.putFloat(key, (Float) value);
        else if(value instanceof Boolean) editor.putBoolean(key, (Boolean) value);
        return editor.commit();
    }
    public static <T> boolean putSPValue(Context context, String key, T value){
        if(isEmpty(key)){return false;}
        SharedPreferences.Editor editor = getSPEditor(context);
        if(editor == null){return false;}
        if(value instanceof String) editor.putString(key, (String) value);
        else if(value instanceof Set<?>) editor.putStringSet(key, (Set<String>) value);
        else if(value instanceof Integer) editor.putInt(key, (Integer) value);
        else if(value instanceof Long) editor.putLong(key, (Long) value);
        else if(value instanceof Float) editor.putFloat(key, (Float) value);
        else if(value instanceof Boolean) editor.putBoolean(key, (Boolean) value);
        return editor.commit();
    }
    public static String getSPString(String key, String... defaultValue){
        String dv = null;
        if(!isEmpty(defaultValue)){dv = defaultValue[0];}
        if(isEmpty(key)){return dv;}
        SharedPreferences sp = getSP();
        if(sp == null){return dv;}
        return sp.getString(key, dv);
    }
    public static Set<String> getSPStringSet(String key, Set<String>... defaultValue){
        Set<String> dv = null;
        if(!isEmpty(defaultValue)){dv = defaultValue[0];}
        if(isEmpty(key)){return dv;}
        SharedPreferences sp = getSP();
        if(sp == null){return dv;}
        return sp.getStringSet(key, dv);
    }
    public static int getSPInt(String key, int... defaultValue){
        int dv = -1;
        if(!isEmpty(defaultValue)){dv = defaultValue[0];}
        if(isEmpty(key)){return dv;}
        SharedPreferences sp = getSP();
        if(sp == null){return dv;}
        return sp.getInt(key, dv);
    }
    public static long getSPLong(String key, long... defaultValue){
        long dv = -1;
        if(!isEmpty(defaultValue)){dv = defaultValue[0];}
        if(isEmpty(key)){return dv;}
        SharedPreferences sp = getSP();
        if(sp == null){return dv;}
        return sp.getLong(key, dv);
    }
    public static float getSPFloat(String key, float... defaultValue){
        float dv = -1.0f;
        if(!isEmpty(defaultValue)){dv = defaultValue[0];}
        if(isEmpty(key)){return dv;}
        SharedPreferences sp = getSP();
        if(sp == null){return dv;}
        return sp.getFloat(key, dv);
    }
    public static boolean getSPBoolean(String key, boolean... defaultValue){
        boolean dv = false;
        if(!isEmpty(defaultValue)){dv = defaultValue[0];}
        if(isEmpty(key)){return dv;}
        SharedPreferences sp = getSP();
        if(sp == null){return dv;}
        return sp.getBoolean(key, dv);
    }
    public void clearSP(){
        getSPEditor().clear().apply();
    }
    /***************************net work*******************************/
    public static boolean isWifiAvailable(boolean... isToast) {
        boolean isAvailabel = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) (XApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isAvailable()){
                        isAvailabel =  true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!isEmpty(isToast) && isToast[0] && !isAvailabel){
            ToastKit.show("当前wifi不可用，请检查网络！");
        }
        return false;
    }
    public static boolean isNetworkAvailable(boolean... isToast) {
        boolean isAvailabel = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) (XApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    isAvailabel = networkInfo.isAvailable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!isEmpty(isToast) && isToast[0] && !isAvailabel){
            ToastKit.show("当前网络不可用，请检查网络！");
        }
        return isAvailabel;
    }
    /*************************** Storage *******************************/
    public static String getAvailableStorage() {
        File sf;
        try {
            sf = Environment.getExternalStorageDirectory();
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && sf.exists() && sf.isDirectory()
                    && sf.canWrite()) {
                return sf.getPath();
            }

            sf = Environment.getDownloadCacheDirectory();
            if (sf.exists() && sf.isDirectory()
                    && sf.canWrite()) {
                return sf.getPath();
            }

            sf = Environment.getDataDirectory();
            if (sf.exists() && sf.isDirectory()
                    && sf.canWrite()) {
                return sf.getPath();
            }

            sf = Environment.getRootDirectory();
            if (sf.exists() && sf.isDirectory()
                    && sf.canWrite()) {
                return sf.getPath();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return XApp.getContext().getCacheDir().getPath();
    }
    public static List<String> getStorage() {
        List<String> paths = new ArrayList<String>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED)
                && extFile.exists() && extFile.isDirectory()
                && extFile.canWrite()) {
            paths.add(extFile.getAbsolutePath());
        }
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            // obtain executed result of command line code of 'mount', to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                paths.add(mountPath);
            }

            if(ToolsKit.isEmpty(paths)) {
                File sf = Environment.getDownloadCacheDirectory();
                if (sf.exists() && sf.isDirectory()
                        && sf.canWrite()){
                    paths.add(sf.getPath());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                if(is != null){is.close();}
                if(isr != null){isr.close();}
                if(br != null){br.close();}
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return paths;
    }

    /*************************** bitmap & bytes & drawable *******************************/
    public static byte[] bitmap2Bytes(Bitmap bitmapm) {
        if(bitmapm == null) return null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmapm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        if(ToolsKit.isEmpty(bytes)){return null;}
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(XApp.getContext().getResources(), bitmap);
    }
    public static Drawable bytes2Drawable(byte[] bytes) {
        Bitmap bitmap = bytes2Bitmap(bytes);
        if(bitmap != null){
            return bitmap2Drawable(bitmap);
        }
        return null;
    }
    public static List<Bitmap> getVideoFrames(String vPath, int intervalSeccond){
        if(ToolsKit.isEmpty(vPath)) return null;
        MediaMetadataRetriever metadataRetriever = null;
        try {
            metadataRetriever = new MediaMetadataRetriever();
            metadataRetriever.setDataSource(vPath);
            String sTime = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(isEmpty(sTime)) return null;
            int ds = Integer.valueOf(sTime) / 1000;
            if(ds < 1) return null;
            List<Bitmap> bitmaps = new ArrayList<>();
            if(intervalSeccond < 0) intervalSeccond = 0;
            for(int i = 1; i < ds;){
                Bitmap bitmap = metadataRetriever.getFrameAtTime(i * 1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                if(bitmap != null) bitmaps.add(bitmap);
                i += intervalSeccond;
            }
            return bitmaps;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }finally {
            if(metadataRetriever != null){
                metadataRetriever.release();
                metadataRetriever = null;
            }
        }
        return null;
    }
    public static Bitmap getVideoFrame(String vPath, int atSeccond){
        if(ToolsKit.isEmpty(vPath)) return null;
        MediaMetadataRetriever metadataRetriever = null;
        try {
            metadataRetriever = new MediaMetadataRetriever();
            metadataRetriever.setDataSource(vPath);
            String sTime = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(isEmpty(sTime)) return null;
            int ds = Integer.valueOf(sTime) / 1000;
            if(ds < 1) return null;
            if(atSeccond < 1) atSeccond = 1;
            else if(atSeccond > ds) atSeccond = ds;
            Bitmap bitmap = metadataRetriever.getFrameAtTime(atSeccond * 1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            return bitmap;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }finally {
            if(metadataRetriever != null){
                metadataRetriever.release();
                metadataRetriever = null;
            }
        }
        return null;
    }

    /*************************** format *******************************/
    private static SimpleDateFormat simpleDateFormat;
    public static String getFormatDuration(int duration){
        if(simpleDateFormat == null) simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        if(duration <= 0){return "00:00";}
        return simpleDateFormat.format(duration);
    }
    public static void closeFormat(){
        simpleDateFormat = null;
    }

    /***************************lifecycle*******************************/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static <W> boolean isLifecycle(W w){
        if(w == null) return true;

        boolean isLifecycle = false;
        if(w instanceof Activity){
            Activity activity = (Activity)w;
            if(activity.isFinishing() || activity.isDestroyed()){
                isLifecycle = true;
            }
        }else if(w instanceof Fragment){
            Fragment fragment = (Fragment)w;
            if(fragment.isRemoving() || fragment.isDetached()){
                isLifecycle = true;
            }
        }else if(w instanceof android.app.Fragment){
            android.app.Fragment afragment = (android.app.Fragment)w;
            if(afragment.isRemoving() || afragment.isDetached()){
                isLifecycle = true;
            }
        }
        return isLifecycle;
    }
}
