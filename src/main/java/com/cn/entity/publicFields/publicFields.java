package com.cn.entity.publicFields;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by cuixiaowei on 2016/11/14.
 */
@MappedSuperclass
public class publicFields {

    /**
     * 创建人
     */
    @Column(name = "ALLPAY_CREATER", length = 1000)
    private String ALLPAY_CREATER;
    /**
     * 创建时间
     */
    @Column(name = "ALLPAY_CREATETIME")
    @Type(type = "java.util.Date")
    private Date ALLPAY_CREATETIME;

    /**
     * 修改人
     */
    @Column(name = "ALLPAY_UPDATER", length = 1000)
    private String ALLPAY_UPDATER;

    /**
     * 修改时间
     */
    @Column(name = "ALLPAY_UPDATETIME")
    @Type(type = "java.util.Date")
    private Date ALLPAY_UPDATETIME;

    /**
     * 逻辑删除标记  1---未删除 2---已删除
     */
    @Column(name = "ALLPAY_LOGICDEL", length = 1)
    private String ALLPAY_LOGICDEL;

    /**
     * 操作日志记录(人物-时间-具体操作，不做清空，追加信息)
     */
    @Column(name = "ALLPAY_LOGRECORD", length = 4000)
    private String ALLPAY_LOGRECORD;
    /**
     *  0:停用 1：审核通过2:合同到期  3、4:预注册 5:待审核 6:驳回(商户)
     *  AUDITING:审核中（0），AUDIT_FAILED：审核驳回（1），AUDIT_SUCCESS：审核通过（2），INIT-初始（3），PROCESS-处理中（0），SUCCESS-成功（2），FAIL-失败（1） （口碑）
     */
    @Column(name = "ALLPAY_STATE", length = 1)
    private int ALLPAY_STATE;

    /**
     *  1---启用  0---停用
     */
    @Column(name = "ALLPAY_ISSTART", length = 1)
    private int ALLPAY_ISSTART;

    public String getALLPAY_CREATER() {
        return ALLPAY_CREATER;
    }

    public void setALLPAY_CREATER(String ALLPAY_CREATER) {
        this.ALLPAY_CREATER = ALLPAY_CREATER;
    }

    public Date getALLPAY_CREATETIME() {
        return ALLPAY_CREATETIME;
    }

    public void setALLPAY_CREATETIME(Date ALLPAY_CREATETIME) {
        this.ALLPAY_CREATETIME = ALLPAY_CREATETIME;
    }

    public String getALLPAY_UPDATER() {
        return ALLPAY_UPDATER;
    }

    public void setALLPAY_UPDATER(String ALLPAY_UPDATER) {
        this.ALLPAY_UPDATER = ALLPAY_UPDATER;
    }

    public Date getALLPAY_UPDATETIME() {
        return ALLPAY_UPDATETIME;
    }

    public void setALLPAY_UPDATETIME(Date ALLPAY_UPDATETIME) {
        this.ALLPAY_UPDATETIME = ALLPAY_UPDATETIME;
    }

    public String getALLPAY_LOGICDEL() {
        return ALLPAY_LOGICDEL;
    }

    public void setALLPAY_LOGICDEL(String ALLPAY_LOGICDEL) {
        this.ALLPAY_LOGICDEL = ALLPAY_LOGICDEL;
    }

    public String getALLPAY_LOGRECORD() {
        return ALLPAY_LOGRECORD;
    }

    public void setALLPAY_LOGRECORD(String ALLPAY_LOGRECORD) {
        this.ALLPAY_LOGRECORD = ALLPAY_LOGRECORD;
    }


    public int getALLPAY_ISSTART() {
        return ALLPAY_ISSTART;
    }

    public void setALLPAY_ISSTART(int ALLPAY_ISSTART) {
        this.ALLPAY_ISSTART = ALLPAY_ISSTART;
    }

    public int getALLPAY_STATE() {
        return ALLPAY_STATE;
    }

    public void setALLPAY_STATE(int ALLPAY_STATE) {
        this.ALLPAY_STATE = ALLPAY_STATE;
    }
}
