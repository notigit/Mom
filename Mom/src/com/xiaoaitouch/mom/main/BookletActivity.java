package com.xiaoaitouch.mom.main;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.xiaoaitouch.mom.MomApplication;
import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.configs.Configs;
import com.xiaoaitouch.mom.dao.UserInfo;
import com.xiaoaitouch.mom.droid.BaseActivity;
import com.xiaoaitouch.mom.util.Utils;

/**
 * 生成册子
 * 
 * @author huxin
 * 
 */
public class BookletActivity extends BaseActivity {
	@Bind(R.id.booklet_webview)
	WebView messageWebview;
	@Bind(R.id.booklet_progressbar)
	ProgressBar progressBar;
	private MomApplication mApplication;
	private UserInfo mUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booklet_activity);
		ButterKnife.bind(this);
		mApplication = (MomApplication) getApplication();
		mUserInfo = mApplication.getUserInfo();
		initComponent();
	}

	@OnClick(R.id.activity_top_back_image)
	public void onBack() {
		onBackBtnClick();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initComponent() {
		messageWebview.setVisibility(View.VISIBLE); // 隐藏webview
		messageWebview.clearHistory();
		messageWebview.clearView();
		if (mUserInfo != null && !TextUtils.isEmpty(mUserInfo.getAccount())
				&& !TextUtils.isEmpty(mUserInfo.getPwd())) {
			messageWebview.loadUrl(Configs.SERVER_URL
					+ "/html/h5.html?uniqueness=" + Utils.createUniqueness()
					+ "&userName=" + mUserInfo.getAccount() + "&pwd="
					+ mUserInfo.getPwd());
		} else
			messageWebview.loadUrl(Configs.SERVER_URL
					+ "/html/h5.html?uniqueness=" + Utils.createUniqueness()
					+ "&userName=&pwd=");

		WebSettings webSettings = messageWebview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		messageWebview.requestFocus();
		messageWebview.setWebViewClient(new MyWebViewClient());
		messageWebview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				progressBar.setProgress(newProgress);
				progressBar.postInvalidate();
			}
		});

	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.GONE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setProgress(0);
		}

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view,
				final String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			progressBar.setVisibility(View.GONE);
			view.clearView();
		}
	}

}
