package com.cn.entity.po;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * @author hzz
 * 数据字典表
 */
@Entity
@Table(name="ALLPAY_DATA_DICTIONARY")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AllpayDataDictionary {

    /**
     * 数据字典
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "DICTIONARY_ID", length = 32)
    private String dictionary_Id;

    /**
     * 编码
     */
    @Column(name = "DICTIONARY_CODE", length = 60)
    private String dictionary_Code;

    /**
     * 名称
     */
    @Column(name = "DICTIONARY_NAME", length = 80)
    private String dictionary_Name;

    /**
     * 字符1
     */
    @Column(name = "DICTIONARY_STRING1", length = 255)
    private String dictionary_String1;
    /**
     * 字符2
     */
    @Column(name = "DICTIONARY_STRING2", length = 255)
    private String dictionary_String2;
    /**
     * 字符3
     */
    @Column(name = "DICTIONARY_STRING3", length = 255)
    private String dictionary_String3;
    /**
     * 字符4
     */
    @Column(name = "DICTIONARY_STRING4", length = 255)
    private String dictionary_String4;
    /**
     * 字符5
     */
    @Column(name = "DICTIONARY_STRING5", length = 255)
    private String dictionary_String5;
    /**
     * 顺序
     */
    @Column(name = "DICTIONARY_SEQNO", length = 32)
    private String dictionary_Seqno;
    /**
     * 是否有效
     */
    @Column(name = "DICTIONARY_ISVALID", length = 1)
    private String dictionary_Isvalid;
    /**
     * 备注
     */
    @Column(name = "DICTIONARY_REMARK", length = 255)
    private String dictionary_Remark;
    /**
     * 创建人
     */
    @Column(name = "DICTIONARY_CREATE", length = 40)
    private String dictionary_Create;
    /**
     * 创建时间
     */
    @Column(name = "DICTIONARY_CREATED")
    @Type(type = "java.util.Date")
    private Date dictionary_Created;
    /**
     * 修改人
     */
    @Column(name = "DICTIONARY_UPDATE", length = 40)
    private String dictionary_Update;
    /**
     * 修改时间
     */
    @Column(name = "DICTIONARY_UPDATED")
    @Type(type = "java.util.Date")
    private Date dictionary_Updated;

    public AllpayDataDictionary() {
    }


    public String getDictionary_Code() {
        return dictionary_Code;
    }

    public void setDictionary_Code(String dictionary_Code) {
        this.dictionary_Code = dictionary_Code;
    }

    public String getDictionary_Name() {
        return dictionary_Name;
    }

    public void setDictionary_Name(String dictionary_Name) {
        this.dictionary_Name = dictionary_Name;
    }

    public String getDictionary_String1() {
        return dictionary_String1;
    }

    public void setDictionary_String1(String dictionary_String1) {
        this.dictionary_String1 = dictionary_String1;
    }

    public String getDictionary_String2() {
        return dictionary_String2;
    }

    public void setDictionary_String2(String dictionary_String2) {
        this.dictionary_String2 = dictionary_String2;
    }

    public String getDictionary_String3() {
        return dictionary_String3;
    }

    public void setDictionary_String3(String dictionary_String3) {
        this.dictionary_String3 = dictionary_String3;
    }

    public String getDictionary_String4() {
        return dictionary_String4;
    }

    public void setDictionary_String4(String dictionary_String4) {
        this.dictionary_String4 = dictionary_String4;
    }

    public String getDictionary_String5() {
        return dictionary_String5;
    }

    public void setDictionary_String5(String dictionary_String5) {
        this.dictionary_String5 = dictionary_String5;
    }


    public String getDictionary_Id() {
        return dictionary_Id;
    }

    public void setDictionary_Id(String dictionary_Id) {
        this.dictionary_Id = dictionary_Id;
    }

    public String getDictionary_Seqno() {
        return dictionary_Seqno;
    }

    public void setDictionary_Seqno(String dictionary_Seqno) {
        this.dictionary_Seqno = dictionary_Seqno;
    }

    public String getDictionary_Isvalid() {
        return dictionary_Isvalid;
    }

    public void setDictionary_Isvalid(String dictionary_Isvalid) {
        this.dictionary_Isvalid = dictionary_Isvalid;
    }

    public String getDictionary_Remark() {
        return dictionary_Remark;
    }

    public void setDictionary_Remark(String dictionary_Remark) {
        this.dictionary_Remark = dictionary_Remark;
    }

    public String getDictionary_Create() {
        return dictionary_Create;
    }

    public void setDictionary_Create(String dictionary_Create) {
        this.dictionary_Create = dictionary_Create;
    }

    public Date getDictionary_Created() {
        return dictionary_Created;
    }

    public void setDictionary_Created(Date dictionary_Created) {
        this.dictionary_Created = dictionary_Created;
    }

    public String getDictionary_Update() {
        return dictionary_Update;
    }

    public void setDictionary_Update(String dictionary_Update) {
        this.dictionary_Update = dictionary_Update;
    }

    public Date getDictionary_Updated() {
        return dictionary_Updated;
    }

    public void setDictionary_Updated(Date dictionary_Updated) {
        this.dictionary_Updated = dictionary_Updated;
    }
}
