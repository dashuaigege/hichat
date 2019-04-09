package org.wongws.hichat.context;

import java.util.ArrayList;
import java.util.List;

import org.wongws.hichat.domain.ChatInfo;
import org.wongws.hichat.domain.SimpleUser;

public class UserContext {
	private SimpleUser user;

	public SimpleUser getUser() {
		return user;
	}

	public void setUser(SimpleUser user) {
		this.user = user;
	}

	private List<SimpleUser> users;

	public List<SimpleUser> getUsers() {
		return users;
	}

	public void setUsers(List<SimpleUser> users) {
		this.users = users;
		if (users != null && users.size() > 0) {
			chats = new ArrayList<>();
			for (SimpleUser user : users) {
				ChatInfo chatInfo=new ChatInfo();
				chatInfo.setUserId(user.getId());
				chatInfo.setMessages(new ArrayList<>());
				chats.add(chatInfo);
			}
			setChats(chats);
		}
	}

	private List<ChatInfo> chats;

	public List<ChatInfo> getChats() {
		return chats;
	}

	public void setChats(List<ChatInfo> chats) {
		this.chats = chats;
	}
}
