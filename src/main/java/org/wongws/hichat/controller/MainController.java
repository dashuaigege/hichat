package org.wongws.hichat.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wongws.hichat.context.UserContext;
import org.wongws.hichat.domain.ChatInfo;
import org.wongws.hichat.domain.Message;
import org.wongws.hichat.domain.RecordInfo;
import org.wongws.hichat.entity.HcUser;
import org.wongws.hichat.domain.SimpleUser;
import org.wongws.hichat.util.Util;

@RestController
public class MainController {
	@RequestMapping("/userContext")
	public UserContext getUserContext(Principal principal) {
		UserContext userContext = new UserContext();
		HcUser user = (HcUser) principal;

		String hid = user.getUser_hid();
		String name = user.getUsername();
		Long img = user.getId();
		SimpleUser simpleUser = new SimpleUser(hid, name, img, true);

		userContext.setUser(simpleUser);
		//
		// OnlineUser userinfo = new UserInfo();
		// userinfo.setName(name);
		// userinfo.setId(userid);
		// userinfo.setImg(Util.IMGURL + userid + ".jpg");
		//
		// chat.setUser(userinfo);
		// chat.setSessionList(new ArrayList<RecordInfo>());
		// chat.setUserList(new ArrayList<UserInfo>());
		//
		// for (Map.Entry<String, Integer> item : Util.UserDic.entrySet()) {
		// if (item.getKey().equals(name))
		// continue;
		// UserInfo tempUser = new UserInfo();
		// tempUser.setName(item.getKey());
		// tempUser.setId(item.getValue());
		// tempUser.setImg(Util.IMGURL + item.getValue() + ".jpg");
		// chat.getUserList().add(tempUser);
		//
		// RecordInfo record=new RecordInfo();
		// record.setUserName(item.getKey());
		// record.setUserId(item.getValue());
		// record.setMessages(new ArrayList<Message>());
		// chat.getSessionList().add(record);
		// }
		return userContext;
	}
}
