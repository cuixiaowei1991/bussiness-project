package com.cn.dao;

import com.cn.entity.po.Allpay_PosParter;

/**
 * Created by cuixiaowei on 2016/11/28.
 */
public interface posParterDao {
    /**
     * 根据主键id获取POS合作方信息
     * @param posparterid
     * @return
     */
    public Allpay_PosParter getPosParter(String posparterid);
}
