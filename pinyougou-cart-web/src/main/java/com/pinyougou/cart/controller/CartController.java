package com.pinyougou.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	@CrossOrigin(origins="http://localhost:9105",allowCredentials="true")	// spring 4.2版本以上支持注解的方式，allowCredentials="true"可以缺省
	public Result addGoodsToCartList(Long itemId, Integer num) {
		
		// response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");	// 允许跨域请求
		// response.setHeader("Access-Control-Allow-Credentials", "true");				// 允许携带cookie （方法中如果会操作cookie的话，必须添加该配置）
		
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
				System.out.println("向cookie中存入数据"+JSON.toJSONString(cartList));
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
		// 获取当前登录用户名
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// 从cookie中读取购物车数据
		String cookieValue = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if (cookieValue == null || "".equals(cookieValue)) cookieValue = "[]";
		List<Cart> cartList_cookie = JSON.parseArray(cookieValue, Cart.class);
		
		if (username.equals("anonymousUser")) {		// 如果未登录	从cookie中读取
			System.out.println("从cookie中读取");
			return cartList_cookie;
		}else {		// 用户已登录	从redis中读取
			List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
			if (cartList_cookie.size()>0) {		// 本地购物车未合并
				System.out.println("合并本地和redis购物车数据");
				// 合并本地和redis购物车数据
				cartList_redis = cartService.mergeCartList(cartList_cookie, cartList_redis);
				// 在存储到redis中
				cartService.addCartListToRedis( username, cartList_redis);
				// 清空本地缓存购物车
				util.CookieUtil.deleteCookie(request, response, "cartList");
			}
			return cartList_redis;
		}
		
	}
	
}
