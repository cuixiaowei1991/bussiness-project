package com.cn.service.impl.businessServiceImpl;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.dao.AllpayFileUploadDao;
import com.cn.dao.AllpayTMSDao;
import com.cn.entity.dto.AllpayTMSDto;
import com.cn.entity.po.Allpay_FileUpload;
import com.cn.entity.po.Allpay_TMS;
import com.cn.service.businessService.AllpayTMSService;
import com.cn.service.impl.kafkaserviceimpl.SendToKafkaServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TMS终端管理业务层实现
 * Created by WangWenFang on 2016/12/9.
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class AllpayTMSServiceImpl implements AllpayTMSService {

    @Autowired
    private AllpayTMSDao allpayTMSDao;

    @Autowired
    private AllpayFileUploadDao allpayFileUploadDao;

    @Value("${fileUpload}")
    private String fileUpload;

    @Autowired
    private SendToKafkaServiceImpl sendToKafkaService;
    @Override
    public String obtainList(AllpayTMSDto allpayTMSDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        if(null == allpayTMSDto.getCurragePage() || null == allpayTMSDto.getPageSize()){ //
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00110);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00110);
            return resultJO.toString();
        }

        JSONArray array = new JSONArray();
        List<Map<String, Object>> list = allpayTMSDao.obtainList(allpayTMSDto, allpayTMSDto.getCurragePage()-1, allpayTMSDto.getPageSize());
        int total = allpayTMSDao.count(allpayTMSDto);
        if(null != list && list.size() > 0){
            for(int i=0,count=list.size(); i<count; i++){
                JSONObject allpayTMSJO = new JSONObject();
                Map<String, Object> map = list.get(i);

                allpayTMSJO.put("tmsSeq", i+1);  //序号
                allpayTMSJO.put("tmsId", map.get("TMS_ID"));  //TMS信息ID
                allpayTMSJO.put("tmsTerminalname", map.get("TMS_TERMINALNAME"));	//厂商
                allpayTMSJO.put("tmsEnterterminalname", map.get("TMS_ENTERTERMINALNAME"));	//厂商名称
                allpayTMSJO.put("tmsModelsname", map.get("TMS_MODELSNAME"));	//机型
                allpayTMSJO.put("tmsApplicationname", CommonHelper.nullToString(map.get("TMS_APPLICATIONNAME")));	//应用名称
                allpayTMSJO.put("tmsVernum", map.get("TMS_VERNUM"));	//版本号
                allpayTMSJO.put("tmsFeilname", CommonHelper.nullToString(fileUpload + map.get("TMS_FEILNAME")));//文件名称
                allpayTMSJO.put("allpayCreatetime", map.get("ALLPAY_CREATETIME"));	//录入时间
                allpayTMSJO.put("tmsState", map.get("TMS_STATE"));  //状态	String（0:停用 1：启用）

                array.put(allpayTMSJO);
            }
        }
        resultJO.put("lists", array);
        resultJO.put("curragePage", allpayTMSDto.getCurragePage());  //当前页
        resultJO.put("pageSize", allpayTMSDto.getPageSize());  //每页显示记录条数
        resultJO.put("total", total);  //数据的总条数
        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);  //apay返回的状态码
        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);  //apay返回的状态码描述
        return resultJO.toString();
    }

    @Override
    public String insert(AllpayTMSDto allpayTMSDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        JSONObject check = check(allpayTMSDto);
        String fileName = allpayTMSDto.getTmsFeilname();
        if(check.length() >= 2){
            return check.toString();
        }
        if(check.length() == 1){
            fileName = check.getString("fileName");
        }
        Allpay_TMS allpayTMS = new Allpay_TMS();

        allpayTMS.setTms_terminalname(allpayTMSDto.getTmsTerminalname());  //厂商
        allpayTMS.setTms_enterterminalname(allpayTMSDto.getTmsEnterterminalname());	//厂商名称
        allpayTMS.setTms_modelsname(allpayTMSDto.getTmsModelsname());	//机型
        allpayTMS.setTms_applicationname(allpayTMSDto.getTmsApplicationname());	//应用名称
        allpayTMS.setTms_vernum(allpayTMSDto.getTmsVernum());	//版本号
        allpayTMS.setTms_feilname(fileName);	//文件名称
        allpayTMS.setTms_state(allpayTMSDto.getTmsState());	  //状态	0:停用 1：启用
        allpayTMS.setTms_feiltype(allpayTMSDto.getTmsFeiltype());	      //程序类型
        allpayTMS.setTms_updatecontent(allpayTMSDto.getTmsUpdatecontent());	//更新内容

        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_NEW, "", allpayTMSDto.getUserNameFromBusCookie());
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");
        allpayTMS.setALLPAY_ISSTART(1);  //状态 是否启用	0:停用 1：启用
        allpayTMS.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
        allpayTMS.setALLPAY_CREATER(userName);  //创建人
        allpayTMS.setALLPAY_CREATETIME(now);  //创建时间
        allpayTMS.setALLPAY_UPDATETIME(now);  //修改时间
        allpayTMS.setALLPAY_UPDATER(userName);
        allpayTMS.setALLPAY_LOGRECORD(record);

        boolean result = allpayTMSDao.insert(allpayTMS);
        if(result){

            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);

            //文件上传管理表
            Allpay_FileUpload file = new Allpay_FileUpload();
            file.setFileupload_record(allpayTMS.getTms_id());  //文件对应相关表主键id
            file.setFileupload_type("2");  //文件类型 1--图片  2--其他文件类型
            file.setFileupload_path(fileUpload+fileName);  //文件路径
            file.setFileupload_name(fileName);  //文件名

            file.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
            file.setALLPAY_CREATER(userName);  //创建人
            file.setALLPAY_CREATETIME(now);  //创建时间
            file.setALLPAY_UPDATETIME(now);  //修改时间
            file.setALLPAY_UPDATER(userName);
            file.setALLPAY_LOGRECORD(record);
            boolean re = allpayFileUploadDao.insert(file);
            if(re){
                sendToKafkaService.sendApayTmsmessage(allpayTMS.getTms_id(),"");
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
                resultJO.put(MsgAndCode.RSP_DESC, "新增上传文件信息异常！");

                //删除TMS信息
                allpayTMSDao.delete(allpayTMS.getTms_id());
            }
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, "新增TMS信息异常！");
        }

        return resultJO.toString();
    }

    /**
     * 必填项验证
     * @param allpayTMSDto
     * @return
     */
    public JSONObject check(AllpayTMSDto allpayTMSDto){
        JSONObject resultJO = new JSONObject();
        String fileName = allpayTMSDto.getTmsFeilname();

        if(CommonHelper.isEmpty(allpayTMSDto.getTmsTerminalname())){  //厂商
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00270);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00270);
            return resultJO;
        }
        if(CommonHelper.isEmpty(allpayTMSDto.getTmsEnterterminalname())){  //厂商名称
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00271);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00271);
            return resultJO;
        }
        if(CommonHelper.isEmpty(allpayTMSDto.getTmsModelsname())){  //机型
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00272);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00272);
            return resultJO;
        }
        if(CommonHelper.isEmpty(allpayTMSDto.getTmsApplicationname())){  //应用名称
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00273);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00273);
            return resultJO;
        }
        if(CommonHelper.isEmpty(allpayTMSDto.getTmsVernum())){  //版本号
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00274);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00274);
            return resultJO;
        }
        if(CommonHelper.isEmpty(fileName)){  // 文件名称
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00275);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00275);
            return resultJO;
        }
        if(CommonHelper.isEmpty(""+allpayTMSDto.getTmsState())){  // 状态	0:停用 1：启用
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00276);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00276);
            return resultJO;
        }
        if(CommonHelper.isEmpty(""+allpayTMSDto.getTmsFeiltype())){  // 程序类型
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00277);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00277);
            return resultJO;
        }
        if(CommonHelper.isEmpty(""+allpayTMSDto.getTmsUpdatecontent())){  // 更新内容
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00278);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00278);
            return resultJO;
        }

        if(fileName.contains("uploads/")){
            fileName = fileName.split("uploads/")[1];
            resultJO.put("fileName", fileName);
        }
        return resultJO;
    }

    @Override
    public String update(AllpayTMSDto allpayTMSDto) throws Exception {
        JSONObject resultJO = new JSONObject();

        String tmsId = allpayTMSDto.getTmsId();  //TMS信息ID
        if(CommonHelper.isEmpty(tmsId)){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00279);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00279);
            return resultJO.toString();
        }

        JSONObject check = check(allpayTMSDto);  //验证其他必填字段
        String fileName = allpayTMSDto.getTmsFeilname();
        if(check.length() >= 2){
            return check.toString();
        }
        if(check.length() == 1){
            fileName = check.getString("fileName");
        }

        Allpay_TMS allpayTMS = allpayTMSDao.getById(Allpay_TMS.class, tmsId);
        if(null == allpayTMS){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00280);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00280);
            return resultJO.toString();
        }

        allpayTMS.setTms_terminalname(allpayTMSDto.getTmsTerminalname());  //厂商
        allpayTMS.setTms_enterterminalname(allpayTMSDto.getTmsEnterterminalname());	//厂商名称
        allpayTMS.setTms_modelsname(allpayTMSDto.getTmsModelsname());	//机型
        allpayTMS.setTms_applicationname(allpayTMSDto.getTmsApplicationname());	//应用名称
        allpayTMS.setTms_vernum(allpayTMSDto.getTmsVernum());	//版本号
        allpayTMS.setTms_feilname(fileName);	//文件名称
        allpayTMS.setTms_state(allpayTMSDto.getTmsState());	// 状态	0:停用 1：启用
        allpayTMS.setTms_feiltype(allpayTMSDto.getTmsFeiltype());	  //程序类型
        allpayTMS.setTms_updatecontent(allpayTMSDto.getTmsUpdatecontent());	 //更新内容

        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, allpayTMS.getALLPAY_LOGRECORD(), allpayTMSDto.getUserNameFromBusCookie());
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");

        allpayTMS.setALLPAY_UPDATETIME(now);  //修改时间
        allpayTMS.setALLPAY_UPDATER(userName);
        allpayTMS.setALLPAY_LOGRECORD(record);
        boolean result = allpayTMSDao.update(allpayTMS);
        if(result){

            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);

            //先删除上传文件
            boolean re = allpayFileUploadDao.deleteByRecordId(tmsId);
            if(re){
                //再插入上传文件
                Allpay_FileUpload file = new Allpay_FileUpload();
                file.setFileupload_record(tmsId);  //文件对应相关表主键id
                file.setFileupload_type("2");  //文件类型 1--图片  2--其他文件类型
                file.setFileupload_path(fileName);  //文件路径
                file.setFileupload_name(fileName);  //文件名

                file.setALLPAY_LOGICDEL("1");  //逻辑删除标记  1---未删除 2---已删除
                file.setALLPAY_UPDATETIME(now);  //修改时间
                file.setALLPAY_UPDATER(userName);
                file.setALLPAY_LOGRECORD(record);
                allpayFileUploadDao.insert(file);
                sendToKafkaService.sendApayTmsmessage(allpayTMS.getTms_id(),"");
            }else{
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
                resultJO.put(MsgAndCode.RSP_DESC, "修改上传文件信息异常！");
            }
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, "修改TMS信息异常！");
        }
        return resultJO.toString();
    }

    @Override
    public String getById(AllpayTMSDto allpayTMSDto) throws Exception {
        JSONObject resultJO = new JSONObject();
        String tmsId = allpayTMSDto.getTmsId();  //TMS信息ID
        if(CommonHelper.isEmpty(tmsId)){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00279);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00279);
            return resultJO.toString();
        }
        Allpay_TMS allpayTMS = allpayTMSDao.getById(Allpay_TMS.class, tmsId);
        if(null == allpayTMS){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00280);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00280);
            return resultJO.toString();
        }

        //返回前端信息
        resultJO.put("tmsId", allpayTMS.getTms_id());  //TMS信息ID
        resultJO.put("tmsTerminalname", allpayTMS.getTms_terminalname());    //厂商
        resultJO.put("tmsEnterterminalname", allpayTMS.getTms_enterterminalname());    //厂商名称
        resultJO.put("tmsModelsname", allpayTMS.getTms_modelsname());    //机型
        resultJO.put("tmsApplicationname", allpayTMS.getTms_applicationname());    //应用名称
        resultJO.put("tmsVernum", allpayTMS.getTms_vernum());	//版本号
        resultJO.put("tmsFeilname", fileUpload + allpayTMS.getTms_feilname());  //文件名称
        resultJO.put("tmsState", allpayTMS.getTms_state());  //状态	0:停用 1：启用
        resultJO.put("tmsFeiltype", allpayTMS.getTms_feiltype());  //程序类型
        resultJO.put("tmsUpdatecontent", allpayTMS.getTms_updatecontent());  // 更新内容
        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);   //apay返回的状态码	000 为正常返回,其他为异常返回
        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE); //apay返回的状态码描述	异常返回时的异常说明
        return resultJO.toString();
    }

    @Override
    public String delete(AllpayTMSDto allpayTMSDto) throws Exception {
        JSONObject resultJO = new JSONObject();

        String tmsId = allpayTMSDto.getTmsId();  //TMS信息ID
        if(CommonHelper.isEmpty(tmsId)){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00279);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00279);
            return resultJO.toString();
        }

        Allpay_TMS allpayTMS = allpayTMSDao.getById(Allpay_TMS.class, tmsId);
        if(null == allpayTMS){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00280);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00280);
            return resultJO.toString();
        }

        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, allpayTMS.getALLPAY_LOGRECORD(), allpayTMSDto.getUserNameFromBusCookie());
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");

        allpayTMS.setALLPAY_UPDATETIME(now);  //修改时间
        allpayTMS.setALLPAY_UPDATER(userName);
        allpayTMS.setALLPAY_LOGICDEL("2");  //删除
        allpayTMS.setALLPAY_LOGRECORD(record);
        boolean result = allpayTMSDao.update(allpayTMS);
        if(result){
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);

            //删除上传文件
            Allpay_FileUpload file = new Allpay_FileUpload();

            JSONObject publicFileds1 = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_DELETE, file.getALLPAY_LOGRECORD(), allpayTMSDto.getUserNameFromBusCookie());
            String userName1 = publicFileds1.getString("userName");
            Date now1 = CommonHelper.getStringToDate(publicFileds1.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record1 = publicFileds1.getString("record");

            file.setALLPAY_UPDATETIME(now1);  //修改时间
            file.setALLPAY_UPDATER(userName1);
            file.setALLPAY_LOGICDEL("2");  //删除
            file.setALLPAY_LOGRECORD(record1);
            allpayFileUploadDao.update(file);
            sendToKafkaService.sendApayTmsmessage(allpayTMS.getTms_id(),"del");
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, "删除TMS信息异常！");
        }
        return resultJO.toString();
    }

}
