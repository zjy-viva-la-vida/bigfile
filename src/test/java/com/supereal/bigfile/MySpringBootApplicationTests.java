package com.supereal.bigfile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.supereal.bigfile.entity.UploadFile;
import com.supereal.bigfile.repository.UploadFileRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MySpringBootApplicationTests extends BasicUtClass{

	@Autowired
	UploadFileRepository uploadFileRepository;
	
	@Test
	public void localCacheTest() throws JsonProcessingException, InterruptedException{
		System.out.println("第一次："); //从数据库中获取
		UploadFile p = uploadFileRepository.findUploadFileById("25669b8dd0c44cf6944973e1a5ed439c");
		logger.info("1st time: {}", objectMapper.writeValueAsString(p));
		
		System.out.println("第二次："); //从缓存中获取
		p = uploadFileRepository.findUploadFileById("25669b8dd0c44cf6944973e1a5ed439c");
		logger.info("2nd time: {}", objectMapper.writeValueAsString(p));
		
		Thread.sleep(5000);
		
		System.out.println("第三次："); //5秒钟超时后，从数据库中获取
		p = uploadFileRepository.findUploadFileById("25669b8dd0c44cf6944973e1a5ed439c");
		logger.info("3rd time: {}", objectMapper.writeValueAsString(p));

		System.out.println("第四次："); //从缓存中获取
		p = uploadFileRepository.findUploadFileById("25669b8dd0c44cf6944973e1a5ed439c");
		logger.info("4th time: {}", objectMapper.writeValueAsString(p));

	}
	
	
/*	@Autowired
	VelocityEngine velocityEngine;
	
	@Test
	public void velocityTest(){
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("time", XDateUtils.nowToString());
		model.put("message", "这是测试的内容。。。");
		model.put("toUserName", "张三");
		model.put("fromUserName", "老许");
		System.out.println(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "welcome.vm", "UTF-8", model));
	}*/

}
