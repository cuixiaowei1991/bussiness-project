package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 威富通
 * Created by sun.yayi on 2017/1/9.
 */
@Entity

@Table(name="WFT_SHOP")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Wft_Shop extends publicFields {

    /**
     * 威富通表id主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "WFT_SHOP_ID", length = 32)
    private String wft_shop_id;

    /**
     * 商户名称
     */
    @Column(name = "WFT_SHOP_NAME", length = 1000)
    private String wft_shop_name;

    /**
     * 商户状态
     */
    @Column(name = "WFT_SHOP_STATE", length = 2)
    private String wft_shop_state;

    /**
     * 商户所走银行
     */
    @Column(name = "WFT_SHOP_BANKTYPE", length = 1000)
    private String wft_shop_banktype;

    /**
     * 外部商户号(业务系统商户表id)
     */
    @Column(name = "WFT_SHOP_OUTMERCHANTID", length = 32)
    private String wft_shop_outmerchantid;

    /**
     * 行业类别id
     */
    @Column(name = "WFT_SHOP_INDUSTRYID", length = 32)
    private String wft_shop_industryid;

    /**
     * 商户所属省份id
     */
    @Column(name = "WFT_SHOP_PROVINCEID", length = 32)
    private String wft_shop_provinceid;

    /**
     * 商户所属城市id
     */
    @Column(name = "WFT_SHOP_CITYID", length = 32)
    private String wft_shop_cityid;

    /**
     * 威富通返回信息
     */
    @Column(name = "WFT_SHOP_BACKMESS", length = 1000)
    private String wft_shop_backmess;

    /**
     * 发送时间
     */
    @Column(name = "WFT_SHOP_SENDDATE")
    @Type(type = "java.util.Date")
    private Date wft_shop_senddate;

    /**
     * 返回时间
     */
    @Column(name = "WFT_SHOP_BACKDATE")
    @Type(type = "java.util.Date")
    private Date wft_shop_backdate;

    /**
     * 商户地址
     */
    @Column(name = "WFT_SHOP_ADDRESS", length = 1000)
    private String wft_shop_address;

    /**
     * 商户负责人
     */
    @Column(name = "WFT_SHOP_PRINCIPAL", length = 100)
    private String wft_shop_principal;

    /**
     * 商户负责人电话
     */
    @Column(name = "WFT_SHOP_PRINCIPALTEL", length = 16)
    private String wft_shop_principaltel;

    /**
     * 商户负责人邮箱
     */
    @Column(name = "WFT_SHOP_PRINCIPALEMAIL", length = 50)
    private String wft_shop_principalemail;

    /**
     * 录入者
     */
    @Column(name = "WFT_SHOP_INTERPEOPLE", length = 50)
    private String wft_shop_interpeople;

    /**
     * 录入者id
     */
    @Column(name = "WFT_SHOP_INTERPEOPLEID", length = 32)
    private String WFT_SHOP_INTERPEOPLEID;

    /**
     * 威富通返回的商户号
     */
    @Column(name = "WFT_SHOP_MERCHANTID", length = 32)
    private String wft_shop_merchantid;

    /**
     * 银行卡号
     */
    @Column(name = "WFT_SHOP_BANKCARDNUM", length = 32)
    private String wft_shop_bankcardnum;

    /**
     * 开户银行id
     */
    @Column(name = "WFT_SHOP_BANKID", length = 32)
    private String wft_shop_bankid;

    /**
     * 银行卡持有人
     */
    @Column(name = "WFT_SHOP_BANKACCOUNTNAME", length = 500)
    private String wft_shop_bankaccountname;

    /**
     * 账户类型
     */
    @Column(name = "WFT_SHOP_BANKACCOUNTTYPE", length = 2)
    private String wft_shop_bankaccounttype;

    /**
     * 联行号
     */
    @Column(name = "WFT_SHOP_BANKCODEID", length = 32)
    private String wft_shop_bankcodeid;

    /**
     * 联行名称
     */
    @Column(name = "WFT_SHOP_BANKCODENAME", length = 1000)
    private String wft_shop_bankcodename;

    /**
     * 联行所在省份id
     */
    @Column(name = "WFT_SHOP_BANKCODEPROVICEID", length = 32)
    private String wft_shop_bankcodeproviceid;

    /**
     * 联行所在城市id
     */
    @Column(name = "WFT_SHOP_BANKCODECITYID", length = 32)
    private String wft_shop_bankcodecityid;

    /**
     * 持卡人证件类型
     */
    @Column(name = "WFT_SHOP_BANKIDCARDTYPE", length = 2)
    private String wft_shop_bankidcardtype;

    /**
     * 持卡人证件号
     */
    @Column(name = "WFT_SHOP_BANKIDCARD", length = 200)
    private String wft_shop_bankidcard;

    /**
     * 持卡人手机号
     */
    @Column(name = "WFT_SHOP_BANKACCOUNTTEL", length = 20)
    private String wft_shop_bankaccounttel;
    /**持卡人地址**/
    @Column(name = "WFT_SHOP_BANKACCOUNTADDRESS", length = 1000)
    private String wft_shop_bankAccountAddress;
    
    /**
     * 结算账户
     */
    @Column(name = "WFT_SHOP_SETTLEMENT", length = 1000)
    private String wft_shop_settlement;

    /**
     * 账户信息同步时间
     */
    @Column(name = "WFT_SHOP_UPDATEDATE")
    @Type(type = "java.util.Date")
    private Date wft_shop_updatedate;

    /**
     * 威富通返回账户id,修改账户信息用
     */
    @Column(name = "WFT_SHOP_WFTACCOUNTID", length = 32)
    private String wft_shop_wftaccountid;

    /**
     * 支付宝费率
     */
    @Column(name = "WFT_SHOP_ZFBBILL", length = 10)
    private String wft_shop_zfbbill;

    /**
     * 微信费率
     */
    @Column(name = "WFT_SHOP_WXBILL", length = 10)
    private String wft_shop_wxbill;

    /**
     * 第三方交易码(只微信有)
     */
    @Column(name = "WFT_SHOP_THIRDCODE", length = 20)
    private String wft_shop_thirdcode;

    /**
     * 威富通商户秘钥
     * @return
     */
    @Column(name = "WFT_SHOP_SECRETKEY ", length = 100)
    private String wft_shop_secretKey;

    /**
     * 威富通商户appid
     * @return
     */
    @Column(name = "WFT_SHOP_APPID ", length = 50)
    private String wft_shop_appid;

    public String getWft_shop_secretKey() {
        return wft_shop_secretKey;
    }

    public void setWft_shop_secretKey(String wft_shop_secretKey) {
        this.wft_shop_secretKey = wft_shop_secretKey;
    }

    public String getWft_shop_appid() {
        return wft_shop_appid;
    }

    public void setWft_shop_appid(String wft_shop_appid) {
        this.wft_shop_appid = wft_shop_appid;
    }

    public String getWft_shop_id() {
        return wft_shop_id;
    }

    public void setWft_shop_id(String wft_shop_id) {
        this.wft_shop_id = wft_shop_id;
    }

    public String getWft_shop_name() {
        return wft_shop_name;
    }

    public void setWft_shop_name(String wft_shop_name) {
        this.wft_shop_name = wft_shop_name;
    }

    public String getWft_shop_state() {
        return wft_shop_state;
    }

    public void setWft_shop_state(String wft_shop_state) {
        this.wft_shop_state = wft_shop_state;
    }

    public String getWft_shop_banktype() {
        return wft_shop_banktype;
    }

    public void setWft_shop_banktype(String wft_shop_banktype) {
        this.wft_shop_banktype = wft_shop_banktype;
    }

    public String getWft_shop_outmerchantid() {
        return wft_shop_outmerchantid;
    }

    public void setWft_shop_outmerchantid(String wft_shop_outmerchantid) {
        this.wft_shop_outmerchantid = wft_shop_outmerchantid;
    }

    public String getWft_shop_industryid() {
        return wft_shop_industryid;
    }

    public void setWft_shop_industryid(String wft_shop_industryid) {
        this.wft_shop_industryid = wft_shop_industryid;
    }

    public String getWft_shop_provinceid() {
        return wft_shop_provinceid;
    }

    public void setWft_shop_provinceid(String wft_shop_provinceid) {
        this.wft_shop_provinceid = wft_shop_provinceid;
    }

    public String getWft_shop_cityid() {
        return wft_shop_cityid;
    }

    public void setWft_shop_cityid(String wft_shop_cityid) {
        this.wft_shop_cityid = wft_shop_cityid;
    }

    public String getWft_shop_backmess() {
        return wft_shop_backmess;
    }

    public void setWft_shop_backmess(String wft_shop_backmess) {
        this.wft_shop_backmess = wft_shop_backmess;
    }

    public Date getWft_shop_senddate() {
        return wft_shop_senddate;
    }

    public void setWft_shop_senddate(Date wft_shop_senddate) {
        this.wft_shop_senddate = wft_shop_senddate;
    }

    public Date getWft_shop_backdate() {
        return wft_shop_backdate;
    }
    
    public String getWft_shop_bankAccountAddress() {
		return wft_shop_bankAccountAddress;
	}

	public void setWft_shop_bankAccountAddress(String wft_shop_bankAccountAddress) {
		this.wft_shop_bankAccountAddress = wft_shop_bankAccountAddress;
	}

	public void setWft_shop_backdate(Date wft_shop_backdate) {
        this.wft_shop_backdate = wft_shop_backdate;
    }

    public String getWft_shop_address() {
        return wft_shop_address;
    }

    public void setWft_shop_address(String wft_shop_address) {
        this.wft_shop_address = wft_shop_address;
    }

    public String getWft_shop_principal() {
        return wft_shop_principal;
    }

    public void setWft_shop_principal(String wft_shop_principal) {
        this.wft_shop_principal = wft_shop_principal;
    }

    public String getWft_shop_principaltel() {
        return wft_shop_principaltel;
    }

    public void setWft_shop_principaltel(String wft_shop_principaltel) {
        this.wft_shop_principaltel = wft_shop_principaltel;
    }

    public String getWft_shop_principalemail() {
        return wft_shop_principalemail;
    }

    public void setWft_shop_principalemail(String wft_shop_principalemail) {
        this.wft_shop_principalemail = wft_shop_principalemail;
    }

    public String getWft_shop_interpeople() {
        return wft_shop_interpeople;
    }

    public void setWft_shop_interpeople(String wft_shop_interpeople) {
        this.wft_shop_interpeople = wft_shop_interpeople;
    }

    public String getWFT_SHOP_INTERPEOPLEID() {
        return WFT_SHOP_INTERPEOPLEID;
    }

    public void setWFT_SHOP_INTERPEOPLEID(String WFT_SHOP_INTERPEOPLEID) {
        this.WFT_SHOP_INTERPEOPLEID = WFT_SHOP_INTERPEOPLEID;
    }

    public String getWft_shop_merchantid() {
        return wft_shop_merchantid;
    }

    public void setWft_shop_merchantid(String wft_shop_merchantid) {
        this.wft_shop_merchantid = wft_shop_merchantid;
    }

    public String getWft_shop_bankcardnum() {
        return wft_shop_bankcardnum;
    }

    public void setWft_shop_bankcardnum(String wft_shop_bankcardnum) {
        this.wft_shop_bankcardnum = wft_shop_bankcardnum;
    }

    public String getWft_shop_bankid() {
        return wft_shop_bankid;
    }

    public void setWft_shop_bankid(String wft_shop_bankid) {
        this.wft_shop_bankid = wft_shop_bankid;
    }

    public String getWft_shop_bankaccountname() {
        return wft_shop_bankaccountname;
    }

    public void setWft_shop_bankaccountname(String wft_shop_bankaccountname) {
        this.wft_shop_bankaccountname = wft_shop_bankaccountname;
    }

    public String getWft_shop_bankaccounttype() {
        return wft_shop_bankaccounttype;
    }

    public void setWft_shop_bankaccounttype(String wft_shop_bankaccounttype) {
        this.wft_shop_bankaccounttype = wft_shop_bankaccounttype;
    }

    public String getWft_shop_bankcodeid() {
        return wft_shop_bankcodeid;
    }

    public void setWft_shop_bankcodeid(String wft_shop_bankcodeid) {
        this.wft_shop_bankcodeid = wft_shop_bankcodeid;
    }

    public String getWft_shop_bankcodename() {
        return wft_shop_bankcodename;
    }

    public void setWft_shop_bankcodename(String wft_shop_bankcodename) {
        this.wft_shop_bankcodename = wft_shop_bankcodename;
    }

    public String getWft_shop_bankcodeproviceid() {
        return wft_shop_bankcodeproviceid;
    }

    public void setWft_shop_bankcodeproviceid(String wft_shop_bankcodeproviceid) {
        this.wft_shop_bankcodeproviceid = wft_shop_bankcodeproviceid;
    }

    public String getWft_shop_bankcodecityid() {
        return wft_shop_bankcodecityid;
    }

    public void setWft_shop_bankcodecityid(String wft_shop_bankcodecityid) {
        this.wft_shop_bankcodecityid = wft_shop_bankcodecityid;
    }

    public String getWft_shop_bankidcardtype() {
        return wft_shop_bankidcardtype;
    }

    public void setWft_shop_bankidcardtype(String wft_shop_bankidcardtype) {
        this.wft_shop_bankidcardtype = wft_shop_bankidcardtype;
    }

    public String getWft_shop_bankidcard() {
        return wft_shop_bankidcard;
    }

    public void setWft_shop_bankidcard(String wft_shop_bankidcard) {
        this.wft_shop_bankidcard = wft_shop_bankidcard;
    }

    public String getWft_shop_bankaccounttel() {
        return wft_shop_bankaccounttel;
    }

    public void setWft_shop_bankaccounttel(String wft_shop_bankaccounttel) {
        this.wft_shop_bankaccounttel = wft_shop_bankaccounttel;
    }

    public String getWft_shop_settlement() {
        return wft_shop_settlement;
    }

    public void setWft_shop_settlement(String wft_shop_settlement) {
        this.wft_shop_settlement = wft_shop_settlement;
    }

    public Date getWft_shop_updatedate() {
        return wft_shop_updatedate;
    }

    public void setWft_shop_updatedate(Date wft_shop_updatedate) {
        this.wft_shop_updatedate = wft_shop_updatedate;
    }

    public String getWft_shop_wftaccountid() {
        return wft_shop_wftaccountid;
    }

    public void setWft_shop_wftaccountid(String wft_shop_wftaccountid) {
        this.wft_shop_wftaccountid = wft_shop_wftaccountid;
    }

    public String getWft_shop_zfbbill() {
        return wft_shop_zfbbill;
    }

    public void setWft_shop_zfbbill(String wft_shop_zfbbill) {
        this.wft_shop_zfbbill = wft_shop_zfbbill;
    }

    public String getWft_shop_wxbill() {
        return wft_shop_wxbill;
    }

    public void setWft_shop_wxbill(String wft_shop_wxbill) {
        this.wft_shop_wxbill = wft_shop_wxbill;
    }

    public String getWft_shop_thirdcode() {
        return wft_shop_thirdcode;
    }

    public void setWft_shop_thirdcode(String wft_shop_thirdcode) {
        this.wft_shop_thirdcode = wft_shop_thirdcode;
    }
}
