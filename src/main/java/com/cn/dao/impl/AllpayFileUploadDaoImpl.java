package com.cn.dao.impl;

import com.cn.dao.AllpayFileUploadDao;
import com.cn.dao.util.HibernateDAO;
import com.cn.entity.dto.AllpayTMSDto;
import com.cn.entity.po.Allpay_FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 上传文件管理dao层实现
 * Created by WangWenFang on 2016/12/20.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Repository("allpayFileUploadDao")
public class AllpayFileUploadDaoImpl implements AllpayFileUploadDao {

    @Autowired
    private HibernateDAO hibernateDAO;

    @Override
    public List<Map<String, Object>> obtainList(AllpayTMSDto allpayTMSDto, Integer currentPage, Integer pageSize) throws Exception {
        return null;
    }

    @Override
    public int count(AllpayTMSDto allpayTMSDto) throws Exception {
        return 0;
    }

    @Override
    public boolean insert(Allpay_FileUpload allpayFileUpload) throws Exception {
        String result =  hibernateDAO.save(allpayFileUpload);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Allpay_FileUpload allpayFileUpload) throws Exception {
        String result =  hibernateDAO.saveOrUpdate(allpayFileUpload);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String fileuploadId) throws Exception {
        String result =  hibernateDAO.removeById(Allpay_FileUpload.class, fileuploadId);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteByRecordId(String recordId) throws Exception {
        String sql = "DELETE FROM ALLPAY_FILEUPLOAD AF WHERE AF.FILEUPLOAD_RECORD = '"+recordId+"'";
        String result = hibernateDAO.executeBySql(sql);
        if("success".equals(result)){
            return true;
        }
        return false;
    }

    @Override
    public <T> T getById(Class<T> clazz, String fileuploadId) throws Exception {
        return hibernateDAO.listById(clazz, fileuploadId, false);
    }
}
