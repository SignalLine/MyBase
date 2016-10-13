package com.rilin.lzy.mybase.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by lzy on 16/3/3.
 */
public class BitmapCache implements ImageLoader.ImageCache {
    //LruCache对象
    private LruCache<String, Bitmap> lruCache ;
    //设置最大缓存为10Mb，大于这个值会启动自动回收
   // private int max = 20*1024*1024;

    // 获取应用程序最大可用内存
    int maxMemory = (int) Runtime.getRuntime().maxMemory();
    int max = maxMemory / 8;

    public BitmapCache(){
        //初始化 LruCache
        lruCache = new LruCache<String, Bitmap>(max){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String url) {
        L.d("000000-------0000000----------000000-------000000-------0000000------00000--=", url);
        return lruCache.get(url);
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        L.d("111111-------1111111----------11111-------1111-------1111111------11111111--=", url);
        lruCache.put(url, bitmap);
    }
}