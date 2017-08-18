package com.cn.dao.impl;

import com.cn.dao.AllpayComuserInfoDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.po.Allpay_ComUserInfo;
import com.cn.entity.po.Wft_Shop;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cuixiaowei on 2017/1/5.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Repository("AllpayComuserInfoDao")
public class AllpayComuserInfoDaoImpl implements AllpayComuserInfoDao {

    @Autowired
    private HibernateDAO hibernateDAO;
    @Override
    public <T> String insert(T entity) {
        try {
            hibernateDAO.saveOrUpdate(entity);

            return "success";
        }catch(Exception e)
        {
            return "fail";
        }
    }

    @Override
    public List<Allpay_ComUserInfo> getComUserInfoByParatmeter(String paratmeter) {
        Conjunction conj = Restrictions.conjunction();
        conj.add(Restrictions.eq("comuser_businesslicensnum",paratmeter));
        return hibernateDAO.listByCriteria(Allpay_ComUserInfo.class, conj, false);
       // return hibernateDAO.listByField(Allpay_ComUserInfo.class,"comuser_businesslicensnum",paratmeter);
    }
}
