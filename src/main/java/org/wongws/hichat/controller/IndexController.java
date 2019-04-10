package org.wongws.hichat.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wongws.hichat.command.SigninCommand;
import org.wongws.hichat.context.SimpleUserContext;
import org.wongws.hichat.service.UserService;
import org.wongws.hichat.util.Util;

@RestController
public class IndexController {
	@Autowired
	private UserService userService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@RequestMapping("/doSignin")
	public String doSignin(HttpServletRequest request, SigninCommand signinCommand) {
		try {
			boolean hasMatch = userService.hasMatchUsername(signinCommand.getUsername());
			if (hasMatch)
				return "exist";
			String username = signinCommand.getUsername();
			String password = signinCommand.getPassword();
			String ip = Util.getIpAddr(request);
			SimpleUserContext simpleUserContext = userService.saveUser(username, password, ip);
			if (simpleUserContext != null) {
				messagingTemplate.convertAndSend("/topic/getNewUser", simpleUserContext);
				return "true";
			}
			return "false";
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
	}
}
