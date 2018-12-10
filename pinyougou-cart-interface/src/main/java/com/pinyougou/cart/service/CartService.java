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

}
