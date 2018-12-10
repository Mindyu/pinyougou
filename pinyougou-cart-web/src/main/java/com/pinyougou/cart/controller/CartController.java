package com.pinyougou.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;

import entity.Result;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@Reference
	private CartService cartService;
	
	@RequestMapping("/addGoodsToCartList")
	public Result addGoodsToCartList(Long itemId, Integer num) {
		
		// 获取当前登录用户名
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
				
		try {
			// 取出购物车列表
			List<Cart> cartList = findCartList();
			// 修改购物车列表信息
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
			
			if (username.equals("anonymousUser")) {	// 未登录
				// 将购物车列表信息存入 cookie
				util.CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600*24, "UTF-8");
				System.out.println("向cookie中存入数据");
			}else {		// 已登录
				cartService.addCartListToRedis(username, cartList);
			}
			return new Result(true, "存入购物车成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "存入购物车失败");
		}
	}
	
	@RequestMapping("/findCartList")
	public List<Cart> findCartList() {
		List<Cart> cartList = null;
		// 获取当前登录用户名
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if (username.equals("anonymousUser")) {		// 如果未登录	从cookie中读取
			System.out.println("从cookie中读取");
			String cookieValue = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
			if (cookieValue == null || "".equals(cookieValue)) cookieValue = "[]";
			cartList = JSON.parseArray(cookieValue, Cart.class);
		}else {		// 用户已登录	从redis中读取
			cartList = cartService.findCartListFromRedis(username);
		}
		
		return cartList;
	}
	
}
