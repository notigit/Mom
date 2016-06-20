package com.xiaoaitouch.mom.bean;

import java.util.Comparator;

import com.xiaoaitouch.mom.dao.ShareCardModel;

public class ShareCardComparator implements Comparator<ShareCardModel> {

	@Override
	public int compare(ShareCardModel lhs, ShareCardModel rhs) {
		if (lhs.getCreateTime() > rhs.getCreateTime()) {
			return -1;
		} else if (lhs.getCreateTime() < rhs.getCreateTime()) {
			return 1;
		} else {
			return this.compareTime(lhs.getCreateTime(), rhs.getCreateTime());
		}
	}

	private int compareTime(long lt, long rt) {
		if (lt > rt) {
			return -1;
		} else if (lt < rt) {
			return 1;
		} else {
			return 0;
		}
	}
}