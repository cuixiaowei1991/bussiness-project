package com.cn.controller;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.entity.dto.AllpayAgentDto;
import com.cn.entity.dto.AllpayPosParterDto;
import com.cn.service.businessService.AllpayAgentService;
import com.cn.service.businessService.AllpayPosParterService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static all.union.tools.others.BeanUtilsHelper.jsonToBen;

/**
 * 代理合作方管理
 *
 * Created by sun.yayi on 2016/12/8.
 */
@Controller
@RequestMapping("/posParterInfo")
public class AllpayPosParterController {

    @Autowired
    private AllpayPosParterService allpayPosParterServiceImpl;

    /**
     * 2.6.1获取pos合作方信息列表
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getList(@RequestBody AllpayPosParterDto bean){
        LogHelper.info("2.6.1获取pos合作方信息列表 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayPosParterServiceImpl.getList(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }


    /**
     * 2.6.2新增pos合作方信息
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/insert", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insert(@RequestBody AllpayPosParterDto bean){
        LogHelper.info("2.6.2新增pos合作方信息 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayPosParterServiceImpl.insertAllpayAgent(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 2.6.3修改pos合作方信息
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/update", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String update(@RequestBody AllpayPosParterDto bean){
        LogHelper.info("2.6.3修改pos合作方信息 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayPosParterServiceImpl.updateAllpayAgent(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 2.6.4获取pos合作方信息
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getInfo(@RequestBody AllpayPosParterDto bean){
        LogHelper.info("2.6.4获取pos合作方信息 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayPosParterServiceImpl.getAllpayPosParterInfo(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 2.6.5删除pos合作方信息
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/delete", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String delete(@RequestBody AllpayPosParterDto bean){
        LogHelper.info("2.6.5删除pos合作方信息 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayPosParterServiceImpl.deletePosParterInfo(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 导出
     * @param
     * @return
     */

    @RequestMapping(value="/outData")
    public void outData(HttpServletRequest request,HttpServletResponse response){

        try{
            String requestBody = CommonHelper.obtainRequestBody(request);

            AllpayPosParterDto bean=jsonToBen(requestBody,AllpayPosParterDto.class);
            LogHelper.info("导出 请求参数："+bean.toString());
            allpayPosParterServiceImpl.outDataPosParter(bean, response);
        }catch (Exception e) {
            LogHelper.error(e, "导出POS合作方列表出现异常！", false);
        }
    }

}
