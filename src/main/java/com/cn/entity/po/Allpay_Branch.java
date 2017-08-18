package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 分公司表
 * Created by cuixiaowei on 2016/11/29.
 */
@Entity
@Table(name="ALLPAY_BRANCH")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_Branch extends publicFields {
    /**
     * 分公司表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "BRANCH_ID", length = 32)
    private String branch_id;
    /**
     * 分公司名称
     */
    @Column(name = "BRANCH_NAME", length = 200)
    private String branch_name;
    /**
     * 1：分公司 0：总部
     */
    @Column(name = "BRANCH_TYPE", length = 1)
    private int branch_type;
    /**
     * 分公司负责人手机号
     */
    @Column(name = "BRANCH_MANAGERPHONENUMBER", length = 20)
    private String branch_managerphonenumber;
    /**
     * 分公司地址
     */
    @Column(name = "BRANCH_ADDRESS", length = 500)
    private String branch_address;
    /**
     * 分公司负责人
     */
    @Column(name = "BRANCH_MANAGER", length = 50)
    private String branch_manager;
    /**
     * 分公司状态
     */
    @Column(name = "BRANCH_STATE", length = 1)
    private int branch_state;

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public int getBranch_type() {
        return branch_type;
    }

    public void setBranch_type(int branch_type) {
        this.branch_type = branch_type;
    }

    public String getBranch_managerphonenumber() {
        return branch_managerphonenumber;
    }

    public void setBranch_managerphonenumber(String branch_managerphonenumber) {
        this.branch_managerphonenumber = branch_managerphonenumber;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
    }

    public String getBranch_manager() {
        return branch_manager;
    }

    public void setBranch_manager(String branch_manager) {
        this.branch_manager = branch_manager;
    }

    public int getBranch_state() {
        return branch_state;
    }

    public void setBranch_state(int branch_state) {
        this.branch_state = branch_state;
    }
}
