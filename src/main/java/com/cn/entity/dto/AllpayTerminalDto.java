package com.cn.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONArray;

/**
 * pos终端管理dto
 * Created by WangWenFang on 2016/12/5.
 */
public class AllpayTerminalDto {

    @JsonProperty
    private String storeId;	//门店id

    @JsonProperty
    private String posId;	//posid

    @JsonProperty
    private String merchantId;	//所属商户id

    @JsonProperty
    private String termCode;	//终端编号8位

    @JsonProperty
    private String posType;	//Pos类型		1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos

    @JsonProperty
    private String payChannel;	//应用渠道（系统）(默认1(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)

    @JsonProperty
    private String branchId;	//Pos所属分公司id

    @JsonProperty
    private String posParterId;	//所属pos合作方id

    @JsonProperty
    private String peiJianPosParterId;	  // 配件所属pos合作方Id

    @JsonProperty
    private String userType;	      //角色 String(0网点管理,1网点业务,2商户管理)

    @JsonProperty
    private String merchaCode;	//商户编号（业务编号）

    @JsonProperty
    private Integer posStatus;	     //状态 是否启用

    @JsonProperty
    private Integer curragePage;	//当前页码

    @JsonProperty
    private Integer pageSize;	//每页显示记录条数

    @JsonProperty
    private String createStartTime;	//录入开始时间

    @JsonProperty
    private String createEndTime;	//录入结束时间

    @JsonProperty
    private Integer codeCount;	//批量生成终端条数

    @JsonProperty
    private String superTermCode;	//关联pos

    @JsonProperty
    private String shopuserName;	//指定人员账户

    @JsonProperty
    private String limitConfiguration;	//权限配置方案

    @JsonProperty
    private String userId;  //软POS对应用户id

    @JsonProperty
    private String userNameFromBusCookie;  //登录名称

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTermCode() {
        return termCode;
    }

    public void setTermCode(String termCode) {
        this.termCode = termCode;
    }

    public String getPosType() {
        return posType;
    }

    public void setPosType(String posType) {
        this.posType = posType;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getPosParterId() {
        return posParterId;
    }

    public void setPosParterId(String posParterId) {
        this.posParterId = posParterId;
    }

    public String getPeiJianPosParterId() {
        return peiJianPosParterId;
    }

    public void setPeiJianPosParterId(String peiJianPosParterId) {
        this.peiJianPosParterId = peiJianPosParterId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMerchaCode() {
        return merchaCode;
    }

    public void setMerchaCode(String merchaCode) {
        this.merchaCode = merchaCode;
    }

    public Integer getPosStatus() {
        return posStatus;
    }

    public void setPosStatus(Integer posStatus) {
        this.posStatus = posStatus;
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

    public String getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = createStartTime;
    }

    public String getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    public Integer getCodeCount() {
        return codeCount;
    }

    public void setCodeCount(Integer codeCount) {
        this.codeCount = codeCount;
    }

    public String getSuperTermCode() {
        return superTermCode;
    }

    public void setSuperTermCode(String superTermCode) {
        this.superTermCode = superTermCode;
    }

    public String getShopuserName() {
        return shopuserName;
    }

    public void setShopuserName(String shopuserName) {
        this.shopuserName = shopuserName;
    }

    public String getLimitConfiguration() {
        return limitConfiguration;
    }

    public void setLimitConfiguration(String limitConfiguration) {
        this.limitConfiguration = limitConfiguration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNameFromBusCookie() {
        return userNameFromBusCookie;
    }

    public void setUserNameFromBusCookie(String userNameFromBusCookie) {
        this.userNameFromBusCookie = userNameFromBusCookie;
    }

    @Override
    public String toString() {
        return "AllpayTerminalDto{" +
                "storeId='" + storeId + '\'' +
                ", posId='" + posId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", termCode='" + termCode + '\'' +
                ", posType='" + posType + '\'' +
                ", payChannel='" + payChannel + '\'' +
                ", branchId='" + branchId + '\'' +
                ", posParterId='" + posParterId + '\'' +
                ", peiJianPosParterId='" + peiJianPosParterId + '\'' +
                ", userType='" + userType + '\'' +
                ", merchaCode='" + merchaCode + '\'' +
                ", posStatus='" + posStatus + '\'' +
                ", curragePage='" + curragePage + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", createStartTime='" + createStartTime + '\'' +
                ", createEndTime='" + createEndTime + '\'' +
                ", codeCount='" + codeCount + '\'' +
                ", superTermCode='" + superTermCode + '\'' +
                ", shopuserName='" + shopuserName + '\'' +
                ", limitConfiguration='" + limitConfiguration + '\'' +
                ", userId='" + userId + '\'' +
                ", userNameFromBusCookie='" + userNameFromBusCookie + '\'' +
                '}';
    }
}
