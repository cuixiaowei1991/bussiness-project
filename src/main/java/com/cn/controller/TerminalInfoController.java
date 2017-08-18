package com.cn.controller;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.entity.dto.AllpayTerminalDto;
import com.cn.service.businessService.TerminalInfoService;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import static all.union.tools.others.BeanUtilsHelper.jsonToBen;

/**
 * pos终端管理
 * Created by WangWenFang on 2016/12/5.
 */
@Controller
@RequestMapping("/terminalInfo")
public class TerminalInfoController {

    @Autowired
    private TerminalInfoService terminalInfoService;

    /**
     * 2.4.1获取POS终端信息列表
     * @param allpayTerminalDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTerminalList", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getTerminalList(@RequestBody AllpayTerminalDto allpayTerminalDto){
        LogHelper.info("2.4.1获取POS终端信息列表=============allpayTerminalDto=" + allpayTerminalDto.toString());
        String result = "";
        try {
            result = terminalInfoService.obtainList(allpayTerminalDto);
        } catch (Exception e) {
            LogHelper.error(e, "获取POS终端信息列表出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.4.2新增pos终端信息
     * @param allpayTerminalDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insertPosInfo(@RequestBody AllpayTerminalDto allpayTerminalDto){
        LogHelper.info("2.4.2新增pos终端信息=============allpayTerminalDto=" + allpayTerminalDto.toString());
        String result = "";
        try {
            result = terminalInfoService.insert(allpayTerminalDto);
        } catch (Exception e) {
            LogHelper.error(e, "新增pos终端信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        LogHelper.info("2.4.2新增pos终端信息=============返回结果=" +result.toString());
        return result;
    }

    /**
     * 2.4.3修改pos终端信息
     * @param allpayTerminalDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String updatePosInfo(@RequestBody AllpayTerminalDto allpayTerminalDto){
        LogHelper.info("2.4.3修改pos终端信息=============allpayTerminalDto=" + allpayTerminalDto.toString());
        String result = "";
        try {
            result = terminalInfoService.update(allpayTerminalDto);
        } catch (Exception e) {
            LogHelper.error(e, "修改pos终端信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.4.4获取pos终端信息
     * @param allpayTerminalDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getPosInfoById(@RequestBody AllpayTerminalDto allpayTerminalDto){
        LogHelper.info("2.4.4获取pos终端信息=============allpayTerminalDto=" + allpayTerminalDto.toString());
        String result = "";
        try {
            result = terminalInfoService.getById(allpayTerminalDto);
        } catch (Exception e) {
            LogHelper.error(e, "获取pos终端信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.4.5删除pos终端信息
     * @param allpayTerminalDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deletePosInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String deletePosInfo(@RequestBody AllpayTerminalDto allpayTerminalDto){
        LogHelper.info("2.4.5删除pos终端信息=============allpayTerminalDto=" + allpayTerminalDto.toString());
        String result = "";
        try {
            result = terminalInfoService.delete(allpayTerminalDto);
        } catch (Exception e) {
            LogHelper.error(e, "删除pos终端信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.4.9新增终端菜单配置信息
     * @param allpayTerminalDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/InsertPosMenuSetInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String insertPosMenuSetInfo(@RequestBody AllpayTerminalDto allpayTerminalDto){
        LogHelper.info("2.4.9新增终端菜单配置信息=============allpayTerminalDto=" + allpayTerminalDto.toString());
        String result = "";
        try {
            result = terminalInfoService.insertPosMenuSetInfo(allpayTerminalDto);
        } catch (Exception e) {
            LogHelper.error(e, "新增终端菜单配置信息出现异常！", false);
            JSONObject resultJO = new JSONObject();
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return result;
    }

    /**
     * 2.4.10获取终端菜单配置信息
     * @param allpayTerminalDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPosMenuSetInfo", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getPosMenuSetInfo(@RequestBody AllpayTerminalDto allpayTerminalDto){
        LogHelper.info("2.4.10获取终端菜单配置信息=============allpayTerminalDto=" + allpayTerminalDto.toString());
        JSONObject resultJO = new JSONObject();
        try {
            resultJO = terminalInfoService.getPosMenuSetInfo(allpayTerminalDto);
        } catch (Exception e) {
            LogHelper.error(e, "获取终端菜单配置信息出现异常！", false);
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);  //返回的状态码
            resultJO.put(MsgAndCode.RSP_DESC, e.getMessage());  //返回的状态码描述
            return resultJO.toString();
        }
        return resultJO.toString();
    }

    /**
     * 2.4.11导出POS终端信息列表
     * @param
     * @return
     */

