package com.xiaoaitouch.mom.bean;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * 上传Bitmap实体类
 */
public class FormImage {

	private String mFileName;

	private Bitmap mBitmap;

	public FormImage(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}

	public String getName() {
		return "file";
	}

	public String getFileName() {
		return mFileName;
	}

	public void setFileName(String path) {
		this.mFileName = path;
	}

	public byte[] getValue() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		return bos.toByteArray();
	}

	public String getMime() {
		return "image/png/jpg";
	}
}
