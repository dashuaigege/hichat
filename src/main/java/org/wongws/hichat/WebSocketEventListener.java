package org.wongws.hichat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import org.wongws.hichat.context.SimpleUserContext;
import org.wongws.hichat.domain.SimpleUser;
import org.wongws.hichat.util.Util;

@Component
public class WebSocketEventListener {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程单例的。
	private static AtomicLong connectionIds = new AtomicLong(0);
	// concurrent包的线程安全HashMap，用来存放每个客户端对应的webSocket对象。
	private static ConcurrentHashMap<Object, Integer> concurrentHashMap = new ConcurrentHashMap<>();

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@EventListener
	public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getUser() != null) {
			logger.info("Received a new web socket connectionc - {}", headerAccessor);
			String userId = headerAccessor.getUser().getName();
			// 保存用户访问数量
			if (concurrentHashMap.containsKey(userId)) {
				concurrentHashMap.put(userId, concurrentHashMap.get(userId) + 1);
			} else {
				concurrentHashMap.put(userId, 1);
			}
			int count = concurrentHashMap.size();
			logger.info("user_id为：{}的用户链接访问成功，用户访问数量为：{},当前链接数为：{}", userId, count, connectionIds.incrementAndGet());
		}
	}

	@EventListener
	public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getUser() != null) {
			logger.info("A web socket client Subscribed a topic - {}", headerAccessor);
			logger.info("user_id为：{}的用户链接已订阅成功,等待消息通知", headerAccessor.getUser().getName());
		}
	}

	@EventListener
	public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getUser() != null) {
			logger.info("A web socket client unsubscribed a topic - {}", headerAccessor);
			logger.info("user_id为：{}的用户链接已取消订阅成功", headerAccessor.getUser().getName());
		}
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		if (headerAccessor.getUser() != null) {

			String userId = headerAccessor.getUser().getName();
			// 统计每个用户人数，为0移除
			if (null == concurrentHashMap.get(userId)) {
				logger.warn("计数过程出错");
				return;
			}
			if (concurrentHashMap.get(userId) - 1 == 0) {
				concurrentHashMap.remove(userId);
			} else {
				concurrentHashMap.put(userId, concurrentHashMap.get(userId) - 1);
			}

			int count = concurrentHashMap.size();
			// 在线连接数减1
			logger.info("user_id为：{}的用户链接断开，用户访问数量为：{},当前链接数为：{}", headerAccessor.getUser().getName(), count,
					connectionIds.decrementAndGet());

			for (Map.Entry<String, SimpleUser> item : Util.User_OnOff_Dic.entrySet()) {
				if (item.getValue().getName().equals(userId)) {
					item.getValue().setOnline(false);
					SimpleUserContext simpleUserContext = new SimpleUserContext();
					simpleUserContext.setUser(item.getValue());
					messagingTemplate.convertAndSend("/topic/getOn_Offline", simpleUserContext);
					break;
				}
			}
		}

	}

}