    @RequestMapping(value = "/exportList")
    public void exportTerminalList(HttpServletRequest request,HttpServletResponse response){

        try {
            String requestBody = CommonHelper.obtainRequestBody(request);
            AllpayTerminalDto allpayTerminalDto=jsonToBen(requestBody,AllpayTerminalDto.class);
            LogHelper.info("2.4.11导出POS终端信息列表=============allpayTerminalDto=" + allpayTerminalDto.toString());
            terminalInfoService.exportTerminalList(allpayTerminalDto,response);
        } catch (Exception e) {
            LogHelper.error(e, "导出POS终端信息列表出现异常！", false);
        }
    }

    /**
     * 判断 商户编号+终端编号 是否重复
     * @param allpayTerminalDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/checkMerCodeExist",produces="application/json;charset=UTF-8")
    public String checkMerCodeExist(@RequestBody AllpayTerminalDto allpayTerminalDto){

        LogHelper.info("判断商户编号+终端编号是否重复 allpayTerminalDto="+allpayTerminalDto.toString());
        String result = null;
        try {
            result = terminalInfoService.obtainByMerCode(allpayTerminalDto);
        } catch (Exception ex) {
            LogHelper.error(ex, "判断商户编号+终端编号是否重复!", false);
            return returnErrorMessage(ex);
        }
        return result;
    }

    /**
     * 返回服务器异常信息
     * @return
     */
    private String returnErrorMessage(Exception ex){
       JSONObject jsonObject = new JSONObject();
        jsonObject.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
        jsonObject.put(MsgAndCode.RSP_DESC, ex.getMessage());
        return jsonObject.toString();
    }

    /**
     * 2.4.14关联pos批量导入信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/importSuper", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String importRelatePos(@RequestBody Map<String, Object> source){
        LogHelper.info("2.4.14关联pos批量导入信息 请求参数：" + source);
        JSONObject resultJO = new JSONObject();
        try{
            resultJO=terminalInfoService.importRelatePos(source);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        LogHelper.info("2.4.14关联pos批量导入信息 返回结果：" + resultJO.toString());
        return resultJO.toString();
    }

    /**
     * 2.4.15软pos批量导入信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/import", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String importSoftPos(@RequestBody Map<String, Object> source){
        LogHelper.info("2.4.15软pos批量导入信息 请求参数：" + source);
        JSONObject resultJO = new JSONObject();
        try{
            resultJO=terminalInfoService.importSoftPos(source);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        LogHelper.info("2.4.13人员（账号）及软pos批量导入信息 返回结果：" + resultJO.toString());
        return resultJO.toString();
    }

    /**
     * 2.4.16Pos创建追加批量导入信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/importAppend", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String importPos(@RequestBody Map<String, Object> source){
        LogHelper.info("2.4.16Pos创建追加批量导入信息 请求参数：" + source);
        JSONObject resultJO = new JSONObject();
        try{
            resultJO=terminalInfoService.importPos(source);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        LogHelper.info("2.4.15Pos创建追加批量导入信息 返回结果：" + resultJO.toString());
        return resultJO.toString();
    }

    /**
     * 新增软pos人员时，调用的 新增软pos信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/importUserSoftPos", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String importUserSoftPos(@RequestBody Map<String, Object> source){
        LogHelper.info("新增软pos信息 请求参数：" + source);
        JSONObject resultJO = new JSONObject();
        try{
            resultJO=terminalInfoService.importUserSoftPos(source);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        LogHelper.info("新增软pos信息 返回结果：" + resultJO.toString());
        return resultJO.toString();
    }

    /**
     * 修改软pos人员时，调用的 查询软pos信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getUserSoftPos", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String getUserSoftPos(@RequestBody Map<String, Object> source){
        LogHelper.info("查询软pos信息 请求参数：" + source);
        JSONObject resultJO = new JSONObject();
        try{
            resultJO=terminalInfoService.getUserSoftPos(source);
        }catch (Exception e) {
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
            e.printStackTrace();
        }
        LogHelper.info("查询软pos信息 返回结果：" + resultJO.toString());
        return resultJO.toString();
    }
    /**
     * 根据merCod,terCode获取信息
     * @param source
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/obtainMsgForTerAndMercCode",produces="application/json;charset=UTF-8")
    public String obtainMsgForTerAndMercCode(@RequestBody Map<String, Object> source){
    	
    	 LogHelper.info("根据merCod,terCode获取信息:" + source);
    	String result = "";
    	try {
			result = terminalInfoService.obtainMsgForTerAndMercCode(source);
		} catch (Exception ex) {
			LogHelper.error(ex, "根据merCod,terCode获取信息!!!!!", false);
			return returnErrorMessage();
		}
    	LogHelper.info("根据merCod,terCode获取信息:" + result);
    	return result;
    }
    
     private String returnErrorMessage(){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
		node.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_002_MSG);
		return node.toString();
	}
}
