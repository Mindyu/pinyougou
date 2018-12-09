package com.pinyougou.user.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("经过认证类："+username);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		// 角色固定了，如果存在多种角色的话，那么此处可能会去数据库中查找来实现动态设置用户角色
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));	
		return new User(username, "", authorities);
	}

}
