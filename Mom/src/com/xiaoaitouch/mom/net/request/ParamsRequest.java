package com.xiaoaitouch.mom.net.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.xiaoaitouch.mom.net.ExpireStrategy;
import com.xiaoaitouch.mom.util.Logger;
import com.xiaoaitouch.mom.util.Utils;

/**
 * 
 * @author zhangcong
 * 
 * @param <T>
 */
public class ParamsRequest<T> extends Request<T> {
    private final Map<String, String> mQueryParams = new HashMap<String, String>();
    private final Map<String, String> mPostParams = new HashMap<String, String>();
    private Map<String, String> mHeaders = new HashMap<String, String>();
    private ExpireStrategy mExpireStrategy;

    public ParamsRequest(String url, ErrorListener listener) {
        this(Method.GET, url, listener);
    }

    public ParamsRequest(int method, String url,
            Response.ErrorListener listener) {
        super(method, url, listener);

        // TODO不开启重试，服务器图片处理阻塞会导致超实重复发送消息,等服务等解决之后开启重试
        setRetryPolicy(new RetryPolicy() {

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }

            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }
        });
    }

    public void setExpireStrategy(ExpireStrategy strategy) {
        this.mExpireStrategy = strategy;
    }

    public ExpireStrategy getExpireStrategy() {
        return mExpireStrategy;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        // parseCookie(response.headers);

        return null;
    }

    @Override
    protected void deliverResponse(T response) {

    }

    @Override
    public String getUrl() {
        String url = super.getUrl();
        Set<String> keySet = mQueryParams.keySet();

        for (String key : keySet) {
            url = Uri.parse(url).buildUpon().appendQueryParameter(key, mQueryParams.get(key)).build().toString();
        }

        Logger.d(url);

        return url;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        addHeader(PropertyKey.INTERFACE_VERSION, PropertyValue.INTERFACE_VERSION + "");
        addHeader(PropertyKey.SYSTEM_VERSION, Utils.getVersionCode() + "");
        addHeader(PropertyKey.ANDROID_SYSTEM, PropertyValue.ANDROID_SYSTEM);
        addHeader(PropertyKey.ANDROID_VERSION, String.valueOf(android.os.Build.VERSION.SDK_INT));
        addHeader(PropertyKey.CHANNEL, Utils.getChannel());
        addHeader(PropertyKey.APPID, Utils.getAppID());

        return mHeaders;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mPostParams;
    }

    public ParamsRequest<T> addPostParam(BasicNameValuePair param) {
        addPostParam(param.getName(), param.getValue());

        return this;
    }

    public ParamsRequest<T> addPostParam(String key, String value) {
        mPostParams.put(key, value);

        return this;
    }

    public ParamsRequest<T> addQueryParam(BasicNameValuePair param) {
        addQueryParam(param.getName(), param.getValue());

        return this;
    }

    public ParamsRequest<T> addQueryParam(String key, String value) {
        mQueryParams.put(key, value);

        return this;
    }

    public ParamsRequest<T> addHeader(BasicNameValuePair param) {
        addHeader(param.getName(), param.getValue());

        return this;
    }

    public ParamsRequest<T> addHeader(String key, String value) {
        mHeaders.put(key, value);

        return this;
    }

    /**
     * 属性键的类.
     */
    public static final class PropertyKey {
        public static final String INTERFACE_VERSION = "iv";
        public static final String SYSTEM_VERSION = "sv";
        public static final String ANDROID_SYSTEM = "system";
        public static final String ANDROID_VERSION = "system-version";
        public static final String CHANNEL = "channel";
        public static final String APPID = "appId";
    }

    public static final class PropertyValue {

        public static final int INTERFACE_VERSION = 1;

        public static final String ANDROID_SYSTEM = "android";

    }

}
