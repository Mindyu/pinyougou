package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

public interface BrandService {

	public List<TbBrand> findAll();
	
	/**
	 * 品牌分页
	 * @param pageNum  当前页面
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	/**
	 * 品牌条件查询
	 * @param pageNum  当前页面
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbBrand tbBrand, int pageNum, int pageSize);
	
	/**
	 * 增加品牌
	 * @param tbBrand
	 */
	public void add(TbBrand tbBrand);
	
	/**
	 * 品牌查询
	 * @param id	品牌ID信息
	 * @return
	 */
	public TbBrand findOne(Long id);
	
	/**
	 * 品牌信息修改
	 * @param tbBrand
	 */
	public void update(TbBrand tbBrand);
	
	public void delete(Long[] ids);
	
	/**
	 *  返回下拉列表
	 * @return
	 */
	public List<Map> selectOptionList();
}
