package org.wongws.hichat.domain;

import java.util.List;

public class ChatInfo {
	private SimpleUser user;
	private List<SimpleUser> userList;
	private List<RecordInfo> sessionList;

	public SimpleUser getUser() {
		return user;
	}

	public void setUser(SimpleUser user) {
		this.user = user;
	}

	public List<SimpleUser> getUserList() {
		return userList;
	}

	public void setUserList(List<SimpleUser> userList) {
		this.userList = userList;
	}

	public List<RecordInfo> getSessionList() {
		return sessionList;
	}

	public void setSessionList(List<RecordInfo> sessionList) {
		this.sessionList = sessionList;
	}

}
