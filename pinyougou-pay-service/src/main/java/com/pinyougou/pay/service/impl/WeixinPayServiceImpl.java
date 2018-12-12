package com.pinyougou.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;

import util.HttpClient;

@Service(timeout=6000)
public class WeixinPayServiceImpl implements WeixinPayService{
	// 取出 common 包中配置
	@Value("${appid}")
	private String appid;		// 公众号id
	
	@Value("${partner}")
	private String partner;		// 商户号
	
	@Value("${partnerkey}")
	private String partnerkey;	// API密钥
	
	@Value("${notifyurl}")
	private String notifyurl;	// 回调地址

	@Override
	public Map<String, String> createNative(String out_trade_no, String total_fee) {
		// 1. 创建参数
		Map<String, String> map = new HashMap<>();
		map.put("appid", appid);				 // 公众号ID 
		map.put("mch_id", partner);				 // 商户ID
		map.put("nonce_str", WXPayUtil.generateNonceStr());	// 随机字符串
		map.put("body", "品优购");				 // 商品描述
		map.put("out_trade_no", out_trade_no);	 // 订单号
		map.put("total_fee", total_fee);		 // 总额
		map.put("spbill_create_ip", "127.0.0.1");// IP地址
		map.put("notify_url", notifyurl);		 // 回调地址
		map.put("trade_type", "NATIVE");		 // 交易类型
		
		try {
			// 2. 生成要发送的xml数据，并发送请求
			String signedXml = WXPayUtil.generateSignedXml(map, partnerkey);// 生成带签名的xml
			System.out.println("待发送的xml数据："+signedXml);
			
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			client.setHttps(true);
			client.setXmlParam(signedXml);
			client.post();
			
			// 3. 获取结果
			String xmlResult = client.getContent();
			System.out.println(xmlResult);
			Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlResult);
			
			Map<String, String> result = new HashMap<>();
			result.put("code_url", resultMap.get("code_url"));	// 返回的 二维码url 
			result.put("total_fee", total_fee);					// 金额
			result.put("out_trade_no", out_trade_no);			// 订单号
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, String> queryPayStatus(String out_trade_no) {
		// 创建参数
		Map<String, String> map = new HashMap<>();
		map.put("appid", appid);
		map.put("mch_id", partner);
		map.put("out_trade_no", out_trade_no);
		map.put("nonce_str", WXPayUtil.generateNonceStr());	// 随机字符串
		try {
			String xmlSigned = WXPayUtil.generateSignedXml(map, partnerkey);
			
			// 发送请求
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			client.setHttps(true);
			client.setXmlParam(xmlSigned);
			client.post();
			
			// 获取结果
			String xmlResult = client.getContent();
			Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlResult);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
}
