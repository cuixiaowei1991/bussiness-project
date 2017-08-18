package com.cn.controller;

import com.cn.common.LogHelper;
import com.cn.service.businessService.AllpayComuserInfoService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by cuixiaowei on 2017/1/5.
 */
@Controller
@RequestMapping("/shopCompany")
public class AllpayComuserInfoController {

    @Autowired
    private AllpayComuserInfoService allpayComuserInfoService;

    /**
     * 新增商户企业信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/insert", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insertCompanyInfo(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.2.1新增商户企业信息："+source);
        String result="";
        try {
            result=  allpayComuserInfoService.insert(source);
        } catch (Exception e) {
            LogHelper.error(e,"1新增商户企业信息异常！！",false);
            e.printStackTrace();
        }
        LogHelper.info("2.2.1新增商户企业信息返回："+result);
        return result;
    }

    /**
     * 2.2.2新增商户pos小票设置信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/insertPosSet", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insertPosSet(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("新增商户pos小票设置信息:"+source);
        String result="";

        try {
            result=  allpayComuserInfoService.insertPosSet(source);
        } catch (Exception e) {
            LogHelper.error(e, "新增商户pos小票设置信息异常!!!!!", false);
            e.printStackTrace();
        }
        LogHelper.info("新增商户pos小票设置信息result=" + result);
        return result;
    }
    /**
     * 2.2.3获取商户pos小票设置信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getShopPosSetInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getShopPosSetInfo(@RequestBody Map<String, Object> source)
    {
        String result="";

        try {
            result=  allpayComuserInfoService.getShopPosSetInfo(source);
        } catch (Exception e) {
            LogHelper.error(e, "获取商户pos小票设置信息异常!!!!!", false);
            e.printStackTrace();
        }
        LogHelper.info("获取商户pos小票设置信息result=" + result);
        return result;
    }

    /**
     * 2.2.4修改商户信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/updateShopInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String updateShopUserInfo(@RequestBody Map<String, Object> source)
    {
        String result="";

        try {
            result=  allpayComuserInfoService.updateShopInfo(source);
        } catch (Exception e) {
            LogHelper.error(e, "修改商户pos小票设置信息异常!!!!!", false);
            e.printStackTrace();
        }
        LogHelper.info("修改商户pos小票设置信息result=" + result);
        return result;
    }
}
