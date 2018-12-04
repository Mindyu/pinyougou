package com.pinyougou.sellergoods.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.mapper.TbSellerMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbGoodsExample;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDesMapper;

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private TbBrandMapper brandMapper;

	@Autowired
	private TbSellerMapper sellerMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		goods.getGoods().setAuditStatus("0"); // 设置未申请状态
		goodsMapper.insert(goods.getGoods());
		Long goodsId = goods.getGoods().getId();
		
		TbGoodsDesc record = goods.getGoodsDesc();
		record.setGoodsId(goodsId);
		goodsDesMapper.insert(record); // 插入商品扩展信息

		insertItemList(goods);
	}

	// 插入 SKU 列表数据，统一新增与修改模块的代码
	private void insertItemList(Goods goods) {
		if ("1".equals(goods.getGoods().getIsEnableSpec())) {
			for (TbItem item : goods.getItemList()) {
				// 构建标题 SPU名称 + 规格选项值
				String title = goods.getGoods().getGoodsName(); // SPU名称

				Map<String, Object> map = JSON.parseObject(item.getSpec()); // {"spec":{"网络":"移动3G","机身内存":"16G"}
				for (String key : map.keySet()) {
					title += " " + map.get(key);
				}
				item.setTitle(title);

				setItemValue(item, goods);

				itemMapper.insert(item);
			}
		} else { // 未启用规格
			TbItem item = new TbItem();
			item.setTitle(goods.getGoods().getGoodsName()); // 标题
			item.setPrice(goods.getGoods().getPrice()); // 价格
			item.setNum(9999); // 库存量
			item.setStatus("1"); // 状态
			item.setIsDefault("1"); // 是否默认
			item.setSpec("{}");
			setItemValue(item, goods);
			itemMapper.insert(item);
		}
	}

	public void setItemValue(TbItem item, Goods goods) {
		// 三级商品分类
		item.setCategoryid(goods.getGoods().getCategory3Id());
		// 创建时间
		item.setCreateTime(new Date());
		// 修改时间
		item.setUpdateTime(new Date());
		item.setGoodsId(goods.getGoods().getId()); // 商品ID
		item.setSellerId(goods.getGoods().getSellerId()); // 商家ID

		// 分类名称
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(item.getCategoryid());
		item.setCategory(itemCat.getName());

		// 品牌名称
		TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
		item.setBrand(brand.getName());

		// 商家名称（店铺名称）
		TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
		item.setSeller(seller.getNickName());

		// 首图片名称
		List<Map> list = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
		if (list != null && list.size() > 0) {
			item.setImage((String) list.get(0).get("url"));
		}
	}

	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods) {
		// 更新基本数据
		goodsMapper.updateByPrimaryKey(goods.getGoods());
		// 更新扩展表
		goodsDesMapper.updateByPrimaryKey(goods.getGoodsDesc());
		// 更新SKU列表信息 (先删后插)
		TbItemExample example = new TbItemExample();
		com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getGoods().getId());
		itemMapper.deleteByExample(example);
		// 重新插入SKU信息
		insertItemList(goods);
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id) {
		Goods goods = new Goods();
		// 商品基本信息表
		goods.setGoods(goodsMapper.selectByPrimaryKey(id));
		// 商品扩展表
		goods.setGoodsDesc(goodsDesMapper.selectByPrimaryKey(id));
		;

		// 商品 SKU 规格表
		TbItemExample example = new TbItemExample();
		com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		List<TbItem> list = itemMapper.selectByExample(example);
		goods.setItemList(list);

		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			TbGoods goods =  goodsMapper.selectByPrimaryKey(id);
			goods.setIsDelete("1");			// 表示逻辑删除
			goodsMapper.updateByPrimaryKey(goods);
		}
	}

	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbGoodsExample example = new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeleteIsNull();	// 指定未被删除(当前子段为空)

		if (goods != null) {
			if (goods.getSellerId() != null && goods.getSellerId().length() > 0) { // 商家ID查询, 精确查询
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
				criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
			}
			if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
				criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
			}
			if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
				criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
			}
			if (goods.getCaption() != null && goods.getCaption().length() > 0) {
				criteria.andCaptionLike("%" + goods.getCaption() + "%");
			}
			if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
				criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
			}
			if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
				criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
			}
			if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
				criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
			}

		}

		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void updateStatus(Long[] ids, String status) {
		for (int i = 0; i < ids.length; i++) {
			TbGoods goods = goodsMapper.selectByPrimaryKey(ids[i]);
			goods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(goods);
		}

	}
	
	/**
	 * 根据SPU集合查询SKU信息列表
	 * @param ids
	 * @param status
	 * @return
	 */
	@Override
	public List<TbItem> findItemListByGoodsIdAndStatus(Long[] ids, String status){
		TbItemExample example = new TbItemExample();
		com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(status);
		criteria.andGoodsIdIn(Arrays.asList(ids));
		return itemMapper.selectByExample(example);
	}

}
