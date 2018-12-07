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
 * 监听类用于实现页面生成
 * @author YCQ
 *
 */
@Component
public class ItemPageListener implements MessageListener {

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Long[] ids = (Long[]) objectMessage.getObject();
			System.out.println("监听到消息："+Arrays.toString(ids));
			boolean flag = true;
			for (Long id : ids) {
				if (!itemPageService.genItemHtml(id)) {
					flag = false;
				};
			}
			System.out.println("完成页面生成服务:"+flag);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
