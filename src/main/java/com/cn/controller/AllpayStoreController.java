package com.cn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.JsonHelper;
import com.cn.common.LogHelper;
import com.cn.service.businessService.AllpayStoreService;

/**
 * 门店管理
 * @author songzhili
 * 2016年12月1日下午4:16:33
 */
@Controller
@RequestMapping("/allpayStore")
public class AllpayStoreController {
   
	@Autowired
	private AllpayStoreService allpayStoreService;
	/**
	 * 添加门店
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insert",produces="application/json;charset=UTF-8")
	public String insert(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>门店管理添加门店");
		String result = null;
		try {
			result = allpayStoreService.insert(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "门店管理添加门店异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete",produces="application/json;charset=UTF-8")
	public String delete(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>门店管理删除门店");
		String result = null;
		try {
			result = allpayStoreService.delete(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "门店管理删除门店异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 修改门店信息
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/update",produces="application/json;charset=UTF-8")
	public String update(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>门店管理修改门店信息source="+source);
		String result = null;
		try {
			result = allpayStoreService.update(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "门店管理修改门店异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 获取门店信息
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get",produces="application/json;charset=UTF-8")
	public String obtain(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>门店管理获取门店信息");
		String result = null;
		try {
			result = allpayStoreService.obtain(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "门店管理获取门店信息异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/checkStoreExist",produces="application/json;charset=UTF-8")
	public String checkStoreExist(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>门店管理判断门店是否存在");
		String result = null;
		try {
			result = allpayStoreService.checkStoreExist(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "门店管理判断门店是否存在异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	
	/**
	 * 获取门店信息列表
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list",produces="application/json;charset=UTF-8")
	public String obtainList(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>门店管理获取门店列表信息");
		String result = null;
		try {
			result = allpayStoreService.obtainList(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "门店管理获取门店列表信息异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 获取POS终端信息列表
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/terminalPosList",produces="application/json;charset=UTF-8")
	public String obtainTerminalPosList(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>门店管理获取POS终端信息列表");
		String result = null;
		try {
			result = allpayStoreService.obtainTerminalPosList(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "门店管理获取POS终端信息列表!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response){
		
		try {
			String requestBody = CommonHelper.obtainRequestBody(request);
			JsonHelper jsonHelper = new JsonHelper();
			Object obj = jsonHelper.read(requestBody);
			Map<String, Object> source = null;
			if(obj instanceof Map<?,?>){
				source = (Map<String, Object>)obj;
			}
			LogHelper.info("========>门店列表导出source:"+source);
			allpayStoreService.exportExcel(source,response);
		} catch (Exception ex) {
			LogHelper.error(ex, "门店列表导出excel息异常!!!!!", false);
		}
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
}
