package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Pos合作方表
 * Created by cuixiaowei on 2016/11/28.
 */
@Entity
@Table(name="ALLPAY_POSPARTER")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_PosParter extends publicFields {
    /**
     * Pos合作方表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "POSPARTER_ID", length = 32)
    private String posparter_id;

    @Column(name = "POSPARTER_PARTERID", length = 32)
    private String posparter_parterid;

    /**
     * 合作方全称
     */
    @Column(name = "POSPARTER_PARTERALLNAME", length = 200)
    private String posparter_parterallname;
    /**
     * 合作方简称
     */
    @Column(name = "POSPARTER_PARTERSHORTNAME", length = 200)
    private String posparter_partershortname;
    /**
     * 联系人
     */
    @Column(name = "POSPARTER_CONNECTPERSON", length = 50)
    private String posparter_connectperson;
    /**
     * 联系方式
     */
    @Column(name = "POSPARTER_CONNECTTEL", length = 20)
    private String posparter_connecttel;
    /**
     * 地址
     */
    @Column(name = "POSPARTER_ADDRESS", length = 500)
    private String posparter_address;
    /**
     * 合作方级别 1：第一级合作方，2：第二级合作方
     */
    @Column(name = "POSPARTER_PARTERLEVEL", length = 1)
    private int posparter_parterlevel;
    /**
     * 分公司id
     */
    @Column(name = "POSPARTER_BRANCH_ID", length = 32)
    private String posparter_branch_id;
    /**
     * 所属上级合作方
     */
    @Column(name = "POSPARTER_BELONGPARTERID", length = 32)
    private String posparter_belongparterid;
    /**
     * 省代码
     */
    @Column(name = "POSPARTER_PROVINCE_ID", length = 32)
    private String posparter_province_id;
    /**
     * 省名称
     */
    @Column(name = "POSPARTER_PROVINCE_name", length = 100)
    private String posparter_province_name;
    /**
     * 市代码
     */
    @Column(name = "POSPARTER_CITY_ID", length = 32)
    private String posparter_city_id;
    /**
     * 市名称
     */
    @Column(name = "POSPARTER_CITY_NAME", length = 100)
    private String posparter_city_name;
    /**
     * 区县代码
     */
    @Column(name = "POSPARTER_COUNTRY_ID", length = 32)
    private String posparter_country_id;
    /**
     * 区县名称
     */
    @Column(name = "POSPARTER_COUNTRY_NAME", length = 100)
    private String posparter_country_name;
    /**
     * 备注
     */
    @Column(name = "POSPARTER_REMARK", length = 1000)
    private String posparter_remark;

    /**
     * 拓展人
     */
    @Column(name = "POSPARTER_EXPANDPEOPLE", length = 50)
    private String posparter_expandpeople;

    public String getPosparter_expandpeople() {
        return posparter_expandpeople;
    }

    public void setPosparter_expandpeople(String posparter_expandpeople) {
        this.posparter_expandpeople = posparter_expandpeople;
    }

    public String getPosparter_id() {
        return posparter_id;
    }

    public void setPosparter_id(String posparter_id) {
        this.posparter_id = posparter_id;
    }


    public String getPosparter_parterallname() {
        return posparter_parterallname;
    }

    public void setPosparter_parterallname(String posparter_parterallname) {
        this.posparter_parterallname = posparter_parterallname;
    }

    public String getPosparter_partershortname() {
        return posparter_partershortname;
    }

    public void setPosparter_partershortname(String posparter_partershortname) {
        this.posparter_partershortname = posparter_partershortname;
    }

    public String getPosparter_connectperson() {
        return posparter_connectperson;
    }

    public void setPosparter_connectperson(String posparter_connectperson) {
        this.posparter_connectperson = posparter_connectperson;
    }

    public String getPosparter_connecttel() {
        return posparter_connecttel;
    }

    public void setPosparter_connecttel(String posparter_connecttel) {
        this.posparter_connecttel = posparter_connecttel;
    }

    public String getPosparter_address() {
        return posparter_address;
    }

    public void setPosparter_address(String posparter_address) {
        this.posparter_address = posparter_address;
    }

    public int getPosparter_parterlevel() {
        return posparter_parterlevel;
    }

    public void setPosparter_parterlevel(int posparter_parterlevel) {
        this.posparter_parterlevel = posparter_parterlevel;
    }

    public String getPosparter_branch_id() {
        return posparter_branch_id;
    }

    public void setPosparter_branch_id(String posparter_branch_id) {
        this.posparter_branch_id = posparter_branch_id;
    }

    public String getPosparter_belongparterid() {
        return posparter_belongparterid;
    }

    public void setPosparter_belongparterid(String posparter_belongparterid) {
        this.posparter_belongparterid = posparter_belongparterid;
    }

    public String getPosparter_province_id() {
        return posparter_province_id;
    }

    public void setPosparter_province_id(String posparter_province_id) {
        this.posparter_province_id = posparter_province_id;
    }

    public String getPosparter_province_name() {
        return posparter_province_name;
    }

    public void setPosparter_province_name(String posparter_province_name) {
        this.posparter_province_name = posparter_province_name;
    }

    public String getPosparter_city_id() {
        return posparter_city_id;
    }

    public void setPosparter_city_id(String posparter_city_id) {
        this.posparter_city_id = posparter_city_id;
    }

    public String getPosparter_city_name() {
        return posparter_city_name;
    }

    public void setPosparter_city_name(String posparter_city_name) {
        this.posparter_city_name = posparter_city_name;
    }

    public String getPosparter_country_id() {
        return posparter_country_id;
    }

    public void setPosparter_country_id(String posparter_country_id) {
        this.posparter_country_id = posparter_country_id;
    }

    public String getPosparter_country_name() {
        return posparter_country_name;
    }

    public void setPosparter_country_name(String posparter_country_name) {
        this.posparter_country_name = posparter_country_name;
    }

    public String getPosparter_remark() {
        return posparter_remark;
    }

    public void setPosparter_remark(String posparter_remark) {
        this.posparter_remark = posparter_remark;
    }

    public String getPosparter_parterid() {
        return posparter_parterid;
    }

    public void setPosparter_parterid(String posparter_parterid) {
        this.posparter_parterid = posparter_parterid;
    }
}
