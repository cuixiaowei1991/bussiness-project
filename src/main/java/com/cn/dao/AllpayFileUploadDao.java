package com.cn.dao;

import com.cn.entity.dto.AllpayTMSDto;
import com.cn.entity.po.Allpay_FileUpload;

import java.util.List;
import java.util.Map;

/**
 * 上传文件管理dao层接口
 * Created by WangWenFang on 2016/12/20.
 */
public interface AllpayFileUploadDao {

    List<Map<String, Object>> obtainList(AllpayTMSDto allpayTMSDto, Integer currentPage, Integer pageSize)throws Exception;

    int count(AllpayTMSDto allpayTMSDto) throws Exception;

    boolean insert(Allpay_FileUpload allpayFileUpload) throws Exception;

    boolean update(Allpay_FileUpload allpayFileUpload) throws Exception;

    boolean delete(String fileuploadId) throws Exception;

    boolean deleteByRecordId(String recordId) throws Exception;

    <T> T getById(Class<T> clazz, String tmsId) throws Exception;
}
