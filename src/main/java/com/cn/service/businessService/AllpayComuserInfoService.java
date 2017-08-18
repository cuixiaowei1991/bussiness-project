package com.cn.service.businessService;

import com.cn.entity.po.Allpay_ComUserInfo;

import java.util.Map;

/**
 * Created by cuixiaowei on 2017/1/5.
 */
public interface AllpayComuserInfoService {
    /**
     * 新增公司信息
     * @param source
     * @return
     */
    public String insert(Map<String, Object> source) throws Exception;

    /**
     * 新增POS小票信息
     * @param source
     * @return
     */
    public String insertPosSet(Map<String, Object> source) throws Exception;

    /**
     * 2.2.3获取商户pos小票设置信息
     * @param source
     * @return
     * @throws Exception
     */
    public String getShopPosSetInfo(Map<String, Object> source) throws Exception;

    /**
     * 2.2.4修改商户信息
     * @param source
     * @return
     * @throws Exception
     */
    public String updateShopInfo(Map<String, Object> source) throws Exception;


}
