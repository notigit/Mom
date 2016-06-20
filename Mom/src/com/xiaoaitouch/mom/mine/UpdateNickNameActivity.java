package com.xiaoaitouch.mom.mine;

import java.lang.reflect.Type;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.Response.Listener;
import com.google.gson.reflect.TypeToken;
import com.xiaoaitouch.event.EventBus;
import com.xiaoaitouch.event.bean.MineEvent;
import com.xiaoaitouch.mom.DatabaseMaster;
import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.bean.NickName;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.dao.DaoSession;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.dao.UserInfoDao;
import com.xiaoaitouch.mom.droid.BaseActivity;
import com.xiaoaitouch.mom.net.api.HttpApi;
import com.xiaoaitouch.mom.net.request.GsonTokenRequest;
import com.xiaoaitouch.mom.net.response.JsonResponse;
import com.xiaoaitouch.mom.util.ViewUtils;

/**
 * 修改昵称
 * 
 * @author huxin
 * 
 */
public class UpdateNickNameActivity extends BaseActivity {
	@Bind(R.id.user_input_nickname_et)
	EditText mInputNickName;

	private String mNickName = "";
	private MomApplication mApplication;
	private DaoSession mDbSession;
	private UserInfoDao mUserInfoDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (MomApplication) getApplication();
		mDbSession = DatabaseMaster.instance().getMainDbSession();
		mUserInfoDao = mDbSession.getUserInfoDao();
		setContentView(R.layout.update_nickname_activity);
		ButterKnife.bind(this);
		if (mApplication.getUserInfo() != null
				&& mApplication.getUserInfo().getNickName() != null) {
			mNickName = mApplication.getUserInfo().getNickName();
			mInputNickName.setText(mNickName);
		}
	}

	@OnClick(R.id.activity_top_back_image)
	public void onBack() {
		ViewUtils.hideSoftInput(mActivity, mInputNickName.getWindowToken());
		onBackBtnClick();
	}

	@OnClick(R.id.nickname_save_tv)
	public void submitData() {
		ViewUtils.hideSoftInput(mActivity, mInputNickName.getWindowToken());
		String nickName = mInputNickName.getText().toString().trim();
		if (TextUtils.isEmpty(nickName)) {
			showToast("请输入新的昵称");
		} else if (mNickName.equals(nickName)) {
			showToast("您输入的昵称与之前一致");
		} else {
			GsonTokenRequest<NickName> request = new GsonTokenRequest<NickName>(
					com.android.volley.Request.Method.POST, Configs.SERVER_URL
							+ "/user/neckname",
					new Listener<JsonResponse<NickName>>() {

						@Override
						public void onResponse(JsonResponse<NickName> response) {
							switch (response.state) {
							case Configs.UN_USE:
								showToast("版本过低请升级新版本");
								break;
							case Configs.FAIL:
								showToast(response.msg);
								break;
							case Configs.SUCCESS:
								updateUserData(response.data);
								break;
							}
						}
					}, null) {

				@Override
				public Type getType() {
					Type type = new TypeToken<JsonResponse<NickName>>() {
					}.getType();
					return type;
				}
			};
			HttpApi.getUpdateNickName(mActivity, "/user/neckname", request,
					nickName);
		}
	}

	private void updateUserData(NickName nickName) {
		UserInfo uInfo = new UserInfo();
		if (mApplication.getUserInfo() != null) {
			uInfo = mApplication.getUserInfo();
		}
		uInfo.setNickName(nickName.getNeckname());
		mUserInfoDao.deleteAll();
		mApplication.setUserInfo(uInfo);
		mUserInfoDao.insertOrReplace(uInfo);
		EventBus.getDefault().post(new MineEvent());
		onBackBtnClick();
	}
}
