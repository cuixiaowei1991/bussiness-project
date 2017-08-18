package com.cn.dao;

import com.cn.entity.dto.AllpayAgentDto;
import com.cn.entity.dto.AllpayPosParterDto;
import com.cn.entity.po.Allpay_PosParter;

import java.util.List;
import java.util.Map;

/**
 * Created by sun.yayi on 2016/12/6.
 */
public interface AllpayPosParterDao {

    public List<?> getPosPArterByParterId(String parterId, String parterName) throws Exception;

    public boolean saveOrUpdate(Allpay_PosParter posParter) throws Exception;

    Allpay_PosParter getPosParterById(String parterId) throws Exception;

    List<Map<String,Object>> obtainList(AllpayPosParterDto bean, String currentPage, String pageSize) throws Exception;

    int count(AllpayPosParterDto bean) throws Exception;

    List<Map<String, Object>> getPosParterBySql(String parterId) throws Exception;
}
