package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 威富通联行号表
 * Created by sun.yayi on 2017/1/9.
 */

@Entity
@Table(name="WFT_BANKCODE")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Wft_Bankcode extends publicFields {

    /**
     *  威富通联行号表id主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "WFT_BANKCODE_ID", length = 32)
    private String wft_bankcode_id;

    /**
     * 威富通联行号
     */
    @Column(name = "WFT_BANKCODE_BANKCODE", length = 50)
    private String wft_bankcode_bankcode;

    /**
     * 威富通联行名称
     */
    @Column(name = "WFT_BANKCODE_BANKNAME", length = 200)
    private String wft_bankcode_bankname;

    public String getWft_bankcode_id() {
        return wft_bankcode_id;
    }

    public void setWft_bankcode_id(String wft_bankcode_id) {
        this.wft_bankcode_id = wft_bankcode_id;
    }

    public String getWft_bankcode_bankcode() {
        return wft_bankcode_bankcode;
    }

    public void setWft_bankcode_bankcode(String wft_bankcode_bankcode) {
        this.wft_bankcode_bankcode = wft_bankcode_bankcode;
    }

    public String getWft_bankcode_bankname() {
        return wft_bankcode_bankname;
    }

    public void setWft_bankcode_bankname(String wft_bankcode_bankname) {
        this.wft_bankcode_bankname = wft_bankcode_bankname;
    }
}
