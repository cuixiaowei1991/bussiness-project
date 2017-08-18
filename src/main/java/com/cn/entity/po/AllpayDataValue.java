package com.cn.entity.po;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * @author hzz
 * 数据字典值
 */
@Entity
@Table(name="ALLPAY_DATA_VALUE")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AllpayDataValue {
    /**
     * 数据字典值
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "DATA_ID", length = 32,unique = false)
    private String data_Id;

    /**
     * 编码
     */
    @Column(name = "DATA_CODE", length = 32)
    private String data_Code;

    /**
     * 名称
     */
    @Column(name = "DATA_NAME", length = 80)
    private String data_Name;
    /**
     * 上级
     */
    @Column(name = "DATA_PARENT_ID", length = 32)
    private String data_Parent_Id;
    /**
     * 数据字典
     */
    @Column(name = "DATA_DICTIONARY", length = 32)
    private String data_Dictionary;
    /**
     * 字符1
     */
    @Column(name = "DATA_STRING1", length = 255)
    private String data_String1;
    /**
     * 字符2
     */
    @Column(name = "DATA_STRING2", length = 255)
    private String data_String2;
    /**
     * 字符3
     */
    @Column(name = "DATA_STRING3", length = 255)
    private String data_String3;
    /**
     * 字符4
     */
    @Column(name = "DATA_STRING4", length = 255)
    private String data_String4;
    /**
     * 字符5
     */
    @Column(name = "DATA_STRING5", length = 255)
    private String data_String5;
    /**
     * 顺序
     */
    @Column(name = "DATA_SEQNO", length = 10)
    private int data_Seqno;
    /**
     * 是否有效
     */
    @Column(name = "DATA_ISVALID", length = 1)
    private int data_Isvalid;
    /**
     * 备注
     */
    @Column(name = "DATA_REMARK", length = 255)
    private String data_Remark;
    /**
     * 创建人
     */
    @Column(name = "DATA_CREATE", length = 40)
    private String data_Create;
    /**
     * 创建时间
     */
    @Column(name = "DATA_CREATED")
    @Type(type = "java.util.Date")
    private Date data_Created;
    /**
     * 修改人
     */
    @Column(name = "DATA_UPDATE", length = 40)
    private String data_Update;
    /**
     * 修改时间
     */
    @Column(name = "DATA_UPDATED")
    @Type(type = "java.util.Date")
    private Date data_Updated;

    public AllpayDataValue() {
    }

    public String getData_Code() {
        return data_Code;
    }

    public void setData_Code(String data_Code) {
        this.data_Code = data_Code;
    }

    public String getData_Name() {
        return data_Name;
    }

    public void setData_Name(String data_Name) {
        this.data_Name = data_Name;
    }

    public String getData_Id() {
        return data_Id;
    }

    public void setData_Id(String data_Id) {
        this.data_Id = data_Id;
    }

    public String getData_Parent_Id() {
        return data_Parent_Id;
    }

    public void setData_Parent_Id(String data_Parent_Id) {
        this.data_Parent_Id = data_Parent_Id;
    }

    public String getData_Dictionary() {
        return data_Dictionary;
    }

    public void setData_Dictionary(String data_Dictionary) {
        this.data_Dictionary = data_Dictionary;
    }

    public String getData_String1() {
        return data_String1;
    }

    public void setData_String1(String data_String1) {
        this.data_String1 = data_String1;
    }

    public String getData_String2() {
        return data_String2;
    }

    public void setData_String2(String data_String2) {
        this.data_String2 = data_String2;
    }

    public String getData_String3() {
        return data_String3;
    }

    public void setData_String3(String data_String3) {
        this.data_String3 = data_String3;
    }

    public String getData_String4() {
        return data_String4;
    }

    public void setData_String4(String data_String4) {
        this.data_String4 = data_String4;
    }

    public String getData_String5() {
        return data_String5;
    }

    public void setData_String5(String data_String5) {
        this.data_String5 = data_String5;
    }

    public Number getData_Seqno() {
        return data_Seqno;
    }

    public void setData_Seqno(int data_Seqno) {
        this.data_Seqno = data_Seqno;
    }

    public int getData_Isvalid() {
        return data_Isvalid;
    }

    public void setData_Isvalid(int data_Isvalid) {
        this.data_Isvalid = data_Isvalid;
    }

    public String getData_Remark() {
        return data_Remark;
    }

    public void setData_Remark(String data_Remark) {
        this.data_Remark = data_Remark;
    }

    public String getData_Create() {
        return data_Create;
    }

    public void setData_Create(String data_Create) {
        this.data_Create = data_Create;
    }

    public Date getData_Created() {
        return data_Created;
    }

    public void setData_Created(Date data_Created) {
        this.data_Created = data_Created;
    }

    public String getData_Update() {
        return data_Update;
    }

    public void setData_Update(String data_Update) {
        this.data_Update = data_Update;
    }

    public Date getData_Updated() {
        return data_Updated;
    }

    public void setData_Updated(Date data_Updated) {
        this.data_Updated = data_Updated;
    }
}