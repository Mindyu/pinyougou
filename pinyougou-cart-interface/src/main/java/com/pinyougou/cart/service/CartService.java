package com.pinyougou.cart.service;

import java.util.List;

import com.pinyougou.pojogroup.Cart;

public interface CartService {
	
	/**
	 * 添加商品到购物车
	 * @param list		购物车列表
	 * @param itemId	skuID 
	 * @param num		商品数量
	 * @return
	 */
	public List<Cart> addGoodsToCartList(List<Cart> list, Long itemId, Integer num);
	
	/**
	 * 根据用户名从Redis中提取购物车
	 * @param username
	 * @return
	 */
	public List<Cart> findCartListFromRedis(String username);
	
	/**
	 * 向Redis中添加购物车数据
	 * @param username
	 * @param cartList
	 */
	public void addCartListToRedis(String username, List<Cart> cartList);

	/**
	 * 合并本地和redis购物车数据
	 * @param cartList1
	 * @param cartList2
	 * @return
	 */
	public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2);
}
