package com.cn.controller;

import com.cn.common.LogHelper;
import com.cn.service.businessService.ZfbKouBeiShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by cuixiaowei on 2017/1/11.
 */
@Controller
@RequestMapping("/zfbInfo")
public class ZfbKouBeiShopController {
    @Autowired
    private ZfbKouBeiShopService zfbKouBeiShopService;
    @ResponseBody
    @RequestMapping(value="/InsertZfbShop", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insertZfbShop(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.2.1新增支付宝口碑信息："+source);
        String result="";
        try {
            result= zfbKouBeiShopService.insertZfbShop(source);
        } catch (Exception e) {
            LogHelper.error(e,"新增支付宝口碑信息异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("新增支付宝口碑信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/GetZfbShopList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getZfbShopList(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.2.2获取支付宝口碑信息List："+source);
        String result="";
        try {
            result= zfbKouBeiShopService.obtainShopList(source);
        } catch (Exception e) {
            LogHelper.error(e,"获取支付宝口碑信息异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("获取支付宝口碑信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/GetZfbShopInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getZfbShopInfo(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.2.3获取支付宝口碑信息："+source);
        String result="";
        try {
            result= zfbKouBeiShopService.obtainShop(source);
        } catch (Exception e) {
            LogHelper.error(e,"获取支付宝口碑信息异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("获取支付宝口碑信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/UpdateZfbShop", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String updateZfbShopInfo(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.2.4修改支付宝口碑信息:"+source);
        String result="";
        try {
            result= zfbKouBeiShopService.updateShop(source);
        } catch (Exception e) {
            LogHelper.error(e,"修改支付宝口碑信息异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("修改支付宝口碑信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/RefreshZfbSate", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String uploadZfbImgInfo(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.2.6刷新支付宝状态:"+source);
        String result="";
        try {
            result= zfbKouBeiShopService.refreshZfbSate(source);
        } catch (Exception e) {
            LogHelper.error(e,"刷新支付宝状态信息异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("刷新支付宝状态信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/ShopIsAuthorize", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String shopIsAuthorize(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.2.7商户是否授权token:"+source);
        String result="";
        try {
            result= zfbKouBeiShopService.shopIsAuthorize(source);
        } catch (Exception e) {
            LogHelper.error(e,"商户是否授权token异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("商户是否授权token信息:"+result);
        return result;
    }
    @ResponseBody
    @RequestMapping(value="/SaveZfbTokenInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String saveZfbTokenInfo(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("2.2.8存储支付宝token:"+source);
        String result="";
        try {
            result= zfbKouBeiShopService.saveZfbTokenInfo(source);
        } catch (Exception e) {
            LogHelper.error(e,"存储支付宝token异常！！！！",false);
            e.printStackTrace();
        }
        LogHelper.info("存储支付宝token信息:"+result);
        return result;
    }

}
