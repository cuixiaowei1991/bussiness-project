package com.cn.service.impl.businessServiceImpl;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.dao.AllpayChannelSetDao;
import com.cn.dao.AllpayMerchantDao;
import com.cn.dao.AllpayStoreDao;
import com.cn.dao.ZfbKouBeiShopDao;
import com.cn.entity.po.Allpay_ChannelSet;
import com.cn.entity.po.Allpay_Merchant;
import com.cn.entity.po.Allpay_Store;
import com.cn.entity.po.KouBeiShop;
import com.cn.service.businessService.ZfbKouBeiShopService;

import com.cn.service.impl.kafkaserviceimpl.SendToKafkaServiceImpl;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by cuixiaowei on 2017/1/11.
 */
@Service("ZfbKouBeiShopService")
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class ZfbKouBeiShopServiceImpl implements ZfbKouBeiShopService {

    @Autowired
    private AllpayStoreDao allpayStoreDao;
    @Autowired
    private ZfbKouBeiShopDao zfbKouBeiShopDao;
	@Autowired
	private SendToKafkaServiceImpl sendToKafkaService;
	@Autowired
	private AllpayChannelSetDao allpayChannelSetDao;
	@Autowired
	private AllpayMerchantDao allpayMerchantDao;
	@Value("${img_Intranet_url}")
	private String img_Intranet_url;
    @Override
    public String insertZfbShop(Map<String, Object> source) throws Exception {

        /*if(CommonHelper.isNullOrEmpty(source.get("shopName")))
        {
            return returnMissParamMessage("商户名称：shopName");
        }*/
        if(CommonHelper.isNullOrEmpty(source.get("storeId")))
        {
            return returnMissParamMessage("门店id：storeId");
        }
        /*if(CommonHelper.isNullOrEmpty(source.get("brandName")))
        {
            return returnMissParamMessage("品牌名称：brandName");
        }*/
        /*if(CommonHelper.isNullOrEmpty(source.get("wftIndustry")))
        {
            return returnMissParamMessage("威富通行业：wftIndustry");
        }*/
        /*if(CommonHelper.isNullOrEmpty(source.get("storeMainName")))
        {
            return returnMissParamMessage("主店名称：storeMainName");
        }
        if(CommonHelper.isNullOrEmpty(source.get("storeFName")))
        {
            return returnMissParamMessage("分店名称：storeFName");
        }
        if(CommonHelper.isNullOrEmpty(source.get("brandLogoImg")))
        {
            return returnMissParamMessage("LOGO图片：brandLogoImg");
        }
        if(CommonHelper.isNullOrEmpty(source.get("country")))
        {
            return returnMissParamMessage("县：country");
        }
        if(CommonHelper.isNullOrEmpty(source.get("city")))
        {
            return returnMissParamMessage("市：city");
        }
        *//*if(CommonHelper.isNullOrEmpty(source.get("address")))
        {
            return returnMissParamMessage("地址：address");
        }*//*
        if(CommonHelper.isNullOrEmpty(source.get("latitude")))
        {
            return returnMissParamMessage("经纬度：latitude");
        }
        if(CommonHelper.isNullOrEmpty(source.get("storePhone")))
        {
            return returnMissParamMessage("门店电话：storePhone");
        }
        if(CommonHelper.isNullOrEmpty(source.get("businessTime")))
        {
            return returnMissParamMessage("营业时间：businessTime");
        }
        if(CommonHelper.isNullOrEmpty(source.get("avgPrice")))
        {
            return returnMissParamMessage("人均消费：avgPrice");
        }
        if(CommonHelper.isNullOrEmpty(source.get("isWifi")))
        {
            return returnMissParamMessage("是否存在WiFi：isWifi");
        }
        if(CommonHelper.isNullOrEmpty(source.get("isParking")))
        {
            return returnMissParamMessage("是否停车场：isParking");
        }
        if(CommonHelper.isNullOrEmpty(source.get("isNoSmoking")))
        {
            return returnMissParamMessage("是否有无吸烟区：isNoSmoking");
        }
        if(CommonHelper.isNullOrEmpty(source.get("isBox")))
        {
            return returnMissParamMessage("是否有包间：isBox");
        }
        if(CommonHelper.isNullOrEmpty(source.get("valueAdded")))
        {
            return returnMissParamMessage("其他服务信息：valueAdded");
        }
        if(CommonHelper.isNullOrEmpty(source.get("storeFirstImg")))
        {
            return returnMissParamMessage("门店首图：storeFirstImg");
        }
        if(CommonHelper.isNullOrEmpty(source.get("storeTouImg")))
        {
            return returnMissParamMessage("门店门头图片：storeTouImg");
        }
        if(CommonHelper.isNullOrEmpty(source.get("storeNeiImg")))
        {
            return returnMissParamMessage("门店内景图片：storeNeiImg");
        }
        if(CommonHelper.isNullOrEmpty(source.get("licenceImg")))
        {
            return returnMissParamMessage("营业执照图片：licenceImg");
        }
        if(CommonHelper.isNullOrEmpty(source.get("licenceCode")))
        {
            return returnMissParamMessage("营业执照注册号：licenceCode");
        }
        if(CommonHelper.isNullOrEmpty(source.get("licenceName")))
        {
            return returnMissParamMessage("营业执照名称：licenceName");
        }
        if(CommonHelper.isNullOrEmpty(source.get("certificateImg")))
        {
            return returnMissParamMessage("经营许可证：certificateImg");
        }
        if(CommonHelper.isNullOrEmpty(source.get("certificateExpires")))
        {
            return returnMissParamMessage("经营许可证有效期：certificateExpires");
        }
        if(CommonHelper.isNullOrEmpty(source.get("authLetterImg")))
        {
            return returnMissParamMessage("门店授权函图片：authLetterImg");
        }
        if(CommonHelper.isNullOrEmpty(source.get("isYuId")))
        {
            return returnMissParamMessage("开发返佣id：isYuId");
        }
        if(CommonHelper.isNullOrEmpty(source.get("notifyPhone")))
        {
            return returnMissParamMessage("收款成功短信接收手机号：notifyPhone");
        }
		if(CommonHelper.isNullOrEmpty(source.get("auditState")))
		{
			return returnMissParamMessage("口碑创建成功返回的审核状态：auditState");
		}
		if(CommonHelper.isNullOrEmpty(source.get("serialNumber")))
		{
			return returnMissParamMessage("口碑创建成功返回的流水号：serialNumber");
		}*/

		List<KouBeiShop> kouBeiShops= zfbKouBeiShopDao.getKouBeiByStoreID(source.get("storeId").toString());
		KouBeiShop kouBeiShop = null;
		if(kouBeiShops==null || kouBeiShops.size()<=0)
		{
			kouBeiShop=new KouBeiShop();
		}
		else
		{
			kouBeiShop=kouBeiShops.get(0);
		}
        //Allpay_Store allpayStore=allpayStoreDao.getStoreInfoByParameter("store_shopidnumber", String.valueOf(source.get("brandName")));

		if("AUDITING".equals(source.get("auditState").toString())||"PROCESS".equals(source.get("auditState").toString()))
		{//审核中
			kouBeiShop.setALLPAY_STATE(0);
		}
		if("AUDIT_FAILED".equals(source.get("auditState").toString())||"FAIL".equals(source.get("auditState").toString()))
		{//审核中
			kouBeiShop.setALLPAY_STATE(1);
		}
		if("AUDIT_SUCCESS".equals(source.get("auditState").toString())||"SUCCESS".equals(source.get("auditState").toString()))
		{//审核中
			kouBeiShop.setALLPAY_STATE(2);
		}
		if("INIT".equals(source.get("auditState").toString()))
		{//审核中
			kouBeiShop.setALLPAY_STATE(3);
		}
        kouBeiShop.setALLPAY_ISSTART(1);
        //kouBeiShop.setKoubeishop_name(String.valueOf(source.get("shopName")));
        kouBeiShop.setKoubeishop_store_id(String.valueOf(source.get("storeId")));
		kouBeiShop.setKoubeishop_cityname(String.valueOf(source.get("cityName")));
		kouBeiShop.setKoubeishop_countyname(String.valueOf(source.get("countryName")));
		kouBeiShop.setKoubeishop_provincename(String.valueOf(source.get("provinceName")));
        kouBeiShop.setKoubeishop_brandname(String.valueOf(source.get("brandName")));
        //kouBeiShop.set(String.valueOf(source.get("brandName")));
        kouBeiShop.setKoubeishop_mainname(String.valueOf(source.get("storeMainName")));
        kouBeiShop.setKoubeishop_name(String.valueOf(source.get("storeFName")));
        kouBeiShop.setKoubeishop_countyid(String.valueOf(source.get("country")));
        kouBeiShop.setKoubeishop_cityid(String.valueOf(source.get("city")));
        kouBeiShop.setKoubeishop_provinceid(String.valueOf(source.get("province")));
        kouBeiShop.setKoubeishop_address(String.valueOf(source.get("address")));
        kouBeiShop.setKoubeishop_longitude(String.valueOf(source.get("latitude")).split(",")[0]);//经度
        kouBeiShop.setKoubeishop_latitude(String.valueOf(source.get("latitude")).split(",")[1]);//维度
        kouBeiShop.setKoubeishop_firstcategorynum(String.valueOf(source.get("zfbIndustry1")));
        kouBeiShop.setKoubeishop_firstcategoryname(String.valueOf(source.get("zfbIndustryName1")));
        kouBeiShop.setKoubeishop_secondcategoryname(String.valueOf(source.get("zfbIndustryName2")));
        kouBeiShop.setKoubeishop_secondcategorynum(String.valueOf(source.get("zfbIndustry2")));
        kouBeiShop.setKoubeishop_threedcategorynum(String.valueOf(source.get("zfbIndustry3")));
        kouBeiShop.setKoubeishop_threedcategoryname(String.valueOf(source.get("zfbIndustryName3")));
        kouBeiShop.setKoubeishop_shoptel(String.valueOf(source.get("storePhone")));
        kouBeiShop.setKoubeishop_businesstime(String.valueOf(source.get("businessTime")));
        kouBeiShop.setKoubeishop_avgprice(String.valueOf(source.get("avgPrice")));
        kouBeiShop.setKoubeishop_wifi(String.valueOf(source.get("isWifi")));
        kouBeiShop.setKoubeishop_parking(String.valueOf(source.get("isParking")));
        kouBeiShop.setKoubeishop_nosmoking(String.valueOf(source.get("isNoSmoking")));
        kouBeiShop.setKoubeishop_box(String.valueOf(source.get("isBox")));
        kouBeiShop.setKoubeishop_valueadded(String.valueOf(source.get("valueAdded")));
        kouBeiShop.setKoubeishop_licencecode(String.valueOf(source.get("licenceCode")));
        kouBeiShop.setKoubeishop_licencename(String.valueOf(source.get("licenceName")));
        kouBeiShop.setKoubeishop_businesscertificateexpires(String.valueOf(source.get("certificateExpires")));
        kouBeiShop.setKoubeishop_isvuid(String.valueOf(source.get("isYuId")));
        kouBeiShop.setKoubeishop_notifymobile(String.valueOf(source.get("notifyPhone")));
		kouBeiShop.setKoubeishop_koubeinum(String.valueOf(source.get("serialNumber")));
        /**解析logo图片**/
		List<Map<String, Object>> logo = obtainImageMsgList(source.get("brandLogoImg"));
        kouBeiShop.setKoubeishop_brandlogo_zfb(checkTextIsOrNotNull(logo.get(0).get("imageId")));
        kouBeiShop.setKoubeishop_brandlogo(checkTextIsOrNotNull(logo.get(0).get("imagePath")));
        /**门店首图**/
		List<Map<String, Object>> first = obtainImageMsgList(source.get("storeFirstImg"));
		kouBeiShop.setKoubeishop_mainimg(checkTextIsOrNotNull(first.get(0).get("imagePath")));
		kouBeiShop.setKoubeishop_mainimg_zfb(checkTextIsOrNotNull(first.get(0).get("imageId")));

        /**门店门头图片**/
		List<Map<String, Object>> tou = obtainImageMsgList(source.get("storeTouImg"));
		kouBeiShop.setKoubeishop_shopheadimg_zfb(checkTextIsOrNotNull(tou.get(0).get("imageId")));
		kouBeiShop.setKoubeishop_shopheadimg(checkTextIsOrNotNull(tou.get(0).get("imagePath")));

        /**门店内景图片**/
        List<Map<String, Object>> nei = obtainImageMsgList(source.get("storeNeiImg"));
        if(nei.size() > 1){
        	kouBeiShop.setKoubeishop_indoorimg1(checkTextIsOrNotNull(nei.get(0).get("imagePath")));
        	kouBeiShop.setKoubeishop_indoorimg1_zfb(checkTextIsOrNotNull(nei.get(0).get("imageId")));
        	kouBeiShop.setKOUBEISHOP_INDOORIMG2(checkTextIsOrNotNull(nei.get(1).get("imagePath")));
        	kouBeiShop.setKoubeishop_indoorimg2_zfb(checkTextIsOrNotNull(nei.get(1).get("imageId")));
        }
        /**营业执照图片**/
		List<Map<String, Object>> licence = obtainImageMsgList(source.get("licenceImg"));
        kouBeiShop.setKoubeishop_licenceimg(checkTextIsOrNotNull(licence.get(0).get("imagePath")));
        kouBeiShop.setKoubeishop_licenceimg_zfb(checkTextIsOrNotNull(licence.get(0).get("imageId")));
        /**经营许可证图片**/
		List<Map<String, Object>> certificate = obtainImageMsgList(source.get("certificateImg"));
        kouBeiShop.setKoubeishop_businesscertificate(checkTextIsOrNotNull(certificate.get(0).get("imagePath")));
        kouBeiShop.setKoubeishop_businesscertificate_zfb(checkTextIsOrNotNull(certificate.get(0).get("imageId")));
        /**门店授权函图片**/
		List<Map<String, Object>> authLetter = obtainImageMsgList(source.get("authLetterImg"));
        kouBeiShop.setKoubeishop_authletter(checkTextIsOrNotNull(authLetter.get(0).get("imagePath")));
        kouBeiShop.setKoubeishop_authletter_zfb(checkTextIsOrNotNull(authLetter.get(0).get("imageId")));
		JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", ""+source.get("userNameFromBusCookie"));
		String userName = publicFileds.getString("userName");
		Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
		String record = publicFileds.getString("record");
		kouBeiShop.setALLPAY_UPDATETIME(now);
		kouBeiShop.setALLPAY_UPDATER(userName);
		kouBeiShop.setALLPAY_CREATER(userName);
		kouBeiShop.setALLPAY_LOGRECORD(record);
		kouBeiShop.setALLPAY_CREATETIME(now);
		zfbKouBeiShopDao.insertZfbShop(kouBeiShop);
        return returnSuccessMessage();
    }
    
    /**
     * 获取口碑店列表
     */
    @Override
	public String obtainShopList(Map<String, Object> source) throws Exception {
    	
    	
    	if(CommonHelper.isNullOrEmpty(source.get("curragePage"))
				|| CommonHelper.isNullOrEmpty(source.get("pageSize"))){
			returnMissParamMessage("curragePage或者pageSize");
		}
    	if(CommonHelper.isNullOrEmpty(source.get("shopName"))
				|| CommonHelper.isNullOrEmpty(source.get("brach"))
				|| CommonHelper.isNullOrEmpty(source.get("storeState"))){
			returnMissParamMessage("shopName或者brach或者storeState");
		}
    	int currentPage = Integer.parseInt(source.get("curragePage").toString());
		int pageSize = Integer.parseInt(source.get("pageSize").toString());
		/****/
		List<Map<String, Object>> list = zfbKouBeiShopDao.obtainShopList(source.get("shopName"), 
				source.get("brach"), source.get("storeState"), currentPage, pageSize);
		if(CommonHelper.isNullOrEmpty(list)){
			return returnNullObjectMsg("根据信息:"+source);
		}
		int total = zfbKouBeiShopDao.obtainShopNumber(source.get("shopName"), 
				source.get("brach"), source.get("storeState"));
    	/****/
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		for(Map<String, Object> map : list){
			ObjectNode nodeOne = mapper.createObjectNode();
			nodeOne.put("shopName", checkTextIsOrNotNull(map.get("MERCHANT_MERCHNAME")));
			nodeOne.put("storeCode", checkTextIsOrNotNull(map.get("STORE_SHOPIDNUMBER")));
			nodeOne.put("shopStortName", checkTextIsOrNotNull(map.get("MERCHANT_SHOPSHORTNAME")));
			nodeOne.put("storeStortName", checkTextIsOrNotNull(map.get("STORE_SHOPSHORTNAME")));
			nodeOne.put("kbStoreName", checkTextIsOrNotNull(map.get("KOUBEISHOP_MAINNAME")));
			nodeOne.put("Address", checkTextIsOrNotNull(map.get("STORE_ADDRESS")));
			nodeOne.put("zfbNum", checkTextIsOrNotNull(map.get("KOUBEISHOP_KOUBEINUM")));
			nodeOne.put("zfbKbId", checkTextIsOrNotNull(map.get("KOUBEISHOP_KOUBEIID")));
			nodeOne.put("storeState", checkTextIsOrNotNull(map.get("ALLPAY_STATE")));
			nodeOne.put("shopEId", checkTextIsOrNotNull(map.get("KOUBEISHOPE_ID")));
			nodeOne.put("shopId", checkTextIsOrNotNull(map.get("MERCHANT_ID")));
			nodeOne.put("storeId", checkTextIsOrNotNull(map.get("STORE_ID")));
			nodeOne.put("wechantToken", checkTextIsOrNotNull(map.get("CHANNELSET_PARAMETER_VALUE")));
			if(checkTextIsOrNotNull(map.get("CHANNELSET_CHANNEL_CODE")).equals("142"))
			{
				nodeOne.put("channelName", "ZLKJ");
			}
			if(checkTextIsOrNotNull(map.get("CHANNELSET_CHANNEL_CODE")).equals("147"))
			{
				nodeOne.put("channelName", "NJMTN");
			}

			array.add(nodeOne);
		}
		ObjectNode node = mapper.createObjectNode();
		node.put("lists", array);
		node.put("curragePage", currentPage);
		node.put("pageSize", pageSize);
		node.put("total", total);
		return returnSuccessMessage(node);
	}
     
    /**
     * 获取详情
     */
	@Override
	public String obtainShop(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("shopeId"))){
			returnMissParamMessage("shopeId");
		}
		Map<String, Object> map = zfbKouBeiShopDao.obtainShop(source.get("shopeId").toString());
		if(CommonHelper.isNullOrEmpty(map)){
			return returnNullObjectMsg("根据信息:"+source.get("shopeId").toString());
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("storeId", checkTextIsOrNotNull(map.get("KOUBEISHOP_STORE_ID")));
		node.put("shopeId", checkTextIsOrNotNull(map.get("KOUBEISHOPE_ID")));
		node.put("storeCode", checkTextIsOrNotNull(map.get("STORE_SHOPIDNUMBER")));
		node.put("brandName", checkTextIsOrNotNull(map.get("KOUBEISHOP_BRANDNAME")));
		node.put("zfbIndustry1", checkTextIsOrNotNull(map.get("KOUBEISHOP_FIRSTCATEGORYNUM")));
		node.put("zfbIndustryName1", checkTextIsOrNotNull(map.get("KOUBEISHOP_FIRSTCATEGORYNAME")));
		node.put("zfbIndustry2", checkTextIsOrNotNull(map.get("KOUBEISHOP_SECONDCATEGORYNUM")));
		node.put("zfbIndustryName2", checkTextIsOrNotNull(map.get("KOUBEISHOP_SECONDCATEGORYNAME")));
		node.put("zfbIndustry3", checkTextIsOrNotNull(map.get("KOUBEISHOP_THREEDCATEGORYNUM")));
		node.put("zfbIndustryName3", checkTextIsOrNotNull(map.get("KOUBEISHOP_THREEDCATEGORYNAME")));
		node.put("storeMainName", checkTextIsOrNotNull(map.get("KOUBEISHOP_MAINNAME")));
		node.put("storeFName", checkTextIsOrNotNull(map.get("KOUBEISHOP_NAME")));
		node.put("brandLogoImg", obtainImageForMsg(map.get("KOUBEISHOP_BRANDLOGO_ZFB"),
				map.get("KOUBEISHOP_BRANDLOGO")));
		node.put("country", checkTextIsOrNotNull(map.get("KOUBEISHOP_COUNTYID")));
		node.put("countryName", checkTextIsOrNotNull(map.get("KOUBEISHOP_COUNTYNAME")));
		node.put("city", checkTextIsOrNotNull(map.get("KOUBEISHOP_CITYID")));
		node.put("cityName", checkTextIsOrNotNull(map.get("KOUBEISHOP_CITYNAME")));
		node.put("province", checkTextIsOrNotNull(map.get("KOUBEISHOP_PROVINCEID")));
		node.put("provinceName", checkTextIsOrNotNull(map.get("KOUBEISHOP_PROVINCENAME")));
		node.put("address", checkTextIsOrNotNull(map.get("KOUBEISHOP_ADDRESS")));
		node.put("latitude", checkTextIsOrNotNull(map.get("KOUBEISHOP_LONGITUDE"))+","
		+checkTextIsOrNotNull(map.get("KOUBEISHOP_LATITUDE")));
		node.put("storePhone", checkTextIsOrNotNull(map.get("KOUBEISHOP_SHOPTEL")));
		node.put("businessTime", checkTextIsOrNotNull(map.get("KOUBEISHOP_BUSINESSTIME")));
		node.put("avgPrice", checkTextIsOrNotNull(map.get("KOUBEISHOP_AVGPRICE")));
		node.put("isWifi", transferData(checkTextIsOrNotNull(map.get("KOUBEISHOP_WIFI"))));
		node.put("isParking", transferData(checkTextIsOrNotNull(map.get("KOUBEISHOP_PARKING"))));
		node.put("isNoSmoking", transferData(checkTextIsOrNotNull(map.get("KOUBEISHOP_NOSMOKING"))));
		node.put("isBox", transferData(checkTextIsOrNotNull(map.get("KOUBEISHOP_BOX"))));
		node.put("valueAdded", checkTextIsOrNotNull(map.get("KOUBEISHOP_VALUEADDED")));
		node.put("storeFirstImg", obtainImageForMsg(map.get("KOUBEISHOP_MAINIMG_ZFB"),
				map.get("KOUBEISHOP_MAINIMG")));
		node.put("storeTouImg", obtainImageForMsg(map.get("KOUBEISHOP_SHOPHEADIMG_ZFB"),
				map.get("KOUBEISHOP_SHOPHEADIMG")));
		node.put("storeNeiImg", obtainImgArrayForMsg(map.get("KOUBEISHOP_INDOORIMG1_ZFB"),
				map.get("KOUBEISHOP_INDOORIMG1"),

				map.get("KOUBEISHOP_INDOORIMG2_ZFB"), 
				map.get("KOUBEISHOP_INDOORIMG2")));
		/****/
		node.put("licenceImg",obtainImageForMsg(map.get("KOUBEISHOP_LICENCEIMG_ZFB"),
				map.get("KOUBEISHOP_LICENCEIMG")));
		node.put("licenceCode", checkTextIsOrNotNull(map.get("KOUBEISHOP_LICENCECODE")));
		node.put("licenceName", checkTextIsOrNotNull(map.get("KOUBEISHOP_LICENCENAME")));
		node.put("certificateImg", obtainImageForMsg(map.get("KOUBEISHOP_CERTIFICATE_ZFB"),
				map.get("KOUBEISHOP_CERTIFICATE")));
		node.put("authLetterImg", obtainImageForMsg(map.get("KOUBEISHOP_AUTHLETTER_ZFB"),
				map.get("KOUBEISHOP_AUTHLETTER")));
		node.put("certificateExpires", checkTextIsOrNotNull(map.get("KOUBEISHOP_TIFICATEEXPIRES")));
		node.put("isYuId", checkTextIsOrNotNull(map.get("KOUBEISHOP_ISVUID")));
		node.put("notifyPhone", checkTextIsOrNotNull(map.get("KOUBEISHOP_NOTIFYMOBILE")));
		if(checkTextIsOrNotNull(map.get("CHANNELSET_CHANNEL_CODE")).equals("142"))
		{
			node.put("channelName", "ZLKJ");
		}
		if(checkTextIsOrNotNull(map.get("CHANNELSET_CHANNEL_CODE")).equals("147"))
		{
			node.put("channelName", "NJMTN");
		}
		return returnSuccessMessage(node);
	}

	@Override
	public String updateShop(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("shopeId"))){
			returnMissParamMessage("shopeId");
		}
		KouBeiShop shop = zfbKouBeiShopDao.obtainForId(source.get("shopeId").toString());
		/****/
		if(CommonHelper.isNullOrEmpty(shop)){
			return returnNullObjectMsg("根据信息:"+source.get("shopeId").toString());
		}
		updateShopForSource(shop, source, false);
		/**修改门店编码**/
		if(!CommonHelper.isNullOrEmpty(source.get("storeId"))
				&& !CommonHelper.isNullOrEmpty(source.get("storeCode"))){
			zfbKouBeiShopDao.updateStoreNumber(source.get("storeId").toString(), 
					source.get("storeCode").toString());
		}
		zfbKouBeiShopDao.update(shop);
		return returnSuccessMessage();
	}

	/**
	 * 2.2.6刷新支付宝状态
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@Override
	public String refreshZfbSate(Map<String, Object> source) throws Exception
	{
		LogHelper.info("刷新支付宝状态参数："+source);
		if(CommonHelper.isNullOrEmpty(source.get("shopeId"))){
			returnMissParamMessage("shopeId");
		}
		if(CommonHelper.isNullOrEmpty(source.get("zfbNum"))){
			returnMissParamMessage("zfbNum");
		}
		if(CommonHelper.isNullOrEmpty(source.get("shopId"))){
			returnMissParamMessage("shopId");
		}
		if(CommonHelper.isNullOrEmpty(source.get("shopToken"))){
			returnMissParamMessage("shopToken");
		}
		KouBeiShop kouBeiShop= zfbKouBeiShopDao.obtainForId(source.get("shopeId").toString());
		if(CommonHelper.isNullOrEmpty(kouBeiShop)){
			return returnNullObjectMsg("根据信息:"+source.get("shopeId").toString());
		}
		kouBeiShop.setALLPAY_UPDATETIME(new Date());
		kouBeiShop.setKoubeishop_auditmess(source.get("kbBackMess").toString());
		kouBeiShop.setKoubeishop_koubeiid(source.get("kbShopNum").toString());
		if("AUDITING".equals(source.get("kbAuditState").toString())||"PROCESS".equals(source.get("kbAuditState").toString()))
		{//审核中
			kouBeiShop.setALLPAY_STATE(0);
		}
		if("AUDIT_FAILED".equals(source.get("kbAuditState").toString())||"FAIL".equals(source.get("kbAuditState").toString()))
		{//审核中
			kouBeiShop.setALLPAY_STATE(1);
		}
		if("AUDIT_SUCCESS".equals(source.get("kbAuditState").toString())||"SUCCESS".equals(source.get("kbAuditState").toString()))
		{//审核中
			kouBeiShop.setALLPAY_STATE(2);
		}
		if("INIT".equals(source.get("kbAuditState").toString()))
		{//审核中
			kouBeiShop.setALLPAY_STATE(3);
		}
		zfbKouBeiShopDao.insertZfbShop(kouBeiShop);

		Allpay_Store allpayStore= allpayStoreDao.obtain(kouBeiShop.getKoubeishop_store_id());
		if(CommonHelper.isNullOrEmpty(allpayStore)){
			return returnNullObjectMsg("根据门店ID信息:"+source.get(kouBeiShop.getKoubeishop_store_id()).toString());
		}
		allpayStore.setStore_ReputationId(source.get("kbShopNum").toString());
		allpayStoreDao.update(allpayStore);
		sendToKafkaService.sentWangDianToKafka(kouBeiShop.getKoubeishop_store_id(), "");

		return returnSuccessMessage();
	}

	/**
	 * 存储支付宝token
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@Override
	public String saveZfbTokenInfo(Map<String, Object> source) throws Exception
	{
		LogHelper.info("存储支付宝token："+source);
		if(CommonHelper.isNullOrEmpty(source.get("shopPID"))){
			returnMissParamMessage("支付宝口碑ID：shopPID");
		}
		if(CommonHelper.isNullOrEmpty(source.get("channelNum"))){
			returnMissParamMessage("支付宝渠道号：channelNum");
		}
		/*if(CommonHelper.isNullOrEmpty(source.get("shopToken"))){
			returnMissParamMessage("支付宝口碑token：shopToken");
		}*/
		List<Map<String, Object>> zfbKouBeiMapList = zfbKouBeiShopDao.getKouBeiByPID(source.get("shopPID").toString());
		if(zfbKouBeiMapList==null || zfbKouBeiMapList.size()<=0)
		{
			return returnNullObjectMsg("根据支付宝口碑PID信息:"+source.get("shopPID").toString());
		}
		List<Allpay_ChannelSet> tokenList=zfbKouBeiShopDao.getKouBeiShopIDandToken(zfbKouBeiMapList.get(0).get("CHANNELSET_MERCHANT_ID").toString(),source.get("channelNum").toString(), "token");
		if(tokenList==null || tokenList.size()<=0)
		{
			return returnNullObjectMsg("根据商户ID信息:"+zfbKouBeiMapList.get(0).get("CHANNELSET_MERCHANT_ID").toString()+"和token标志，");
		}

		tokenList.get(0).setChannelset_parameter_value(source.get("shopToken").toString());
		allpayChannelSetDao.update(tokenList.get(0));
		List<Allpay_ChannelSet> tokenState = zfbKouBeiShopDao.getKouBeiShopIDandToken(zfbKouBeiMapList.get(0).get("CHANNELSET_MERCHANT_ID").toString(),source.get("channelNum").toString(), "tokenState");
		if(tokenState==null || tokenState.size()<=0)
		{
			return returnNullObjectMsg("根据商户ID信息:"+zfbKouBeiMapList.get(0).get("CHANNELSET_MERCHANT_ID").toString()+"和tokenState标志，");
		}
		if(CommonHelper.isNullOrEmpty(source.get("shopToken")))
		{
			//未授权
			tokenState.get(0).setChannelset_parameter_value("0");
		}
		else
		{
			//已授权
			tokenState.get(0).setChannelset_parameter_value("1");
		}

		allpayChannelSetDao.update(tokenState.get(0));
		sendToKafkaService.sentQvDaoToKafka(zfbKouBeiMapList.get(0).get("CHANNELSET_MERCHANT_ID").toString(),source.get("channelNum").toString());

		return returnSuccessMessage();
	}

	/**
	 * 2.2.7商户是否授权token
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@Override
	public String shopIsAuthorize(Map<String, Object> source) throws Exception
	{
		LogHelper.info("商户是否授权token："+source);

		if(CommonHelper.isNullOrEmpty(source.get("shopId")))
		{
			//未授权
			returnMissParamMessage("shopId");
		}

		List<Allpay_ChannelSet> tokenList=zfbKouBeiShopDao.getKouBeiShopIDandToken(source.get("shopId").toString(),"noCode", "token");
		if(tokenList==null || tokenList.size()<=0)
		{
			return returnNullObjectMsg("根据商户ID信息:"+source.get("shopId").toString()+"和token标志，");
		}
		List<Allpay_ChannelSet> pIDList=zfbKouBeiShopDao.getKouBeiShopIDandToken(source.get("shopId").toString(),"noCode", "shopPID");
		if(pIDList==null || pIDList.size()<=0)
		{
			return returnNullObjectMsg("根据商户ID信息:"+source.get("shopId").toString()+"和shopPID标志，");
		}
		Allpay_Merchant allpay_merchant= allpayMerchantDao.obtain(source.get("shopId").toString());
		if(allpay_merchant==null)
		{
			return returnNullObjectMsg("根据商户ID信息:"+source.get("shopId").toString()+"查询商户信息，");
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("token",tokenList.get(0).getChannelset_parameter_value()==null?"":tokenList.get(0).getChannelset_parameter_value());
		node.put("MchID",pIDList.get(0).getChannelset_parameter_value()==null?"":pIDList.get(0).getChannelset_parameter_value());
		node.put("shopOpenChannel",allpay_merchant.getMerchant_shopopenchannel()==null?"":allpay_merchant.getMerchant_shopopenchannel());
		node.put("shopOpenChannel",allpay_merchant.getMerchant_shopopenchannel()==null?"":allpay_merchant.getMerchant_shopopenchannel());
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
		node.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
		return node.toString();
	}

	/**
     * 
     * @param shop
     * @param source
     * @return
     */
	private String updateShopForSource(KouBeiShop shop,Map<String, Object> source,boolean must){
		
		//品牌名称
		if(!CommonHelper.isNullOrEmpty(source.get("brandName"))){
			shop.setKoubeishop_brandname(source.get("brandName").toString());
		}else{
			if(must){
				return returnMissParamMessage("brandName");
			}
		}
		//一级类目编号
		if(!CommonHelper.isNullOrEmpty(source.get("zfbIndustry1"))){
			shop.setKoubeishop_firstcategorynum(source.get("zfbIndustry1").toString());
		}else{
			if(must){
				return returnMissParamMessage("zfbIndustry1");
			}
		}
		//一级类目名称
		if(!CommonHelper.isNullOrEmpty(source.get("zfbIndustryName1"))){
			shop.setKoubeishop_firstcategoryname(source.get("zfbIndustryName1").toString());
		}else{
			if(must){
				return returnMissParamMessage("zfbIndustryName1");
			}
		}
		//二级类目编号
		if(!CommonHelper.isNullOrEmpty(source.get("zfbIndustry2"))){
			shop.setKoubeishop_secondcategorynum(source.get("zfbIndustry2").toString());
		}else{
			if(must){
				return returnMissParamMessage("zfbIndustry2");
			}
		}
		//二级类目名称
		if(!CommonHelper.isNullOrEmpty(source.get("zfbIndustryName2"))){
			shop.setKoubeishop_secondcategoryname(source.get("zfbIndustryName2").toString());
		}else{
			if(must){
				return returnMissParamMessage("zfbIndustryName2");
			}
		}
		//三级类目编号
		if(!CommonHelper.isNullOrEmpty(source.get("zfbIndustry3"))){
			shop.setKoubeishop_threedcategorynum(source.get("zfbIndustry3").toString());
		}else{
			if(must){
				return returnMissParamMessage("zfbIndustry3");
			}
		}
		//三级类目名称
		if(!CommonHelper.isNullOrEmpty(source.get("zfbIndustryName3"))){
			shop.setKoubeishop_threedcategoryname(source.get("zfbIndustryName3").toString());
		}else{
			if(must){
				return returnMissParamMessage("zfbIndustryName3");
			}
		}
		//主门店名
		if(!CommonHelper.isNullOrEmpty(source.get("storeMainName"))){
			shop.setKoubeishop_mainname(source.get("storeMainName").toString());
		}else{
			if(must){
				return returnMissParamMessage("storeMainName");
			}
		}
		//分门店名
		if(!CommonHelper.isNullOrEmpty(source.get("storeFName"))){
			shop.setKoubeishop_name(source.get("storeFName").toString());
		}else{
			if(must){
				return returnMissParamMessage("storeFName");
			}
		}
		//所属省份id
		if(!CommonHelper.isNullOrEmpty(source.get("province"))){
			shop.setKoubeishop_provinceid(source.get("province").toString());
		}else{
			if(must){
				return returnMissParamMessage("province");
			}
		}
		//所属省份名称
		if(!CommonHelper.isNullOrEmpty(source.get("provinceName"))){
			shop.setKoubeishop_provincename(source.get("provinceName").toString());
		}else{
			if(must){
				return returnMissParamMessage("provinceName");
			}
		}
		//所属城市id
		if(!CommonHelper.isNullOrEmpty(source.get("city"))){
			shop.setKoubeishop_cityid(source.get("city").toString());
		}else{
			if(must){
				return returnMissParamMessage("city");
			}
		}
		//所属城市名称
		if(!CommonHelper.isNullOrEmpty(source.get("cityName"))){
			shop.setKoubeishop_cityname(source.get("cityName").toString());
		}else{
			if(must){
				return returnMissParamMessage("cityName");
			}
		}
		//所属县级id
		if(!CommonHelper.isNullOrEmpty(source.get("country"))){
			shop.setKoubeishop_countyid(source.get("country").toString());
		}else{
			if(must){
				return returnMissParamMessage("country");
			}
		}
		//所属县级名称
		if(!CommonHelper.isNullOrEmpty(source.get("countryName"))){
			shop.setKoubeishop_countyname(source.get("countryName").toString());
		}else{
			if(must){
				return returnMissParamMessage("countryName");
			}
		}
		//地址
		if(!CommonHelper.isNullOrEmpty(source.get("address"))){
			shop.setKoubeishop_address(source.get("address").toString());
		}else{
			if(must){
				return returnMissParamMessage("address");
			}
		}
		//经纬度
		if(!CommonHelper.isNullOrEmpty(source.get("latitude"))){
			String[] strs = source.get("latitude").toString().split(",");
			shop.setKoubeishop_longitude(strs[0]);
			if(strs.length > 1){
				shop.setKoubeishop_latitude(strs[1]);
			}
		}else{
			if(must){
				return returnMissParamMessage("latitude");
			}
		}
		//营业时间
		if(!CommonHelper.isNullOrEmpty(source.get("businessTime"))){
			shop.setKoubeishop_businesstime(source.get("businessTime").toString());
		}else{
			if(must){
				return returnMissParamMessage("businessTime");
			}
		}
		//人均消费
		if(!CommonHelper.isNullOrEmpty(source.get("avgPrice"))){
			shop.setKoubeishop_avgprice(source.get("avgPrice").toString());
		}else{
			if(must){
				return returnMissParamMessage("avgPrice");
			}
		}
		//是否有WiFi  T有,F没有
		if(!CommonHelper.isNullOrEmpty(source.get("isWifi"))){
			shop.setKoubeishop_wifi(source.get("isWifi").toString());
		}else{
			if(must){
				return returnMissParamMessage("isWifi");
			}
		}
		//是否有停车场 T有,F没有
		if(!CommonHelper.isNullOrEmpty(source.get("isParking"))){
			shop.setKoubeishop_parking(source.get("isParking").toString());
		}else{
			if(must){
				return returnMissParamMessage("isParking");
			}
		}
		//是否有无烟区  T有,F没有
		if(!CommonHelper.isNullOrEmpty(source.get("isNoSmoking"))){
			shop.setKoubeishop_nosmoking(source.get("isNoSmoking").toString());
		}else{
			if(must){
				return returnMissParamMessage("isNoSmoking");
			}
		}
		//是否有包厢  T有,F没有
		if(!CommonHelper.isNullOrEmpty(source.get("isBox"))){
			shop.setKoubeishop_box(source.get("isBox").toString());
		}else{
			if(must){
				return returnMissParamMessage("isBox");
			}
		}
		//其他服务信息
		if(!CommonHelper.isNullOrEmpty(source.get("valueAdded"))){
			shop.setKoubeishop_valueadded(source.get("valueAdded").toString());
		}else{
			if(must){
				return returnMissParamMessage("valueAdded");
			}
		}
		//营业执照编码
		if(!CommonHelper.isNullOrEmpty(source.get("licenceCode"))){
			shop.setKoubeishop_licencecode(source.get("licenceCode").toString());
		}else{
			if(must){
				return returnMissParamMessage("licenceCode");
			}
		}
		//营业执照名称
		if(!CommonHelper.isNullOrEmpty(source.get("licenceName"))){
			shop.setKoubeishop_licenceimg(source.get("licenceName").toString());
		}else{
			if(must){
				return returnMissParamMessage("licenceName");
			}
		}
		//开发返佣id
		if(!CommonHelper.isNullOrEmpty(source.get("isYuId"))){
			shop.setKoubeishop_isvuid(source.get("isYuId").toString());
		}else{
			if(must){
				return returnMissParamMessage("isYuId");
			}
		}
		//收款成功短信接收手机
		if(!CommonHelper.isNullOrEmpty(source.get("notifyPhone"))){
			shop.setKoubeishop_notifymobile(source.get("notifyPhone").toString());
		}else{
			if(must){
				return returnMissParamMessage("notifyPhone");
			}
		}
		//经营许可证有效期
		if(!CommonHelper.isNullOrEmpty(source.get("certificateExpires"))){
			shop.setKoubeishop_businesscertificateexpires(source.get("certificateExpires").toString());
		}else{
			if(must){
				return returnMissParamMessage("certificateExpires");
			}
		}
		//Logo图片路径
		if(!CommonHelper.isNullOrEmpty(source.get("brandLogoImg"))){
			Map<String, Object> logo = obtainImageMsg(source.get("brandLogoImg"));
			if(!CommonHelper.isNullOrEmpty(logo.get("imagePath"))){
				shop.setKoubeishop_brandlogo(checkTextIsOrNotNull(logo.get("imagePath")));
			}
			//Logo图片支付宝id
			if(!CommonHelper.isNullOrEmpty(logo.get("imageId"))){
				shop.setKoubeishop_brandlogo_zfb(checkTextIsOrNotNull(logo.get("imageId")));
			}
		}
		//门店首图图片
		if(!CommonHelper.isNullOrEmpty(source.get("storeFirstImg"))){
			Map<String, Object> first = obtainImageMsg(source.get("storeFirstImg"));
			if(!CommonHelper.isNullOrEmpty(first.get("imagePath"))){
				shop.setKoubeishop_mainimg(checkTextIsOrNotNull(first.get("imagePath")));
			}
			//门店首图图片id
			if(!CommonHelper.isNullOrEmpty(first.get("imageId"))){
				shop.setKoubeishop_mainimg_zfb(checkTextIsOrNotNull(first.get("imageId")));
			}
		}
		//门店门头图片
		if(!CommonHelper.isNullOrEmpty(source.get("storeTouImg"))){
			Map<String, Object> tou = obtainImageMsg(source.get("storeTouImg"));
			if(!CommonHelper.isNullOrEmpty(tou.get("imagePath"))){
				shop.setKoubeishop_shopheadimg(checkTextIsOrNotNull(tou.get("imagePath")));
			}
			//门店门头图片id
			if(!CommonHelper.isNullOrEmpty(tou.get("imageId"))){
				shop.setKoubeishop_shopheadimg_zfb(checkTextIsOrNotNull(tou.get("imageId")));
			}
		}
		//门店内景图片
		if(!CommonHelper.isNullOrEmpty(source.get("storeNeiImg"))){
			List<Map<String, Object>> nei = obtainImageMsgList(source.get("storeNeiImg"));
	        if(nei.size() > 1){
	        	shop.setKoubeishop_indoorimg1(checkTextIsOrNotNull(nei.get(0).get("imagePath")));
	        	shop.setKoubeishop_indoorimg1_zfb(checkTextIsOrNotNull(nei.get(0).get("imageId")));
	        	shop.setKOUBEISHOP_INDOORIMG2(checkTextIsOrNotNull(nei.get(1).get("imagePath")));
	        	shop.setKoubeishop_indoorimg2_zfb(checkTextIsOrNotNull(nei.get(1).get("imageId")));
	        }
		}
		//营业执照图片
		if(!CommonHelper.isNullOrEmpty(source.get("licenceImg"))){
			/**营业执照图片**/
	        Map<String, Object> licence = obtainImageMsg(source.get("licenceImg"));
			if(!CommonHelper.isNullOrEmpty(licence.get("imagePath"))){
				shop.setKoubeishop_licenceimg(checkTextIsOrNotNull(licence.get("imagePath")));
			}
			if(!CommonHelper.isNullOrEmpty(licence.get("imageId"))){
				shop.setKoubeishop_licenceimg_zfb(checkTextIsOrNotNull(licence.get("imageId")));
			}
		}
        //经营许可证图片
		if(!CommonHelper.isNullOrEmpty(source.get("licenceImg"))){
			/**营业执照图片**/
			Map<String, Object> certificate = obtainImageMsg(source.get("certificateImg"));
			if(!CommonHelper.isNullOrEmpty(certificate.get("imagePath"))){
				shop.setKoubeishop_businesscertificate(checkTextIsOrNotNull(certificate.get("imagePath")));
			}
			if(!CommonHelper.isNullOrEmpty(certificate.get("imageId"))){
				shop.setKoubeishop_businesscertificate_zfb(checkTextIsOrNotNull(certificate.get("imageId")));
			}
		}
        //门店授权函图片
		if(!CommonHelper.isNullOrEmpty(source.get("licenceImg"))){
			Map<String, Object> authLetter = obtainImageMsg(source.get("authLetterImg"));
			if(!CommonHelper.isNullOrEmpty(authLetter.get("imagePath"))){
				shop.setKoubeishop_authletter(checkTextIsOrNotNull(authLetter.get("imagePath")));
			}
			if(!CommonHelper.isNullOrEmpty(authLetter.get("imageId"))){
				shop.setKoubeishop_authletter_zfb(checkTextIsOrNotNull(authLetter.get("imageId")));
			}
		}
		
		return null;
	}
	/***
	 * 
	 * @param source
	 * @return
	 */
	private String transferData(String source){
		
		if(source.equals("T")){
			return "T";
		}
		return "F";
	}
	
	
	private ObjectNode obtainImageForMsg(Object id,Object path){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("imagePath", img_Intranet_url+checkTextIsOrNotNull(path));
		node.put("imageId", checkTextIsOrNotNull(id));
		return node;
	}
	
	private ArrayNode obtainImgArrayForMsg(Object id,Object path,Object idT,Object pathT){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("imageId", checkTextIsOrNotNull(id));
		node.put("imagePath", img_Intranet_url+checkTextIsOrNotNull(path));
		ObjectNode nodeT = mapper.createObjectNode();
		nodeT.put("imageId", checkTextIsOrNotNull(idT));
		nodeT.put("imagePath", img_Intranet_url+checkTextIsOrNotNull(pathT));
		ArrayNode array = mapper.createArrayNode();
		array.add(node);
		array.add(nodeT);
		return array;
	}
	
    /**
     * 
     * @param source
     * @return
     */
    @SuppressWarnings("unchecked")
	private Map<String, Object> obtainImageMsg(Object source){
    	
    	if(source instanceof Map<?, ?>){
    		Map<String, Object> map = (Map<String, Object>)source;
    		return map;
    	}
    	return new HashMap<String, Object>();
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
	 * @param node
	 * @return
	 */
	private String returnSuccessMessage(ObjectNode node){
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
		node.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
		return node.toString();
	}
    /**
	 * 校验　非空
	 * @param obj
	 * @return
	 */
	private String checkTextIsOrNotNull(Object obj){
		
		if(!CommonHelper.isNullOrEmpty(obj)){
			return obj.toString();
		}
		return "";
	}
}
