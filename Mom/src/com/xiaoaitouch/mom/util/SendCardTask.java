package com.xiaoaitouch.mom.util;

import java.io.File;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.reflect.TypeToken;
import com.xiaoaitouch.mom.bean.CardResult;
import com.xiaoaitouch.mom.bean.SendCardParams;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.net.SevenDoVolley;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.request.MultipartRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;

public class SendCardTask extends
		AsyncTask<Void, String, JsonResponse<CardResult>> {
	private SendCardParams cardParams;
	private UserInfo userInfo;
	private CardResultListener mListener;

	public interface CardResultListener {

		public void onCardResultSendSuccess(CardResult msg);

		public void onCardResultSendFailed(JsonResponse<CardResult> result);
	}

	public SendCardTask(SendCardParams params, UserInfo uInfo,
			CardResultListener listener) {
		this.cardParams = params;
		this.userInfo = uInfo;
		this.mListener = listener;
	}

	@Override
	protected JsonResponse<CardResult> doInBackground(Void... arg0) {
		return doRichMsgSend();
	}

	@Override
	protected void onPostExecute(JsonResponse<CardResult> result) {
		if (result.state == Configs.SUCCESS) {
			mListener.onCardResultSendSuccess(result.data);
		} else {
			mListener.onCardResultSendFailed(result);
		}
	}

	private String uploadFile() throws InterruptedException, ExecutionException {
		RequestFuture<JsonResponse<String>> voiceFuture = RequestFuture
				.newFuture();
		String resPath = "";
		if (fileExist(cardParams.getFilePath())) {
			MultipartRequest voiceRequest = new MultipartRequest(
					Configs.SERVER_URL + "/file/upload",
					cardParams.getFilePath(), voiceFuture, voiceFuture);
			SevenDoVolley.addRequest(voiceRequest);
			JsonResponse<String> res = null;
			res = voiceFuture.get();
			resPath = res.data;
		}
		return resPath;
	}

	private boolean fileExist(String path) {
		File f = new File(path);
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	private JsonResponse<CardResult> doRichMsgSend() {
		JsonResponse<CardResult> response = null;
		try {
			String filePath = uploadFile();
			RequestFuture<JsonResponse<CardResult>> future = RequestFuture
					.newFuture();
			GsonTokenRequest<CardResult> request = new GsonTokenRequest<CardResult>(
					Method.POST, Configs.SERVER_URL + "/user/mom/feeling",
					future, future) {
				@Override
				public Type getType() {
					Type type = new TypeToken<JsonResponse<CardResult>>() {
					}.getType();

					return type;
				}
			};
			if (userInfo != null && !TextUtils.isEmpty(userInfo.getAccount())
					&& !TextUtils.isEmpty(userInfo.getPwd())) {
				request.addPostParam("userName", userInfo.getAccount());
				request.addPostParam("pwd", userInfo.getPwd());
			} else {
				request.addPostParam("uniqueness", Utils.createUniqueness());
			}
			request.addPostParam("timestemp",
					String.valueOf(System.currentTimeMillis()));
			request.addPostParam("img", filePath);
			request.addPostParam(
					"message",
					cardParams.getMessage() == null ? "" : cardParams
							.getMessage());
			request.addPostParam("date", cardParams.getDate());
			request.addPostParam("lat", cardParams.getLat() + "");
			request.addPostParam("lng", cardParams.getLng() + "");
			request.addPostParam("feeling", cardParams.getFeeling() + "");
			request.addPostParam(
					"location",
					cardParams.getLocation() == null ? "" : cardParams
							.getLocation());
			request.addPostParam("type", cardParams.getType() + "");
			request.addPostParam("createTime", cardParams.getCreateTime() + "");

			SevenDoVolley.addRequest(request);
			response = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			Log.w(getClass().getName(), e.getMessage(), e.getCause());
		}

		return response;
	}
}
