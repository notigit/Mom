package com.xiaoaitouch.mom.net.request;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.xiaoaitouch.mom.net.HttpHeaderParser;

/**
 * 
 * @author zhangcong
 * 
 * @param <T>
 */
public class GsonRequest<T> extends ParamsRequest<T> {
    private final Gson mGson = new Gson();
    private final Class<T> mClazz;
    private final Listener<T> mListener;

    public GsonRequest(String url, Class<T> clazz,
            Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mListener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        super.parseNetworkResponse(response);

        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            T obj = mGson.fromJson(json, mClazz);
            if (obj == null) {
                throw new JsonParseException("parse failed.");
            }
            return Response.success(
                    obj, HttpHeaderParser.parseCacheHeaders(response, getExpireStrategy()));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonParseException e) {
            return Response.error(new ParseError(e));
        }
    }
}
