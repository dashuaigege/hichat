package org.wongws.hichat.context;

import java.util.ArrayList;

import org.wongws.hichat.domain.ChatInfo;
import org.wongws.hichat.domain.SimpleUser;

public class SimpleUserContext {
	private SimpleUser user;

	private ChatInfo chat;

	public SimpleUser getUser() {
		return user;
	}

	public void setUser(SimpleUser user) {
		this.user = user;
		if (user != null) {
			ChatInfo chatInfo = new ChatInfo();
			chatInfo.setUserId(user.getId());
			chatInfo.setMessages(new ArrayList<>());
			setChat(chatInfo);
		}
	}

	public ChatInfo getChat() {
		return chat;
	}

	public void setChat(ChatInfo chat) {
		this.chat = chat;
	}
}
