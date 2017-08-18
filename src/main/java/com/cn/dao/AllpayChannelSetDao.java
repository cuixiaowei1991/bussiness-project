package com.cn.dao;

import com.cn.entity.dto.AllpayChannelSetDto;
import com.cn.entity.po.Allpay_ChannelSet;
import org.apache.poi.hssf.record.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * 业务签约管理dao层接口
 * Created by WangWenFang on 2016/12/7.
 */
public interface AllpayChannelSetDao {

    List<Map<String, Object>> obtainList(AllpayChannelSetDto allpayChannelSetDto, Integer currentPage, Integer pageSize, boolean isExport)throws Exception;

    int count(AllpayChannelSetDto allpayChannelSetDto) throws Exception;

    boolean insert(Allpay_ChannelSet allpayChannelSet) throws Exception;

    boolean update(Allpay_ChannelSet allpayChannelSet) throws Exception;

    <T> T getById(Class<T> clazz, String channelSetId) throws Exception;

    List<?> getByMerIdChanCode(String merchantId, String channelCode) throws Exception;

    boolean removeByMerIdChanCode(String merchantId, String channelCode) throws Exception;

    List<Map<String, Object>> obtainMerAndChanList(AllpayChannelSetDto allpayChannelSetDto, Integer currentPage, Integer pageSize, boolean isByPage)throws Exception;

    List<Allpay_ChannelSet> getChannelsByMerchantId(String merchantId,String code) throws Exception;

    boolean updateMerChnnel(String merchantId, String channleList) throws Exception;
}
