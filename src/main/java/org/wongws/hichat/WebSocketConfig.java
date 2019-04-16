package org.wongws.hichat;

import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
/*
 * 通过该注解开启使用STOMP协议来传输基于代理(message broker)的消息,
 * 这时控制器支持使用@MessageMapping，就像使用@RequestMapping一样
 */
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 注册一个STOMP endpoint,并指定使用SockJS协议。
		registry.addEndpoint("/endpointBroadcast").withSockJS()
				.setClientLibraryUrl("https://cdn.bootcss.com/sockjs-client/1.3.0/sockjs.min.js");
		;
		registry.addEndpoint("/endpointPersonal").withSockJS()
				.setClientLibraryUrl("https://cdn.bootcss.com/sockjs-client/1.3.0/sockjs.min.js");
		;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 广播室配置一个/topic消息代理
		// 点对点式配置一个/queue消息代理
		registry.enableSimpleBroker("/queue", "/topic");
	}

	/**
	 * WebSocket 握手拦截器 可做一些用户认证拦截处理
	 */
	private HandshakeInterceptor notifyHandshakeInterceptor() {
		return new HandshakeInterceptor() {
			/**
			 * websocket握手连接
			 * 
			 * @return 返回是否同意握手
			 */
			@Override
			public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
					WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
				ServletServerHttpRequest req = (ServletServerHttpRequest) request;
				// 通过url的query参数获取认证参数
				String token = req.getServletRequest().getParameter("token");
				logger.info("用户身份token:{}", token);
				// 根据token认证用户，不通过返回拒绝握手
				Principal user = authenticate(token);
				if (user == null) {
					logger.warn("用户身份token:{}，身份验证错误或者用户未登录！", token);
					return false;
				}
				// 保存认证用户
				attributes.put("user", user);
				return true;
			}

			@Override
			public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
					WebSocketHandler wsHandler, Exception exception) {
				// LOGGER.warn(exception.getMessage());
			}
		};
	}

	/**
	 * 根据token认证授权
	 * 
	 * @param token
	 */
	private Principal authenticate(String token) {
		// 实现用户的认证并返回用户信息，如果认证失败返回 null
		logger.info(token);

		// if(SessionUtil.isLogin(token)){
		// SessionVo sessionVo = SessionUtil.get(token);
		NoticePrincipal noticePrincipal = new NoticePrincipal();
		// noticePrincipal.setName(String.valueOf(sessionVo.getStore().get("store_id")));

		return noticePrincipal;
		// }

		// return null;
	}

	// WebSocket 握手处理器
	private DefaultHandshakeHandler notifyDefaultHandshakeHandler() {
		return new DefaultHandshakeHandler() {
			@Override
			protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
					Map<String, Object> attributes) {
				// 设置认证通过的用户到当前会话中
				return (Principal) attributes.get("user");
			}

			@Override
			protected void doStop() {
				super.doStop();
			}
		};
	}

	// 用户信息需继承 Principal 并实现 getName() 方法，返回全局唯一值
	class NoticePrincipal implements Principal {

		private String userId;

		@Override
		public String getName() {
			return userId;
		}

		public void setName(String userId) {
			this.userId = userId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			NoticePrincipal that = (NoticePrincipal) o;

			return userId != null ? userId.equals(that.userId) : that.userId == null;

		}

		@Override
		public int hashCode() {
			return userId != null ? userId.hashCode() : 0;
		}
	}

}
