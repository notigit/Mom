package com.xiaoaitouch.mom.bean;

import java.util.Comparator;

public class CardComparator implements Comparator<MainData> {

	@Override
	public int compare(MainData lhs, MainData rhs) {
		if (lhs.getDescTime() < rhs.getDescTime()) {
			return -1;
		} else if (lhs.getDescTime() > rhs.getDescTime()) {
			return 1;
		} else {
			return 0;
		}
	}
}