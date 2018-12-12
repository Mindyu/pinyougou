package com.pinyougou.cart.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeixinPayService;

import entity.Result;
import util.IdWorker;

@RestController
@RequestMapping("/pay")
public class PayController {

	@Reference
	private WeixinPayService weixinPayService;
	
	/**
	 * 生成二维码
	 * @return
	 */
	@RequestMapping("/createNative")
	public Map<String, String> createNative() {
		IdWorker idWorker = new IdWorker();
		return weixinPayService.createNative(idWorker.nextId()+"", "1");	// 金额以分为单位
	}
	
	/**
	 * 通过 订单ID 查询订单支付状态
	 * @param out_trade_no
	 * @return
	 */
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no) {
		Result result = null;
		int i = 100;
		
		while(true) {
			Map<String, String> map = weixinPayService.queryPayStatus(out_trade_no);
			if (map == null) {
				result = new Result(false, "支付出错"); break;
			}
			if(map.get("trade_state").equals("SUCCESS")){	// 如果成功
				result = new Result(true, "支付成功"); break;
			}
			try {
				Thread.sleep(3000);
				if (--i <= 0) {
					return new Result(false, "二维码超时");			// 100*3 秒  5分钟
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		
		return result;
	}
}
