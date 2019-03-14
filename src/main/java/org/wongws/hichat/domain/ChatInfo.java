package org.wongws.hichat.domain;

import java.util.List;

public class ChatInfo {
	private UserInfo user;
	private List<UserInfo> userList;
	private List<RecordInfo> sessionList;

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public List<UserInfo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserInfo> userList) {
		this.userList = userList;
	}

	public List<RecordInfo> getSessionList() {
		return sessionList;
	}

	public void setSessionList(List<RecordInfo> sessionList) {
		this.sessionList = sessionList;
	}

}
