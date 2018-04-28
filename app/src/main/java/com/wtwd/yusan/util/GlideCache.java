package com.wtwd.yusan.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Administrator on 2018/4/27 0027.
 */


/**
 * Glide设置缓存位置
 * 当前为内部存储，未Root手机不能进入文件夹查看
 */
public class GlideCache implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        //手机app路径
        appRootPath = context.getCacheDir().getPath() + "/cache/images";
//        Log.e("TAG", "image path : ---> " + getStorageDirectory() + "/cache/images");
//        builder.setDiskCache(new DiskLruCacheFactory(getStorageDirectory() + "/GlideDisk", diskCacheSizeBytes));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, appRootPath, diskCacheSizeBytes));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

    }

    //外部路径
    private String sdRootPath = Environment.getExternalStorageDirectory().getPath();
    private String appRootPath = null;

    private String getStorageDirectory() {
        Log.e("TAG", "Environment.MEDIA_MOUNTED : ---> " + Environment.MEDIA_MOUNTED);
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                sdRootPath : appRootPath;
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
