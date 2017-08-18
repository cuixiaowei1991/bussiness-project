package com.cn.service.impl.businessServiceImpl;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.common.WriteExcelFile;
import com.cn.dao.*;
import com.cn.entity.dto.AllpayTerminalDto;
import com.cn.entity.po.Allpay_Terminal;
import com.cn.service.businessService.TerminalInfoService;
import com.cn.service.impl.kafkaserviceimpl.SendToKafkaServiceImpl;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
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
 * pos终端管理业务层实现
 * Created by WangWenFang on 2016/12/5.
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class TerminalInfoServiceImpl implements TerminalInfoService {

    @Autowired
    private TerminalDao terminalDao;

    @Autowired
    private AllpayMerchantDao allpayMerchantDao;

    @Autowired
    private AllpayStoreDao allpayStoreDao;

    @Autowired
    private AllpayPosParterDao allpayPosparterDaoImpl;

    @Autowired
    private agentDao agentDao;

    @Value("${excelpath}")
    private String excelpath;

    @Value("${outpath}")
    private String outpath;

    @Autowired
    private SendToKafkaServiceImpl sendToKafkaService;
    
    public String obtainList(AllpayTerminalDto allpayTerminalDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        if(null == allpayTerminalDto.getCurragePage() || null == allpayTerminalDto.getPageSize()){ //
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00110);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00110);
            return resultJO.toString();
        }

        JSONArray array = new JSONArray();
        List<Map<String, Object>> list = terminalDao.obtainList(allpayTerminalDto, allpayTerminalDto.getCurragePage() - 1, allpayTerminalDto.getPageSize(), false);
        int total = terminalDao.count(allpayTerminalDto);
        if(null != list && list.size() > 0){
            for(int i=0,count=list.size(); i<count; i++){
                JSONObject allpayTerminalJO = new JSONObject();
                Map<String, Object> map = list.get(i);

                allpayTerminalJO.put("posId", map.get("TERMINAL_POSID"));  //终端id
                allpayTerminalJO.put("merchantId", map.get("TERMINAL_MERCHANT_ID"));	//所属商户id
                allpayTerminalJO.put("merchantName", map.get("MERCHANT_MERCHNAME"));	//所属商户名称
                allpayTerminalJO.put("merchCode", map.get("TERMINAL_MERCHCODE"));	//商户编号
                allpayTerminalJO.put("storeId", map.get("TERMINAL_STORE_ID"));	//门店id
                allpayTerminalJO.put("storeName", CommonHelper.nullToString(map.get("STORE_SHOPSHORTNAME")));	//门店名称
                allpayTerminalJO.put("termCode", CommonHelper.nullToString(map.get("TERMINAL_TERMCODE")));	//终端号
                allpayTerminalJO.put("branchId", map.get("TERMINAL_BRANCHID"));	//所属分公司id
                allpayTerminalJO.put("branchName", CommonHelper.nullToString(map.get("ORGANIZATION_NAME")));//所属分公司名称
                allpayTerminalJO.put("posParterId", CommonHelper.nullToString(map.get("TERMINAL_POSPARTERID")));	//所属pos合作方id
                allpayTerminalJO.put("posParterName", CommonHelper.nullToString(map.get("SUP_PARTERALLNAME")));	  //所属pos合作方名称
                allpayTerminalJO.put("peijianPosParterId", map.get("TERMINAL_PEIJIANPOSPARTERID"));	//配件所属pos合作方id
                allpayTerminalJO.put("peijianPosParterName", CommonHelper.nullToString(map.get("POSPARTER_PARTERALLNAME")));  //配件所属pos合作方名称
                allpayTerminalJO.put("superTermCode", CommonHelper.nullToString(map.get("SUP_TERMCODE")));  //关联pos编号
                allpayTerminalJO.put("superMerchCode", CommonHelper.nullToString(map.get("SUP_MERCHCODE")));  //关联商户编号编号
                allpayTerminalJO.put("payChannel", map.get("TERMINAL_PAYCHANNEL"));  //应用渠道（系统）(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)
                allpayTerminalJO.put("terminalState", map.get("ALLPAY_ISSTART"));  //状态	0:停用 1：启用
                allpayTerminalJO.put("posType", map.get("TERMINAL_POSTYPE"));  //Pos类型	1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
                allpayTerminalJO.put("posCreateTime", map.get("ALLPAY_CREATETIME"));  //配置时间

                array.put(allpayTerminalJO);
            }
        }
        resultJO.put("lists", array);
        resultJO.put("curragePage", allpayTerminalDto.getCurragePage());  //当前页
        resultJO.put("pageSize", allpayTerminalDto.getPageSize());  //每页显示记录条数
        resultJO.put("total", total);  //数据的总条数
        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);  //apay返回的状态码
        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);  //apay返回的状态码描述
        return resultJO.toString();
    }

    public String insert(AllpayTerminalDto allpayTerminalDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        int size = allpayTerminalDto.getCodeCount();  //批量生成终端条数
        String merCode = null;
        if(!"2".equals(allpayTerminalDto.getPosType()) && CommonHelper.isEmpty("" + size)){  //软pos没有批量生成终端条数
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00203);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00203);
            return resultJO.toString();
        }

        if("2".equals(allpayTerminalDto.getPosType())){
            List<Map<String, Object>> listStore = (List<Map<String, Object>>)allpayStoreDao.checkStore(allpayTerminalDto.getMerchantId(), allpayTerminalDto.getStoreId());
            if(null == listStore || listStore.size() == 0){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00418);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00418);
                return resultJO.toString();
            }
            merCode = ""+listStore.get(0).get("STORE_SHOPNUMBER");   //获取门店的15位编号
        }else{
            merCode = allpayTerminalDto.getMerchaCode();
        }

        JSONObject check = check(allpayTerminalDto);
        if(null != check){
            return check.toString();
        }

        for(int i=0; i<size; i++){
            Allpay_Terminal allpayTerminal = new Allpay_Terminal();
            String termCode = allpayTerminalDto.getTermCode();
            if(i != 0){
                termCode = String.valueOf(Integer.parseInt(termCode)+1);
            }

            if(termCode.length() != 8){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
                resultJO.put(MsgAndCode.RSP_DESC, termCode+"当前终端编号长度不正确");
                return resultJO.toString();
            }
            //判断商户编号+终端编号是否存在
            allpayTerminalDto.setTermCode(termCode);
            boolean boo = terminalDao.obtainByParamCode(allpayTerminalDto);
            if(boo){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00417);
                resultJO.put(MsgAndCode.RSP_DESC, termCode+", "+merCode+" "+MsgAndCode.MESSAGE_00417);
                return resultJO.toString();
            }
            allpayTerminal.setTerminal_store_id(allpayTerminalDto.getStoreId());  //门店id
            allpayTerminal.setTerminal_merchant_id(allpayTerminalDto.getMerchantId());	//所属商户id
            allpayTerminal.setTerminal_termcode(termCode);	//终端编号 8位
            allpayTerminal.setTerminal_postype(Integer.parseInt(allpayTerminalDto.getPosType()));	//Pos类型		1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
            allpayTerminal.setTerminal_paychannel(allpayTerminalDto.getPayChannel());	//应用渠道（系统）(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)
            allpayTerminal.setTerminal_branchid(allpayTerminalDto.getBranchId());	//Pos所属分公司id
            allpayTerminal.setTerminal_posparterid(allpayTerminalDto.getPosParterId());	//所属pos合作方id
            allpayTerminal.setTerminal_peijianposparterid(allpayTerminalDto.getPeiJianPosParterId());	  //配件所属pos合作方Id
            allpayTerminal.setTerminal_merchcode(merCode);	//商户编号（业务编号）
            allpayTerminal.setALLPAY_ISSTART(allpayTerminalDto.getPosStatus());	     //状态 是否启用	0:停用 1：启用
            if(!CommonHelper.isEmpty(allpayTerminalDto.getSuperTermCode())){
                allpayTerminal.setTerminal_supertermcode(allpayTerminalDto.getSuperTermCode());	//关联pos
            }
            if("2".equals(allpayTerminalDto.getPosType())){
                allpayTerminal.setTerminal_shopuserid(allpayTerminalDto.getUserId());
                allpayTerminal.setTerminal_usertype(Integer.parseInt(allpayTerminalDto.getUserType()));	      //角色	String(0网点管理,1网点业务,2商户管理)
            }

            JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", allpayTerminalDto.getUserNameFromBusCookie());
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");

            allpayTerminal.setALLPAY_CREATER(userName);  //创建人
            allpayTerminal.setALLPAY_CREATETIME(now);  //创建时间
            allpayTerminal.setALLPAY_UPDATETIME(now);  //修改时间
            allpayTerminal.setALLPAY_UPDATER(userName);
            allpayTerminal.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
            allpayTerminal.setALLPAY_LOGRECORD(record);
            allpayTerminal.setTerminal_permission(null);
            boolean result = terminalDao.insert(allpayTerminal);
            if(result){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
                sendToKafkaService.sendApayPosToKafka(allpayTerminal.getTerminal_posid(), "");
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
                resultJO.put(MsgAndCode.RSP_DESC, "新增pos终端信息异常！");
            }
        }

        return resultJO.toString();
    }

    public String update(AllpayTerminalDto allpayTerminalDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        String posId = allpayTerminalDto.getPosId();  //pos终端Id
        if(CommonHelper.isEmpty(posId)){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00212);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00212);
            return resultJO.toString();
        }

        JSONObject check = check(allpayTerminalDto);  //验证其他必填字段
        if(null != check){
            return check.toString();
        }

        Allpay_Terminal allpayTerminal = getById(Allpay_Terminal.class, posId);
        if(null == allpayTerminal){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00213);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00213);
            return resultJO.toString();
        }

        //判断 商户编号+终端编号 是否存在
        String termCode = allpayTerminalDto.getTermCode();
        String merCode = "";
        if("2".equals(allpayTerminalDto.getPosType())){
            List<Map<String, Object>> listStore = (List<Map<String, Object>>)allpayStoreDao.checkStore(allpayTerminalDto.getMerchantId(), allpayTerminalDto.getStoreId());
            if(null == listStore || listStore.size() == 0){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00418);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00418);
                return resultJO.toString();
            }
            merCode = ""+listStore.get(0).get("STORE_SHOPNUMBER");   //获取门店的15位编号
        }else{
            merCode = allpayTerminalDto.getMerchaCode();
        }
        allpayTerminalDto.setMerchaCode(merCode);
        boolean boo = terminalDao.obtainByParamCode(allpayTerminalDto);
        if(boo){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00417);
            resultJO.put(MsgAndCode.RSP_DESC, termCode+", "+merCode+" "+MsgAndCode.MESSAGE_00417);
            return resultJO.toString();
        }

        allpayTerminal.setTerminal_store_id(allpayTerminalDto.getStoreId());  //门店id
        allpayTerminal.setTerminal_merchant_id(allpayTerminalDto.getMerchantId());	//所属商户id
        allpayTerminal.setTerminal_termcode(termCode);	//终端编号 8位
        allpayTerminal.setTerminal_postype(Integer.parseInt(allpayTerminalDto.getPosType()));	//Pos类型		1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
        allpayTerminal.setTerminal_paychannel(allpayTerminalDto.getPayChannel());	//应用渠道（系统）(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)
        allpayTerminal.setTerminal_branchid(allpayTerminalDto.getBranchId());	//Pos所属分公司id
        allpayTerminal.setTerminal_posparterid(allpayTerminalDto.getPosParterId());	//所属pos合作方id
        allpayTerminal.setTerminal_peijianposparterid(allpayTerminalDto.getPeiJianPosParterId());	  //配件所属pos合作方Id
        allpayTerminal.setTerminal_usertype(Integer.parseInt(allpayTerminalDto.getUserType()));	      //角色	String(0网点管理,1网点业务,2商户管理)
        allpayTerminal.setTerminal_merchcode(merCode);	//商户编号（业务编号）
        allpayTerminal.setALLPAY_ISSTART(allpayTerminalDto.getPosStatus());	     //状态 是否启用	0:停用 1：启用
        if(!CommonHelper.isEmpty(allpayTerminalDto.getSuperTermCode())){
            allpayTerminal.setTerminal_supertermcode(allpayTerminalDto.getSuperTermCode());	//关联pos
        }

        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, allpayTerminal.getALLPAY_LOGRECORD(), allpayTerminalDto.getUserNameFromBusCookie());
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");

        allpayTerminal.setALLPAY_UPDATETIME(now);  //修改时间
        allpayTerminal.setALLPAY_UPDATER(userName);
        allpayTerminal.setALLPAY_LOGRECORD(record);
        boolean result = terminalDao.update(allpayTerminal);
        if(result){
            sendToKafkaService.sendApayPosToKafka(posId,"");
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, "修改pos终端信息异常！");
        }
        return resultJO.toString();
    }

    public <T> T getById(Class<T> clazz, String posId) throws Exception {
        return terminalDao.getById(clazz, posId);
    }

    public String getById(AllpayTerminalDto allpayTerminalDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        String posId = allpayTerminalDto.getPosId();  //pos终端Id
        if(CommonHelper.isEmpty(posId)){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00212);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00212);
            return resultJO.toString();
        }
        Allpay_Terminal allpayTerminal = getById(Allpay_Terminal.class, posId);
        if(null == allpayTerminal){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00213);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00213);
            return resultJO.toString();
        }

        //查询商户名称
        String merchantId = allpayTerminal.getTerminal_merchant_id();
        List<Map<String, Object>> list = (List<Map<String, Object>>)terminalDao.getMerNameBySql(merchantId);
        if(null == list || list.size() == 0){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00221);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00221);
            return resultJO.toString();
        }
        String merchantName = ""+list.get(0).get("MERCHANT_MERCHNAME");

        //返回前端信息
        resultJO.put("storeId", allpayTerminal.getTerminal_store_id());  //门店id
        resultJO.put("posId", allpayTerminal.getTerminal_posid());    //posid
        resultJO.put("merchantId", merchantId);    //所属商户id
        resultJO.put("merchantName", merchantName);	//所属商户名称
        resultJO.put("termCode", allpayTerminal.getTerminal_termcode());    //终端编号  8位
        resultJO.put("posType", allpayTerminal.getTerminal_postype());    //Pos类型		1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
        resultJO.put("payChannel", allpayTerminal.getTerminal_paychannel());	//应用渠道（系统）(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)
        resultJO.put("branchId", CommonHelper.nullToString(allpayTerminal.getTerminal_branchid()));  //Pos所属分公司id
        resultJO.put("posParterId", CommonHelper.nullToString(allpayTerminal.getTerminal_posparterid()));  //所属pos合作方id
        resultJO.put("peiJianPosParterId", CommonHelper.nullToString(allpayTerminal.getTerminal_peijianposparterid()));  //配件所属pos合作方Id
        resultJO.put("userType", allpayTerminal.getTerminal_usertype());  //角色	String(0网点管理,1网点业务,2商户管理)
        resultJO.put("merchaCode", allpayTerminal.getTerminal_merchcode());  //商户编号（业务编号）
        resultJO.put("posStatus", allpayTerminal.getALLPAY_ISSTART());  //状态 是否启用	0:停用 1：启用
        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);   //apay返回的状态码	000 为正常返回,其他为异常返回
        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE); //apay返回的状态码描述	异常返回时的异常说明
        return resultJO.toString();
    }

    @Override
    public String insertPosMenuSetInfo(AllpayTerminalDto allpayTerminalDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        String posType = allpayTerminalDto.getPosType();
        if(CommonHelper.isEmpty(posType)){  //pos类型
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00204);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00204);
            return resultJO.toString();
        }
        if(CommonHelper.isEmpty(allpayTerminalDto.getLimitConfiguration())){  //权限配置方案
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00220);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00220);
            return resultJO.toString();
        }

        if(posType.contains(",")){
            String[] posTypes = posType.split(",");
            posType = "";
            for(int i=0;i<posTypes.length;i++){
                posType += "'"+posTypes[i]+"'";
                if(i != posTypes.length-1 ){
                    posType += ",";
                }
            }
        }

        Allpay_Terminal terminal = new Allpay_Terminal();
        terminal.setTerminal_merchant_id(allpayTerminalDto.getMerchantId());
        terminal.setTerminal_store_id(allpayTerminalDto.getStoreId());
        terminal.setTerminal_permission(allpayTerminalDto.getLimitConfiguration());

        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_MENUSET, terminal.getALLPAY_LOGRECORD(), allpayTerminalDto.getUserNameFromBusCookie());
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");

        terminal.setALLPAY_UPDATETIME(now);  //修改时间
        terminal.setALLPAY_UPDATER(userName);
        terminal.setALLPAY_LOGRECORD(record);
        boolean result = terminalDao.insertPosMenuSetInfo(terminal, posType);
        if(result){
            List<Allpay_Terminal> list= (List<Allpay_Terminal>) terminalDao.getTerminalBySql(allpayTerminalDto);
            if(list!=null && list.size()>0)
            {
                for (int i=0;i<list.size();i++)
                {
                    sendToKafkaService.sendApayPosToKafka(list.get(i).getTerminal_posid().toString(),"");
                }
            }

            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00213);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00213);
            return resultJO.toString();
        }
        return resultJO.toString();
    }

    @Override
    public void exportTerminalList(AllpayTerminalDto allpayTerminalDto,HttpServletResponse response) throws Exception {
        List<String[]> array = new ArrayList<>();
        List<Map<String, Object>> list = terminalDao.obtainList(allpayTerminalDto, null, null, true);
        if(null != list && list.size() > 0){
            Map<String, Object> map = null;
            String[] terminal = null;
            List<Map<String,Object>> posTypeList = null;
            List<Map<String,Object>> chanelList = null;
            String[] payChannel = null;
            String posType = null;
            String payChannels = null;
            Integer isStrart = null;
            for(int i=0, count=list.size(); i<count; i++){
                map = list.get(i);
                terminal = new String[10];
                terminal[0] = String.valueOf(i + 1);
                terminal[1] = "";
                if(!CommonHelper.isNullOrEmpty(map.get("TERMINAL_MERCHCODE"))){
                	terminal[1] = map.get("TERMINAL_MERCHCODE").toString();
                }
                terminal[2] = "";
                if(!CommonHelper.isNullOrEmpty(map.get("TERMINAL_TERMCODE"))){
                	terminal[2] = map.get("TERMINAL_TERMCODE").toString();
                }
                posType = map.get("TERMINAL_POSTYPE") == null ? "":map.get("TERMINAL_POSTYPE").toString();
                payChannels = map.get("TERMINAL_PAYCHANNEL") == null ? "":map.get("TERMINAL_PAYCHANNEL").toString();

                posTypeList = terminalDao.selectDataDictionary("posType", posType);
                if(null == posTypeList || posTypeList.size() == 0){
                    terminal[3] = "";
                }else{
                    terminal[3] = posTypeList.get(0).get("DATA_NAME").toString();  //Pos类型	1：传统Pos  2:软Pos,3虚拟Pos 4:PCPos
                }

                payChannel = null;
                if(payChannels.contains(",")){
                    payChannel = payChannels.split(",");
                }
                if(null != payChannel){  //多个应用系统 (1-allpay,2-浦发，3-荣邦，4-银盛)
                    for(String s : payChannel){
                        chanelList = terminalDao.selectDataDictionary("applicationSystem", s);
                        terminal[4] = "";
                        if(null == chanelList || chanelList.size() == 0){
                            terminal[4] = "";
                        }else{
                            terminal[4] += chanelList.get(0).get("DATA_NAME")+",";
                        }
                    }
                    terminal[4] = terminal[4].substring(0, terminal[4].lastIndexOf(","));
                }else{  //单个应用系统, (1-allpay,2-浦发，3-荣邦，4-银盛)(单选)
                    chanelList = terminalDao.selectDataDictionary("applicationSystem", payChannels);
                    if(null == chanelList || chanelList.size() == 0){
                        terminal[4] = "";
                    }else{
                        terminal[4] = chanelList.get(0).get("DATA_NAME").toString();
                    }
                }

                if(!CommonHelper.isNullOrEmpty(map.get("STORE_SHOPNAME"))){
                    terminal[5] = map.get("STORE_SHOPNAME").toString();  //部署门店
                }else{
                    terminal[5] = "";
                }

                if(null != map.get("TERMINAL_SUPERTERMCODE") && null != map.get("SUP_MERCHCODE")){
                    terminal[6] = map.get("SUP_MERCHCODE").toString();  //关联终端信息
                }else{
                    terminal[6] = "";
                }
                if(null != map.get("TERMINAL_SUPERTERMCODE") && null != map.get("SUP_TERMCODE")){
                    terminal[7] = map.get("SUP_TERMCODE").toString();
                }else{
                    terminal[7] = "";
                }

                isStrart = Integer.parseInt(map.get("ALLPAY_ISSTART").toString());
                if(1 == isStrart){  //状态	0:停用 1：启用
                    terminal[8] = "启用";
                }else if(0 == isStrart){
                    terminal[8] = "停用";
                }else {
                    terminal[8] = "";
                }
                if(!CommonHelper.isNullOrEmpty(map.get("ALLPAY_CREATETIME"))){
                    terminal[9] = CommonHelper.formatTime(map.get("ALLPAY_CREATETIME"), "yyyy-MM-dd HH:mm:ss");  //配置时间
                }else{
                    terminal[9]="";
                }
                array.add(terminal);
            }
        }
        String[] sheet = {"序号", "商户号", "终端号", "POS类型", "应用系统", "部署门店", "被关联商户号", "被关联终端号","状态", "配置时间"};
        WriteExcelFile.writeExcel(array, "TerminalList", sheet, "TerminalList", outpath, response);
    }

    /**
     * 删除pos终端信息
     * @param allpayTerminalDto
     * @return
     * @throws Exception
     */
    @Override
    public String delete(AllpayTerminalDto allpayTerminalDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        String posId = allpayTerminalDto.getPosId();  //pos终端Id
        if(CommonHelper.isEmpty(posId)){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00212);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00212);
            return resultJO.toString();
        }

        Allpay_Terminal allpayTerminal = getById(Allpay_Terminal.class, posId);
        if(null == allpayTerminal){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00213);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00213);
            return resultJO.toString();
        }

        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, allpayTerminal.getALLPAY_LOGRECORD(), allpayTerminalDto.getUserNameFromBusCookie());
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");

        allpayTerminal.setALLPAY_UPDATETIME(now);  //修改时间
        allpayTerminal.setALLPAY_UPDATER(userName);
        allpayTerminal.setALLPAY_LOGICDEL("2");  //删除
        allpayTerminal.setALLPAY_LOGRECORD(record);
        boolean result = terminalDao.update(allpayTerminal);
        if(result){
            sendToKafkaService.sendApayPosToKafka(posId,"del");
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, "删除pos终端信息异常！");
        }
        return resultJO.toString();
    }

    @Override
    public String obtainByMerCode(AllpayTerminalDto allpayTerminalDto) throws Exception {
        if(CommonHelper.isNullOrEmpty(allpayTerminalDto.getMerchaCode())) {
            return returnMissParamMessage(MsgAndCode.CODE_00210, MsgAndCode.MESSAGE_00210);
        }
        boolean exist = terminalDao.obtainByParamCode(allpayTerminalDto);
        JSONObject jsonObject = new JSONObject();
        if(exist){
            jsonObject.put("exist", "1");
        }else{
            jsonObject.put("exist", "0");
        }
        return returnSuccessMessage(jsonObject);
    }

    /**
     * POS关联批量导入
     * @param source
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject importRelatePos(Map<String, Object> source) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(source!=null){
            String superMerchantCode=""+source.get("superMerchantCode");  // 被关联的POS商户号
            String superTermCode=""+source.get("superTermCode");  // 被关联的终端号码
            String merchantCode=""+source.get("merchantCode");  // POS商户号
            String termCode=""+source.get("termCode");  // 终端号码
            String posType=""+source.get("posType");  // 终端类型(默认1, 1:传统pos,4:pc版pos)
            String payChannel=""+source.get("payChannel");  // 终端应用系统(默认1(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)

            if(CommonHelper.isEmpty(superMerchantCode)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
                resultJO.put(MsgAndCode.RSP_DESC, "被关联的POS商户号不能为空！");
                return resultJO;
            }
            if(CommonHelper.isEmpty(superTermCode)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
                resultJO.put(MsgAndCode.RSP_DESC, "被关联的终端号码不能为空！");
                return resultJO;
            }
            if(CommonHelper.isEmpty(merchantCode)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00210);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00210);
                return resultJO;
            }
            if(CommonHelper.isEmpty(termCode)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00202);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00202);
                return resultJO;
            }
            if(merchantCode.length() != 15){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00406);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00406);
                return resultJO;
            }
            if(termCode.length() != 8){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00208);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00208);
                return resultJO;
            }
            if(CommonHelper.isEmpty(posType)){
                posType = "1";
            }else{
                if(!"1".equals(posType) && !"4".equals(posType)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00206);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00206);
                    return resultJO;
                }
            }
            if(CommonHelper.isEmpty(payChannel)){
                payChannel = "1";
            }else{  //1-allpay,2-浦发，3-荣邦，4-银盛
                List<Map<String,Object>> chanelList = terminalDao.selectDataDictionary("applicationSystem", payChannel);
                if(null == chanelList || chanelList.size() == 0){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00207);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00207);
                    return resultJO;
                }
            }

            //判断被关联pos商户号和被关联终端号是否存在
            AllpayTerminalDto terminalDto = new AllpayTerminalDto();
            terminalDto.setMerchaCode(superMerchantCode);
            terminalDto.setTermCode(superTermCode);
            List<Allpay_Terminal> list= (List<Allpay_Terminal>)terminalDao.getTerminalBySql(terminalDto);
            if(null != list && list.size() > 0){
                //判断被关联pos是否是传统pos或pc pos, 传统pos1 软pos2 虚拟pos3 PCpos4
                int pt = list.get(0).getTerminal_postype();
                if(pt == 2 || pt == 3){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00413);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00413);
                    return resultJO;
                }
                //判断pos商户号+终端号是否存在
                AllpayTerminalDto terminal = new AllpayTerminalDto();
                terminal.setMerchaCode(merchantCode);
                terminal.setTermCode(termCode);
                boolean boo = terminalDao.obtainByParamCode(terminal);
                if(boo){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00417);
                    resultJO.put(MsgAndCode.RSP_DESC, merchantCode+", "+termCode+" "+MsgAndCode.MESSAGE_00417);
                    return resultJO;
                }
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00213);
                resultJO.put(MsgAndCode.RSP_DESC, "被关联的"+MsgAndCode.MESSAGE_00213);
                return resultJO;
            }

            //插入pos终端信息
            Allpay_Terminal supTerminal = list.get(0);
            Allpay_Terminal terminal = new Allpay_Terminal();
            terminal.setTerminal_merchcode(merchantCode);	//商户编号  （shopNumber）15位
            terminal.setTerminal_store_id("" + supTerminal.getTerminal_store_id());	//门店ID
            terminal.setTerminal_termcode(termCode);	//终端编号 8位
            terminal.setTerminal_postype(Integer.parseInt(posType));    //1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
            terminal.setTerminal_branchid("" + supTerminal.getTerminal_branchid());    //pos所属分公司ID
            terminal.setTerminal_agentid("" + supTerminal.getTerminal_agentid());      //pos所属代理
            terminal.setTerminal_posparterid("" + supTerminal.getTerminal_posparterid());    //pos所属pos合作方
            terminal.setTerminal_peijianposparterid("" + supTerminal.getTerminal_peijianposparterid());    //pos配件所属pos合作方
            terminal.setTerminal_merchant_id("" + supTerminal.getTerminal_merchant_id());	//商户ID
            terminal.setTerminal_supertermcode("" + supTerminal.getTerminal_posid());  //关联pos
            terminal.setTerminal_paychannel(payChannel);	//终端应用系统(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)

            JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_BULK_IMPORT, "", ""+source.get("userNameFromBusCookie"));
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");

            terminal.setALLPAY_CREATER(userName);
            terminal.setALLPAY_CREATETIME(now);
            terminal.setALLPAY_UPDATETIME(now);  //修改时间
            terminal.setALLPAY_LOGICDEL("1");
            terminal.setALLPAY_LOGRECORD(record);
            terminal.setALLPAY_ISSTART(1);  //启用
            terminal.setTerminal_permission(null);
            boolean boo = terminalDao.insert(terminal);
            if(!boo){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
                resultJO.put(MsgAndCode.RSP_DESC, "批量导入关联pos终端信息失败！");
                return resultJO;
            }
            sendToKafkaService.sendApayPosToKafka(terminal.getTerminal_posid(),"");
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            resultJO.put("merchantCode", merchantCode);	//商户号15位
            resultJO.put("termCode", termCode);	//终端号
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    /**
     * POS创建批量导入
     * @param source
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject importPos(Map<String, Object> source) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(source!=null){
            String merchantName=""+source.get("merchantName");  //商户名称(必填)
            String storeName=""+source.get("storeName");  //门店名称(必填)
            String branchName=""+source.get("branchName");  //终端所属分公司(默认商户分公司)
            String posParterName=""+source.get("posParterName");  //归属(POS合作方全称)
            String peiJianPosParterName=""+source.get("peiJianPosParterName");  //配件归属(POS合作方全称)
            String merchantCode=""+source.get("merchantCode");   //POS商户号(必填)可以为空系统生成
            String termCode=""+source.get("termCode");  //终端号(必填)可以为空系统生成
            String posType=""+source.get("posType");  //POS类型(1：传统POS；4：PCPOS)
            String payChannel=""+source.get("payChannel");  //应用系统(默认1:allpay、2:浦发、3:荣邦、4:银盛)

            if(CommonHelper.isEmpty(merchantName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00400);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00400);
                return resultJO;
            }
            if(CommonHelper.isEmpty(storeName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00405);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00405);
                return resultJO;
            }
            if(CommonHelper.isEmpty(merchantCode)){
                merchantCode = allpayMerchantDao.getPosMerCode(15);  //随机生成15位不重复的POS商户号
            }else{
                if(merchantCode.length() != 15){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00406);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00406);
                    return resultJO;
                }
            }
            if(CommonHelper.isEmpty(termCode)) {
                termCode = terminalDao.getTermCode(8);  //随机生成8位不重复的终端编号
            }else{
                if(termCode.length() != 8){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00208);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00208);
                    return resultJO;
                }
            }
            if(CommonHelper.isEmpty(posType)){
                posType = "1";
            }else{
                if(!"1".equals(posType) && !"4".equals(posType)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00206);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00206);
                    return resultJO;
                }
            }
            if(CommonHelper.isEmpty(payChannel)){
                payChannel = "1";
            }else{  //1-allpay,2-浦发，3-荣邦，4-银盛
                List<Map<String,Object>> chanelList = terminalDao.selectDataDictionary("applicationSystem", payChannel);
                if(null == chanelList || chanelList.size() == 0){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00207);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00207);
                    return resultJO;
                }
            }

            List<Map<String, Object>> listStore = null;  //门店列表
            //判断商户名称是否存在
            List<Map<String, Object>> list= (List<Map<String, Object>>)allpayMerchantDao.checkMerName(merchantName,null);
            if(null != list && list.size() > 0){
                //判断该商户下门店名称是否存在
                listStore = (List<Map<String, Object>>)allpayStoreDao.checkStore(merchantName, storeName);
                if(null != listStore && listStore.size() > 0){
                    //判断pos商户号+终端号是否存在
                    AllpayTerminalDto terminalDto = new AllpayTerminalDto();
                    terminalDto.setTermCode(termCode);
                    terminalDto.setMerchaCode(merchantCode);
                    boolean boo = terminalDao.obtainByParamCode(terminalDto);
                    if(boo){
                        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00417);
                        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00417);
                        return resultJO;
                    }
                }else{
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00418);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00418);
                    return resultJO;
                }
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00221);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00221);
                return resultJO;
            }

            Map<String, Object> map = list.get(0);
            String branchId = "";  //分公司id
            if(CommonHelper.isEmpty(branchName)){
                branchId = ""+map.get("MERCHANT_BRANCHCOMPANYID");
            }else{
                //查询分公司id
                List<Map<String, Object>> branchs =(List<Map<String, Object>>)agentDao.selectBranch(branchName);
                LogHelper.debug("分公司内容：" + branchName + "分公司个数：" + branchs.size());
                if(branchs!=null && branchs.size()>0){
                    branchId=""+branchs.get(0).get("ORGANIZATION_ID");
                }else{
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00407);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00407);
                    return resultJO;
                }
            }

            //查询终端所属pos合作方id
            String posParterId = "";
            if(!CommonHelper.isEmpty(posParterName)){
                List<Map<String, Object>> listposPart = (List<Map<String, Object>>)allpayPosparterDaoImpl.getPosPArterByParterId(null, posParterName);
                if(null == listposPart || listposPart.size() ==0){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00340);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00340);
                    return resultJO;
                }
                posParterId = ""+listposPart.get(0).get("POSPARTER_ID");
            }

            //查询配件所属pos合作方id
            String peiJianPosParterId = "";
            if(!CommonHelper.isEmpty(peiJianPosParterName)){
                List<Map<String, Object>> list1 = (List<Map<String, Object>>)allpayPosparterDaoImpl.getPosPArterByParterId(null, peiJianPosParterName);
                if(null == list1 || list1.size() ==0){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00341);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00341);
                    return resultJO;
                }
                peiJianPosParterId = ""+list1.get(0).get("POSPARTER_ID");
            }

            //插入pos终端信息
            Allpay_Terminal terminal = new Allpay_Terminal();
            terminal.setTerminal_merchcode(merchantCode);	//商户编号  （shopNumber）15位
            terminal.setTerminal_store_id("" + listStore.get(0).get("STORE_ID"));	//门店ID
            terminal.setTerminal_termcode(termCode);	//终端编号 8位
            terminal.setTerminal_postype(Integer.parseInt(posType));	//1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
            terminal.setTerminal_branchid(branchId);	//pos所属分公司ID
//            terminal.setTerminal_agentid(supTerminal.getTerminal_agentid());	  //pos所属代理
            terminal.setTerminal_posparterid(posParterId);	//pos所属pos合作方
            terminal.setTerminal_peijianposparterid(peiJianPosParterId);	//pos配件所属pos合作方
            terminal.setTerminal_merchant_id("" + map.get("MERCHANT_ID"));	//商户ID
            terminal.setTerminal_paychannel(payChannel);	//终端应用系统(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)

            JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_BULK_IMPORT, "", ""+source.get("userNameFromBusCookie"));
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");

            terminal.setALLPAY_CREATER(userName);
            terminal.setALLPAY_CREATETIME(now);
            terminal.setALLPAY_UPDATETIME(now);  //修改时间
            terminal.setALLPAY_LOGICDEL("1");
            terminal.setALLPAY_LOGRECORD(record);
            terminal.setALLPAY_ISSTART(1);  //启用
            terminal.setTerminal_permission(null);
            boolean boo = terminalDao.insert(terminal);
            if(!boo){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00410);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00410);
                return resultJO;
            }
            sendToKafkaService.sendApayPosToKafka(terminal.getTerminal_posid(),"");
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            resultJO.put("merchantCode", merchantCode);	//商户号15位
            resultJO.put("termCode", termCode);	//终端号
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    /**
     * 软pos批量导入
     * @param source
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject importSoftPos(Map<String, Object> source) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(source!=null){
            String merchantName=""+source.get("merchantName");  //商户名称(必填)
            String storeName=""+source.get("storeName");  //门店名称(必填)
            String branchName=""+source.get("branchName");  //分公司名称
            String userType=""+source.get("userType");  //商户类型
            String userPhone=""+source.get("userPhone");   //管理员手机
            String agentName=""+source.get("agentName");  //代理商名称
            String shopuserId = ""+source.get("shopuserId");  //软POS对应人员id

            if(CommonHelper.isEmpty(merchantName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00400);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00400);
                return resultJO;
            }
            if(CommonHelper.isEmpty(branchName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00402);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00402);
                return resultJO;
            }
            if(CommonHelper.isEmpty(agentName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00301);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00301);
                return resultJO;
            }
            if(CommonHelper.isEmpty(storeName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00405);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00405);
                return resultJO;
            }
            if(CommonHelper.isEmpty(userPhone)) {
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00215);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00215);
                return resultJO;
            }
            if(CommonHelper.isEmpty(userType)) {
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00214);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00214);
                return resultJO;
            }
            if(CommonHelper.isEmpty(shopuserId)) {
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00420);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00420);
                return resultJO;
            }

            String termCode = null;
            String merchantCode = null;
            List<Map<String, Object>> listStore = null;  //门店列表
            //判断商户名称是否存在
            List<Map<String, Object>> list= (List<Map<String, Object>>)allpayMerchantDao.checkMerName(merchantName,null);
            if(null != list && list.size() > 0){
                //判断该商户下门店名称是否存在
                listStore = (List<Map<String, Object>>)allpayStoreDao.checkStore(merchantName, storeName);
                if(null == listStore || listStore.size() == 0){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00418);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00418);
                    return resultJO;
                }

                termCode = userPhone.substring(3);  //截取手机号后8位为终端编号
                merchantCode = ""+listStore.get(0).get("STORE_SHOPNUMBER");   //获取门店的15位编号
                //判断POS商户号+终端号是否存在
                AllpayTerminalDto terminalDto = new AllpayTerminalDto();
                terminalDto.setTermCode(termCode);
                terminalDto.setMerchaCode(merchantCode);
                boolean boo = terminalDao.obtainByParamCode(terminalDto);
                if(boo){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00417);
                    resultJO.put(MsgAndCode.RSP_DESC, merchantCode+", "+termCode+" "+MsgAndCode.MESSAGE_00417);
                    return resultJO;
                }
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00221);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00221);
                return resultJO;
            }

            //查询分公司id
            String branchId = "";  //分公司id
            List<Map<String, Object>> branchs =(List<Map<String, Object>>)agentDao.selectBranch(branchName);
            LogHelper.debug("分公司内容：" + branchName + "分公司个数：" + branchs.size());
            if(branchs!=null && branchs.size()>0){
                branchId=""+branchs.get(0).get("ORGANIZATION_ID");
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00407);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00407);
                return resultJO;
            }

            //查询代理商id
            List<Map<String, Object>> agents = agentDao.getAgentByAgentNum(null, agentName);
            String agentId = "";
            if(null != agents && agents.size() > 0){
                agentId = ""+agents.get(0).get("AGENT_ID");
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00412);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00412);
                return resultJO;
            }

            //插入软pos终端信息
            Allpay_Terminal terminal = new Allpay_Terminal();
            terminal.setTerminal_merchcode(merchantCode);	//商户编号  （shopNumber）15位
            terminal.setTerminal_store_id("" + listStore.get(0).get("STORE_ID"));    //门店ID
            terminal.setTerminal_termcode(termCode);    //终端编号 8位， 指定人员  手机号后8位
            terminal.setTerminal_postype(2);	//1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
            terminal.setTerminal_branchid(branchId);	//pos所属分公司ID
            terminal.setTerminal_agentid(agentId);	  //pos所属代理
            terminal.setTerminal_merchant_id("" + list.get(0).get("MERCHANT_ID"));	//商户ID
            terminal.setTerminal_shopuserid(shopuserId);  //软POS对应人员id
            terminal.setTerminal_usertype(Integer.parseInt(userType));

            JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_BULK_IMPORT, "", ""+source.get("userNameFromBusCookie"));
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");

            terminal.setALLPAY_CREATER(userName);
            terminal.setALLPAY_CREATETIME(now);
            terminal.setALLPAY_UPDATETIME(now);  //修改时间
            terminal.setALLPAY_LOGICDEL("1");
            terminal.setALLPAY_LOGRECORD(record);
            terminal.setALLPAY_ISSTART(1);  //启用
            terminal.setTerminal_permission(null);
            boolean bool = terminalDao.insert(terminal);
            if(!bool){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00410);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00410);
                return resultJO;
            }
            sendToKafkaService.sendApayPosToKafka(terminal.getTerminal_posid(),"");
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            resultJO.put("merchantCode", merchantCode);	//商户号15位
            resultJO.put("termCode", termCode);	//终端号
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    /**
     * 获取终端菜单配置信息
     * @param allpayTerminalDto
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject getPosMenuSetInfo(AllpayTerminalDto allpayTerminalDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        if(CommonHelper.isEmpty(""+allpayTerminalDto.getMerchantId())){  //商户id
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00201);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00201);
            return resultJO;
        }
        if(CommonHelper.isEmpty(""+allpayTerminalDto.getStoreId())){  //门店id
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00200);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00200);
            return resultJO;
        }
        if(CommonHelper.isEmpty(allpayTerminalDto.getPosType())){  //pos类型
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00204);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00204);
            return resultJO;
        }

        List<Map<String, Object>> list = (List<Map<String, Object>>)terminalDao.getPosMenuSetInfo(allpayTerminalDto);
        if(null != list && list.size() >0 ){
            Map<String, Object> map = list.get(0);
            resultJO.put("merchantId", map.get("TERMINAL_MERCHANT_ID"));	//商户id
            resultJO.put("storeId", map.get("TERMINAL_STORE_ID"));	//门店id
            resultJO.put("storeName", CommonHelper.nullToString(map.get("STORE_SHOPNAME")));	//门店名称
            resultJO.put("posType", map.get("TERMINAL_POSTYPE"));	//Pos类型	1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
            resultJO.put("limitConfiguration", CommonHelper.nullToString(map.get("TERMINAL_PERMISSION")));	//权限配置方案
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, "未查询到当前商户下的终端菜单配置信息！");
        }
        return resultJO;
    }

    /**
     * 新增软pos人员时，调用的 新增软pos信息
     * @param source
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject importUserSoftPos(Map<String, Object> source) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(source!=null){
            String userShopId=""+source.get("userShopId");  //所属商户id
            String storeId=""+source.get("storeId");  //门店id
            String userRoleId=""+source.get("userRoleId");  //角色ID
            String userPhone=""+source.get("userPhone");   //管理员手机
            String shopuserId = ""+source.get("shopuserId");  //软POS对应人员id

            if(CommonHelper.isEmpty(userShopId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00216);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00216);
                return resultJO;
            }
            if(CommonHelper.isEmpty(storeId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00200);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00200);
                return resultJO;
            }
            if(CommonHelper.isEmpty(userRoleId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00209);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00209);
                return resultJO;
            }
            if(CommonHelper.isEmpty(userPhone)) {
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00215);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00215);
                return resultJO;
            }
            if(CommonHelper.isEmpty(shopuserId)) {
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00420);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00420);
                return resultJO;
            }
            String termCode = userPhone.substring(3);  //截取手机号后8位为终端编号
            List<Map<String, Object>>  listStore = (List<Map<String, Object>>)allpayStoreDao.checkStore(userShopId, storeId);
            if(null == listStore || listStore.size() == 0){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00418);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00418);
                return resultJO;
            }

            String merchantCode = ""+listStore.get(0).get("STORE_SHOPNUMBER");   //获取门店的15位编号
            //判断POS商户号+终端号是否存在
            AllpayTerminalDto terminalDto = new AllpayTerminalDto();
            terminalDto.setTermCode(termCode);
            terminalDto.setMerchaCode(merchantCode);
            boolean boo = terminalDao.obtainByParamCode(terminalDto);
            if(boo){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00417);
                resultJO.put(MsgAndCode.RSP_DESC, merchantCode+", "+termCode+" "+MsgAndCode.MESSAGE_00417);
                return resultJO;
            }

            //插入软pos终端信息
            Allpay_Terminal terminal = new Allpay_Terminal();
            terminal.setTerminal_merchcode(merchantCode);	//商户编号  （shopNumber）15位
            terminal.setTerminal_store_id(storeId);    //门店ID
            terminal.setTerminal_termcode(termCode);    //终端编号 8位， 指定人员  手机号后8位
            terminal.setTerminal_postype(2);	//1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
            terminal.setTerminal_merchant_id(userShopId);	//商户ID
            terminal.setTerminal_usertype(Integer.parseInt(userRoleId)); //管理员类型(0门店管理,1门店业务,2商户管理)
            terminal.setTerminal_shopuserid(shopuserId);  //软POS对应人员id

            JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_BULK_IMPORT, "", ""+source.get("userNameFromBusCookie"));
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");

            terminal.setALLPAY_CREATER(userName);
            terminal.setALLPAY_CREATETIME(now);
            terminal.setALLPAY_UPDATETIME(now);  //修改时间
            terminal.setALLPAY_LOGICDEL("1");
            terminal.setALLPAY_LOGRECORD(record);
            terminal.setALLPAY_ISSTART(1);  //启用
            terminal.setTerminal_permission(null);
            boolean bool = terminalDao.insert(terminal);
            if(!bool){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00410);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00410);
                return resultJO;
            }
            sendToKafkaService.sendApayPosToKafka(terminal.getTerminal_posid(),"");
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            resultJO.put("merchantCode", merchantCode);	//商户号15位
            resultJO.put("termCode", termCode);	//终端号
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    /**
     * 查询软pos信息
     * @param source
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject getUserSoftPos(Map<String, Object> source) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(source!=null){
            String shopuserId = ""+source.get("shopuserId");  //软POS对应人员id

            if(CommonHelper.isEmpty(shopuserId)) {
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00420);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00420);
                return resultJO;
            }

            List<Map<String, Object>> list= (List<Map<String, Object>>)terminalDao.getUserSoftPos(shopuserId);
            if(null == list || list.size() == 0){
                resultJO.put("exist", "0");  //软pos不存在
                return resultJO;
            }
            resultJO.put("exist", "1");  //软pos已存在
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    private String returnSuccessMessage(JSONObject jsonObject){
        jsonObject.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
        jsonObject.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        return jsonObject.toString();
    }

    /**
     *
     * @param code
     * @param msg
     * @return
     */
    private String returnMissParamMessage(String code, String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MsgAndCode.RSP_CODE, code);
        jsonObject.put(MsgAndCode.RSP_DESC, msg);
        return jsonObject.toString();
    }

    /**
     * 必填项验证
     * @param allpayTerminalDto
     * @return
     */
    public JSONObject check(AllpayTerminalDto allpayTerminalDto) throws Exception {
        JSONObject resultJO = null;
        String posType = allpayTerminalDto.getPosType();  //Pos类型		1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
        if(CommonHelper.isEmpty(posType)){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00204);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00204);
            return resultJO;
        }
        if(CommonHelper.isEmpty(allpayTerminalDto.getStoreId())){  //门店id
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00200);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00200);
            return resultJO;
        }
        if(CommonHelper.isEmpty(allpayTerminalDto.getMerchantId())){  //所属商户id
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00201);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00201);
            return resultJO;
        }
        if(CommonHelper.isEmpty(allpayTerminalDto.getTermCode())){  //终端编号  8位
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00202);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00202);
            return resultJO;
        }
        if(!"2".equals(posType)){
            if(CommonHelper.isEmpty(allpayTerminalDto.getMerchaCode())){  //商户编号（业务编号）
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00210);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00210);
                return resultJO;
            }
        }
        if(CommonHelper.isNullOrEmpty(allpayTerminalDto.getPosStatus())) {  // 状态 是否启用	0:停用 1：启用
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00211);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00211);
            return resultJO;
        }
        if("1".equals(posType) || "4".equals(posType)){  //Pos类型		1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
            if(CommonHelper.isEmpty(allpayTerminalDto.getPayChannel())){  //应用渠道（系统）(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00205);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00205);
                return resultJO;
            }
        }
        if("2".equals(posType)) {  //Pos类型		1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
            if(CommonHelper.isEmpty(allpayTerminalDto.getUserType())){  //角色	String(0网点管理,1网点业务,2商户管理)
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00209);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00209);
                return resultJO;
            }
        }
        return resultJO;
    }
   
    /**
     * 根据merCod,terCode获取信息
     */
	@Override
	public String obtainMsgForTerAndMercCode(Map<String, Object> source)
			throws Exception {
		
		String merCode = null;
		if(!CommonHelper.isNullOrEmpty(source.get("merCode"))){
			merCode = source.get("merCode").toString();
		}
		String terCode = null;
		if(!CommonHelper.isNullOrEmpty(source.get("terCode"))){
			terCode = source.get("terCode").toString();
		}
		List<Map<String, Object>> list = terminalDao.obtainMsgForTerAndMerCode(merCode, terCode);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("payChannel", "");
		node.put("storeName", "");
		if(!list.isEmpty()){
			Map<String, Object> map = list.get(0);
			node.put("payChannel", checkTextIsOrNotNull(map.get("TERMINAL_PAYCHANNEL")));
			node.put("storeName", checkTextIsOrNotNull(map.get("STORE_SHOPNAME")));
		}
		return returnSuccessMessage(node);
	}
   
	private String returnSuccessMessage(ObjectNode node){
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
		node.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
		return node.toString();
	}
	
    private String checkTextIsOrNotNull(Object obj){
		
		if(!CommonHelper.isNullOrEmpty(obj)){
			return obj.toString();
		}
		return "";
	}
}
