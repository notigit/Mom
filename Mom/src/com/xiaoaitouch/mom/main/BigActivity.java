package com.xiaoaitouch.mom.main;

import android.os.Bundle;
import android.widget.GridView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.adapter.BigAdapter;
import com.xiaoaitouch.mom.droid.BaseActivity;

/**
 * 大事件
 * 
 * @author huxin
 * 
 */
public class BigActivity extends BaseActivity {
	@Bind(R.id.big_gridview)
	GridView mGridView;
	private BigAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.big_activity);
		ButterKnife.bind(this);
		initData();
	}

	private void initData() {
		mAdapter = new BigAdapter(mContext);
		mGridView.setAdapter(mAdapter);
	}

	@OnClick(R.id.activity_top_back_image)
	public void onBack() {
		onBackBtnClick();
	}

}
