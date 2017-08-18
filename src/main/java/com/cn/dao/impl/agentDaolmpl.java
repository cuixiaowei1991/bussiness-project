package com.cn.dao.impl;

import com.cn.common.CommonHelper;
import com.cn.dao.agentDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.dto.AllpayAgentDto;
import com.cn.entity.po.Allpay_Agent;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 代理商合作方 管理
 * Created by cuixiaowei on 2016/11/28.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Repository("agentDao")
public class agentDaolmpl implements agentDao {
    @Autowired
    private HibernateDAO hibernateDAO;

    public Allpay_Agent getMerchant(String agentId){
        Session session= hibernateDAO.sessionFactory.getCurrentSession();
        return (Allpay_Agent) session.get(Allpay_Agent.class,agentId);
    }

    public boolean saveOrUpate(Allpay_Agent agent) throws Exception{
        String result =  hibernateDAO.saveOrUpdate(agent);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    public List<Map<String, Object>> obtainList(AllpayAgentDto bean, String currentPage, String pageSize) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT AGENT.AGENT_ID,AGENT.AGENT_NAME,AGENT.AGENT_NUM,AGENT.AGENT_ADDRESS,AGENT.AGENT_TYPE,AGENT.AGENT_LEVEL,AGENT.AGENT_LOCATION,SUP_AGENT.AGENT_NAME AS SUP_AGENTNAME,SUP_AGENT.AGENT_ID AS SUP_AGENTID,AGENT.AGENT_BRANCH_ID,AO.ORGANIZATION_NAME,TO_CHAR(AGENT.ALLPAY_CREATETIME,'YYYY-MM-DD HH24:MI:SS') ALLPAY_CREATETIME,AGENT.ALLPAY_ISSTART");
        sql.append(getSql(bean));
        sql.append(" ORDER BY AGENT.ALLPAY_UPDATETIME DESC");
        List<Map<String, Object>> list=null;
        if(null != pageSize && null != currentPage){
            list= (List<Map<String, Object>>) hibernateDAO.listByPageBySQL(sql.toString(), Integer.parseInt(pageSize), Integer.parseInt(currentPage)-1, false);
        }else{
            list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(), false);
        }
        return list;
    }

    public int count(AllpayAgentDto bean) throws Exception{
        StringBuffer sql = new StringBuffer("SELECT AGENT.AGENT_ID");
        sql.append(getSql(bean));
        List list=hibernateDAO.listBySQL(sql.toString(),false);
        return list.size();
    }

    public String getSql(AllpayAgentDto bean){
        String userNameFromAgentCookie = CommonHelper.nullToString(bean.getUserNameFromAgentCookie());
        StringBuffer sql = new StringBuffer(" FROM ALLPAY_AGENT AGENT");
        sql.append(" LEFT JOIN ALLPAY_AGENT SUP_AGENT ON AGENT.AGENT_LOCATION=SUP_AGENT.AGENT_ID AND SUP_AGENT.ALLPAY_LOGICDEL=1");
        sql.append(" LEFT JOIN ALLPAY_ORGANIZATION AO ON AGENT.AGENT_BRANCH_ID=AO.ORGANIZATION_ID AND AO.ALLPAY_LOGICDEL=1 WHERE 0=0");
        if(!CommonHelper.isEmpty(bean.getAgentName())){
            sql.append(" AND AGENT.AGENT_NAME LIKE '%").append(bean.getAgentName()).append("%'");
        }
        if(!CommonHelper.isEmpty(bean.getAgentNum())){
            sql.append(" AND AGENT.AGENT_NUM = '").append(bean.getAgentNum()).append("'");
        }
        if(!CommonHelper.isEmpty(bean.getBranchId())){
            sql.append(" AND AGENT.AGENT_BRANCH_ID='").append(bean.getBranchId()).append("'");
        }
        if(!CommonHelper.isEmpty(bean.getAgentParentId()) && CommonHelper.isEmpty(userNameFromAgentCookie)){
            sql.append(" AND AGENT.AGENT_LOCATION='").append(bean.getAgentParentId()).append("'");
        }
        if(!CommonHelper.isEmpty(bean.getAgentId())){
            if(!CommonHelper.isEmpty(userNameFromAgentCookie)){
                sql.append(" AND (AGENT.AGENT_LOCATION='").append(bean.getAgentId()).append("' OR AGENT.AGENT_ID = '").append(bean.getAgentId()).append("')");
            }else{
                sql.append(" AND AGENT.AGENT_ID = '").append(bean.getAgentId()).append("'");
            }
        }
        if (!CommonHelper.isEmpty(bean.getLevel())){
            sql.append(" AND AGENT.AGENT_LEVEL=").append(bean.getLevel());
        }
        if(!CommonHelper.isEmpty(bean.getAgentType())) {  //代理商类型 0：个人代理  ，1：公司代理,空为全部
            sql.append(" AND AGENT.AGENT_TYPE=").append(bean.getAgentType());
        }
        if(!CommonHelper.isEmpty(bean.getIsStart())) {  // 是否启用（0:停用 1：启用  空为全部）
            sql.append(" AND AGENT.ALLPAY_ISSTART=").append(bean.getIsStart());
        }
        if(!CommonHelper.isEmpty(bean.getCreateStartTime())){  //录入开始时间
            String start = bean.getCreateStartTime()+" 00:00:00";
            sql.append(" AND AGENT.ALLPAY_CREATETIME >= TO_DATE('").append(start).append("', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(!CommonHelper.isEmpty(bean.getCreateEndTime())){ //录入结束时间
            String end = bean.getCreateEndTime()+" 23:59:59";
            sql.append(" AND AGENT.ALLPAY_CREATETIME <= TO_DATE('").append(end).append("', 'YYYY-MM-DD HH24:MI:SS')");
        }
        sql.append(" AND AGENT.ALLPAY_LOGICDEL=1");
        return sql.toString();
    }

    @Override
    public List<Map<String, Object>> getAgentBySql(String agentId) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT AA.AGENT_NAME,AA.AGENT_ID,AA.AGENT_SHORTNAME,AA.AGENT_LEGALPERSONNAME,AA.AGENT_LEGALPERSONPHONE,");
        sql.append("AA.AGENT_SHOPPEOPLENAME,AA.AGENT_LEVEL, AA.AGENT_BRANCH_ID,AO.ORGANIZATION_NAME,AA.AGENT_LOCATION,SA.AGENT_NAME AS SA_AGENT_NAME,");
        sql.append("AA.AGENT_PROVINCE_ID, AA.AGENT_PROVINCE_NAME,AA.AGENT_CITY_ID,AA.AGENT_CITY_NAME,AA.AGENT_COUNTRY_ID,AA.AGENT_COUNTRY_NAME,");
        sql.append("AA.AGENT_ADDRESS,AA.ALLPAY_ISSTART,AA.AGENT_TYPE,AA.AGENT_ALLPAYID,AA.AGENT_ALLPAYKEY FROM ALLPAY_AGENT AA");
        sql.append(" LEFT JOIN ALLPAY_ORGANIZATION AO ON AA.AGENT_BRANCH_ID = AO.ORGANIZATION_ID");
        sql.append(" LEFT JOIN ALLPAY_AGENT SA ON AA.AGENT_LOCATION = SA.AGENT_ID  WHERE AA.AGENT_ID = '").append(agentId).append("'");
        List<Map<String, Object>> list=(List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(), false);
        return list;
    }

    @Override
    public List<Allpay_Agent> selectAgent(String agentName, String branchId) throws Exception{
        List<Allpay_Agent> list=null;
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("agent_name", agentName));
        conj.add(Restrictions.eq("agent_branch_id",branchId));
        conj.add(Restrictions.eq("agent_level",1));
        conj.add(Restrictions.eq("ALLPAY_ISSTART",1));
       /* if("2".equals(level)){
            conj2.add(Restrictions.eq("agentLevel","1"));
        }
        if("3".equals(level)){
            conj2.add(Restrictions.eq("agentLevel","2"));
        }*/

        list=hibernateDAO.listByCriteria(Allpay_Agent.class,conj,false);
        return list;
    }

    @Override
    public List<Map<String,Object>> getAgentByAgentNum(String agentNum, String agentName) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT AGENT.AGENT_ID,AGENT.AGENT_NAME FROM ALLPAY_AGENT AGENT WHERE 1=1");
        if(!CommonHelper.isEmpty(agentNum)){
            sql.append(" AND AGENT.AGENT_NUM = '").append(agentNum).append("'");
        }
        if(!CommonHelper.isEmpty(agentName)){
            sql.append(" AND AGENT.AGENT_NAME = '").append(agentName).append("'");
        }
        sql.append(" AND AGENT.ALLPAY_LOGICDEL = 1 AND AGENT.ALLPAY_ISSTART = 1");
        List<Map<String,Object>> list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(),false);
        return list;
    }

    @Override
    public List<Map<String,Object>> isExist(AllpayAgentDto bean) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT AGENT.AGENT_ID FROM ALLPAY_AGENT AGENT WHERE 0=0");
        sql.append(" AND AGENT.AGENT_ALLPAYID='").append(bean.getAllpayid()).append("'");
        sql.append(" AND AGENT.ALLPAY_LOGICDEL=1");
        List<Map<String,Object>> list=null;
        list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(),false);
        return list;
    }

    @Override
    public List<Map<String, Object>> getAllAgents(AllpayAgentDto bean) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT AGENT.AGENT_ID,AGENT.AGENT_NAME FROM ALLPAY_AGENT AGENT WHERE 0=0");
        if(!CommonHelper.isNullOrEmpty(bean.getBranchId())){
            sql.append(" AND AGENT.AGENT_BRANCH_ID='").append(bean.getBranchId()).append("'");
        }
        sql.append(" AND AGENT.ALLPAY_LOGICDEL=1");
        List<Map<String,Object>> list=null;
        list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(),false);
        return list;
    }

    @Override
    public List<?> selectBranch(String branchName) throws Exception {
        String sql = "SELECT AO.ORGANIZATION_ID FROM ALLPAY_ORGANIZATION AO WHERE AO.ORGANIZATION_NAME = '"+branchName+"' AND AO.ALLPAY_LOGICDEL=1";
        List<Map<String,Object>> list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(),false);
        return list;
    }

    @Override
    public List<?> selectDataDictionary(String code, String value) throws Exception {
        String sql = "SELECT V.DATA_DICTIONARY,V.DATA_CODE, V.DATA_NAME, D.DICTIONARY_CODE, V.DATA_ISVALID FROM DATA_DICTIONARY D INNER JOIN DATA_VALUE V ON D.DICTIONARY_ID = V.DATA_DICTIONARY" +
                " WHERE V.DATA_ISVALID = '1' AND D.DICTIONARY_CODE = '"+code+"' AND V.DATA_NAME = '"+value+"'";
        List<Map<String,Object>> list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(),false);
        return list;
    }

    /**
     * 判断分公司是否存在
     * @param branchName
     * @return
     * @throws Exception
     */
    @Override
    public int CheckBranch(String branchName) throws Exception {
        String sql = "SELECT AO.ORGANIZATION_ID FROM ALLPAY_ORGANIZATION AO WHERE AO.ORGANIZATION_NAME = '"+branchName + "' AND AO.ALLPAY_LOGICDEL = 1";
        int count = 0;
        List list = hibernateDAO.listBySQL(sql, false);
        if(null != list && list.size() >0){
            count = list.size();
        }
        return count;
    }

    /**
     * 判断代理商是否存在
     * @param agentNum
     * @return
     * @throws Exception
     */
    @Override
    public int CheckAgent(String agentNum) throws Exception {
        String sql = "SELECT AA.AGENT_ID FROM ALLPAY_AGENT AA WHERE AA.AGENT_NUM = '"+agentNum + "' AND AA.ALLPAY_LOGICDEL = 1";
        int count = 0;
        List list = hibernateDAO.listBySQL(sql, false);
        if(null != list && list.size() >0){
            count = list.size();
        }
        return count;
    }

    /**
     * 随机生成num位不重复的代理商编号
     * @param num
     * @return
     * @throws Exception
     */
    @Override
    public String getAgentNum(int num) throws Exception {
        String result="";
        while (true) {
            result = CommonHelper.getRandomNum(num);
            String sql = "SELECT AA.AGENT_ID FROM ALLPAY_AGENT AA WHERE AA.AGENT_NUM = '"+result + "'";
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

    @Override
    public List<Map<String, Object>> getAgentsByAgentName(String agentName) throws Exception {
        String sql="SELECT A.AGENT_ID FROM ALLPAY_AGENT A WHERE A.AGENT_NAME='"+agentName+"' AND A.ALLPAY_LOGICDEL=1";
        List<Map<String,Object>> list=null;
        list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(),false);
        return list;
    }

    @Override
    public boolean delete(String agentId) throws Exception {
        String result =  hibernateDAO.removeById(Allpay_Agent.class,agentId);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> checkAgentPhoneExist(String phone) throws Exception {
        String sql=" SELECT A.AGENT_ID FROM ALLPAY_AGENT A WHERE A.AGENT_LEGALPERSONPHONE='"+phone+"' AND A.ALLPAY_LOGICDEL=1";
        List<Map<String,Object>> list=null;
        list= (List<Map<String, Object>>) hibernateDAO.listBySQL(sql.toString(),false);
        return list;
    }

}
