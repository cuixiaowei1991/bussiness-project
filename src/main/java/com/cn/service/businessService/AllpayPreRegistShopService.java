package com.cn.service.businessService;

import com.cn.entity.dto.AllpayPreRegistShopDto;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by sun.yayi on 2017/2/8.
 */
public interface AllpayPreRegistShopService {

    JSONObject getList(AllpayPreRegistShopDto bean) throws Exception;

    JSONObject importPreRegistShop(Map<String, Object> source) throws Exception;
}
