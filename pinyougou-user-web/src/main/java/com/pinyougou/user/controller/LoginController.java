package com.pinyougou.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

	@RequestMapping("/name")
	public Map<String, String> findLoginUser() {
		Map<String, String> map = new HashMap<>();
		// 当前用户名
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		map.put("userName", name);
		System.out.println("当前用户："+name);
		return map;
	}
	
}
