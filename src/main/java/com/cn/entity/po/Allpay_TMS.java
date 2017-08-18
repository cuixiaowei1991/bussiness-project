package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TMS信息表
 * Created by cuixiaowei on 2016/11/28.
 */
@Entity
@Table(name="ALLPAY_TMS")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_TMS extends publicFields {
    /**
     * tms表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "TMS_ID", length = 32)
    private String tms_id;
    /**
     * 厂商
     */
    @Column(name = "TMS_TERMINALNAME", length = 200)
    private String tms_terminalname;
    /**
     * 厂商名称
     */
    @Column(name = "TMS_ENTERTERMINALNAME", length = 200)
    private String tms_enterterminalname;
    /**
     * 应用名称
     */
    @Column(name = "TMS_APPLICATIONNAME", length = 200)
    private String tms_applicationname;
    /**
     * 版本号
     */
    @Column(name = "TMS_VERNUM", length = 50)
    private String tms_vernum;
    /**
     * 文件类型 0：主控，1：应用,2：参数
     */
    @Column(name = "TMS_FEILTYPE", length = 1)
    private int tms_feiltype;
    /**
     * 文件名
     */
    @Column(name = "TMS_FEILNAME", length = 200)
    private String tms_feilname;
    /**
     * 更新内容
     */
    @Column(name = "TMS_UPDATECONTENT", length = 200)
    private String tms_updatecontent;

    /**
     * 机型
     */
    @Column(name = "TMS_MODELSNAME", length = 200)
    private String tms_modelsname;
    /**
     * 启用状态 0：启用，1：禁用
     */
    @Column(name = "TMS_STATE", length = 1)
    private int tms_state;

    public String getTms_id() {
        return tms_id;
    }

    public void setTms_id(String tms_id) {
        this.tms_id = tms_id;
    }

    public String getTms_terminalname() {
        return tms_terminalname;
    }

    public void setTms_terminalname(String tms_terminalname) {
        this.tms_terminalname = tms_terminalname;
    }

    public String getTms_enterterminalname() {
        return tms_enterterminalname;
    }

    public void setTms_enterterminalname(String tms_enterterminalname) {
        this.tms_enterterminalname = tms_enterterminalname;
    }

    public String getTms_applicationname() {
        return tms_applicationname;
    }

    public void setTms_applicationname(String tms_applicationname) {
        this.tms_applicationname = tms_applicationname;
    }

    public String getTms_vernum() {
        return tms_vernum;
    }

    public void setTms_vernum(String tms_vernum) {
        this.tms_vernum = tms_vernum;
    }

    public int getTms_feiltype() {
        return tms_feiltype;
    }

    public void setTms_feiltype(int tms_feiltype) {
        this.tms_feiltype = tms_feiltype;
    }

    public String getTms_feilname() {
        return tms_feilname;
    }

    public void setTms_feilname(String tms_feilname) {
        this.tms_feilname = tms_feilname;
    }

    public String getTms_updatecontent() {
        return tms_updatecontent;
    }

    public void setTms_updatecontent(String tms_updatecontent) {
        this.tms_updatecontent = tms_updatecontent;
    }

    public String getTms_modelsname() {
        return tms_modelsname;
    }

    public void setTms_modelsname(String tms_modelsname) {
        this.tms_modelsname = tms_modelsname;
    }

    public int getTms_state() {
        return tms_state;
    }

    public void setTms_state(int tms_state) {
        this.tms_state = tms_state;
    }
}
