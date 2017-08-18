package com.cn.service.businessService;

import com.cn.entity.dto.AllpayTMSDto;

/**
 * TMS终端管理业务层接口
 * Created by WangWenFang on 2016/12/9.
 */
public interface AllpayTMSService {

    String obtainList(AllpayTMSDto allpayTMSDto)throws Exception;

    String insert(AllpayTMSDto allpayTMSDto) throws Exception;

    String update(AllpayTMSDto allpayTMSDto) throws Exception;

    String getById(AllpayTMSDto allpayTMSDto) throws Exception;

    String delete(AllpayTMSDto allpayTMSDto) throws Exception;
}
