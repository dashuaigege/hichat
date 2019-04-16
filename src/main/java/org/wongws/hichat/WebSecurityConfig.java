package org.wongws.hichat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wongws.hichat.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService customUserService() {
		return new UserServiceImpl();
	}

	@Autowired
	private UserAuthenticationFailureHandler userAuthenticationFailureHandler;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/login", "/doSignin","/ws").permitAll().anyRequest().authenticated().and()
				.formLogin().loginPage("/login").successHandler(loginSuccessHandler)
				.failureHandler(userAuthenticationFailureHandler).loginProcessingUrl("/doLogin")
				.usernameParameter("username").passwordParameter("password").permitAll().and().logout().permitAll()
				.and().rememberMe().tokenValiditySeconds(3600).key("mykey").and().csrf().disable().headers().frameOptions().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserService());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/static/**", "/css/**", "/js/**", "/font/**", "/**.ico");
	}
}
