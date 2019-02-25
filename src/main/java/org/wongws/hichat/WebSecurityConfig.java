package org.wongws.hichat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.alibaba.fastjson.JSON;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/login").permitAll().anyRequest().authenticated().and().formLogin()
				.loginPage("/login").successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
							HttpServletResponse httpServletResponse, Authentication authentication)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						PrintWriter out = httpServletResponse.getWriter();
						Map<String, Object> result = new HashMap<String, Object>();
						result.put("status", true);
						result.put("statusText", "登录成功");
						result.put("targetUrl", "/chat");
						out.write(JSON.toJSONString(result));
						out.flush();
						out.close();
					}
				}).failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
							HttpServletResponse httpServletResponse, AuthenticationException e)
							throws IOException, ServletException {
						httpServletResponse.setContentType("application/json;charset=utf-8");
						PrintWriter out = httpServletResponse.getWriter();
						Map<String, Object> result = new HashMap<String, Object>();
						result.put("status", false);
						result.put("statusText", "登录失败");
						out.write(JSON.toJSONString(result));
						out.flush();
						out.close();
					}
				}).loginProcessingUrl("/doLogin").usernameParameter("username").passwordParameter("password")
				.permitAll().and().logout().permitAll().and().csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("wws")
				.password(new BCryptPasswordEncoder().encode("wws")).roles("USER").and().withUser("lmm")
				.password(new BCryptPasswordEncoder().encode("lmm")).roles("USER")
				.and().withUser("tzc")
				.password(new BCryptPasswordEncoder().encode("tzc")).roles("USER")
				.and().withUser("zf")
				.password(new BCryptPasswordEncoder().encode("zf")).roles("USER")
				.and().withUser("yz")
				.password(new BCryptPasswordEncoder().encode("yz")).roles("USER")
				.and().withUser("wvv")
				.password(new BCryptPasswordEncoder().encode("wvv")).roles("USER");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/static/**", "/css/**", "/js/**", "/font/**");
	}
}
