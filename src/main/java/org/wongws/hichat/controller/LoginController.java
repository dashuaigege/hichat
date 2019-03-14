package org.wongws.hichat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@RequestMapping({ "/", "/login", "/login.html" })
	public String index() {
		return "/login";
	}
}
