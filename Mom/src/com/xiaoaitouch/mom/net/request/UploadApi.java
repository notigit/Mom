package com.xiaoaitouch.mom.net.request;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.xiaoaitouch.mom.bean.FormImage;
import com.xiaoaitouch.mom.net.SevenDoVolley;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传文件
 * 
 * @author huxin
 * 
 */
public class UploadApi {

	/**
	 * 上传头像接口
	 * 
	 * @param url
	 * @param path
	 * @param bitmap
	 * @param listener
	 */
	public static void uploadImg(String url, String path, Bitmap bitmap,
			ResponseListener listener) {
		List<FormImage> imageList = new ArrayList<FormImage>();
		FormImage formImage = new FormImage(bitmap);
		formImage.setFileName(path);
		imageList.add(formImage);
		Request request = new PostUploadRequest(url, imageList, listener);
		SevenDoVolley.getRequestQueue().add(request);
	}

	/**
	 * 发送卡片
	 * 
	 * @param url
	 * @param path
	 * @param bitmap
	 * @param listener
	 */
	public static void sendCard(String url, String path, Bitmap bitmap,
			ResponseListener listener) {
		List<FormImage> imageList = new ArrayList<FormImage>();
		FormImage formImage = new FormImage(bitmap);
		formImage.setFileName(path);
		imageList.add(formImage);
		Request request = new PostUploadRequest(url, imageList, listener);
		SevenDoVolley.getRequestQueue().add(request);
	}
}
