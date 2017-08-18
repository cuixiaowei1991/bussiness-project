package com.cn.entity.po;

import com.cn.common.CommonHelper;
import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by cuixiaowei on 2016/11/28.
 */
@Entity
@Table(name="ALLPAY_TERMINAL")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_Terminal extends publicFields {
    /**
     * 商户信息表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "TERMINAL_POSID", length = 32)
    private String terminal_posid;
    /**
     * 商户编号  （shopNumber）15位
     */
    @Column(name = "TERMINAL_MERCHCODE", length = 20)
    private String terminal_merchcode;
    /**
     * 门店ID
     */
    @Column(name = "TERMINAL_STORE_ID", length = 32)
    private String terminal_store_id;
    /**
     * 终端编号 8位
     */
    @Column(name = "TERMINAL_TERMCODE", length = 20)
    private String terminal_termcode;
    /**
     * 菜单权限
     */
    @Column(name = "TERMINAL_PERMISSION", length = 500)
    private String terminal_permission;
    /**
     * 1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos
     */
    @Column(name = "TERMINAL_POSTYPE", length = 1)
    private int terminal_postype;
    /**
     * 备注
     */
    @Column(name = "TERMINAL_POSNOTE", length = 1000)
    private String terminal_posnote;
    /**
     * pos所属分公司ID
     */
    @Column(name = "TERMINAL_BRANCHID", length = 32)
    private String terminal_branchid;
    /**
     * pos所属代理
     */
    @Column(name = "TERMINAL_AGENTID", length = 32)
    private String terminal_agentid;
    /**
     * pos所属pos合作方
     */
    @Column(name = "TERMINAL_POSPARTERID", length = 32)
    private String terminal_posparterid;
    /**
     * pos配件所属pos合作方
     */
    @Column(name = "TERMINAL_PEIJIANPOSPARTERID", length = 32)
    private String terminal_peijianposparterid;
    /**
     * 商户ID
     */
    @Column(name = "TERMINAL_MERCHANT_ID", length = 32)
    private String terminal_merchant_id;
    /**
     * 角色0网点管理,1网点业务,2商户管理
     */
    @Column(name = "TERMINAL_USERTYPE", length = 1)
    private int terminal_usertype;
    /**
     * 如果为“”，则表示无关联pos，如果为32位uuid，则表示关联pos的id
     */
    @Column(name = "TERMINAL_SUPERTERMCODE", length = 32)
    private String terminal_supertermcode;
    /**
     * 终端应用系统(默认1(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)
     */
    @Column(name = "TERMINAL_PAYCHANNEL", length = 200)
    private String terminal_paychannel;
    /**
     *  是否接收一码付消息推送  0不接收 1接收
     */
    @Column(name = "TERMINAL_USEAPPSENDMESS", length = 1)
    private String terminal_userappsendmess;

    /**
     * 软POS对应用户id
     */
    @Column(name = "TERMINAL_SHOPUSERID", length = 32)
    private String terminal_shopuserid;

    public String getTerminal_shopuserid() {
        return terminal_shopuserid;
    }

    public void setTerminal_shopuserid(String terminal_shopuserid) {
        this.terminal_shopuserid = terminal_shopuserid;
    }

    public String getTerminal_posid() {
        return terminal_posid;
    }

    public void setTerminal_posid(String terminal_posid) {
        this.terminal_posid = terminal_posid;
    }

    public String getTerminal_merchcode() {
        return terminal_merchcode;
    }

    public void setTerminal_merchcode(String terminal_merchcode) {
        this.terminal_merchcode = terminal_merchcode;
    }

    public String getTerminal_store_id() {
        return terminal_store_id;
    }

    public void setTerminal_store_id(String terminal_store_id) {
        this.terminal_store_id = terminal_store_id;
    }

    public String getTerminal_termcode() {
        return terminal_termcode;
    }

    public void setTerminal_termcode(String terminal_termcode) {
        this.terminal_termcode = terminal_termcode;
    }

    public String getTerminal_permission() {
        return terminal_permission;
    }

    public void setTerminal_permission(String terminal_permission) {
        if(!CommonHelper.isEmpty(terminal_permission)){
            this.terminal_permission = terminal_permission;
        }else{
            this.terminal_permission = "1111111111111111111111111111111111111111100000000000000000000000000000000000";   //pos默认菜单权限
        }
    }

    public int getTerminal_postype() {
        return terminal_postype;
    }

    public void setTerminal_postype(int terminal_postype) {
        this.terminal_postype = terminal_postype;
    }

    public String getTerminal_posnote() {
        return terminal_posnote;
    }

    public void setTerminal_posnote(String terminal_posnote) {
        this.terminal_posnote = terminal_posnote;
    }

    public String getTerminal_branchid() {
        return terminal_branchid;
    }

    public void setTerminal_branchid(String terminal_branchid) {
        this.terminal_branchid = terminal_branchid;
    }

    public String getTerminal_agentid() {
        return terminal_agentid;
    }

    public void setTerminal_agentid(String terminal_agentid) {
        this.terminal_agentid = terminal_agentid;
    }

    public String getTerminal_posparterid() {
        return terminal_posparterid;
    }

    public void setTerminal_posparterid(String terminal_posparterid) {
        this.terminal_posparterid = terminal_posparterid;
    }

    public String getTerminal_peijianposparterid() {
        return terminal_peijianposparterid;
    }

    public void setTerminal_peijianposparterid(String terminal_peijianposparterid) {
        this.terminal_peijianposparterid = terminal_peijianposparterid;
    }

    public String getTerminal_merchant_id() {
        return terminal_merchant_id;
    }

    public void setTerminal_merchant_id(String terminal_merchant_id) {
        this.terminal_merchant_id = terminal_merchant_id;
    }

    public int getTerminal_usertype() {
        return terminal_usertype;
    }

    public void setTerminal_usertype(int terminal_usertype) {
        this.terminal_usertype = terminal_usertype;
    }

    public String getTerminal_supertermcode() {
        return terminal_supertermcode;
    }

    public void setTerminal_supertermcode(String terminal_supertermcode) {
        this.terminal_supertermcode = terminal_supertermcode;
    }

    public String getTerminal_paychannel() {
        return terminal_paychannel;
    }

    public void setTerminal_paychannel(String terminal_paychannel) {
        this.terminal_paychannel = terminal_paychannel;
    }

    public String getTerminal_userappsendmess() {
        return terminal_userappsendmess;
    }

    public void setTerminal_userappsendmess(String terminal_userappsendmess) {
        this.terminal_userappsendmess = terminal_userappsendmess;
    }
}
