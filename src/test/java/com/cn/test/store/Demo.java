package com.cn.test.store;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

import com.cn.test.common.WebUtils;

/**
 * 
 * @author songzhili
 * 2016年12月7日下午12:17:01
 */
public class Demo {
    
	@Test
	public void list(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/allpayStore/list";
		node.put("curragePage", "1");
		node.put("pageSize", "10");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void terminalPosList(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("storeId", "ff80818158d781b80158d78860c30000");
		String url = "http://127.0.0.1:8080/BusinessProject/allpayStore/terminalPosList";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void insert(){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("merchantId", "4028b8815939cb11015939d5b3dd0001");
		node.put("storeName", "中关村02");
		node.put("storeShortName", "中关村03");
		node.put("posShopName", "pos中关村01");
		node.put("servicePhone", "15651701380");
		node.put("provinceId", "01");
		node.put("provinceName", "北京");
		node.put("cityId", "02");
		node.put("cityName", "北京");
		node.put("countryId", "03");
		node.put("countryName", "昌平");
		node.put("storeAddress", "北京市西城区新街口外大街");
		node.put("locationTude", "21212.333,212123.3");
		node.put("storeStatus", "1");
		String url = "http://127.0.0.1:8080/BusinessProject/allpayStore/insert";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void update(){
        
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("storeId", "ff808181593a65bf01593a6a26060000");
		node.put("storeAddress", "北京市西城区新街口外大街28号院15号楼");
		node.put("merchantId", "4028b8815939cb11015939d5b3dd0001");
		node.put("storeName", "中关村02");
		node.put("shopIdNum", "497471578");
		node.put("shopNum", "909698712270965");
		String url = "http://127.0.0.1:8080/BusinessProject/allpayStore/update";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void get(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("storeId", "ff80818158d781b80158d78860c30000");
		String url = "http://127.0.0.1:8080/BusinessProject/allpayStore/get";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void test000(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("incidate", "ff80818158d781b80158d78860c30000");
		String url = "http://127.0.0.1:8081/unification-service-provider/qingjiesuan/get";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
		
	}
	
	@Test
	public void test001(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("domainOrAppId", "www.taobao.com");
		node.put("status", "1");
		String url = "http://127.0.0.1:8080/BusinessPowerSys/allpaySafeCheck/insert";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	
	
	@Test
	public void test002(){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessPowerSys/allpaySafeCheck/list";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void exportExcel(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("curragePage", "1");
		node.put("pageSize", "1000");
		String url = "http://127.0.0.1:8082/BusinessProject/allpayStore/exportExcel";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
}
