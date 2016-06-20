package com.xiaoaitouch.mom.bean;

/**
 * 第三方登录-传参实体类
 * 
 * @author huxin
 * 
 */
public class SocialLogin {
	private String socailUnid;
	private String userLoginName;
	private String userName;
	private String head_pic;
	private int source;

	public SocialLogin(String socailUnid, String userLoginName,
			String userName, String head_pic, int source) {
		this.socailUnid = socailUnid;
		this.userLoginName = userLoginName;
		this.userName = userName;
		this.head_pic = head_pic;
		this.source = source;
	}

	/**
	 * @return the socailUnid
	 */
	public String getSocailUnid() {
		return socailUnid;
	}

	/**
	 * @param socailUnid
	 *            the socailUnid to set
	 */
	public void setSocailUnid(String socailUnid) {
		this.socailUnid = socailUnid;
	}

	/**
	 * @return the userLoginName
	 */
	public String getUserLoginName() {
		return userLoginName;
	}

	/**
	 * @param userLoginName
	 *            the userLoginName to set
	 */
	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the head_pic
	 */
	public String getHead_pic() {
		return head_pic;
	}

	/**
	 * @param head_pic
	 *            the head_pic to set
	 */
	public void setHead_pic(String head_pic) {
		this.head_pic = head_pic;
	}

	/**
	 * @return the source
	 */
	public int getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(int source) {
		this.source = source;
	}

}
