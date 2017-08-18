package com.cn.service.businessService;

import java.util.Map;

/**
 * Created by cuixiaowei on 2017/1/11.
 */
public interface ZfbKouBeiShopService {
    /**
     * 2.2.1新增支付宝口碑信息
     * @param source
     * @return
     */
    public String insertZfbShop(Map<String, Object> source) throws Exception;
    /****/
    public String obtainShopList(Map<String, Object> source) throws Exception;
    /****/
    public String obtainShop(Map<String, Object> source) throws Exception;
    /****/
    public String updateShop(Map<String, Object> source) throws Exception;

    /**
     * 刷新支付宝状态
     * @param source
     * @return
     * @throws Exception
     */
    public String refreshZfbSate(Map<String, Object> source) throws Exception;

    /**
     * 存储支付宝token
     * @param source
     * @return
     * @throws Exception
     */
    public String saveZfbTokenInfo(Map<String, Object> source) throws  Exception;

    /**
     * 2.2.7商户是否授权token
     * @return
     * @throws Exception
     */
    public String shopIsAuthorize(Map<String, Object> source) throws Exception;


}
