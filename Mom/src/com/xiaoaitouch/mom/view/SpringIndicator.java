package com.xiaoaitouch.mom.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoaitouch.mom.R;

/**
 * 
 * @author huxin
 * 
 */
public class SpringIndicator extends FrameLayout {

	private LinearLayout tabContainer;
	private ViewPager viewPager;

	public SpringIndicator(Context context) {
		this(context, null);
	}

	public SpringIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public void setViewPager(final ViewPager viewPager) {
		this.viewPager = viewPager;
		initSpringView();
		setUpListener();
	}

	private void initSpringView() {
		addTabContainerView();
		addTabItems();
	}

	private void addTabContainerView() {
		tabContainer = new LinearLayout(getContext());
		tabContainer.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		tabContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabContainer.setGravity(Gravity.CENTER);
		addView(tabContainer);
	}

	private void addTabItems() {
		for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					32, 32);
			layoutParams.leftMargin = 6;
			layoutParams.rightMargin = 6;
			TextView textView = new TextView(getContext());
			if (viewPager.getAdapter().getPageTitle(i) != null) {
				textView.setText(viewPager.getAdapter().getPageTitle(i));
			}
			textView.setBackgroundResource(R.drawable.welcome_oval_bg);
			textView.setLayoutParams(layoutParams);
			tabContainer.addView(textView);
		}
	}

	private void setUpListener() {
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						super.onPageSelected(position);

					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
						ViewGroup.LayoutParams layoutParams = tabContainer
								.getChildAt(position).getLayoutParams();
						layoutParams.width = (int) (positionOffset * 32 + 32);
						tabContainer.getChildAt(position).setLayoutParams(
								layoutParams);
					}

					@Override
					public void onPageScrollStateChanged(int state) {
						super.onPageScrollStateChanged(state);

					}
				});
	}
}
