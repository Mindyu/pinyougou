package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {

	/**
	 * 生成二维码
	 * @param out_trade_no	订单号
	 * @param total_fee		金额
	 * @return
	 */
	public Map<String,String> createNative(String out_trade_no,String total_fee);
	
	/**
	 * 根据订单ID查询支付状态
	 * @param out_trade_no
	 * @return
	 */
	public Map<String,String> queryPayStatus(String out_trade_no);
}
