package com.cn.dao;

import java.util.List;
import java.util.Map;

import com.cn.entity.po.Allpay_Store;
/**
 * 门店管理
 * @author songzhili
 * 2016年12月1日下午4:04:00
 */
public interface AllpayStoreDao {
    
	
	/****/
	public String insert(Allpay_Store store)throws Exception;
	/****/
	public String delete(String storeId,boolean serious,String logRecord) throws Exception;
	/****/
	public String update(Allpay_Store store) throws Exception;
	/****/
    public Allpay_Store obtain(String storeId) throws Exception;
    /****/
    public boolean checkStoreExistForStoreName(Map<String, Object> source)throws Exception;
    /****/
    public boolean checkStoreExistForStoreNum(Map<String, Object> source)throws Exception;
    /****/
    public Map<String, Object> obtainForStoreId(String storeId)throws Exception;
    /****/
    public List<Map<String, Object>> obtainConsumerImage(String storeId)throws Exception;
    /****/
    public List<Map<String, Object>> obtainList(Map<String, Object> source,boolean export) throws Exception;
    /****/
    public List<Map<String, Object>> obtainTerminalPosList(String storeId)throws Exception;
    /****/
    public int count(Map<String, Object> source) throws Exception;

    List<?> checkStore(String merName, String storeName) throws Exception;

    Allpay_Store getStoreInfoByParameter(String filds,String parameter) throws Exception;

    String getShopIdNum(int num) throws Exception;
}
