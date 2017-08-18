package com.cn.dao;

import java.util.List;
import java.util.Map;

import com.cn.entity.po.Allpay_ComUserInfo;
import com.cn.entity.po.Allpay_Merchant;
/**
 * 商户管理
 * @author songzhili
 * 2016年12月1日下午2:06:33
 */
public interface AllpayMerchantDao {
	
	
	public String insert(Allpay_Merchant merchant)throws Exception;
	/****/
	public String delete(String merchantId,boolean serious,String logRecord) throws Exception;
	/****/
	public String update(Allpay_Merchant merchant) throws Exception;
	/****/
	public String updateStatus(String merchantId,String status,String remark)throws Exception;
	/****/
    public Allpay_Merchant obtain(String merchantId)throws Exception;
    /****/
    public boolean obtainByMerchantName(Map<String, Object> source)throws Exception;
    /****/
    public Map<String, Object> obtainMerMsgForMerchantId(String merchantId)throws Exception;
    /****/
    public List<Map<String, Object>> obtainMerChannelMsg(String merchantId)throws Exception;
    /****/
    public Allpay_ComUserInfo obtainComsumerInfo(String merchantId)throws Exception;
    /****/
    public List<Map<String, Object>> obtainConsumerImage(String comuserId)throws Exception;
    /****/
    public List<Map<String, Object>> obtainList(Map<String, Object> source,boolean export)throws Exception;
    /****/
    public int count(Map<String, Object> source)throws Exception;
    /****/
    public Map<String, String> obtainMerchantStateFromDictionary(String key)throws Exception;
    
    String getMerchaCode(int num) throws Exception;

    String getPosMerCode(int num) throws Exception;

    int checkMerchaCode(String merchaCode) throws Exception;

    List<?> checkMerName(String merName, String phone) throws Exception;

    List<Map<String, Object>> getMerchantLoginTotalList(Map<String, Object> source,boolean export) throws Exception;

    int countLogin(Map<String, Object> source) throws Exception;
}
