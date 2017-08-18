package com.cn.dao;

import com.cn.entity.dto.AllpayTMSDto;
import com.cn.entity.po.Allpay_TMS;

import java.util.List;
import java.util.Map;

/**
 * TMS终端管理dao层接口
 * Created by WangWenFang on 2016/12/9.
 */
public interface AllpayTMSDao {

    List<Map<String, Object>> obtainList(AllpayTMSDto allpayTMSDto, Integer currentPage, Integer pageSize)throws Exception;

    int count(AllpayTMSDto allpayTMSDto) throws Exception;

    boolean insert(Allpay_TMS allpayTMS) throws Exception;

    boolean update(Allpay_TMS allpayTMS) throws Exception;

    <T> T getById(Class<T> clazz, String tmsId) throws Exception;

    boolean delete(String tmsId) throws Exception;
}
