package com.pinyougou.search.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Service(timeout=10000)		// 超时5S，默认是1S
public class ItemSearchServiceImpl implements ItemSearchService{

	@Autowired
	private SolrTemplate solrTemplate; 
	
	@Override
	public Map search(Map searchMap) {
		Map map = new HashMap();
		/*
		Query query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		map.put("rows", page.getContent());		// page.getContent() 返回一个 List 集合
		*/
		
		// 高亮显示
		HighlightQuery query = new SimpleHighlightQuery();
		
		// 构建高亮选项
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");	// 高亮域（可以为多个）
		highlightOptions.setSimplePrefix("<em style='color:red'>");	// 前缀
		highlightOptions.setSimplePostfix("</em>");					// 后缀
		
		query.setHighlightOptions(highlightOptions);	// 为查询设置高亮查询
		
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		 HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		 // 高亮入口集合（每条高亮结果的入口）
		 List<HighlightEntry<TbItem>> entryList = page.getHighlighted();
		 
		 for (HighlightEntry<TbItem> entry : entryList) {
			 // 获取高亮列表（高亮域的个数）
			 List<Highlight> hightLightList = entry.getHighlights();
			 /*
			 for (Highlight highLight : hightLightList) {
				 // 每个域可能存在多值（复制域）
				 List<String> sns = highLight.getSnipplets();
				 System.out.println(sns);
			 }*/
			 if (entry.getHighlights().size()>0 && entry.getHighlights().get(0).getSnipplets().size()>0) {
				 TbItem item = entry.getEntity();
				 item.setTitle(entry.getHighlights().get(0).getSnipplets().get(0));		// 用高亮标签结果替换
			 }
		 }
		 
		 map.put("rows", page.getContent());
		
		return map;
	}

}
