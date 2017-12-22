package com.ro.xdroid.cache;

/**
 * Created by roffee on 2017/8/9 16:00.
 * Contact with 460545614@qq.com
 */
public interface CacheListener {
    interface OnCacheSizeListener{
        void onCacheSize(long size);
    }
    interface OnCacheClearListener{
        void onCacheClear(boolean isOk);
    }
}
