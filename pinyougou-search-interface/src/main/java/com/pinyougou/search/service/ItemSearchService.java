package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbItem;

public interface ItemSearchService {
	
	/**
	 *  搜索方法
	 * @param searchMap
	 * @return
	 */
	public Map search(Map searchMap);
	
	/**
	 * 导入SKU列表信息
	 * @param itemList
	 */
	public void importItemList(List<TbItem> itemList);
	
	/**
	 * 删除商品列表
	 * @param ids
	 */
	public void deleteByGoodsIds(Long[] ids);
}
