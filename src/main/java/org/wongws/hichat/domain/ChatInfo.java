package org.wongws.hichat.domain;

import java.util.List;

public class ChatInfo {
	private String userId;
	private List<Message> messages;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
