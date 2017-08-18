package com.cn.controller;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.LogHelper;
import com.cn.entity.dto.AllpayPreRegistShopDto;
import com.cn.service.businessService.AllpayPreRegistShopService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by sun.yayi on 2017/2/8.
 */
@Controller
@RequestMapping("/preRegistShop")
public class AllpayPreRegistShopController {

    @Autowired
   private AllpayPreRegistShopService allpayPreRegistShopServiceImpl;

    /**
     * 2.2.13 获取预注册商户列表
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getList(@RequestBody AllpayPreRegistShopDto bean){
        LogHelper.info("获取预注册商户列表 请求参数：" + bean.toString());
        JSONObject resultJO = new JSONObject();
        try{
            resultJO= allpayPreRegistShopServiceImpl.getList(bean);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }

    /**
     * 预注册商户 导入
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/import", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String importPreRegistShop(@RequestBody Map<String, Object> source){
        LogHelper.info("预注册商户 导入 请求参数：" + source);
        JSONObject resultJO = new JSONObject();
        try{
            resultJO=allpayPreRegistShopServiceImpl.importPreRegistShop(source);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        return resultJO.toString();
    }
}
