package com.cn.controller;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.LogHelper;
import com.cn.entity.dto.AllpayTMSDto;
import com.cn.service.businessService.AllpayTMSService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TMS终端管理
 * Created by WangWenFang on 2016/12/9.
 */
@Controller
@RequestMapping("/allpayTMS")
public class AllpayTMSController {

    @Autowired
    private AllpayTMSService allpayTMSService;

    /**
     * 2.7.1获取TMS终端信息列表
     * @param allpayTMSDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getTerminalList(@RequestBody AllpayTMSDto allpayTMSDto){
        LogHelper.info("2.7.1获取TMS终端信息列表=============allpayTMSDto=" + allpayTMSDto.toString());
        String result = "";
        try {
            result = allpayTMSService.obtainList(allpayTMSDto);
        } catch (Exception e) {
            LogHelper.error(e, "获取TMS终端信息列表出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.7.2新增TMS终端信息
     * @param allpayTMSDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insertPosInfo(@RequestBody AllpayTMSDto allpayTMSDto){
        LogHelper.info("2.7.2新增TMS终端信息=============allpayTMSDto=" + allpayTMSDto.toString());
        String result = "";
        try {
            result = allpayTMSService.insert(allpayTMSDto);
        } catch (Exception e) {
            LogHelper.error(e, "新增TMS终端信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.7.3获取TMS终端信息
     * @param allpayTMSDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getPosInfoById(@RequestBody AllpayTMSDto allpayTMSDto){
        LogHelper.info("2.7.3获取TMS终端信息=============allpayTMSDto=" + allpayTMSDto.toString());
        String result = "";
        try {
            result = allpayTMSService.getById(allpayTMSDto);
        } catch (Exception e) {
            LogHelper.error(e, "获取TMS终端信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.7.4删除TMS终端信息
     * @param allpayTMSDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteAllpayTMS", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String deleteAllpayTMS(@RequestBody AllpayTMSDto allpayTMSDto){
        LogHelper.info("2.7.4删除TMS终端信息=============allpayTMSDto=" + allpayTMSDto.toString());
        String result = "";
        try {
            result = allpayTMSService.delete(allpayTMSDto);
        } catch (Exception e) {
            LogHelper.error(e, "删除TMS终端信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.7.5修改TMS终端信息
     * @param allpayTMSDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String updatePosInfo(@RequestBody AllpayTMSDto allpayTMSDto){
        LogHelper.info("2.7.5修改TMS终端信息=============allpayTMSDto=" + allpayTMSDto.toString());
        String result = "";
        try {
            result = allpayTMSService.update(allpayTMSDto);
        } catch (Exception e) {
            LogHelper.error(e, "修改TMS终端信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

}
