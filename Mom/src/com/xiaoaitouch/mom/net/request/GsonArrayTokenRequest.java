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
import com.xiaoaitouch.mom.net.response.JsonArrayResponse;
import com.xiaoaitouch.mom.util.Logger;

/**
 * 
 * @author huxin
 * 
 * @param <T>
 */
public abstract class GsonArrayTokenRequest<T> extends ParamsRequest<JsonArrayResponse<T>> {
    private Gson mGson = new Gson();
    private Listener<JsonArrayResponse<T>> mListener;

    public GsonArrayTokenRequest(String url, Listener<JsonArrayResponse<T>> listener, ErrorListener error) {
        this(Method.GET, url, listener, error);
    }

    public GsonArrayTokenRequest(int method, String url, Listener<JsonArrayResponse<T>> listener, ErrorListener error) {
        super(method, url, error);

        this.mListener = listener;
    }

    public abstract Type getType();

    @Override
    protected Response<JsonArrayResponse<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            Logger.d(json);
            JsonArrayResponse<T> fromJson = mGson.fromJson(json, getType());
            if (fromJson == null) {
                throw new JsonParseException("json is null.");
            }

            return Response.success(fromJson, HttpHeaderParser.parseCacheHeaders(response, getExpireStrategy()));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonParseException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JsonArrayResponse<T> response) {
        mListener.onResponse(response);
    }
}
