package org.wongws.hichat.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.wongws.hichat.domain.WiselyMessage;
import org.wongws.hichat.domain.WiselyResponse;
import org.wongws.hichat.util.Util;

@Controller
public class WsController {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chat")
	public void handleChat(Principal principal, String msg, @Header("receiver") String receiver) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sendId", Util.UserDic.get(principal.getName()));
		result.put("message", msg);
		messagingTemplate.convertAndSendToUser(receiver, "/queue/notifications", result);
	}

	// 当浏览器向服务器端发送请求时，通过@MessageMapping注解映射/welcome这个地址，类似于@requestMapping
	// 当服务端有消息时，会对订阅了@SendTo注解中的路径的浏览器发送消息
	@MessageMapping("/welcome")
	@SendTo("/topic/getResponse")
	public WiselyResponse say(WiselyMessage message) throws InterruptedException {
		// Thread.sleep(3000);
		WiselyResponse result = new WiselyResponse("Welcome, " + message.getName() + " !");
		// return JSON.toJSONString(result);
		return result;
	}

}
