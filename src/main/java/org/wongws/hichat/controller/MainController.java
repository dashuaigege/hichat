package org.wongws.hichat.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wongws.hichat.context.UserContext;
import org.wongws.hichat.domain.SimpleUser;
import org.wongws.hichat.entity.HcUser;
import org.wongws.hichat.service.UserService;
import org.wongws.hichat.util.ImageProducerUtil;
import org.wongws.hichat.util.Util;

@RestController
public class MainController {
	@Autowired
	private UserService userService;

	@RequestMapping("/userContext")
	public UserContext getUserContext(Principal principal) {
		UserContext userContext = new UserContext();
		HcUser user = userService.getUserByUsername(principal.getName());

		String hid = user.getUser_hid();
		String name = user.getUsername();
		long id = user.getId();
		SimpleUser simpleUser = new SimpleUser(hid, name, id, true);

		userContext.setUser(simpleUser);
		List<SimpleUser> users = new ArrayList<>();
		for (Map.Entry<String, SimpleUser> otherUser : Util.User_OnOff_Dic.entrySet()) {
			if (!otherUser.getKey().equals(hid))
				users.add(otherUser.getValue());
		}
		userContext.setUsers(users);
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
