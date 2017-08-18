package com.cn.entity.po;

import com.cn.entity.publicFields.publicFields;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 渠道配置关系表
 * Created by cuixiaowei on 2016/11/29.
 */
@Entity
@Table(name="ALLPAY_CHANNELSET")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Allpay_ChannelSet extends publicFields {
    /**
     * 渠道配置关系表主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "CHANNELSET_ID", length = 32)
    private String channelset_id;
    /**
     * 商户id
     */
    @Column(name = "CHANNELSET_MERCHANT_ID", length = 32)
    private String channelset_merchant_id;
    /**
     * 渠道CODE
     */
    @Column(name = "CHANNELSET_CHANNEL_CODE", length = 32)
    private String channelset_channel_code;
    /**
     * 参数名称
     */
    @Column(name = "CHANNELSET_PARAMETER_NAME", length = 1000)
    private String channelset_parameter_name;
    /**
     * 参数值
     */
    @Column(name = "CHANNELSET_PARAMETER_VALUE", length = 2000)
    private String channelset_parameter_value;
    /**
     * 参数类型
     */
    @Column(name = "CHANNELSET_PARAMETER_TYPE", length = 32)
    private String channelset_parameter_type;
    /**
     * 参数扩展
     */
    @Column(name = "CHANNELSET_PARAMETER_EXPAND", length = 1000)
    private String channelset_parameter_expand;

    public String getChannelset_id() {
        return channelset_id;
    }

    public void setChannelset_id(String channelset_id) {
        this.channelset_id = channelset_id;
    }

    public String getChannelset_merchant_id() {
        return channelset_merchant_id;
    }

    public void setChannelset_merchant_id(String channelset_merchant_id) {
        this.channelset_merchant_id = channelset_merchant_id;
    }

    public String getChannelset_channel_code() {
        return channelset_channel_code;
    }

    public void setChannelset_channel_code(String channelset_channel_code) {
        this.channelset_channel_code = channelset_channel_code;
    }

    public String getChannelset_parameter_name() {
        return channelset_parameter_name;
    }

    public void setChannelset_parameter_name(String channelset_parameter_name) {
        this.channelset_parameter_name = channelset_parameter_name;
    }

    public String getChannelset_parameter_value() {
        return channelset_parameter_value;
    }

    public void setChannelset_parameter_value(String channelset_parameter_value) {
        this.channelset_parameter_value = channelset_parameter_value;
    }

    public String getChannelset_parameter_type() {
        return channelset_parameter_type;
    }

    public void setChannelset_parameter_type(String channelset_parameter_type) {
        this.channelset_parameter_type = channelset_parameter_type;
    }

    public String getChannelset_parameter_expand() {
        return channelset_parameter_expand;
    }

    public void setChannelset_parameter_expand(String channelset_parameter_expand) {
        this.channelset_parameter_expand = channelset_parameter_expand;
    }
}
