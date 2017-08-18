package com.cn.dao.impl;

import com.cn.common.CommonHelper;
import com.cn.dao.AllpayPosParterDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.dto.AllpayPosParterDto;
import com.cn.entity.po.Allpay_PosParter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by sun.yayi on 2016/12/6.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Repository
public class AllpayPosparterDaoImpl implements AllpayPosParterDao {
    @Autowired
    private HibernateDAO hibernateDAO;

    public List<?> getPosPArterByParterId(String parterId, String parterName) throws Exception {
        String sql="SELECT POS.POSPARTER_ID FROM ALLPAY_POSPARTER POS WHERE 1=1";
        if(!CommonHelper.isEmpty(parterId)){
            sql += " AND POS.POSPARTER_ID='"+parterId+"'";
        }
        if(!CommonHelper.isEmpty(parterName)){
            sql += " AND POS.POSPARTER_PARTERALLNAME='"+parterName+"'";
        }
        sql += " AND POS.ALLPAY_LOGICDEL=1";
        return hibernateDAO.listBySQL(sql,false);
    }

    public boolean saveOrUpdate(Allpay_PosParter posParter) throws Exception {
        String result =  hibernateDAO.saveOrUpdate(posParter);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    public Allpay_PosParter getPosParterById(String parterId) throws Exception {
        Allpay_PosParter posParter=null;
        posParter=hibernateDAO.listById(Allpay_PosParter.class,parterId,false);
        return posParter;
    }

    @Override
    public List<Map<String, Object>> getPosParterBySql(String parterId) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT AP.POSPARTER_ID, AP.POSPARTER_PARTERALLNAME, AP.POSPARTER_PARTERSHORTNAME, AP.POSPARTER_CONNECTPERSON, AP.POSPARTER_CONNECTTEL,");
        sql.append("AP.POSPARTER_PARTERLEVEL, AP.POSPARTER_BRANCH_ID, AO.ORGANIZATION_NAME, AP.POSPARTER_BELONGPARTERID, SA.POSPARTER_PARTERALLNAME AS SA_PARTERALLNAME,");
        sql.append("AP.POSPARTER_PROVINCE_ID, AP.POSPARTER_PROVINCE_NAME, AP.POSPARTER_CITY_ID, AP.POSPARTER_CITY_NAME, AP.POSPARTER_COUNTRY_ID, AP.POSPARTER_COUNTRY_NAME,");
        sql.append("AP.POSPARTER_ADDRESS, AP.ALLPAY_ISSTART, AP.POSPARTER_EXPANDPEOPLE FROM ALLPAY_POSPARTER AP");
        sql.append(" LEFT JOIN ALLPAY_ORGANIZATION AO ON AP.POSPARTER_BRANCH_ID = AO.ORGANIZATION_ID");
        sql.append(" LEFT JOIN ALLPAY_POSPARTER SA ON AP.POSPARTER_BELONGPARTERID = SA.POSPARTER_ID WHERE AP.POSPARTER_ID = '").append(parterId).append("'");
        List<Map<String, Object>> list=(List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(), false);
        return list;
    }

    public List<Map<String, Object>> obtainList(AllpayPosParterDto bean, String currentPage, String pageSize) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT POS.POSPARTER_ID,POS.POSPARTER_PARTERALLNAME,POS.POSPARTER_PARTERLEVEL,AO.ORGANIZATION_ID,AO.ORGANIZATION_NAME,TO_CHAR(POS.ALLPAY_CREATETIME,'YYYY-MM-DD HH24:MI:SS') ALLPAY_CREATETIME,POS.ALLPAY_ISSTART");
        sql.append(getSql(bean));
        List<Map<String, Object>> list=null;
        if(null != currentPage && null != pageSize){
            list= (List<Map<String, Object>>) hibernateDAO.listByPageBySQL(sql.toString(),Integer.parseInt(pageSize),Integer.parseInt(currentPage)-1,false);
        }else{
            list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(),false);
        }
        return list;
    }

    public int count(AllpayPosParterDto bean) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT COUNT(POS.POSPARTER_ID) AS SUM");
        sql.append(getSql(bean));
        int count=hibernateDAO.CountBySQL(sql.toString());
        return count;
    }

    /**
     * 获取公共查询sql
     * @param bean
     * @return
     */
    public String getSql(AllpayPosParterDto bean){
        StringBuffer sql = new StringBuffer(" FROM ALLPAY_POSPARTER POS");
        sql.append(" LEFT JOIN ALLPAY_ORGANIZATION AO ON POS.POSPARTER_BRANCH_ID=AO.ORGANIZATION_ID AND AO.ALLPAY_LOGICDEL=1 ");
        sql.append(" WHERE 1=1");
        if(!CommonHelper.isEmpty(bean.getParterName())){
            sql.append(" AND POS.POSPARTER_PARTERALLNAME LIKE '%").append(bean.getParterName()).append("%'");
        }
        if(!CommonHelper.isEmpty(bean.getBranchId())){
            sql.append(" AND POS.POSPARTER_BRANCH_ID='").append(bean.getBranchId()).append("'");
        }
        if(!CommonHelper.isEmpty(bean.getBelongParterId())){
            sql.append(" AND POS.POSPARTER_BELONGPARTERID='").append(bean.getBelongParterId()).append("'");
        }
        if(!CommonHelper.isEmpty(bean.getParterLevel())){  //合作方级别	1：第一级合作方，2：第二级合作方
            sql.append(" AND POS.POSPARTER_PARTERLEVEL='").append(bean.getParterLevel()).append("'");
        }
        if(!CommonHelper.isEmpty(bean.getIsStart())){  // 是否启用（0:停用 1：启用  空为全部）
            sql.append(" AND POS.ALLPAY_ISSTART=").append(bean.getIsStart());
        }
        if(!CommonHelper.isEmpty(bean.getCreateStartTime())){  //录入开始时间
            String start = bean.getCreateStartTime()+" 00:00:00";
            sql.append(" AND POS.ALLPAY_CREATETIME >= TO_DATE('").append(start).append("', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isEmpty(bean.getCreateEndTime())){ //录入结束时间
            String end = bean.getCreateEndTime()+" 23:59:59";
            sql.append(" AND POS.ALLPAY_CREATETIME <= TO_DATE('").append(end).append("', 'YYYY-MM-DD HH24:MI:SS')");
        }
        sql.append(" AND POS.ALLPAY_LOGICDEL=1 ORDER BY POS.ALLPAY_UPDATETIME DESC");
        return sql.toString();
    }

}
