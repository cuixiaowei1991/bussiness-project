package com.cn.service.businessService;

import com.cn.entity.dto.AllpayTerminalDto;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * pos终端管理业务层接口
 * Created by WangWenFang on 2016/12/5.
 */
public interface TerminalInfoService {

    String obtainList(AllpayTerminalDto allpayTerminalDto)throws Exception;

    String insert(AllpayTerminalDto allpayTerminalDto) throws Exception;

    String update(AllpayTerminalDto allpayTerminalDto) throws Exception;

    <T> T getById(Class<T> clazz, String userId) throws Exception;

    String getById(AllpayTerminalDto allpayTerminalDto) throws Exception;

    String insertPosMenuSetInfo(AllpayTerminalDto allpayTerminalDto) throws Exception;

    void exportTerminalList(AllpayTerminalDto allpayTerminalDto,HttpServletResponse response) throws Exception ;

    String delete(AllpayTerminalDto allpayTerminalDto) throws Exception;

    String obtainByMerCode(AllpayTerminalDto allpayTerminalDto) throws Exception;

    JSONObject importRelatePos(Map<String, Object> source) throws Exception;

    JSONObject importPos(Map<String, Object> source) throws Exception;

    JSONObject importSoftPos(Map<String, Object> source) throws Exception;

    JSONObject getPosMenuSetInfo(AllpayTerminalDto allpayTerminalDto) throws Exception;

    JSONObject importUserSoftPos(Map<String, Object> source) throws Exception;

    JSONObject getUserSoftPos(Map<String, Object> source) throws Exception;
    /****/
    String obtainMsgForTerAndMercCode(Map<String, Object> source)throws Exception;
}
