package com.cn.dao;

import com.cn.entity.dto.AllpayAgentDto;
import com.cn.entity.dto.AllpayPreRegistShopDto;
import com.cn.entity.po.Allpay_PreRegistShop;

import java.util.List;
import java.util.Map;

/**
 * Created by sun.yayi on 2017/2/8.
 */
public interface AllpayPreRegistShopDao {

    List<Map<String, Object>> obtainList(AllpayPreRegistShopDto bean, int currentPage, int pageSize) throws Exception;

    int count(AllpayPreRegistShopDto bean)  throws Exception;

    String insert(Allpay_PreRegistShop preRegistShop) throws Exception;
}
