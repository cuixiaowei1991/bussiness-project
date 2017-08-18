package com.cn.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sun.yayi on 2016/12/8.
 */
public class AllpayPosParterDto {

    @JsonProperty
    private String parterId;
    @JsonProperty
    private String parterName; //合作方名称
    @JsonProperty
    private String parterShortName; //合作方简称
    @JsonProperty
    private String connectPerson; //	联系人
    @JsonProperty
    private String connectTel;	//联系方式
    @JsonProperty
    private String expandPeople;	//拓展人	       string
    @JsonProperty
    private String parterLevel;	//级别	string
    @JsonProperty
    private String branchId;	//所属分公司id	String
    @JsonProperty
    private String belongParterId;	//上级合作方id
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
    private String parterAddress;	//         地址	string
    @JsonProperty
    private String parterState;	    //   是否启用	0:停用 1：启用
    @JsonProperty
    private String curragePage;	//当前页码
    @JsonProperty
    private String pageSize;	//每页显示记录条数
    @JsonProperty
    private String isStart;	      //是否启用（0:停用 1：启用  空为全部）
    @JsonProperty
    private String createStartTime;	//录入开始时间
    @JsonProperty
    private String createEndTime;	//录入结束时间
    @JsonProperty
    private String userNameFromBusCookie;  //登录用户名

    @Override
    public String toString() {
        return "AllpayPosParterDto{" +
                "parterId='" + parterId + '\'' +
                ", parterName='" + parterName + '\'' +
                ", parterShortName='" + parterShortName + '\'' +
                ", connectPerson='" + connectPerson + '\'' +
                ", connectTel='" + connectTel + '\'' +
                ", expandPeople='" + expandPeople + '\'' +
                ", parterLevel='" + parterLevel + '\'' +
                ", branchId='" + branchId + '\'' +
                ", belongParterId='" + belongParterId + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", countryId='" + countryId + '\'' +
                ", countryName='" + countryName + '\'' +
                ", parterAddress='" + parterAddress + '\'' +
                ", parterState='" + parterState + '\'' +
                ", curragePage='" + curragePage + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", isStart='" + isStart + '\'' +
                ", createStartTime='" + createStartTime + '\'' +
                ", createEndTime='" + createEndTime + '\'' +
                ", userNameFromBusCookie='" + userNameFromBusCookie + '\'' +
                '}';
    }

    public String getParterId() {
        return parterId;
    }

    public void setParterId(String parterId) {
        this.parterId = parterId;
    }

    public String getParterName() {
        return parterName;
    }

    public void setParterName(String parterName) {
        this.parterName = parterName;
    }

    public String getParterShortName() {
        return parterShortName;
    }

    public void setParterShortName(String parterShortName) {
        this.parterShortName = parterShortName;
    }

    public String getConnectPerson() {
        return connectPerson;
    }

    public void setConnectPerson(String connectPerson) {
        this.connectPerson = connectPerson;
    }

    public String getConnectTel() {
        return connectTel;
    }

    public void setConnectTel(String connectTel) {
        this.connectTel = connectTel;
    }

    public String getExpandPeople() {
        return expandPeople;
    }

    public void setExpandPeople(String expandPeople) {
        this.expandPeople = expandPeople;
    }

    public String getParterLevel() {
        return parterLevel;
    }

    public void setParterLevel(String parterLevel) {
        this.parterLevel = parterLevel;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBelongParterId() {
        return belongParterId;
    }

    public void setBelongParterId(String belongParterId) {
        this.belongParterId = belongParterId;
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

    public String getParterAddress() {
        return parterAddress;
    }

    public void setParterAddress(String parterAddress) {
        this.parterAddress = parterAddress;
    }

    public String getParterState() {
        return parterState;
    }

    public void setParterState(String parterState) {
        this.parterState = parterState;
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
}


