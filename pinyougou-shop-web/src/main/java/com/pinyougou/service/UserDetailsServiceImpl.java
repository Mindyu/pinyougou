package com.pinyougou.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

/**
 *  认证类
 * @author YCQ
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService{

	private SellerService sellerService;
	
	public void setSellerService(SellerService sellerService) {	// 通过配置的方式添加
		this.sellerService = sellerService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println("执行 UserDetailsServiceImpl 认证");
		// 构建角色列表
		List<GrantedAuthority> grantAuths = new ArrayList<>();
		grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		
		TbSeller seller = sellerService.findOne(username);
		if (seller!=null && "1".equals(seller.getStatus())) {
			return new User(username, seller.getPassword(), grantAuths);
		}else {
			return null;
		}
	}

}
