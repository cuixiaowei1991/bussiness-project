package com.cn.service.impl.businessServiceImpl;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.common.WriteExcelFile;
import com.cn.dao.AllpayPosParterDao;
import com.cn.dao.agentDao;
import com.cn.entity.dto.AllpayAgentDto;
import com.cn.entity.po.Allpay_Agent;
import com.cn.entity.po.Allpay_PosParter;
import com.cn.service.businessService.AllpayAgentService;
import com.cn.service.impl.kafkaserviceimpl.SendToKafkaServiceImpl;
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
 * Created by sun.yayi on 2016/12/5.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Service
public class AllpayAgentServiceImpl implements AllpayAgentService {
    @Value("${excelpath}")
    private String excelpath;
    @Value("${outpath}")
    private String outpath;

    @Autowired
    private agentDao agentDao;
    @Autowired
    private AllpayPosParterDao allpayPosparterDaoImpl;
    @Autowired
    private SendToKafkaServiceImpl sendToKafkaService;

    public JSONObject insertAllpayAgent(AllpayAgentDto bean) throws Exception{
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String agentName=bean.getAgentName();  //名称
            String shortName=bean.getAgentShortName(); //简称
            String level=bean.getLevel(); //代理级别
            String parentId=bean.getAgentParentId(); //上级代理id
            String branchId=bean.getBranchId(); //分公司
            String provinceId=bean.getProvinceId(); //省
            String provinceName=bean.getProvinceName(); //省名
            String cityId=bean.getCityId(); //市ID
            String cityName=bean.getCityName(); //市明
            String countryId=bean.getCountryId();
            String countryName=bean.getCountryName();
            String address=bean.getAgentAddress(); //地址
            String legalPersonName=bean.getLegalPersonName();//代理联系人
            String legalPersonPhone=bean.getLagalPersonPhone();//电话
            String shopPeopleName=bean.getShopPeopleName(); //拓展人
            String agentType=bean.getAgentType();
            String allpayid=bean.getAllpayid();
            String allpayKey=bean.getAllpayKey();

            if(CommonHelper.isEmpty(agentName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00301);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00301);
                return resultJO;
            }
            if(CommonHelper.isEmpty(agentType)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00335);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00335);
                return resultJO;
            }
            if(CommonHelper.isEmpty(legalPersonName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00303);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00303);
                return resultJO;
            }
            if(CommonHelper.isEmpty(legalPersonPhone)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00304);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00304);
                return resultJO;
            }
           /* if(CommonHelper.isEmpty(shortName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00302);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00302);
                return resultJO;
            }
            if(CommonHelper.isEmpty(provinceId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00309);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00309);
                return resultJO;
            }
            if(CommonHelper.isEmpty(provinceName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00310);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00310);
                return resultJO;
            }
            if(CommonHelper.isEmpty(cityId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00311);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00311);
                return resultJO;
            }
            if(CommonHelper.isEmpty(cityName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00312);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00312);
                return resultJO;
            }
            if(CommonHelper.isEmpty(countryId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00313);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00313);
                return resultJO;
            }
            if(CommonHelper.isEmpty(countryName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00314);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00314);
                return resultJO;
            }
            if(CommonHelper.isEmpty(address)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00315);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00315);
                return resultJO;
            }
            if(CommonHelper.isEmpty(allpayid)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00338);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00338);
                return resultJO;
            }
            if(CommonHelper.isEmpty(allpayKey)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00339);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00339);
                return resultJO;
            }
            */
            if(CommonHelper.isEmpty(shopPeopleName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00305);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00305);
                return resultJO;
            }
            if(CommonHelper.isEmpty(bean.getBranchId())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00307);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00307);
                return resultJO;
            }
            if(CommonHelper.isEmpty(level)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00306);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00306);
                return resultJO;
            }/*else if(2==Integer.parseInt(level)){
                if(CommonHelper.isEmpty(parentId)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00308);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00308);
                    return resultJO;
                }
            }*/
            if(CommonHelper.isEmpty(parentId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00308);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00308);
                return resultJO;
            }

            if(CommonHelper.isEmpty(bean.getAgentState())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00316);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00316);
                return resultJO;
            }

            String isCreate=bean.getIsCreatePosParter();
            if(CommonHelper.isEmpty(isCreate)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00317);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00317);
                return resultJO;
            }
            Allpay_Agent agent=new Allpay_Agent();
            String agentNum = agentDao.getAgentNum(4);  //随机生成4位不重复的代理商编号
            agent.setAgent_name(agentName);
            agent.setAgent_shortname(shortName);
            agent.setAgent_level(Integer.parseInt(level));
            agent.setAgent_location(parentId);
            agent.setAgent_branch_id(branchId);
            agent.setAgent_province_id(provinceId);
            agent.setAgent_province_name(provinceName);
            agent.setAgent_city_id(cityId);
            agent.setAgent_city_name(cityName);
            agent.setAgent_country_id(countryId);
            agent.setAgent_country_name(countryName);
            agent.setAgent_address(address);
            agent.setAgent_legalpersonname(legalPersonName);
            agent.setAgent_legalpersonphone(legalPersonPhone);
            agent.setAgent_shoppeoplename(shopPeopleName);
            agent.setAgent_allpayid(allpayid);
            agent.setAgent_allpaykey(allpayKey);
            agent.setAgent_Type(agentType);
            agent.setAgent_num(agentNum);

            String userNameFromBusCookie = CommonHelper.nullToString(bean.getUserNameFromBusCookie());
            String userNameFromAgentCookie = CommonHelper.nullToString(bean.getUserNameFromAgentCookie());
            JSONObject publicFileds = null;
            if(!CommonHelper.isEmpty(userNameFromBusCookie)){
                publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", userNameFromBusCookie);
            }else if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
                publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", userNameFromAgentCookie);
            }else{
                publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", null);
            }
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");

            agent.setALLPAY_CREATER(userName);
            agent.setALLPAY_CREATETIME(now);
            agent.setALLPAY_UPDATETIME(now);  //修改时间
            agent.setALLPAY_LOGICDEL("1");
            agent.setALLPAY_LOGRECORD(record);
            agent.setALLPAY_ISSTART(Integer.parseInt(bean.getAgentState()));
            agent.setALLPAY_STATE(5);

            agentDao.saveOrUpate(agent);

            sendToKafkaService.sendApayagentToKafka(agent.getAgent_id(),"");

            if(1==Integer.parseInt(isCreate)){
                String parterId=CommonHelper.getRandomNum(9);
                List list=allpayPosparterDaoImpl.getPosPArterByParterId(parterId, null);
                while(list!=null && list.size()>0){
                    parterId=CommonHelper.getRandomNum(9);
                    list=allpayPosparterDaoImpl.getPosPArterByParterId(parterId, null);
                }
                Allpay_PosParter posParter=new Allpay_PosParter();
                posParter.setPosparter_parterid(parterId);
                posParter.setPosparter_parterallname(agentName);
                posParter.setPosparter_partershortname(shortName);
                posParter.setPosparter_connectperson(legalPersonName);
                posParter.setPosparter_connecttel(legalPersonPhone);
                posParter.setPosparter_parterlevel(Integer.parseInt(level));
                posParter.setPosparter_address(address);
                posParter.setPosparter_branch_id(branchId);
                posParter.setPosparter_belongparterid(parentId);
                posParter.setPosparter_province_id(provinceId);
                posParter.setPosparter_province_name(provinceName);
                posParter.setPosparter_city_id(cityId);
                posParter.setPosparter_city_name(cityName);
                posParter.setPosparter_country_id(countryId);
                posParter.setPosparter_country_name(countryName);
                posParter.setPosparter_expandpeople(shopPeopleName);

                posParter.setALLPAY_CREATER(userName);
                posParter.setALLPAY_CREATETIME(now);
                posParter.setALLPAY_UPDATETIME(now);  //修改时间
                posParter.setALLPAY_LOGICDEL("1");
                posParter.setALLPAY_LOGRECORD(record);
                posParter.setALLPAY_ISSTART(Integer.parseInt(bean.getAgentState()));
                posParter.setALLPAY_STATE(5);
                allpayPosparterDaoImpl.saveOrUpdate(posParter);
                sendToKafkaService.sendApayPOSParter(posParter.getPosparter_id(),"");
            }
                resultJO.put("agentId",agent.getAgent_id());
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    public JSONObject getList(AllpayAgentDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String currentPage=bean.getCurragePage();
            String pageSize=bean.getPageSize();
            if(CommonHelper.isEmpty(currentPage) || CommonHelper.isEmpty(pageSize) ){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00110);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00110);
                return resultJO;
            }
            List<Map<String, Object>> list=agentDao.obtainList(bean, currentPage, pageSize);
            int total=agentDao.count(bean);
            JSONArray array=new JSONArray();
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    Map<String, Object> map=list.get(i);
                    JSONObject json=new JSONObject();
                    json.put("agentName",CommonHelper.nullToString(map.get("AGENT_NAME")));
                    json.put("agentNum",CommonHelper.nullToString(map.get("AGENT_NUM")));
                    json.put("agentId",CommonHelper.nullToString(map.get("AGENT_ID")));
                    json.put("agentLevel",CommonHelper.nullToString(map.get("AGENT_LEVEL")));
                    json.put("agentAddress",CommonHelper.nullToString(map.get("AGENT_ADDRESS")));
                    json.put("agentParentId",map.get("SUP_AGENTID")==null ? 0:map.get("SUP_AGENTID"));	//所属父级代理商id
                    json.put("branchId",CommonHelper.nullToString(map.get("AGENT_BRANCH_ID")));
                    json.put("branchName",CommonHelper.nullToString(map.get("ORGANIZATION_NAME")));
                    json.put("agentType",CommonHelper.nullToString(map.get("AGENT_TYPE")));
                    json.put("createTime",CommonHelper.nullToString(map.get("ALLPAY_CREATETIME")));
                    json.put("agentState",CommonHelper.nullToString(map.get("ALLPAY_ISSTART")));
                    array.put(json);
                }
            }
            resultJO.put("lists",array);
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            resultJO.put("curragePage",currentPage);
            resultJO.put("pageSize",pageSize);
            resultJO.put("total",total);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    public JSONObject updateAllpayAgent(AllpayAgentDto bean) throws Exception{
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String agentId=bean.getAgentId();
            String agentName=bean.getAgentName();  //名称
            String shortName=bean.getAgentShortName(); //简称
            String level=bean.getLevel(); //代理级别
            String parentId=bean.getAgentParentId(); //上级代理id
            String branchId=bean.getBranchId(); //分公司
            String provinceId=bean.getProvinceId(); //省
            String provinceName=bean.getProvinceName(); //省名
            String cityId=bean.getCityId(); //市ID
            String cityName=bean.getCityName(); //市明
            String countryId=bean.getCountryId();
            String countryName=bean.getCountryName();
            String address=bean.getAgentAddress(); //地址
            String legalPersonName=bean.getLegalPersonName();//代理联系人
            String legalPersonPhone=bean.getLagalPersonPhone();//电话
//            String shopPeopleName=bean.getShopPeopleName(); //拓展人
            String agentType=bean.getAgentType();

            if(CommonHelper.isEmpty(agentId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00300);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00300);
                return resultJO;
            }
            if(CommonHelper.isEmpty(agentType)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00335);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00335);
                return resultJO;
            }
            if(CommonHelper.isEmpty(agentName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00301);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00301);
                return resultJO;
            }

           /* if(CommonHelper.isEmpty(shortName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00302);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00302);
                return resultJO;
            }
            if(CommonHelper.isEmpty(legalPersonName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00303);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00303);
                return resultJO;
            }
             if(CommonHelper.isEmpty(legalPersonName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00303);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00303);
                return resultJO;
            }
            if(CommonHelper.isEmpty(legalPersonPhone)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00304);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00304);
                return resultJO;
            }
            if(CommonHelper.isEmpty(legalPersonPhone)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00304);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00304);
                return resultJO;
            }
             if(CommonHelper.isEmpty(bean.getProvinceId())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00309);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00309);
                return resultJO;
            }
            if(CommonHelper.isEmpty(bean.getProvinceName())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00310);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00310);
                return resultJO;
            }
            if(CommonHelper.isEmpty(bean.getCityId())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00311);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00311);
                return resultJO;
            }
            if(CommonHelper.isEmpty(bean.getCityName())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00312);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00312);
                return resultJO;
            }
            if(CommonHelper.isEmpty(bean.getCountryId())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00313);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00313);
                return resultJO;
            }
            if(CommonHelper.isEmpty(bean.getCountryName())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00314);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00314);
                return resultJO;
            }
            if(CommonHelper.isEmpty(bean.getAgentAddress())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00315);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00315);
                return resultJO;
            }
            if(CommonHelper.isEmpty(shopPeopleName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00305);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00305);
                return resultJO;
            }*/
            if(CommonHelper.isEmpty(bean.getBranchId())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00307);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00307);
                return resultJO;
            }
            if(CommonHelper.isEmpty(level)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00306);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00306);
                return resultJO;
            }/*else if(2==Integer.parseInt(level)){
                if(CommonHelper.isEmpty(parentId)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00308);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00308);
                    return resultJO;
                }
            }*/
            if(CommonHelper.isEmpty(parentId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00308);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00308);
                return resultJO;
            }

            if(CommonHelper.isEmpty(bean.getAgentState())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00316);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00316);
                return resultJO;
            }
            String isCreate=bean.getIsCreatePosParter();
            if(CommonHelper.isEmpty(isCreate)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00317);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00317);
                return resultJO;
            }
            Allpay_Agent agent=agentDao.getMerchant(agentId);
            agent.setAgent_name(agentName);
            agent.setAgent_shortname(shortName);
            agent.setAgent_level(Integer.parseInt(level));
            agent.setAgent_location(parentId);
            agent.setAgent_branch_id(branchId);
            agent.setAgent_province_id(provinceId);
            agent.setAgent_province_name(provinceName);
            agent.setAgent_city_id(cityId);
            agent.setAgent_city_name(cityName);
            agent.setAgent_country_id(countryId);
            agent.setAgent_country_name(countryName);
            agent.setAgent_address(address);
            agent.setAgent_legalpersonname(legalPersonName);
            agent.setAgent_legalpersonphone(legalPersonPhone);
