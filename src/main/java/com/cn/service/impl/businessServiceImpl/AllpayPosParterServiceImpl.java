package com.cn.service.impl.businessServiceImpl;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.WriteExcelFile;
import com.cn.dao.AllpayPosParterDao;
import com.cn.entity.dto.AllpayAgentDto;
import com.cn.entity.dto.AllpayPosParterDto;
import com.cn.entity.po.Allpay_Agent;
import com.cn.entity.po.Allpay_PosParter;
import com.cn.service.businessService.AllpayPosParterService;
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
 * Created by sun.yayi on 2016/12/6.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Service
public class AllpayPosParterServiceImpl implements AllpayPosParterService {

    @Autowired
    private AllpayPosParterDao allpayPosparterDaoImpl;

    @Value("${excelpath}")
    private String excelpath;

    @Value("${outpath}")
    private String outpath;

    @Autowired
    private SendToKafkaServiceImpl sendToKafkaService;

    public void outDataPosParter(AllpayPosParterDto bean,HttpServletResponse response) throws Exception {
        List<String[]> posParterList = new ArrayList<String[]>();
        if(bean!=null){
            List<Map<String, Object>> list=allpayPosparterDaoImpl.obtainList(bean, null, null);
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    String[] ss = new String[5];
                    Map<String, Object> map1=list.get(i);
                    ss[0]=CommonHelper.nullToString(map1.get("POSPARTER_PARTERALLNAME"));
                    ss[1]=(CommonHelper.nullToString(map1.get("POSPARTER_PARTERLEVEL")));
                    if("1".equals(ss[1])){
                        ss[1] = "一级";
                    }else if("2".equals(ss[1])){
                        ss[1] = "二级";
                    }else{
                        ss[1] = "";
                    }
                    ss[2]=CommonHelper.nullToString(map1.get("ORGANIZATION_NAME"));
                    ss[3]=CommonHelper.nullToString(map1.get("ALLPAY_CREATETIME"));
                    ss[4]=CommonHelper.nullToString(map1.get("ALLPAY_ISSTART"));
                    if("1".equals(ss[4])){
                        ss[4] = "启用";
                    }else if("0".equals(ss[4])){
                        ss[4] = "停用";
                    }else{
                        ss[4] = "";
                    }
                    posParterList.add(ss);
                }

            }
        }
        String[] W = { "合作方名称", "合作方级别", "归属分子公司", "录入时间", "状态"};
        WriteExcelFile.writeExcel(posParterList, "POS_Parter", W, "POS_Parter", outpath, response);
    }

    @Override
    public JSONObject deletePosParterInfo(AllpayPosParterDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String parterId=bean.getParterId();
            if(CommonHelper.isEmpty(parterId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00318);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00318);
                return resultJO;
            }
            Allpay_PosParter posParter=allpayPosparterDaoImpl.getPosParterById(parterId);
            if(posParter!=null){
                JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, posParter.getALLPAY_LOGRECORD(),bean.getUserNameFromBusCookie());
                String userName = publicFileds.getString("userName");
                Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
                String record = publicFileds.getString("record");

                posParter.setALLPAY_UPDATER(userName);
                posParter.setALLPAY_UPDATETIME(now);  //修改时间
                posParter.setALLPAY_LOGRECORD(record);
                posParter.setALLPAY_LOGICDEL("2");
                allpayPosparterDaoImpl.saveOrUpdate(posParter);
                sendToKafkaService.sendApayPOSParter(parterId,"del");
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
                return resultJO;
            }
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    public JSONObject getList(AllpayPosParterDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String currentPage=bean.getCurragePage();
            String pageSize=bean.getPageSize();
            if(CommonHelper.isEmpty(currentPage) || CommonHelper.isEmpty(pageSize) ){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00110);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00110);
                return resultJO;
            }
            List<Map<String, Object>> list=allpayPosparterDaoImpl.obtainList(bean,currentPage, pageSize);
            int total=allpayPosparterDaoImpl.count(bean);
            JSONArray array=new JSONArray();
            if(list!=null && list.size()>0){
               for(int i=0;i<list.size();i++){
                  JSONObject object=new JSONObject();
                   Map<String, Object> map=list.get(i);
                   object.put("parterName",CommonHelper.nullToString(map.get("POSPARTER_PARTERALLNAME")));
                   object.put("parterId",CommonHelper.nullToString(map.get("POSPARTER_ID")));
                   object.put("parterLevel",CommonHelper.nullToString(map.get("POSPARTER_PARTERLEVEL")));
                   object.put("branchId",CommonHelper.nullToString(map.get("ORGANIZATION_ID")));
                   object.put("branchName",CommonHelper.nullToString(map.get("ORGANIZATION_NAME")));
                   object.put("createTime",CommonHelper.nullToString(map.get("ALLPAY_CREATETIME")));
                   object.put("parterState",CommonHelper.nullToString(map.get("ALLPAY_ISSTART")));
                   array.put(object);
               }
            }
            resultJO.put("lists",array);
            resultJO.put("curragePage",currentPage);
            resultJO.put("pageSize",pageSize);
            resultJO.put("total",total);
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    public JSONObject insertAllpayAgent(AllpayPosParterDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String parterName=bean.getParterName();
            String parterShortName=bean.getParterShortName();
            String expandPeople = bean.getExpandPeople();	//拓展人
            String parterLevel=bean.getParterLevel();
            String belongParterId=bean.getBelongParterId(); //上级合作方id
            String branchId=bean.getBranchId(); //分公司
            String provinceId=bean.getProvinceId(); //省
            String provinceName=bean.getProvinceName(); //省名
            String cityId=bean.getCityId(); //市ID
            String cityName=bean.getCityName(); //市明
            String countryId=bean.getCountryId();
            String countryName=bean.getCountryName();
            String parterAddress=bean.getParterAddress(); //地址
            String connectPerson=bean.getConnectPerson();//代理联系人
            String connectTel=bean.getConnectTel();//电话
            String parterState=bean.getParterState();

            if(CommonHelper.isEmpty(parterName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00319);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00319);
                return resultJO;
            }

            if(CommonHelper.isEmpty(expandPeople)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00305);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00305);
                return resultJO;
            }

            /*if(CommonHelper.isEmpty(parterShortName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00320);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00320);
                return resultJO;
            }
            if(CommonHelper.isEmpty(connectPerson)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00321);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00321);
                return resultJO;
            }
            if(CommonHelper.isEmpty(connectTel)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00322);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00322);
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
            if(CommonHelper.isEmpty(parterAddress)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00333);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00333);
                return resultJO;
            }*/
            if(CommonHelper.isEmpty(branchId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00325);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00325);
                return resultJO;
            }
            if(CommonHelper.isEmpty(parterLevel)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00324);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00324);
                return resultJO;
            }/*else if(2==Integer.parseInt(parterLevel)){
                if(CommonHelper.isEmpty(belongParterId)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00326);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00326);
                    return resultJO;
                }
            }*/
            if(CommonHelper.isEmpty(belongParterId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00326);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00326);
                return resultJO;
            }

            if(CommonHelper.isEmpty(parterState)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00334);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00334);
                return resultJO;
            }
            String parterId=CommonHelper.getRandomNum(9);
            List list=allpayPosparterDaoImpl.getPosPArterByParterId(parterId, null);
            while(list!=null && list.size()>0){
                parterId=CommonHelper.getRandomNum(9);
                list=allpayPosparterDaoImpl.getPosPArterByParterId(parterId, null);
            }
            Allpay_PosParter posParter=new Allpay_PosParter();
            posParter.setPosparter_parterid(parterId);
            posParter.setPosparter_parterallname(parterName);
            posParter.setPosparter_partershortname(parterShortName);
            posParter.setPosparter_connectperson(connectPerson);
            posParter.setPosparter_connecttel(connectTel);
            posParter.setPosparter_expandpeople(expandPeople);
            posParter.setPosparter_parterlevel(Integer.parseInt(parterLevel));
            posParter.setPosparter_address(parterAddress);
            posParter.setPosparter_branch_id(branchId);
            posParter.setPosparter_belongparterid(belongParterId);
            posParter.setPosparter_province_id(provinceId);
            posParter.setPosparter_province_name(provinceName);
            posParter.setPosparter_city_id(cityId);
            posParter.setPosparter_city_name(cityName);
            posParter.setPosparter_country_id(countryId);
            posParter.setPosparter_country_name(countryName);

            JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "",bean.getUserNameFromBusCookie());
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");

            posParter.setALLPAY_CREATER(userName);
            posParter.setALLPAY_CREATETIME(now);
            posParter.setALLPAY_UPDATETIME(now);  //修改时间
            posParter.setALLPAY_LOGICDEL("1");
            posParter.setALLPAY_LOGRECORD(record);
            posParter.setALLPAY_ISSTART(Integer.parseInt(parterState));
            posParter.setALLPAY_STATE(5);
            allpayPosparterDaoImpl.saveOrUpdate(posParter);
            sendToKafkaService.sendApayPOSParter(posParter.getPosparter_id(),"");
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    public JSONObject updateAllpayAgent(AllpayPosParterDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String parterId=bean.getParterId();
            String parterName=bean.getParterName();
            String parterShortName=bean.getParterShortName();
            String expandPeople = bean.getExpandPeople();	//拓展人
            String parterLevel=bean.getParterLevel();
            String belongParterId=bean.getBelongParterId(); //上级合作方id
            String branchId=bean.getBranchId(); //分公司
            String provinceId=bean.getProvinceId(); //省
            String provinceName=bean.getProvinceName(); //省名
            String cityId=bean.getCityId(); //市ID
            String cityName=bean.getCityName(); //市明
            String countryId=bean.getCountryId();
            String countryName=bean.getCountryName();
            String parterAddress=bean.getParterAddress(); //地址
            String connectPerson=bean.getConnectPerson();//代理联系人
            String connectTel=bean.getConnectTel();//电话
            String parterState=bean.getParterState();

            if(CommonHelper.isEmpty(parterId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00318);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00318);
                return resultJO;
            }
            if(CommonHelper.isEmpty(parterName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00319);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00319);
                return resultJO;
            }
            if(CommonHelper.isEmpty(expandPeople)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00305);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00305);
                return resultJO;
            }
            /*if(CommonHelper.isEmpty(parterShortName)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00320);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00320);
                return resultJO;
            }
            if(CommonHelper.isEmpty(connectPerson)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00321);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00321);
                return resultJO;
            }
            if(CommonHelper.isEmpty(connectTel)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00322);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00322);
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
            if(CommonHelper.isEmpty(parterAddress)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00333);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00333);
                return resultJO;
            }*/
            if(CommonHelper.isEmpty(branchId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00325);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00325);
                return resultJO;
            }
            if(CommonHelper.isEmpty(parterLevel)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00324);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00324);
                return resultJO;
            }/*else if(2==Integer.parseInt(parterLevel)){
                if(CommonHelper.isEmpty(belongParterId)){
                    resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00326);
                    resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00326);
                    return resultJO;
                }
            }*/
            if(CommonHelper.isEmpty(belongParterId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00326);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00326);
                return resultJO;
            }

            if(CommonHelper.isEmpty(parterState)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00334);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00334);
                return resultJO;
            }

            Allpay_PosParter posParter=allpayPosparterDaoImpl.getPosParterById(parterId);
            if(posParter!=null){
                posParter.setPosparter_parterallname(parterName);
                posParter.setPosparter_partershortname(parterShortName);
                posParter.setPosparter_connectperson(connectPerson);
                posParter.setPosparter_connecttel(connectTel);
                posParter.setPosparter_expandpeople(expandPeople);
                posParter.setPosparter_parterlevel(Integer.parseInt(parterLevel));
                posParter.setPosparter_branch_id(branchId);
                posParter.setPosparter_belongparterid(belongParterId);
                posParter.setPosparter_province_id(provinceId);
                posParter.setPosparter_province_name(provinceName);
                posParter.setPosparter_city_id(cityId);
                posParter.setPosparter_city_name(cityName);
                posParter.setPosparter_country_id(countryId);
                posParter.setPosparter_country_name(countryName);
                posParter.setPosparter_address(parterAddress);

                JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, posParter.getALLPAY_LOGRECORD(),bean.getUserNameFromBusCookie());
                String userName = publicFileds.getString("userName");
                Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
                String record = publicFileds.getString("record");

                posParter.setALLPAY_UPDATER(userName);
                posParter.setALLPAY_UPDATETIME(now);  //修改时间
                posParter.setALLPAY_LOGRECORD(record);
                posParter.setALLPAY_ISSTART(Integer.parseInt(parterState));
                allpayPosparterDaoImpl.saveOrUpdate(posParter);
                sendToKafkaService.sendApayPOSParter(parterId,"");
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
                return resultJO;
            }
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    public JSONObject getAllpayPosParterInfo(AllpayPosParterDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String parterId=bean.getParterId();
            if(CommonHelper.isEmpty(parterId)){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00318);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00318);
                return resultJO;
            }

            List<Map<String, Object>> posParter=allpayPosparterDaoImpl.getPosParterBySql(parterId);
            if(posParter!=null && posParter.size() >0){
                Map<String, Object> map = posParter.get(0);
                resultJO.put("parterId",parterId);
                resultJO.put("parterName",CommonHelper.nullToString(map.get("POSPARTER_PARTERALLNAME")));
                resultJO.put("parterShortName",CommonHelper.nullToString(map.get("POSPARTER_PARTERSHORTNAME")));
                resultJO.put("connectPerson",CommonHelper.nullToString(map.get("POSPARTER_CONNECTPERSON")));
                resultJO.put("connectTel",CommonHelper.nullToString(map.get("POSPARTER_CONNECTTEL")));
                resultJO.put("expandPeople",CommonHelper.nullToString(map.get("POSPARTER_EXPANDPEOPLE")));
                resultJO.put("parterLevel",CommonHelper.nullToString(map.get("POSPARTER_PARTERLEVEL")));
                resultJO.put("branchId",CommonHelper.nullToString(map.get("POSPARTER_BRANCH_ID")));
                resultJO.put("branchName",CommonHelper.nullToString(map.get("ORGANIZATION_NAME")));	//所属分公司名称
                resultJO.put("belongParterId",map.get("POSPARTER_BELONGPARTERID")==null?0:map.get("POSPARTER_BELONGPARTERID"));
                resultJO.put("belongParterName",CommonHelper.nullToString(map.get("SA_PARTERALLNAME")));	//上级代理商名称
                resultJO.put("provinceId",CommonHelper.nullToString(map.get("POSPARTER_PROVINCE_ID")));
                resultJO.put("provinceName",CommonHelper.nullToString(map.get("POSPARTER_PROVINCE_NAME")));
                resultJO.put("cityId",CommonHelper.nullToString(map.get("POSPARTER_CITY_ID")));
                resultJO.put("cityName",CommonHelper.nullToString(map.get("POSPARTER_CITY_NAME")));
                resultJO.put("countryId",CommonHelper.nullToString(map.get("POSPARTER_COUNTRY_ID")));
                resultJO.put("countryName",CommonHelper.nullToString(map.get("POSPARTER_COUNTRY_NAME")));
                resultJO.put("parterAddress",CommonHelper.nullToString(map.get("POSPARTER_ADDRESS")));
                resultJO.put("parterState",CommonHelper.nullToString(map.get("ALLPAY_ISSTART")));
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
                return resultJO;
            }
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }


}
