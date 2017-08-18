package com.cn.service.businessService;

import org.json.JSONObject;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 商户管理
 * @author songzhili
 * 2016年12月1日下午2:14:44
 */
public interface AllpayMerchantService {
   
	
	public String insert(Map<String, Object> source) throws Exception;
	/****/
	public String delete(Map<String, Object> source) throws Exception;
	/****/
	public String update(Map<String, Object> source) throws Exception;
	/****/
	public String updateStatus(Map<String, Object> source)throws Exception;
	/****/
    public String obtain(Map<String, Object> source) throws Exception;
    /****/
    public String obtainByMerchantName(Map<String, Object> source)throws Exception;
    /****/
    public String obtainMerChannelMsg(Map<String, Object> source) throws Exception;
    /****/
    public String obtainMerBusinessMsg(Map<String, Object> source) throws Exception;
    /****/
    public String obtainList(Map<String, Object> source) throws Exception;
    /****/
    public void exportExcelForList(Map<String, Object> source,HttpServletResponse response) throws Exception;

    JSONObject importMerStoPos(Map<String, Object> source) throws Exception;

    JSONObject getMerchantLoginTotalList(Map<String, Object> source) throws Exception;

    void exportMerchantLoginTotalList(Map<String, Object> source,HttpServletResponse response) throws Exception;
    /****/
    public String obtainMerchantImageList(Map<String, Object> source)throws Exception;
}
