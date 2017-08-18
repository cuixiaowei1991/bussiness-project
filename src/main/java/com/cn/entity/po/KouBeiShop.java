package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 口碑门店表
 * Created by sun.yayi on 2017/1/9.
 */


@Entity
@Table(name="KOUBEISHOP")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class KouBeiShop extends publicFields {

    /**
     * 口碑表id主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "KOUBEISHOPE_ID", length = 32)
    private String koubeishope_id;

    /**
     * 对应门店表id
     */
    @Column(name = "KOUBEISHOP_STORE_ID", length = 32)
    private String koubeishop_store_id;

    /**
     * 主门店名
     */
    @Column(name = "KOUBEISHOP_MAINNAME", length = 100)
    private String koubeishop_mainname;

    /**
     * 分门店名
     */
    @Column(name = "KOUBEISHOP_NAME", length = 100)
    private String koubeishop_name;

    /**
     * 审核失败原因
     */
    @Column(name = "KOUBEISHOP_AUDITMESS", length = 300)
    private String koubeishop_auditmess;

    /**
     * 支付宝返回的口碑id
     */
    @Column(name = "KOUBEISHOP_KOUBEIID", length = 50)
    private String koubeishop_koubeiid;

    /**
     * 支付宝返回的口碑流水号
     */
    @Column(name = "KOUBEISHOP_KOUBEINUM", length = 50)
    private String koubeishop_koubeinum;

    /**
     * 品牌名
     */
    @Column(name = "KOUBEISHOP_BRANDNAME", length = 50)
    private String koubeishop_brandname;

    /**
     * 一级类目编号
     */
    @Column(name = "KOUBEISHOP_FIRSTCATEGORYNUM", length = 20)
    private String koubeishop_firstcategorynum;

    /**
     * 一级类目名称
     */
    @Column(name = "KOUBEISHOP_FIRSTCATEGORYNAME", length = 200)
    private String koubeishop_firstcategoryname;

    /**
     *二级类目编号
     */
    @Column(name = "KOUBEISHOP_SECONDCATEGORYNUM", length = 20)
    private String koubeishop_secondcategorynum;

    /**
     * 二级类目名称
     */
    @Column(name = "KOUBEISHOP_SECONDCATEGORYNAME", length = 200)
    private String koubeishop_secondcategoryname;

    /**
     * 三级类目编号
     */
    @Column(name = "KOUBEISHOP_THREEDCATEGORYNUM", length = 20)
    private String koubeishop_threedcategorynum;

    /**
     * 三级类目名称
     */
    @Column(name = "KOUBEISHOP_THREEDCATEGORYNAME", length = 200)
    private String koubeishop_threedcategoryname;

    /**
     * 品牌logo
     */
    @Column(name = "KOUBEISHOP_BRANDLOGO", length = 200)
    private String koubeishop_brandlogo;

    /**
     * 所属省份id
     */
    @Column(name = "KOUBEISHOP_PROVINCEID", length = 32)
    private String koubeishop_provinceid;

    /**
     * 所属省份名称
     */
    @Column(name = "KOUBEISHOP_PROVINCENAME", length = 15)
    private String koubeishop_provincename;

    /**
     * 所属城市id
     */
    @Column(name = "KOUBEISHOP_CITYID", length = 32)
    private String koubeishop_cityid;

    /**
     * 所属城市名称
     */
    @Column(name = "KOUBEISHOP_CITYNAME", length = 15)
    private String koubeishop_cityname;

    /**
     * 所属县级id
     */
    @Column(name = "KOUBEISHOP_COUNTYID", length = 32)
    private String koubeishop_countyid;

    /**
     * 所属县级名称
     */
    @Column(name = "KOUBEISHOP_COUNTYNAME", length = 15)
    private String koubeishop_countyname;

    /**
     * 地址
     */
    @Column(name = "KOUBEISHOP_ADDRESS", length = 200)
    private String koubeishop_address;

    /**
     * 经度
     */
    @Column(name = "KOUBEISHOP_LONGITUDE", length = 20)
    private String koubeishop_longitude;

    /**
     * 纬度
     */
    @Column(name = "KOUBEISHOP_LATITUDE", length = 20)
    private String koubeishop_latitude;

    /**
     * 门店电话
     */
    @Column(name = "KOUBEISHOP_SHOPTEL", length = 20)
    private String koubeishop_shoptel;

    /**
     * 营业时间
     */
    @Column(name = "KOUBEISHOP_BUSINESSTIME", length = 20)
    private String koubeishop_businesstime;

    /**
     * 人均消费
     */
    @Column(name = "KOUBEISHOP_AVGPRICE", length = 10)
    private String koubeishop_avgprice;

    /**
     * 是否有WiFi  T有,F没有
     */
    @Column(name = "KOUBEISHOP_WIFI", length = 5)
    private String koubeishop_wifi;

    /**
     * 是否有停车场 T有,F没有
     */
    @Column(name = "KOUBEISHOP_PARKING", length = 5)
    private String koubeishop_parking;

    /**
     * 是否有无烟区  T有,F没有
     */
    @Column(name = "KOUBEISHOP_NOSMOKING", length = 5)
    private String koubeishop_nosmoking;

    /**
     * 是否有包厢  T有,F没有
     */
    @Column(name = "KOUBEISHOP_BOX", length = 5)
    private String koubeishop_box;

    /**
     * 其他服务信息
     */
    @Column(name = "KOUBEISHOP_VALUEADDED", length = 200)
    private String koubeishop_valueadded;

    /**
     * 门店首图
     */
    @Column(name = "KOUBEISHOP_MAINIMG", length = 200)
    private String koubeishop_mainimg;

    /**
     * 门头照
     */
    @Column(name = "KOUBEISHOP_SHOPHEADIMG", length = 200)
    private String koubeishop_shopheadimg;

    /**
     * 内部照1
     */
    @Column(name = "KOUBEISHOP_INDOORIMG1", length = 200)
    private String koubeishop_indoorimg1;

    /**
     * 内部照2
     */
    @Column(name = "KOUBEISHOP_INDOORIMG2", length = 200)
    private String KOUBEISHOP_INDOORIMG2;

    /**
     * 营业执照
     */
    @Column(name = "KOUBEISHOP_LICENCEIMG", length = 200)
    private String koubeishop_licenceimg;

    /**
     * 营业执照编码
     */
    @Column(name = "KOUBEISHOP_LICENCECODE", length = 50)
    private String koubeishop_licencecode;

    /**
     * 营业执照名称
     */
    @Column(name = "KOUBEISHOP_LICENCENAME", length = 60)
    private String koubeishop_licencename;

    /**
     * 经营许可证
     */
    @Column(name = "KOUBEISHOP_CERTIFICATE", length = 200)
    private String koubeishop_businesscertificate;

    /**
     * 经营许可证有效期(xxxx-xx-xx)
     */
    @Column(name = "KOUBEISHOP_TIFICATEEXPIRES", length = 20)
    private String koubeishop_businesscertificateexpires;

    /**
     * 授权函
     */
    @Column(name = "KOUBEISHOP_AUTHLETTER", length = 200)
    private String koubeishop_authletter;

    /**
     * 开发者返佣id
     */
    @Column(name = "KOUBEISHOP_ISVUID", length = 30)
    private String koubeishop_isvuid;

    /**
     * 收款成功短信接收手机号
     */
    @Column(name = "KOUBEISHOP_NOTIFYMOBILE", length = 20)
    private String koubeishop_notifymobile;

    /**
     * 品牌logo在支付宝的id
     */
    @Column(name = "KOUBEISHOP_BRANDLOGO_ZFB", length = 100)
    private String koubeishop_brandlogo_zfb;

    /**
     * 经营许可证在支付宝的id
     */
    @Column(name = "KOUBEISHOP_CERTIFICATE_ZFB", length = 100)
    private String koubeishop_businesscertificate_zfb;

    /**
     * 门店首图在支付宝的id
     */
    @Column(name = "KOUBEISHOP_MAINIMG_ZFB", length = 100)
    private String koubeishop_mainimg_zfb;

    /**
     * 门头照在支付宝的id
     */
    @Column(name = "KOUBEISHOP_SHOPHEADIMG_ZFB", length = 100)
    private String koubeishop_shopheadimg_zfb;

    /**
     * 内部照1在支付宝的id
     */
    @Column(name = "KOUBEISHOP_INDOORIMG1_ZFB", length = 100)
    private String koubeishop_indoorimg1_zfb;

    /**
     * 内部照2在支付宝的id
     */
    @Column(name = "KOUBEISHOP_INDOORIMG2_ZFB", length = 100)
    private String koubeishop_indoorimg2_zfb;

    /**
     * 营业执照在支付宝的id
     */
    @Column(name = "KOUBEISHOP_LICENCEIMG_ZFB", length = 100)
    private String koubeishop_licenceimg_zfb;

    /**
     * 授权函在支付宝的id
     */
    @Column(name = "KOUBEISHOP_AUTHLETTER_ZFB", length = 100)
    private String koubeishop_authletter_zfb;

    public String getKoubeishop_authletter_zfb() {
        return koubeishop_authletter_zfb;
    }

    public void setKoubeishop_authletter_zfb(String koubeishop_authletter_zfb) {
        this.koubeishop_authletter_zfb = koubeishop_authletter_zfb;
    }

    public String getKoubeishope_id() {
        return koubeishope_id;
    }

    public void setKoubeishope_id(String koubeishope_id) {
        this.koubeishope_id = koubeishope_id;
    }

    public String getKoubeishop_store_id() {
        return koubeishop_store_id;
    }

    public void setKoubeishop_store_id(String koubeishop_store_id) {
        this.koubeishop_store_id = koubeishop_store_id;
    }

    public String getKoubeishop_mainname() {
        return koubeishop_mainname;
    }

    public void setKoubeishop_mainname(String koubeishop_mainname) {
        this.koubeishop_mainname = koubeishop_mainname;
    }

    public String getKoubeishop_name() {
        return koubeishop_name;
    }

    public void setKoubeishop_name(String koubeishop_name) {
        this.koubeishop_name = koubeishop_name;
    }

    public String getKoubeishop_auditmess() {
        return koubeishop_auditmess;
    }

    public void setKoubeishop_auditmess(String koubeishop_auditmess) {
        this.koubeishop_auditmess = koubeishop_auditmess;
    }

    public String getKoubeishop_koubeiid() {
        return koubeishop_koubeiid;
    }

    public void setKoubeishop_koubeiid(String koubeishop_koubeiid) {
        this.koubeishop_koubeiid = koubeishop_koubeiid;
    }

    public String getKoubeishop_koubeinum() {
        return koubeishop_koubeinum;
    }

    public void setKoubeishop_koubeinum(String koubeishop_koubeinum) {
        this.koubeishop_koubeinum = koubeishop_koubeinum;
    }

    public String getKoubeishop_brandname() {
        return koubeishop_brandname;
    }

    public void setKoubeishop_brandname(String koubeishop_brandname) {
        this.koubeishop_brandname = koubeishop_brandname;
    }

    public String getKoubeishop_firstcategorynum() {
        return koubeishop_firstcategorynum;
    }

    public void setKoubeishop_firstcategorynum(String koubeishop_firstcategorynum) {
        this.koubeishop_firstcategorynum = koubeishop_firstcategorynum;
    }

    public String getKoubeishop_firstcategoryname() {
        return koubeishop_firstcategoryname;
    }

    public void setKoubeishop_firstcategoryname(String koubeishop_firstcategoryname) {
        this.koubeishop_firstcategoryname = koubeishop_firstcategoryname;
    }

    public String getKoubeishop_secondcategorynum() {
        return koubeishop_secondcategorynum;
    }

    public void setKoubeishop_secondcategorynum(String koubeishop_secondcategorynum) {
        this.koubeishop_secondcategorynum = koubeishop_secondcategorynum;
    }

    public String getKoubeishop_secondcategoryname() {
        return koubeishop_secondcategoryname;
    }

    public void setKoubeishop_secondcategoryname(String koubeishop_secondcategoryname) {
        this.koubeishop_secondcategoryname = koubeishop_secondcategoryname;
    }

    public String getKoubeishop_threedcategorynum() {
        return koubeishop_threedcategorynum;
    }

    public void setKoubeishop_threedcategorynum(String koubeishop_threedcategorynum) {
        this.koubeishop_threedcategorynum = koubeishop_threedcategorynum;
    }

    public String getKoubeishop_threedcategoryname() {
        return koubeishop_threedcategoryname;
    }

    public void setKoubeishop_threedcategoryname(String koubeishop_threedcategoryname) {
        this.koubeishop_threedcategoryname = koubeishop_threedcategoryname;
    }

    public String getKoubeishop_brandlogo() {
        return koubeishop_brandlogo;
    }

    public void setKoubeishop_brandlogo(String koubeishop_brandlogo) {
        this.koubeishop_brandlogo = koubeishop_brandlogo;
    }

    public String getKoubeishop_provinceid() {
        return koubeishop_provinceid;
    }

    public void setKoubeishop_provinceid(String koubeishop_provinceid) {
        this.koubeishop_provinceid = koubeishop_provinceid;
    }

    public String getKoubeishop_provincename() {
        return koubeishop_provincename;
    }

    public void setKoubeishop_provincename(String koubeishop_provincename) {
        this.koubeishop_provincename = koubeishop_provincename;
    }

    public String getKoubeishop_cityid() {
        return koubeishop_cityid;
    }

    public void setKoubeishop_cityid(String koubeishop_cityid) {
        this.koubeishop_cityid = koubeishop_cityid;
    }

    public String getKoubeishop_cityname() {
        return koubeishop_cityname;
    }

    public void setKoubeishop_cityname(String koubeishop_cityname) {
        this.koubeishop_cityname = koubeishop_cityname;
    }

    public String getKoubeishop_countyid() {
        return koubeishop_countyid;
    }

    public void setKoubeishop_countyid(String koubeishop_countyid) {
        this.koubeishop_countyid = koubeishop_countyid;
    }

    public String getKoubeishop_countyname() {
        return koubeishop_countyname;
    }

    public void setKoubeishop_countyname(String koubeishop_countyname) {
        this.koubeishop_countyname = koubeishop_countyname;
    }

    public String getKoubeishop_address() {
        return koubeishop_address;
    }

    public void setKoubeishop_address(String koubeishop_address) {
        this.koubeishop_address = koubeishop_address;
    }

    public String getKoubeishop_longitude() {
        return koubeishop_longitude;
    }

    public void setKoubeishop_longitude(String koubeishop_longitude) {
        this.koubeishop_longitude = koubeishop_longitude;
    }

    public String getKoubeishop_latitude() {
        return koubeishop_latitude;
    }

    public void setKoubeishop_latitude(String koubeishop_latitude) {
        this.koubeishop_latitude = koubeishop_latitude;
    }

    public String getKoubeishop_shoptel() {
        return koubeishop_shoptel;
    }

    public void setKoubeishop_shoptel(String koubeishop_shoptel) {
        this.koubeishop_shoptel = koubeishop_shoptel;
    }

    public String getKoubeishop_businesstime() {
        return koubeishop_businesstime;
    }

    public void setKoubeishop_businesstime(String koubeishop_businesstime) {
        this.koubeishop_businesstime = koubeishop_businesstime;
    }

    public String getKoubeishop_avgprice() {
        return koubeishop_avgprice;
    }

    public void setKoubeishop_avgprice(String koubeishop_avgprice) {
        this.koubeishop_avgprice = koubeishop_avgprice;
    }

    public String getKoubeishop_wifi() {
        return koubeishop_wifi;
    }

    public void setKoubeishop_wifi(String koubeishop_wifi) {
        this.koubeishop_wifi = koubeishop_wifi;
    }

    public String getKoubeishop_parking() {
        return koubeishop_parking;
    }

    public void setKoubeishop_parking(String koubeishop_parking) {
        this.koubeishop_parking = koubeishop_parking;
    }

    public String getKoubeishop_nosmoking() {
        return koubeishop_nosmoking;
    }

    public void setKoubeishop_nosmoking(String koubeishop_nosmoking) {
        this.koubeishop_nosmoking = koubeishop_nosmoking;
    }

    public String getKoubeishop_box() {
        return koubeishop_box;
    }

    public void setKoubeishop_box(String koubeishop_box) {
        this.koubeishop_box = koubeishop_box;
    }

    public String getKoubeishop_valueadded() {
        return koubeishop_valueadded;
    }

    public void setKoubeishop_valueadded(String koubeishop_valueadded) {
        this.koubeishop_valueadded = koubeishop_valueadded;
    }

    public String getKoubeishop_mainimg() {
        return koubeishop_mainimg;
    }

    public void setKoubeishop_mainimg(String koubeishop_mainimg) {
        this.koubeishop_mainimg = koubeishop_mainimg;
    }

    public String getKoubeishop_shopheadimg() {
        return koubeishop_shopheadimg;
    }

    public void setKoubeishop_shopheadimg(String koubeishop_shopheadimg) {
        this.koubeishop_shopheadimg = koubeishop_shopheadimg;
    }

    public String getKoubeishop_indoorimg1() {
        return koubeishop_indoorimg1;
    }

    public void setKoubeishop_indoorimg1(String koubeishop_indoorimg1) {
        this.koubeishop_indoorimg1 = koubeishop_indoorimg1;
    }

    public String getKOUBEISHOP_INDOORIMG2() {
        return KOUBEISHOP_INDOORIMG2;
    }

    public void setKOUBEISHOP_INDOORIMG2(String KOUBEISHOP_INDOORIMG2) {
        this.KOUBEISHOP_INDOORIMG2 = KOUBEISHOP_INDOORIMG2;
    }

    public String getKoubeishop_licenceimg() {
        return koubeishop_licenceimg;
    }

    public void setKoubeishop_licenceimg(String koubeishop_licenceimg) {
        this.koubeishop_licenceimg = koubeishop_licenceimg;
    }

    public String getKoubeishop_licencecode() {
        return koubeishop_licencecode;
    }

    public void setKoubeishop_licencecode(String koubeishop_licencecode) {
        this.koubeishop_licencecode = koubeishop_licencecode;
    }

    public String getKoubeishop_licencename() {
        return koubeishop_licencename;
    }

    public void setKoubeishop_licencename(String koubeishop_licencename) {
        this.koubeishop_licencename = koubeishop_licencename;
    }

    public String getKoubeishop_businesscertificate() {
        return koubeishop_businesscertificate;
    }

    public void setKoubeishop_businesscertificate(String koubeishop_businesscertificate) {
        this.koubeishop_businesscertificate = koubeishop_businesscertificate;
    }

    public String getKoubeishop_businesscertificateexpires() {
        return koubeishop_businesscertificateexpires;
    }

    public void setKoubeishop_businesscertificateexpires(String koubeishop_businesscertificateexpires) {
        this.koubeishop_businesscertificateexpires = koubeishop_businesscertificateexpires;
    }

    public String getKoubeishop_authletter() {
        return koubeishop_authletter;
    }

    public void setKoubeishop_authletter(String koubeishop_authletter) {
        this.koubeishop_authletter = koubeishop_authletter;
    }

    public String getKoubeishop_isvuid() {
        return koubeishop_isvuid;
    }

    public void setKoubeishop_isvuid(String koubeishop_isvuid) {
        this.koubeishop_isvuid = koubeishop_isvuid;
    }

    public String getKoubeishop_notifymobile() {
        return koubeishop_notifymobile;
    }

    public void setKoubeishop_notifymobile(String koubeishop_notifymobile) {
        this.koubeishop_notifymobile = koubeishop_notifymobile;
    }

    public String getKoubeishop_brandlogo_zfb() {
        return koubeishop_brandlogo_zfb;
    }

    public void setKoubeishop_brandlogo_zfb(String koubeishop_brandlogo_zfb) {
        this.koubeishop_brandlogo_zfb = koubeishop_brandlogo_zfb;
    }

    public String getKoubeishop_businesscertificate_zfb() {
        return koubeishop_businesscertificate_zfb;
    }

    public void setKoubeishop_businesscertificate_zfb(String koubeishop_businesscertificate_zfb) {
        this.koubeishop_businesscertificate_zfb = koubeishop_businesscertificate_zfb;
    }

    public String getKoubeishop_mainimg_zfb() {
        return koubeishop_mainimg_zfb;
    }

    public void setKoubeishop_mainimg_zfb(String koubeishop_mainimg_zfb) {
        this.koubeishop_mainimg_zfb = koubeishop_mainimg_zfb;
    }

    public String getKoubeishop_shopheadimg_zfb() {
        return koubeishop_shopheadimg_zfb;
    }

    public void setKoubeishop_shopheadimg_zfb(String koubeishop_shopheadimg_zfb) {
        this.koubeishop_shopheadimg_zfb = koubeishop_shopheadimg_zfb;
    }

    public String getKoubeishop_indoorimg1_zfb() {
        return koubeishop_indoorimg1_zfb;
    }

    public void setKoubeishop_indoorimg1_zfb(String koubeishop_indoorimg1_zfb) {
        this.koubeishop_indoorimg1_zfb = koubeishop_indoorimg1_zfb;
    }

    public String getKoubeishop_indoorimg2_zfb() {
        return koubeishop_indoorimg2_zfb;
    }

    public void setKoubeishop_indoorimg2_zfb(String koubeishop_indoorimg2_zfb) {
        this.koubeishop_indoorimg2_zfb = koubeishop_indoorimg2_zfb;
    }

    public String getKoubeishop_licenceimg_zfb() {
        return koubeishop_licenceimg_zfb;
    }

    public void setKoubeishop_licenceimg_zfb(String koubeishop_licenceimg_zfb) {
        this.koubeishop_licenceimg_zfb = koubeishop_licenceimg_zfb;
    }
}
