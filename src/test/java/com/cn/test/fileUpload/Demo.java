package com.cn.test.fileUpload;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

import com.cn.test.common.WebUtils;


/**
 * 
 * @author songzhili
 * 2017年4月11日下午2:11:57
 */
public class Demo {
   
	
	@Test
	public void test() throws UnsupportedEncodingException{
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("outerKey", "213212erer312132ss1");
		node.put("fileType", "1");
		ArrayNode array = mapper.createArrayNode();
		ObjectNode nodeOne = mapper.createObjectNode();
		nodeOne.put("imageName", "sdfsdfsdfsfssdfsfdsdf");
		nodeOne.put("imagePath", "sdfsfsdfsfsfdsdfs");
		array.add(nodeOne);
		ObjectNode nodeTwo = mapper.createObjectNode();
		nodeTwo.put("imageName", "sdfsdfsdfsfssdfsfdsdf");
		nodeTwo.put("imagePath", "sdfsfsdfsfsfdsdfs");
		array.add(nodeTwo);
		node.put("imageList", array);
		String url = "http://127.0.0.1:8080/BusinessProject/fileUpload/add";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
}
