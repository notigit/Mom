package com.xiaoaitouch.mom.net;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.xiaoaitouch.mom.droid.BaseApplication;

public class ShortFileLoader {
	private final RequestQueue mRequestQueue;

	private int mBatchResponseDelayMs = 100;

	private final HashMap<String, BatchedFileRequest> mInFlightRequests = new HashMap<String, BatchedFileRequest>();

	private final HashMap<String, BatchedFileRequest> mBatchedResponses = new HashMap<String, BatchedFileRequest>();

	private final Handler mHandler = new Handler(Looper.getMainLooper());

	private Runnable mRunnable;

	public ShortFileLoader(RequestQueue queue) {
		mRequestQueue = queue;
	}

	public interface FileListener extends ErrorListener {
		public void onResponse(FileContainer response, boolean isImmediate);
	}

	public FileContainer get(String requestUrl, FileListener imageListener) {
		// only fulfill requests that were initiated from the main thread.
		throwIfNotOnMainThread();

		String cacheKey = "";
		cacheKey = getCacheKey(requestUrl);

		// The bitmap did not exist in the cache, fetch it!
		FileContainer imageContainer = new FileContainer(null, requestUrl,
				cacheKey, imageListener);

		// Check to see if a request is already in-flight.
		BatchedFileRequest request = mInFlightRequests.get(cacheKey);
		if (request != null) {
			// If it is, add this request to the list of listeners.
			request.addContainer(imageContainer);
			return imageContainer;
		}

		// The request is not already in flight. Send the new request to the
		// network and
		// track it.
		Request<File> newRequest = makeFileRequest(requestUrl, cacheKey);

		mRequestQueue.add(newRequest);
		mInFlightRequests.put(cacheKey, new BatchedFileRequest(newRequest,
				imageContainer));
		return imageContainer;
	}

	protected Request<File> makeFileRequest(String requestUrl,
			final String cacheKey) {
		return new ShortFileRequest(requestUrl, new File(
				BaseApplication.sContext.getCacheDir(), "short_file"),
				new Listener<File>() {
					@Override
					public void onResponse(File response) {
						onGetFileSuccess(cacheKey, response);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						onGetFileFailed(cacheKey, error);
					}
				});
	}

	public void setBatchedResponseDelay(int newBatchedResponseDelayMs) {
		mBatchResponseDelayMs = newBatchedResponseDelayMs;
	}

	protected void onGetFileSuccess(String cacheKey, File response) {
		// remove the request from the list of in-flight requests.
		BatchedFileRequest request = mInFlightRequests.remove(cacheKey);

		if (request != null) {
			// Update the response bitmap.
			request.mResponseBitmap = response;

			// Send the batched response
			batchResponse(cacheKey, request);
		}
	}

	protected void onGetFileFailed(String cacheKey, VolleyError error) {
		// Notify the requesters that something failed via a null result.
		// Remove this request from the list of in-flight requests.
		BatchedFileRequest request = mInFlightRequests.remove(cacheKey);

		if (request != null) {
			// Set the error for this request
			request.setError(error);

			// Send the batched response
			batchResponse(cacheKey, request);
		}
	}

	public class FileContainer {
		private File mFile;

		private final FileListener mListener;

		private final String mCacheKey;

		private final String mRequestUrl;

		public FileContainer(File file, String requestUrl, String cacheKey,
				FileListener listener) {
			mFile = file;
			mRequestUrl = requestUrl;
			mCacheKey = cacheKey;
			mListener = listener;
		}

		public void cancelRequest() {
			if (mListener == null) {
				return;
			}

			BatchedFileRequest request = mInFlightRequests.get(mCacheKey);
			if (request != null) {
				boolean canceled = request
						.removeContainerAndCancelIfNecessary(this);
				if (canceled) {
					mInFlightRequests.remove(mCacheKey);
				}
			} else {
				// check to see if it is already batched for delivery.
				request = mBatchedResponses.get(mCacheKey);
				if (request != null) {
					request.removeContainerAndCancelIfNecessary(this);
					if (request.mContainers.size() == 0) {
						mBatchedResponses.remove(mCacheKey);
					}
				}
			}
		}

		public File getFile() {
			return mFile;
		}

		public String getRequestUrl() {
			return mRequestUrl;
		}
	}

	private class BatchedFileRequest {
		private final Request<?> mRequest;

		private File mResponseBitmap;

		private VolleyError mError;

		private final LinkedList<FileContainer> mContainers = new LinkedList<FileContainer>();

		public BatchedFileRequest(Request<?> request, FileContainer container) {
			mRequest = request;
			mContainers.add(container);
		}

		public void setError(VolleyError error) {
			mError = error;
		}

		public VolleyError getError() {
			return mError;
		}

		public void addContainer(FileContainer container) {
			mContainers.add(container);
		}

		public boolean removeContainerAndCancelIfNecessary(
				FileContainer container) {
			mContainers.remove(container);
			if (mContainers.size() == 0) {
				mRequest.cancel();
				return true;
			}
			return false;
		}
	}

	private void batchResponse(String cacheKey, BatchedFileRequest request) {
		mBatchedResponses.put(cacheKey, request);
		// If we don't already have a batch delivery runnable in flight, make a
		// new one.
		// Note that this will be used to deliver responses to all callers in
		// mBatchedResponses.
		if (mRunnable == null) {
			mRunnable = new Runnable() {
				@Override
				public void run() {
					for (BatchedFileRequest bir : mBatchedResponses.values()) {
						for (FileContainer container : bir.mContainers) {
							// If one of the callers in the batched request
							// canceled the request
							// after the response was received but before it was
							// delivered,
							// skip them.
							if (container.mListener == null) {
								continue;
							}
							if (bir.getError() == null) {
								container.mFile = bir.mResponseBitmap;
								container.mListener
										.onResponse(container, false);
							} else {
								container.mListener.onErrorResponse(bir
										.getError());
							}
						}
					}
					mBatchedResponses.clear();
					mRunnable = null;
				}

			};
			// Post the runnable.
			mHandler.postDelayed(mRunnable, mBatchResponseDelayMs);
		}
	}

	private void throwIfNotOnMainThread() {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new IllegalStateException(
					"ImageLoader must be invoked from the main thread.");
		}
	}

	private static String getCacheKey(String url) {
		return url;
	}
}
