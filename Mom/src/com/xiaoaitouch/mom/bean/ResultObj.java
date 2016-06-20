package com.xiaoaitouch.mom.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ResultObj {

	/**
	 * 成功.
	 */
	public static final int SUCCESS = 1;

	/**
	 * 失败.
	 */
	public static final int FAIL = -1;

	/**
	 * 版本不可用
	 */
	public static final int UN_USE = -2;

	private JSONObject json;

	public ResultObj(JSONObject json) {
		this.json = json;
	}

	public ResultObj(String s) throws JSONException {
		this.json = new JSONObject(s);
	}

	public int getState() throws JSONException {
		return this.json.getInt("state");
	}

	public String getMessage() throws JSONException {
		return this.json.getString("msg");
	}

	public JSONArray getArrayData() throws JSONException {
		return this.json.getJSONArray("data");
	}

	public JSONObject getObjectData() throws JSONException {
		return this.json.getJSONObject("data");
	}

	public String getStringData() throws JSONException {
		return this.json.getString("data");
	}

	public long getLongData() throws JSONException {
		return this.json.getLong("data");
	}

	public int getIntData() throws JSONException {
		return this.json.getInt("data");
	}

}
