package com.cn.dao;

import com.cn.entity.dto.AllpayAgentDto;
import com.cn.entity.po.Allpay_Agent;

import java.util.List;
import java.util.Map;

/**
 * Created by cuixiaowei on 2016/11/28.
 */
public interface agentDao {
    /**
     * 根据主键id 查询 商户信息
     * @param agentId
     * @return
     */
    public Allpay_Agent getMerchant(String agentId);

    public boolean saveOrUpate(Allpay_Agent agent) throws Exception;

    List<Map<String, Object>> obtainList(AllpayAgentDto bean, String currentPage, String pageSize) throws Exception;

    int count(AllpayAgentDto bean) throws Exception;

    List<Map<String, Object>> getAgentBySql(String agentId) throws Exception;

    List<Allpay_Agent> selectAgent(String agentName, String branchId) throws Exception;

    List<Map<String,Object>> getAgentByAgentNum(String agentNum, String agentName) throws Exception;

    List<Map<String,Object>> isExist(AllpayAgentDto bean) throws Exception;

    List<Map<String,Object>> getAllAgents(AllpayAgentDto bean) throws Exception;

    List<?> selectBranch(String branchName) throws Exception;

    List<?> selectDataDictionary(String code, String value) throws Exception;

    int CheckBranch(String branchName) throws Exception;

    int CheckAgent (String agentNum) throws Exception;

    String getAgentNum(int num) throws Exception;

    List<Map<String,Object>> getAgentsByAgentName(String agentName) throws Exception;

    public boolean delete(String agentId) throws Exception;

    List<Map<String,Object>> checkAgentPhoneExist(String phone) throws Exception;
}
