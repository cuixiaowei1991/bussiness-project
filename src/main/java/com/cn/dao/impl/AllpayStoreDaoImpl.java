package com.cn.dao.impl;

import java.util.List;
import java.util.Map;

import com.cn.common.CommonHelper;
import com.cn.dao.AllpayStoreDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.po.Allpay_Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
/**
 * 门店管理
 * @author songzhili
 * 2016年12月1日下午4:04:00
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Repository("allpayStoreDao")
public class AllpayStoreDaoImpl implements AllpayStoreDao {

    @Autowired
    private HibernateDAO hibernateDAO;

	@Override
	public String insert(Allpay_Store store) throws Exception {
		
		hibernateDAO.save(store);
		return "success";
	}

	@Override
	public String delete(String storeId,boolean serious,String logRecord) throws Exception {
		
		if(serious){
			hibernateDAO.removeById(Allpay_Store.class, storeId);
		}else{
			StringBuilder together = new StringBuilder();
			together.append("update ALLPAY_STORE sto set sto.ALLPAY_LOGICDEL = '2'");
			together.append(",sto.ALLPAY_LOGRECORD = '").append(logRecord).append("'");
			together.append(" where sto.STORE_ID = '").append(storeId).append("'");
			hibernateDAO.executeBySql(together.toString());
		}
		return "success";
	}

	@Override
	public String update(Allpay_Store store) throws Exception {
		
		hibernateDAO.update(store);
		return "success";
	}
	
	@Override
	public Allpay_Store obtain(String storeId) {

	   Allpay_Store store = hibernateDAO.listById(Allpay_Store.class, storeId,
				false);
	   return store;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkStoreExistForStoreName(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("storeName"))){
			return false;
		}
		StringBuilder together = new StringBuilder();
		together.append("select sto.STORE_ID from ALLPAY_STORE sto where ");
		together.append(" sto.STORE_MERCHANT_ID = '").append(source.get("merchantId")).append("'");
		if(!CommonHelper.isNullOrEmpty(source.get("storeName"))){
			together.append(" AND sto.STORE_SHOPNAME = '").append(source.get("storeName")).append("'");
		}
		together.append(" AND sto.ALLPAY_LOGICDEL=1");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		if(!list.isEmpty()){
			Map<String, Object> temMap = list.get(0);
			if(CommonHelper.isNullOrEmpty(source.get("storeId"))){
				return true;
			}else if(!source.get("storeId").equals(temMap.get("STORE_ID"))){
				return true;
			}
			return false;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkStoreExistForStoreNum(Map<String, Object> source)
			throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("shopIdNum"))
				&& CommonHelper.isNullOrEmpty(source.get("shopNum"))){
			return false;
		}
		StringBuilder together = new StringBuilder();
		together.append("select sto.STORE_ID from ALLPAY_STORE sto where ");
		together.append(" sto.STORE_MERCHANT_ID = '").append(source.get("merchantId")).append("'");
		if(!CommonHelper.isNullOrEmpty(source.get("shopIdNum"))){
		   together.append(" AND sto.STORE_SHOPIDNUMBER = '").append(source.get("shopIdNum")).append("'");
	    }
	    if(!CommonHelper.isNullOrEmpty(source.get("shopNum"))){
		   together.append(" AND sto.STORE_SHOPNUMBER = '").append(source.get("shopNum")).append("'");
	    }
		together.append(" AND sto.ALLPAY_LOGICDEL=1");
	    List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		if(!list.isEmpty()){
			Map<String, Object> temMap = list.get(0);
			if(CommonHelper.isNullOrEmpty(source.get("storeId"))){
				return true;
			}else if(!source.get("storeId").equals(temMap.get("STORE_ID"))){
				return true;
			}
			return false;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> obtainForStoreId(String storeId) throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select sto.STORE_SHOPNAME,sto.STORE_SHOPIDNUMBER,sto.STORE_ADDRESS,sto.STORE_SHOPNUMBER");
		together.append(",sto.STORE_PROVINCENAME,sto.STORE_PROVINCEID,sto.STORE_CITYNAME,sto.STORE_POSSHOPNAME");
		together.append(",sto.STORE_COUNTRY_ID,sto.STORE_COUNTRY_NAME,sto.STORE_CITYID,sto.STORE_SHOPSHORTNAME");
		together.append(",sto.STORE_LOCATIONTUDE,sto.STORE_SPECIALSERVICES,sto.STORE_CUSTOMERSERVICEPHONE");
		together.append(",TO_CHAR(sto.STORE_BUSINESSSTARTHOURS,'YYYY-MM-DD HH24:MI:SS') BEGIN_TIME");
		together.append(",TO_CHAR(sto.STORE_BUSINESSENDHOURS,'YYYY-MM-DD HH24:MI:SS') END_TIME");
		together.append(",TO_CHAR(sto.ALLPAY_CREATETIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME,sto.ALLPAY_CREATER");
		together.append(",TO_CHAR(sto.ALLPAY_UPDATETIME,'YYYY-MM-DD HH24:MI:SS') UPDATE_TIME,sto.ALLPAY_UPDATER");
		together.append(",sto.ALLPAY_ISSTART,mer.MERCHANT_ID,mer.MERCHANT_MERCHNAME from ALLPAY_STORE");
		together.append(" sto join ALLPAY_MERCHANT　mer on mer.MERCHANT_ID = sto.STORE_MERCHANT_ID");
		together.append(" where sto.STORE_ID = '").append(storeId).append("'");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> obtainConsumerImage(String storeId)
			throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select ima.FILEUPLOAD_PATH,ima.FILEUPLOAD_NAME from ALLPAY_FILEUPLOAD ima ");
		together.append(" where ima.FILEUPLOAD_RECORD = '").append(storeId).append("'");
		together.append(" AND ima.FILEUPLOAD_TYPE = '1' ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> obtainList(Map<String, Object> source,boolean export)
			throws Exception {
		
		int currentPage = 1;
		int pageSize = 20000;
		if(!export){
			currentPage = Integer.parseInt(source.get("curragePage").toString());
			pageSize = Integer.parseInt(source.get("pageSize").toString());
		}
		StringBuilder together = new StringBuilder();
		together.append("select sto.STORE_SHOPNAME,ROWNUM R,sto.STORE_ADDRESS,sto.ALLPAY_CREATER,sto.ALLPAY_ISSTART");
		together.append(",mer.MERCHANT_MERCHNAME,TO_CHAR(sto.ALLPAY_CREATETIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME");
		if(!export){
			together.append(",sto.STORE_ID,mer.MERCHANT_ID,sto.STORE_SHOPIDNUMBER,sto.STORE_SHOPNUMBER");
		}
		together.append(" from ALLPAY_STORE sto left join ALLPAY_MERCHANT　mer on mer.MERCHANT_ID = sto.STORE_MERCHANT_ID AND mer.ALLPAY_LOGICDEL = '1'");
		together.append(" left join ALLPAY_AGENT age on mer.merchant_agentid = age.agent_id AND age.ALLPAY_LOGICDEL = '1'");
		together.append(obtainConditionSQL(source));
		together.append(" order by sto.ALLPAY_UPDATETIME desc, R DESC");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listByPageBySQL(together.toString(),
						pageSize, currentPage - 1, false);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int count(Map<String, Object> source) throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select count(*) numberr from ALLPAY_STORE sto");
		together.append(" left join ALLPAY_MERCHANT　mer on mer.MERCHANT_ID = sto.STORE_MERCHANT_ID AND mer.ALLPAY_LOGICDEL = '1'");
		together.append(" left join ALLPAY_AGENT age on mer.merchant_agentid = age.agent_id AND age.ALLPAY_LOGICDEL = '1'");
		together.append(obtainConditionSQL(source));
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		if(!list.isEmpty()){
			Map<String, Object> map = list.get(0);
			return Integer.parseInt(map.get("NUMBERR").toString());
		}
		return 0;
	}

	/**
	 * 判断当前商户下门店名称是否存在
	 * @param merName
	 * @param storeName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<?> checkStore(String merName, String storeName) throws Exception {
		String sql = "SELECT AST.STORE_ID, AST.STORE_SHOPNAME, AST.STORE_SHOPNUMBER FROM ALLPAY_STORE AST LEFT JOIN ALLPAY_MERCHANT AM ON AST.STORE_MERCHANT_ID = AM.MERCHANT_ID WHERE (AST.STORE_SHOPNAME = '"+storeName+"' AND AM.MERCHANT_MERCHNAME = '"+merName+"')";
		sql += " OR (AST.STORE_ID = '"+storeName+"' AND AM.MERCHANT_ID = '"+merName+"')";
		sql += " AND AST.ALLPAY_LOGICDEL = 1 AND AM.ALLPAY_LOGICDEL = 1";
		List list = hibernateDAO.listBySQL(sql, false);
		return list;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> obtainTerminalPosList(String storeId)
			throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select ter.TERMINAL_POSID,ter.TERMINAL_TERMCODE,ter.TERMINAL_BRANCHID");
		together.append(",ter.TERMINAL_SUPERTERMCODE,ter.ALLPAY_ISSTART,ter.TERMINAL_POSTYPE");
		together.append(",TO_CHAR(ter.ALLPAY_CREATETIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME");
		together.append(",mer.MERCHANT_ID,mer.MERCHANT_MERCHNAME,mer.MERCHANT_SHOPNUMBER");
		together.append(",bra.BRANCH_NAME,pos.POSPARTER_ID,pos.POSPARTER_PARTERALLNAME");
		together.append(" from ALLPAY_TERMINAL ter ");
		together.append(" left join ALLPAY_MERCHANT mer on ter.TERMINAL_MERCHANT_ID = mer.MERCHANT_ID");
		together.append(" left join ALLPAY_BRANCH bra on ter.TERMINAL_BRANCHID = bra.BRANCH_ID");
		together.append(" left join ALLPAY_POSPARTER pos on ter.TERMINAL_PEIJIANPOSPARTERID = pos.POSPARTER_ID");
		together.append(" where ter.TERMINAL_STORE_ID = '").append(storeId).append("'");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		return list;
	}
	/**
	 * 
	 * @param source
	 * @return
	 */
	private  String obtainConditionSQL(Map<String, Object> source){
	     
		StringBuilder together = new StringBuilder();
		together.append(" where 1=1 AND sto.ALLPAY_LOGICDEL = '1'");
		if(!CommonHelper.isNullOrEmpty(source.get("storeName"))){
			together.append(" AND sto.STORE_SHOPNAME like '%").append(source.get("storeName")).append("%'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("merchantId"))){
			together.append(" AND sto.STORE_MERCHANT_ID = '").append(source.get("merchantId")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("agentId"))){
			String userNameFromAgentCookie = CommonHelper.nullToString(source.get("userNameFromAgentCookie"));
			if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
				together.append(" AND (mer.MERCHANT_AGENTID='").append(source.get("agentId")).append("'");
				together.append(" OR age.AGENT_ID='").append(source.get("agentId")).append("' OR age.AGENT_LOCATION = '").append(source.get("agentId")).append("')");
			}else{
				together.append(" AND mer.MERCHANT_AGENTID='").append(source.get("agentId")).append("'");
			}
		}
		if(!CommonHelper.isNullOrEmpty(source.get("provinceId"))){
			together.append(" AND sto.STORE_PROVINCEID = '").append(source.get("provinceId")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("cityId"))){
			together.append(" AND sto.STORE_CITYID = '").append(source.get("cityId")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("countryId"))){
			together.append(" AND sto.STORE_COUNTRY_ID = '").append(source.get("countryId")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("storeStatus"))){
			together.append(" AND sto.ALLPAY_ISSTART = ").append(source.get("storeStatus"));
		}
		return together.toString();
	}
	@Override
	public Allpay_Store getStoreInfoByParameter(String filds, String parameter) throws Exception
	{
		return hibernateDAO.listByField(Allpay_Store.class,filds,parameter);
	}

	/**
	 * 随机生成num位网点编号
	 * @param num
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getShopIdNum(int num) throws Exception {
		String result="";
		while (true) {
			result = CommonHelper.getRandomNum(num);
			String sql = "SELECT S.STORE_SHOPIDNUMBER FROM ALLPAY_STORE S WHERE S.STORE_SHOPIDNUMBER = '"+result + "'";
			int count = 0;
			@SuppressWarnings("rawtypes")
			List list = hibernateDAO.listBySQL(sql, false);
			if(null != list && list.size() >0){
				count = list.size();
			}
			if (count == 0) {
				break;
			}
		}
		return result;
	}
}