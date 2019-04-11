package org.wongws.hichat.context;

import java.util.ArrayList;
import java.util.List;

import org.wongws.hichat.domain.ChatInfo;
import org.wongws.hichat.domain.SimpleUser;
import org.wongws.hichat.util.RedisUtil;
import org.wongws.hichat.util.SpringUtil;
import org.wongws.hichat.util.Util;

import com.alibaba.fastjson.JSONObject;

public class UserContext {

	private RedisUtil getRedisUtil() {
		return SpringUtil.getBean(RedisUtil.class);
	}

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
				// 此处需要读取未读消息，从Redis中获取
				// item：sendNameToReciverName value：ChatInfo
				String key = Util.HICHATLEAVEMESSAGES;
				String item = user.getName() + "TO" + this.user.getName();
				ChatInfo chatInfo = null;
				if (getRedisUtil().hHasKey(key, item)) {
					chatInfo = JSONObject.parseObject(getRedisUtil().hget(key, item).toString(), ChatInfo.class);
					getRedisUtil().hdel(key, item);
				} else {
					chatInfo = new ChatInfo();
					chatInfo.setUserId(user.getId());
					chatInfo.setMessages(new ArrayList<>());
				}
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
