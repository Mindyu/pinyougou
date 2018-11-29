package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;

@Service  // 若未配置则会出现“Failed to check the status of the service”错误。在注册中心找不到服务。
@Transactional
public class BrandServiceImpl implements BrandService {

	@Autowired
	private TbBrandMapper branMapper;
	
	@Override
	public List<TbBrand> findAll() {
		return branMapper.selectByExample(null);
	}

	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		
		Page<TbBrand> page = (Page<TbBrand>) branMapper.selectByExample(null);
		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void add(TbBrand tbBrand) {
		branMapper.insert(tbBrand);
	}

	@Override
	public TbBrand findOne(Long id) {
		return branMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(TbBrand tbBrand) {
		branMapper.updateByPrimaryKey(tbBrand);
	}

	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			branMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbBrand tbBrand, int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		
		TbBrandExample tbBrandExample = new TbBrandExample();
		
		Criteria criteria = tbBrandExample.createCriteria();
		
		if (tbBrand!=null) {
			if (tbBrand.getName()!=null && tbBrand.getName().length()>0) {
				criteria.andNameLike("%" + tbBrand.getName() + "%");
			}
			if (tbBrand.getFirstChar()!=null && tbBrand.getFirstChar().length()>0) {
				criteria.andFirstCharEqualTo(tbBrand.getFirstChar());
			}
		}
		
		Page<TbBrand> page = (Page<TbBrand>) branMapper.selectByExample(tbBrandExample);
		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		return branMapper.selectOptionList();
	}
}
