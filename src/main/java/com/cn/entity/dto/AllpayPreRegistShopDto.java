package com.cn.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sun.yayi on 2017/2/8.
 */
public class AllpayPreRegistShopDto {

    @JsonProperty
    private String startCTime; //录入时间开始时间
    @JsonProperty
    private String endCTime; //录入时间结束时间
    @JsonProperty
    private String branchCompanyName; //分公司全称

    @JsonProperty
    private String storeprovinceName;   //省份名称

    @JsonProperty
    private String shopState;  //商户类别

    @JsonProperty
    private String curragePage;	//当前页码
    @JsonProperty
    private String pageSize;	//每页显示记录条数

    @Override
    public String toString() {
        return "AllpayPreRegistShopDto{" +
                "startCTime='" + startCTime + '\'' +
                ", endCTime='" + endCTime + '\'' +
                ", branchCompanyName='" + branchCompanyName + '\'' +
                ", storeprovinceName='" + storeprovinceName + '\'' +
                ", shopState='" + shopState + '\'' +
                ", curragePage='" + curragePage + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
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

    public String getStartCTime() {
        return startCTime;
    }

    public void setStartCTime(String startCTime) {
        this.startCTime = startCTime;
    }

    public String getEndCTime() {
        return endCTime;
    }

    public void setEndCTime(String endCTime) {
        this.endCTime = endCTime;
    }

    public String getBranchCompanyName() {
        return branchCompanyName;
    }

    public void setBranchCompanyName(String branchCompanyName) {
        this.branchCompanyName = branchCompanyName;
    }

    public String getStoreprovinceName() {
        return storeprovinceName;
    }

    public void setStoreprovinceName(String storeprovinceName) {
        this.storeprovinceName = storeprovinceName;
    }

    public String getShopState() {
        return shopState;
    }

    public void setShopState(String shopState) {
        this.shopState = shopState;
    }
}
