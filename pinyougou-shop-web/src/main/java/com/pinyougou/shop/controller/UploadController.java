package com.pinyougou.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;
import util.FastDFSClient;

/**
 * 文件上传
 * @author YCQ
 *
 */
@RestController
public class UploadController {
	
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;		// 文件服务器地址 http://ip/
	
	@RequestMapping("/upload")
	public Result upload(MultipartFile file) {
		
		String originFileName = file.getOriginalFilename();	// 获取文件名
		String extName = originFileName.substring(originFileName.lastIndexOf('.')+1);	// 获取扩展名
		
		try {
			FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
			String file_id = client.uploadFile(file.getBytes(), extName);
			String url = FILE_SERVER_URL + file_id;			// 图片的完整地址
			return new Result(true, url);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "上传失败");
		}
		
	}
	

}
