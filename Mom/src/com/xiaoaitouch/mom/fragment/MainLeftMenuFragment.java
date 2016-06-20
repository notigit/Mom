package com.xiaoaitouch.mom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.droid.BaseFragment;
import com.xiaoaitouch.mom.main.BookletActivity;
import com.xiaoaitouch.mom.mine.SettingActivity;

public class MainLeftMenuFragment extends BaseFragment {
	@Bind(R.id.main_left_ad_image)
	ImageView mMainLeftAdImage;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_left_menu_fragment,
				container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
	}

	private void initView() {
	}

	@OnClick(R.id.main_left_createbook_lay)
	public void openBookletActivity() {
		startIntent(BookletActivity.class);
	}

	@OnClick(R.id.main_left_setting_lay)
	public void openSettingActivity() {
		startIntent(SettingActivity.class);
	}

}
