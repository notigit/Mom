package com.xiaoaitouch.mom.net;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageRequest;
import com.xiaoaitouch.mom.util.Logger;

public class BitmapLruCache extends LruCache<String, Bitmap> implements
		ImageCache {
	public BitmapLruCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = get(url);
		if (bitmap == null && url.contains("file://")) {
			Logger.d(url);
			String pathName = url.substring(url.indexOf("file://") + 7);
			String pattern = "#W(\\d*)#H(\\d*)";
			int width = 0;
			int height = 0;
			try {
				Pattern p = Pattern.compile(pattern);
				Matcher matcher = p.matcher(url);
				if (matcher.find()) {
					width = Integer.parseInt(matcher.group(1));
					height = Integer.parseInt(matcher.group(2));
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			bitmap = doParse(width, height,
					URLDecoder.decode(URLDecoder.decode(pathName)));
			if (bitmap != null) {
				putBitmap(url, bitmap);
			}
		}

		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}

	private Bitmap doParse(int maxWith, int maxHeight, String pathName) {
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		if (maxWith == 0 && maxHeight == 0) {
			decodeOptions.inPreferredConfig = Config.RGB_565;
			bitmap = BitmapFactory.decodeFile(pathName, decodeOptions);
		} else {
			// If we have to resize this image, first get the natural bounds.
			decodeOptions.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(pathName, decodeOptions);
			int actualWidth = decodeOptions.outWidth;
			int actualHeight = decodeOptions.outHeight;

			// Then compute the dimensions we would ideally like to decode to.
			int desiredWidth = ImageRequest.getResizedDimension(maxWith,
					maxHeight, actualWidth, actualHeight);
			int desiredHeight = ImageRequest.getResizedDimension(maxHeight,
					maxWith, actualHeight, actualWidth);

			// Decode to the nearest power of two scaling factor.
			decodeOptions.inJustDecodeBounds = false;
			// TODO(ficus): Do we need this or is it okay since API 8 doesn't
			// support it?
			// decodeOptions.inPreferQualityOverSpeed =
			// PREFER_QUALITY_OVER_SPEED;
			decodeOptions.inSampleSize = ImageRequest.findBestSampleSize(
					actualWidth, actualHeight, desiredWidth, desiredHeight);
			Bitmap tempBitmap = bitmap = BitmapFactory.decodeFile(pathName,
					decodeOptions);

			// If necessary, scale down to the maximal acceptable size.
			if (tempBitmap != null
					&& (tempBitmap.getWidth() > desiredWidth || tempBitmap
							.getHeight() > desiredHeight)) {
				bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
						desiredHeight, true);
				tempBitmap.recycle();
			} else {
				bitmap = tempBitmap;
			}
		}

		return bitmap;
	}
}
