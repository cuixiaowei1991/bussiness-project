package com.cn.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cn.common.CommonHelper;
import com.cn.dao.AllpayMerchantDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.po.Allpay_ComUserInfo;
import com.cn.entity.po.Allpay_Merchant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 商户管理
 * @author songzhili
 * 2016年12月1日下午2:06:05
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Repository("allpayMerchantDao")
public class AllpayMerchantDaoImpl implements AllpayMerchantDao {
	
	
    @Autowired
    private HibernateDAO hibernateDAO;
    
    
	public String insert(Allpay_Merchant merchant) throws Exception {
		
		hibernateDAO.save(merchant);
		return "success";
	}
	
	public String delete(String merchantId,boolean serious,String logRecord) throws Exception {
		
		if(serious){
			hibernateDAO.removeById(Allpay_Merchant.class, merchantId);
		}else{
			StringBuilder together = new StringBuilder();
			together.append("update Allpay_Merchant mer set mer.ALLPAY_LOGICDEL = '2'");
			together.append(",mer.ALLPAY_LOGRECORD = '").append(logRecord).append("'");
			together.append(" where mer.MERCHANT_ID = '").append(merchantId).append("'");
			hibernateDAO.executeBySql(together.toString());
		}
		return "success";
	}


	public String update(Allpay_Merchant merchant) throws Exception {
		
		hibernateDAO.update(merchant);
		return "success";
	}
	
