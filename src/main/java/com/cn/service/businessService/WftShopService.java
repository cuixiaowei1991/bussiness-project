package com.cn.service.businessService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by cuixiaowei on 2017/1/11.
 */
public interface WftShopService {
    /**
     * 2.1.1威富通商户列表
     * @param source
     * @return
     * @throws Exception
     */
    public String getWftShopList(Map<String, Object> source) throws Exception;


    /**
     * 2.1.2新增威富通商户信息
     * @param source
     * @return
     * @throws Exception
     */
    public String insertWftShopInfo(Map<String, Object> source) throws Exception;

    /**
     * 2.1.3修改威富通商户信息
     * @param source
     * @return
     * @throws Exception
     */
    public String updateWftShop(Map<String, Object> source) throws Exception;

    /**
     *(主表信息)同步威富通接收、推送、威富通商户号信息
     * @param source
     * @return
     * @throws Exception
     */
    public String updateWftShopByWFT(Map<String, Object> source) throws Exception;

    /**
     * （支付类型表信息）同步威富通接收、推送、威富通支付状态信息
     * @param source
     * @return
     * @throws Exception
     */
    public String updateWftPayTypeByWFT(Map<String, Object> source) throws Exception;
    /****/
    public String insertShop(Map<String, Object> source)throws Exception; 
    /****/
    public String updateMerchantStatus(Map<String, Object> source)throws Exception;
    /****/
    public String updateBill(Map<String, Object> source)throws Exception;
    /****/
    public String updateShop(Map<String, Object> source)throws Exception;
    /****/
    public String obtainShop(Map<String, Object> source)throws Exception;
    /****/
    public String obtainPayTypeList(Map<String, Object> source)throws Exception;
    /****/
    public String obtainBankCodeList(Map<String, Object> source)throws Exception;

    /**
     * 导出威富通列表
     * @param source
     * @return
     * @throws Exception
     */
    public void exportExcelForList(Map<String, Object> source,HttpServletResponse response)throws Exception;

}
