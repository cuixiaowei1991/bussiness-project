package com.cn.dao.impl;

import com.cn.dao.posParterDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.po.Allpay_PosParter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cuixiaowei on 2016/11/28.
 */
@Transactional
@Repository("posParterDao")
public class posParterDaoImpl implements posParterDao {
    @Autowired
    private HibernateDAO hibernateDAO;
    public Allpay_PosParter getPosParter(String posparterid) {
        Session session= hibernateDAO.sessionFactory.getCurrentSession();
        return (Allpay_PosParter) session.get(Allpay_PosParter.class,posparterid);
    }
}
