package com.cn.controller;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.entity.dto.AllpayAgentDto;
import com.cn.entity.dto.AllpayChannelSetDto;
import com.cn.service.businessService.AllpayChannelSetService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static all.union.tools.others.BeanUtilsHelper.jsonToBen;

/**
 * 业务签约管理
 * Created by WangWenFang on 2016/12/7.
 */
@Controller
@RequestMapping("/allpayChannelSet")
public class AllpayChannelSetController {

    @Autowired
    private AllpayChannelSetService allpayChannelSetService;

    /**
     * 2.4.8获取业务信息列表
     * @param allpayChannelSetDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBusinessInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getBugsinessList(@RequestBody AllpayChannelSetDto allpayChannelSetDto){
        LogHelper.info("2.4.8获取业务信息列表=============allpayChannelSetDto=" + allpayChannelSetDto.toString());
        String result = "";
        try {
            result = allpayChannelSetService.obtainList(allpayChannelSetDto);
        } catch (Exception e) {
            LogHelper.error(e, "获取业务信息列表出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        LogHelper.info("2.4.7获取业务信息列表返回result======"+result);
        return result;
    }

    /**
     * 2.4.12导出业务签约信息列表
     * @param
     * @return
     */

    @RequestMapping(value = "/exportList")
    public void exportBugsinessList(HttpServletRequest request, HttpServletResponse response){
        try {
            String requestBody = CommonHelper.obtainRequestBody(request);
            LogHelper.info("2.4.12导出业务签约信息列表=============allpayChannelSetDto=" + requestBody);
            AllpayChannelSetDto allpayChannelSetDto=jsonToBen(requestBody,AllpayChannelSetDto.class);
            allpayChannelSetService.exportList(allpayChannelSetDto,response);
        } catch (Exception e) {
            LogHelper.error(e, "导出业务信息列表出现异常！", false);
        }
    }

    /**
     * 2.4.6新增业务签约信息
     * @param allpayChannelSetDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertBusiness", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insertBusinessInfo(@RequestBody AllpayChannelSetDto allpayChannelSetDto){
        LogHelper.info("2.4.6新增业务签约信息=============allpayChannelSetDto=" + allpayChannelSetDto.toString());
        String result = "";
        try {
            result = allpayChannelSetService.update(allpayChannelSetDto);
        } catch (Exception e) {
            LogHelper.error(e, "新增业务签约信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 修改业务签约信息
     * @param allpayChannelSetDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String update(@RequestBody AllpayChannelSetDto allpayChannelSetDto){
        LogHelper.info("=============allpayChannelSetDto=" + allpayChannelSetDto.toString());
        String result = "";
        try {
            result = allpayChannelSetService.update(allpayChannelSetDto);
        } catch (Exception e) {
            LogHelper.error(e, "出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.4.7获取业务签约信息
     * @param allpayChannelSetDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBusiness", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getBusinessInfoById(@RequestBody AllpayChannelSetDto allpayChannelSetDto){
        LogHelper.info("2.4.7获取业务签约信息=============allpayChannelSetDto=" + allpayChannelSetDto.toString());
        String result = "";
        try {
            result = allpayChannelSetService.getByMerIdChanCode(allpayChannelSetDto);
        } catch (Exception e) {
            LogHelper.error(e, "获取业务签约信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }
}
