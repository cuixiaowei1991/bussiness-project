package com.cn.service.businessService;

import com.cn.entity.dto.AllpayTMSDto;

/**
 * 上传文件管理业务层接口
 * Created by WangWenFang on 2016/12/20.
 */
public interface AllpayFileUploadService {

    String obtainList(AllpayTMSDto allpayTMSDto)throws Exception;

    String insert(AllpayTMSDto allpayTMSDto) throws Exception;

    String update(AllpayTMSDto allpayTMSDto) throws Exception;

    String getById(AllpayTMSDto allpayTMSDto) throws Exception;

    String delete(AllpayTMSDto allpayTMSDto) throws Exception;
}
