package com.cn.dao.impl;

import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.dao.ZfbKouBeiShopDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.po.Allpay_ChannelSet;
import com.cn.entity.po.KouBeiShop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by cuixiaowei on 2017/1/11.
 */
@Transactional
@Repository("ZfbKouBeiShopDao")
public class ZfbKouBeiShopDaoImpl implements ZfbKouBeiShopDao {
    @Autowired
    private HibernateDAO hibernateDAO;
    @Override
    public String insertZfbShop(KouBeiShop kouBeiShop) throws Exception
    {
        return  hibernateDAO.saveOrUpdate(kouBeiShop);
    }
    
    
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> obtainShopList(Object merchantName,Object branchId,Object storeState
    		,int currentPage,int pageSize)
			throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select mer.MERCHANT_MERCHNAME,mer.MERCHANT_SHOPSHORTNAME,sto.STORE_SHOPIDNUMBER");
		together.append(",sto.STORE_SHOPSHORTNAME,sho.KOUBEISHOP_MAINNAME,sto.STORE_ADDRESS,sho.KOUBEISHOP_KOUBEINUM");
		together.append(",sto.STORE_REPUTATIONID,sho.ALLPAY_STATE,sho.KOUBEISHOPE_ID,sho.KOUBEISHOP_KOUBEIID,mer.MERCHANT_ID,sto.STORE_ID");
		together.append(",cha.CHANNELSET_PARAMETER_VALUE,cha.CHANNELSET_CHANNEL_CODE ");
		together.append(obtainConditionSql(merchantName, branchId, storeState, true));
		together.append(" order by sho.ALLPAY_UPDATETIME desc");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listByPageBySQL(together.toString(), pageSize, 
						currentPage-1, false);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int obtainShopNumber(Object merchantName,Object branchId,Object storeState)
			throws Exception {
		
		StringBuilder together = new StringBuilder(); 
		together.append("select count(*) numberr ")
		.append(obtainConditionSql(merchantName, branchId, storeState, true));
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		if(!list.isEmpty()){
			return Integer.parseInt(list.get(0).get("NUMBERR").toString());
		}
		return 0;
	}
	/**
	 * 
	 * @param merchantName
	 * @param branchId
	 * @param storeState
	 * @return
	 */
	private  String obtainConditionSql(Object merchantName,Object branchId,Object storeState,boolean append){
		
		StringBuilder together = new StringBuilder();
		together.append(" from ALLPAY_STORE sto left join KOUBEISHOP sho on sho.KOUBEISHOP_STORE_ID = sto.STORE_ID");
		together.append(" left join ALLPAY_MERCHANT mer on sto.STORE_MERCHANT_ID = mer.MERCHANT_ID");
		if(append){
			together.append(" left join ALLPAY_CHANNELSET cha on sto.STORE_MERCHANT_ID = cha.CHANNELSET_MERCHANT_ID");
		}
		together.append(" where 1=1 AND mer.ALLPAY_LOGICDEL = '1' AND sto.ALLPAY_LOGICDEL = '1'");
		if(append){
			together.append(" AND (cha.CHANNELSET_CHANNEL_CODE = '142' or cha.CHANNELSET_CHANNEL_CODE = '147')");
			together.append(" AND cha.CHANNELSET_PARAMETER_NAME = 'token'");
		}
		if(!CommonHelper.isNullOrEmpty(merchantName)){
			together.append(" AND mer.MERCHANT_MERCHNAME like '%").append(merchantName).append("%'");
		}
		if(!CommonHelper.isNullOrEmpty(branchId)){
			together.append(" AND mer.MERCHANT_BRANCHCOMPANYID = '").append(branchId).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(storeState)){
			together.append(" AND sho.ALLPAY_STATE = '").append(storeState).append("'");
		}
		return together.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> obtainShop(String shopId) {
		
		StringBuilder together = new StringBuilder();
		together.append("select sho.*,sto.STORE_SHOPIDNUMBER,cha.CHANNELSET_CHANNEL_CODE from KOUBEISHOP sho ");
		together.append(" left join ALLPAY_STORE sto on sho.KOUBEISHOP_STORE_ID = sto.STORE_ID");
		together.append(" left join ALLPAY_CHANNELSET cha on cha.CHANNELSET_MERCHANT_ID = sto.STORE_MERCHANT_ID");
		together.append(" where sho.KOUBEISHOPE_ID = '").append(shopId).append("'");
		together.append(" AND (cha.CHANNELSET_CHANNEL_CODE = '142' or cha.CHANNELSET_CHANNEL_CODE = '147')");
		together.append(" AND cha.CHANNELSET_PARAMETER_NAME = 'token'");
		together.append(" AND sto.ALLPAY_LOGICDEL = '1'");
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}


	@Override
	public String update(KouBeiShop shop) throws Exception {
		hibernateDAO.update(shop);
		return "success";
	}


	@Override
	public String updateStoreNumber(String storeId,String storeNum) throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("update ALLPAY_STORE sto set sto.STORE_SHOPIDNUMBER = '");
		together.append(storeNum).append("' where sto.STORE_ID = '").append(storeId).append("'");
		hibernateDAO.executeBySql(together.toString());
		return "success";
	}


	@Override
	public KouBeiShop obtainForId(String shopId) throws Exception {
		KouBeiShop shop = hibernateDAO.listById(KouBeiShop.class, shopId, false);
		return shop;
	}

	@Override
	public List<Map<String, Object>> getKouBeiByPID(String shopPID) throws Exception
	{

		String sql="select * from ALLPAY_CHANNELSET channelset where channelset.CHANNELSET_PARAMETER_VALUE='"+shopPID+"'" +
				" and channelset.CHANNELSET_PARAMETER_NAME='shopPID'";
		LogHelper.info("sql语句：" + sql);
		return (List<Map<String, Object>>) hibernateDAO.listBySQL(sql, false);
	}

	@Override
	public List<Allpay_ChannelSet> getKouBeiShopIDandToken(String shopid,String code,String marked) throws Exception
	{

		String sql="";
		if("noCode".equals(code))
		{
			 sql="from Allpay_ChannelSet channelset where channelset.channelset_merchant_id='"+shopid+"'"+
					" and channelset.channelset_channel_code in ('142','147') and channelset.channelset_parameter_name='"+marked+"'";
		}
		else
		{
			sql="from Allpay_ChannelSet channelset where channelset.channelset_merchant_id='"+shopid+"'"+
					" and channelset.channelset_channel_code='"+code+"' and channelset.channelset_parameter_name='"+marked+"'";
		}
		LogHelper.info("HQL语句：" + sql);
		return (List<Allpay_ChannelSet>) hibernateDAO.listByHQL(sql);
	}

	@Override
	public List<KouBeiShop> getKouBeiByStoreID(String storeId) throws Exception {
		String sql="from KouBeiShop shop where shop.koubeishop_store_id='"+storeId+"'";
		LogHelper.info("HQL语句：" + sql);
		return (List<KouBeiShop>) hibernateDAO.listByHQL(sql);
	}


}









