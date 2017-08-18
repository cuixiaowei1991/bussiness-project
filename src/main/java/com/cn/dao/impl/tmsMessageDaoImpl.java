package com.cn.dao.impl;

import com.cn.dao.tmsMessageDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.po.Allpay_TMS;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cuixiaowei on 2016/11/28.
 */
@Transactional
@Repository("tmsMessageDao")
public class tmsMessageDaoImpl implements tmsMessageDao {
    @Autowired
    private HibernateDAO hibernateDAO;
    public Allpay_TMS getTerminal(String tmsMessageId) {
        Session session= hibernateDAO.sessionFactory.getCurrentSession();
        return (Allpay_TMS) session.get(Allpay_TMS.class,tmsMessageId);
    }
}
