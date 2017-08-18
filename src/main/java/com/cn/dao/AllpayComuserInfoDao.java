package com.cn.dao;

import com.cn.entity.po.Allpay_ComUserInfo;

import java.util.List;

/**
 * Created by cuixiaowei on 2017/1/5.
 */
public interface AllpayComuserInfoDao {
    //public String insert(Allpay_ComUserInfo comUserInfo);
    public <T> String insert(T entity);
    public List<Allpay_ComUserInfo> getComUserInfoByParatmeter(String paratmeter);
}
