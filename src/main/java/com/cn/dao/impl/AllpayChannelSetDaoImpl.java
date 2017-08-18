package com.cn.dao.impl;

import com.cn.common.CommonHelper;
import com.cn.dao.AllpayChannelSetDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.dto.AllpayChannelSetDto;
import com.cn.entity.po.Allpay_ChannelSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 业务签约管理dao层实现
 * Created by WangWenFang on 2016/12/7.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Repository("allpayChannelSetDao")
public class AllpayChannelSetDaoImpl implements AllpayChannelSetDao {

    @Autowired
    private HibernateDAO hibernateDAO;

    @Override
    public List<Map<String, Object>> obtainList(AllpayChannelSetDto allpayChannelSetDto, Integer currentPage, Integer pageSize, boolean isExport) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT AC.CHANNELSET_ID, AC.CHANNELSET_CHANNEL_CODE, AC.CHANNELSET_MERCHANT_ID, AC.ALLPAY_CREATETIME, AM.MERCHANT_MERCHNAME, ");
        sql.append("AM.MERCHANT_SHOPSHORTNAME, AC.ALLPAY_STATE, AC.CHANNELSET_PARAMETER_EXPAND, AC.CHANNELSET_PARAMETER_NAME, AC.CHANNELSET_PARAMETER_VALUE ");
                sql.append(getSql(allpayChannelSetDto, false));
        List<Map<String, Object>> list = null;
        if(isExport || null == currentPage){  //导出 或 不分页查询
            list = (List<Map<String, Object>>)hibernateDAO.listBySQL(sql.toString(), false);
        }else{  //查询
            list = (List<Map<String, Object>>)hibernateDAO.listByPageBySQL(sql.toString(), pageSize, currentPage, false);
        }
        return list;
    }

    @Override
    public int count(AllpayChannelSetDto allpayChannelSetDto) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT COUNT(A.CHANNELSET_MERCHANT_ID) AS SUM FROM (SELECT AC.CHANNELSET_MERCHANT_ID, AC.CHANNELSET_CHANNEL_CODE");
        sql.append(getSql(allpayChannelSetDto, true));
        sql.append(" ) A");
        return hibernateDAO.CountBySQL(sql.toString());
    }

    @Override
    public boolean insert(Allpay_ChannelSet allpayChannelSet) throws Exception {
        String result =  hibernateDAO.save(allpayChannelSet);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Allpay_ChannelSet allpayChannelSet) throws Exception {
        String result =  hibernateDAO.saveOrUpdate(allpayChannelSet);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public <T> T getById(Class<T> clazz, String posId) throws Exception {
        return hibernateDAO.listById(clazz, posId, false);
    }

    @Override
    public List<?> getByMerIdChanCode(String merchantId, String channelCode) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT AC.CHANNELSET_ID, AC.CHANNELSET_CHANNEL_CODE, AC.CHANNELSET_MERCHANT_ID, AC.CHANNELSET_PARAMETER_NAME, ");
        sql.append("AC.CHANNELSET_PARAMETER_VALUE, AC.CHANNELSET_PARAMETER_TYPE, AC.CHANNELSET_PARAMETER_EXPAND ");
        sql.append(getQueDelSql(merchantId, channelCode));
        return hibernateDAO.listBySQL(sql.toString(), false);
    }

    @Override
    public boolean removeByMerIdChanCode(String merchantId, String channelCode) throws Exception {
        StringBuffer sql = new StringBuffer("DELETE ");
        sql.append(getQueDelSql(merchantId, channelCode));
        String result = hibernateDAO.executeBySql(sql.toString());
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> obtainMerAndChanList(AllpayChannelSetDto allpayChannelSetDto, Integer currentPage, Integer pageSize, boolean isByPage) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT AC.CHANNELSET_MERCHANT_ID, AC.CHANNELSET_CHANNEL_CODE");
        sql.append(getSql(allpayChannelSetDto, true));
        List<Map<String, Object>> list = null;
        if(isByPage){
            list = (List<Map<String, Object>>)hibernateDAO.listByPageBySQL(sql.toString(), pageSize, currentPage, false);
        }else{
            list = (List<Map<String, Object>>)hibernateDAO.listBySQL(sql.toString(), false);
        }

        return list;
    }

    @Override
    public List<Allpay_ChannelSet> getChannelsByMerchantId(String merchantId,String code) throws Exception {
        String sql="FROM Allpay_ChannelSet T WHERE T.channelset_merchant_id = '"+merchantId+"' AND T.channelset_channel_code='"+code+"'";
        return (List<Allpay_ChannelSet>) hibernateDAO.listByHQL(sql);
    }

    /**
     * 修改商户已开通业务渠道
     * @param merchantId
     * @param channleList
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateMerChnnel(String merchantId, String channleList) throws Exception {
        String sql = "UPDATE ALLPAY_MERCHANT AM SET AM.MERCHANT_SHOPOPENCHANNEL = '"+channleList+"' WHERE AM.MERCHANT_ID = '"+merchantId+"'";
        String result = hibernateDAO.executeBySql(sql);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    /**
     * 获取公共查询sql
     * @param allpayChannelSetDto
     * @param isGroupBy
     * @return
     */
    public String getSql(AllpayChannelSetDto allpayChannelSetDto, boolean isGroupBy){
        StringBuffer sql = new StringBuffer(" FROM ALLPAY_CHANNELSET AC");
        sql.append(" LEFT JOIN ALLPAY_MERCHANT AM ON AC.CHANNELSET_MERCHANT_ID = AM.MERCHANT_ID AND AM.ALLPAY_LOGICDEL = 1")
                .append(" WHERE 1=1");

        if(!CommonHelper.isEmpty(allpayChannelSetDto.getMerchantName())){  //商户名称
            sql.append(" AND AM.MERCHANT_MERCHNAME LIKE '%").append(allpayChannelSetDto.getMerchantName()).append("%'");
        }
        if(!CommonHelper.isEmpty(allpayChannelSetDto.getBusiness())) {    //受理编号（channelSetId渠道配置表id）
            sql.append(" AND AC.CHANNELSET_ID = '").append(allpayChannelSetDto.getBusiness()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayChannelSetDto.getMerchantShortName())) {    //商户简称
            sql.append(" AND AC.MERCHANT_SHOPSHORTNAME LIKE '%").append(allpayChannelSetDto.getMerchantShortName()).append("%'");
        }
        if(!CommonHelper.isEmpty(allpayChannelSetDto.getStartTime())){  //受理开始时间
            String start = allpayChannelSetDto.getStartTime() + " 00:00:00";
            sql.append(" AND AC.ALLPAY_CREATETIME >= TO_DATE('").append(start).append("', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isEmpty(allpayChannelSetDto.getEndTime())){ //受理结束时间
            String end = allpayChannelSetDto.getEndTime() + " 23:59:59";
            sql.append(" AND AC.ALLPAY_CREATETIME <= TO_DATE('").append(end).append("', 'YYYY-MM-DD HH24:MI:SS')");

        }
        if(!CommonHelper.isEmpty(""+allpayChannelSetDto.getStatus())){  //受理状态  （未申请0 审核中1 已通过2 被驳回	3 待审核4）
            sql.append(" AND AC.CHANNELSET_PARAMETER_NAME = 'state' AND AC.CHANNELSET_PARAMETER_VALUE = '").append(allpayChannelSetDto.getStatus()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayChannelSetDto.getChoose())){  //状态 1启用 0停用
            sql.append(" AND AC.CHANNELSET_PARAMETER_NAME = 'choose' AND AC.CHANNELSET_PARAMETER_VALUE = '").append(allpayChannelSetDto.getChoose()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayChannelSetDto.getMerchantId())){  //商户id
            sql.append(" AND AC.CHANNELSET_MERCHANT_ID = '").append(allpayChannelSetDto.getMerchantId()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayChannelSetDto.getChannelCode())){  //业务渠道
            sql.append(" AND AC.CHANNELSET_CHANNEL_CODE IN (").append(allpayChannelSetDto.getChannelCode()).append(")");
        }
        if(!CommonHelper.isEmpty(allpayChannelSetDto.getCompanyId())){  //分公司id
            sql.append(" AND AM.MERCHANT_BRANCHCOMPANYID = '").append(allpayChannelSetDto.getCompanyId()).append("'");
        }

        if(isGroupBy){
            sql.append(" GROUP BY AC.CHANNELSET_MERCHANT_ID, AC.CHANNELSET_CHANNEL_CODE");  //分组
        }else{
            sql.append(" ORDER BY AC.ALLPAY_UPDATETIME DESC");
        }
        return sql.toString();
    }

    /**
     * 获取公共查询和删除sql
     * @param merchantId
     * @param channelCode
     * @return
     */
    public String getQueDelSql(String merchantId, String channelCode){
        StringBuffer sql = new StringBuffer("FROM ALLPAY_CHANNELSET AC ");
        sql.append("WHERE AC.CHANNELSET_MERCHANT_ID = '").append(merchantId).append("' ")
                .append("AND AC.CHANNELSET_CHANNEL_CODE = '").append(channelCode).append("'");

        return sql.toString();
    }
}
