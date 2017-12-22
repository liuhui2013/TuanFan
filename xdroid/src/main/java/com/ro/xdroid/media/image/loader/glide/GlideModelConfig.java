package com.ro.xdroid.media.image.loader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * Created by roffee on 2017/8/7 09:27.
 * Contact with 460545614@qq.com
 */
@GlideModule
public class GlideModelConfig extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        //重新设置内存限制
        builder.setMemoryCache(new LruResourceCache(100*1024*1024));
    }
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
//        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}

