package com.cn.controller;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.entity.dto.AllpayAgentDto;
import com.cn.service.businessService.AllpayAgentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static all.union.tools.others.BeanUtilsHelper.jsonToBen;

/**
 * 代理商合作方管理
 *
 * Created by sun.yayi on 2016/12/5.
 */
@Controller
@RequestMapping("/agentInfo")
public class AllpayAgentController {

    @Autowired
    private AllpayAgentService allpayAgentServiceImpl;

    /**
     * 2.5.1获取代理商信息列表
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getList(@RequestBody AllpayAgentDto bean){
        LogHelper.info("2.5.1获取代理商信息列表 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayAgentServiceImpl.getList(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }


    /**
     * 2.5.2 新增代理商信息
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/insert", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insert(@RequestBody AllpayAgentDto bean){
        LogHelper.info("2.5.2 新增代理商信息 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayAgentServiceImpl.insertAllpayAgent(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 2.5.3修改代理商信息
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/update", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String update(@RequestBody AllpayAgentDto bean){
        LogHelper.info("2.5.3修改代理商信息 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayAgentServiceImpl.updateAllpayAgent(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 2.5.4获取代理商信息
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getInfo(@RequestBody AllpayAgentDto bean){
        LogHelper.info("2.5.4获取代理商信息 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayAgentServiceImpl.getAllpayAgentInfo(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 2.5.5删除代理商信息
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/delete", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String delete(@RequestBody AllpayAgentDto bean){
        LogHelper.info("2.5.5删除代理商信息 请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayAgentServiceImpl.deleteAgentInfo(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 接入商ID是否重复
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/repeatAllpayId", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String repeatAllpayId(@RequestBody AllpayAgentDto bean){
        LogHelper.info("接入商ID是否重复  请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayAgentServiceImpl.repeatAllpayId(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 代理商名称是否重复
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/checkAgentExist", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String checkAgentExist(@RequestBody AllpayAgentDto bean){
        LogHelper.info("代理商名称是否重复  请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayAgentServiceImpl.checkAgentExist(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 代理商联系电话是否重复
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/checkAgentPhoneExist", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String checkAgentPhoneExist(@RequestBody AllpayAgentDto bean){
        LogHelper.info("代理商联系人手机号是否重复  请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayAgentServiceImpl.checkAgentPhoneExist(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }


    @ResponseBody
    @RequestMapping(value="/getAgents", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getAgents(@RequestBody AllpayAgentDto bean){
        LogHelper.info("获取全部代理商列表  请求参数："+bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayAgentServiceImpl.getAgents(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 导出
     *
     * @return
     */

    @RequestMapping(value="/outData")
    public void outData(HttpServletRequest request,HttpServletResponse response){

        try{
            String requestBody = CommonHelper.obtainRequestBody(request);
            LogHelper.info("导出代理商列表 请求参数：" + requestBody);
            AllpayAgentDto bean=jsonToBen(requestBody,AllpayAgentDto.class);
            allpayAgentServiceImpl.outDataAgent(bean, response);
        }catch (Exception e) {
            LogHelper.error(e, "导出代理商列表出现异常！", false);
        }
    }


    @ResponseBody
    @RequestMapping(value="/importData", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String importData(@RequestBody Map<String, Object> source){
        LogHelper.info("导入代理商代理商列表 请求参数："+source);
        JSONObject resultJO = new JSONObject();
        try{
            resultJO=allpayAgentServiceImpl.importData(source);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }


}
