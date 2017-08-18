package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 网点表
 * Created by cuixiaowei on 2016/11/25.
 */
@Entity
@Table(name="ALLPAY_STORE")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_Store extends publicFields {
    /**
     * 网点表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "STORE_ID", length = 32)
    private String store_id;
    /**
     * 商户主建
     */
    @Column(name = "STORE_MERCHANT_ID", length = 32)
    private String store_merchant_id;
    /**
     * 网点9位编号
     */
    @Column(name = "STORE_SHOPIDNUMBER", length = 20)
    private String store_shopidnumber;
    /**
     * 15位编码（百度是18位的门店号）
     */
    @Column(name = "STORE_SHOPNUMBER", length = 20)
    private String store_shopnumber;
    /**
     * 网点名称
     */
    @Column(name = "STORE_SHOPNAME", length = 200)
    private String store_shopname;
    /**
     * 网点简称
     */
    @Column(name = "STORE_SHOPSHORTNAME", length = 200)
    private String store_shopshoptname;
    /**
     * 网点地址
     */
    @Column(name = "STORE_ADDRESS", length = 500)
    private String store_address;
    /**
     * 省名称
     */
    @Column(name = "STORE_PROVINCENAME", length = 100)
    private String store_provincename;
    /**
     * 省代码
     */
    @Column(name = "STORE_PROVINCEID", length = 32)
    private String store_provinceid;

    /**
     * 市名称
     */
    @Column(name = "STORE_CITYNAME", length = 100)
    private String store_cityname;
    /**
     * 市代码
     */
    @Column(name = "STORE_CITYID", length = 32)
    private String store_cityid;
    /**
     * 区县代码
     */
    @Column(name = "STORE_COUNTRY_ID", length = 32)
    private String store_country_id;
    /**
     * 区县名称
     */
    @Column(name = "STORE_COUNTRY_NAME", length = 100)
    private String store_country_name;
    /**
     * 网点经纬度(正常格式39.9528,116.3679)
     */
    @Column(name = "STORE_LOCATIONTUDE", length = 100)
    private String store_locationtude;
    /**
     * 营业开始时间
     */
    @Column(name = "STORE_BUSINESSSTARTHOURS")
    @Type(type = "java.util.Date")
    private Date store_businessstarthours;
    /**
     * 营业结束时间
     */
    @Column(name = "STORE_BUSINESSENDHOURS")
    @Type(type = "java.util.Date")
    private Date store_businessendhours;
    /**
     * 客服电话
     */
    @Column(name = "STORE_CUSTOMERSERVICEPHONE", length = 20)
    private String store_customerservicephone;
    /**
     * 特色服务
     */
    @Column(name = "STORE_SPECIALSERVICES", length = 500)
    private String store_specialservices;
    /**
     * pos显示网点名称
     */
    @Column(name = "STORE_POSSHOPNAME", length = 200)
    private String store_posshopname;
    /**
     * 支付宝口碑id
     */
    @Column(name = "STORE_REPUTATIONID", length = 200)
    private String store_ReputationId;

    public String getStore_ReputationId() {
        return store_ReputationId;
    }

    public void setStore_ReputationId(String store_ReputationId) {
        this.store_ReputationId = store_ReputationId;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_merchant_id() {
        return store_merchant_id;
    }

    public void setStore_merchant_id(String store_merchant_id) {
        this.store_merchant_id = store_merchant_id;
    }

    public String getStore_shopidnumber() {
        return store_shopidnumber;
    }

    public void setStore_shopidnumber(String store_shopidnumber) {
        this.store_shopidnumber = store_shopidnumber;
    }

    public String getStore_shopnumber() {
        return store_shopnumber;
    }

    public void setStore_shopnumber(String store_shopnumber) {
        this.store_shopnumber = store_shopnumber;
    }

    public String getStore_shopname() {
        return store_shopname;
    }

    public void setStore_shopname(String store_shopname) {
        this.store_shopname = store_shopname;
    }

    public String getStore_shopshoptname() {
        return store_shopshoptname;
    }

    public void setStore_shopshoptname(String store_shopshoptname) {
        this.store_shopshoptname = store_shopshoptname;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_provincename() {
        return store_provincename;
    }

    public void setStore_provincename(String store_provincename) {
        this.store_provincename = store_provincename;
    }

    public String getStore_provinceid() {
        return store_provinceid;
    }

    public void setStore_provinceid(String store_provinceid) {
        this.store_provinceid = store_provinceid;
    }

    public String getStore_cityname() {
        return store_cityname;
    }

    public void setStore_cityname(String store_cityname) {
        this.store_cityname = store_cityname;
    }

    public String getStore_cityid() {
        return store_cityid;
    }

    public void setStore_cityid(String store_cityid) {
        this.store_cityid = store_cityid;
    }

    public String getStore_country_id() {
        return store_country_id;
    }

    public void setStore_country_id(String store_country_id) {
        this.store_country_id = store_country_id;
    }

    public String getStore_country_name() {
        return store_country_name;
    }

    public void setStore_country_name(String store_country_name) {
        this.store_country_name = store_country_name;
    }

    public String getStore_locationtude() {
        return store_locationtude;
    }

    public void setStore_locationtude(String store_locationtude) {
        this.store_locationtude = store_locationtude;
    }

    public Date getStore_businessstarthours() {
        return store_businessstarthours;
    }

    public void setStore_businessstarthours(Date store_businessstarthours) {
        this.store_businessstarthours = store_businessstarthours;
    }

    public Date getStore_businessendhours() {
        return store_businessendhours;
    }

    public void setStore_businessendhours(Date store_businessendhours) {
        this.store_businessendhours = store_businessendhours;
    }

    public String getStore_customerservicephone() {
        return store_customerservicephone;
    }

    public void setStore_customerservicephone(String store_customerservicephone) {
        this.store_customerservicephone = store_customerservicephone;
    }

    public String getStore_specialservices() {
        return store_specialservices;
    }

    public void setStore_specialservices(String store_specialservices) {
        this.store_specialservices = store_specialservices;
    }

    public String getStore_posshopname() {
        return store_posshopname;
    }

    public void setStore_posshopname(String store_posshopname) {
        this.store_posshopname = store_posshopname;
    }
}
