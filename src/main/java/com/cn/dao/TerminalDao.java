package com.cn.dao;

import com.cn.entity.dto.AllpayTerminalDto;
import com.cn.entity.po.Allpay_Terminal;

import java.util.List;
import java.util.Map;

/**
 * pos终端管理dao层接口
 * Created by WangWenFang on 2016/12/5.
 */
public interface TerminalDao {

    /**
     * 根据主键id获取POS信息
     * @param posid
     * @return
     */
    Allpay_Terminal getTerminal(String posid);

    List<Map<String, Object>> obtainList(AllpayTerminalDto allpayTerminalDto, Integer currentPage, Integer pageSize, boolean isExport)throws Exception;

    int count(AllpayTerminalDto allpayTerminalDto) throws Exception;

    boolean insert(Allpay_Terminal allpayTerminal) throws Exception;

    boolean update(Allpay_Terminal allpayTerminal) throws Exception;

    boolean insertPosMenuSetInfo(Allpay_Terminal allpayTerminal, String posType) throws Exception;

    <T> T getById(Class<T> clazz, String posId) throws Exception;

    List<?> getMerNameBySql(String merId) throws Exception;

    List<?> getTerminalBySql(AllpayTerminalDto allpayTerminalDto) throws Exception;

    boolean obtainByParamCode(AllpayTerminalDto allpayTerminalDto)throws Exception;

    String getTermCode(int num) throws Exception;

    List<?> getPosMenuSetInfo(AllpayTerminalDto allpayTerminalDto) throws Exception;

    List<?> getUserSoftPos(String userId) throws Exception;

    List<Map<String,Object>> selectDataDictionary(String code, String value) throws Exception;
    /****/
    List<Map<String, Object>> obtainMsgForTerAndMerCode(String merCode,String terCode)throws Exception;
    
}
