package com.cn.service.impl.businessServiceImpl;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.common.WriteExcelFile;
import com.cn.dao.AllpayChannelSetDao;
import com.cn.dao.AllpayMerchantDao;
import com.cn.entity.dto.AllpayChannelSetDto;
import com.cn.entity.po.Allpay_ChannelSet;
import com.cn.entity.po.Allpay_Merchant;
import com.cn.service.businessService.AllpayChannelSetService;

import com.cn.service.impl.kafkaserviceimpl.SendToKafkaServiceImpl;
import org.apache.poi.hssf.record.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务签约管理业务层实现
 * Created by WangWenFang on 2016/12/7.
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class AllpayChannelSetServiceImpl implements AllpayChannelSetService {

    @Autowired
    private AllpayChannelSetDao allpayChannelSetDao;
    @Autowired
    private AllpayMerchantDao allpayMerchantDao;
    @Value("${excelpath}")
    private String excelpath;

    @Value("${outpath}")
    private String outpath;
    @Autowired
    private SendToKafkaServiceImpl sendToKafkaService;
    
    @Override
    public String obtainList(AllpayChannelSetDto allpayChannelSetDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        Integer curragePage = allpayChannelSetDto.getCurragePage();
        Integer pageSize = allpayChannelSetDto.getPageSize();
        if(null == curragePage || null == pageSize){ //
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00110);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00110);
            return resultJO.toString();
        }

        JSONArray array = new JSONArray();
        List<Map<String, Object>> merAndChanList = allpayChannelSetDao.obtainMerAndChanList(allpayChannelSetDto, curragePage-1, pageSize, true);  //商户id,渠道code集合
        int total = allpayChannelSetDao.count(allpayChannelSetDto);
        if(null != merAndChanList && merAndChanList.size() > 0){
            for(Map<String, Object> map : merAndChanList){
                JSONObject allpayChannelSetJO = new JSONObject();

                allpayChannelSetJO.put("businessCode", map.get("CHANNELSET_CHANNEL_CODE"));  //业务编号
                allpayChannelSetJO.put("merchantId", map.get("CHANNELSET_MERCHANT_ID"));  //商户id

                allpayChannelSetDto.setChannelCode("" + map.get("CHANNELSET_CHANNEL_CODE"));
                allpayChannelSetDto.setMerchantId(""+map.get("CHANNELSET_MERCHANT_ID"));
                List<Map<String, Object>> list = allpayChannelSetDao.obtainList(allpayChannelSetDto, null, null, false);
                if(null != list && list.size() > 0){
                    for(Map<String, Object> map1 : list){
                        allpayChannelSetJO.put("businessName", map1.get("CHANNELSET_PARAMETER_EXPAND") == null ? "":map1.get("CHANNELSET_PARAMETER_EXPAND"));	//业务名称
                        allpayChannelSetJO.put("merchantName", map1.get("MERCHANT_MERCHNAME"));  //商户名称
                        allpayChannelSetJO.put("businessCreateTime", map1.get("ALLPAY_CREATETIME") == null ? "":map1.get("ALLPAY_CREATETIME"));	//签约时间
                        if ("state".equals("" + map1.get("CHANNELSET_PARAMETER_NAME"))) {  //渠道状态	0未申请 1审核中 2通过 3驳回 4待审核
                            allpayChannelSetJO.put("businessChannelState", map1.get("CHANNELSET_PARAMETER_VALUE") == null ? "" : map1.get("CHANNELSET_PARAMETER_VALUE"));
                        }
                        if ("choose".equals("" + map1.get("CHANNELSET_PARAMETER_NAME"))) {  //状态	0:停用 1：启用
                            allpayChannelSetJO.put("businessState", map1.get("CHANNELSET_PARAMETER_VALUE") == null ? "" : map1.get("CHANNELSET_PARAMETER_VALUE"));
                        }
                    }
                }
                array.put(allpayChannelSetJO);
            }
        }
        resultJO.put("lists", array);
        resultJO.put("curragePage", allpayChannelSetDto.getCurragePage());  //当前页
        resultJO.put("pageSize", allpayChannelSetDto.getPageSize());  //每页显示记录条数
        resultJO.put("total", total);  //数据的总条数
        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);  //apay返回的状态码
        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);  //apay返回的状态码描述
        return resultJO.toString();
    }

    @Override
    public void exportList(AllpayChannelSetDto allpayChannelSetDto, HttpServletResponse response) throws Exception {
        List<String[]> array = new ArrayList<>();
        List<Map<String, Object>> merAndChanList = allpayChannelSetDao.obtainMerAndChanList(allpayChannelSetDto, null, null, false);  //商户id,渠道code集合
        if(null != merAndChanList && merAndChanList.size() > 0){
            for(int i=0, count=merAndChanList.size(); i<count; i++){
                Map<String, Object> map = merAndChanList.get(i);
                String[] terminal = new String[7];
                terminal[0] = String.valueOf(i + 1);

                allpayChannelSetDto.setChannelCode("" + map.get("CHANNELSET_CHANNEL_CODE"));
                allpayChannelSetDto.setMerchantId(""+map.get("CHANNELSET_MERCHANT_ID"));
                List<Map<String, Object>> list = allpayChannelSetDao.obtainList(allpayChannelSetDto, null, null, false);
                if(null != list && list.size() > 0){
                    for(Map<String, Object> map1 : list){
                        terminal[1] = CommonHelper.nullToString(map1.get("CHANNELSET_PARAMETER_EXPAND"));  //业务名称
                        terminal[2] = CommonHelper.nullToString(map1.get("MERCHANT_MERCHNAME"));  //受理商户
                        terminal[3] = CommonHelper.nullToString(map1.get("ALLPAY_CREATETIME"));  //申请时间

                        if ("state".equals("" + map1.get("CHANNELSET_PARAMETER_NAME"))) {
                            Integer state = Integer.parseInt(""+map1.get("CHANNELSET_PARAMETER_VALUE"));
                            if(0 == state){
                                terminal[4] = "未申请";   //渠道状态	0未申请 1审核中 2已通过 3被驳回 4待审核
                            }else if(1 == state){
                                terminal[4] = "审核中";
                            }else if(2 == state){
                                terminal[4] = "已通过";
                            }else if(3 == state){
                                terminal[4] = "被驳回";
                            }else if(4 == state){
                                terminal[4] = "待审核";
                            }else{
                                terminal[4] = "";
                            }
                        }
                        if ("choose".equals("" + map1.get("CHANNELSET_PARAMETER_NAME"))) {  //状态	0:停用 1：启用
                            Integer isStart = Integer.parseInt(""+map1.get("CHANNELSET_PARAMETER_VALUE"));
                            if(1 == isStart){
                                terminal[5] = "启用";  //状态	0:停用 1：启用
                            }else if(0 == isStart){
                                terminal[5] = "停用";
                            }else{
                                terminal[5] = "";
                            }
                        }
                    }
                }
                array.add(terminal);
            }
        }

        String[] sheet = {"序号", "业务名称", "受理商户", "签约时间", "渠道状态", "状态"};  //"归属渠道",
        WriteExcelFile.writeExcel(array, "ChannelSetList", sheet, "ChannelSetList", outpath, response);
    }

    @Override
    public String insert(AllpayChannelSetDto allpayChannelSetDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        List<Map<String, Object>> channelSetArray = allpayChannelSetDto.getChannelSetList();  //channelSetList

        if(null == channelSetArray || channelSetArray.size() == 0){  //业务签约配置信息
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00218);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00218);
            return resultJO.toString();
        }

        String merId = allpayChannelSetDto.getMerchantId();
        String channelCode = allpayChannelSetDto.getChannelCode();
        String channelName = allpayChannelSetDto.getChannelName();

        //插入业务签约信息
        for(int i=0, count = channelSetArray.size(); i<count; i++){
            Allpay_ChannelSet channelSet = new Allpay_ChannelSet();
            Map<String, Object> map= channelSetArray.get(i);
            if(null == map || map.size() == 0){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00218);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00218);
                return resultJO.toString();
            }

            channelSet.setChannelset_merchant_id(merId);  //商户id
            channelSet.setChannelset_channel_code(channelCode);  //渠道码（业务类型）
            channelSet.setChannelset_parameter_expand(channelName);  //渠道名称
            channelSet.setChannelset_parameter_name("" + map.get("parameterName"));	//参数名称
            channelSet.setChannelset_parameter_value("" + map.get("parameterValue")); //参数值
            channelSet.setChannelset_parameter_type(""+map.get("paramType")); //参数类型

            JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", allpayChannelSetDto.getUserNameFromBusCookie());
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");

            channelSet.setALLPAY_CREATER(userName);  //创建人
            channelSet.setALLPAY_CREATETIME(now);  //创建时间
            channelSet.setALLPAY_UPDATETIME(now);  //修改时间
            channelSet.setALLPAY_UPDATER(userName);
            channelSet.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
            channelSet.setALLPAY_LOGRECORD(record);

            boolean result = allpayChannelSetDao.insert(channelSet);
            if(!result){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
                resultJO.put(MsgAndCode.RSP_DESC, "新增业务签约信息失败！");
                return resultJO.toString();
            }

        }
        sendToKafkaService.sentQvDaoToKafka(merId,channelCode);
        //查询当前商户已开通业务渠道信息
        AllpayChannelSetDto channelSetDto = new AllpayChannelSetDto();
        channelSetDto.setMerchantId(allpayChannelSetDto.getMerchantId());
        List<Map<String, Object>> merAndChanList = allpayChannelSetDao.obtainMerAndChanList(channelSetDto, null, null, false);  //商户id,渠道code集合
        JSONArray array = new JSONArray("[]");
        if(null != merAndChanList && merAndChanList.size() > 0){
            for(Map<String, Object> map : merAndChanList){
                JSONObject object = new JSONObject();

                object.put("channelCode", map.get("CHANNELSET_CHANNEL_CODE"));  //业务（渠道）码

                channelSetDto.setChannelCode("" + map.get("CHANNELSET_CHANNEL_CODE"));
                channelSetDto.setMerchantId("" + map.get("CHANNELSET_MERCHANT_ID"));
                List<Map<String, Object>> list = allpayChannelSetDao.obtainList(channelSetDto, null, null, false);
                if(null != list && list.size() > 0){
                    for(Map<String, Object> map1 : list){
                        object.put("channelName", map1.get("CHANNELSET_PARAMETER_EXPAND") == null ? "":map1.get("CHANNELSET_PARAMETER_EXPAND"));	//业务名称
                        object.put("createTime", map1.get("ALLPAY_CREATETIME") == null ? "":CommonHelper.formatTime(map1.get("ALLPAY_CREATETIME"), "yyyy-MM-dd HH:mm:ss"));	//签约时间
                        if ("state".equals("" + map1.get("CHANNELSET_PARAMETER_NAME"))) {  //渠道状态	0未申请 1审核中 2通过 3驳回 4待审核
                            object.put("state", map1.get("CHANNELSET_PARAMETER_VALUE") == null ? "" : map1.get("CHANNELSET_PARAMETER_VALUE"));
                        }
                        if ("choose".equals("" + map1.get("CHANNELSET_PARAMETER_NAME"))) {  //状态	0:停用 1：启用
                            object.put("choose", map1.get("CHANNELSET_PARAMETER_VALUE") == null ? "" : map1.get("CHANNELSET_PARAMETER_VALUE"));
                        }
                    }
                }
                array.put(object);
            }
        }

        //插入商户已开通渠道信息
        allpayChannelSetDao.updateMerChnnel(merId, array.toString());
        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        return resultJO.toString();
    }

    @Override
    public String update(AllpayChannelSetDto allpayChannelSetDto) throws Exception {
        JSONObject resultJO = new JSONObject();

        JSONObject check = check(allpayChannelSetDto, 0);
        if(check.length() >= 2 ){
            return check.toString();
        }

        //先根据 商户id，渠道码（业务类型） 删除所有的业务签约信息
        boolean delResult = allpayChannelSetDao.removeByMerIdChanCode(allpayChannelSetDto.getMerchantId(), allpayChannelSetDto.getChannelCode());
        if(!delResult){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, "删除业务签约信息失败！");
            return resultJO.toString();
        }

        //再新增业务签约信息
        String result = insert(allpayChannelSetDto);

        return result;
    }

    @Override
    public String getByMerIdChanCode(AllpayChannelSetDto allpayChannelSetDto) throws Exception {
        JSONObject resultJO = new JSONObject();

        JSONObject check = check(allpayChannelSetDto, 1);
        if(check.length() >= 2 ){
            return check.toString();
        }

        String merchantId = allpayChannelSetDto.getMerchantId();
        String channelCode = allpayChannelSetDto.getChannelCode();
        String channelName = "";
        List<Map<String, Object>> list = (List<Map<String, Object>>)allpayChannelSetDao.getByMerIdChanCode(merchantId, channelCode);
        JSONArray jsonArray = new JSONArray();
        if(null != list && list.size() > 0 ){
            for(Map<String, Object> channelSet: list){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("parameterName", channelSet.get("CHANNELSET_PARAMETER_NAME"));
                jsonObject.put("parameterValue", channelSet.get("CHANNELSET_PARAMETER_VALUE"));
                jsonObject.put("paramType", channelSet.get("CHANNELSET_PARAMETER_TYPE"));
                jsonObject.put("channelSetId", channelSet.get("CHANNELSET_ID"));
                jsonArray.put(jsonObject);
            }
            channelName = ""+list.get(0).get("CHANNELSET_PARAMETER_EXPAND");
        }

        resultJO.put("channelSetList", jsonArray);  //channelSetList
        resultJO.put("merchantId", merchantId);  //商户id
        resultJO.put("channelCode", channelCode);  //渠道码（业务类型）
        resultJO.put("channelName", channelName);  //	渠道名称
        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        return resultJO.toString();
    }

    /**
     * 验证必填项
     * @param allpayChannelSetDto
     * @return
     */
    public JSONObject check(AllpayChannelSetDto allpayChannelSetDto, int flag){
        JSONObject resultJO = new JSONObject();
        if(CommonHelper.isEmpty(allpayChannelSetDto.getMerchantId())){  //商户id
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00216);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00216);
            return resultJO;
        }
        if(CommonHelper.isEmpty(allpayChannelSetDto.getChannelCode())){  //渠道码（业务类型）
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00217);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00217);
            return resultJO;
        }
        if(flag == 0){  //新增时验证
            if(CommonHelper.isEmpty(allpayChannelSetDto.getChannelName())){  //渠道名称
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00222);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00222);
                return resultJO;
            }
        }
        return resultJO;
    }

}
