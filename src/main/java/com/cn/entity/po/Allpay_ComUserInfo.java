package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 公司（个人）信息公共表
 * Created by cuixiaowei on 2016/12/2.
 */
@Entity
@Table(name="ALLPAY_COMUSERINFO")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_ComUserInfo extends publicFields {
    /**
     * 公司（个人）信息公共表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "COMUSER_ID", length = 32)
    private String comuser_id;
    /**
     * 商户ID
     */
    @Column(name = "COMUSER_SHOPID", length = 32)
    private String comuser_shopid;
    /**
     * 账户信息（公司账户或者个人账户）
     */
    @Column(name = "COMUSER_COUNTINFO", length = 1000)
    private String comuser_countinfo;
    /**
     * 公司名称
     */
    @Column(name = "COMUSER_COMPANYNAME", length = 1000)
    private String comuser_companyname;
    /**
     * 开户名称
     */
    @Column(name = "COMUSER_OPENCOUNTNAME", length = 500)
    private String comuser_opencountname;
    /**
     * 开户银行（公司或者个人）
     */
    @Column(name = "COMUSER_OPENCOUNTBANK", length = 500)
    private String comuser_opencountbank;
    /**
     * 银行账号
     */
    @Column(name = "COMUSER_BANKNUM", length = 50)
    private String comuser_banknum;
    /**
     * 证件号码（法人或者开户人身份证号）
     */
    @Column(name = "COMUSER_IDENTITYPAPERNUM", length = 50)
    private String comuser_identitypapernum;
    /**
     * 姓名（法人或者个人）
     */
    @Column(name = "COMUSER_IDENTITYNAME", length = 50)
    private String comuser_identityname;
    /**
     * 营业执照注册号
     */
    @Column(name = "COMUSER_BUSINESSLICENSENUM", length = 50)
    private String comuser_businesslicensnum;
    /**
     * 营业场所地址
     */
    @Column(name = "COMUSER_BUSINESSPLACEADDRESS", length = 1000)
    private String comuser_businessplaceaddress;
    /**
     * 公司或者个人  1---公司  2---个人
     * 公司（公司账户信息、公司名称、开户名称、公司开户银行、银行账号、法人证件号码、法人姓名、营业执照注册号、营业场所地址）
     * 个人（个人账户、个人开户银行、个人身份证号、个人姓名）
     */
    @Column(name = "COMUSER_ISCOMORPERSON", length = 1)
    private int comuser_iscomorperson;

    /**
     * 统一社会信用代码
     */
    @Column(name = "COMUSER_SOCIALCREDITCODE", length = 50)
    private String comuser_socialcreditcode;

    /**
     * 组织机构代码
     */
    @Column(name = "COMUSER_ORGANIZATIONCODE", length = 50)
    private String comuser_organizationcode;

    public String getComuser_socialcreditcode() {
        return comuser_socialcreditcode;
    }

    public void setComuser_socialcreditcode(String comuser_socialcreditcode) {
        this.comuser_socialcreditcode = comuser_socialcreditcode;
    }

    public String getComuser_organizationcode() {
        return comuser_organizationcode;
    }

    public void setComuser_organizationcode(String comuser_organizationcode) {
        this.comuser_organizationcode = comuser_organizationcode;
    }

    public String getComuser_id() {
        return comuser_id;
    }

    public void setComuser_id(String comuser_id) {
        this.comuser_id = comuser_id;
    }

    public String getComuser_shopid() {
        return comuser_shopid;
    }

    public void setComuser_shopid(String comuser_shopid) {
        this.comuser_shopid = comuser_shopid;
    }

    public String getComuser_countinfo() {
        return comuser_countinfo;
    }

    public void setComuser_countinfo(String comuser_countinfo) {
        this.comuser_countinfo = comuser_countinfo;
    }

    public String getComuser_companyname() {
        return comuser_companyname;
    }

    public void setComuser_companyname(String comuser_companyname) {
        this.comuser_companyname = comuser_companyname;
    }

    public String getComuser_opencountname() {
        return comuser_opencountname;
    }

    public void setComuser_opencountname(String comuser_opencountname) {
        this.comuser_opencountname = comuser_opencountname;
    }

    public String getComuser_opencountbank() {
        return comuser_opencountbank;
    }

    public void setComuser_opencountbank(String comuser_opencountbank) {
        this.comuser_opencountbank = comuser_opencountbank;
    }

    public String getComuser_banknum() {
        return comuser_banknum;
    }

    public void setComuser_banknum(String comuser_banknum) {
        this.comuser_banknum = comuser_banknum;
    }

    public String getComuser_identitypapernum() {
        return comuser_identitypapernum;
    }

    public void setComuser_identitypapernum(String comuser_identitypapernum) {
        this.comuser_identitypapernum = comuser_identitypapernum;
    }

    public String getComuser_identityname() {
        return comuser_identityname;
    }

    public void setComuser_identityname(String comuser_identityname) {
        this.comuser_identityname = comuser_identityname;
    }

    public String getComuser_businesslicensnum() {
        return comuser_businesslicensnum;
    }

    public void setComuser_businesslicensnum(String comuser_businesslicensnum) {
        this.comuser_businesslicensnum = comuser_businesslicensnum;
    }

    public String getComuser_businessplaceaddress() {
        return comuser_businessplaceaddress;
    }

    public void setComuser_businessplaceaddress(String comuser_businessplaceaddress) {
        this.comuser_businessplaceaddress = comuser_businessplaceaddress;
    }

    public int getComuser_iscomorperson() {
        return comuser_iscomorperson;
    }

    public void setComuser_iscomorperson(int comuser_iscomorperson) {
        this.comuser_iscomorperson = comuser_iscomorperson;
    }
}
