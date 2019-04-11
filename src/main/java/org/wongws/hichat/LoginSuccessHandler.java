package org.wongws.hichat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.wongws.hichat.entity.HcLoginlog;
import org.wongws.hichat.entity.HcUser;
import org.wongws.hichat.repository.HcLoginlogRepository;
import org.wongws.hichat.repository.HcUserRepository;
import org.wongws.hichat.util.Util;

import com.alibaba.fastjson.JSON;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Autowired
	private HcUserRepository hcUserRepository;
	@Autowired
	private HcLoginlogRepository hcLoginlogRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {
		try {
			HcUser user = (HcUser) authentication.getPrincipal();
			// HcUser user = hcUserRepository.findByUsername(userDetails.getUsername());
			user.setPassword(null);
			// save loginlog
			HcLoginlog loginlog = new HcLoginlog();
			loginlog.setUserid(user.getId());
			loginlog.setIp(Util.getIpAddr(httpServletRequest));
			hcLoginlogRepository.save(loginlog);

			httpServletRequest.getSession().setAttribute("user_hid", user.getUser_hid());
			httpServletRequest.getSession().setAttribute("username", user.getUsername());

			httpServletResponse.setContentType("application/json;charset=utf-8");
			PrintWriter out = httpServletResponse.getWriter();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("status", true);
			result.put("statusText", "登录成功");
			result.put("targetUrl", contextPath + "/chat");
			out.write(JSON.toJSONString(result));
			out.flush();
			out.close();
			super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
		} catch (Exception e) {
			logger.error("onAuthenticationSuccess failed:" + e.getMessage());
		}
	}
}
