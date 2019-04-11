package org.wongws.hichat;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.wongws.hichat.service.UserService;
import org.wongws.hichat.util.ChatEnum;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		UserService userService = (UserService) contextRefreshedEvent.getApplicationContext().getBean("userService");
		userService.loadUsersByRole(ChatEnum.EnuRoleType.ROLE_USER.getValue());
	}

}
