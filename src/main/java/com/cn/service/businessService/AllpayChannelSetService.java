package com.cn.service.businessService;

import com.cn.entity.dto.AllpayChannelSetDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 业务签约管理业务层接口
 * Created by WangWenFang on 2016/12/7.
 */
public interface AllpayChannelSetService {

    String obtainList(AllpayChannelSetDto allpayChannelSetDto)throws Exception;

    void exportList(AllpayChannelSetDto allpayChannelSetDto,HttpServletResponse response)throws Exception;

    String insert(AllpayChannelSetDto allpayChannelSetDto) throws Exception;

    String update(AllpayChannelSetDto allpayChannelSetDto) throws Exception;

    String getByMerIdChanCode(AllpayChannelSetDto allpayChannelSetDto) throws Exception;

}