	@Override
	public String updateStatus(String merchantId,String status, String remark) throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("update ALLPAY_MERCHANT　mer set mer.ALLPAY_STATE = ").append(status);
		if(!CommonHelper.isEmpty(remark)){
			together.append(", mer.MERCHANT_REJECTREMARK = '").append(remark).append("'");
		}
		together.append(" where mer.MERCHANT_ID = '").append(merchantId).append("'");
		hibernateDAO.executeBySql(together.toString());
		return "success";
	}
	
	
    public Allpay_Merchant obtain(String merchantId) {
    	
       Allpay_Merchant merchant = 
    		   hibernateDAO.listById(Allpay_Merchant.class, merchantId, false);
       return merchant;
    }
    
    @SuppressWarnings("unchecked")
    @Override
	public boolean obtainByMerchantName(Map<String, Object> source) throws Exception {
    	
        StringBuilder together = new StringBuilder();
        together.append("select mer.MERCHANT_ID from ALLPAY_MERCHANT mer");
        together.append(" where mer.MERCHANT_MERCHNAME = '").append(source.get("mhtName")).append("'");
        together.append(" AND mer.ALLPAY_LOGICDEL = '1' ");
        List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
        if(!list.isEmpty()){
        	Map<String, Object> map = list.get(0);
        	if(CommonHelper.isNullOrEmpty(source.get("merchantId"))){
        		return true;
        	}else if(!source.get("merchantId").toString().equals(map.get("MERCHANT_ID"))){
        		return true;
        	}
        	return false;
        }
		return false;
	}
    
    @SuppressWarnings("unchecked")
	public Map<String, Object> obtainMerMsgForMerchantId(String merchantId)
			throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select mer.MERCHANT_MERCHNAME,mer.MERCHANT_AGENTID,mer.MERCHANT_SHOPSHORTNAME");
		together.append(",mer.MERCHANT_INDUSTRY,mer.MERCHANT_INDUSTRYNUM,mer.MERCHANT_PROVINCEID");
		together.append(",mer.MERCHANT_CITYID,mer.MERCHANT_COUNTRY_ID,mer.MERCHANT_ADDRESS,mer.ALLPAY_ISSTART");
		together.append(",mer.MERCHANT_IMGURL,mer.MERCHANT_CONTACTSPHONE,mer.MERCHANT_CONTACTSNAME");
		together.append(",mer.MERCHANT_BRANCHCOMPANYID,mer.MERCHANT_BRANCHCOMPANYNAME,mer.ALLPAY_STATE,sto.STORE_COUNT");
		together.append(",age.AGENT_NAME,sage.AGENT_ID,usr.SHOPUSER_NAME,usr.SHOPUSER_PHONE,usr.SHOPUSER_ID");
		together.append(" from ALLPAY_MERCHANT　mer left join ALLPAY_AGENT age on mer.MERCHANT_AGENTID = age.AGENT_ID");
		together.append(" left join ALLPAY_AGENT sage on age.agent_location = sage.AGENT_ID");
		together.append(" left join ALLPAY_SHOPUSER usr on mer.MERCHANT_ID = usr.SHOPUSER_SHOPID");
		together.append(" left join (select count(k.STORE_MERCHANT_ID) store_Count,k.STORE_MERCHANT_ID");
		together.append(" from ALLPAY_STORE k where k.ALLPAY_LOGICDEL = '1' group by k.STORE_MERCHANT_ID) sto");
		together.append(" on mer.MERCHANT_ID = sto.STORE_MERCHANT_ID ");
		together.append(" where mer.MERCHANT_ID = '").append(merchantId).append("'");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
    
    @SuppressWarnings("unchecked")
    @Override
	public List<Map<String, Object>> obtainMerChannelMsg(String merchantId)
			throws Exception {
    	
    	StringBuilder together = new StringBuilder();
    	together.append("select cha.CHANNELSET_PARAMETER_VALUE,cha.CHANNELSET_PARAMETER_EXPAND");
    	together.append(",TO_CHAR(cha.ALLPAY_CREATETIME,'YYYY-MM-DD') CREATE_TIME");
    	together.append(",cha.CHANNELSET_CHANNEL_CODE,cha.CHANNELSET_PARAMETER_NAME");
    	together.append(" from ALLPAY_CHANNELSET cha where cha.CHANNELSET_MERCHANT_ID='");
    	together.append(merchantId).append("'");
    	List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		return list;
	}

	@Override
	public Allpay_ComUserInfo obtainComsumerInfo(String merchantId)
			throws Exception {
		
		Allpay_ComUserInfo comsumer = hibernateDAO.listByField(Allpay_ComUserInfo.class, 
    			"comuser_shopid", merchantId);
		return comsumer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> obtainConsumerImage(String comuserId)
			throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select ima.FILEUPLOAD_PATH,ima.FILEUPLOAD_NAME from ALLPAY_FILEUPLOAD ima ");
		together.append(" where ima.FILEUPLOAD_RECORD = '").append(comuserId).append("'");
		together.append(" AND ima.FILEUPLOAD_TYPE = '1' ");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		return list;
	}
	

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> obtainList(Map<String, Object> source,boolean export)
			throws Exception {
		
		int currentPage = 1;
		int pageSize = 10000;
		if(!export){
			currentPage = Integer.parseInt(source.get("curragePage").toString());
			pageSize = Integer.parseInt(source.get("pageSize").toString());
		}
		StringBuilder together = new StringBuilder();
		together.append("select mer.MERCHANT_MERCHNAME,mer.ALLPAY_CREATER,mer.MERCHANT_INDUSTRY");
		together.append(",TO_CHAR(mer.ALLPAY_CREATETIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME");
		together.append(",mer.ALLPAY_ISSTART,mer.ALLPAY_STATE,mer.MERCHANT_ID");
		together.append(",mer.MERCHANT_SHOPOPENCHANNEL,mer.ALLPAY_UPDATETIME");
		together.append(",mer.MERCHANT_ADDRESS,sto.STORE_COUNT,age.AGENT_NAME,age.AGENT_LEVEL");
		if(export){
			together.append(",at1.chuan_pos_count,at2.ruan_pos_count,at4.pc_pos_count,org.ORGANIZATION_NAME");
			together.append(",age.AGENT_LEGALPERSONNAME,mer.MERCHANT_PROVINCENAME,mer.MERCHANT_CITYNAME");
		}
		if(!export){
			together.append(",mer.MERCHANT_SHOPSHORTNAME,mer.MERCHANT_AGENTID,mer.MERCHANT_INDUSTRYNUM");
			together.append(",mer.MERCHANT_BRANCHCOMPANYID,mer.MERCHANT_BRANCHCOMPANYNAME");
		}
		together.append(" from ALLPAY_MERCHANT　mer");
		together.append(" left join (select count(k.STORE_MERCHANT_ID) store_Count,k.STORE_MERCHANT_ID");
		together.append(" from ALLPAY_STORE k where k.ALLPAY_LOGICDEL = '1' group by k.STORE_MERCHANT_ID) sto");
		together.append(" on mer.MERCHANT_ID=sto.STORE_MERCHANT_ID ");
		together.append(" left join ALLPAY_AGENT age on mer.MERCHANT_AGENTID = age.AGENT_ID");
        if(export){
        	/**所属分公司**/
        	together.append(" left join ALLPAY_ORGANIZATION org on age.AGENT_BRANCH_ID = org.ORGANIZATION_ID");
        	/**传统pos**/
        	together.append(" left join (select count(t1.TERMINAL_MERCHANT_ID) chuan_pos_count,t1.TERMINAL_MERCHANT_ID");
        	together.append(" from ALLPAY_TERMINAL t1 where t1.ALLPAY_LOGICDEL = '1' AND t1.TERMINAL_POSTYPE = 1");
        	together.append(" group by t1.TERMINAL_MERCHANT_ID) at1");
        	together.append(" on mer.MERCHANT_ID = at1.TERMINAL_MERCHANT_ID ");
        	/**软pos**/
        	together.append(" left join (select count(t2.TERMINAL_MERCHANT_ID) ruan_pos_count,t2.TERMINAL_MERCHANT_ID");
        	together.append(" from ALLPAY_TERMINAL t2 where t2.ALLPAY_LOGICDEL = '1' AND t2.TERMINAL_POSTYPE = 2");
        	together.append(" group by t2.TERMINAL_MERCHANT_ID) at2");
        	together.append(" on mer.MERCHANT_ID = at2.TERMINAL_MERCHANT_ID ");
        	/**pc pos**/
        	together.append(" left join (select count(t4.TERMINAL_MERCHANT_ID) pc_pos_count,t4.TERMINAL_MERCHANT_ID");
        	together.append(" from ALLPAY_TERMINAL t4 where t4.ALLPAY_LOGICDEL = '1' AND t4.TERMINAL_POSTYPE = 4");
        	together.append(" group by t4.TERMINAL_MERCHANT_ID) at4");
        	together.append(" on mer.MERCHANT_ID = at4.TERMINAL_MERCHANT_ID ");
		}
		together.append(obtainConditionSQL(source));
		if(!CommonHelper.isNullOrEmpty(source.get("mhtOpenChannel"))){
			together.append(" AND exists (select cha.CHANNELSET_MERCHANT_ID from ");
			together.append(obtainChannelSql(source.get("mhtOpenChannel").toString()));
			together.append(" cha where mer.MERCHANT_ID　=　cha.CHANNELSET_MERCHANT_ID)");
		}
		together.append(" order by mer.ALLPAY_UPDATETIME desc,rownum desc");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listByPageBySQL(together.toString(), pageSize, currentPage - 1, false);
		return list;
	}


	@SuppressWarnings("unchecked")
	public int count(Map<String, Object> source) throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select count(DISTINCT mer.MERCHANT_ID) numberr");
		together.append(" from ALLPAY_MERCHANT　mer");
		together.append(" left join (select count(k.STORE_MERCHANT_ID) store_Count,k.STORE_MERCHANT_ID");
		together.append(" from ALLPAY_STORE k where k.ALLPAY_LOGICDEL = '1' group by k.STORE_MERCHANT_ID) sto ");
		together.append(" on mer.MERCHANT_ID=sto.STORE_MERCHANT_ID ");
		together.append(" left join ALLPAY_AGENT age on mer.MERCHANT_AGENTID = age.AGENT_ID");
		together.append(obtainConditionSQL(source));
		if(!CommonHelper.isNullOrEmpty(source.get("mhtOpenChannel"))){
			together.append(" AND exists (select cha.CHANNELSET_MERCHANT_ID from ");
			together.append(obtainChannelSql(source.get("mhtOpenChannel").toString()));
			together.append(" cha where mer.MERCHANT_ID　=　cha.CHANNELSET_MERCHANT_ID)");
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		if(!list.isEmpty()){
			Map<String, Object> map = list.get(0);
			return Integer.parseInt(map.get("NUMBERR").toString());
		}
		return 0;
	}
	/**
	 * 
	 * @param source
	 * @return
	 */
	private String obtainChannelSql(String source){
		
		String[] arrayStr = source.split(",");
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(int t=0;t<arrayStr.length;t++){
			sb.append("'").append(arrayStr[t]).append("'");
			if(t< arrayStr.length-1){
				sb.append(",");
			}
		}
		sb.append(")");
		StringBuilder temStr = new StringBuilder();
		temStr.append("(select DISTINCT k.CHANNELSET_MERCHANT_ID from");
		temStr.append(" (select DISTINCT t.CHANNELSET_CHANNEL_CODE,t.CHANNELSET_MERCHANT_ID ");
		temStr.append(" from ALLPAY_CHANNELSET t where t.CHANNELSET_CHANNEL_CODE in ");
		temStr.append(sb).append(") k) ");
		return temStr.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> obtainMerchantStateFromDictionary(String key)
			throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select val.DATA_NAME,val.DATA_CODE from DATA_VALUE val inner join DATA_DICTIONARY dic");
		together.append("　on val.DATA_DICTIONARY = dic.DICTIONARY_ID where dic.DICTIONARY_CODE = '");
		together.append(key).append("'");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		Map<String,String> map = new HashMap<String, String>();
		if(!list.isEmpty()){
			for(Map<String, Object> mapTem : list){
				if(!CommonHelper.isNullOrEmpty(mapTem.get("DATA_CODE"))
						&& !CommonHelper.isNullOrEmpty(mapTem.get("DATA_NAME"))){
					map.put(mapTem.get("DATA_CODE").toString(), mapTem.get("DATA_NAME").toString());
				}
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	private String obtainConditionSQL(Map<String, Object> source){
		
		StringBuilder together = new StringBuilder();
		together.append(" where 1=1 AND mer.ALLPAY_LOGICDEL = '1' ");
		if(!CommonHelper.isNullOrEmpty(source.get("mhtStartCreateTime"))){
			together.append(" AND TO_CHAR(mer.ALLPAY_CREATETIME, 'YYYY-MM-DD HH24:MI:SS') >= '");
			together.append(source.get("mhtStartCreateTime")).append(" 00:00:00'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtEndCreateTime"))){
			together.append(" AND TO_CHAR(mer.ALLPAY_CREATETIME, 'YYYY-MM-DD HH24:MI:SS') <= '"); 
			together.append(source.get("mhtEndCreateTime")).append(" 23:59:59'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtName"))){
			together.append(" AND (mer.MERCHANT_MERCHNAME like '%").append(source.get("mhtName")).append("%'");
			together.append(" OR mer.MERCHANT_SHOPSHORTNAME like '%").append(source.get("mhtName")).append("%')");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("isStart"))){
			together.append(" AND mer.ALLPAY_ISSTART = ").append(source.get("isStart"));
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtIndustryNum"))){
			together.append(" AND mer.MERCHANT_INDUSTRYNUM = '").append(source.get("mhtIndustryNum")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtProvinceId"))){
			together.append(" AND mer.MERCHANT_PROVINCEID = '").append(source.get("mhtProvinceId")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtCityId"))){
			together.append(" AND mer.MERCHANT_CITYID = '").append(source.get("mhtCityId")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtBeanchCompId"))){
			together.append(" AND mer.MERCHANT_BRANCHCOMPANYID='").append(source.get("mhtBeanchCompId")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtAgentId"))){
			String userNameFromAgentCookie = CommonHelper.nullToString(source.get("userNameFromAgentCookie"));
			if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
				together.append(" AND (mer.MERCHANT_AGENTID='").append(source.get("mhtAgentId")).append("'");
				together.append(" OR age.AGENT_ID='").append(source.get("mhtAgentId")).append("' OR age.AGENT_LOCATION = '").append(source.get("mhtAgentId")).append("')");
			}else{
				together.append(" AND mer.MERCHANT_AGENTID='").append(source.get("mhtAgentId")).append("'");
			}
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtApproveStatus"))){//商户审核状态
			together.append(" AND mer.ALLPAY_STATE='").append(source.get("mhtApproveStatus")).append("'");
		}
		return together.toString();
	}
	/**
	 * 随机生成num位不重复的商户编号
	 * @param num
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getMerchaCode(int num){
		String result="";
		while (true) {
			result = CommonHelper.getRandomNum(num);
			String sql = "SELECT AM.MERCHANT_SHOPNUMBER FROM ALLPAY_MERCHANT AM WHERE AM.MERCHANT_SHOPNUMBER = '"+result + "'";
			int count = 0;
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

	/**
	 * 随机生成num位不重复的pos商户号
	 * @param num
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getPosMerCode(int num){
		String result="";
		while (true) {
			result = CommonHelper.getRandomNum(num);
			String sql = "SELECT T.TERMINAL_MERCHCODE FROM ALLPAY_TERMINAL T WHERE T.TERMINAL_MERCHCODE = '"+result + "'";
			int count = 0;
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

	/**
	 * 判断商户编号是否已存在
	 * @param merchaCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int checkMerchaCode(String merchaCode) throws Exception {
		String sql = "SELECT AM.MERCHANT_SHOPNUMBER FROM ALLPAY_MERCHANT AM WHERE AM.MERCHANT_SHOPNUMBER = '"+merchaCode + "' AND AM.ALLPAY_LOGICDEL = 1";
		int count = 0;
		List list = hibernateDAO.listBySQL(sql, false);
		if(null != list && list.size() >0){
			count = list.size();
		}
		return count;
	}

	/**
	 * 判断商户名称是否已存在
	 * 判断联系人手机号是否已存在
	 * @param merName
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<?> checkMerName(String merName, String phone) throws Exception {
		String sql = "SELECT AM.MERCHANT_ID, AM.MERCHANT_BRANCHCOMPANYID, AM.MERCHANT_MERCHNAME, AM.MERCHANT_SHOPNUMBER FROM ALLPAY_MERCHANT AM WHERE 1=1";
		if(null != merName){
			sql += " AND AM.MERCHANT_MERCHNAME = '"+merName+"'";
		}
		if(null != phone){
			sql += " AND AM.MERCHANT_CONTACTSPHONE = '"+phone+"'";
		}
		sql += "AND AM.ALLPAY_LOGICDEL = 1";
		List list = hibernateDAO.listBySQL(sql, false);
		return list;
	}

	/**
	 * 获取商户登录统计列表
	 * 导出商户登录统计列表
	 * @param source
	 * @param export
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> getMerchantLoginTotalList(Map<String, Object> source, boolean export) throws Exception {
		StringBuffer sql = new StringBuffer("SELECT M.MERCHANT_ID, M.MERCHANT_MERCHNAME, M.MERCHANT_SHOPSHORTNAME, A.AGENT_ID, A.AGENT_NAME, U.LAST_LOGIN_TIME, O.ORGANIZATION_ID,O.ORGANIZATION_NAME,");
		sql.append("U.SHOPUSER_NAME,U.SHOPUSER_NICKNAME,A.AGENT_LEVEL");
		sql.append(getSql(source));
		List<Map<String, Object>> list = null;
		if(export){
			list = (List<Map<String, Object>>)hibernateDAO.listBySQL(sql.toString(), false);
		}else{
			list = (List<Map<String, Object>>)hibernateDAO.listByPageBySQL(sql.toString(),Integer.parseInt(source.get("pageSize").toString()), Integer.parseInt(source.get("curragePage").toString())-1, false);
		}
		return list;
	}

	public String getSql(Map<String, Object> source){
		StringBuffer sql = new StringBuffer(" FROM ALLPAY_SHOPUSER U");
		sql.append(" INNER JOIN ALLPAY_MERCHANT M ON U.SHOPUSER_SHOPID = M.MERCHANT_ID AND M.ALLPAY_LOGICDEL = 1");
		sql.append(" INNER JOIN ALLPAY_AGENT A ON M.MERCHANT_AGENTID = A.AGENT_ID AND A.ALLPAY_LOGICDEL = 1");
		sql.append(" INNER JOIN ALLPAY_ORGANIZATION O ON M.MERCHANT_BRANCHCOMPANYID = O.ORGANIZATION_ID AND O.ALLPAY_LOGICDEL = 1 WHERE 1=1");
		if(!CommonHelper.isNullOrEmpty(source.get("mhtName"))){  //商户名称
			sql.append(" AND M.MERCHANT_MERCHNAME LIKE '%").append(source.get("mhtName")).append("%'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtBeanchCompId"))){  //所属分公司Id
			sql.append(" AND O.ORGANIZATION_ID = '").append(source.get("mhtBeanchCompId")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtAgentId"))){  //所属代理商Id
			sql.append(" AND A.AGENT_ID = '").append(source.get("mhtAgentId")).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtStartLoginTime"))){  //登录开始时间
			String start = source.get("mhtStartLoginTime").toString();
			if(start.length() <= 10){
				start += " 00:00:00";
			}
			sql.append(" AND U.LAST_LOGIN_TIME >= '").append(start).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(source.get("mhtEndLoginTime"))){  //登录结束时间
			String end = source.get("mhtEndLoginTime").toString();
			if(end.length() <= 10){
				end += " 23:59:59";
			}
			sql.append(" AND U.LAST_LOGIN_TIME <= '").append(end).append("'");
		}
		sql.append(" AND U.ALLPAY_LOGICDEL = 1 ORDER BY U.LAST_LOGIN_TIME DESC");
		return sql.toString();
	}

	@Override
	public int countLogin(Map<String, Object> source) throws Exception {
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS SUM");
		sql.append(getSql(source));
		return hibernateDAO.CountBySQL(sql.toString());
	}

}
