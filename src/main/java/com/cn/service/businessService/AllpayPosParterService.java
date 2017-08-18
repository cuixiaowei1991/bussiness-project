package com.cn.service.businessService;

import com.cn.entity.dto.AllpayPosParterDto;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * pos合作方
 * Created by sun.yayi on 2016/12/6.
 */
public interface AllpayPosParterService {

    JSONObject getList(AllpayPosParterDto bean) throws Exception;

    JSONObject insertAllpayAgent(AllpayPosParterDto bean) throws Exception;

    JSONObject updateAllpayAgent(AllpayPosParterDto bean) throws Exception;

    JSONObject getAllpayPosParterInfo(AllpayPosParterDto bean) throws Exception;

    void outDataPosParter(AllpayPosParterDto bean,HttpServletResponse response) throws Exception;

    JSONObject deletePosParterInfo(AllpayPosParterDto bean) throws Exception;
}
