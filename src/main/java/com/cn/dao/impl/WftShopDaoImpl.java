package com.cn.dao.impl;

import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.dao.WftShopDao;
import com.cn.dao.util.HibernateDAO;

import com.cn.entity.po.Wft_PayType;
import com.cn.entity.po.Wft_Shop;

import com.cn.entity.po.Wft_Shop;


import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cn.common.CommonHelper.formatTime;

/**
 * Created by cuixiaowei on 2017/1/11.
 */
@Transactional
@Repository("WftShopDao")
public class WftShopDaoImpl implements WftShopDao {

    @Autowired
    private HibernateDAO hibernateDAO;
    @Override
    public List<Map<String, Object>> getWftShopList(Map<String, Object> source,String mark) throws Exception
    {
        StringBuilder sql=new StringBuilder();
        sql.append("select paymain.*,pay.payName from ( ");
        sql.append("SELECT rownum linenum, WFT.WFT_SHOP_ID,WFT.WFT_SHOP_OUTMERCHANTID,WFT.WFT_SHOP_MERCHANTID, WFT.WFT_SHOP_NAME,WFT.WFT_SHOP_STATE,WFT.WFT_SHOP_BANKTYPE,WFT.WFT_SHOP_WXBILL,WFT.WFT_SHOP_ZFBBILL," +
                "WFT.ALLPAY_CREATER,WFT.ALLPAY_ISSTART,WFT.ALLPAY_CREATETIME,TO_CHAR(WFT.WFT_SHOP_BACKDATE,'YYYY-MM-DD HH24:MI:SS') WFT_SHOP_BACKDATE,WFT.WFT_SHOP_BACKMESS," +
                "WFT.WFT_SHOP_INDUSTRYID,WFT.WFT_SHOP_BANKACCOUNTNAME,WFT.WFT_SHOP_BANKID,WFT.WFT_SHOP_BANKACCOUNTTYPE,WFT.WFT_SHOP_BANKIDCARDTYPE,"+
                "WFT.WFT_SHOP_BANKCODEID,WFT.WFT_SHOP_BANKCODENAME,TO_CHAR(WFT.ALLPAY_UPDATETIME,'YYYY-MM-DD HH24:MI:SS') ALLPAY_UPDATETIME,"+
                "BANK.BANKNAME,WFT.ALLPAY_UPDATER,CITYCODE.AREANAME CITYNAME,PROVICECODE.AREANAME PROVICENAME,INDU.INDUSTRYNAME, "+
                "MERCHANT.MERCHANT_MERCHNAME,MERCHANT.MERCHANT_BRANCHCOMPANYID,AGENT.AGENT_ID,AGENT.AGENT_NAME,ORGANIZATION.ORGANIZATION_NAME,WFT.WFT_SHOP_SECRETKEY,WFT.WFT_SHOP_APPID ");


        /*sql.append("  (case\n" +
                        "         when (select count(1)\n" +
                        "                 from WFT_PAYTYPE pay\n" +
                        "                where pay.WFT_PAYTYPE_WFTSHOP_ID = WFT.wft_shop_id\n" +
                        "                  and  (pay.WFT_PAYTYPE_BACKSTATE is null or pay.WFT_PAYTYPE_BACKSTATE = '' or pay.WFT_PAYTYPE_BACKSTATE = '0')) = 0 then\n" +
                        "          '完成'\n" +
                        "         else\n" +
                        "          '未完成'\n" +
                        "       end) payName\n");*/



        sql.append(" FROM WFT_SHOP WFT LEFT JOIN WFTBANKID BANK ON WFT.WFT_SHOP_BANKID=BANK.BANKCODE LEFT JOIN WFTAREACODE CITYCODE ON CITYCODE.areacode=WFT.WFT_SHOP_CITYID " +
                "LEFT JOIN WFTAREACODE PROVICECODE ON PROVICECODE.areacode=WFT.WFT_SHOP_PROVINCEID LEFT JOIN WFTINDUSTRYCODE INDU ON INDU.INDUSTRYCODE=WFT.WFT_SHOP_INDUSTRYID " +
                "LEFT JOIN ALLPAY_MERCHANT MERCHANT ON MERCHANT.MERCHANT_ID=WFT.WFT_SHOP_OUTMERCHANTID " +
                "LEFT JOIN ALLPAY_AGENT AGENT ON MERCHANT.MERCHANT_AGENTID=AGENT.AGENT_ID LEFT JOIN ALLPAY_ORGANIZATION ORGANIZATION ON ORGANIZATION.ORGANIZATION_ID=MERCHANT.MERCHANT_BRANCHCOMPANYID WHERE 1=1 " +
                "and WFT.ALLPAY_LOGICDEL='1' ");
        if(!CommonHelper.isNullOrEmpty(source.get("shopName")))
        {
            sql.append(" AND WFT.WFT_SHOP_NAME like'%"+source.get("shopName")+"%'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("wftIndustry")))
        {
            sql.append(" AND WFT.WFT_SHOP_INDUSTRYID='"+source.get("wftIndustry")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("wftBank")))
        {
            sql.append(" AND WFT.WFT_SHOP_BANKTYPE='"+source.get("wftBank")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("province")))
        {
            sql.append(" AND WFT.WFT_SHOP_PROVINCEID='"+source.get("province")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("city")))
        {
            sql.append(" AND WFT.WFT_SHOP_CITYID='"+source.get("city")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("startCreateTime")))
        {
            String startCreateTime=String.valueOf(source.get("startCreateTime"))+" 00:00:00";
            sql.append(" AND WFT.ALLPAY_CREATETIME >= to_date('"+startCreateTime+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("endCreateTime")))
        {
            String endCreateTime=String.valueOf(source.get("endCreateTime"))+" 23:59:59";
            sql.append(" AND WFT.ALLPAY_CREATETIME <= to_date('"+endCreateTime+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("shopCode")))
        {
            sql.append(" AND WFT.WFT_SHOP_MERCHANTID='"+source.get("shopCode")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("wftBranch")))
        {
            sql.append(" AND MERCHANT.MERCHANT_BRANCHCOMPANYID='"+source.get("wftBranch")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("wftAgent")))
        {
            sql.append(" AND MERCHANT.MERCHANT_AGENTID='"+source.get("wftAgent")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("sCodeBackStartTime")))
        {
            String sCodeBackStartTime=String.valueOf(source.get("sCodeBackStartTime"))+" 00:00:00";
            sql.append(" AND WFT.WFT_SHOP_BACKDATE >= to_date('"+sCodeBackStartTime+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("sCodeBackEndTime")))
        {
            String sCodeBackEndTime=String.valueOf(source.get("sCodeBackEndTime"))+" 23:59:59";
            sql.append(" AND WFT.WFT_SHOP_BACKDATE <= to_date('"+sCodeBackEndTime+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("isState")))
        {
            sql.append(" AND WFT.WFT_SHOP_STATE='"+source.get("isState")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("createName")))
        {
            sql.append(" AND WFT.ALLPAY_CREATER like'%"+source.get("createName")+"%'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("isHaveShopCode")))
        {
            if("1".equals(source.get("isHaveShopCode").toString()))
            {//无商户号
                sql.append(" AND (WFT.WFT_SHOP_MERCHANTID IS NULL OR WFT.WFT_SHOP_MERCHANTID='')");
            }
            if("0".equals(source.get("isHaveShopCode").toString()))
            {
                sql.append(" AND WFT.WFT_SHOP_MERCHANTID IS NOT NULL ");
            }


        }

        if(!CommonHelper.isNullOrEmpty(source.get("merchantId")))
        {
            sql.append(" AND WFT.WFT_SHOP_OUTMERCHANTID ='"+source.get("merchantId")+"'");
        }

        sql.append(" order by WFT.ALLPAY_UPDATETIME desc, linenum desc");

        sql.append(" ) paymain ");
        sql.append("left join （select WFT_PAYTYPE_WFTSHOP_ID,decode(sum(WFT_PAYTYPE_BACKSTATE),6,'完成','未完成') payName\n" +
                "from WFT_PAYTYPE pay group by WFT_PAYTYPE_WFTSHOP_ID） pay on paymain.wft_shop_id=pay.WFT_PAYTYPE_WFTSHOP_ID");

        if(!CommonHelper.isNullOrEmpty(source.get("isZFTypeEx")))
        {
            if("1".equals(String.valueOf(source.get("isZFTypeEx"))))
            {
                sql.append(" where pay.payName='未完成'");
            }
            if("0".equals(String.valueOf(source.get("isZFTypeEx"))))
            {
                sql.append(" where pay.payName='完成'");
            }
            if("2".equals(String.valueOf(source.get("isZFTypeEx"))))
            {
                sql.append(" where pay.payName='完成' or pay.payName is null ");
            }

        }
        LogHelper.info("sql语句："+sql.toString());
        List<Map<String, Object>> wtfList=null;
        if("count".equals(mark))
        {
            wtfList=(List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(), false);
        }
        if("pageCurrage".equals(mark))
        {
             wtfList= (List<Map<String, Object>>) hibernateDAO.listByPageBySQL(sql.toString(),Integer.parseInt(String.valueOf(source.get("pageSize"))),
                    Integer.parseInt(String.valueOf(source.get("curragePage")))-1,false);
        }
        if("exportExcel".equals(mark))
        {
            wtfList=(List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(), false);
        }

        return wtfList;
    }









    @Override
    public int getWftShopListCount(Map<String, Object> source, String mark) throws Exception {

        StringBuilder sql=new StringBuilder();
        sql.append("SELECT count(*) SUM from ( ");

        //sql.append("select paymain.*,pay.payName from ( ");
        sql.append("SELECT rownum linenum, WFT.WFT_SHOP_ID,WFT.WFT_SHOP_OUTMERCHANTID,WFT.WFT_SHOP_MERCHANTID, WFT.WFT_SHOP_NAME,WFT.WFT_SHOP_STATE,WFT.WFT_SHOP_BANKTYPE,WFT.WFT_SHOP_WXBILL,WFT.WFT_SHOP_ZFBBILL," +
                "WFT.ALLPAY_CREATER,WFT.ALLPAY_ISSTART,WFT.ALLPAY_CREATETIME,TO_CHAR(WFT.WFT_SHOP_BACKDATE,'YYYY-MM-DD HH24:MI:SS') WFT_SHOP_BACKDATE,WFT.WFT_SHOP_BACKMESS," +
                "WFT.WFT_SHOP_INDUSTRYID,WFT.WFT_SHOP_BANKACCOUNTNAME,WFT.WFT_SHOP_BANKID,WFT.WFT_SHOP_BANKACCOUNTTYPE,WFT.WFT_SHOP_BANKIDCARDTYPE,"+
                "WFT.WFT_SHOP_BANKCODEID,WFT.WFT_SHOP_BANKCODENAME,TO_CHAR(WFT.ALLPAY_UPDATETIME,'YYYY-MM-DD HH24:MI:SS') ALLPAY_UPDATETIME,"+
                "BANK.BANKNAME,WFT.ALLPAY_UPDATER,CITYCODE.AREANAME CITYNAME,PROVICECODE.AREANAME PROVICENAME,INDU.INDUSTRYNAME, "+
                "MERCHANT.MERCHANT_MERCHNAME,MERCHANT.MERCHANT_BRANCHCOMPANYID,AGENT.AGENT_ID,AGENT.AGENT_NAME,ORGANIZATION.ORGANIZATION_NAME ");


        /*sql.append("  (case\n" +
                        "         when (select count(1)\n" +
                        "                 from WFT_PAYTYPE pay\n" +
                        "                where pay.WFT_PAYTYPE_WFTSHOP_ID = WFT.wft_shop_id\n" +
                        "                  and  (pay.WFT_PAYTYPE_BACKSTATE is null or pay.WFT_PAYTYPE_BACKSTATE = '' or pay.WFT_PAYTYPE_BACKSTATE = '0')) = 0 then\n" +
                        "          '完成'\n" +
                        "         else\n" +
                        "          '未完成'\n" +
                        "       end) payName\n");*/



        sql.append(" FROM WFT_SHOP WFT LEFT JOIN WFTBANKID BANK ON WFT.WFT_SHOP_BANKID=BANK.BANKCODE LEFT JOIN WFTAREACODE CITYCODE ON CITYCODE.areacode=WFT.WFT_SHOP_CITYID " +
                "LEFT JOIN WFTAREACODE PROVICECODE ON PROVICECODE.areacode=WFT.WFT_SHOP_PROVINCEID LEFT JOIN WFTINDUSTRYCODE INDU ON INDU.INDUSTRYCODE=WFT.WFT_SHOP_INDUSTRYID " +
                "LEFT JOIN ALLPAY_MERCHANT MERCHANT ON MERCHANT.MERCHANT_ID=WFT.WFT_SHOP_OUTMERCHANTID " +
                "LEFT JOIN ALLPAY_AGENT AGENT ON MERCHANT.MERCHANT_AGENTID=AGENT.AGENT_ID LEFT JOIN ALLPAY_ORGANIZATION ORGANIZATION ON ORGANIZATION.ORGANIZATION_ID=MERCHANT.MERCHANT_BRANCHCOMPANYID WHERE 1=1 " +
                "and WFT.ALLPAY_LOGICDEL='1' ");
        if(!CommonHelper.isNullOrEmpty(source.get("shopName")))
        {
            sql.append(" AND WFT.WFT_SHOP_NAME like'%"+source.get("shopName")+"%'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("wftIndustry")))
        {
            sql.append(" AND WFT.WFT_SHOP_INDUSTRYID='"+source.get("wftIndustry")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("wftBank")))
        {
            sql.append(" AND WFT.WFT_SHOP_BANKTYPE='"+source.get("wftBank")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("province")))
        {
            sql.append(" AND WFT.WFT_SHOP_PROVINCEID='"+source.get("province")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("city")))
        {
            sql.append(" AND WFT.WFT_SHOP_CITYID='"+source.get("city")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("startCreateTime")))
        {
            String startCreateTime=String.valueOf(source.get("startCreateTime"))+" 00:00:00";
            sql.append(" AND WFT.ALLPAY_CREATETIME >= to_date('"+startCreateTime+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("endCreateTime")))
        {
            String endCreateTime=String.valueOf(source.get("endCreateTime"))+" 23:59:59";
            sql.append(" AND WFT.ALLPAY_CREATETIME <= to_date('"+endCreateTime+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("shopCode")))
        {
            sql.append(" AND WFT.WFT_SHOP_MERCHANTID='"+source.get("shopCode")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("wftBranch")))
        {
            sql.append(" AND MERCHANT.MERCHANT_BRANCHCOMPANYID='"+source.get("wftBranch")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("wftAgent")))
        {
            sql.append(" AND MERCHANT.MERCHANT_AGENTID='"+source.get("wftAgent")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("sCodeBackStartTime")))
        {
            String sCodeBackStartTime=String.valueOf(source.get("sCodeBackStartTime"))+" 00:00:00";
            sql.append(" AND WFT.WFT_SHOP_BACKDATE >= to_date('"+sCodeBackStartTime+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("sCodeBackEndTime")))
        {
            String sCodeBackEndTime=String.valueOf(source.get("sCodeBackEndTime"))+" 23:59:59";
            sql.append(" AND WFT.WFT_SHOP_BACKDATE <= to_date('"+sCodeBackEndTime+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("isState")))
        {
            sql.append(" AND WFT.WFT_SHOP_STATE='"+source.get("isState")+"'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("createName")))
        {
            sql.append(" AND WFT.ALLPAY_CREATER like'%"+source.get("createName")+"%'");
        }
        if(!CommonHelper.isNullOrEmpty(source.get("isHaveShopCode")))
        {
            if("1".equals(source.get("isHaveShopCode").toString()))
            {//无商户号
                sql.append(" AND (WFT.WFT_SHOP_MERCHANTID IS NULL OR WFT.WFT_SHOP_MERCHANTID='')");
            }
            if("0".equals(source.get("isHaveShopCode").toString()))
            {
                sql.append(" AND WFT.WFT_SHOP_MERCHANTID IS NOT NULL ");
            }


        }

        if(!CommonHelper.isNullOrEmpty(source.get("merchantId")))
        {
            sql.append(" AND WFT.WFT_SHOP_OUTMERCHANTID ='"+source.get("merchantId")+"'");
        }

        sql.append(" order by WFT.ALLPAY_UPDATETIME desc, linenum desc");

        sql.append(" ) paymain ");
        sql.append("left join （select WFT_PAYTYPE_WFTSHOP_ID,decode(sum(WFT_PAYTYPE_BACKSTATE),6,'完成','未完成') payName\n" +
                "from WFT_PAYTYPE pay group by WFT_PAYTYPE_WFTSHOP_ID） pay on paymain.wft_shop_id=pay.WFT_PAYTYPE_WFTSHOP_ID");

        if(!CommonHelper.isNullOrEmpty(source.get("isZFTypeEx")))
        {
            if("1".equals(String.valueOf(source.get("isZFTypeEx"))))
            {
                sql.append(" where pay.payName='未完成'");
            }
            if("0".equals(String.valueOf(source.get("isZFTypeEx"))))
            {
                sql.append(" where pay.payName='完成'");
            }

        }
        return hibernateDAO.CountBySQL(sql.toString());

    }


    @Override
    public String insertWftShopInfo(Wft_Shop wftShop) throws Exception {

        return hibernateDAO.saveOrUpdate(wftShop);
    }

    @Override
    public Wft_Shop checkWftShopInfoByParamters(String filds, String paramters) throws Exception {
        return hibernateDAO.listByField(Wft_Shop.class, filds, paramters);
    }

    @Override
    public List<Wft_Shop> checkWftShopInfoByParamters(String paramters) throws Exception
    {
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("wft_shop_outmerchantid",paramters));
        conj.add(Restrictions.eq("ALLPAY_LOGICDEL", "1"));
        return hibernateDAO.listByCriteria(Wft_Shop.class, conj, false);
    }

    @Override
    public String insertWftPayTypeInfo(Wft_PayType wftPayType) throws Exception
    {
        return hibernateDAO.saveOrUpdate(wftPayType);
    }

    @Override
    public Wft_Shop getWftShopInfoByID(String id) throws Exception {
        return hibernateDAO.listById(Wft_Shop.class, id, false);
    }

    @Override
    public List<Wft_PayType> getWftPayTypeByParamters(String wftid) throws Exception
    {
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("wft_paytype_wftshop_id",wftid));
        return hibernateDAO.listByCriteria(Wft_PayType.class, conj, false);
    }

    @Override
    public Wft_PayType getWft_PayTypeByID(String ID) throws Exception {
        return hibernateDAO.listById(Wft_PayType.class, ID, false);
    }


    @Override
	public String updateStatus(String wftId,String status) throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("update WFT_SHOP shop set shop.WFT_SHOP_STATE = '").append(status);
		together.append("' where shop.WFT_SHOP_ID = '").append(wftId).append("'");
        LogHelper.info("sql语句：" + together.toString());
		hibernateDAO.executeBySql(together.toString());
		return "success";
	}

    
	@Override
	public String updateBill(String wftId, Object wftWXBill, Object wftZFBBill)
			throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("update WFT_SHOP shop set");
		if(!CommonHelper.isNullOrEmpty(wftWXBill)){
			together.append(" shop.WFT_SHOP_WXBILL = '").append(wftWXBill).append("'");
			if(!CommonHelper.isNullOrEmpty(wftZFBBill)){
				together.append(",");
				together.append(" shop.WFT_SHOP_ZFBBILL = '").append(wftZFBBill).append("'");
			}
		}else{
			together.append(" shop.WFT_SHOP_ZFBBILL = '").append(wftZFBBill).append("'");
		}
        together.append(",shop.ALLPAY_UPDATETIME=to_date('").append(formatTime(new Date(),"yyyy-MM-dd HH:mm:ss"))
                .append("','YYYY-MM-DD HH24:MI:SS') ");
		together.append(" where shop.WFT_SHOP_ID = '").append(wftId).append("'");
        LogHelper.info("sql语句：" + together.toString());
		hibernateDAO.executeBySql(together.toString());
		return "success";
	}


	@Override
	public Wft_Shop obtainById(String wftId) throws Exception {
		
		Wft_Shop shop = hibernateDAO.listById(Wft_Shop.class, wftId, false);
		return shop;
	}


	@Override
	public String update(Wft_Shop shop) throws Exception {
		
		hibernateDAO.update(shop);
		return "success";
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> obtainPayType(String wftId)
			throws Exception {
        String sql="";
		if("".equals(wftId))
        {
             sql="select * from WFT_PAYTYPE pay where pay.WFT_PAYTYPE_BACKSTATE is null or pay.WFT_PAYTYPE_BACKSTATE = '' or pay.WFT_PAYTYPE_BACKSTATE = '0'";
        }
        else
        {
             sql = "select * from WFT_PAYTYPE ty where ty.WFT_PAYTYPE_WFTSHOP_ID = '"+wftId+"'";
        }

        LogHelper.info("sql语句：" + sql.toString());
	    List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(sql, false);
		return list;
	}




	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> obtainBankCodeList(int currentPage,
			int pageSize, Object bankCode, Object bankName) throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select * from WFT_BANKCODE co where 1=1 ");
		if(!CommonHelper.isNullOrEmpty(bankCode)){
			together.append(" AND co.WFT_BANKCODE_BANKCODE like '%").append(bankCode).append("%'");
		}
		if(!CommonHelper.isNullOrEmpty(bankName)){
			together.append(" AND co.WFT_BANKCODE_BANKNAME like '%").append(bankName).append("%'");
		}
        LogHelper.info("sql语句："+together.toString());
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listByPageBySQL(together.toString(), pageSize, currentPage-1, false);
		return list;
	}


	@SuppressWarnings("unchecked")
	@Override
	public int obtainBankCodeCount(Object bankCode, Object bankName)
			throws Exception {
		
		
		StringBuilder together = new StringBuilder();
		together.append("select count(*) SUM from WFT_BANKCODE co where 1=1 ");
		if(!CommonHelper.isNullOrEmpty(bankCode)){
			together.append(" AND co.WFT_BANKCODE_BANKCODE like '%").append(bankCode).append("%'");
		}
		if(!CommonHelper.isNullOrEmpty(bankName)){
			together.append(" AND co.WFT_BANKCODE_BANKNAME like '%").append(bankName).append("%'");
		}
        LogHelper.info("sql语句："+together.toString());
		int list =
					hibernateDAO.CountBySQL(together.toString());
		/*if(!list.isEmpty()){
			//Map<String, Object> map = list.get(0);
			return list.size();
		}*/
		return list;
	}


	@Override
	public String insert(Wft_Shop shop) throws Exception {
		hibernateDAO.save(shop);
		return "success";
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> obtainShopDetailMSg(String wftId)
			throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select shop.*,mer.MERCHANT_MERCHNAME,mer.MERCHANT_INDUSTRYNUM");
		together.append(",mer.MERCHANT_CITYID,mer.MERCHANT_PROVINCEID");
		together.append(" from WFT_SHOP shop left join ALLPAY_MERCHANT mer ");
		together.append(" on shop.WFT_SHOP_OUTMERCHANTID = mer.MERCHANT_ID");
		together.append(" where shop.WFT_SHOP_ID = '").append(wftId).append("'");
        LogHelper.info("sql语句：" + together.toString());
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listBySQL(together.toString(), false);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
    

}













