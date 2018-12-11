package com.pinyougou.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;

@Service(timeout=6000)
public class CartServiceImpl implements CartService{

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		
		// 1. 根据skuID （itemId）查询商品明细sku的对象
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		if (item == null) throw new RuntimeException("商品不存在");
		// 时间差，比如在添加提交订单时，商品下架了
		if (!"1".equals(item.getStatus())) throw new RuntimeException("商品不存在");
		
		// 2. 根据sku对象获取商家ID
		String sellerId = item.getSellerId();
		
		// 3. 根据商家ID在购物车列表中查询购物车对象
		Cart cart = searchCartBySellerId(cartList, sellerId);
		
		// 4.如果购物车列表中不存在该商家ID对应的购物车对象
		if (cart == null) {
			// 4.1创建该商家的购物车对象
			cart = new Cart();
			cart.setSellerId(sellerId);
			cart.setSellerName(item.getSeller());
			
			// 创建购物车明细对象
			List<TbOrderItem> orderItemList = new ArrayList<>();
			TbOrderItem orderItem = createOrderItem(item, num);
			orderItemList.add(orderItem);
			
			cart.setOrderItemList(orderItemList);
			// 4.2将该购物车对象添加到购物车列表中
			cartList.add(cart);
		}else {	 // 5. 如果购物车列表中存在该商家ID对应的购物车对象
			// 然后判断购物车对象中是否存在该商品的明细对象
			TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
			if (orderItem == null) { 	// 5.1 如果明细列表中不存在，创建明细对象添加到购物车对象中
				// 创建购物车明细对象
				orderItem = createOrderItem(item, num);
				cart.getOrderItemList().add(orderItem);	
			}else {		// 5.2 如果明细列表中存在，则增加对应的数量
				orderItem.setNum(orderItem.getNum()+num);	// 更改数量
				orderItem.setTotalFee( new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()) );	// 更改价格
				if(orderItem.getNum()<1) cart.getOrderItemList().remove(orderItem);	// 当明细的数量小于1时移除
				if (cart.getOrderItemList().size()<1) cartList.remove(cart);		// 当购物车的明细项数为0时，移除购物车列表该对象
			}
		}
		
		return cartList;
	}
	
	/**
	 * 根据商家ID在购物车列表中查询该商家的购物车
	 * @param cartList
	 * @param sellerId
	 * @return
	 */
	private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
		for(Cart cart :cartList) {
			if (sellerId.equals(cart.getSellerId())) {
				return cart;
			}
		}
		return null;
	}
	
	// 创建新的购物明细对象
	private TbOrderItem createOrderItem(TbItem item, Integer num) {
		if(num<1) throw new RuntimeException("非法数量");
		TbOrderItem order = new TbOrderItem();
		order.setGoodsId(item.getGoodsId());
		order.setItemId(item.getId());
		order.setNum(num);
		order.setPicPath(item.getImage());
		order.setPrice(item.getPrice());
		order.setSellerId(item.getSellerId());
		order.setTitle(item.getTitle());
		order.setTotalFee(new BigDecimal( item.getPrice().doubleValue()*num ));
		return order;
	}

	/**
	 * 在购物车明细列表中，根据SKUID查询购物车明细对象
	 * @param orderItemList
	 * @param itemId
	 * @return
	 */
	private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {
		for(TbOrderItem orderItem : orderItemList) {
			if (orderItem.getItemId().longValue()==itemId.longValue()) {
				return orderItem;
			}
		}
		return null;
	}

	@Override
	public List<Cart> findCartListFromRedis(String username) {
		// System.out.println("从redis中获取购物车数据");
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
		if(cartList == null) cartList = new ArrayList<>();
		return cartList;
	}

	@Override
	public void addCartListToRedis(String username, List<Cart> cartList) {
		// System.out.println("向Redis中存储购物车数据");
		redisTemplate.boundHashOps("cartList").put(username, cartList);
	}

	@Override
	public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
		if(cartList1==null && cartList2 == null) return new ArrayList<Cart>();
		for(Cart cart : cartList2) {
			for(TbOrderItem orderItem : cart.getOrderItemList()) {
				cartList1 = addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
			}
		}
		return cartList1;
	}
}
