package com.cn.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

/**
 * Created by sun.yayi on 2016/12/5.
 */
public class AllpayAgentDto {
    @JsonProperty
    private String agentId;
    @JsonProperty
    private String agentName; //代理商名称
    @JsonProperty
    private String agentNum; //代理商编号
    @JsonProperty
    private String agentShortName; //代理商简称
    @JsonProperty
    private String legalPersonName; //	代理人名称	string
    @JsonProperty
    private String lagalPersonPhone;	//代理人手机号	string
    @JsonProperty
    private String shopPeopleName;	//拓展人	       string
    @JsonProperty
    private String level;	//级别	string
    @JsonProperty
    private String branchId;	//所属分公司id	String
    @JsonProperty
    private String agentParentId;	//上级代理id	string
    @JsonProperty
    private String provinceId;	//省ID	String
    @JsonProperty
    private String provinceName; 	//省名称	string
    @JsonProperty
    private String cityId;	        // 市ID	String
    @JsonProperty
    private String cityName;	    // 市名称	string
    @JsonProperty
    private String countryId;	    //     县ID	string
    @JsonProperty
    private String countryName;	        // 县名称	string
    @JsonProperty
    private String agentAddress;	//         地址	string
    @JsonProperty
    private String agentState;	    //   是否启用	0:停用 1：启用
    @JsonProperty
    private String isCreatePosParter;	//是否创建pos合作方	0：否 1：是
    @JsonProperty
    private String allpayid;
    @JsonProperty
    private String allpayKey;
    @JsonProperty
    private String curragePage;	//当前页码
    @JsonProperty
    private String pageSize;	//每页显示记录条数
    @JsonProperty
    private String agentType; //代理商类型 0：个人代理  ，1：公司代理
    @JsonProperty
    private String serious;  //是否物理删除 1是
    @JsonProperty
    private String isStart;	      //是否启用（0:停用 1：启用  空为全部）
    @JsonProperty
    private String createStartTime;	//录入开始时间
    @JsonProperty
    private String createEndTime;	//录入结束时间
    @JsonProperty
    private String userNameFromBusCookie;  //业务系统登录用户名

    @JsonProperty
    private String userNameFromAgentCookie;  //代理商系统登录名

    @Override
    public String toString() {
        return "AllpayAgentDto{" +
                "agentId='" + agentId + '\'' +
                ", agentName='" + agentName + '\'' +
                ", agentNum='" + agentNum + '\'' +
                ", agentShortName='" + agentShortName + '\'' +
                ", legalPersonName='" + legalPersonName + '\'' +
                ", lagalPersonPhone='" + lagalPersonPhone + '\'' +
                ", shopPeopleName='" + shopPeopleName + '\'' +
                ", level='" + level + '\'' +
                ", branchId='" + branchId + '\'' +
                ", agentParentId='" + agentParentId + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", countryId='" + countryId + '\'' +
                ", countryName='" + countryName + '\'' +
                ", agentAddress='" + agentAddress + '\'' +
                ", agentState='" + agentState + '\'' +
                ", isCreatePosParter='" + isCreatePosParter + '\'' +
                ", allpayid='" + allpayid + '\'' +
                ", allpayKey='" + allpayKey + '\'' +
                ", curragePage='" + curragePage + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", agentType='" + agentType + '\'' +
                ", serious='" + serious + '\'' +
                ", isStart='" + isStart + '\'' +
                ", createStartTime='" + createStartTime + '\'' +
                ", createEndTime='" + createEndTime + '\'' +
                ", userNameFromBusCookie='" + userNameFromBusCookie + '\'' +
                ", userNameFromAgentCookie='" + userNameFromAgentCookie + '\'' +
                '}';
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentShortName() {
        return agentShortName;
    }

    public String getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum;
    }

    public void setAgentShortName(String agentShortName) {
        this.agentShortName = agentShortName;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getLagalPersonPhone() {
        return lagalPersonPhone;
    }

    public void setLagalPersonPhone(String lagalPersonPhone) {
        this.lagalPersonPhone = lagalPersonPhone;
    }

    public String getShopPeopleName() {
        return shopPeopleName;
    }

    public void setShopPeopleName(String shopPeopleName) {
        this.shopPeopleName = shopPeopleName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getAgentParentId() {
        return agentParentId;
    }

    public void setAgentParentId(String agentParentId) {
        this.agentParentId = agentParentId;
    }

    public String getCurragePage() {
        return curragePage;
    }

    public void setCurragePage(String curragePage) {
        this.curragePage = curragePage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getAgentState() {
        return agentState;
    }

    public void setAgentState(String agentState) {
        this.agentState = agentState;
    }

    public String getIsCreatePosParter() {
        return isCreatePosParter;
    }

    public void setIsCreatePosParter(String isCreatePosParter) {
        this.isCreatePosParter = isCreatePosParter;
    }

    public String getAllpayid() {
        return allpayid;
    }

    public void setAllpayid(String allpayid) {
        this.allpayid = allpayid;
    }

    public String getAllpayKey() {
        return allpayKey;
    }

    public void setAllpayKey(String allpayKey) {
        this.allpayKey = allpayKey;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getSerious() {
        return serious;
    }

    public void setSerious(String serious) {
        this.serious = serious;
    }

    public String getUserNameFromBusCookie() {
        return userNameFromBusCookie;
    }

    public void setUserNameFromBusCookie(String userNameFromBusCookie) {
        this.userNameFromBusCookie = userNameFromBusCookie;
    }

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
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

    public String getUserNameFromAgentCookie() {
        return userNameFromAgentCookie;
    }

    public void setUserNameFromAgentCookie(String userNameFromAgentCookie) {
        this.userNameFromAgentCookie = userNameFromAgentCookie;
    }
}
