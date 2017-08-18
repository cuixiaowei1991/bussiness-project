package com.cn.service.impl.businessServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.cn.common.LogHelper;
import com.cn.dao.AllpayFileUploadDao;
import com.cn.entity.dto.AllpayTerminalDto;
import com.cn.entity.po.Allpay_FileUpload;
import com.cn.service.impl.kafkaserviceimpl.SendToKafkaServiceImpl;

import org.apache.commons.fileupload.FileUpload;
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
import com.cn.common.WriteExcelFile;
import com.cn.dao.AllpayStoreDao;
import com.cn.dao.TerminalDao;
import com.cn.entity.po.Allpay_Store;
import com.cn.entity.po.Allpay_Terminal;
import com.cn.service.businessService.AllpayStoreService;

import org.springframework.transaction.annotation.Transactional;

/**
 * 门店管理
 * @author songzhili
 * 2016年12月1日下午4:12:23
 */
@Service("allpayStoreService")
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class AllpayStoreServiceImpl implements AllpayStoreService {
     
	@Autowired
	private AllpayStoreDao allpayStoreDao;

	@Autowired
	private TerminalDao terminalDao;
	 
	@Value("${excelpath}")
	private String excelpath;

    @Value("${outpath}")
	private String outpath;

	@Value("${fileUpload}")
	private String fileUpload;

	@Autowired
	private AllpayFileUploadDao allpayFileUploadDao;

	@Autowired
	private SendToKafkaServiceImpl sendToKafkaService;
	@Override
	public String insert(Map<String, Object> source) throws Exception {
		LogHelper.info("========>门店管理添加门店 source="+source.toString());
		String checkResult = checkStoreRepeat(source, false);
		if(checkResult != null){
			return checkResult;
		}
		/*****/
		Allpay_Store store = new Allpay_Store();
		String result = insertOrUpdateStore(store, source, true);
		if(!CommonHelper.isEmpty(result)){
			return result;
		}

		List<Map<String, Object>> imageList= obtainImageMsgList(source.get("imageList"));  //门店照片

		String userNameFromBusCookie = CommonHelper.nullToString(source.get("userNameFromBusCookie"));
		String userNameFromAgentCookie = CommonHelper.nullToString(source.get("userNameFromAgentCookie"));
		JSONObject publicFileds = null;
		if(!CommonHelper.isEmpty(userNameFromBusCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", userNameFromBusCookie);
		}else if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", userNameFromAgentCookie);
			if(null == imageList){
				return returnMissParamMessage("门店照片");
			}
			for (int i=0,count=imageList.size();i<count;i++){
				Map<String, Object> map= imageList.get(i);
				if(CommonHelper.isNullOrEmpty(map.get("imgPath")) || CommonHelper.isNullOrEmpty(map.get("imgName"))){
					return returnMissParamMessage("门店照片");
				}
			}
		}else{
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", null);
		}
		String userName = publicFileds.getString("userName");
		Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
		String record = publicFileds.getString("record");
	    store.setALLPAY_CREATER(userName);  //创建人
	    store.setALLPAY_CREATETIME(now);  //创建时间
	    store.setALLPAY_UPDATETIME(now);  //修改时间
	    store.setALLPAY_UPDATER(userName);
	    store.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
	    store.setALLPAY_LOGRECORD(record);
	    allpayStoreDao.insert(store);

		if(null != imageList && imageList.size() >0){
			//门店照片
			for (int i=0,count=imageList.size();i<count;i++){
				Map<String, Object> map= imageList.get(i);
				String imagePath=map.get("imgPath").toString();//图片路径
				String imageName =map.get("imgName").toString();//图片名称

				Allpay_FileUpload file=new Allpay_FileUpload();
				file.setFileupload_record(store.getStore_id());
				file.setFileupload_type("1");
				file.setFileupload_name(imageName);
				file.setFileupload_path(imagePath);
				file.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
				file.setALLPAY_CREATER(userName);  //创建人
				file.setALLPAY_CREATETIME(now);  //创建时间
				file.setALLPAY_UPDATETIME(now);  //修改时间
				file.setALLPAY_UPDATER(userName);
				file.setALLPAY_LOGRECORD(record);
				allpayFileUploadDao.insert(file);
			}
		}

		sendToKafkaService.sentWangDianToKafka(store.getStore_id(),"");
	    //创建虚拟pos
	    long randomNumber = 0l;
	    randomNumber = generateRandomNumber(8);
		AllpayTerminalDto terminalDto = new AllpayTerminalDto();
		terminalDto.setTermCode(Long.toString(randomNumber));
	    boolean bool = terminalDao.obtainByParamCode(terminalDto);
	    while(bool){
	    	randomNumber = generateRandomNumber(8);
			terminalDto.setTermCode(Long.toString(randomNumber));
			bool = terminalDao.obtainByParamCode(terminalDto);
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
		return returnSuccessMessage();
	}
    
	@Override
	public String delete(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("storeId"))){
			return returnMissParamMessage("storeId");
		}
		String storeId = source.get("storeId").toString();
		Allpay_Store store = allpayStoreDao.obtain(storeId);
		if(CommonHelper.isNullOrEmpty(store)){
			return returnNullObjectMsg("根据storeId:"+storeId);
		}
		boolean serious = false;
		if(!CommonHelper.isNullOrEmpty(source.get("serious"))
				&& "1".equals(source.get("serious"))){
			serious = true;
		}
		String userNameFromBusCookie = CommonHelper.nullToString(source.get("userNameFromBusCookie"));
		String userNameFromAgentCookie = CommonHelper.nullToString(source.get("userNameFromAgentCookie"));
		JSONObject publicFileds = null;
		if(!CommonHelper.isEmpty(userNameFromBusCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, store.getALLPAY_LOGRECORD(), userNameFromBusCookie);
		}else if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, store.getALLPAY_LOGRECORD(), userNameFromAgentCookie);
		}else{
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, store.getALLPAY_LOGRECORD(), null);
		}
		String record = publicFileds.getString("record");
		allpayStoreDao.delete(source.get("storeId").toString(),serious,record);
		sendToKafkaService.sentWangDianToKafka(source.get("storeId").toString(),"del");
		return returnSuccessMessage();
	}

	@Override
	public String update(Map<String, Object> source) throws Exception {
		
		String checkResult = checkStoreRepeat(source, true);
		if(checkResult != null){
			return checkResult;
		}
		if(CommonHelper.isNullOrEmpty(source.get("storeId"))){
			return returnMissParamMessage("storeId");
		}
		String storeId = source.get("storeId").toString();
		Allpay_Store store = allpayStoreDao.obtain(storeId);
		if(CommonHelper.isNullOrEmpty(store)){
			return returnNullObjectMsg("根据storeId:"+storeId);
		}
		/****/
		insertOrUpdateStore(store, source, false);

		List<Map<String, Object>> imageList= obtainImageMsgList(source.get("imageList"));  //门店照片

		String userNameFromBusCookie = CommonHelper.nullToString(source.get("userNameFromBusCookie"));
		String userNameFromAgentCookie = CommonHelper.nullToString(source.get("userNameFromAgentCookie"));
		JSONObject publicFileds = null;
		if(!CommonHelper.isEmpty(userNameFromBusCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, store.getALLPAY_LOGRECORD(), userNameFromBusCookie);
		}else if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, store.getALLPAY_LOGRECORD(), userNameFromAgentCookie);
			if(null == imageList){
				return returnMissParamMessage("门店照片");
			}
			for (int i=0,count=imageList.size();i<count;i++){
				Map<String, Object> map= imageList.get(i);
				if(CommonHelper.isNullOrEmpty(map.get("imgPath")) || CommonHelper.isNullOrEmpty(map.get("imgName"))){
					return returnMissParamMessage("门店照片");
				}
			}
		}else{
			publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, store.getALLPAY_LOGRECORD(), null);
		}
		String userName = publicFileds.getString("userName");
		Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
		String record = publicFileds.getString("record");

		store.setALLPAY_UPDATETIME(now);  //修改时间
	    store.setALLPAY_UPDATER(userName);
	    store.setALLPAY_LOGRECORD(record);
	    allpayStoreDao.update(store);

		if(null != imageList && imageList.size() >0){
			//先删除该门店的门店照片
			allpayFileUploadDao.deleteByRecordId(store.getStore_id());

			//再插入门店照片
			for (int i=0,count=imageList.size();i<count;i++){
				Map<String, Object> map= imageList.get(i);
				String imagePath=map.get("imgPath").toString();//图片路径
				String imageName =map.get("imgName").toString();//图片名称

				Allpay_FileUpload file=new Allpay_FileUpload();
				file.setFileupload_record(store.getStore_id());
				file.setFileupload_type("1");
				file.setFileupload_name(imageName);
				file.setFileupload_path(imagePath);
				file.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
				file.setALLPAY_UPDATETIME(now);  //修改时间
				file.setALLPAY_UPDATER(userName);
				file.setALLPAY_LOGRECORD(record);
				allpayFileUploadDao.insert(file);
			}
		}

		sendToKafkaService.sentWangDianToKafka(storeId,"");
		return returnSuccessMessage();
	}
	
    /**
     * 获取门店详细信息
     */
	@Override
	public String obtain(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("storeId"))){
			return returnMissParamMessage("storeId");
		}
		String storeId = source.get("storeId").toString();
		Map<String, Object> map = allpayStoreDao.obtainForStoreId(storeId);
		if(CommonHelper.isNullOrEmpty(map)){
			return returnNullObjectMsg("根据storeId:"+storeId);
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("storeId", storeId);
		node.put("storeName", checkIsOrNotNull(map.get("STORE_SHOPNAME")));
		node.put("storeShortName", checkIsOrNotNull(map.get("STORE_SHOPSHORTNAME")));
		node.put("posShopName", checkIsOrNotNull(map.get("STORE_POSSHOPNAME")));
		node.put("shopIdNum", checkIsOrNotNull(map.get("STORE_SHOPIDNUMBER")));
		node.put("shopNum", checkIsOrNotNull(map.get("STORE_SHOPNUMBER")));
		node.put("merchantId", checkIsOrNotNull(map.get("MERCHANT_ID")));
		node.put("merchantName", checkIsOrNotNull(map.get("MERCHANT_MERCHNAME")));
		node.put("storeAddress", checkIsOrNotNull(map.get("STORE_ADDRESS")));
		node.put("locationTude", checkIsOrNotNull(map.get("STORE_LOCATIONTUDE")));
		node.put("businessStartHours", checkIsOrNotNull(map.get("BEGIN_TIME")));
		node.put("businessEndHours", checkIsOrNotNull(map.get("END_TIME")));
		node.put("servicePhone", checkIsOrNotNull(map.get("STORE_CUSTOMERSERVICEPHONE")));
		node.put("specialServices", checkIsOrNotNull(map.get("STORE_SPECIALSERVICES")));
		node.put("storeUpdateTime", checkIsOrNotNull(map.get("UPDATE_TIME")));
		node.put("storeUpdateName", checkIsOrNotNull(map.get("ALLPAY_UPDATER")));
		node.put("storeCreateTime", checkIsOrNotNull(map.get("CREATE_TIME")));
		node.put("storeCreateName", checkIsOrNotNull(map.get("ALLPAY_CREATER")));
		node.put("storeStatus", checkIsOrNotNull(map.get("ALLPAY_ISSTART")));
		node.put("provinceId", checkIsOrNotNull(map.get("STORE_PROVINCEID")));
		node.put("provinceName", checkIsOrNotNull(map.get("STORE_PROVINCENAME")));
		node.put("cityId", checkIsOrNotNull(map.get("STORE_CITYID")));
		node.put("cityName", checkIsOrNotNull(map.get("STORE_CITYNAME")));
		node.put("countryId", checkIsOrNotNull(map.get("STORE_COUNTRY_ID")));
		node.put("countryName", checkIsOrNotNull(map.get("STORE_COUNTRY_NAME")));
		List<Map<String, Object>> imageList = allpayStoreDao.obtainConsumerImage(storeId);
		ArrayNode array = mapper.createArrayNode();
		if(!imageList.isEmpty()){
			for(int t=0;t<imageList.size();t++){
				Map<String, Object> temMap = imageList.get(t);
				ObjectNode nodeOne = mapper.createObjectNode();
				nodeOne.put("imgName", checkIsOrNotNull(temMap.get("FILEUPLOAD_NAME")));
				nodeOne.put("imgPath", checkIsOrNotNull(fileUpload+temMap.get("FILEUPLOAD_PATH")));
				array.add(nodeOne);
			}
		}
		node.put("imageList", array);
		return returnSuccessMessage(node);
	}
	
	@Override
	public String checkStoreExist(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("merchantId"))
				|| CommonHelper.isNullOrEmpty(source.get("storeName"))){
			return returnMissParamMessage("merchantId或者storeName");
		}
		boolean exist = allpayStoreDao.checkStoreExistForStoreName(source);
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
     * 获取门店信息列表
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
		List<Map<String, Object>> list = allpayStoreDao.obtainList(source,false);
		int total = allpayStoreDao.count(source);
		if(!list.isEmpty()){
			for(int t=0;t<list.size();t++){
				Map<String, Object> map = list.get(t);
				ObjectNode nodeOne = mapper.createObjectNode();
				nodeOne.put("storeId", checkIsOrNotNull(map.get("STORE_ID")));
				nodeOne.put("storeName", checkIsOrNotNull(map.get("STORE_SHOPNAME")));
				nodeOne.put("merchantId", checkIsOrNotNull(map.get("MERCHANT_ID")));
				nodeOne.put("storeStatus", checkIsOrNotNull(map.get("ALLPAY_ISSTART")));
				nodeOne.put("shopIdNum", checkIsOrNotNull(map.get("STORE_SHOPIDNUMBER")));  //网点9位编号
				nodeOne.put("shopNum", checkIsOrNotNull(map.get("STORE_SHOPNUMBER")));	//15位编码
				nodeOne.put("merchantName", checkIsOrNotNull(map.get("MERCHANT_MERCHNAME")));
				nodeOne.put("storeAddress", checkIsOrNotNull(map.get("STORE_ADDRESS")));
				nodeOne.put("storeCreateTime", checkIsOrNotNull(map.get("CREATE_TIME")));
				nodeOne.put("storeCreateName", checkIsOrNotNull(map.get("ALLPAY_CREATER")));
				array.add(nodeOne);
			}
			node.put("lists", array);
		} else{
			node.put("lists", array);
		}
		node.put("curragePage", source.get("curragePage").toString());
		node.put("pageSize", source.get("pageSize").toString());
		node.put("total", total);
		return returnSuccessMessage(node);
	}
	
	/**
	 * 门店excel导出
	 */
	@Override
	public void exportExcel(Map<String, Object> source,HttpServletResponse response) throws Exception {
		
		List<Map<String, Object>> list = allpayStoreDao.obtainList(source,true);
		if(!list.isEmpty()){
			 List<String[]> exportList = new ArrayList<String[]>();
			 int i=0;
			 for(Map<String, Object> map : list){
				 String[] export = new String[7];
				 export[0] = String.valueOf(++i);
				 export[1] = checkIsOrNotNull(map.get("STORE_SHOPNAME"));
				 export[2] = checkIsOrNotNull(map.get("MERCHANT_MERCHNAME"));
				 export[3] = checkIsOrNotNull(map.get("STORE_ADDRESS"));
				 export[4] = checkIsOrNotNull(map.get("CREATE_TIME"));
				 export[5] = checkIsOrNotNull(map.get("ALLPAY_CREATER"));
				 export[6] = transferIsStart(checkIsOrNotNull(map.get("ALLPAY_ISSTART")));
				 exportList.add(export);
			 }
			 String[] columnName = { "序号", "门店名称", "所属商户", "所在地", "录入时间", "录入者","状态"};
			 WriteExcelFile.writeExcel(exportList, "STORE_LIST", columnName, "STORE_LIST",
					 outpath, response);
		}
	} 
	
	/**
	 * 获取POS终端信息列表
	 */
	@Override
	public String obtainTerminalPosList(Map<String, Object> source)
			throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("storeId"))){
			return returnMissParamMessage("storeId");
		}
		String storeId = source.get("storeId").toString();
		List<Map<String, Object>> list = allpayStoreDao.obtainTerminalPosList(storeId);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		ArrayNode array = mapper.createArrayNode();
		if(!list.isEmpty()){
			for(int t=0;t<list.size();t++){
				Map<String, Object> map = list.get(t);
				ObjectNode nodeOne = mapper.createObjectNode();
				nodeOne.put("posId", checkIsOrNotNull(map.get("TERMINAL_POSID")));
				nodeOne.put("merchantId", checkIsOrNotNull(map.get("MERCHANT_ID")));
				nodeOne.put("merchantName", checkIsOrNotNull(map.get("MERCHANT_MERCHNAME")));
				nodeOne.put("merchCode", checkIsOrNotNull(map.get("MERCHANT_SHOPNUMBER")));
				nodeOne.put("termCode", checkIsOrNotNull(map.get("TERMINAL_TERMCODE")));
				nodeOne.put("branchId", checkIsOrNotNull(map.get("TERMINAL_BRANCHID")));
				nodeOne.put("branchName", checkIsOrNotNull(map.get("BRANCH_NAME")));
				nodeOne.put("peijianPosParterId", checkIsOrNotNull(map.get("POSPARTER_ID")));
				nodeOne.put("peijianPosParterName", checkIsOrNotNull(map.get("POSPARTER_PARTERALLNAME")));
				nodeOne.put("superTermCode", checkIsOrNotNull(map.get("TERMINAL_SUPERTERMCODE")));
				nodeOne.put("terminalState", checkIsOrNotNull(map.get("ALLPAY_ISSTART")));
				nodeOne.put("posType", checkIsOrNotNull(map.get("TERMINAL_POSTYPE")));
				nodeOne.put("posCreateTime", checkIsOrNotNull(map.get("CREATE_TIME")));
				array.add(nodeOne);
			}
		}
		node.put("lists", array);
		return returnSuccessMessage(node);
	}
	/**
	 * 
	 * @param source
	 * @param must
	 * @return
	 */
	private String insertOrUpdateStore(Allpay_Store store,Map<String, Object> source,boolean must){
		
		//所属商户ID
		if(!CommonHelper.isNullOrEmpty(source.get("merchantId"))){
			store.setStore_merchant_id(source.get("merchantId").toString());
		}else{
			if(must){
				return returnMissParamMessage("merchantId");
			}
		}
		//网点9位编号
		if(!CommonHelper.isNullOrEmpty(source.get("shopIdNum"))){
			store.setStore_shopidnumber(source.get("shopIdNum").toString());
		}else{
			if(must){
				return returnMissParamMessage("shopIdNum");
			}
		}
		//15位编码
		if(!CommonHelper.isNullOrEmpty(source.get("shopNum"))){
			store.setStore_shopnumber(source.get("shopNum").toString());
		}else{
			if(must){
				return returnMissParamMessage("shopNum");
			}
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("storeName"))){
			store.setStore_shopname(source.get("storeName").toString());
		}else{
			if(must){
				return returnMissParamMessage("storeName");
			}
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("storeShortName"))){
			store.setStore_shopshoptname(source.get("storeShortName").toString());
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("posShopName"))){
			store.setStore_posshopname(source.get("posShopName").toString());
		}else{
			if(must){
				return returnMissParamMessage("posShopName");
			}
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("servicePhone"))){
			store.setStore_customerservicephone(source.get("servicePhone").toString());
		}else{
			if(must){
				return returnMissParamMessage("servicePhone");
			}
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("provinceId"))){
			store.setStore_provinceid(source.get("provinceId").toString());
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("provinceName"))){
			store.setStore_provincename(source.get("provinceName").toString());
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("cityId"))){
			store.setStore_cityid(source.get("cityId").toString());
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("cityName"))){
			store.setStore_cityname(source.get("cityName").toString());
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("countryId"))){
			store.setStore_country_id(source.get("countryId").toString());
		}
		//
		if(!CommonHelper.isNullOrEmpty(source.get("countryName"))){
			store.setStore_country_name(source.get("countryName").toString());
		}
		//门店地址
		if(!CommonHelper.isNullOrEmpty(source.get("storeAddress"))){
			store.setStore_address(source.get("storeAddress").toString());
		}
		//网点经纬度
		if(!CommonHelper.isNullOrEmpty(source.get("locationTude"))){
			store.setStore_locationtude(source.get("locationTude").toString());
		}
		//  状态 是否启用
		if(!CommonHelper.isNullOrEmpty(source.get("storeStatus"))){
			store.setALLPAY_ISSTART(Integer.parseInt(source.get("storeStatus").toString()));
		}else{
			if(must){
				return returnMissParamMessage("storeStatus");
			}
		}
		return null;
	}
	/**
	 * 
	 * @param source
	 * @param updateStore
	 * @return
	 * @throws Exception
	 */
	private String checkStoreRepeat(Map<String, Object> source,boolean updateStore) throws Exception{
		
		Map<String, Object> temMap = new HashMap<String, Object>(4,0.75f);
		temMap.put("merchantId", source.get("merchantId"));
		temMap.put("storeName", source.get("storeName"));
		if(updateStore){
			temMap.put("storeId", source.get("storeId"));
		}
		boolean existName = allpayStoreDao.checkStoreExistForStoreName(temMap);
		if(existName){
			return returnExistStore("storeName");
		}
		temMap.remove("storeName");
		/****/
		if(CommonHelper.isNullOrEmpty(source.get("shopIdNum")) && !updateStore){
			source.put("shopIdNum", new Long(generateRandomNumber(9)));
		}
		if(CommonHelper.isNullOrEmpty(source.get("shopNum")) && !updateStore){
			source.put("shopNum", new Long(generateRandomNumber(15)));
		}
		temMap.put("shopIdNum", source.get("shopIdNum"));
		boolean existStore = allpayStoreDao.checkStoreExistForStoreNum(temMap);
		if(existStore){
			return returnExistStore("shopIdNum");
		}
		temMap.remove("shopIdNum");
		temMap.put("shopNum", source.get("shopNum"));
		existStore = allpayStoreDao.checkStoreExistForStoreNum(temMap);
		if(existStore){
			return returnExistStore("shopNum");
		}
		return null;
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
	 * 校验　非空
	 * @param obj
	 * @return
	 */
	private String checkIsOrNotNull(Object obj){
		
		if(!CommonHelper.isNullOrEmpty(obj)){
			return obj.toString();
		}
		return "";
	}
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
	
    private String returnNullObjectMsg(String errorMsg){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
		node.put(MsgAndCode.RSP_DESC, errorMsg+MsgAndCode.CODE_003_MSG);
		return node.toString();
	}
    
    private String returnExistStore(String existMsg){
    	
    	ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00281);
		node.put(MsgAndCode.RSP_DESC, existMsg+MsgAndCode.MESSAGE_00281);
		return node.toString();
    }
    /**
     * 生成制定位数的随机数
     * @param n
     * @return
     */
    private long generateRandomNumber(int n){  
	      return (long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);  
	 }

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
