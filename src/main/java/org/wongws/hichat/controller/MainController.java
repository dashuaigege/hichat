package org.wongws.hichat.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wongws.hichat.context.UserContext;
import org.wongws.hichat.domain.SimpleUser;
import org.wongws.hichat.entity.HcUser;
import org.wongws.hichat.service.UserService;
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
			else
				otherUser.getValue().setOnline(true);
		}
		userContext.setUsers(users);
		return userContext;
	}

}
