package org.wongws.hichat.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.wongws.hichat.context.SimpleUserContext;
import org.wongws.hichat.domain.ChatInfo;
import org.wongws.hichat.domain.Message;
import org.wongws.hichat.domain.SimpleUser;
import org.wongws.hichat.domain.WiselyMessage;
import org.wongws.hichat.domain.WiselyResponse;
import org.wongws.hichat.entity.HcUser;
import org.wongws.hichat.function.SaveMessages;
import org.wongws.hichat.helper.StringHelper;
import org.wongws.hichat.repository.HcMessageRepository;
import org.wongws.hichat.service.UserService;
import org.wongws.hichat.util.RedisUtil;
import org.wongws.hichat.util.Util;

import com.alibaba.fastjson.JSONObject;

@Controller
public class WsController {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private HcMessageRepository hcMessageRepository;

	@Autowired
	private Executor asyncServiceExecutor;

	@MessageMapping("/chat")
	public void handleChat(Principal principal, String msg, @Header("receiver") String userId) {
		HcUser user = userService.getUserByUsername(principal.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		if (Util.User_OnOff_Dic.containsKey(userId)) {
			boolean isOnline = Util.User_OnOff_Dic.get(userId).isOnline();
			String reciverName = Util.User_OnOff_Dic.get(userId).getName();
			// 开个线程 做数据保存
			SaveMessages saveMessages = new SaveMessages(user.getUser_hid(), userId, msg, hcMessageRepository);
			asyncServiceExecutor.execute(saveMessages);
			if (isOnline) {
				result.put("message", msg);
				result.put("sendId", user.getUser_hid());
				messagingTemplate.convertAndSendToUser(reciverName, "/queue/hichatPersonal", result);
			} else {
				String key = Util.HICHATLEAVEMESSAGES;
				String item = principal.getName() + "TO" + reciverName;
				ChatInfo chatInfo = null;
				if (redisUtil.hHasKey(key, item)) {
					chatInfo = JSONObject.parseObject(redisUtil.hget(key, item).toString(), ChatInfo.class);
				} else {
					chatInfo = new ChatInfo();
					chatInfo.setUserId(user.getUser_hid());
					chatInfo.setMessages(new ArrayList<>());
				}
				Message message = new Message();
				message.setDate(new Date());
				message.setSelf(false);
				message.setText(msg);
				chatInfo.getMessages().add(message);
				redisUtil.hset(key, item, chatInfo);
			}
		}
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

	@MessageMapping("/notifyNewUser")
	@SendTo("/topic/getNewUser")
	public SimpleUserContext notifyNewUser(SimpleUser simpleUser) throws InterruptedException {
		SimpleUserContext simpleUserContext = new SimpleUserContext();
		simpleUserContext.setUser(simpleUser);
		return simpleUserContext;
	}

	@MessageMapping("/notifyOn_Offline")
	@SendTo("/topic/getOn_Offline")
	public SimpleUserContext notificationOnline(SimpleUser user) throws InterruptedException {
		SimpleUserContext simpleUserContext = new SimpleUserContext();
		if (!StringHelper.isNullOrEmpty(user.getId())) {
			if (Util.User_OnOff_Dic.containsKey(user.getId())) {
				Util.User_OnOff_Dic.get(user.getId()).setOnline(user.isOnline());
				simpleUserContext.setUser(Util.User_OnOff_Dic.get(user.getId()));
			}
		}
		return simpleUserContext;
	}

}
