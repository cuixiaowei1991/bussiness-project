package com.cn.service;

/**
 * Created by cuixiaowei on 2016/11/24.
 */
public interface SendToKafkaService {
    /**
     * 推送商户信息至kafka
     * @param merchantId
     * @return
     */
    public boolean sentMerchantToKafka(String merchantId,String marked) throws Exception;

    /**
     * 推送网点信息至kafka
     * @param wangdianId
     * @return
     */
    public boolean sentWangDianToKafka(String wangdianId,String marked) throws Exception;

    /**
     * 推送渠道信息至kafka
     * @param merchantId
     * @return
     */
    public boolean sentQvDaoToKafka(String merchantId,String code) throws Exception;

    /**
     * 推送POS信息至kafka
     * @param posid
     * @return
     */
    public boolean sendApayPosToKafka(String posid,String marked) throws Exception;

    /**
     * 推送代理信息至kafka
     * @param agentId
     * @return
     */
    public boolean sendApayagentToKafka(String agentId,String marked) throws Exception;

    /**
     * 推送POS合作方信息至kafka
     * @param posParterId
     * @return
     */
    public boolean sendApayPOSParter(String posParterId,String marked) throws Exception;

    /**
     * 推送TMS信息至kafka
     * @param tmsMessageId
     * @return
     */
    public boolean sendApayTmsmessage(String tmsMessageId,String marked) throws Exception;
    /**
     * 推送测试信息至kafka
     * @param posParterId
     * @return
     */
    public boolean sendTestToKafka(String posParterId);

    /**
     * 推送威富通相关信息至kafka
     * @return
     */
    public boolean sendWFTToKafka(String wftId) throws Exception;
}
