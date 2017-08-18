package com.cn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.JsonHelper;
import com.cn.common.LogHelper;
import com.cn.service.businessService.AllpayMerchantService;

/**
 * 商户管理
 * @author songzhili
 * 2016年12月1日下午2:21:04
 */
@Controller
@RequestMapping("/allpayMerchant")
public class AllpayMerchantController {
   
	@Autowired
	private AllpayMerchantService allpayMerchantService;
	/**
	 * 新增商户基本信息
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insert",produces="application/json;charset=UTF-8")
	public String insert(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商户管理添加商户source="+source);
		String result = null;
		try {
			result = allpayMerchantService.insert(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户管理添加商户异常!!!!!", false);
			return returnErrorMessage();
		}
		LogHelper.info("商户管理添加商户返回result="+result);
		return result;
	}
	
	/**
	 * 删除商户
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete",produces="application/json;charset=UTF-8")
	public String delete(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商户管理删除商户source="+source);
		String result = null;
		try {
			result = allpayMerchantService.delete(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户管理删除商户异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 修改商户基本信息
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/update",produces="application/json;charset=UTF-8")
	public String update(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商户管理修改商户信息");
		String result = null;
		try {
			result = allpayMerchantService.update(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户管理修改商户异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 修改商户状态
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateStatus",produces="application/json;charset=UTF-8")
	public String updateStatus(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商户管理修改商户状态");
		String result = null;
		try {
			result = allpayMerchantService.updateStatus(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户管理修改商户状态异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 获取商户基本信息
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get",produces="application/json;charset=UTF-8")
	public String obtain(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商户管理获取商户信息");
		String result = null;
		try {
			result = allpayMerchantService.obtain(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户管理获取商户信息异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 商户管理判断商户是否重名
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/checkMerchantExist",produces="application/json;charset=UTF-8")
	public String checkMerchantExist(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商户管理判断商户是否重名");
		String result = null;
		try {
			result = allpayMerchantService.obtainByMerchantName(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户管理判断商户是否重名异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 获取商户已开通业务信息
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getChannelMsg",produces="application/json;charset=UTF-8")
	public String obtainMerChannelMsg(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商户管理获取商户已开通业务信息");
		String result = null;
		try {
			result = allpayMerchantService.obtainMerChannelMsg(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户管理获取商户已开通业务信息异常!!!!!", false);
			return returnErrorMessage();
		}
		LogHelper.info("获取开通业务信息："+result);
		return result;
	}
	/**
	 * 获取商户企业信息
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCompanyMsg",produces="application/json;charset=UTF-8")
	public String obtainMerBusinessMsg(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商户管理获取商户企业信息");
		String result = null;
		try {
			result = allpayMerchantService.obtainMerBusinessMsg(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户管理获取商户企业信息异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 获取商户信息列表
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list",produces="application/json;charset=UTF-8")
	public String obtainList(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>商户管理获取商户列表信息source="+source);
		String result = null;
		try {
			result = allpayMerchantService.obtainList(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户管理获取商户列表信息异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
	}
	/**
	 * 获取商户所属的图片信息
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/obtainMerchantImageList",produces="application/json;charset=UTF-8")
	public String obtainMerchantImageList(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>获取商户所属的图片信息source="+source);
		String result = null;
		try {
			result = allpayMerchantService.obtainMerchantImageList(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "获取商户所属的图片信息异常!!!!!", false);
			return returnErrorMessage();
		}
		return result;
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
    
//    @ResponseBody
//	@RequestMapping(value="/exportExcel",produces="application/json;charset=UTF-8")
//	public String exportExcel(@RequestBody Map<String, Object> source){
//		
//		LogHelper.info("========>商户列表excel列表导出　source="+source);
//		String result = null;
//		try {
//			result = allpayMerchantService.exportExcelForList(source);
//		} catch (Exception ex) {
//			LogHelper.error(ex, "商户列表excel列表导出异常!!!!!", false);
//			return returnErrorMessage();
//		}
//		return result;
//	}
    
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
			LogHelper.info("========>商户列表excel列表导出　source="+source);
			allpayMerchantService.exportExcelForList(source,response);
		} catch (Exception ex) {
			LogHelper.error(ex, "商户列表excel列表导出异常!!!!!", false);
		}
    }
	/**
	 * 2.2.12门店、商户及pos终端信息批量导入
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/import", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	public String importMerStoPos(@RequestBody Map<String, Object> source){
		LogHelper.info("2.2.12门店、商户及pos终端信息批量导入 请求参数：" + source);
		JSONObject resultJO = new JSONObject();
		try{
			resultJO=allpayMerchantService.importMerStoPos(source);
		}catch (Exception e) {
			resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
			resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
			e.printStackTrace();
		}
		LogHelper.info("2.2.12门店、商户及pos终端信息批量导入 返回结果=====resultJO="+resultJO.toString());
		return resultJO.toString();
	}

	/**
	 * 2.2.14获取商户登录统计列表
	 * @param source
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMerchantLoginTotalList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getMerchantLoginTotalList(@RequestBody Map<String, Object> source){
		LogHelper.info("2.2.14获取商户登录统计列表 请求参数：" + source);
		JSONObject resultJO = new JSONObject();
		try{
			resultJO=allpayMerchantService.getMerchantLoginTotalList(source);
		}catch (Exception e) {
			resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
			resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
			e.printStackTrace();
		}
		return resultJO.toString();
	}

	/**
	 * 2.2.15导出商户登录统计列表
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/exportMerchantLoginTotalList")
	public void exportMerchantLoginTotalList(HttpServletRequest request,HttpServletResponse response){
		LogHelper.info("2.2.15导出商户登录统计列表 请求参数：" + request);
		try {
			String requestBody = CommonHelper.obtainRequestBody(request);
			JsonHelper jsonHelper = new JsonHelper();
			Object obj = jsonHelper.read(requestBody);
			Map<String, Object> source = null;
			if(obj instanceof Map<?,?>){
				source = (Map<String, Object>)obj;
			}
			LogHelper.info("========>导出商户登录统计列表　source="+source);
			allpayMerchantService.exportMerchantLoginTotalList(source,response);
		} catch (Exception ex) {
			LogHelper.error(ex, "导出商户登录统计列表异常!!!!!", false);
		}
	}
	
	
}