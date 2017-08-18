package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 代理商表
 * Created by cuixiaowei on 2016/11/28.
 */
@Entity
@Table(name="ALLPAY_AGENT")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_Agent extends publicFields {
    /**
     * 代理商表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "AGENT_ID", length = 32)
    private String agent_id;
    /**
     * 代理级别 1 ：一级代理，2： 二级代理
     */
    @Column(name = "AGENT_LEVEL", length = 1)
    private int agent_level;

    /**
     * 上级代理id  0:没有上级
     */
    @Column(name = "AGENT_LOCATION", length = 32)
    private String agent_location;

    /**
     * 代理所属分公司id
     */
    @Column(name = "AGENT_BRANCH_ID", length = 32)
    private String agent_branch_id;
    /**
     * 代理全称
     */
    @Column(name = "AGENT_NAME", length = 200)
    private String agent_name;
    /**
     * 代理简称
     */
    @Column(name = "AGENT_SHORTNAME", length = 200)
    private String agent_shortname;
    /**
     * 省代码
     */
    @Column(name = "AGENT_PROVINCE_ID", length = 32)
    private String agent_province_id;
    /**
     * 省名称
     */
    @Column(name = "AGENT_PROVINCE_NAME", length = 100)
    private String agent_province_name;
    /**
     * 市代码
     */
    @Column(name = "AGENT_CITY_ID", length = 32)
    private String agent_city_id;
    /**
     * 市名称
     */
    @Column(name = "AGENT_CITY_NAME", length = 100)
    private String agent_city_name;
    /**
     * 区县代码
     */
    @Column(name = "AGENT_COUNTRY_ID", length = 32)
    private String agent_country_id;
    /**
     * 区县名称
     */
    @Column(name = "AGENT_COUNTRY_NAME", length = 100)
    private String agent_country_name;
    /**
     * 代理地址
     */
    @Column(name = "AGENT_ADDRESS", length = 500)
    private String agent_address;
    /**
     * 代理联系人姓名
     */
    @Column(name = "AGENT_LEGALPERSONNAME", length = 50)
    private String agent_legalpersonname;
    /**
     * 代理联系人手机号
     */
    @Column(name = "AGENT_LEGALPERSONPHONE", length = 20)
    private String agent_legalpersonphone;
    /**
     * 备注
     */
    @Column(name = "AGENT_REMARK", length = 1000)
    private String agent_remark;
    /**
     * Allpay分配给代理商接入ID
     */
    @Column(name = "AGENT_AllPAYID", length = 32)
    private String agent_allpayid;
    /**
     * Allpay分配给代理商接入key1
     */
    @Column(name = "AGENT_AllPAYKEY", length = 200)
    private String agent_allpaykey;
    /**
     * 代理商拓展人
     */
    @Column(name = "AGENT_SHOPPEOPLENAME", length = 50)
    private String agent_shoppeoplename;

    /**
     * 代理商类型 0：个人代理  ，1：公司代理
     */
    @Column(name = "AGENT_TYPE", length = 2)
    public String agent_Type;

    /**
     * 代理商编号（4位）
     */
    @Column(name = "AGENT_NUM", length = 10)
    public String agent_num;

    public String getAgent_num() {
        return agent_num;
    }

    public void setAgent_num(String agent_num) {
        this.agent_num = agent_num;
    }

    public String getAgent_Type() {
        return agent_Type;
    }

    public void setAgent_Type(String agent_Type) {
        this.agent_Type = agent_Type;
    }
    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public int getAgent_level() {
        return agent_level;
    }

    public void setAgent_level(int agent_level) {
        this.agent_level = agent_level;
    }

    public String getAgent_location() {
        return agent_location;
    }

    public void setAgent_location(String agent_location) {
        this.agent_location = agent_location;
    }

    public String getAgent_branch_id() {
        return agent_branch_id;
    }

    public void setAgent_branch_id(String agent_branch_id) {
        this.agent_branch_id = agent_branch_id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getAgent_shortname() {
        return agent_shortname;
    }

    public void setAgent_shortname(String agent_shortname) {
        this.agent_shortname = agent_shortname;
    }

    public String getAgent_province_id() {
        return agent_province_id;
    }

    public void setAgent_province_id(String agent_province_id) {
        this.agent_province_id = agent_province_id;
    }

    public String getAgent_province_name() {
        return agent_province_name;
    }

    public void setAgent_province_name(String agent_province_name) {
        this.agent_province_name = agent_province_name;
    }

    public String getAgent_city_id() {
        return agent_city_id;
    }

    public void setAgent_city_id(String agent_city_id) {
        this.agent_city_id = agent_city_id;
    }

    public String getAgent_city_name() {
        return agent_city_name;
    }

    public void setAgent_city_name(String agent_city_name) {
        this.agent_city_name = agent_city_name;
    }

    public String getAgent_country_id() {
        return agent_country_id;
    }

    public void setAgent_country_id(String agent_country_id) {
        this.agent_country_id = agent_country_id;
    }

    public String getAgent_country_name() {
        return agent_country_name;
    }

    public void setAgent_country_name(String agent_country_name) {
        this.agent_country_name = agent_country_name;
    }

    public String getAgent_address() {
        return agent_address;
    }

    public void setAgent_address(String agent_address) {
        this.agent_address = agent_address;
    }

    public String getAgent_legalpersonname() {
        return agent_legalpersonname;
    }

    public void setAgent_legalpersonname(String agent_legalpersonname) {
        this.agent_legalpersonname = agent_legalpersonname;
    }

    public String getAgent_legalpersonphone() {
        return agent_legalpersonphone;
    }

    public void setAgent_legalpersonphone(String agent_legalpersonphone) {
        this.agent_legalpersonphone = agent_legalpersonphone;
    }

    public String getAgent_remark() {
        return agent_remark;
    }

    public void setAgent_remark(String agent_remark) {
        this.agent_remark = agent_remark;
    }

    public String getAgent_allpayid() {
        return agent_allpayid;
    }

    public void setAgent_allpayid(String agent_allpayid) {
        this.agent_allpayid = agent_allpayid;
    }

    public String getAgent_allpaykey() {
        return agent_allpaykey;
    }

    public void setAgent_allpaykey(String agent_allpaykey) {
        this.agent_allpaykey = agent_allpaykey;
    }

    public String getAgent_shoppeoplename() {
        return agent_shoppeoplename;
    }

    public void setAgent_shoppeoplename(String agent_shoppeoplename) {
        this.agent_shoppeoplename = agent_shoppeoplename;
    }
}
