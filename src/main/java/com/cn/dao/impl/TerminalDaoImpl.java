package com.cn.dao.impl;

import com.cn.common.CommonHelper;
import com.cn.dao.TerminalDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.dto.AllpayTerminalDto;
import com.cn.entity.po.Allpay_Terminal;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.dsig.CanonicalizationMethod;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

/**
 * pos终端管理dao层实现
 * Created by WangWenFang on 2016/12/5.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Repository("terminalDao")
public class TerminalDaoImpl implements TerminalDao {

    @Autowired
    private HibernateDAO hibernateDAO;

    public Allpay_Terminal getTerminal(String posid) {
        Session session= hibernateDAO.sessionFactory.getCurrentSession();
        return (Allpay_Terminal) session.get(Allpay_Terminal.class,posid);
    }

    /**
     * 查询pos终端列表
     * @param allpayTerminalDto
     * @param currentPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> obtainList(AllpayTerminalDto allpayTerminalDto, Integer currentPage, Integer pageSize, boolean isExport) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT T.TERMINAL_POSID, ROWNUM R, T.TERMINAL_STORE_ID, S.STORE_SHOPNAME, S.STORE_SHOPSHORTNAME, T.TERMINAL_MERCHANT_ID, ");
        sql.append("T.TERMINAL_TERMCODE, T.TERMINAL_BRANCHID, AO.ORGANIZATION_NAME, T.TERMINAL_PEIJIANPOSPARTERID, AP.POSPARTER_PARTERALLNAME, AM.MERCHANT_MERCHNAME, ")
                .append("T.TERMINAL_SUPERTERMCODE, T.TERMINAL_PAYCHANNEL, T.ALLPAY_ISSTART, T.TERMINAL_POSTYPE, T.ALLPAY_CREATETIME, T.TERMINAL_MERCHCODE,")
                .append(" SUPT.TERMINAL_MERCHCODE AS SUP_MERCHCODE, SUPT.TERMINAL_TERMCODE AS SUP_TERMCODE, T.TERMINAL_POSPARTERID, AP_SUP.POSPARTER_PARTERALLNAME AS SUP_PARTERALLNAME")
                .append(getSql(allpayTerminalDto, true));
        List<Map<String, Object>> list = null;
        if(isExport){  //导出
            list = (List<Map<String, Object>>)hibernateDAO.listBySQL(sql.toString(), false);
        }else{  //查询
            list = (List<Map<String, Object>>)hibernateDAO.listByPageBySQL(sql.toString(), pageSize, currentPage, false);
        }
        return list;
    }

    /**
     * 查询pos终端记录条数
     * @param allpayTerminalDto
     * @return
     * @throws Exception
     */
    public int count(AllpayTerminalDto allpayTerminalDto) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT COUNT(T.TERMINAL_POSID) AS SUM ");
        sql.append(getSql(allpayTerminalDto, false));
        return hibernateDAO.CountBySQL(sql.toString());
    }

    /**
     * 新增pos终端信息
     * @param allpayTerminal
     * @return
     * @throws Exception
     */
    public boolean insert(Allpay_Terminal allpayTerminal) throws Exception {
        String result =  hibernateDAO.save(allpayTerminal);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    /**
     * 修改pos终端信息
     * @param allpayTerminal
     * @return
     * @throws Exception
     */
    public boolean update(Allpay_Terminal allpayTerminal) throws Exception {
        String result =  hibernateDAO.saveOrUpdate(allpayTerminal);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    /**
     * 终端菜单配置
     * @param allpayTerminal
     * @return
     * @throws Exception
     */
    @Override
    public boolean insertPosMenuSetInfo(Allpay_Terminal allpayTerminal, String posType) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE ALLPAY_TERMINAL ATE SET ATE.TERMINAL_PERMISSION = '").append(allpayTerminal.getTerminal_permission()).append("',");
        sql.append(" ATE.ALLPAY_UPDATETIME = TO_DATE('").append(CommonHelper.formatTime(allpayTerminal.getALLPAY_UPDATETIME(), "yyyy-MM-dd HH:mm:ss")).append("', 'YYYY-MM-DD HH24:MI:SS'),")
                .append(" ATE.ALLPAY_UPDATER = '").append(allpayTerminal.getALLPAY_UPDATER()).append("',")
                .append(" ATE.ALLPAY_LOGRECORD  = '").append(allpayTerminal.getALLPAY_LOGRECORD()).append("' WHERE 1=1");
        if(!CommonHelper.isEmpty(allpayTerminal.getTerminal_merchant_id())){
            sql.append(" AND ATE.TERMINAL_MERCHANT_ID = '").append(allpayTerminal.getTerminal_merchant_id()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminal.getTerminal_store_id())){
            sql.append(" AND ATE.TERMINAL_STORE_ID = '").append(allpayTerminal.getTerminal_store_id()).append("'");
        }
        if(!CommonHelper.isEmpty(posType)){
            sql.append(" AND ATE.TERMINAL_POSTYPE IN (").append(posType).append(")");
        }
        sql.append(" AND ATE.ALLPAY_LOGICDEL = 1");
        int result = hibernateDAO.saveOrUpdateBySQL(sql.toString());
        if(result > 0){
            return true;
        }
        return false;
    }

    /**
     * 获取终端菜单配置
     * @param allpayTerminalDto
     * @return
     * @throws Exception
     */
    @Override
    public List<?> getPosMenuSetInfo(AllpayTerminalDto allpayTerminalDto) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT ATE.TERMINAL_PERMISSION, ATE.TERMINAL_MERCHANT_ID, ATE.TERMINAL_STORE_ID, ATE.TERMINAL_POSTYPE, AST.STORE_SHOPNAME");
        sql.append(" FROM ALLPAY_TERMINAL ATE LEFT JOIN ALLPAY_STORE AST ON ATE.TERMINAL_STORE_ID = AST.STORE_ID AND AST.ALLPAY_LOGICDEL = 1 WHERE 1=1");
        if(!CommonHelper.isEmpty(allpayTerminalDto.getMerchantId())){
            sql.append(" AND ATE.TERMINAL_MERCHANT_ID = '").append(allpayTerminalDto.getMerchantId()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getStoreId())){
            sql.append(" AND ATE.TERMINAL_STORE_ID = '").append(allpayTerminalDto.getStoreId()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getPosType())){
            sql.append(" AND ATE.TERMINAL_POSTYPE in (").append(allpayTerminalDto.getPosType()).append(")");
        }
        sql.append(" AND ATE.ALLPAY_LOGICDEL = 1");
        List<?> list = hibernateDAO.listBySQL(sql.toString(), false);
        return list;
    }

    @Override
    public List<?> getUserSoftPos(String userId) throws Exception {
        String sql = "SELECT T.TERMINAL_POSID FROM ALLPAY_TERMINAL T WHERE T.TERMINAL_SHOPUSERID = '"+userId+"' AND T.ALLPAY_LOGICDEL = 1";
        return hibernateDAO.listBySQL(sql, false);
    }

    /**
     * 根据id查询pos终端信息
     * @param clazz
     * @param posId
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T getById(Class<T> clazz, String posId) throws Exception {
        return hibernateDAO.listById(clazz, posId, false);
    }

    /**
     * 根据商户id查询商户名称
     * @param merId
     * @return
     * @throws Exception
     */
    @Override
    public List<?> getMerNameBySql(String merId) throws Exception {
        String sql = "SELECT AM.MERCHANT_MERCHNAME FROM ALLPAY_MERCHANT AM WHERE AM.MERCHANT_ID = '"+merId+"'  AND AM.ALLPAY_LOGICDEL = 1";
        return hibernateDAO.listBySQL(sql, false);
    }

    /**
     * 根据商户id、门店id、pos类型查询pos终端信息
     * 根据pos商户号、终端号查询pos终端信息
     * @param allpayTerminalDto
     * @return
     * @throws Exception
     */
    @Override
    public List<?> getTerminalBySql(AllpayTerminalDto allpayTerminalDto) throws Exception {
        StringBuffer sql = new StringBuffer("FROM Allpay_Terminal T WHERE 1=1");
        if(!CommonHelper.isEmpty(allpayTerminalDto.getMerchantId())){
            sql.append(" AND T.terminal_merchant_id = '").append(allpayTerminalDto.getMerchantId()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getStoreId())){
            sql.append(" AND T.terminal_store_id = '").append(allpayTerminalDto.getStoreId()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getPosType())){
            sql.append(" AND T.terminal_postype in (").append(allpayTerminalDto.getPosType()).append(")");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getMerchaCode())){
            sql.append(" AND T.terminal_merchcode = '").append(allpayTerminalDto.getMerchaCode()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getTermCode())){
            sql.append(" AND T.terminal_termcode = '").append(allpayTerminalDto.getTermCode()).append("'");
        }
        sql.append(" AND T.ALLPAY_LOGICDEL = 1");
        return hibernateDAO.listByHQL(sql.toString());
    }

    /**
     * 验证 pos商户号+终端编号 是否存在
     * @param allpayTerminalDto
     * @return
     * @throws Exception
     */
    @Override
    public boolean obtainByParamCode(AllpayTerminalDto allpayTerminalDto) throws Exception {
        StringBuilder together = new StringBuilder();
        together.append("SELECT T.TERMINAL_POSID FROM ALLPAY_TERMINAL T WHERE 1=1");
        if(!CommonHelper.isEmpty(allpayTerminalDto.getMerchaCode())){
            together.append(" AND T.TERMINAL_MERCHCODE ='").append(allpayTerminalDto.getMerchaCode()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getTermCode())){
            together.append(" AND T.TERMINAL_TERMCODE ='").append(allpayTerminalDto.getTermCode()).append("'");
        }
        together.append(" AND T.ALLPAY_LOGICDEL = 1");
        List<Map<String, Object>> list = (List<Map<String, Object>>)
                hibernateDAO.listBySQL(together.toString(), false);
        if(!list.isEmpty()){
            Map<String, Object> map = list.get(0);
            if(CommonHelper.isNullOrEmpty(allpayTerminalDto.getPosId())){
                return true;
            }else if(!(allpayTerminalDto.getPosId().equals(map.get("TERMINAL_POSID")))){
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 随机生成num位不重复的终端编号
     * @param num
     * @return
     * @throws Exception
     */
    @Override
    public String getTermCode(int num) throws Exception {
        String result="";
        while (true) {
            result = CommonHelper.getRandomNum(num);
            String sql = "SELECT TA.TERMINAL_TERMCODE FROM ALLPAY_TERMINAL TA WHERE TA.TERMINAL_TERMCODE = '"+result + "'";
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
     * 获取公共查询sql
     * @param allpayTerminalDto
     * @param flag
     * @return
     */
    public String getSql(AllpayTerminalDto allpayTerminalDto, boolean flag){
        StringBuffer sql = new StringBuffer(" FROM ALLPAY_TERMINAL T ");
        sql.append("LEFT JOIN ALLPAY_MERCHANT AM ON T.TERMINAL_MERCHANT_ID = AM.MERCHANT_ID AND AM.ALLPAY_LOGICDEL = 1")
                .append(" LEFT JOIN ALLPAY_STORE S ON T.TERMINAL_STORE_ID = S.STORE_ID AND S.ALLPAY_LOGICDEL = 1")
                .append(" LEFT JOIN ALLPAY_ORGANIZATION AO ON T.TERMINAL_BRANCHID = AO.ORGANIZATION_ID AND AO.ALLPAY_LOGICDEL = 1")
                .append(" LEFT JOIN ALLPAY_POSPARTER AP ON T.TERMINAL_PEIJIANPOSPARTERID = AP.POSPARTER_ID AND AP.ALLPAY_LOGICDEL = 1")
                .append(" LEFT JOIN ALLPAY_POSPARTER AP_SUP ON T.TERMINAL_POSPARTERID = AP_SUP.POSPARTER_ID AND AP_SUP.ALLPAY_LOGICDEL = 1")
                .append(" LEFT JOIN ALLPAY_TERMINAL SUPT ON SUPT.TERMINAL_POSID = T.TERMINAL_SUPERTERMCODE AND SUPT.ALLPAY_LOGICDEL = 1 WHERE 1=1");

        if(!CommonHelper.isEmpty(allpayTerminalDto.getStoreId())){  //门店id
            sql.append(" AND T.TERMINAL_STORE_ID = '").append(allpayTerminalDto.getStoreId()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getTermCode())) {    //终端号
            sql.append(" AND T.TERMINAL_TERMCODE = '").append(allpayTerminalDto.getTermCode()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getMerchaCode())) {    //商户号
            sql.append(" AND T.TERMINAL_MERCHCODE = '").append(allpayTerminalDto.getMerchaCode()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getMerchantId())) {    //商户id
            sql.append(" AND T.TERMINAL_MERCHANT_ID = '").append(allpayTerminalDto.getMerchantId()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getPosType())) {    //Pos类型	1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
            sql.append(" AND T.TERMINAL_POSTYPE in (").append(allpayTerminalDto.getPosType()).append(")");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getCreateStartTime())){  //录入开始时间
            String start = allpayTerminalDto.getCreateStartTime()+" 00:00:00";
            sql.append(" AND T.ALLPAY_CREATETIME >= TO_DATE('").append(start).append("', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getCreateEndTime())){ //录入结束时间
            String end = allpayTerminalDto.getCreateEndTime()+" 23:59:59";
            sql.append(" AND T.ALLPAY_CREATETIME <= TO_DATE('").append(end).append("', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getBranchId())){  //分公司id
            sql.append(" AND T.TERMINAL_BRANCHID = '").append(allpayTerminalDto.getBranchId()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getPosParterId())){  //合作方id
            sql.append(" AND T.TERMINAL_POSPARTERID = '").append(allpayTerminalDto.getPosParterId()).append("'");
        }
        if(!CommonHelper.isEmpty(allpayTerminalDto.getPeiJianPosParterId())){  //配件合作方id
            sql.append(" AND T.TERMINAL_PEIJIANPOSPARTERID = '").append(allpayTerminalDto.getPeiJianPosParterId()).append("'");
        }

        sql.append(" AND T.ALLPAY_LOGICDEL = 1 ORDER BY T.ALLPAY_UPDATETIME DESC");  //ALLPAY_LOGICDEL 1 未删除
        if(flag){  //如果是查询
            sql.append(" , R DESC");
        }
        return sql.toString();
    }

    @Override
    public List<Map<String,Object>> selectDataDictionary(String code, String value) throws Exception {
        String sql = "SELECT V.DATA_DICTIONARY,V.DATA_CODE, V.DATA_NAME, D.DICTIONARY_CODE, V.DATA_ISVALID FROM DATA_DICTIONARY D INNER JOIN DATA_VALUE V ON D.DICTIONARY_ID = V.DATA_DICTIONARY" +
                " WHERE V.DATA_ISVALID = '1' AND D.DICTIONARY_CODE = '"+code+"' AND V.DATA_CODE = '"+value+"'";
        List<Map<String,Object>> list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(),false);
        return list;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> obtainMsgForTerAndMerCode(String merCode,
			String terCode) throws Exception {
		
		StringBuilder together = new StringBuilder();
		together.append("select t.TERMINAL_PAYCHANNEL,l.STORE_SHOPNAME from ALLPAY_TERMINAL t");
		together.append(" left join ALLPAY_STORE l on t.TERMINAL_STORE_ID = l.STORE_ID");
		together.append(" where 1=1 ");
		if(!CommonHelper.isNullOrEmpty(merCode)){
			together.append(" AND t.TERMINAL_MERCHCODE = '").append(merCode).append("'");
		}
		if(!CommonHelper.isNullOrEmpty(terCode)){
			together.append(" AND t.TERMINAL_TERMCODE = '").append(terCode).append("'");
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) 
				hibernateDAO.listByPageBySQL(together.toString(), 10, 0, false);
		return list;
	}
}
