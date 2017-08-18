package com.cn.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 业务签约管理dto
 * Created by WangWenFang on 2016/12/7.
 */
public class AllpayChannelSetDto {

    @JsonProperty
    private String merchantId;	//商户id

    @JsonProperty
    private String merchantName;	//商户名称

    @JsonProperty
    private String business;	//受理编号（channelSetId渠道配置表id）

    @JsonProperty
    private String merchantShortName;	//商户简称

    @JsonProperty
    private String startTime;	//受理开始时间

    @JsonProperty
    private String endTime;	//受理结束时间

    @JsonProperty
    private String channelCode;	//归属渠道

    @JsonProperty
    private Integer status;	//受理状态	（1审核中，2未审核，3已通过，4被驳回）

    @JsonProperty
    private String choose;	//启用状态	String（1启用，0 停用）

    @JsonProperty
    private Integer curragePage;	//当前页码	从1开始计算

    @JsonProperty
    private Integer pageSize;	//每页显示记录条数

    @JsonProperty
    private List<Map<String, Object>> channelSetList;  //渠道配置列表

    @JsonProperty
    private String channelName;	//渠道名称

    @JsonProperty
    private String companyId;	//分子公司

    @JsonProperty
    private String userNameFromBusCookie;  //登录名称

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public Integer getCurragePage() {
        return curragePage;
    }

    public void setCurragePage(Integer curragePage) {
        this.curragePage = curragePage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<Map<String, Object>> getChannelSetList() {
        return channelSetList;
    }

    public void setChannelSetList(List<Map<String, Object>> channelSetList) {
        this.channelSetList = channelSetList;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserNameFromBusCookie() {
        return userNameFromBusCookie;
    }

    public void setUserNameFromBusCookie(String userNameFromBusCookie) {
        this.userNameFromBusCookie = userNameFromBusCookie;
    }

    @Override
    public String toString() {
        return "AllpayChannelSetDto{" +
                "merchantId='" + merchantId + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", business='" + business + '\'' +
                ", merchantShortName='" + merchantShortName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", channelCode='" + channelCode + '\'' +
                ", status='" + status + '\'' +
                ", choose='" + choose + '\'' +
                ", channelSetList='" + channelSetList + '\'' +
                ", channelName='" + channelName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", userNameFromBusCookie='" + userNameFromBusCookie + '\'' +
                ", curragePage='" + curragePage + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';//
    }
}
