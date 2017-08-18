package com.cn.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cn.dao.util.HibernateDAO;

/**
 * 单点登录查询
 * @author songzhili
 * 2017年2月14日上午11:38:08
 */
@Transactional
@Repository("ssoAuthDao")
public class SsoAuthDaoImpl {
   
    @Autowired
    private HibernateDAO hibernateDAO;
	/****/
    private static String businessSql = null;
    /****/
    private static String merchantSql = null;
    /****/
    private static String merchantIdSql = null;
    
    
    static{
    	StringBuilder together = new StringBuilder();
		together.append("select usr.USER_ID,usr.USER_NAME,usr.USER_PHONE,usr.USER_NICKNAME");
		together.append(",usr.USER_PASSWORD,usr.USER_ISSTART,usr.USER_ORGANIZATIONID,rol.ROLE_ISSTART");
		together.append(",rol.ROLE_NAME,rol.ROLE_ID,org.ORGANIZATION_NAME,org.ORGANIZATION_TYPE");
		together.append(" from ALLPAY_USER usr inner join ALLPAY_ROLE rol on usr.USER_ROLEID = rol.ROLE_ID");
		together.append(" inner join ALLPAY_ORGANIZATION org on usr.USER_ORGANIZATIONID = org.ORGANIZATION_ID");
		together.append(" where 1=1 AND rol.ROLE_ISSTART = 1 AND rol.ALLPAY_LOGICDEL = '1'");
		together.append(" AND org.ORGANIZATION_STATE = 1 AND org.ALLPAY_LOGICDEL = '1'");
		together.append(" AND usr.USER_ISSTART = 1 AND usr.ALLPAY_LOGICDEL = '1'");
    	businessSql = together.toString();
    	/*****/
    	together.setLength(0);
    	together.append("select sho.SHOPUSER_ID,sho.SHOPUSER_NICKNAME,sho.SHOPUSER_NAME");
		together.append(",sho.SHOPUSER_PHONE,sho.SHOPUSER_PASSWORD,sho.SHOPUSER_ISSTART");
		together.append(",sho.SHOPUSER_ROLE,mer.MERCHANT_MERCHNAME,mer.MERCHANT_ID,mer.ALLPAY_ISSTART");
		together.append(" from ALLPAY_SHOPUSER sho inner join ALLPAY_MERCHANT mer ");
		together.append(" on sho.SHOPUSER_SHOPID = mer.MERCHANT_ID");
		together.append(" where 1=1 AND mer.ALLPAY_LOGICDEL = '1'");
		together.append(" AND sho.SHOPUSER_ISSTART = 1 AND sho.ALLPAY_LOGICDEL = '1'");
    	merchantSql = together.toString();
    	/****/
    	together.setLength(0);
    	together.append("select * from (select sho.SHOPUSER_ID,sho.SHOPUSER_NICKNAME");
		together.append(",sho.SHOPUSER_NAME,sho.SHOPUSER_PHONE,sho.SHOPUSER_PASSWORD");
		together.append(",sho.SHOPUSER_ISSTART,sho.SHOPUSER_ROLE,mer.MERCHANT_MERCHNAME");
		together.append(",mer.MERCHANT_ID,mer.ALLPAY_ISSTART from ALLPAY_SHOPUSER sho");
		together.append(" inner join ALLPAY_MERCHANT mer on sho.SHOPUSER_SHOPID = mer.MERCHANT_ID");
		together.append(" where 1=1 AND sho.SHOPUSER_ISSTART = 1 AND sho.SHOPUSER_ROLE = '2'");
		together.append(" AND sho.ALLPAY_LOGICDEL = '1' AND mer.ALLPAY_LOGICDEL = '1'");
		together.append(" AND mer.MERCHANT_ID = '");
    	merchantIdSql = together.toString();
    }
    /**
     * 业务系统查询
     * @param source
     * @return
     */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForBusiness(Map<String, Object> source) throws Exception{
		
		StringBuilder together = new StringBuilder();
		/**根据用户名查询**/
		together.append(businessSql).append(" AND usr.USER_NAME = '")
		.append(source.get("username")).append("'");
		List<Map<String, Object>> result = 
				(List<Map<String, Object>>)hibernateDAO.listBySQL(together.toString(), false);
		if(!result.isEmpty()){
			return result.get(0);
		}
		/**根据注册手机号查询**/
		together.setLength(0);
		together.append(businessSql).append(" AND usr.USER_PHONE = '")
		.append(source.get("username")).append("'");;
		result = (List<Map<String, Object>>)hibernateDAO.listBySQL(together.toString(), false);
		if(!result.isEmpty()){
			return result.get(0);
		}
		return null;
	}
	/**
	 * 商助用户登录
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForMerhcant(Map<String, Object> source)throws Exception{
		
		StringBuilder together = new StringBuilder();
		/**根据用户名查询**/
		together.append(merchantSql).append(" AND sho.SHOPUSER_NAME = '")
		.append(source.get("username")).append("'");
		List<Map<String, Object>> result = 
				(List<Map<String, Object>>)hibernateDAO.listBySQL(together.toString(), false);
		if(!result.isEmpty()){
			return result.get(0);
		}
		/**根据手机号查询**/
		together.setLength(0);
		together.append(merchantSql).append(" AND sho.SHOPUSER_PHONE = '")
		.append(source.get("username")).append("'");
		result = (List<Map<String, Object>>)hibernateDAO.listBySQL(together.toString(), false);
		if(!result.isEmpty()){
			return result.get(0);
		}
		/**根据账号Id查询**/
		together.setLength(0);
		together.append(merchantSql).append(" AND sho.SHOPUSER_ACCOUNTID = '")
		.append(source.get("username")).append("'");
		result = (List<Map<String, Object>>)hibernateDAO.listBySQL(together.toString(), false);
		if(!result.isEmpty()){
			return result.get(0);
		}
		return null;
	}
	
	/**
	 * 业务系统跳转到商助系统
	 * @param source
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryForMerhcantId(Map<String, Object> source)throws Exception{
		
		StringBuilder together = new StringBuilder();
		together.append(merchantIdSql).append(source.get("merchantId")).append("'");
		together.append(" order by sho.ALLPAY_CREATETIME ASC) where ROWNUM <=1");
		List<Map<String, Object>> result = 
				(List<Map<String, Object>>)hibernateDAO.listBySQL(together.toString(), false);
		if(!result.isEmpty()){
			return result.get(0);
		}
		return null;
	}
}