//            agent.setAgent_shoppeoplename(shopPeopleName);
            agent.setAgent_allpayid(bean.getAllpayid());
            agent.setAgent_allpaykey(bean.getAllpayKey());
            agent.setAgent_Type(agentType);

            String userNameFromBusCookie = CommonHelper.nullToString(bean.getUserNameFromBusCookie());
            String userNameFromAgentCookie = CommonHelper.nullToString(bean.getUserNameFromAgentCookie());
            JSONObject publicFileds = null;
            if(!CommonHelper.isEmpty(userNameFromBusCookie)){
                publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, agent.getALLPAY_LOGRECORD(), userNameFromBusCookie);
            }else if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
                publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, agent.getALLPAY_LOGRECORD(), userNameFromAgentCookie);
            }else{
                publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, agent.getALLPAY_LOGRECORD(), null);
            }
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");

            agent.setALLPAY_UPDATER(userName);
            agent.setALLPAY_UPDATETIME(now);  //修改时间
            agent.setALLPAY_LOGRECORD(record);
            agent.setALLPAY_ISSTART(Integer.parseInt(bean.getAgentState()));
            agentDao.saveOrUpate(agent);
            sendToKafkaService.sendApayagentToKafka(agent.getAgent_id(),"");
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    public JSONObject getAllpayAgentInfo(AllpayAgentDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String agentId=bean.getAgentId();
            if(CommonHelper.isEmpty(agentId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00300);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00300);
                return resultJO;
            }
            List<Map<String, Object>> agent=agentDao.getAgentBySql(agentId);
            if(null != agent && agent.size() >0 ){
                Map<String, Object> map = agent.get(0);
                resultJO.put("agentName",CommonHelper.nullToString(map.get("AGENT_NAME")));
                resultJO.put("agentId",CommonHelper.nullToString(map.get("AGENT_ID")));
                resultJO.put("agentShortName",CommonHelper.nullToString(map.get("AGENT_SHORTNAME")));
                resultJO.put("legalPersonName",CommonHelper.nullToString(map.get("AGENT_LEGALPERSONNAME")));
                resultJO.put("lagalPersonPhone",CommonHelper.nullToString(map.get("AGENT_LEGALPERSONPHONE")));
                resultJO.put("shopPeopleName",CommonHelper.nullToString(map.get("AGENT_SHOPPEOPLENAME")));
                resultJO.put("level",CommonHelper.nullToString(map.get("AGENT_LEVEL")));
                resultJO.put("branchId",CommonHelper.nullToString(map.get("AGENT_BRANCH_ID")));
                resultJO.put("branchName",CommonHelper.nullToString(map.get("ORGANIZATION_NAME")));	//所属分公司名称
                resultJO.put("location",CommonHelper.nullToString(map.get("AGENT_LOCATION")));
                resultJO.put("locationName",CommonHelper.nullToString(map.get("SA_AGENT_NAME")));	//上级代理商名称
                resultJO.put("provinceId",CommonHelper.nullToString(map.get("AGENT_PROVINCE_ID")));
                resultJO.put("provinceName",CommonHelper.nullToString(map.get("AGENT_PROVINCE_NAME")));
                resultJO.put("cityId",CommonHelper.nullToString(map.get("AGENT_CITY_ID")));
                resultJO.put("cityName",CommonHelper.nullToString(map.get("AGENT_CITY_NAME")));
                resultJO.put("countryId",CommonHelper.nullToString(map.get("AGENT_COUNTRY_ID")));
                resultJO.put("countryName",CommonHelper.nullToString(map.get("AGENT_COUNTRY_NAME")));
                resultJO.put("agentAddress",CommonHelper.nullToString(map.get("AGENT_ADDRESS")));
                resultJO.put("agentState",CommonHelper.nullToString(map.get("ALLPAY_ISSTART")));
                resultJO.put("agentType",CommonHelper.nullToString(map.get("AGENT_TYPE")));
                resultJO.put("allpayid",CommonHelper.nullToString(map.get("AGENT_ALLPAYID")));
                resultJO.put("allpayKey",CommonHelper.nullToString(map.get("AGENT_ALLPAYKEY")));
            }
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    @Override
    public JSONObject deleteAgentInfo(AllpayAgentDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String agentId=bean.getAgentId();
            if(CommonHelper.isEmpty(agentId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00300);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00300);
                return resultJO;
            }
            if("1".equals(bean.getSerious())){
                agentDao.delete(agentId);
            }else{
                Allpay_Agent agent=agentDao.getMerchant(agentId);

                String userNameFromBusCookie = CommonHelper.nullToString(bean.getUserNameFromBusCookie());
                String userNameFromAgentCookie = CommonHelper.nullToString(bean.getUserNameFromAgentCookie());
                JSONObject publicFileds = null;
                if(!CommonHelper.isEmpty(userNameFromBusCookie)){
                    publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, agent.getALLPAY_LOGRECORD(), userNameFromBusCookie);
                }else if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
                    publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, agent.getALLPAY_LOGRECORD(), userNameFromAgentCookie);
                }else{
                    publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, agent.getALLPAY_LOGRECORD(), null);
                }
                String userName = publicFileds.getString("userName");
                Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
                String record = publicFileds.getString("record");

                agent.setALLPAY_UPDATER(userName);
                agent.setALLPAY_UPDATETIME(now);  //修改时间
                agent.setALLPAY_LOGRECORD(record);
                agent.setALLPAY_LOGICDEL("2");
                agentDao.saveOrUpate(agent);
                sendToKafkaService.sendApayagentToKafka(agentId,"del");
            }
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    @Override
    public JSONObject repeatAllpayId(AllpayAgentDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            if(CommonHelper.isEmpty(bean.getAllpayid())){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00338);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00338);
                return resultJO;
            }
            String isExit="0";
            List<Map<String,Object>> list=agentDao.isExist(bean);
            if(list!=null && list.size()>0){
                if(CommonHelper.isNullOrEmpty(bean.getAgentId())){
                    isExit="1";
                }else{
                    Map map=list.get(0);
                    if(!bean.getAgentId().equals(CommonHelper.nullToString(map.get("AGENT_ID")))){
                        isExit="1";
                    }
                }
            }
            resultJO.put("exist",isExit);
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    @Override
    public JSONObject getAgents(AllpayAgentDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            List<Map<String,Object>> list = agentDao.getAllAgents(bean);
            JSONArray array=new JSONArray();
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                   Map<String,Object> map=list.get(i);
                   JSONObject object=new JSONObject();
                   object.put("agentId",CommonHelper.nullToString(map.get("AGENT_ID")));
                   object.put("agentName",CommonHelper.nullToString(map.get("AGENT_NAME")));
                   array.put(object);
                }
            }
            resultJO.put("lists",array);
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    @Override
    public JSONObject checkAgentExist(AllpayAgentDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String agentName=bean.getAgentName();
            if(CommonHelper.isNullOrEmpty(agentName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00301);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00301);
                return resultJO;
            }else{
                List<Map<String,Object>> list=agentDao.getAgentsByAgentName(bean.getAgentName());
                String isExit="0";
                if(list!=null && list.size()>0){
                    if(CommonHelper.isNullOrEmpty(bean.getAgentId())){
                        isExit="1";
                    }else{
                        Map map=list.get(0);
                        if(!bean.getAgentId().equals(CommonHelper.nullToString(map.get("AGENT_ID")))){
                            isExit="1";
                        }
                    }
                }
                resultJO.put("exist",isExit);
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            }
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    @Override
    public JSONObject checkAgentPhoneExist(AllpayAgentDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String phone=bean.getLagalPersonPhone();
            if(CommonHelper.isNullOrEmpty(phone)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00304);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00304);
                return resultJO;
            }else{
                List<Map<String,Object>> list=agentDao.checkAgentPhoneExist(phone);
                String isExit="0";
                if(list!=null && list.size()>0){
                    if(CommonHelper.isNullOrEmpty(bean.getAgentId())){
                        isExit="1";
                    }else{
                        Map map=list.get(0);
                        if(!bean.getAgentId().equals(CommonHelper.nullToString(map.get("AGENT_ID")))){
                            isExit="1";
                        }
                    }
                }
                resultJO.put("exist",isExit);
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            }
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    public void outDataAgent(AllpayAgentDto bean,HttpServletResponse response) throws Exception {
        List<String[]> posParterList = new ArrayList<String[]>();
        if(bean!=null){
            List<Map<String, Object>> list=agentDao.obtainList(bean, null, null);
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    String[] ss = new String[8];
                    Map<String, Object> map1=list.get(i);
                    ss[0]=""+(i+1);
                    ss[1]=CommonHelper.nullToString(map1.get("AGENT_NAME"));
                    ss[2]=CommonHelper.nullToString(map1.get("AGENT_NUM"));
                    ss[3]=CommonHelper.nullToString(map1.get("AGENT_LEVEL"));
                    if("1".equals(ss[3])){
                        ss[3] = "一级";
                    }else if("2".equals(ss[3])){
                        ss[3] = "二级";
                    }else{
                        ss[3] = "";
                    }
                    ss[4]=CommonHelper.nullToString(map1.get("ORGANIZATION_NAME"));
                    ss[5]=CommonHelper.nullToString(map1.get("AGENT_TYPE"));
                    if("0".equals(ss[5])){
                        ss[5] = "个人代理";
                    }else if("1".equals(ss[5])){
                        ss[5] = "公司代理";
                    }else{
                        ss[5] = "";
                    }
                    ss[6]=CommonHelper.nullToString(map1.get("ALLPAY_CREATETIME"));
                    ss[7]=CommonHelper.nullToString(map1.get("ALLPAY_ISSTART"));
                    if("1".equals(ss[7])){
                        ss[7] = "启用";
                    }else if("0".equals(ss[6])){
                        ss[7] = "停用";
                    }else{
                        ss[7] = "";
                    }
                    posParterList.add(ss);
                }
            }
        }
        String[] W = { "序号", "代理商名称", "代理商编号", "代理商级别", "归属分子公司", "代理商类型", "录入时间", "状态"};
        WriteExcelFile.writeExcel(posParterList, "Agent_List", W, "Agent_List",outpath, response);
    }


    @Override
    public JSONObject importData(Map<String, Object> source) throws Exception {
        JSONObject resultJO=new JSONObject();
        /*if(source!=null){
            JSONArray array=new JSONArray(source.get("jsonStr"));
            for(int i=0;i<array.length();i++){
                JSONObject object=array.getJSONObject(i);
                String agentName=object.getString("agentName");  //名称
                String shortName=object.getString("shortName"); //简称
                String level=object.getString("level"); //代理级别
                String parentName=object.getString("parentName");
                String branchName=object.getString("branchName");
                String provinceName=object.getString("provinceName"); //省名
                String cityName=object.getString("cityName"); //市明
                String countryName=object.getString("countryName");
                String address=object.getString("address"); //地址
                String agentType=object.getString("agentType");
                String legalPersonName=object.getString("legalPersonName");
                String legalPersonPhone=object.getString("legalPersonPhone");
                String shopPeopleName=object.getString("shopPeopleName");

                if(CommonHelper.isEmpty(agentName)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00301);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00301);
                    return resultJO;
                }
                if(CommonHelper.isEmpty(shortName)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00302);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00302);
                    return resultJO;
                }
                if(CommonHelper.isEmpty(legalPersonName)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00303);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00303);
                    return resultJO;
                }
                if(CommonHelper.isEmpty(legalPersonPhone)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00304);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00304);
                    return resultJO;
                }
                if(CommonHelper.isEmpty(shopPeopleName)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00305);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00305);
                    return resultJO;
                }
                if(CommonHelper.isEmpty(branchName)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00336);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00336);
                    return resultJO;
                }
                if(CommonHelper.isEmpty(level)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00306);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00306);
                    return resultJO;
                }else if("二级".equals(level)){
                    if(CommonHelper.isEmpty(parentName)){
                        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00337);
                        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00337);
                        return resultJO;
                    }
                }
                if(CommonHelper.isEmpty(provinceName)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00310);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00310);
                    return resultJO;
                }
                if(CommonHelper.isEmpty(cityName)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00312);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00312);
                    return resultJO;
                }
                if(CommonHelper.isEmpty(countryName)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00314);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00314);
                    return resultJO;
                }
                List<Allpay_Branch> branchs =allpayBranchDaoImpl.selectBranch(branchName);
                LogHelper.debug("分公司内容：" + branchName + "分公司内容：" + branchs.size());
                String branchId=""; //分公司
                if(branchs!=null && branchs.size()>0){
                    branchId=branchs.get(0).getBranch_id();
                }else{
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00307);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00307);
                    return resultJO;
                }
                List<Allpay_Agent> agents=null;
                String parentId="";
                String agentLevel="";
                if("一级".equals(level)){
                    agentLevel="1";
                }
                if("二级".equals(level)){
                    agentLevel="2";
                    List<Allpay_Agent> agentList=agentDao.selectAgent(agentName,branchId);
                    if(agentList!=null && agentList.size()>0){
                        parentId=agentList.get(0).getAgent_id();//上级代理id
                    }
                }

               *//* String provinceId=agentDao.selectRegionCode(1,provinceName); //省
                String cityId=agentDao.selectRegionCode(2, cityName); //市ID
                String countryId=agentDao.selectRegionCode(3,countryName);*//*

                Allpay_Agent agent=new Allpay_Agent();
                agent.setAgent_name(agentName);
                agent.setAgent_shortname(shortName);
                agent.setAgent_level(Integer.parseInt(agentLevel));
                agent.setAgent_location(parentId);
                agent.setAgent_branch_id(branchId);
                agent.setAgent_province_id(provinceId);
                agent.setAgent_province_name(provinceName);
                agent.setAgent_city_id(cityId);
                agent.setAgent_city_name(cityName);
                agent.setAgent_country_id(countryId);
                agent.setAgent_city_name(countryName);
                agent.setAgent_address(address);
                agent.setAgent_legalpersonname(legalPersonName);
                agent.setAgent_legalpersonphone(legalPersonPhone);
                agent.setAgent_shoppeoplename(shopPeopleName);
                agent.setAgent_allpayid(bean.getAllpayid());
                agent.setAgent_allpaykey(bean.getAllpayKey());

                agent.setALLPAY_CREATER("");
                Date now = CommonHelper.getNowDateShort();
                agent.setALLPAY_CREATETIME(now);
                agent.setALLPAY_UPDATETIME(now);  //修改时间
                agent.setALLPAY_LOGICDEL("1");
                agent.setALLPAY_LOGRECORD("username-" + now + "-" + MsgAndCode.OPERATION_NEW);
                agent.setALLPAY_ISSTART(1);
                agent.setALLPAY_STATE(5);
                agentDao.saveOrUpate(agent);

            }
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }*/
        return resultJO;
    }



}
