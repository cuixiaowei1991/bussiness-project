package com.cn.dao;


import com.cn.entity.po.Allpay_TMS;

/**
 * Created by cuixiaowei on 2016/11/28.
 */
public interface tmsMessageDao {
    /**
     * 根据主键id获取POS信息
     * @param tmsMessageId
     * @return
     */
    public Allpay_TMS getTerminal(String tmsMessageId);
}
