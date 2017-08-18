package com.cn.dao.impl;

import com.cn.common.CommonHelper;
import com.cn.dao.AllpayTMSDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.dto.AllpayTMSDto;
import com.cn.entity.po.Allpay_TMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * TMS终端管理dao层实现
 * Created by WangWenFang on 2016/12/9.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Repository("allpayTMSDao")
public class AllpayTMSDaoImpl implements AllpayTMSDao {

    @Autowired
    private HibernateDAO hibernateDAO;

    @Override
    public List<Map<String, Object>> obtainList(AllpayTMSDto allpayTMSDto, Integer currentPage, Integer pageSize) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT T.TMS_ID, T.TMS_TERMINALNAME, T.TMS_ENTERTERMINALNAME, T.TMS_MODELSNAME, T.TMS_APPLICATIONNAME, ");
        sql.append("T.TMS_VERNUM, T.TMS_FEILNAME, TO_CHAR(T.ALLPAY_CREATETIME, 'YYYY-MM-DD HH24:MI:SS') ALLPAY_CREATETIME, T.TMS_STATE ");
        sql.append(getSql(allpayTMSDto));
        List<Map<String, Object>> list = (List<Map<String, Object>>)hibernateDAO.listByPageBySQL(sql.toString(), pageSize, currentPage, false);
        return list;
    }

    @Override
    public int count(AllpayTMSDto allpayTMSDto) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT COUNT(T.TMS_ID) AS SUM ");
        sql.append(getSql(allpayTMSDto));
        return hibernateDAO.CountBySQL(sql.toString());
    }

    @Override
    public boolean insert(Allpay_TMS allpayTMS) throws Exception {
        String result =  hibernateDAO.save(allpayTMS);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Allpay_TMS allpayTMS) throws Exception {
        String result =  hibernateDAO.saveOrUpdate(allpayTMS);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public <T> T getById(Class<T> clazz, String tmsId) throws Exception {
        return hibernateDAO.listById(clazz, tmsId, false);
    }

    @Override
    public boolean delete(String tmsId) throws Exception {
        String result = hibernateDAO.removeById(Allpay_TMS.class, tmsId);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    /**
     * 获取公共查询sql
     * @param allpayTMSDto
     * @return
     */
    public String getSql(AllpayTMSDto allpayTMSDto){
        StringBuffer sql = new StringBuffer("FROM ALLPAY_TMS T WHERE 1=1 ");

        if(!CommonHelper.isEmpty(allpayTMSDto.getTmsTerminalname())){  //厂商
            sql.append("AND T.TMS_TERMINALNAME = '").append(allpayTMSDto.getTmsTerminalname()).append("' ");
        }
        if(!CommonHelper.isEmpty(allpayTMSDto.getTmsModelsname())) {    //机型
            sql.append("AND T.TMS_MODELSNAME = '").append(allpayTMSDto.getTmsModelsname()).append("' ");
        }
        if(!CommonHelper.isEmpty(allpayTMSDto.getTmsVernum())) {    //版本号
            sql.append("AND T.TMS_VERNUM = '").append(allpayTMSDto.getTmsVernum()).append("' ");
        }
        if(!CommonHelper.isEmpty(""+allpayTMSDto.getTmsApplicationname())) {    //应用名称
            sql.append("AND T.TMS_APPLICATIONNAME LIKE '%").append(allpayTMSDto.getTmsApplicationname()).append("%' ");
        }

        sql.append("AND T.ALLPAY_LOGICDEL = 1 ORDER BY T.ALLPAY_UPDATETIME DESC ");  //ALLPAY_LOGICDEL 1 未删除
        return sql.toString();
    }
}
