package com.xiaoaitouch.mom.net;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class SevenDoImageLoader extends ImageLoader {
    private int method = Method.GET;
    private List<BasicNameValuePair> mParams;

    public SevenDoImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }

    public ImageContainer post(String requestUrl, List<BasicNameValuePair> params, ImageListener imageListener,
            int maxWidth, int maxHeight) {
        method = Method.POST;
        mParams = params;

        return super.get(requestUrl, imageListener, maxWidth, maxHeight);
    }

    public ImageContainer post(String requestUrl, List<BasicNameValuePair> params, ImageListener listener) {
        return post(requestUrl, params, listener, 0, 0);
    }

    @Override
    protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight, final String cacheKey) {
        ImageRequest request = new ImageRequest(requestUrl, method, new Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                onGetImageSuccess(cacheKey, response);
            }
        }, maxWidth, maxHeight,
                Config.RGB_565, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onGetImageError(cacheKey, error);
                    }
                });
        if (mParams != null && method == Method.POST) {
            for (BasicNameValuePair p : mParams) {
                request.addPostParam(p);
            }
        }
        return request;
    }

}
