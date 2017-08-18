package com.cn.service.impl.businessServiceImpl;

import com.cn.dao.AllpayFileUploadDao;
import com.cn.entity.dto.AllpayTMSDto;
import com.cn.service.businessService.AllpayFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 上传文件管理业务层实现
 * Created by WangWenFang on 2016/12/20.
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class AllpayFileUploadServiceImpl implements AllpayFileUploadService {

    @Autowired
    private AllpayFileUploadDao allpayFileUploadDao;

    @Override
    public String obtainList(AllpayTMSDto allpayTMSDto) throws Exception {
        return null;
    }

    @Override
    public String insert(AllpayTMSDto allpayTMSDto) throws Exception {
        return null;
    }

    @Override
    public String update(AllpayTMSDto allpayTMSDto) throws Exception {
        return null;
    }

    @Override
    public String getById(AllpayTMSDto allpayTMSDto) throws Exception {
        return null;
    }

    @Override
    public String delete(AllpayTMSDto allpayTMSDto) throws Exception {
        return null;
    }
}
