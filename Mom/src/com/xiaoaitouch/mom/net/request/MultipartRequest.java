package com.xiaoaitouch.mom.net.request;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.google.gson.reflect.TypeToken;
import com.xiaoaitouch.mom.net.MultipartEntity;
import com.xiaoaitouch.mom.net.response.JsonResponse;

/**
 * @author huxin
 * 
 */
public class MultipartRequest extends GsonTokenRequest<String> {

    private MultipartEntity mHttpEntity;

    public MultipartRequest(String url, String filePath,
            Response.Listener<JsonResponse<String>> listener,
            Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);

        mHttpEntity = new MultipartEntity();
        File file = new File(filePath);
        try {
            mHttpEntity.addPart("file", file.getName(), new FileInputStream(file), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MultipartRequest(String url, File file,
            Response.Listener<JsonResponse<String>> listener,
            Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);

        mHttpEntity = new MultipartEntity();
        try {
            mHttpEntity.addPart("file", file.getName(), new FileInputStream(file), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        // 必需调用这个接口写入边界，坑～
        mHttpEntity.writeLastBoundaryIfNeeds();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    public Type getType() {
        Type type = new TypeToken<JsonResponse<String>>() {
        }.getType();

        return type;
    }

}
