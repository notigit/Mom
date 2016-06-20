/*
 * Copyright (C) 2011 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.xiaoaitouch.mom.net;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.xiaoaitouch.mom.net.request.ParamsRequest;

/**
 * 短小文件请求
 * 
 * @author zhangcong
 * 
 */
public class ShortFileRequest extends ParamsRequest<File> {
    private static final int TIMEOUT_MS = 1000;
    private static final int MAX_RETRIES = 2;

    /** Default backoff multiplier for requests */
    private static final float BACKOFF_MULT = 2f;

    private final Response.Listener<File> mListener;

    /** Decoding lock so that we don't decode more than one image at a time (to avoid OOM's) */
    private static final Object sDecodeLock = new Object();
    private File mRootDir;

    /**
     * 小文件下载请求
     * 
     * @param url
     * @param root
     *            文件存放的目录
     * @param listener
     * @param errorListener
     */
    public ShortFileRequest(String url, File root, Response.Listener<File> listener,
            Response.ErrorListener errorListener) {
        this(url, Method.GET, root, listener, errorListener);
    }

    public ShortFileRequest(String url, int method, File root, Response.Listener<File> listener,
            Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, MAX_RETRIES, BACKOFF_MULT));
        mListener = listener;
        this.mRootDir = root;
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    @Override
    protected Response<File> parseNetworkResponse(NetworkResponse response) {
        // Serialize all decode on a global lock to reduce concurrent heap usage.
        synchronized (sDecodeLock) {
            try {
                return doParse(response);
            } catch (OutOfMemoryError e) {
                VolleyLog.e("Caught OOM for %d byte image, url=%s", response.data.length, getUrl());
                return Response.error(new ParseError(e));
            }
        }
    }

    /**
     * The real guts of parseNetworkResponse. Broken out for readability.
     */
    private Response<File> doParse(NetworkResponse response) {
        byte[] data = response.data;
        File file = null;
        if (data != null) {
            file = new File(mRootDir, getFilenameForKey(getUrl()));
            // simple verify the file is available.
            if (file != null && file.exists() && file.length() == data.length) {
                return Response.success(file, HttpHeaderParser.parseCacheHeaders(response, new DefaultExpire()));
            } else {
                try {
                    FileUtils.writeByteArrayToFile(file, data);
                    return Response.success(file, HttpHeaderParser.parseCacheHeaders(response, new DefaultExpire()));
                } catch (IOException e) {
                    e.printStackTrace();

                    return Response.error(new ParseError(e));
                }
            }
        }

        return Response.error(new ParseError(response));
    }

    private String getFilenameForKey(String key) {
        int firstHalfLength = key.length() / 2;
        String localFilename = String.valueOf(key.substring(0, firstHalfLength).hashCode());
        localFilename += String.valueOf(key.substring(firstHalfLength).hashCode());
        return localFilename;
    }

    @Override
    protected void deliverResponse(File response) {
        mListener.onResponse(response);
    }

}
