package com.pinyougou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	// 将数据库中的数据导入到 solr 中
	public void importItemData() {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");			// 状态必须为1 (商品状态，1-正常，2-下架，3-删除)
		List<TbItem> list = itemMapper.selectByExample(example);
		
		System.out.println("------商品列表-----");
		for (TbItem item : list) {
			System.out.println(item.getId()+" "+item.getTitle()+" "+item.getBrand());
			Map map = JSON.parseObject(item.getSpec(), Map.class);	// 将数据库中的spec字符串转换为map对象
			item.setSpecMap(map);
		}
		
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
		
		System.out.println("------结束------");
	}
	
	public static void main(String[] args) {
		// 加载 当前资源文件目录下的 spring/applicationContext.xml文件和 pingyougou-dao 下的连接数据库的配置文件
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");	
		SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
		solrUtil.importItemData(); 
	}
	
}
