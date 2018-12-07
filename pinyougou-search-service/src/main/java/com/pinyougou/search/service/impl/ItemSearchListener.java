package com.pinyougou.search.service.impl;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Component
public class ItemSearchListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (javax.jms.TextMessage) message;
		try {
			String text = textMessage.getText();
			System.out.println("监听到消息："+text);
			
			List<TbItem> itemlist = JSON.parseArray(text,TbItem.class);
			itemSearchService.importItemList(itemlist);
			System.out.println("导入到solr索引库");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
