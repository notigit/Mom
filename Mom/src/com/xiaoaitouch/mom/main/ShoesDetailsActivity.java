package com.xiaoaitouch.mom.main;

import com.xiaoaitouch.mom.R;
import com.xiaoaitouch.mom.droid.BaseActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 小爱智能孕妇鞋详细
 * 
 * @author Administrator
 * 
 */
public class ShoesDetailsActivity extends BaseActivity {
	@Bind(R.id.shoes_manage_btnBack)
	ImageView btnBack;
	@Bind(R.id.shoes_details_btnDetails)
	Button btnDetails;
	@Bind(R.id.shoes_details_btnBound)
	Button btnBound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoes_details_layout);
		ButterKnife.bind(this);
		
		getWidget();
	}

	public void getWidget() {
	

	}

	@OnClick(R.id.shoes_manage_btnBack)
	public void onBack() {
		onBackBtnClick();
	}
	
	
	@OnClick(R.id.shoes_details_btnBound)
	public void onBound() {
		setResult(RESULT_OK);
		onBackBtnClick();
	}

}
