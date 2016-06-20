package com.xiaoaitouch.mom.net.request;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.xiaoaitouch.mom.net.HttpHeaderParser;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.util.Logger;

public abstract class GsonTokenRequest<T> extends ParamsRequest<JsonResponse<T>> {
    private Gson mGson = new Gson();
    private Listener<JsonResponse<T>> mListener;

    public GsonTokenRequest(String url, Listener<JsonResponse<T>> listener, ErrorListener error) {
        this(Method.GET, url, listener, error);
    }

    public GsonTokenRequest(int method, String url, Listener<JsonResponse<T>> listener, ErrorListener error) {
        super(method, url, error);

        this.mListener = listener;
    }

    public abstract Type getType();

    @Override
    protected Response<JsonResponse<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            Logger.d(json);

            JsonResponse<T> fromJson = mGson.fromJson(json, getType());
            if (fromJson == null) {
                throw new JsonParseException("parse failed.");
            }
            return Response.success(fromJson, HttpHeaderParser.parseCacheHeaders(response, getExpireStrategy()));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonParseException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JsonResponse<T> response) {
        mListener.onResponse(response);
    }
}
