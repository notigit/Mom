package com.xiaoaitouch.mom.mine;

import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.xiaoaitouch.event.EventBus;
import com.xiaoaitouch.event.bean.MineEvent;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.MineInfo;
import com.xiaoaitouch.mom.bean.ResultObj;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.droid.BaseActivity;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.request.StringRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.util.Encode;
import com.xiaoaitouch.mom.util.UserDataUtils;
import com.xiaoaitouch.mom.util.ViewUtils;

/**
 * 注册
 * 
 * @author huxin
 * 
 */
public class RegisterActivity extends BaseActivity {
	@Bind(R.id.user_input_content_et)
	EditText mInputTell;
	@Bind(R.id.user_input_captchas_et)
	EditText mInputCaptchas;
	@Bind(R.id.user_input_pwd_et)
	EditText mInputPwd;
	@Bind(R.id.user_get_captchas_tv)
	TextView mCaptchasTv;

	private Timer mTimer = new Timer();
	private int mDelayTime = 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.activity_top_back_image)
	public void onBack() {
		onBackBtnClick();
	}

	// 发送验证码
	@OnClick(R.id.user_get_captchas_tv)
	public void sendCode() {
		String mTell = mInputTell.getText().toString().trim();
		ViewUtils.hideSoftInput(mActivity, mInputTell.getWindowToken());
		if (!TextUtils.isEmpty(mTell)) {
			mBlockDialog.show();
			StringRequest request = new StringRequest(Method.POST,
					Configs.SERVER_URL + "/send/code", new Listener<String>() {

						@Override
						public void onResponse(String response) {
							mBlockDialog.dismiss();
							try {
								ResultObj result = new ResultObj(response);
								switch (result.getState()) {
								case ResultObj.FAIL:
									showToast(result.getMessage());
									break;
								case ResultObj.SUCCESS:
									mTimer.schedule(task, 1000, 1000);
									break;
								case ResultObj.UN_USE:
									showToast("版本过低请升级新版本");
									break;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							mBlockDialog.dismiss();
						}
					});
			HttpApi.sendCode(mActivity, "/send/code", request, mTell);
		} else {
			showToast("请输入手机号码");
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mCaptchasTv.setTextColor(getResources().getColor(
						R.color.item_gray));
				mCaptchasTv.setText(mDelayTime + "秒");
				mCaptchasTv.setClickable(false);
				if (mDelayTime < 0) {
					mTimer.cancel();
					mCaptchasTv.setTextColor(getResources().getColor(
							R.color.activity_top_color));
					mCaptchasTv.setText(getResources().getString(
							R.string.mine_register_captchas_tv));
					mCaptchasTv.setClickable(true);
				}
				break;

			default:
				break;
			}
		}
	};

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			mDelayTime--;
			Message message = new Message();
			message.what = 1;
			mHandler.sendMessage(message);
		}
	};

	@OnClick(R.id.user_register_tv)
	public void submitRegister() {
		String tell = mInputTell.getText().toString().trim();
		String captchas = mInputCaptchas.getText().toString().trim();
		String pwd = mInputPwd.getText().toString().trim();
		if (TextUtils.isEmpty(tell)) {
			showToast("请输入手机号码");
		} else if (TextUtils.isEmpty(captchas)) {
			showToast("请输入验证码");
		} else if (TextUtils.isEmpty(pwd)) {
			showToast("请输入密码");
		} else {
			String[] str = { tell, Encode.getMd5Value(Encode.getMd5Value(pwd)),
					captchas };
			mBlockDialog.show();
			GsonTokenRequest<MineInfo> request = new GsonTokenRequest<MineInfo>(
					com.android.volley.Request.Method.POST, Configs.SERVER_URL
							+ "/user/register",
					new Listener<JsonResponse<MineInfo>>() {

						@Override
						public void onResponse(JsonResponse<MineInfo> response) {
							mBlockDialog.dismiss();
							switch (response.state) {
							case Configs.UN_USE:
								showToast("版本过低请升级新版本");
								break;
							case Configs.FAIL:
								showToast(response.msg);
								break;
							case Configs.SUCCESS:
								UserDataUtils uDataUtils = new UserDataUtils(
										mActivity);
								uDataUtils.saveData(response.data);
								EventBus.getDefault().post(new MineEvent());
								showToast("注册成功");
								onBackBtnClick();
								break;
							}
						}

					}, null) {

				@Override
				public Type getType() {
					Type type = new TypeToken<JsonResponse<MineInfo>>() {
					}.getType();

					return type;
				}
			};
			HttpApi.getRegister(mActivity, "/user/register", request, str);
		}
	}
}
