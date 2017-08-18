package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 三方关系表
 * Created by cuixiaowei on 2016/11/29.
 */
@Entity
@Table(name="ALLPAY_THIRDPARTY")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_Thirdparty extends publicFields {
    /**
     * 三方关系表UUID
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "THIRDPARTY_ID", length = 32)
    private String thirdparty_id;
    /**
     * 网点ID
     */
    @Column(name = "THIRDPARTY_STORE_ID", length = 32)
    private String thirdparty_store_id;
    /**
     * 三方平台端id
     */
    @Column(name = "THIRDPARTY_THIRD_ID", length = 100)
    private String thirdparty_third_id;
    /**
     * 渠道标记 码表
     */
    @Column(name = "THIRDPARTY_QVDAOSIGN", length = 100)
    private String thirdparty_qvdaosign;

    public String getThirdparty_id() {
        return thirdparty_id;
    }

    public void setThirdparty_id(String thirdparty_id) {
        this.thirdparty_id = thirdparty_id;
    }

    public String getThirdparty_store_id() {
        return thirdparty_store_id;
    }

    public void setThirdparty_store_id(String thirdparty_store_id) {
        this.thirdparty_store_id = thirdparty_store_id;
    }

    public String getThirdparty_third_id() {
        return thirdparty_third_id;
    }

    public void setThirdparty_third_id(String thirdparty_third_id) {
        this.thirdparty_third_id = thirdparty_third_id;
    }

    public String getThirdparty_qvdaosign() {
        return thirdparty_qvdaosign;
    }

    public void setThirdparty_qvdaosign(String thirdparty_qvdaosign) {
        this.thirdparty_qvdaosign = thirdparty_qvdaosign;
    }
}
