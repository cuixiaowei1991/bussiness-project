package com.cn.service.businessService;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 门店管理
 * @author songzhili
 * 2016年12月1日下午4:12:23
 */
public interface AllpayStoreService {
    
	public String insert(Map<String, Object> source) throws Exception;
	/****/
	public String delete(Map<String, Object> source) throws Exception;
	/****/
	public String update(Map<String, Object> source) throws Exception;
	/****/
    public String obtain(Map<String, Object> source) throws Exception;
    /****/
    public String checkStoreExist(Map<String, Object> source) throws Exception;
    /****/
    public String obtainList(Map<String, Object> source) throws Exception;
    /****/
    public void exportExcel(Map<String, Object> source,HttpServletResponse response) throws Exception;
    /****/
    public String obtainTerminalPosList(Map<String, Object> source)throws Exception;
}
