package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 文件上传表
 * Created by cuixiaowei on 2016/11/28.
 */
@Entity
@Table(name="ALLPAY_FILEUPLOAD")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_FileUpload extends publicFields {
    /**
     * 文件上传表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "FILEUPLOAD_ID", length = 32)
    private String fileupload_id;

    /**
     * 文件路径
     */
    @Column(name = "FILEUPLOAD_PATH", length = 1000)
    private String fileupload_path;

    /**
     * 文件名
     */
    @Column(name = "FILEUPLOAD_NAME", length = 500)
    private String fileupload_name;

    /**
     * 文件对应相关表主键id
     */
    @Column(name = "FILEUPLOAD_RECORD", length = 32)
    private String fileupload_record;

    /**
     * 文件类型 1--图片  2--其他文件类型 （字段允许为空）
     */
    @Column(name = "FILEUPLOAD_TYPE", length = 1)
    private String fileupload_type;

    public String getFileupload_id() {
        return fileupload_id;
    }

    public void setFileupload_id(String fileupload_id) {
        this.fileupload_id = fileupload_id;
    }

    public String getFileupload_path() {
        return fileupload_path;
    }

    public void setFileupload_path(String fileupload_path) {
        this.fileupload_path = fileupload_path;
    }

    public String getFileupload_name() {
        return fileupload_name;
    }

    public void setFileupload_name(String fileupload_name) {
        this.fileupload_name = fileupload_name;
    }

    public String getFileupload_record() {
        return fileupload_record;
    }

    public void setFileupload_record(String fileupload_record) {
        this.fileupload_record = fileupload_record;
    }

    public String getFileupload_type() {
        return fileupload_type;
    }

    public void setFileupload_type(String fileupload_type) {
        this.fileupload_type = fileupload_type;
    }
}
