package com.cn.service.impl.businessServiceImpl;

import com.cn.common.LogHelper;
import com.cn.dao.AllpayFileUploadDao;
import com.cn.dao.AllpayMerchantDao;
import com.cn.entity.po.Allpay_FileUpload;
import com.cn.entity.po.Allpay_Merchant;
import com.cn.service.impl.kafkaserviceimpl.SendToKafkaServiceImpl;
import org.json.JSONObject;
import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.dao.AllpayComuserInfoDao;
import com.cn.entity.po.Allpay_ComUserInfo;
import com.cn.service.businessService.AllpayComuserInfoService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cn.common.CommonHelper.formatTime;
import static com.cn.common.CommonHelper.getStringToDate;

/**
 * Created by cuixiaowei on 2017/1/5.
 */
@Service("AllpayComuserInfoService")
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class AllpayComuserInfoServiceImpl implements AllpayComuserInfoService {

    @Autowired
    private AllpayComuserInfoDao allpayComuserInfoDao;
    @Autowired
    private AllpayMerchantDao allpayMerchantDao;
    @Autowired
    private AllpayFileUploadDao allpayFileUploadDao;
    @Autowired
    private SendToKafkaServiceImpl sendToKafkaService;
    @Override
    public String insert(Map<String, Object> source) throws Exception
    {
        source.get("imageList");//数组

        Allpay_ComUserInfo comUserInfo=null;
        if(CommonHelper.isEmpty(source.get("isComOrPerson").toString()))
        {
            return returnMissParamMessage("isComOrPerson");
        }
        if(CommonHelper.isEmpty(source.get("openCountBank").toString()))
        {
            return returnMissParamMessage("openCountBank");
        }
        if(CommonHelper.isEmpty(source.get("bankNum").toString()))
        {
            return returnMissParamMessage("bankNum");
        }
        if(CommonHelper.isEmpty(source.get("countInfo").toString()))
        {
            return returnMissParamMessage("countInfo");
        }
        if(CommonHelper.isEmpty(source.get("identityName").toString()))
        {
            return returnMissParamMessage("identityName");
        }
        if(CommonHelper.isEmpty(source.get("identityPaperNum").toString()))
        {
            return returnMissParamMessage("identityPaperNum");
        }
        if(CommonHelper.isEmpty(source.get("shopId").toString()))
        {
            return returnMissParamMessage("shopId");
        }
        if("1".equals(source.get("isComOrPerson")))
        {//公司信息
            if(CommonHelper.isEmpty(source.get("companyName").toString()))
            {
                return returnMissParamMessage("companyName");
            }

            /*if(CommonHelper.isEmpty(source.get("socialCreditCode").toString()))
            {
                return returnMissParamMessage("socialCreditCode");
            }
            if(CommonHelper.isEmpty(source.get("organizationCode").toString()))
            {
                return returnMissParamMessage("organizationCode");
            }*/

            if(CommonHelper.isEmpty(source.get("businessLicenseNum").toString()))
            {
                return returnMissParamMessage("businessLicenseNum");
            }
            if(CommonHelper.isEmpty(source.get("businessLaceAddress").toString()))
            {
                return returnMissParamMessage("businessLaceAddress");
            }

            comUserInfo= allpayMerchantDao.obtainComsumerInfo(source.get("shopId").toString());
            if(comUserInfo==null)
            {
                comUserInfo=new Allpay_ComUserInfo();
            }
            comUserInfo.setComuser_shopid(source.get("shopId").toString());
            comUserInfo.setComuser_countinfo(source.get("countInfo").toString());
            comUserInfo.setComuser_companyname(source.get("companyName").toString());
            comUserInfo.setComuser_opencountname(source.get("openCountName").toString());
            comUserInfo.setComuser_opencountbank(source.get("openCountBank").toString());
            comUserInfo.setComuser_identitypapernum(source.get("identityPaperNum").toString());
            comUserInfo.setComuser_identityname(source.get("identityName").toString());
            comUserInfo.setComuser_businesslicensnum(source.get("businessLicenseNum").toString());
            comUserInfo.setComuser_businessplaceaddress(source.get("businessLaceAddress").toString());
            comUserInfo.setComuser_iscomorperson(1);
            comUserInfo.setComuser_socialcreditcode(source.get("socialCreditCode").toString());
            comUserInfo.setComuser_organizationcode(source.get("organizationCode").toString());
            comUserInfo.setComuser_banknum(source.get("bankNum").toString());
            comUserInfo.setALLPAY_UPDATETIME(new Date());
            comUserInfo.setALLPAY_CREATETIME(new Date());
        }
        if("2".equals(source.get("isComOrPerson")))
        {//个人账户
            comUserInfo= allpayMerchantDao.obtainComsumerInfo(source.get("shopId").toString());
            if(comUserInfo==null)
            {
                comUserInfo=new Allpay_ComUserInfo();
            }
            comUserInfo.setComuser_identityname(source.get("identityName").toString());
            comUserInfo.setComuser_opencountbank(source.get("openCountBank").toString());
            comUserInfo.setComuser_countinfo(source.get("countInfo").toString());
            comUserInfo.setComuser_banknum(source.get("bankNum").toString());
            comUserInfo.setComuser_identitypapernum(source.get("identityPaperNum").toString());
            comUserInfo.setComuser_iscomorperson(2);
            comUserInfo.setALLPAY_UPDATETIME(new Date());
            comUserInfo.setALLPAY_CREATETIME(new Date());
        }
        String userNameFromMerCookie = null;
        if(!CommonHelper.isNullOrEmpty(source.get("userNameFromMerCookie"))){
            userNameFromMerCookie = source.get("userNameFromMerCookie").toString();
        }
        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW,
                comUserInfo.getALLPAY_LOGRECORD(), userNameFromMerCookie);
        String userName = publicFileds.getString("userName");
        Date now = getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");
        comUserInfo.setALLPAY_LOGRECORD(record);
        comUserInfo.setALLPAY_UPDATER(userName);
        comUserInfo.setALLPAY_UPDATETIME(now);
        String result=allpayComuserInfoDao.insert(comUserInfo);
        if("success".equals(result))
        {
            if(!CommonHelper.isEmpty(source.get("imageList").toString()))
            {
                allpayFileUploadDao.deleteByRecordId(comUserInfo.getComuser_id());
                List<Map<String, Object>> imageList=  obtainImageMsgList(source.get("imageList"));
                if(imageList!=null && imageList.size()>0)
                    for(int k=0;k<imageList.size();k++)
                    {
                        Allpay_FileUpload fileUpload = new Allpay_FileUpload();

                        imageList.get(k).get("imgName");
                        imageList.get(k).get("imgPath");
                        fileUpload.setFileupload_name(String.valueOf(imageList.get(k).get("imgName")));
                        fileUpload.setFileupload_path(String.valueOf(imageList.get(k).get("imgPath")));
                        fileUpload.setFileupload_record(comUserInfo.getComuser_id());
                        fileUpload.setFileupload_type("1");
                        allpayComuserInfoDao.insert(fileUpload);
                    }
            }
            return returnSuccessMessage();
        }
        return null;
    }

    @Override
    public String insertPosSet(Map<String, Object> source) throws Exception {
        if(CommonHelper.isEmpty(source.get("shopId").toString()))
        {
            return returnMissParamMessage("shopId");
        }
        if(CommonHelper.isEmpty(source.get("advertising").toString()))
        {
            return returnMissParamMessage("advertising");
        }
        if(CommonHelper.isEmpty(source.get("posLink").toString()))
        {
            return returnMissParamMessage("posLink");
        }
        if(CommonHelper.isEmpty(source.get("shopQrstStartDate").toString()))
        {
            return returnMissParamMessage("shopQrstStartDate");
        }
        if(CommonHelper.isEmpty(source.get("shopQrstEndDate").toString()))
        {
            return returnMissParamMessage("shopQrstEndDate");
        }
        if(CommonHelper.isEmpty(source.get("shopQrStartPrice").toString()))
        {
            return returnMissParamMessage("shopQrStartPrice");
        }
        if(CommonHelper.isEmpty(source.get("shopQrEndPrice").toString()))
        {
            return returnMissParamMessage("shopQrEndPrice");
        }

        Allpay_Merchant merchant= allpayMerchantDao.obtain(source.get("shopId").toString());
        if(merchant==null)
        {
            return returnNullObjectMsg(source.get("shopId").toString());
        }
        merchant.setMerchant_shopqrcode(source.get("posLink").toString());
        merchant.setMerchant_shopadvertising(source.get("advertising").toString());
        merchant.setMerchant_shopqrstartdate(getStringToDate(source.get("shopQrstStartDate").toString(), "yyyy-MM-dd"));
        merchant.setMerchant_shopqrenddate(getStringToDate(source.get("shopQrstEndDate").toString(), "yyyy-MM-dd"));
        merchant.setMerchant_shopqrstartprice(Integer.parseInt(source.get("shopQrStartPrice").toString()));
        merchant.setMerchant_shopqrendprice(Integer.parseInt(source.get("shopQrEndPrice").toString()));
        String userNameFromMerCookie = null;
        if(!CommonHelper.isNullOrEmpty(source.get("userNameFromMerCookie"))){
            userNameFromMerCookie = source.get("userNameFromMerCookie").toString();
        }
        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE,
                merchant.getALLPAY_LOGRECORD(), userNameFromMerCookie);
        String userName = publicFileds.getString("userName");
        Date now = getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");
        merchant.setALLPAY_UPDATETIME(now);
        merchant.setALLPAY_UPDATER(userName);
        merchant.setALLPAY_LOGRECORD(record);
        allpayMerchantDao.update(merchant);
        sendToKafkaService.sentMerchantToKafka(source.get("shopId").toString(),"");
        return returnSuccessMessage();
    }



    @Override
    public String getShopPosSetInfo(Map<String, Object> source) throws Exception
    {
        if(CommonHelper.isEmpty(CommonHelper.nullToString(source.get("shopId"))))
        {
            return returnMissParamMessage("shopId");
        }
        JSONObject resultJO=new JSONObject();
        Allpay_Merchant merchant= allpayMerchantDao.obtain(source.get("shopId").toString());
        if(merchant==null)
        {
            return returnNullObjectMsg("shopId");
        }
        resultJO.put(MsgAndCode.RSP_CODE,MsgAndCode.SUCCESS_CODE);
        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        resultJO.put("posLink", merchant.getMerchant_shopqrcode() == null ? "" : merchant.getMerchant_shopqrcode());
        resultJO.put("advertising",
                merchant.getMerchant_shopadvertising() == null ? "" : merchant.getMerchant_shopadvertising());
        if(null != merchant.getMerchant_shopqrstartdate()){
            resultJO.put("shopQrstStartDate", formatTime(merchant.getMerchant_shopqrstartdate(),"yyyy-MM-dd"));
        }else{
            resultJO.put("shopQrstStartDate", "");
        }
        if(null != merchant.getMerchant_shopqrenddate()){
            resultJO.put("shopQrstEndDate",formatTime(merchant.getMerchant_shopqrenddate(),"yyyy-MM-dd"));
        }else{
            resultJO.put("shopQrstEndDate","");
        }
        resultJO.put("shopQrStartPrice",merchant.getMerchant_shopqrstartprice());
        resultJO.put("shopQrEndPrice",merchant.getMerchant_shopqrendprice() );
        return resultJO.toString();
    }

    @Override
    public String updateShopInfo(Map<String, Object> source) throws Exception
    {
        LogHelper.info("修改商户基本信息："+source);
        if(CommonHelper.isEmpty(source.get("mhtId").toString()))
        {
            return returnMissParamMessage("商户id：mhtId");
        }
        if(CommonHelper.isEmpty(source.get("mhtName").toString()))
        {
            return returnMissParamMessage("商户名称：mhtName");
        }
        if(CommonHelper.isEmpty(source.get("mhtShortName").toString()))
        {
            return returnMissParamMessage("商户简称：mhtShortName");
        }
        if(CommonHelper.isEmpty(source.get("mhtProvinceId").toString()))
        {
            return returnMissParamMessage("省id：mhtProvinceId");
        }
        if(CommonHelper.isEmpty(source.get("mhtCityId").toString()))
        {
            return returnMissParamMessage("市id：mhtCityId");
        }
        if(CommonHelper.isEmpty(source.get("mhtCountryId").toString()))
        {
            return returnMissParamMessage("县id：mhtCountryId");
        }
        if(CommonHelper.isEmpty(source.get("mhtAddress").toString()))
        {
            return returnMissParamMessage("地址：mhtAddress");
        }
        if(CommonHelper.isEmpty(source.get("mhtIndustry").toString()))
        {
            return returnMissParamMessage("行业名称：mhtIndustry");
        }
        if(CommonHelper.isEmpty(source.get("mhtIndustryNum").toString()))
        {
            return returnMissParamMessage("行业代码：mhtIndustryNum");
        }
        if(CommonHelper.isEmpty(source.get("mhtManager").toString()))
        {
            return returnMissParamMessage("管理员：mhtManager");
        }
        if (CommonHelper.isEmpty(source.get("mhtManagerPhone").toString()))
        {
            return returnMissParamMessage("管理员手机号：mhtManagerPhone");
        }

        Allpay_Merchant merchant= allpayMerchantDao.obtain(source.get("mhtId").toString());
        if(merchant==null)
        {
            return returnNullObjectMsg("商户id：mhtId");
        }
        merchant.setMerchant_merchname(source.get("mhtName").toString());
        merchant.setMerchant_shopshoptname(source.get("mhtShortName").toString());
        merchant.setMerchant_provinceid(source.get("mhtProvinceId").toString());
        merchant.setMerchant_cityid(source.get("mhtCityId").toString());
        merchant.setMerchant_country_id(source.get("mhtCountryId").toString());
        merchant.setMerchant_address(source.get("mhtAddress").toString());
        merchant.setMerchant_industry(source.get("mhtIndustry").toString());
        merchant.setMerchant_industrynum(source.get("mhtIndustryNum").toString());
        merchant.setMerchant_contactsname(source.get("mhtManager").toString());
        merchant.setMerchant_contactsphone(source.get("mhtManagerPhone").toString());

        /*
        1 审核通过
        5 待审核
        非审核通过状态下重置为待审核
         */
        if(merchant.getALLPAY_STATE() != 1){
            merchant.setALLPAY_STATE(5);
        }

        String userNameFromMerCookie = null;
        if(!CommonHelper.isNullOrEmpty(source.get("userNameFromMerCookie"))){
            userNameFromMerCookie = source.get("userNameFromMerCookie").toString();
        }
        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE,
                merchant.getALLPAY_LOGRECORD(), userNameFromMerCookie);
        String userName = publicFileds.getString("userName");
        Date now = getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");

        merchant.setALLPAY_UPDATER(userName);
        merchant.setALLPAY_UPDATETIME(now);  //修改时间
        merchant.setALLPAY_LOGRECORD(record);
        allpayMerchantDao.update(merchant);
        sendToKafkaService.sentMerchantToKafka(source.get("mhtId").toString(),"");
        return returnSuccessMessage();

    }


    /**
     *
     * @param errorMessage
     * @return
     */
    private String returnMissParamMessage(String errorMessage){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);
        node.put(MsgAndCode.RSP_DESC, errorMessage+MsgAndCode.PARAM_MISSING_MSG);
        return node.toString();
    }
    /**
     * 返回处理成功信息
     * @return
     */
    private String returnSuccessMessage(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
        node.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        return node.toString();
    }
    /**
     * 根据参数无法　查询到相应的信息
     * @param errorMsg
     * @return
     */
    private String returnNullObjectMsg(String errorMsg){

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put(MsgAndCode.RSP_CODE, "00604");
        node.put(MsgAndCode.RSP_DESC, "根据传入的:"+errorMsg+" 未查到信息！");
        return node.toString();
    }

    /**
     *
     * @param source
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> obtainImageMsgList(Object source){

        if(source instanceof List<?>){
            List<Object> list = (List<Object>)source;
            List<Map<String,Object>> listObj = new ArrayList<Map<String,Object>>();
            for(Object obj : list){
                if(obj instanceof Map<?, ?>){
                    Map<String, Object> map = (Map<String, Object>)obj;
                    listObj.add(map);
                }
            }
            return listObj;
        }
        return new ArrayList<Map<String,Object>>();
    }
}
