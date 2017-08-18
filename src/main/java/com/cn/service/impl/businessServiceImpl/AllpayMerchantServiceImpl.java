package com.cn.service.impl.businessServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.cn.entity.dto.AllpayTerminalDto;
import com.cn.service.impl.kafkaserviceimpl.SendToKafkaServiceImpl;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.JsonHelper;
import com.cn.common.LogHelper;
import com.cn.common.WriteExcelFile;
import com.cn.dao.AllpayMerchantDao;
import com.cn.dao.AllpayPosParterDao;
import com.cn.dao.AllpayStoreDao;
import com.cn.dao.TerminalDao;
import com.cn.dao.agentDao;
import com.cn.entity.po.Allpay_ComUserInfo;
import com.cn.entity.po.Allpay_Merchant;
import com.cn.entity.po.Allpay_Store;
import com.cn.entity.po.Allpay_Terminal;
import com.cn.service.businessService.AllpayMerchantService;

import org.springframework.transaction.annotation.Transactional;


/**
 * 商户管理
 * @author songzhili
 * 2016年12月1日下午2:15:13
 */
@Service("allpayMerchantService")
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class AllpayMerchantServiceImpl implements AllpayMerchantService {
   
	@Autowired
	private AllpayMerchantDao allpayMerchantDao;

	@Autowired
	private agentDao agentDao;

	@Autowired
	private AllpayPosParterDao allpayPosparterDaoImpl;

	@Autowired
	private AllpayStoreDao allpayStoreDao;

	@Autowired
	private TerminalDao terminalDao;
	
	@Value("${excelpath}")
	private String excelpath;

    @Value("${outpath}")
	private String outpath;
	@Value("${img_Intranet_url}")
	private String img_Intranet_url;

	@Autowired
	private SendToKafkaServiceImpl sendToKafkaService;
	/**
	 * 新增
	 */
	@Override
	public String insert(Map<String, Object> source) throws Exception{
		
		Allpay_Merchant merchant = new Allpay_Merchant();
		String result = insertOrUpdateMerchant(merchant, source, true);
		if(!CommonHelper.isNullOrEmpty(result)){
			return result;
		}
		String userNameFromBusCookie = CommonHelper.nullToString(source.get("userNameFromBusCookie"));
		String userNameFromAgentCookie = CommonHelper.nullToString(source.get("userNameFromAgentCookie"));
		JSONObject publicFileds;
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
		String merCode = allpayMerchantDao.getMerchaCode(9);  //随机生成9位不重复的商户编号
	    merchant.setALLPAY_CREATER(userName);  //创建人
	    merchant.setALLPAY_CREATETIME(now);  //创建时间
	    merchant.setALLPAY_UPDATETIME(now);  //修改时间
	    merchant.setALLPAY_UPDATER(userName);
	    merchant.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
	    merchant.setALLPAY_STATE(5);
	    merchant.setALLPAY_LOGRECORD(record);
		merchant.setMerchant_shopnumber(merCode);
		if(!CommonHelper.isNullOrEmpty(source.get("mhtManagerPhone"))){
			merchant.setMerchant_contactsphone(source.get("mhtManagerPhone").toString());
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtManager"))){
			merchant.setMerchant_contactsname(source.get("mhtManager").toString());
		}
		allpayMerchantDao.insert(merchant);
		sendToKafkaService.sentMerchantToKafka(merchant.getMerchant_id(),"");
		//创建门店
		if("1".equals(source.get("isCreateStore"))){
			Allpay_Store store = new Allpay_Store();
			insertStore(store, source);
			store.setALLPAY_CREATER(userName);  //创建人
			store.setALLPAY_CREATETIME(now);  //创建时间
			store.setALLPAY_UPDATETIME(now);  //修改时间
			store.setALLPAY_UPDATER(userName);
			store.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
			store.setALLPAY_STATE(1);
			store.setStore_merchant_id(merchant.getMerchant_id());
			store.setALLPAY_LOGRECORD(record);
			allpayStoreDao.insert(store);
			sendToKafkaService.sentWangDianToKafka(store.getStore_id(),"");
			//创建虚拟pos
			long randomNumber = generateRandomNumber(8);
			AllpayTerminalDto terminalDto = new AllpayTerminalDto();
			terminalDto.setTermCode(Long.toString(randomNumber));
			terminalDto.setMerchaCode(store.getStore_shopnumber());
			boolean boo = terminalDao.obtainByParamCode(terminalDto);
			while(boo){
				randomNumber = generateRandomNumber(8);
				terminalDto.setTermCode(Long.toString(randomNumber));
				terminalDto.setMerchaCode(store.getStore_shopnumber());
				boo = terminalDao.obtainByParamCode(terminalDto);
			}

			Allpay_Terminal terminal = new Allpay_Terminal();
			terminal.setTerminal_postype(3);
			terminal.setTerminal_merchant_id(store.getStore_merchant_id());
			terminal.setTerminal_store_id(store.getStore_id());
			terminal.setTerminal_merchcode(store.getStore_shopnumber());
			terminal.setTerminal_termcode(Long.toString(randomNumber));
			terminal.setALLPAY_ISSTART(1);//状态
			terminal.setALLPAY_CREATER(userName);  //创建人
			terminal.setALLPAY_CREATETIME(now);  //创建时间
			terminal.setALLPAY_UPDATETIME(now);  //修改时间
			terminal.setALLPAY_UPDATER(userName);
			terminal.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
			terminal.setALLPAY_LOGRECORD(record);
			terminal.setTerminal_permission(null);
			terminalDao.insert(terminal);
			sendToKafkaService.sendApayPosToKafka(terminal.getTerminal_posid(),"");
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("merchantId", merchant.getMerchant_id());
		node.put("mhtManager", checkTextIsOrNotNull(source.get("mhtManager")));
		node.put("mhtManagerPhone", checkTextIsOrNotNull(source.get("mhtManagerPhone")));
		return returnSuccessMessage(node);
	}
	/**
	 * 删除
	 */
	@Override
	public String delete(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("merchantId"))){
			return returnMissParamMessage("merchantId");
		}
		String merchantId = source.get("merchantId").toString();
		Allpay_Merchant merchant = allpayMerchantDao.obtain(merchantId);
		if(CommonHelper.isNullOrEmpty(merchant)){
			return returnNullObjectMsg("根据merchantId:"+merchantId);
		}
		boolean serious = false;
		if(!CommonHelper.isNullOrEmpty(source.get("serious"))
				&& "1".equals(source.get("serious"))){
			serious = true;
		}
		String userNameFromBusCookie = CommonHelper.nullToString(source.get("userNameFromBusCookie"));
		String userNameFromAgentCookie = CommonHelper.nullToString(source.get("userNameFromAgentCookie"));
		JSONObject publicFileds;
		if(!CommonHelper.isEmpty(userNameFromBusCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, merchant.getALLPAY_LOGRECORD(),
					userNameFromBusCookie);
		}else if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, merchant.getALLPAY_LOGRECORD(),
					userNameFromAgentCookie);
		}else{
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, merchant.getALLPAY_LOGRECORD(),
					null);
		}
		String record = publicFileds.getString("record");
		allpayMerchantDao.delete(merchantId, serious, record);
		sendToKafkaService.sentMerchantToKafka(merchantId,"del");
		return returnSuccessMessage();
	}
	/**
	 * 更新
	 */
	@Override
	public String update(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("merchantId"))){
			return returnMissParamMessage("merchantId");
		}
		String merchantId = source.get("merchantId").toString();
		Allpay_Merchant merchant = allpayMerchantDao.obtain(merchantId);
		if(CommonHelper.isNullOrEmpty(merchant)){
			return returnNullObjectMsg("根据merchantId:"+merchantId);
		}
		insertOrUpdateMerchant(merchant, source, false);
		String userNameFromBusCookie = CommonHelper.nullToString(source.get("userNameFromBusCookie"));
		String userNameFromAgentCookie = CommonHelper.nullToString(source.get("userNameFromAgentCookie"));
		JSONObject publicFileds;
		if(!CommonHelper.isEmpty(userNameFromBusCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, merchant.getALLPAY_LOGRECORD(),
					userNameFromBusCookie);
		}else if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, merchant.getALLPAY_LOGRECORD(),
					userNameFromAgentCookie);
		}else{
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, merchant.getALLPAY_LOGRECORD(),
					null);
		}
		String userName = publicFileds.getString("userName");
		Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
		String record = publicFileds.getString("record");
	    merchant.setALLPAY_UPDATETIME(now);  //修改时间
	    merchant.setALLPAY_UPDATER(userName);
	    merchant.setALLPAY_LOGRECORD(record);
		if(!CommonHelper.isNullOrEmpty(source.get("mhtManagerPhone"))){
			merchant.setMerchant_contactsphone(source.get("mhtManagerPhone").toString());
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtManager"))){
			merchant.setMerchant_contactsname(source.get("mhtManager").toString());
		}

		/*
        1 审核通过
        5 待审核
        非审核通过状态下重置为待审核
         */
		if(merchant.getALLPAY_STATE() != 1){
			merchant.setALLPAY_STATE(5);
		}

		allpayMerchantDao.update(merchant);
		sendToKafkaService.sentMerchantToKafka(merchant.getMerchant_id(),"");
		return returnSuccessMessage();
	}
	/**
	 * 修改商户状态
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@Override
	public String updateStatus(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("merchantId"))){
			return returnMissParamMessage("merchantId");
		}
		String remark = null;
		if(source.containsKey("remark")){
			remark = source.get("remark").toString();
		}
		allpayMerchantDao.updateStatus(source.get("merchantId").toString(), source.get("status").toString(), remark);
		sendToKafkaService.sentMerchantToKafka(source.get("merchantId").toString(),"");
		return returnSuccessMessage();
	}
	
	/**
	 * 获取详情
	 */
	@Override
	public String obtain(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("merchantId"))){
			return returnMissParamMessage("merchantId");
		}
		String merchantId = source.get("merchantId").toString();
		Map<String, Object> map = allpayMerchantDao.obtainMerMsgForMerchantId(merchantId);
		if(CommonHelper.isNullOrEmpty(map)){
			return returnNullObjectMsg("根据merchantId:"+merchantId);
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("mhtId", merchantId);
		node.put("mhtName", checkTextIsOrNotNull(map.get("MERCHANT_MERCHNAME")));
		node.put("mhtAgentId", checkTextIsOrNotNull(map.get("MERCHANT_AGENTID")));
		node.put("mhtAgentName", checkTextIsOrNotNull(map.get("AGENT_NAME")));
		node.put("branchId", checkTextIsOrNotNull(map.get("MERCHANT_BRANCHCOMPANYID")));	    //分公司id
		node.put("branchName", checkTextIsOrNotNull(map.get("MERCHANT_BRANCHCOMPANYNAME")));	//分公司名称
		node.put("parentAgentId", checkTextIsOrNotNull(map.get("AGENT_ID")));	//父级代理商id
		node.put("mhtShortName", checkTextIsOrNotNull(map.get("MERCHANT_SHOPSHORTNAME")));
		node.put("mhtProvinceId", checkTextIsOrNotNull(map.get("MERCHANT_PROVINCEID")));
		node.put("mhtCityId", checkTextIsOrNotNull(map.get("MERCHANT_CITYID")));
		node.put("mhtCountryId", checkTextIsOrNotNull(map.get("MERCHANT_COUNTRY_ID")));
		node.put("mhtAddress", checkTextIsOrNotNull(map.get("MERCHANT_ADDRESS")));
		node.put("mhtIndustry", checkTextIsOrNotNull(map.get("MERCHANT_INDUSTRY")));
		node.put("mhtIndustryNum", checkTextIsOrNotNull(map.get("MERCHANT_INDUSTRYNUM")));
		node.put("mhtState", checkTextIsOrNotNull(map.get("ALLPAY_ISSTART")));
		node.put("userId", checkTextIsOrNotNull(map.get("SHOPUSER_ID")));
		node.put("mhtManager", checkTextIsOrNotNull(map.get("MERCHANT_CONTACTSNAME")));
		node.put("mhtManagerPhone", checkTextIsOrNotNull(map.get("MERCHANT_CONTACTSPHONE")));
		node.put("mhtImgUrl", checkTextIsOrNotNull(map.get("MERCHANT_IMGURL")));
		node.put("status", checkTextIsOrNotNull(map.get("ALLPAY_STATE")));
		node.put("storeCount", checkTextIsOrNotNull(map.get("STORE_COUNT")));
		return returnSuccessMessage(node);
	}
	
	
	/**
	 * 获取商户已开通业务信息
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@Override
	public String obtainMerChannelMsg(Map<String, Object> source)
			throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("merchantId"))){
			return returnMissParamMessage("merchantId");
		}
		String merchantId = source.get("merchantId").toString();
		List<Map<String, Object>> list = allpayMerchantDao.obtainMerChannelMsg(merchantId);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		ArrayNode array = mapper.createArrayNode();
		if(!list.isEmpty()){
			list = transferChannelData(list);
			for(Map<String, Object> map : list){
				ObjectNode nodeOne = mapper.createObjectNode();
				nodeOne.put("businessId", checkTextIsOrNotNull(map.get("businessId")));
				nodeOne.put("businessName", checkTextIsOrNotNull(map.get("businessName")));
				nodeOne.put("businessCreateTime", checkTextIsOrNotNull(map.get("businessCreateTime")));
				nodeOne.put("businessState", checkTextIsOrNotNull(map.get("businessState")));
				nodeOne.put("businessChannelState", checkTextIsOrNotNull(map.get("businessChannelState")));
				array.add(nodeOne);
			}
		}
		node.put("lists", array);
		LogHelper.info("获取开通业务信息：" + node.toString());
		return returnSuccessMessage(node);
	}
	/**
	 * 获取商户企业信息
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@Override
	public String obtainMerBusinessMsg(Map<String, Object> source)
			throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("merchantId"))){
			return returnMissParamMessage("merchantId");
		}
		String merchantId = source.get("merchantId").toString();
		Allpay_ComUserInfo consumer = allpayMerchantDao.obtainComsumerInfo(merchantId);
		if(CommonHelper.isNullOrEmpty(consumer)){
			return returnNullObjectMsg("根据merchantId:"+merchantId);
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("merchantId", merchantId);
		node.put("companyName", checkTextIsOrNotNull(consumer.getComuser_companyname()));
		node.put("identityName", checkTextIsOrNotNull(consumer.getComuser_identityname()));
		node.put("businessLicenseNum", checkTextIsOrNotNull(consumer.getComuser_businesslicensnum()));
		node.put("openCountBank", checkTextIsOrNotNull(consumer.getComuser_opencountbank()));
		node.put("openCountName", checkTextIsOrNotNull(consumer.getComuser_opencountname()));
		node.put("bankNum", checkTextIsOrNotNull(consumer.getComuser_banknum()));
		node.put("socialCreditCode", checkTextIsOrNotNull(consumer.getComuser_socialcreditcode()));
		node.put("organizationCode", checkTextIsOrNotNull(consumer.getComuser_organizationcode()));
		node.put("identityName", checkTextIsOrNotNull(consumer.getComuser_identityname()));
		node.put("identityPaperNum", checkTextIsOrNotNull(consumer.getComuser_identitypapernum()));
		node.put("businessLicenseNum", checkTextIsOrNotNull(consumer.getComuser_businesslicensnum()));
		node.put("businessLaceAddress", checkTextIsOrNotNull(consumer.getComuser_businessplaceaddress()));
		node.put("isComOrPerson", checkTextIsOrNotNull(consumer.getComuser_iscomorperson()));
		node.put("countInfo", checkTextIsOrNotNull(consumer.getComuser_countinfo()));
		/**图片信息**/
		List<Map<String, Object>> imageList = allpayMerchantDao.obtainConsumerImage(consumer.getComuser_id());
		ArrayNode array = mapper.createArrayNode();
		if(!imageList.isEmpty()){
			for(Map<String, Object> image : imageList){
				if(!CommonHelper.isNullOrEmpty(image.get("FILEUPLOAD_PATH"))){
					ObjectNode nodeOne = mapper.createObjectNode();
					nodeOne.put("imgPath", img_Intranet_url+checkTextIsOrNotNull(image.get("FILEUPLOAD_PATH")));
					nodeOne.put("imgName", checkTextIsOrNotNull(image.get("FILEUPLOAD_NAME")));
					array.add(nodeOne);
				}
			}
		}
		node.put("imageList", array);
		return returnSuccessMessage(node);
	}
	
	@Override
	public String obtainByMerchantName(Map<String, Object> source)
			throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("mhtName"))){
			return returnMissParamMessage("mhtName");
		}
		boolean exist = allpayMerchantDao.obtainByMerchantName(source);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		if(exist){
			node.put("exist", "1");
		}else{
			node.put("exist", "0");
		}
		return returnSuccessMessage(node);
	}
	/**
	 * 获取列表
	 */
	@Override
	public String obtainList(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("curragePage"))
				|| CommonHelper.isNullOrEmpty(source.get("pageSize"))){
			return returnMissParamMessage("curragePage或者pageSize");
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		ArrayNode array = mapper.createArrayNode();
		/**查询列表**/
		List<Map<String, Object>> list = allpayMerchantDao.obtainList(source,false);
		/**总数**/
		int total = allpayMerchantDao.count(source);
		/**查询码表获取商户的状态**/
		Map<String, String> dictionaryData = allpayMerchantDao.obtainMerchantStateFromDictionary("shopState");
		/****/
		JsonHelper jsonHelper = new JsonHelper();
		if(!list.isEmpty()){
			for(int t=0;t<list.size();t++){
				Map<String, Object> map = list.get(t);
				ObjectNode nodeOne = mapper.createObjectNode();
				nodeOne.put("mhtId", checkTextIsOrNotNull(map.get("MERCHANT_ID")));
				nodeOne.put("mhtName", checkTextIsOrNotNull(map.get("MERCHANT_MERCHNAME")));
				//门店数量
				nodeOne.put("storeCount", checkNumberIsOrNotNull(map.get("STORE_COUNT")));
				nodeOne.put("mhtAgentId", checkTextIsOrNotNull(map.get("MERCHANT_AGENTID")));
				nodeOne.put("mhtAgentName", checkTextIsOrNotNull(map.get("AGENT_NAME")));
				nodeOne.put("mhtCreateTime", checkTextIsOrNotNull(map.get("CREATE_TIME")));
				nodeOne.put("mhtCreateName", checkTextIsOrNotNull(map.get("ALLPAY_CREATER")));
				//开通业务渠道数量
				nodeOne.put("openChannelCount", transferChannelCount(jsonHelper,
						checkTextIsOrNotNull(map.get("MERCHANT_SHOPOPENCHANNEL"))));
				//审核状态
				nodeOne.put("mhtApproveStatus", dictionaryData.get(checkNumberIsOrNotNull(map.get("ALLPAY_STATE"))));
				nodeOne.put("mhtState", checkTextIsOrNotNull(map.get("ALLPAY_ISSTART")));//是否启用
				nodeOne.put("mhtAddress", checkTextIsOrNotNull(map.get("MERCHANT_ADDRESS")));
				nodeOne.put("mhtShortName", checkTextIsOrNotNull(map.get("MERCHANT_SHOPSHORTNAME")));//商户简称
				nodeOne.put("branchId", checkTextIsOrNotNull(map.get("MERCHANT_BRANCHCOMPANYID")));//分公司id
				nodeOne.put("branchName", checkTextIsOrNotNull(map.get("MERCHANT_BRANCHCOMPANYNAME")));//分公司名称
				nodeOne.put("mhtIndustry", checkTextIsOrNotNull(map.get("MERCHANT_INDUSTRY")));
				nodeOne.put("mhtIndustryNum", checkTextIsOrNotNull(map.get("MERCHANT_INDUSTRYNUM")));
				nodeOne.put("agentLevel", checkTextIsOrNotNull(map.get("AGENT_LEVEL")));//代理商级别
				if(map.containsKey("ALLPAY_ISSTART")){
					if("0".equals(map.get("ALLPAY_ISSTART").toString())){
						nodeOne.put("isStart", "停用");//是否　启用状态
					}else{
						nodeOne.put("isStart", "启用");//是否　启用状态
					}
				}else{
					nodeOne.put("isStart", "停用");//是否　启用状态
				}
				array.add(nodeOne);
			}
		}
		node.put("lists", array);
		node.put("curragePage", source.get("curragePage").toString());
		node.put("pageSize", source.get("pageSize").toString());
		node.put("total", total);
		return returnSuccessMessage(node);
	}
	/**
	 * 到处excel
	 */
	@Override
	public void exportExcelForList(Map<String, Object> source,HttpServletResponse response) throws Exception{
		
		
		/**查询列表**/
		List<Map<String, Object>> list = allpayMerchantDao.obtainList(source,true);
		/**查询码表获取商户的状态**/
		Map<String, String> dictionaryData = allpayMerchantDao.obtainMerchantStateFromDictionary("shopState");
		if(!list.isEmpty()){
			 List<String[]> exportList = new ArrayList<String[]>();
			 JsonHelper jsonHelper = new JsonHelper();
			 int size = list.size();
			 for(int index = 0;index < size;index++){
				 Map<String, Object> map = list.get(index);
				 String[] export = new String[19];
				 export[0] = Integer.toString(index+1);
				 export[1] = checkTextIsOrNotNull(map.get("MERCHANT_MERCHNAME"));
				 export[2] = checkNumberIsOrNotNull(map.get("STORE_COUNT"));
				 export[3] = checkTextIsOrNotNull(map.get("ORGANIZATION_NAME"));
				 export[4] = checkTextIsOrNotNull(map.get("AGENT_NAME"));
				 export[5] = checkTextIsOrNotNull(map.get("AGENT_LEVEL"));
				 if("1".equals(export[5])){
					 export[5] = "一级";
				 }else if("2".equals(export[5])){
					 export[5] = "二级";
				 }else{
					 export[5] = "";
				 }
				 export[6] = checkTextIsOrNotNull(map.get("AGENT_LEGALPERSONNAME"));
				 export[7] = checkTextIsOrNotNull(map.get("CREATE_TIME"));
				 export[8] = checkTextIsOrNotNull(map.get("ALLPAY_CREATER"));
				 export[9] = Integer.toString(transferChannelCount(jsonHelper,
							checkTextIsOrNotNull(map.get("MERCHANT_SHOPOPENCHANNEL"))));
				 export[10] = dictionaryData.get(checkNumberIsOrNotNull(map.get("ALLPAY_STATE")));
				 export[11] =  transferIsStart(checkTextIsOrNotNull(map.get("ALLPAY_ISSTART")));
				 export[12] = checkTextIsOrNotNull(map.get("MERCHANT_INDUSTRY"));
				 export[13] = checkTextIsOrNotNull(map.get("MERCHANT_PROVINCENAME"));
				 export[14] = checkTextIsOrNotNull(map.get("MERCHANT_CITYNAME"));
				 export[15] = checkTextIsOrNotNull(map.get("MERCHANT_ADDRESS"));
				 export[16] = checkNumberIsOrNotNull(map.get("RUAN_POS_COUNT"));
				 export[17] = checkNumberIsOrNotNull(map.get("CHUAN_POS_COUNT"));
				 export[18] = checkNumberIsOrNotNull(map.get("PC_POS_COUNT"));
				 exportList.add(export);
			 }
			 String[] columnName = { "序号", "商户信息", "门店数量","所属分子公司","所属代理","所属代理商级别","代理商管理员",
					 "录入时间","录入者","已开业务","审核状态","启用状态","所属行业","省份","城市","地址","软POS数量",
					 "传统POS数量","pcPOS数量"};
			 WriteExcelFile.writeExcel(exportList, "MERCHANT_LIST", columnName, "MERCHANT_LIST",
					 outpath, response);
		}
	}
	/**
	 * 商户、门店及POS终端导入
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JSONObject importMerStoPos(Map<String, Object> source) throws Exception {
		JSONObject resultJO=new JSONObject();
        if(source!=null){
			// 商户名称(必填)
			String mhtName=checkTextIsOrNotNull(source.get("mhtName"));
			// 商户简称(必填)
			String mhtShortName=checkTextIsOrNotNull(source.get("mhtShortName"));
			// 商户所属分公司（必填）
			String branchName=checkTextIsOrNotNull(source.get("branchName"));
			// 商户所属代理编号（必填）
			String agentNum=checkTextIsOrNotNull(source.get("agentNum"));
			// 所属行业（必填）
			String mhtIndustry=checkTextIsOrNotNull(source.get("mhtIndustry"));
			// 省份（必填）
			String mhtProvinceName=checkTextIsOrNotNull(source.get("mhtProvinceName"));
			// 地市（必填）
			String mhtCityName=checkTextIsOrNotNull(source.get("mhtCityName"));
			//省ID（必填）
			String mhtProvinceId=checkTextIsOrNotNull(source.get("mhtProvinceId"));
			//市ID（必填）
			String mhtCityId=checkTextIsOrNotNull(source.get("mhtCityId"));
			// 地址（必填）
			String mhtAddress=checkTextIsOrNotNull(source.get("mhtAddress"));
			// 门店名称( 必填)
			String storeName=checkTextIsOrNotNull(source.get("storeName"));
			// 门店简称( 必填)
			String storeShortName=checkTextIsOrNotNull(source.get("storeShortName"));
			// pos类型(1:传统pos,4:pc版pos)
			String posType=checkTextIsOrNotNull(source.get("posType"));
			// 县级
			String mhtCountryName=checkTextIsOrNotNull(source.get("mhtCountryName"));
			//县ID
			String mhtCountryId=checkTextIsOrNotNull(source.get("mhtCountryId"));
			// 联系人姓名
			String mhtManager=checkTextIsOrNotNull(source.get("mhtManager"));
			// 联系人电话（座机必须带-）
			String mhtManagerPhone=checkTextIsOrNotNull(source.get("mhtManagerPhone"));
			// 门店地址(不填用商户的)
			String storeAddress=checkTextIsOrNotNull(source.get("storeAddress"));
			// POS商户号（不填自动生成）
			String merchaCode=checkTextIsOrNotNull(source.get("merchaCode"));
			// 终端编号
			String termCode=checkTextIsOrNotNull(source.get("termCode"));
			// 终端所属pos合作者
			String posParterName=checkTextIsOrNotNull(source.get("posParterName"));
			//pos配件所属pos合作者
			String peiJianPosParterName = checkTextIsOrNotNull(source.get("peiJianPosParterName"));

			if(CommonHelper.isEmpty(mhtName)){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00400);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00400);
				return resultJO;
			}
			if(CommonHelper.isEmpty(mhtShortName)){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00401);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00401);
				return resultJO;
			}
			if(CommonHelper.isEmpty(branchName)){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00402);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00402);
				return resultJO;
			}
			if(CommonHelper.isEmpty(agentNum)){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00403);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00403);
				return resultJO;
			}
			if(CommonHelper.isEmpty(mhtIndustry)){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00404);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00404);
				return resultJO;
			}
			if(CommonHelper.isEmpty(mhtProvinceId) || CommonHelper.isEmpty(mhtProvinceName)){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00414);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00414);
				return resultJO;
			}
			if(CommonHelper.isEmpty(mhtCityId) || CommonHelper.isEmpty(mhtCityName)){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00419);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00419);
				return resultJO;
			}
			if(CommonHelper.isEmpty(mhtAddress)){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00315);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00315);
				return resultJO;
			}
			if(CommonHelper.isEmpty(storeName)){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00405);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00405);
				return resultJO;
			}
			if(!CommonHelper.isEmpty(merchaCode) && merchaCode.length() != 15){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00406);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00406);
				return resultJO;
			}
			if(!CommonHelper.isEmpty(termCode) && termCode.length() != 8){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00208);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00208);
				return resultJO;
			}
			if(posType.length() != 0) {
				if(!"1".equals(posType) && !"4".equals(posType)) {
					resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00206);
					resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00206);
					return resultJO;
				}
			}
			if(CommonHelper.isEmpty(storeShortName)){
				storeShortName = storeName;
			}
			if(CommonHelper.isEmpty(storeAddress)){
				storeAddress = mhtAddress;
			}

			/*
			判断手机号是否存在
			 */
			if(!CommonHelper.isEmpty(mhtManagerPhone)){
				List list = allpayMerchantDao.checkMerName(null, mhtManagerPhone);
				if(null != list && list.size() > 0){
					resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
					resultJO.put(MsgAndCode.RSP_DESC, "联系人电话已经存在！");
					return resultJO;
				}
			}

			/*
			判断商户名称是否存在
			 */
			List<Map<String, Object>> listMer =
					(List<Map<String, Object>>)allpayMerchantDao.checkMerName(mhtName, null);
			if(null != listMer && listMer.size() > 0){
				/*
				判断所属分公司是否存在
				 */
				int branCount = agentDao.CheckBranch(branchName);
				if(branCount > 0){
					/*
					判断所属代理商是否存在
					 */
					int agentCount = agentDao.CheckAgent(agentNum);
					if(agentCount > 0){
						/*
						判断该商户下门店名称是否存在
						 */
						List<Map<String, Object>> listStore =
								(List<Map<String, Object>>)allpayStoreDao.checkStore(mhtName, storeName);
						if(null != listStore && listStore.size() > 0){
							resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00416);
							resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00416);
							return resultJO;
						}
					}else{
						resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00415);
						resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00415);
						return resultJO;
					}
				}else{
					resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00415);
					resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00415);
					return resultJO;
				}
			}

			/*
			查询分公司id
			 */
			List<Map<String, Object>> branchs =(List<Map<String, Object>>)agentDao.selectBranch(branchName);
			String branchId;
			if(branchs!=null && branchs.size()>0){
				branchId=branchs.get(0).get("ORGANIZATION_ID").toString();
			}else{
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00407);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00407);
				return resultJO;
			}

			/*
			查询行业代码
			 */
			List<Map<String, Object>> dataList =
					(List<Map<String, Object>>) agentDao.selectDataDictionary("industry", mhtIndustry);
			String industryNum;
			if(null != dataList && dataList.size() > 0){
				industryNum = dataList.get(0).get("DATA_CODE").toString();
			}else{
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00411);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00411);
				return resultJO;
			}

			/*
			查询代理商id
			 */
			List<Map<String, Object>> agents = agentDao.getAgentByAgentNum(agentNum, null);
			String agentId;
			if(null != agents && agents.size() > 0){
				agentId = agents.get(0).get("AGENT_ID").toString();
			}else{
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00412);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00412);
				return resultJO;
			}

			/*
			查询终端所属pos合作方id
			 */
			List<Map<String, Object>> list =
					(List<Map<String, Object>>)allpayPosparterDaoImpl.getPosPArterByParterId(null, posParterName);
			if(null == list || list.size() ==0){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00340);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00340);
				return resultJO;
			}
			String posParterId =list.get(0).get("POSPARTER_ID").toString();

			/*
			查询配件所属pos合作方id
			 */
			List<Map<String, Object>> list1 =
					(List<Map<String, Object>>)allpayPosparterDaoImpl.getPosPArterByParterId(null, peiJianPosParterName);
			if(null == list1 || list1.size() ==0){
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00341);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00341);
				return resultJO;
			}
			String peiJianPosParterId = list1.get(0).get("POSPARTER_ID").toString();

			/*
			验证终端编号和商户编号是否重复
			 */
			if(!CommonHelper.isEmpty(termCode) && !CommonHelper.isEmpty(merchaCode)){
				AllpayTerminalDto terminalDto = new AllpayTerminalDto();
				terminalDto.setTermCode(termCode);
				terminalDto.setMerchaCode(merchaCode);
				boolean boo = terminalDao.obtainByParamCode(terminalDto);
				if(boo){
					resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00417);
					resultJO.put(MsgAndCode.RSP_DESC, merchaCode+", "+termCode+" "+MsgAndCode.MESSAGE_00417);
					return resultJO;
				}
			}

			//新增商户结果
			String result = "";
			//商户id
			String merId = "";

			/*
			获取公共字段
			 */
			String userNameFromBusCookie = CommonHelper.nullToString(source.get("userNameFromBusCookie"));
			JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_BULK_IMPORT, "",
					userNameFromBusCookie);
			String userName = publicFileds.getString("userName");
			Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
			String record = publicFileds.getString("record");

			if(null == listMer || listMer.size() == 0){
				//随机生成9位不重复的商户编号
				String merCode = allpayMerchantDao.getMerchaCode(9);
				/*
				插入商户信息
				 */
				Allpay_Merchant merchant = new Allpay_Merchant();
				merchant.setMerchant_merchname(mhtName);  //商户名称
				merchant.setMerchant_shopnumber(merCode);	//商户9位编号
				merchant.setMerchant_shopshoptname(mhtShortName);	//商户简称
				merchant.setMerchant_address(mhtAddress);	//商户地址
				merchant.setMerchant_industry(mhtIndustry);	//行业名称
				merchant.setMerchant_industrynum(industryNum);	//行业代码
				merchant.setMerchant_provinceid(mhtProvinceId);	//省代码
				merchant.setMerchant_provincename(mhtProvinceName);  //省名称
				merchant.setMerchant_cityid(mhtCityId);	//市代码
				merchant.setMerchant_cityname(mhtCityName);	//市名称
				merchant.setMerchant_country_id(mhtCountryId);	//区县代码
				merchant.setMerchant_country_name(mhtCountryName);	//区县名称
				merchant.setMerchant_branchcompanyid(branchId);	//所属分公司ID
				merchant.setMerchant_branchcompanyname(branchName);	//分公司名称
				merchant.setMerchant_agentid(agentId);	//代理ID
				merchant.setMerchant_contactsname(mhtManager);   //联系人
				merchant.setMerchant_contactsphone(mhtManagerPhone);  //联系人电话
				merchant.setALLPAY_CREATER(userName);
				merchant.setALLPAY_CREATETIME(now);
				merchant.setALLPAY_UPDATETIME(now);  //修改时间
				merchant.setALLPAY_LOGICDEL("1");
				merchant.setALLPAY_LOGRECORD(record);
				merchant.setALLPAY_ISSTART(1);  //启用
				merchant.setALLPAY_STATE(1);    //审核通过
				result = allpayMerchantDao.insert(merchant);

				if("success".equals(result)){
					merId = merchant.getMerchant_id();
					sendToKafkaService.sentMerchantToKafka(merchant.getMerchant_id(),"");
				}else{
					resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00408);
					resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00408);
					return resultJO;
				}
			}else{
				merId = listMer.get(0).get("MERCHANT_ID").toString();
			}

			//虚拟pos终端号
			String termCodeVir;
			if(CommonHelper.isEmpty(merchaCode)){
				//随机生成15位不重复的POS商户号
				merchaCode = allpayMerchantDao.getPosMerCode(15);
			}

			/*
			插入门店信息
			 */
			Allpay_Store store = new Allpay_Store();
			store.setStore_merchant_id(merId);	//商户主建
			store.setStore_shopidnumber(allpayStoreDao.getShopIdNum(9));  //网点9位编号
			store.setStore_shopnumber(merchaCode);	//15位编码（百度是18位的门店号）
			store.setStore_shopname(storeName);	//网点名称
			store.setStore_shopshoptname(storeShortName);	//网点简称
			store.setStore_posshopname(storeShortName);  //pos显示网点名称
			store.setStore_address(storeAddress);	//网点地址
			store.setStore_provincename(mhtProvinceName);	//省名称
			store.setStore_provinceid(mhtProvinceId);	//省代码
			store.setStore_cityname(mhtCityName);	//市名称
			store.setStore_cityid(mhtCityId);	//市代码
			store.setStore_country_name(mhtCountryName);	//区县名称
			store.setStore_country_id(mhtCountryId);	//区县代码
			store.setALLPAY_CREATER(userName);
			store.setALLPAY_CREATETIME(now);
			store.setALLPAY_UPDATETIME(now);  //修改时间
			store.setALLPAY_LOGICDEL("1");
			store.setALLPAY_LOGRECORD(record);
			store.setALLPAY_ISSTART(1);  //启用
			String res = allpayStoreDao.insert(store);

			if("success".equals(res)){
				sendToKafkaService.sentWangDianToKafka(store.getStore_id(),"");
				/*
				默认创建虚拟pos
				 */
				String merchaCodeVir = allpayMerchantDao.getPosMerCode(15);  //随机生成15位不重复的POS商户号
				termCodeVir = terminalDao.getTermCode(8);  //随机生成8位不重复的终端编号
				Allpay_Terminal virtualPos = new Allpay_Terminal();
				virtualPos.setTerminal_postype(3);  //1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
				virtualPos.setTerminal_merchant_id(merId);  //所属商户
				virtualPos.setTerminal_store_id(store.getStore_id());  //门店
				virtualPos.setTerminal_merchcode(merchaCodeVir);  //POS商户号
				virtualPos.setTerminal_termcode(termCodeVir);  //POS终端号
				virtualPos.setALLPAY_CREATER(userName);
				virtualPos.setALLPAY_CREATETIME(now);
				virtualPos.setALLPAY_UPDATETIME(now);  //修改时间
				virtualPos.setALLPAY_LOGICDEL("1");
				virtualPos.setALLPAY_LOGRECORD(record);
				virtualPos.setALLPAY_ISSTART(1);  //启用
				virtualPos.setTerminal_permission(null);
				boolean booPos = terminalDao.insert(virtualPos);

				if(!booPos){
					resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00410);
					resultJO.put(MsgAndCode.RSP_DESC, "批量导入虚拟pos终端信息失败");
					return resultJO;
				}
				sendToKafkaService.sendApayPosToKafka(virtualPos.getTerminal_posid(),"");

				if(posType.length() != 0){
					//随机生成8位不重复的终端编号
					if(CommonHelper.isEmpty(termCode)){
						termCode = terminalDao.getTermCode(8);
					}

					/*
					插入pos终端信息(传统pos、pc pos)
					 */
					Allpay_Terminal terminal = new Allpay_Terminal();
					terminal.setTerminal_merchcode(merchaCode);	//商户编号  （shopNumber）15位
					terminal.setTerminal_store_id(store.getStore_id());	//门店ID
					terminal.setTerminal_termcode(termCode);	//终端编号 8位
					terminal.setTerminal_postype(Integer.parseInt(posType));//1：pos  2:软pos,3虚拟终端 4:pc pos
					terminal.setTerminal_branchid(branchId);	//pos所属分公司ID
					terminal.setTerminal_agentid(agentId);	  //pos所属代理
					terminal.setTerminal_posparterid(posParterId);	//pos所属pos合作方
					terminal.setTerminal_peijianposparterid(peiJianPosParterId);	//pos配件所属pos合作方
					terminal.setTerminal_merchant_id(merId);	//商户ID
					terminal.setTerminal_paychannel("1");	//终端应用系统(默认1, 1-allpay,2-浦发，3-荣邦，4-银盛)
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
				}
			}else{
				resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00409);
				resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00409);
				return resultJO;
			}
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
			resultJO.put("merchaCode", merchaCode);	//商户号
			resultJO.put("storeAddress", storeAddress);	//门店地址
			resultJO.put("termCode", termCode);	//终端号
			resultJO.put("termCodeVir", termCodeVir);	//虚拟pos终端号
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, "传参不正确！");
            return resultJO;
        }
		return resultJO;
	}

	/**
	 * 获取商户登录统计列表
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject getMerchantLoginTotalList(Map<String, Object> source) throws Exception {
		JSONObject resultJO = new JSONObject();
		String curragePage = CommonHelper.nullOrEmptyToString(source.get("curragePage"));	//当前页码
		String pageSize = CommonHelper.nullOrEmptyToString(source.get("pageSize"));	//每页显示记录条数
		if("".equals(curragePage) || "".equals(pageSize)){
			resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00110);
			resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00110);
			return resultJO;
		}

		int total = 0;
		List<Map<String, Object>> list = allpayMerchantDao.getMerchantLoginTotalList(source, false);
		total = allpayMerchantDao.countLogin(source);
		JSONArray array = new JSONArray();
		if(null != list && list.size() >0 ){
			for(Map<String, Object> map : list){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("mhtId", map.get("MERCHANT_ID"));	//商户id
				jsonObject.put("mhtName", map.get("MERCHANT_MERCHNAME"));		//商户名称
				jsonObject.put("mhtAgentId", map.get("AGENT_ID"));	//代理商id
				jsonObject.put("mhtAgentName", map.get("AGENT_NAME")); //代理商名称
				jsonObject.put("mhtLoginTime", CommonHelper.nullToString(map.get("LAST_LOGIN_TIME")));	//登录时间
				jsonObject.put("mhtShortName", map.get("MERCHANT_SHOPSHORTNAME")); //商户简称
				jsonObject.put("branchId", map.get("ORGANIZATION_ID"));	    //分公司id
				jsonObject.put("branchName", map.get("ORGANIZATION_NAME"));			   //分公司名称
				array.put(jsonObject);
			}
		}
		resultJO.put("lists", array);
		resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);		//apay返回的状态码
		resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);	//apay返回的状态码描述
		resultJO.put("curragePage", curragePage);	//当前页
		resultJO.put("pageSize", pageSize);	//每页显示记录条数
		resultJO.put("total", total);	//数据的总条数
		return resultJO;
	}

	/**
	 * 导出商户登录统计列表
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@Override
	public void exportMerchantLoginTotalList(Map<String, Object> source,HttpServletResponse response)
			throws Exception {
		List<Map<String, Object>> list = allpayMerchantDao.getMerchantLoginTotalList(source, true);
		if(!list.isEmpty()) {
			List<String[]> exportList = new ArrayList<String[]>();
			int size = list.size();
			for (int index = 0; index < size; index++) {
				Map<String, Object> map = list.get(index);
				String[] export = new String[8];
				export[0] = Integer.toString(index + 1);
				export[1] = checkTextIsOrNotNull(map.get("LAST_LOGIN_TIME"));
				export[2] = checkTextIsOrNotNull(map.get("SHOPUSER_NAME"));
				export[3] = checkTextIsOrNotNull(map.get("SHOPUSER_NICKNAME"));
				export[4] = checkNumberIsOrNotNull(map.get("MERCHANT_MERCHNAME"));
				export[5] = checkTextIsOrNotNull(map.get("AGENT_NAME"));
				export[6] = checkTextIsOrNotNull(map.get("AGENT_LEVEL"));
				if("1".equals(export[6])){
					export[6] = "一级";
				}else if("2".equals(export[6])){
					export[6] = "二级";
				}else{
					export[6] = "";
				}
				export[7] = checkTextIsOrNotNull(map.get("ORGANIZATION_NAME"));
				exportList.add(export);
			}
			String[] columnName = {"序号", "历史登录时间", "登录用户账号", "登录用户名称", "登录商户名称", "所属代理",
					"所属代理商级别", "所属分子公司"};
			WriteExcelFile.writeExcel(exportList, "MERCHANT_USER_LOGIN", columnName, "MERCHANT_USER_LOGIN",
					outpath, response);
		}
	}

	/**
	 * 
	 * @param merchant
	 * @param source
	 * @param must
	 * @return
	 */
	private String insertOrUpdateMerchant(Allpay_Merchant merchant,Map<String, Object> source,boolean must){
		
		//商户名称
		if(!CommonHelper.isNullOrEmpty(source.get("mhtName"))){
			merchant.setMerchant_merchname(source.get("mhtName").toString());
		}else{
			if(must){
				return returnMissParamMessage("mhtName");
			}
		}
		//分公司id
		if(!CommonHelper.isNullOrEmpty(source.get("branchId"))){
			merchant.setMerchant_branchcompanyid(source.get("branchId").toString());
		}else{
			if(must){
				return returnMissParamMessage("branchId");
			}
		}
		//分公司名称
		if(!CommonHelper.isNullOrEmpty(source.get("branchName"))){
			merchant.setMerchant_branchcompanyname(source.get("branchName").toString());
		}else{
			if(must){
				return returnMissParamMessage("branchName");
			}
		}
		//代理商id
		if(!CommonHelper.isNullOrEmpty(source.get("mhtAgentId"))){
			merchant.setMerchant_agentid(source.get("mhtAgentId").toString());
		}else{
			if(must){
				return returnMissParamMessage("mhtAgentId");
			}
		}
		//商户简称
		if(!CommonHelper.isNullOrEmpty(source.get("mhtShortName"))){
			merchant.setMerchant_shopshoptname(source.get("mhtShortName").toString());
		}else{
			if(must){
				return returnMissParamMessage("mhtShortName");
			}
		}
		//省id
		if(!CommonHelper.isNullOrEmpty(source.get("mhtProvinceId"))){
			merchant.setMerchant_provinceid(source.get("mhtProvinceId").toString());
		}
		//市id
		if(!CommonHelper.isNullOrEmpty(source.get("mhtCityId"))){
			merchant.setMerchant_cityid(source.get("mhtCityId").toString());
		}
		//县id
		if(!CommonHelper.isNullOrEmpty(source.get("mhtCountryId"))){
			merchant.setMerchant_country_id(source.get("mhtCountryId").toString());
		}
		//地址
		if(!CommonHelper.isNullOrEmpty(source.get("mhtAddress"))){
			merchant.setMerchant_address(source.get("mhtAddress").toString());
		}
		//行业名称(类目)
		if(!CommonHelper.isNullOrEmpty(source.get("mhtIndustry"))){
			merchant.setMerchant_industry(source.get("mhtIndustry").toString());
		}else{
			if(must){
				return returnMissParamMessage("mhtIndustry");
			}
		}
		//行业代码(类目)
		if(!CommonHelper.isNullOrEmpty(source.get("mhtIndustryNum"))){
			merchant.setMerchant_industrynum(source.get("mhtIndustryNum").toString());
		}else{
			if(must){
				return returnMissParamMessage("mhtIndustryNum");
			}
		}
		//是否启用
		if(!CommonHelper.isNullOrEmpty(source.get("mhtState"))){
			merchant.setALLPAY_ISSTART(Integer.parseInt(source.get("mhtState").toString()));
		}else{
			if(must){
				return returnMissParamMessage("mhtState");
			}
		}
		//是否创建门店
		if(!CommonHelper.isNullOrEmpty(source.get("isCreateStore"))){
			merchant.setCreateStore(Integer.parseInt(source.get("isCreateStore").toString()));
		}else{
			if(must){
				return returnMissParamMessage("isCreateStore");
			}
		}
		return null;
	}
	/**
	 * 创建门店
	 * @param store
	 * @param source
	 */
	private void insertStore(Allpay_Store store,Map<String, Object> source) throws Exception {
		
		store.setStore_shopname(source.get("mhtName").toString());
		store.setStore_shopshoptname(source.get("mhtShortName").toString());
		store.setStore_posshopname(source.get("mhtShortName").toString());
		if(!CommonHelper.isNullOrEmpty(source.get("mhtAddress"))){
			store.setStore_address(source.get("mhtAddress").toString());
		}
		store.setALLPAY_ISSTART(Integer.parseInt(source.get("mhtState").toString()));
		if(!CommonHelper.isNullOrEmpty(source.get("mhtProvinceId"))){
			store.setStore_provinceid(source.get("mhtProvinceId").toString());
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtCityId"))){
			store.setStore_cityid(source.get("mhtCityId").toString());
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtCountryId"))){
			store.setStore_country_id(source.get("mhtCountryId").toString());
		}
		store.setStore_shopnumber(allpayMerchantDao.getPosMerCode(15));
		store.setStore_shopidnumber(allpayStoreDao.getShopIdNum(9));
		
	}
	/**
	 * 转换商户渠道信息
	 * @param list
	 * @return
	 * @throws Exception
	 */
    private List<Map<String, Object>> transferChannelData(List<Map<String, Object>> list) throws Exception{
    	
        List<String> idList = new ArrayList<String>();
        int size = list.size();
        String code = null;
        for(int t=0;t<size;t++){
        	Map<String, Object> map = list.get(t);
        	if(!CommonHelper.isNullOrEmpty(map.get("CHANNELSET_CHANNEL_CODE"))){
        		code = map.get("CHANNELSET_CHANNEL_CODE").toString();
        		if(!idList.contains(code)){
        			idList.add(code);
        		}
        	}
        }
        /****/
        List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
        for(String id : idList){
        	Map<String, Object> resultMap = new HashMap<String, Object>(8,0.75f);
        	for(int t=0;t<size;t++){
        		Map<String, Object> map = list.get(t);
        		if(!CommonHelper.isNullOrEmpty(map.get("CHANNELSET_CHANNEL_CODE"))){
        			code = map.get("CHANNELSET_CHANNEL_CODE").toString();
        			if(id.equals(code)){
        				if(!CommonHelper.isNullOrEmpty(map.get("CHANNELSET_PARAMETER_NAME"))){
        					String paramName = map.get("CHANNELSET_PARAMETER_NAME").toString();
        					if("state".equals(paramName)){//渠道状态
        						resultMap.put("businessChannelState", 
        								checkTextIsOrNotNull(map.get("CHANNELSET_PARAMETER_VALUE")));
        					}else if("choose".equals(paramName)){//状态
        						resultMap.put("businessState", 
        								checkTextIsOrNotNull(map.get("CHANNELSET_PARAMETER_VALUE")));
        					}
        					resultMap.put("businessName", checkTextIsOrNotNull(map.get("CHANNELSET_PARAMETER_EXPAND")));
        					resultMap.put("businessCreateTime", checkTextIsOrNotNull(map.get("CREATE_TIME")));
        				}
        			}
        		}
        	}
        	resultMap.put("businessId", id);
        	resultList.add(resultMap);
        }
    	return resultList;
    }
	/**
	 * 根据参数无法　查询到相应的信息
	 * @param errorMsg
	 * @return
	 */
	private String returnNullObjectMsg(String errorMsg){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
		node.put(MsgAndCode.RSP_DESC, errorMsg+MsgAndCode.CODE_003_MSG);
		return node.toString();
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
	/**
	 * 
	 * @param obj
	 * @return
	 */
	private String checkNumberIsOrNotNull(Object obj){
		if(!CommonHelper.isNullOrEmpty(obj)){
			return obj.toString();
		}
		return "0";
	}
	/**
	 * 
	 * @param source
	 * @return
	 */
	private String transferIsStart(String source){
		
		if(CommonHelper.isNullOrEmpty(source)){
			return "";
		}
		if("1".equals(source)){
			return "启用";
		}else if("0".equals(source)){
			return "停用";
		}
		return "";
	}
	/**
	 * 开通业务数量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int transferChannelCount(JsonHelper helper,String source){
		
		if(CommonHelper.isEmpty(source)){
			return 0;
		}
		Object obj = helper.read(source);
		if(obj instanceof List<?>){
			List<Object> objOne = (List<Object>)obj;
			int number = 0;
			for(Object objTwo : objOne){
				if(objTwo instanceof Map<?, ?>){
					Map<String, Object> objThree = (Map<String, Object>)objTwo;
					if(objThree.get("channelCode") != null){
						number++;
					}
				}
			}
			return number;
		}
		return 0;
	}
	 /**
     * 生成制定位数的随机数
     * @param n
     * @return
     */
    private long generateRandomNumber(int n){  
	      return (long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);  
	 }
    
    
	@Override
	public String obtainMerchantImageList(Map<String, Object> source)
			throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("merchantId"))){
		    return returnMissParamMessage("merchantId");
		}
		String merchantId = source.get("merchantId").toString();
		/****/
		List<Map<String, Object>> list = allpayMerchantDao.obtainConsumerImage(merchantId);
		if(list.isEmpty()){
			return returnNullObjectMsg("根据给定的merchantId:"+merchantId);
		}
		/****/
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		ArrayNode array = mapper.createArrayNode();
		boolean hasContent = false;
		for(Map<String, Object> map : list){
			if(!CommonHelper.isNullOrEmpty(map.get("FILEUPLOAD_PATH"))){
				hasContent = true;
				ObjectNode nodeOne = mapper.createObjectNode();
				nodeOne.put("imagePath", img_Intranet_url+map.get("FILEUPLOAD_PATH").toString());
				array.add(nodeOne);
			}
		}
		if(!hasContent){
			return returnNullObjectMsg("根据给定的merchantId:"+merchantId);
		}
		node.put("imageList", array);
		return returnSuccessMessage(node);
	} 
}










