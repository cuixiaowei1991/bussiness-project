package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 威富通支付类型表
 * Created by sun.yayi on 2017/1/9.
 */

@Entity
@Table(name="WFT_PAYTYPE")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Wft_PayType extends publicFields {

    /**
     * 威富通支付类型表id主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "WFT_PAYTYPE_ID", length = 32)
    private String wft_paytype_id;

    /**
     * 对应的威富通表id
     */
    @Column(name = "WFT_PAYTYPE_WFTSHOP_ID", length = 32)
    private String wft_paytype_wftshop_id;

    /**
     * 支付类型名称
     */
    @Column(name = "WFT_PAYTYPE_NAME", length = 50)
    private String wft_paytype_name;

    /**
     * 支付类型类型代码
     */
    @Column(name = "WFT_PAYTYPE_CODE", length = 50)
    private String wft_paytype_code;

    /**
     * 费率
     */
    @Column(name = "WFT_PAYTYPE_BILL", length = 10)
    private String wft_paytype_bill;

    /**
     * 支付类型(0:支付宝,1:微信)
     */
    @Column(name = "WFT_PAYTYPE_TYPE", length = 2)
    private String wft_paytype_type;

    /**
     * 第三方交易码(只微信有)
     */
    @Column(name = "WFT_PAYTYPE_THIRDCODE", length = 20)
    private String wft_paytype_thirdcode;

    /**
     * 威富通返回的代码
     */
    @Column(name = "WFT_PAYTYPE_WFTCODE", length = 50)
    private String wft_paytype_wftcode;

    /**
     * 威富通返回状态
     */
    @Column(name = "WFT_PAYTYPE_BACKSTATE", length = 30)
    private String wft_paytype_backstate;

    /**
     * 威富通返回信息
     */
    @Column(name = "WFT_PAYTYPE_BACKMESS", length = 200)
    private String wft_paytype_backmess;

    /**
     * 发送时间
     */
    @Column(name = "WFT_PAYTYPE_SENDDATE")
    @Type(type = "java.util.Date")
    private Date wft_paytype_senddate;

    /**
     * 返回时间
     */
    @Column(name = "WFT_PAYTYPE_BACKDATE")
    @Type(type = "java.util.Date")
    private Date wft_paytype_backdate;

    public String getWft_paytype_id() {
        return wft_paytype_id;
    }

    public void setWft_paytype_id(String wft_paytype_id) {
        this.wft_paytype_id = wft_paytype_id;
    }

    public String getWft_paytype_wftshop_id() {
        return wft_paytype_wftshop_id;
    }

    public void setWft_paytype_wftshop_id(String wft_paytype_wftshop_id) {
        this.wft_paytype_wftshop_id = wft_paytype_wftshop_id;
    }

    public String getWft_paytype_name() {
        return wft_paytype_name;
    }

    public void setWft_paytype_name(String wft_paytype_name) {
        this.wft_paytype_name = wft_paytype_name;
    }

    public String getWft_paytype_code() {
        return wft_paytype_code;
    }

    public void setWft_paytype_code(String wft_paytype_code) {
        this.wft_paytype_code = wft_paytype_code;
    }

    public String getWft_paytype_bill() {
        return wft_paytype_bill;
    }

    public void setWft_paytype_bill(String wft_paytype_bill) {
        this.wft_paytype_bill = wft_paytype_bill;
    }

    public String getWft_paytype_type() {
        return wft_paytype_type;
    }

    public void setWft_paytype_type(String wft_paytype_type) {
        this.wft_paytype_type = wft_paytype_type;
    }

    public String getWft_paytype_thirdcode() {
        return wft_paytype_thirdcode;
    }

    public void setWft_paytype_thirdcode(String wft_paytype_thirdcode) {
        this.wft_paytype_thirdcode = wft_paytype_thirdcode;
    }

    public String getWft_paytype_wftcode() {
        return wft_paytype_wftcode;
    }

    public void setWft_paytype_wftcode(String wft_paytype_wftcode) {
        this.wft_paytype_wftcode = wft_paytype_wftcode;
    }

    public String getWft_paytype_backstate() {
        return wft_paytype_backstate;
    }

    public void setWft_paytype_backstate(String wft_paytype_backstate) {
        this.wft_paytype_backstate = wft_paytype_backstate;
    }

    public String getWft_paytype_backmess() {
        return wft_paytype_backmess;
    }

    public void setWft_paytype_backmess(String wft_paytype_backmess) {
        this.wft_paytype_backmess = wft_paytype_backmess;
    }

    public Date getWft_paytype_senddate() {
        return wft_paytype_senddate;
    }

    public void setWft_paytype_senddate(Date wft_paytype_senddate) {
        this.wft_paytype_senddate = wft_paytype_senddate;
    }

    public Date getWft_paytype_backdate() {
        return wft_paytype_backdate;
    }

    public void setWft_paytype_backdate(Date wft_paytype_backdate) {
        this.wft_paytype_backdate = wft_paytype_backdate;
    }
}
