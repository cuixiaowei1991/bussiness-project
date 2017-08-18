package com.cn.controller;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.dao.impl.SsoAuthDaoImpl;

/**
 * 单点登录Controller
 * @author songzhili
 * 2017年2月14日下午12:28:39
 */
@Controller
@RequestMapping("/ssoAuth")
public class SsoAuthController {

	
	@Autowired
	private SsoAuthDaoImpl ssoAuthDao;
	
	@ResponseBody
	@RequestMapping(value="/business",produces="application/json;charset=UTF-8")
	public String queryForBusiness(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>业务系统单点登录查询source="+source);
		String result = null;
		try {
			Map<String, Object> map = ssoAuthDao.queryForBusiness(source);
			if(map == null){
				result = returnNullObject();
			}else{
				result = transferBusinessData(map);
			}
		} catch (Exception ex) {
			LogHelper.error(ex, "业务系统单点登录查询信息异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/merchant",produces="application/json;charset=UTF-8")
	public String queryForMerchant(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商助单点登录用户查询source="+source);
		String result = null;
		try {
			Map<String, Object> map = ssoAuthDao.queryForMerhcant(source);
			if(map == null){
				result = returnNullObject();
			}else{
				result = transferMerchantData(map);
			}
		} catch (Exception ex) {
			LogHelper.error(ex, "商助单点登录用户查询信息异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/merchantId",produces="application/json;charset=UTF-8")
	public String queryForMerchantId(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>业务系统跳转到商助系统source="+source);
		String result = null;
		try {
			Map<String, Object> map = ssoAuthDao.queryForMerhcantId(source);
			if(map == null){
				result = returnNullObject();
			}else{
				result = transferMerchantData(map);
			}
		} catch (Exception ex) {
			LogHelper.error(ex, "业务系统跳转到商助系统异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	private String transferBusinessData(Map<String, Object> map){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode result = mapper.createObjectNode();
		result.put("userId", checkTextIsOrNotNull(map.get("USER_ID")));
    	result.put("bus_userName", checkTextIsOrNotNull(map.get("USER_NAME")));
    	result.put("phone", checkTextIsOrNotNull(map.get("USER_PHONE")));
    	result.put("nickName", checkTextIsOrNotNull(map.get("USER_NICKNAME")));
    	result.put("passWord", checkTextIsOrNotNull(map.get("USER_PASSWORD")));
    	result.put("userState", checkTextIsOrNotNull(map.get("USER_ISSTART")));
    	result.put("branchId", checkTextIsOrNotNull(map.get("USER_ORGANIZATIONID")));
    	result.put("branchName", checkTextIsOrNotNull(map.get("ORGANIZATION_NAME")));
    	result.put("branchType", checkTextIsOrNotNull(map.get("ORGANIZATION_TYPE")));
    	result.put("roleId", checkTextIsOrNotNull(map.get("ROLE_ID")));
    	result.put("roleName", checkTextIsOrNotNull(map.get("ROLE_NAME")));
    	result.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
    	result.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
    	return result.toString();
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public String transferMerchantData(Map<String, Object> map){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode result = mapper.createObjectNode();
		result.put("userId", checkTextIsOrNotNull(map.get("SHOPUSER_ID")));
    	result.put("mer_userName", checkTextIsOrNotNull(map.get("SHOPUSER_NAME")));
    	result.put("nickName", checkTextIsOrNotNull(map.get("SHOPUSER_NICKNAME")));
    	result.put("passWord", checkTextIsOrNotNull(map.get("SHOPUSER_PASSWORD")));
    	result.put("userRole", checkTextIsOrNotNull(map.get("SHOPUSER_ROLE")));
    	result.put("userType", checkTextIsOrNotNull(map.get("SHOPUSER_ROLE")));
    	result.put("merchantId", checkTextIsOrNotNull(map.get("MERCHANT_ID")));
    	result.put("shopName", checkTextIsOrNotNull(map.get("MERCHANT_MERCHNAME")));
    	result.put("merchantState", checkTextIsOrNotNull(map.get("ALLPAY_ISSTART")));
    	result.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
    	result.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
    	return result.toString();
	}
	/**
	 * 返回服务器异常信息
	 * @return
	 */
    private String returnErrorMessage(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
		node.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
		return node.toString();
	}
    
    private String returnNullObject(){
    	ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
		node.put(MsgAndCode.RSP_DESC, "用户不存在!!!!");
		return node.toString();
    }
    /**
	 * 校验　非空
	 * @param obj
	 * @return
	 */
	private String checkTextIsOrNotNull(Object obj){
		
		if(!CommonHelper.isNullOrEmpty(obj)){
			return obj.toString();
		}
		return "";
	}
}
