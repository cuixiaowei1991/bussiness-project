package com.cn.dao.impl;

import com.cn.common.CommonHelper;
import com.cn.dao.AllpayPreRegistShopDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.dto.AllpayPreRegistShopDto;
import com.cn.entity.po.Allpay_PreRegistShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by sun.yayi on 2017/2/8.
 */
@Repository
public class AllpayPreRegistShopDaoImpl implements AllpayPreRegistShopDao {
    @Autowired
    private HibernateDAO hibernateDAO;

    @Override
    public List<Map<String, Object>> obtainList(AllpayPreRegistShopDto bean, int currentPage, int pageSize) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT ROWNUM,P.PREREGISTSHOPID,P.BRANCHCOMPANYNAME,P.BRANCHCOMPANYSHORTNAME,TO_CHAR(P.CDATE,'yyyy-mm-dd hh24:mi:ss') AS CDATE,P.PERSON,P.PERSONPHONE,")
                .append("P.SHOPNAME,P.SHOPSTATE,P.STOREADRESS,P.STORENAME,P.STOREPROVINCENAME FROM ALLPAY_PREREGISTSHOP P WHERE 0=0");
        String startCtime=CommonHelper.nullToString(bean.getStartCTime());
        String endCtime=CommonHelper.nullToString(bean.getEndCTime());
        if(!CommonHelper.isNullOrEmpty(startCtime) && !CommonHelper.isNullOrEmpty(endCtime)){
            sql.append(" AND TO_CHAR(P.CDATE, 'yyyy-mm-dd') BETWEEN '"+startCtime+"' AND '"+endCtime+"' ");
            //sql.append(" AND P.CDATE>=TO_DATE('"+startCtime+"','yyyy-mm-dd hh24:mi:ss') AND P.CDATE<= TO_DATE('"+endCtime+"','yyyy-mm-dd hh24:mi:ss')");
        }else if(!CommonHelper.isNullOrEmpty(startCtime)){
            sql.append(" AND P.CDATE>=TO_DATE('"+startCtime+"','yyyy-mm-dd')");
        }else if(!CommonHelper.isNullOrEmpty(endCtime)){
            sql.append(" AND P.CDATE>=TO_DATE('"+endCtime+"','yyyy-mm-dd')");
        }
        if(!CommonHelper.isNullOrEmpty(bean.getBranchCompanyName())){
            sql.append(" AND P.BRANCHCOMPANYNAME like '%"+bean.getBranchCompanyName()+"%'");
        }
        if(!CommonHelper.isNullOrEmpty(bean.getShopState())){
            sql.append(" AND P.SHOPSTATE='"+bean.getShopState()+"'");
        }
        if(!CommonHelper.isNullOrEmpty(bean.getStoreprovinceName())){
            sql.append(" AND P.STOREPROVINCENAME='"+bean.getStoreprovinceName()+"'");
        }
        sql.append(" ORDER BY P.CDATE DESC");
        List<Map<String, Object>> list=null;
        list= (List<Map<String, Object>>) hibernateDAO.listByPageBySQL(sql.toString(), pageSize, currentPage, false);
        return list;
    }

    @Override
    public int count(AllpayPreRegistShopDto bean) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS C FROM ALLPAY_PREREGISTSHOP P WHERE 0=0");
        String startCtime=CommonHelper.nullToString(bean.getStartCTime());
        String endCtime=CommonHelper.nullToString(bean.getEndCTime());
        if(!CommonHelper.isNullOrEmpty(startCtime) && !CommonHelper.isNullOrEmpty(endCtime)){
            sql.append(" AND TO_CHAR(P.CDATE, 'yyyy-mm-dd') BETWEEN '"+startCtime+"' AND '"+endCtime+"' ");
            //sql.append(" AND P.CDATE>=TO_DATE('"+startCtime+"','yyyy-mm-dd hh24:mi:ss') AND P.CDATE<= TO_DATE('"+endCtime+"','yyyy-mm-dd hh24:mi:ss')");
        }else if(!CommonHelper.isNullOrEmpty(startCtime)){
            sql.append(" AND P.CDATE>=TO_DATE('"+startCtime+"','yyyy-mm-dd')");
        }else if(!CommonHelper.isNullOrEmpty(endCtime)){
            sql.append(" AND P.CDATE>=TO_DATE('"+endCtime+"','yyyy-mm-dd')");
        }
        if(!CommonHelper.isNullOrEmpty(bean.getBranchCompanyName())){
            sql.append(" AND P.BRANCHCOMPANYNAME like '%"+bean.getBranchCompanyName()+"%'");
           // sql.append(" AND P.BRANCHCOMPANYNAME='"+bean.getBranchCompanyName()+"'");
        }
        if(!CommonHelper.isNullOrEmpty(bean.getShopState())){
            sql.append(" AND P.SHOPSTATE='"+bean.getShopState()+"'");
        }
        if(!CommonHelper.isNullOrEmpty(bean.getStoreprovinceName())){
            sql.append(" AND P.STOREPROVINCENAME='"+bean.getStoreprovinceName()+"'");
        }
        List<Map<String, Object>> list=null;
        list= (List<Map<String, Object>>)hibernateDAO.listBySQL(sql.toString(),false);
        int count=Integer.parseInt(CommonHelper.nullToString(list.get(0).get("C")));
        return count;
    }

    @Override
    public String insert(Allpay_PreRegistShop preRegistShop) throws Exception {
        hibernateDAO.save(preRegistShop);
        return "success";
    }


}
