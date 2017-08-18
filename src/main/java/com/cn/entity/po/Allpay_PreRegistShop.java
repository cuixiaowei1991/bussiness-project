package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 预注册商户 实体
 * Created by sun.yayi on 2017/2/8.
 */
@Entity
@Table(name="ALLPAY_PREREGISTSHOP")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_PreRegistShop extends publicFields {

    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "PREREGISTSHOPID", length = 32)
    private String preRegistShopId;

    /**
     * 所属分子公司(全称)
     */
    @Column(name = "BRANCHCOMPANYNAME", length = 100)
    private String branchCompanyName;

    /**
     * 所属分子公司(简称)
     */
    @Column(name = "BRANCHCOMPANYSHORTNAME", length = 100)
    private String branchCompanyShortName;

    /**
     * 商户名称
     */
    @Column(name = "SHOPNAME", length = 100)
    private String shopName;

    /**
     * 录入日期
     */
    @Column(name = "CDATE")
    @Type(type = "java.util.Date")
    private Date cdate;

    /**
     * 商户类别
     */
    @Column(name = "SHOPSTATE", length = 50)
    private String shopState;

    /**
     * 门店名称
     */
    @Column(name = "STORENAME", length = 500)
    private String storeName;

    /**
     * 门店省份
     */
    @Column(name = "STOREPROVINCENAME", length = 500)
    private String storeprovinceName;

    /**
     * 门店地址
     */
    @Column(name = "STOREADRESS", length = 500)
    private String storeAdress;

    /**
     * 联系人
     */
    @Column(name = "PERSON", length = 50)
    private String person;

    /**
     * 联系电话
     */
    @Column(name = "PERSONPHONE", length = 50)
    private String personPhone;

    /**
     * 是否可用：0：不可用，1：可用。
     */
    @Column(name = "FLAG", length = 50)
    private String flag;

    /**
     * 数据来源
     */
    @Column(name = "DATASOURCE", length = 500)
    private String dataSource;

    public String getPreRegistShopId() {
        return preRegistShopId;
    }

    public void setPreRegistShopId(String preRegistShopId) {
        this.preRegistShopId = preRegistShopId;
    }

    public String getBranchCompanyName() {
        return branchCompanyName;
    }

    public void setBranchCompanyName(String branchCompanyName) {
        this.branchCompanyName = branchCompanyName;
    }

    public String getBranchCompanyShortName() {
        return branchCompanyShortName;
    }

    public void setBranchCompanyShortName(String branchCompanyShortName) {
        this.branchCompanyShortName = branchCompanyShortName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public String getShopState() {
        return shopState;
    }

    public void setShopState(String shopState) {
        this.shopState = shopState;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreprovinceName() {
        return storeprovinceName;
    }

    public void setStoreprovinceName(String storeprovinceName) {
        this.storeprovinceName = storeprovinceName;
    }

    public String getStoreAdress() {
        return storeAdress;
    }

    public void setStoreAdress(String storeAdress) {
        this.storeAdress = storeAdress;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
