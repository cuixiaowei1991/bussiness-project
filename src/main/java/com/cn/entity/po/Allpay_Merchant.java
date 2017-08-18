package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 商户信息表
 * Created by cuixiaowei on 2016/11/24.
 */
@Entity
@Table(name="ALLPAY_MERCHANT")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_Merchant extends publicFields {
    /**
     * 商户信息表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "MERCHANT_ID", length = 32)
    private String merchant_id;
    /**
     * 商户名称
     */
    @Column(name = "MERCHANT_MERCHNAME", length = 200)
    private String merchant_merchname;
    /**
     * 商户9位编号
     */
    @Column(name = "MERCHANT_SHOPNUMBER", length = 20)
    private String merchant_shopnumber;
    /**
     * 商户简称
     */
    @Column(name = "MERCHANT_SHOPSHORTNAME", length = 200)
    private String merchant_shopshoptname;
    /**
     * 商户地址
     */
    @Column(name = "MERCHANT_ADDRESS", length = 500)
    private String merchant_address;
    /**
     * 行业名称
     */
    @Column(name = "MERCHANT_INDUSTRY", length = 200)
    private String merchant_industry;
    /**
     * 行业代码
     */
    @Column(name = "MERCHANT_INDUSTRYNUM", length = 32)
    private String merchant_industrynum;
    /**
     * 省代码
     */
    @Column(name = "MERCHANT_PROVINCEID", length = 32)
    private String merchant_provinceid;
    /**
     * 省名称
     */
    @Column(name = "MERCHANT_PROVINCENAME", length = 100)
    private String merchant_provincename;
    /**
     * 市代码
     */
    @Column(name = "MERCHANT_CITYID", length = 32)
    private String merchant_cityid;
    /**
     * 市名称
     */
    @Column(name = "MERCHANT_CITYNAME", length = 100)
    private String merchant_cityname;
    /**
     * 区县代码
     */
    @Column(name = "MERCHANT_COUNTRY_ID", length = 32)
    private String merchant_country_id;
    /**
     * 区县名称
     */
    @Column(name = "MERCHANT_COUNTRY_NAME", length = 100)
    private String merchant_country_name;
    /**
     * 所属分公司ID
     */
    @Column(name = "MERCHANT_BRANCHCOMPANYID", length = 32  )
    private String merchant_branchcompanyid;
    /**
     * 分公司名称
     */
    @Column(name = "MERCHANT_BRANCHCOMPANYNAME", length = 200  )
    private String merchant_branchcompanyname;
    /**
     * 商户图片logo（目前只支持一张）
     */
    @Column(name = "MERCHANT_IMGURL", length = 500  )
    private String merchant_imgurl;
    /**
     * 代理ID
     */
    @Column(name = "MERCHANT_AGENTID", length = 32  )
    private String merchant_agentid;
    /**
     * 是否是大集商户,0不是,1是
     */
    @Column(name = "MERCHANT_ISBIGMARKET", length = 1  )
    private int merchant_isbigmarket;
    /**
     * pos小票二维码信息
     */
    @Column(name = "MERCHANT_SHOPQRCODE", length = 500  )
    private String merchant_shopqrcode;
    /**
     * pos广告语
     */
    @Column(name = "MERCHANT_SHOPADVERTISING", length = 500  )
    private String merchant_shopadvertising;
    /**
     * 二维码广告使用起始价格
     */
    @Column(name = "MERCHANT_SHOPQRSTARTPRICE", length = 100  )
    private int merchant_shopqrstartprice;
    /**
     * 二维码广告使用封顶价格
     */
    @Column(name = "MERCHANT_SHOPQRENDPRICE", length = 100  )
    private int merchant_shopqrendprice;
    /**
     * 二维码广告使用起始日期(yyyyMMddHHmmss)
     */
    @Column(name = "MERCHANT_SHOPQRSTARTDATE")
    @Type(type = "java.util.Date")
    private Date merchant_shopqrstartdate;
    /**
     * 二维码广告使用结束日期(yyyyMMddHHmmss)
     */
    @Column(name = "MERCHANT_SHOPQRENDDATE")
    @Type(type = "java.util.Date")
    private Date merchant_shopqrenddate;
    /**
     * 商户已开通业务渠道
     * 数据库存储格式:
     * [{"channelCode":"26","channelName":"飞惠"},{"channelCode":"27","channelName":"飞惠01"}]
     */
    @Column(name = "MERCHANT_SHOPOPENCHANNEL", length = 4000  )
    private String merchant_shopopenchannel;
    /**
     * 是否创建门店 IS_CREATE_STORE
     * 1:是
     * 0:否
     * **/
    @Column(name = "IS_CREATE_STORE",length=1)
    private Integer createStore;

    /**
     * 商户联系人手机号
     * @return
     */
    @Column(name = "MERCHANT_CONTACTSPHONE", length = 50  )
    private String merchant_contactsphone;
    /**
     * 商户联系人姓名
     * @return
     */
    @Column(name = "MERCHANT_CONTACTSNAME", length = 50  )
    private String merchant_contactsname;
    /**
     * 驳回记录
     * @return
     */
    @Column(name = "MERCHANT_REJECTREMARK", length = 4000  )
    private String merchant_rejectremark;


    public String getMerchant_contactsphone() {
        return merchant_contactsphone;
    }

    public void setMerchant_contactsphone(String merchant_contactsphone) {
        this.merchant_contactsphone = merchant_contactsphone;
    }

    public String getMerchant_contactsname() {
        return merchant_contactsname;
    }

    public void setMerchant_contactsname(String merchant_contactsname) {
        this.merchant_contactsname = merchant_contactsname;
    }

    public String getMerchant_rejectremark() {
        return merchant_rejectremark;
    }

    public void setMerchant_rejectremark(String merchant_rejectremark) {
        this.merchant_rejectremark = merchant_rejectremark;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_merchname() {
        return merchant_merchname;
    }

    public void setMerchant_merchname(String merchant_merchname) {
        this.merchant_merchname = merchant_merchname;
    }

    public String getMerchant_shopnumber() {
        return merchant_shopnumber;
    }

    public void setMerchant_shopnumber(String merchant_shopnumber) {
        this.merchant_shopnumber = merchant_shopnumber;
    }

    public String getMerchant_shopshoptname() {
        return merchant_shopshoptname;
    }

    public void setMerchant_shopshoptname(String merchant_shopshoptname) {
        this.merchant_shopshoptname = merchant_shopshoptname;
    }

    public String getMerchant_address() {
        return merchant_address;
    }

    public void setMerchant_address(String merchant_address) {
        this.merchant_address = merchant_address;
    }

    public String getMerchant_industry() {
        return merchant_industry;
    }

    public void setMerchant_industry(String merchant_industry) {
        this.merchant_industry = merchant_industry;
    }

    public String getMerchant_industrynum() {
        return merchant_industrynum;
    }

    public void setMerchant_industrynum(String merchant_industrynum) {
        this.merchant_industrynum = merchant_industrynum;
    }

    public String getMerchant_provinceid() {
        return merchant_provinceid;
    }

    public void setMerchant_provinceid(String merchant_provinceid) {
        this.merchant_provinceid = merchant_provinceid;
    }

    public String getMerchant_provincename() {
        return merchant_provincename;
    }

    public void setMerchant_provincename(String merchant_provincename) {
        this.merchant_provincename = merchant_provincename;
    }

    public String getMerchant_cityid() {
        return merchant_cityid;
    }

    public void setMerchant_cityid(String merchant_cityid) {
        this.merchant_cityid = merchant_cityid;
    }

    public String getMerchant_cityname() {
        return merchant_cityname;
    }

    public void setMerchant_cityname(String merchant_cityname) {
        this.merchant_cityname = merchant_cityname;
    }

    public String getMerchant_country_id() {
        return merchant_country_id;
    }

    public void setMerchant_country_id(String merchant_country_id) {
        this.merchant_country_id = merchant_country_id;
    }

    public String getMerchant_country_name() {
        return merchant_country_name;
    }

    public void setMerchant_country_name(String merchant_country_name) {
        this.merchant_country_name = merchant_country_name;
    }

    public String getMerchant_branchcompanyid() {
        return merchant_branchcompanyid;
    }

    public void setMerchant_branchcompanyid(String merchant_branchcompanyid) {
        this.merchant_branchcompanyid = merchant_branchcompanyid;
    }

    public String getMerchant_branchcompanyname() {
        return merchant_branchcompanyname;
    }

    public void setMerchant_branchcompanyname(String merchant_branchcompanyname) {
        this.merchant_branchcompanyname = merchant_branchcompanyname;
    }

    public String getMerchant_imgurl() {
        return merchant_imgurl;
    }

    public void setMerchant_imgurl(String merchant_imgurl) {
        this.merchant_imgurl = merchant_imgurl;
    }

    public String getMerchant_agentid() {
        return merchant_agentid;
    }

    public void setMerchant_agentid(String merchant_agentid) {
        this.merchant_agentid = merchant_agentid;
    }

    public int getMerchant_isbigmarket() {
        return merchant_isbigmarket;
    }

    public void setMerchant_isbigmarket(int merchant_isbigmarket) {
        this.merchant_isbigmarket = merchant_isbigmarket;
    }

    public String getMerchant_shopqrcode() {
        return merchant_shopqrcode;
    }

    public void setMerchant_shopqrcode(String merchant_shopqrcode) {
        this.merchant_shopqrcode = merchant_shopqrcode;
    }

    public String getMerchant_shopadvertising() {
        return merchant_shopadvertising;
    }

    public void setMerchant_shopadvertising(String merchant_shopadvertising) {
        this.merchant_shopadvertising = merchant_shopadvertising;
    }

    public int getMerchant_shopqrstartprice() {
        return merchant_shopqrstartprice;
    }

    public void setMerchant_shopqrstartprice(int merchant_shopqrstartprice) {
        this.merchant_shopqrstartprice = merchant_shopqrstartprice;
    }

    public int getMerchant_shopqrendprice() {
        return merchant_shopqrendprice;
    }

    public void setMerchant_shopqrendprice(int merchant_shopqrendprice) {
        this.merchant_shopqrendprice = merchant_shopqrendprice;
    }

    public Date getMerchant_shopqrstartdate() {
        return merchant_shopqrstartdate;
    }

    public void setMerchant_shopqrstartdate(Date merchant_shopqrstartdate) {
        this.merchant_shopqrstartdate = merchant_shopqrstartdate;
    }

    public Date getMerchant_shopqrenddate() {
        return merchant_shopqrenddate;
    }

    public void setMerchant_shopqrenddate(Date merchant_shopqrenddate) {
        this.merchant_shopqrenddate = merchant_shopqrenddate;
    }

    public String getMerchant_shopopenchannel() {
        return merchant_shopopenchannel;
    }

    public void setMerchant_shopopenchannel(String merchant_shopopenchannel) {
        this.merchant_shopopenchannel = merchant_shopopenchannel;
    }

	public Integer getCreateStore() {
		return createStore;
	}

	public void setCreateStore(Integer createStore) {
		this.createStore = createStore;
	}
}
