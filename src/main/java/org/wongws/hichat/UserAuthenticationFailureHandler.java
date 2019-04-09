package org.wongws.hichat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException e) throws IOException, ServletException {
		httpServletResponse.setContentType("application/json;charset=utf-8");
		PrintWriter out = httpServletResponse.getWriter();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", false);
		if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
			result.put("statusText", "用户名或密码输入错误，登录失败!");
		} else if (e instanceof DisabledException) {
			result.put("statusText", "账户被禁用，登录失败，请联系管理员!");
		} else {
			result.put("statusText", "登录失败!");
		}
		out.write(JSON.toJSONString(result));
		out.flush();
		out.close();
	}

}
