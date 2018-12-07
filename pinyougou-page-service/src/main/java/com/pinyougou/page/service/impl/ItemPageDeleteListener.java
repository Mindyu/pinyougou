package com.pinyougou.page.service.impl;

import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.page.service.ItemPageService;

/**
 * 静态页面删除监听器
 * @author YCQ
 *
 */
@Component
public class ItemPageDeleteListener implements MessageListener {

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		
		try {
			Long[] ids = (Long[]) objectMessage.getObject();
			System.out.println("监听到消息"+Arrays.toString(ids));
			boolean b = itemPageService.deleteItemHtml(ids);
			System.out.println("完成静态页面删除操作："+b);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
