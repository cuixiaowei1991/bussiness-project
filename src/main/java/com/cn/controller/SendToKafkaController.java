package com.cn.controller;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.LogHelper;
import com.cn.entity.dto.AllpayAgentDto;
import com.cn.entity.dto.AllpayTerminalDto;
import com.cn.service.SendToKafkaService;
import com.cn.service.businessService.AllpayAgentService;
import com.cn.service.businessService.AllpayMerchantService;
import com.cn.service.businessService.AllpayStoreService;
import com.cn.service.businessService.TerminalInfoService;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Created by cuixiaowei on 2016/11/24.
 * 数据推送kafka
 */
@Controller
@RequestMapping("/sendToKafka")
public class SendToKafkaController {

    @Autowired
    private SendToKafkaService sendToKafkaService;
    @Autowired
    private TerminalInfoService terminalInfoService;
    @Autowired
    private AllpayStoreService allpayStoreService;
    @Autowired
    private AllpayMerchantService allpayMerchantService;

    @Autowired
    private AllpayAgentService allpayAgentServiceImpl;

    /**
     * 商户信息推送kafka
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/merchantToKafka",produces="application/json;charset=UTF-8")
    public String sentMerchantToKafka(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("商户id信息列表"+source);
        String result = null;
        JSONObject merchant_result = new JSONObject();
        int count=0;
        try {
            result = allpayMerchantService.obtainList(source);
            JSONObject merchant_jo = new JSONObject(result);
            JSONArray merchant_array=  merchant_jo.getJSONArray("lists");
            for(int i=0;i<merchant_array.length();i++)
            {
                JSONObject jo = merchant_array.getJSONObject(i);
                sendToKafkaService.sentMerchantToKafka(jo.getString("mhtId"),"");
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogHelper.info("总共推送："+count+" 条商户信息数据！");
        merchant_result.put(MsgAndCode.RSP_CODE,MsgAndCode.SUCCESS_CODE);
        merchant_result.put(MsgAndCode.RSP_DESC,MsgAndCode.SUCCESS_MESSAGE);
        merchant_result.put("count", count);
        return merchant_result.toString();


    }
    /**
     * 网点信息推送kafka
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/storeToKafka",produces="application/json;charset=UTF-8")
    public String sentWangDianToKafka(@RequestBody Map<String, Object> source)
    {
        LogHelper.info("网点id信息列表"+source.toString());
        String result=null;
        JSONObject wangdian_result = new JSONObject();
        int count=0;
        try {
            result = allpayStoreService.obtainList(source);
            JSONObject wangdian_jo = new JSONObject(result);
            JSONArray wangdian_array=  wangdian_jo.getJSONArray("lists");
            for(int i=0;i<wangdian_array.length();i++)
            {
                JSONObject jo = wangdian_array.getJSONObject(i);
                sendToKafkaService.sentWangDianToKafka(jo.getString("storeId"),"");
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogHelper.info("总共推送："+count+" 条网点信息数据！");
        wangdian_result.put(MsgAndCode.RSP_CODE,MsgAndCode.SUCCESS_CODE);
        wangdian_result.put(MsgAndCode.RSP_DESC,MsgAndCode.SUCCESS_MESSAGE);
        wangdian_result.put("count", count);
        return wangdian_result.toString();


    }
    /**
     * 渠道信息推送kafka
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/qvDaoToKafka",produces="application/json;charset=UTF-8")
    public void sentQvDaoToKafka(@RequestBody List qvDaos)
    {
        LogHelper.info("渠道id信息列表"+qvDaos.toString());
        int count=0;
        for(int i=0;i<qvDaos.size();i++)
        {
            Boolean result= null;
            try {
                result = sendToKafkaService.sentQvDaoToKafka(qvDaos.get(i).toString(),"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogHelper.info("渠道信息推送kafka结果："+result);
            count++;
        }
        LogHelper.info("总共推送："+count+" 条渠道信息数据！");
    }
    /**
     * 推送POS信息至kafka
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/posToKafka",produces="application/json;charset=UTF-8")
    public String sendApayPosToKafka(@RequestBody AllpayTerminalDto allpayTerminalDto)
    {
        LogHelper.info("2.4.1获取POS终端信息列表=============allpayTerminalDto=" + allpayTerminalDto.toString());
        String result = "";
        int count=0;
        JSONObject terminal_result = new JSONObject();
        try {
            result=terminalInfoService.obtainList(allpayTerminalDto);
            JSONObject terminal_jo = new JSONObject(result);
            JSONArray terminal_array=  terminal_jo.getJSONArray("lists");
            for(int i=0;i<terminal_array.length();i++)
            {
                JSONObject jo = terminal_array.getJSONObject(i);
                sendToKafkaService.sendApayPosToKafka(jo.getString("posId"),"");
                count++;
            }
            LogHelper.info("总共推送："+count+" 条POS信息数据！");

        } catch (Exception e) {
            e.printStackTrace();
        }
        terminal_result.put(MsgAndCode.RSP_CODE,MsgAndCode.SUCCESS_CODE);
        terminal_result.put(MsgAndCode.RSP_DESC,MsgAndCode.SUCCESS_MESSAGE);
        terminal_result.put("count",count);
        return terminal_result.toString();
        //String.valueOf(source.get("posList"));
    }
    /**
     * 推送代理信息至kafka
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/agentToKafka",produces="application/json;charset=UTF-8")
    public void sendApayagentToKafka(@RequestBody  AllpayAgentDto bean)
    {
        LogHelper.info("代理商 id信息列表"+bean.toString());
        JSONObject resultJO = new JSONObject();
        try {
            resultJO= allpayAgentServiceImpl.getList(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray agent_array=  resultJO.getJSONArray("lists");
        int count=0;
        for(int i=0;i<agent_array.length();i++)
        {
            Boolean result= null;
            JSONObject jo = agent_array.getJSONObject(i);
            try {
                result = sendToKafkaService.sendApayagentToKafka(jo.getString("agentId"),"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogHelper.info("代理商信息推送kafka结果："+result);
            count++;
        }
        LogHelper.info("总共推送："+count+" 条代理商信息数据！");
    }
    /**
     * 推送POS合作方信息至kafka
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/posParterToKafka",produces="application/json;charset=UTF-8")
    public void sendApayPOSParter(@RequestBody List posParters)
    {
        LogHelper.info("POS合作方id信息列表"+posParters.toString());
        int count=0;
        for(int i=0;i<posParters.size();i++)
        {
            Boolean result= null;
            try {
                result = sendToKafkaService.sendApayPOSParter(posParters.get(i).toString(),"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogHelper.info("POS合作方信息推送kafka结果："+result);
            count++;
        }
        LogHelper.info("总共推送："+count+" 条POS合作方信息数据！");
    }
    /**
     * 推送TMS信息至kafka
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/tmsMessageToKafka",produces="application/json;charset=UTF-8")
    public void sendApayTmsmessage(@RequestBody List tmss)
    {
        LogHelper.info("TMS id信息列表"+tmss.toString());
        int count=0;
        for(int i=0;i<tmss.size();i++)
        {
            Boolean result= null;
            try {
                result = sendToKafkaService.sendApayTmsmessage(tmss.get(i).toString(),"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogHelper.info("TMS信息推送kafka结果："+result);
            count++;
        }
        LogHelper.info("总共推送："+count+" 条TMS信息数据！");
    }

    @ResponseBody
    @RequestMapping(value="/sendWFTToKafka",method = RequestMethod.POST ,produces="application/json;charset=UTF-8")
    public String sendWFTToKafka(@RequestBody Map<String, Object> source)
    {
        String wftId=String.valueOf(source.get("wftId"));
        LogHelper.info("威富通主键id：" + wftId);
        JSONObject wftJo=new JSONObject();
        try {
            Boolean result= sendToKafkaService.sendWFTToKafka(wftId);
            if(result)
            {

                wftJo.put(MsgAndCode.RSP_CODE,"000");
                wftJo.put(MsgAndCode.RSP_DESC,"success");
            }
            else
            {
                wftJo.put(MsgAndCode.RSP_CODE,"999");
                wftJo.put(MsgAndCode.RSP_DESC,"推送kafka威富通信息失败");
            }
        } catch (Exception e) {
            LogHelper.error(e,"推送威富通kafka信息异常！！！",false);
            wftJo.put(MsgAndCode.RSP_CODE,"999");
            wftJo.put(MsgAndCode.RSP_DESC, "推送kafka威富通信息异常");
            e.printStackTrace();
        }
        return wftJo.toString();
    }


}
