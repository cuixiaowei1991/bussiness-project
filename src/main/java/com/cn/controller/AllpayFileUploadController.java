package com.cn.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.LogHelper;
import com.cn.dao.AllpayFileUploadDao;
import com.cn.entity.po.Allpay_FileUpload;

/**
 * 文件上传controller
 * @author songzhili
 * 2017年4月11日下午1:46:11
 */
@Controller
@RequestMapping("/fileUpload")
public class AllpayFileUploadController {
   
	/****/
	@Autowired
	private AllpayFileUploadDao allpayFileUploadDao;
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/add",produces="application/json;charset=UTF-8")
	public String insert(@RequestBody Map<String, Object> source){
		
		LogHelper.info("========>文件上传source="+source);
		
		List<Allpay_FileUpload> fileList = new ArrayList<Allpay_FileUpload>();
		if(source.containsKey("outerKey")){
			String outerKey = source.get("outerKey").toString();
			String fileType = source.get("fileType").toString();
			Object obj = source.get("imageList");
			if(obj instanceof List<?>){
				List<Object> list = (List<Object>)obj;
				for(Object objT : list){
					if(objT instanceof Map<?, ?>){
						Map<String, Object> map = (Map<String, Object>)objT;
						Allpay_FileUpload file = new Allpay_FileUpload();
						file.setFileupload_record(outerKey);
						file.setFileupload_type(fileType);
						if(map.containsKey("imageName")){
							file.setFileupload_name(map.get("imageName").toString());
						}
						if(map.containsKey("imagePath")){
							file.setFileupload_path(map.get("imagePath").toString());
						}
						fileList.add(file);
					}
				}
			}
		}
		try {
			if(!fileList.isEmpty()){
				for(Allpay_FileUpload file : fileList){
					allpayFileUploadDao.insert(file);
				}
			}
		} catch (Exception ex) {
			LogHelper.error(ex, "文件上传异常!!!!!", false);
			return returnErrorMessage();
		}
		return returnSuccessMessage();
	}
	
	private String returnErrorMessage(){
			
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
		node.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
		return node.toString();
     }
	
	 private String returnSuccessMessage(){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
		node.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
		return node.toString();
	}
}










