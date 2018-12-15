package com.pinyougou.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import com.pinyougou.pojo.TbSeckillGoodsExample.Criteria;

@Component
public class SeckillTask {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	
	/**
	* 定时刷新秒杀商品
	*/
	@Scheduled(cron="0/5 * * * * ?")			// 每分钟执行一次
	public void refreshSeckillGoods(){
		System.out.println("执行了增量更新任务调度"+new Date()); 
		// 查询 Redis 中所有商品键集合
		List ids =  new ArrayList<>(redisTemplate.boundHashOps("seckillGoods").keys()); // 第一次执行为 []
		
		// 查询正在秒杀的商品列表
		TbSeckillGoodsExample example = new TbSeckillGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");						// 已审核状态
		criteria.andStockCountGreaterThan(0);				// 库存量>0
		criteria.andStartTimeLessThanOrEqualTo(new Date());	// 当前时间大于等于开始时间
		criteria.andEndTimeGreaterThanOrEqualTo(new Date());// 当前时间晚于结束时间
		if(ids.size()>0)
			criteria.andIdNotIn(ids);						// 排除已存在的商品，实现增量更新
		List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example );	// 从数据库中读取数据
		
		// 装入缓存
		for( TbSeckillGoods seckill:seckillGoodsList ){
			redisTemplate.boundHashOps("seckillGoods").put(seckill.getId(), seckill);
			System.out.println("添加商品：" + seckill.getId());
		}
		System.out.println("将"+seckillGoodsList.size()+"条商品装入缓存");
		
	} 
	
	// 每秒钟在缓存中查询已过期的商品，发现过期的秒杀商品后同步到数据库，并在缓存中移除该秒杀商品
	@Scheduled(cron="* * * * * ?")			// 每秒钟执行一次
	public void removeSeckillGoods() {
		System.out.println("执行了删除过期商品任务调度"+new Date()); 
		List<TbSeckillGoods> seckillGoodsList =	redisTemplate.boundHashOps("seckillGoods").values();
		
		for( TbSeckillGoods seckillGood : seckillGoodsList) {
			if (seckillGood.getEndTime().getTime()<new Date().getTime()) {
				redisTemplate.boundHashOps("seckillGoods").delete(seckillGood.getId());	// 删除缓存数据
				seckillGoodsMapper.updateByPrimaryKey(seckillGood);		// 向数据库保存记录
				System.out.println("移除秒杀商品："+seckillGood.getId());
			}
		}
	}

}
