package com.cn.test.merchant;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

import com.cn.test.common.JsonHelper;
import com.cn.test.common.WebUtils;

/**
 * 
 * @author songzhili
 * 2016年12月2日下午3:50:19
 */
public class Demo {
   
	@Test
	public void test0(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		ArrayNode array = mapper.createArrayNode();
		node.put("channelCode", "12121212");
		node.put("channelName", "小西天");
		ObjectNode nodeOne = mapper.createObjectNode();
		nodeOne.put("channelCode", "12121212");
		nodeOne.put("channelName", "小西天");
		StringBuilder together = new StringBuilder();
		together.append(node.toString()).append(",").append(nodeOne.toString());
		array.add(node);
		array.add(nodeOne);
		System.out.println(array);
		JsonHelper jsonReader = new JsonHelper();
		Object obj = jsonReader.read(array.toString());
		System.out.println(obj);
	}
	
	@Test
	public void test1(){
		
		StringBuilder together = new StringBuilder();
		together.append("channelCode:26&channelName:飞惠#channelCode:26&channelName:飞惠");
		System.out.println(together);
	}
	
	@Test
	public void list(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://192.168.15.154:8180/allpayMerchant/list";
		
		//node.put("mhtStartCreateTime", "2015-12-01");
		//node.put("mhtEndCreateTime", "2016-12-06");
		node.put("curragePage", "2");
		node.put("pageSize", "20");
		node.put("isStart", "0");
		//node.put("mhtOpenChannel", "11,12,14,142");
		//node.put("mhtName", "考虑考虑");
		try {
			long startTime = System.currentTimeMillis();
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			long endTime = System.currentTimeMillis();
			System.out.println("消耗的时间:"+(endTime-startTime));
			FileUtils.writeByteArrayToFile(new File("/home/song/software/song.txt"), responseData.getBytes("UTF-8"));
			//System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void test102(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://192.168.15.102:8180/BaseDataService!callin.action";
		node.put("curragePage", "1");
		node.put("pageSize", "10");
		try {
			long startTime = System.currentTimeMillis();
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			long endTime = System.currentTimeMillis();
			System.out.println("消耗的时间:"+(endTime-startTime));
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	
	@Test
	public void test000() throws IOException{
		
//		String ss = FileUtils.readFileToString(new File("/home/song/software/song.txt"));
//		JSONReader reader = new JSONReader();
//		Object obj = reader.read(ss);
//		if(obj instanceof Map<?, ?>){
//			Map<String, Object> objT = (Map<String, Object>)obj;
//			Object objX = objT.get("lists");
//			if(objX instanceof List<?>){
//				List<Object> objK = (List<Object>)objX;
//				int num = 0;
//				for(int t=0;t<objK.size();t++){
//					num++;
//				}
//				System.out.println(num);
//			}
//		}
	}
	
	@Test
	public void insert(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://192.168.16.225:8180/allpayMerchant/insert";
		node.put("mhtName", "开发测试003");
		node.put("mhtAgentId", "20170101");
		node.put("mhtShortName", "开发测试02");
		node.put("mhtProvinceId", "04");
		node.put("mhtCityId", "05");
		node.put("mhtCountryId", "06");
		node.put("branchId", "05");
		node.put("branchName", "06");
		node.put("mhtAddress", "北京市西城区新街口外大街28号院02");
		node.put("mhtIndustry", "中东也");
		node.put("mhtIndustryNum", "23");
		node.put("mhtState", "1");
		node.put("mhtManager", "管理员02");
		node.put("mhtManagerPhone", "18790852349");
		node.put("isCreateStore", "1");
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
		String url = "http://127.0.0.1:8080/BusinessProject/allpayMerchant/update";
		node.put("merchantId", "ff80818158d2a2bb0158d2b5a80b0000");
		node.put("mhtState", "5");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void ee(){
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		ObjectNode node = mapper.createObjectNode();
		node.put("channelCode", "23");
		node.put("channelName", "song");
		array.add(node);
		ObjectNode nodeOne = mapper.createObjectNode();
		nodeOne.put("channelCode", "24");
		nodeOne.put("channelName", "song");
		array.add(nodeOne);
		System.out.println(array);
	}
	
	@Test
	public void updateStatus(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/allpayMerchant/updateStatus";
		node.put("merchantId", "ff80818158d2a2bb0158d2b5a80b0000");
		node.put("status", "5");
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
		String url = "http://192.168.15.154:8180/allpayMerchant/get";
		node.put("merchantId", "ff8080815a7f0ee1015a82896b390015");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void getChannelMsg(){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/allpayMerchant/getChannelMsg";
		node.put("merchantId", "ff80818158d2c30b0158d2c33eff0000");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void getCompanyMsg(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/allpayMerchant/getCompanyMsg";
		node.put("merchantId", "ff80818158d2c30b0158d2c33eff0000");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void testC(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/agentInfo/outData";
		node.put("agentId", "ff80818158d2c30b0158d2c33eff0000");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void insertMenuAndFunc(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/allpayMerchant/insertMenuAndFunc";
		node.put("merchantId", "ff80818158d2c30b0158d2c33eff0000");
		ArrayNode array = mapper.createArrayNode();
		ObjectNode nodeOne = mapper.createObjectNode();
		nodeOne.put("merchantMenuId", "merchantMenuId");
		nodeOne.put("merchantFunId", "merchantFunId");
		nodeOne.put("systemId", "systemId");
		array.add(nodeOne);
		ObjectNode nodeTwo = mapper.createObjectNode();
		nodeTwo.put("merchantMenuId", "merchantMenuId");
		nodeTwo.put("merchantFunId", "merchantFunId");
		nodeTwo.put("systemId", "systemId");
		array.add(nodeTwo);
		node.put("lists", array);
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void getMenuAndFunc(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/allpayMerchant/getMenuAndFunc";
		node.put("merchantId", "ff80818158d2c30b0158d2c33eff0000");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void testA(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8082/file-upload/imageUserDefined?width=800&height=800";
		node.put("merchantId", "ff80818158d2c30b0158d2c33eff0000");
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
		String url = "http://127.0.0.1:8082/BusinessProject/allpayMerchant/exportExcel";
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void testInter(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://192.168.20.71:8800/allpaySecurityCheck/securityCreate";
		//String url = "http://192.168.15.154:8280/allpaySecurityCheck/securityCreate";
		node.put("merchantId", "ff80818158d2c30b0158d2c33eff0000");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	
	
	@Test
	public void securityCheckCreate(){
		
		ObjectMapper mapper = new ObjectMapper();
		String url = "http://192.168.15.154:8280/allpaySecurityCheck/securityCreate";
		ObjectNode node = mapper.createObjectNode();
		node.put("domainOrAppId", "192.168.15.156");
		node.put("systemName", "测试系统06");
		node.put("status", "1");
		node.put("enable", "0");
		ObjectNode nodeOne = mapper.createObjectNode();
		nodeOne.put("jsonStr", node.toString());
		nodeOne.put("code", "001");
		nodeOne.put("version", "001");
		try {
			byte [] content = nodeOne.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
//	@Test
//	public void jsonTobean()
//	{
//		String aa="{\"agentName\":\"\",\"branchId\":\"\",\"agentParentId\":\"\",\"agentNum\":\"\",\"agentType\":\"0\"}";
//		AllpayAgentDto bb= jsonToBen(aa, AllpayAgentDto.class);
//		System.out.print(bb.toString());
//	}
	
	
	@Test
	public void queryForBusiness(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/ssoAuth/business";
		node.put("username", "夏天阳");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void queryForMerchant(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/ssoAuth/merchant";
		node.put("username", "13621111111");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	@Test
	public void queryForMerchantId(){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://127.0.0.1:8080/BusinessProject/ssoAuth/merchantId";
		node.put("merchantId", "ff8080815a184d51015a1b94dcb20058");
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	
	
	@Test
	public void obtainMerchantImageList(){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		String url = "http://192.168.15.154:8180/allpayMerchant/obtainMerchantImageList";
		node.put("merchantId", "402890cd5190a604015190a6a0e40196");
		System.out.println(node.toString());
		try {
			byte [] content = node.toString().getBytes("UTF-8");
			String ctype = "application/json;charset=UTF-8";
			String responseData = WebUtils.doPost(url, ctype, content, 50000, 80000);
			System.out.println(responseData);
		} catch (IOException e) {
		}
	}
	
	
	
}










