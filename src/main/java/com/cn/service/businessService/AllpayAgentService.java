package com.cn.service.businessService;

import com.cn.entity.dto.AllpayAgentDto;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 代理商合作方  管理
 * Created by sun.yayi on 2016/12/5.
 */
public interface AllpayAgentService {

    JSONObject insertAllpayAgent(AllpayAgentDto bean) throws Exception;

    JSONObject getList(AllpayAgentDto bean) throws Exception;

    JSONObject updateAllpayAgent(AllpayAgentDto bean) throws Exception;

    JSONObject getAllpayAgentInfo(AllpayAgentDto bean) throws Exception;

    void outDataAgent(AllpayAgentDto bean, HttpServletResponse response) throws Exception;

    JSONObject importData(Map<String, Object> source) throws Exception;

    JSONObject deleteAgentInfo(AllpayAgentDto bean) throws Exception;

    JSONObject repeatAllpayId(AllpayAgentDto bean) throws Exception;

    JSONObject getAgents(AllpayAgentDto bean) throws Exception;

    JSONObject checkAgentExist(AllpayAgentDto bean) throws Exception;

    JSONObject checkAgentPhoneExist(AllpayAgentDto bean) throws Exception;
}
