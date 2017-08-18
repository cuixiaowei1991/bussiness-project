package com.cn.controller;

import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.service.businessService.WftShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static all.union.tools.others.BeanUtilsHelper.json2map;

/**
 * Created by cuixiaowei on 2017/1/11.
 */
@Controller
@RequestMapping("/wftInfo")
public class WftShopController {
    @Autowired
    private WftShopService wftShopService;
    @ResponseBody
    @RequestMapping(value="/GetWftShopList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getWftShopInfoList(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.1.1威富通商户列表："+source);
        String result="";
        try {
            result=wftShopService.getWftShopList(source);
        } catch (Exception e) {
            LogHelper.error(e,"威富通商户列表发生异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("威富通商户列表:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/InsertWftShop", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insertWftShop(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.1.2新增威富通商户信息："+source);
        String result="";
        try {
            result=wftShopService.insertWftShopInfo(source);
        } catch (Exception e) {
            LogHelper.error(e,"新增威富通商户发生异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("新增威富通商户:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/UpdateWftShop", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String updateWftShop(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.1.3修改威富通商户信息:"+source);
        String result="";
        try {
            result=wftShopService.updateWftShop(source);
        } catch (Exception e) {
            LogHelper.error(e,"修改威富通商户信息发生异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("修改威富通商户信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/GetWftShop", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getWftShopInfo(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.1.4获取威富通商户信息:"+source);
        String result="";
        try {
            result=wftShopService.obtainShop(source);
        } catch (Exception e) {
            LogHelper.error(e,"获取威富通商户信息发生异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("获取威富通商户信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/GetWftBankCodeList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getWftBankCodeList(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.1.5获取威富通联行号:"+source);
        String result="";
        try {
            result=wftShopService.obtainBankCodeList(source);
        } catch (Exception e) {
            LogHelper.error(e,"获取威富通联行号发生异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("获取威富通联行号:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/GetWftPayTypeList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getWftPayTypeList(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.1.7获取威富通支付类型信息:"+source);
        String result="";
        try {
            result=wftShopService.obtainPayTypeList(source);
        } catch (Exception e) {
            LogHelper.error(e,"获取威富通支付类型信息发生异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("获取威富通支付类型信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/UpdateWftAccount", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String updateWftAccount(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.1.9修改威富通账户信息:"+source);
        String result="";
        try {
            result=wftShopService.updateShop(source);
        } catch (Exception e) {
            LogHelper.error(e,"修改威富通账户信息发生异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("修改威富通账户信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/UpdateWftBill", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String updateWftBillInfo(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("修改威富通费率信息:"+source);
        String result="";
        try {
            result=wftShopService.updateBill(source);
        } catch (Exception e) {
            LogHelper.error(e,"修改威富通费率信息发生异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("修改威富通费率信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/UpdateWftShopState", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String updateWftShopStateInfo(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.1.12修改威富通商户状态信息:"+source);
        String result="";
        try {
            result=wftShopService.updateMerchantStatus(source);
        } catch (Exception e) {
            LogHelper.error(e,"修改威富通商户状态信息！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("修改威富通商户状态信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/editWftPayMess", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String editWftPayMess(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("1.1.2更新威富通支付类型表信息:"+source);
        String result="";
        try {
            result=wftShopService.updateWftPayTypeByWFT(source);
        } catch (Exception e) {
            LogHelper.error(e,"更新威富通支付类型表信息！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("更新威富通支付类型表信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/editWftShopMess", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String editWftShopMess(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("1.1.1更新威富通主表信息:"+source);
        String result="";
        try {
            result=wftShopService.updateWftShopByWFT(source);
        } catch (Exception e) {
            LogHelper.error(e,"更新威富通主表信息！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("更新威富通主表信息:"+result);
        return result;
    }


    @RequestMapping(value="/exportExcel")
    public void exportExcel(HttpServletRequest request,HttpServletResponse response){


        try {
            String requestBody = CommonHelper.obtainRequestBody(request);
            Map<String, Object> source=json2map(requestBody);
            LogHelper.info("========>威富通列表excel列表导出　source="+source);
            wftShopService.exportExcelForList(source,response);
        } catch (Exception ex) {
            LogHelper.error(ex, "威富通列表excel列表导出异常!!!!!", false);
        }
    }
}
