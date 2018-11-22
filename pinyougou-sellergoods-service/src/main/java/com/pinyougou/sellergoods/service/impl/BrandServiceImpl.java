package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

@Service  // 若未配置则会出现“Failed to check the status of the service ”错误。在注册中心找不到服务。
public class BrandServiceImpl implements BrandService {

	@Autowired
	private TbBrandMapper branMapper;
	
	@Override
	public List<TbBrand> findAll() {
		return branMapper.selectByExample(null);
	}

}
