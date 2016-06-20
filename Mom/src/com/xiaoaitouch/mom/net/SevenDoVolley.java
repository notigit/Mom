package com.xiaoaitouch.mom.net;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;

/**
 * 封装google volley,处理网络请求
 * 
 * @author zhangcong
 * 
 */
public class SevenDoVolley {
    private static final String DEFAULT_CACHE_DIR = "volley";
    private static final int DEFAULT_DISK_CACHE_SIZE = 5 * 1024 * 1024;// 磁盘缓存的大小

    private static RequestQueue mRequestQueue;
    private static SevenDoImageLoader mImageLoader;
    private static ShortFileLoader mFileLoader;

    private SevenDoVolley() {
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static void initCookieManager() {
        CookieManager manager = new CookieManager(new PersistentCookieStore(),
                CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
    }

    public static RequestQueue newRequestQueue(Context context, int maxDiskCacheBytes) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (NameNotFoundException e) {
        }

        HttpStack stack = null;
        if (Build.VERSION.SDK_INT >= 9) {
            stack = new HurlStack();
        } else {
            AndroidHttpClient httpClient = AndroidHttpClient.newInstance(userAgent);
            stack = new HttpClientStack(httpClient);
        }

        Network network = new BasicNetwork(stack);
        initCookieManager();
        RequestQueue queue;
        if (maxDiskCacheBytes <= -1) {
            queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        } else {
            queue = new RequestQueue(new DiskBasedCache(cacheDir, maxDiskCacheBytes), network);
        }
        queue.start();

        return queue;
    }

    public static void init(Context context) {
        mRequestQueue = newRequestQueue(context, DEFAULT_DISK_CACHE_SIZE);

        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int memCache = 1024 * 1024 * memClass / 8;

        mImageLoader = new SevenDoImageLoader(mRequestQueue, new BitmapLruCache(memCache));
        mFileLoader = new ShortFileLoader(mRequestQueue);
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static SevenDoImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public static ShortFileLoader getShortFileLoader() {
        return mFileLoader;
    }

    /**
     * 添加网络请求到请求队列中
     * 
     * @param request
     */
    public static <T> void addRequest(Request<T> request) {
        final RequestQueue requestQueue = getRequestQueue();
        requestQueue.add(request);
    }

    public static void cancleAll(Object tag) {
        final RequestQueue requestQueue = getRequestQueue();
        requestQueue.cancelAll(tag);
    }

    public static void cancleAll(RequestFilter filter) {
        RequestQueue requestQueue = getRequestQueue();
        requestQueue.cancelAll(filter);
    }

}
