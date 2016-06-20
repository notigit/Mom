package com.xiaoaitouch.mom.bean;

import java.util.List;

public class NewCalendar {
	private int week;
	private String timeSlot;// 时间段
	private String currentDate;// 当前日期
	private String dueDate;// 怀孕日期
	private String[] weekEndDate;// 每周末尾日期
	private List<DayDate> mDayDates;// 每周对应的日期
	private String selectDate;//当前选择日期
	

	/**
	 * @return the selectDate
	 */
	public String getSelectDate() {
		return selectDate;
	}

	/**
	 * @param selectDate the selectDate to set
	 */
	public void setSelectDate(String selectDate) {
		this.selectDate = selectDate;
	}

	/**
	 * @return the mDayDates
	 */
	public List<DayDate> getmDayDates() {
		return mDayDates;
	}

	/**
	 * @param mDayDates
	 *            the mDayDates to set
	 */
	public void setmDayDates(List<DayDate> mDayDates) {
		this.mDayDates = mDayDates;
	}

	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the weekEndDate
	 */
	public String[] getWeekEndDate() {
		return weekEndDate;
	}

	/**
	 * @param weekEndDate
	 *            the weekEndDate to set
	 */
	public void setWeekEndDate(String[] weekEndDate) {
		this.weekEndDate = weekEndDate;
	}

	/**
	 * @return the week
	 */
	public int getWeek() {
		return week;
	}

	/**
	 * @param week
	 *            the week to set
	 */
	public void setWeek(int week) {
		this.week = week;
	}

	/**
	 * @return the timeSlot
	 */
	public String getTimeSlot() {
		return timeSlot;
	}

	/**
	 * @param timeSlot
	 *            the timeSlot to set
	 */
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	/**
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate
	 *            the currentDate to set
	 */
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

}
