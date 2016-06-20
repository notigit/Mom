package com.xiaoaitouch.mom.bean;

import java.util.List;

public class CalendarBean {

	private List<Calendars> calendar;

	/**
	 * @return the calendar
	 */
	public List<Calendars> getCalendar() {
		return calendar;
	}

	/**
	 * @param calendar
	 *            the calendar to set
	 */
	public void setCalendar(List<Calendars> calendar) {
		this.calendar = calendar;
	}

	public class Calendars {
		private String date;
		private long id;
		private long userId;

		/**
		 * @return the date
		 */
		public String getDate() {
			return date;
		}

		/**
		 * @param date
		 *            the date to set
		 */
		public void setDate(String date) {
			this.date = date;
		}

		/**
		 * @return the id
		 */
		public long getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(long id) {
			this.id = id;
		}

		/**
		 * @return the userId
		 */
		public long getUserId() {
			return userId;
		}

		/**
		 * @param userId
		 *            the userId to set
		 */
		public void setUserId(long userId) {
			this.userId = userId;
		}

	}

}
