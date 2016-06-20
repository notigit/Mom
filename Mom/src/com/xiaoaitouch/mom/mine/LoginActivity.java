package com.xiaoaitouch.mom.mine;

import java.lang.reflect.Type;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.xiaoaitouch.event.EventBus;
import com.xiaoaitouch.event.bean.MineEvent;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.MineInfo;
import com.xiaoaitouch.mom.bean.SocialLogin;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.configs.Constant;
import com.xiaoaitouch.mom.main.MainFragmentActivity;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.util.BlockDialog;
import com.xiaoaitouch.mom.util.Encode;
import com.xiaoaitouch.mom.util.UserDataUtils;
import com.xiaoaitouch.mom.util.Utils;
import com.xiaoaitouch.mom.util.ViewUtils;

/**
 * 登录
 * 
 * @author huxin
 * 
 */
public class LoginActivity extends Activity {
	@Bind(R.id.user_login_tv)
	TextView mLoginTv;
	@Bind(R.id.user_input_account_et)
	EditText mInputAccount;
	@Bind(R.id.user_input_pwd_et)
	EditText mInputPwd;

	private UMSocialService mController;
	private Context mContext;
	public static LoginActivity mLoginActivity;
	private BlockDialog mBlockDialog;
	private boolean mIsFlage = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		mContext = this;
		mLoginActivity = this;
		ButterKnife.bind(this);
		initData();
	}

	@OnClick(R.id.activity_top_back_image)
	public void onBack() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void initData() {
		mIsFlage = getIntent().getBooleanExtra("isflage", false);
		mBlockDialog = new BlockDialog(this);
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		UMWXHandler wxHandler = new UMWXHandler(this, Constant.WX_APP_ID,
				Constant.WX_APP_Secret);
		wxHandler.setRefreshTokenAvailable(false);
		wxHandler.addToSocialSDK();

		// QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
				Constant.QQ_APP_ID, Constant.QQ_APP_Secret);
		qqSsoHandler.addToSocialSDK();

	}

	@OnClick(R.id.user_login_tv)
	public void onClickLogin() {
		String account = mInputAccount.getText().toString().trim();
		String pwd = mInputPwd.getText().toString().trim();
		ViewUtils.hideSoftInput(this, mInputAccount.getWindowToken());
		if (TextUtils.isEmpty(account)) {
			showToast("请输入账号");
		} else if (TextUtils.isEmpty(pwd)) {
			showToast("请输入密码");
		} else {
			String[] str = { account,
					Encode.getMd5Value(Encode.getMd5Value(pwd)) };
			mBlockDialog.show();
			GsonTokenRequest<MineInfo> request = new GsonTokenRequest<MineInfo>(
					com.android.volley.Request.Method.POST, Configs.SERVER_URL
							+ "/user/getInfo",
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
										LoginActivity.this);
								uDataUtils.saveData(response.data);
								if (mIsFlage) {
									Intent mIntent = new Intent(mContext,
											MainFragmentActivity.class);
									startActivity(mIntent);
								} else {
									EventBus.getDefault().post(new MineEvent());
								}
								finish();
								overridePendingTransition(R.anim.slide_in_left,
										R.anim.slide_out_right);
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
			HttpApi.getUserLogin(this, "/user/getInfo", request, str);
		}
	}

	@OnClick(R.id.user_select_sina_login_iv)
	public void onClickSinaLogin() {
		mController.doOauthVerify(this, SHARE_MEDIA.SINA, mAuthListener);
	}

	@OnClick(R.id.user_select_weixin_login_iv)
	public void onClickWeiXinLogin() {
		mController.doOauthVerify(this, SHARE_MEDIA.WEIXIN, mAuthListener);
	}

	@OnClick(R.id.activity_top_right_tv)
	public void openActivity() {
		Intent mIntent = new Intent(this, ForgitPwdActivity.class);
		startActivity(mIntent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	private final UMAuthListener mAuthListener = new UMAuthListener() {
		@Override
		public void onStart(SHARE_MEDIA platform) {
			showToast("授权开始");
		}

		@Override
		public void onError(SocializeException e, SHARE_MEDIA platform) {
			showToast("授权错误," + e.getMessage());
		}

		@Override
		public void onComplete(Bundle value, SHARE_MEDIA platform) {
			String uid = value.getString("uid");

			if (!TextUtils.isEmpty(uid)) {
				umComplete(value, platform);
			} else {
				showToast("授权失败...");
			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
		}
	};

	private void umComplete(Bundle value, final SHARE_MEDIA platform) {
		mController.getPlatformInfo(mContext, platform, new UMDataListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				if (status == 200 && info != null) {
					if (platform == SHARE_MEDIA.WEIXIN) {
						String headPic = info.get("headimgurl").toString();
						String socailUnid = info.get("openid").toString();
						String userName = info.get("nickname").toString();
						socialLogin(socailUnid, Constant.SOCIAL_SOURCE_WEIXIN,
								null, userName, headPic);
					} else if (platform == SHARE_MEDIA.SINA) {
						String headPic = info.get("profile_image_url")
								.toString();
						String socailUnid = info.get("uid").toString();
						String userName = info.get("screen_name").toString();
						socialLogin(socailUnid, Constant.SOCIAL_SOURCE_SINA,
								null, userName, headPic);
					}
				} else {
					showToast("获得用户数据失败，code=" + status);
				}
			}
		});
	}

	private void socialLogin(final String socailUnid, final int source,
			final String userLoginName, final String userName,
			final String headPic) {

		SocialLoginRequest request = new SocialLoginRequest(mResponseListener,
				mErrorListener);
		SocialLogin sLogin = new SocialLogin(socailUnid, userLoginName,
				userName, headPic, source);
		HttpApi.getUserSocialLogin(this, "/user/social", request, sLogin);
	}

	private final Listener<JsonResponse<MineInfo>> mResponseListener = new Listener<JsonResponse<MineInfo>>() {

		@Override
		public void onResponse(JsonResponse<MineInfo> response) {
			if (response != null && response.data != null) {
				UserDataUtils uDataUtils = new UserDataUtils(LoginActivity.this);
				uDataUtils.saveData(response.data);
				finish();
			} else {
				showToast(response == null ? "登陆失败" : response.msg);
			}
		}
	};

	private final ErrorListener mErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			showToast("登陆失败");
		}
	};

	public static final class PhoneLoginRequest extends
			GsonTokenRequest<MineInfo> {

		public PhoneLoginRequest(String account, String password,
				Listener<JsonResponse<MineInfo>> listener, ErrorListener error) {
			super(Method.POST, "", listener, error);
			// String uniqueness = Util.createUniqueness();
			//
			// addPostParam(new BasicNameValuePair("uniqueness", uniqueness));
			// addPostParam(new BasicNameValuePair("tel", account));
			// addPostParam(new BasicNameValuePair("password", password));
			// addQueryParam("timestamp",
			// String.valueOf(System.currentTimeMillis()));
		}

		@Override
		public Type getType() {
			Type type = new TypeToken<JsonResponse<MineInfo>>() {
			}.getType();
			return type;
		}
	}

	public static final class SocialLoginRequest extends
			GsonTokenRequest<MineInfo> {

		public SocialLoginRequest(Listener<JsonResponse<MineInfo>> listener,
				ErrorListener error) {
			super(Method.POST, Configs.SERVER_URL + "/user/social", listener,
					error);
		}

		@Override
		public Type getType() {
			Type type = new TypeToken<JsonResponse<MineInfo>>() {
			}.getType();

			return type;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		UMSocialService controller = UMServiceFactory
				.getUMSocialService("com.umeng.login");
		UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(
				requestCode);

		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, intent);
		}
	}

	private void showToast(CharSequence text) {
		Utils.showToast(text, Toast.LENGTH_SHORT);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
}
