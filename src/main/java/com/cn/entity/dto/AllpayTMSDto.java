package com.cn.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TMS终端管理dto
 * Created by WangWenFang on 2016/12/9.
 */
public class AllpayTMSDto {

    @JsonProperty
    private String tmsId;  //TMS信息ID

    @JsonProperty
    private String tmsTerminalname;	//厂商

    @JsonProperty
    private String tmsEnterterminalname;	//厂商名称

    @JsonProperty
    private String tmsModelsname;	//机型

    @JsonProperty
    private String tmsApplicationname;	//应用名称

    @JsonProperty
    private String tmsVernum;	//版本号

    @JsonProperty
    private String tmsFeilname;	//文件名称

    @JsonProperty
    private Integer tmsFeiltype;	//程序类型

    @JsonProperty
    private String tmsUpdatecontent;	  //更新内容

    @JsonProperty
    private Integer tmsState;	        //状态	（0:停用 1：启用）

    @JsonProperty
    private Integer curragePage;	//当前页码	从1开始计算

    @JsonProperty
    private Integer pageSize;	//每页显示记录条数

    @JsonProperty
    private String userNameFromBusCookie;  //登录名称

    public String getTmsId() {
        return tmsId;
    }

    public void setTmsId(String tmsId) {
        this.tmsId = tmsId;
    }

    public String getTmsTerminalname() {
        return tmsTerminalname;
    }

    public void setTmsTerminalname(String tmsTerminalname) {
        this.tmsTerminalname = tmsTerminalname;
    }

    public String getTmsEnterterminalname() {
        return tmsEnterterminalname;
    }

    public void setTmsEnterterminalname(String tmsEnterterminalname) {
        this.tmsEnterterminalname = tmsEnterterminalname;
    }

    public String getTmsModelsname() {
        return tmsModelsname;
    }

    public void setTmsModelsname(String tmsModelsname) {
        this.tmsModelsname = tmsModelsname;
    }

    public String getTmsApplicationname() {
        return tmsApplicationname;
    }

    public void setTmsApplicationname(String tmsApplicationname) {
        this.tmsApplicationname = tmsApplicationname;
    }

    public String getTmsVernum() {
        return tmsVernum;
    }

    public void setTmsVernum(String tmsVernum) {
        this.tmsVernum = tmsVernum;
    }

    public String getTmsFeilname() {
        return tmsFeilname;
    }

    public void setTmsFeilname(String tmsFeilname) {
        this.tmsFeilname = tmsFeilname;
    }

    public Integer getTmsFeiltype() {
        return tmsFeiltype;
    }

    public void setTmsFeiltype(Integer tmsFeiltype) {
        this.tmsFeiltype = tmsFeiltype;
    }

    public String getTmsUpdatecontent() {
        return tmsUpdatecontent;
    }

    public void setTmsUpdatecontent(String tmsUpdatecontent) {
        this.tmsUpdatecontent = tmsUpdatecontent;
    }

    public Integer getTmsState() {
        return tmsState;
    }

    public void setTmsState(Integer tmsState) {
        this.tmsState = tmsState;
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

    public String getUserNameFromBusCookie() {
        return userNameFromBusCookie;
    }

    public void setUserNameFromBusCookie(String userNameFromBusCookie) {
        this.userNameFromBusCookie = userNameFromBusCookie;
    }

    @Override
    public String toString() {
        return "AllpayTMSDto{" +
                "tmsId='" + tmsId + '\'' +
                ", tmsTerminalname='" + tmsTerminalname + '\'' +
                ", tmsEnterterminalname='" + tmsEnterterminalname + '\'' +
                ", tmsModelsname='" + tmsModelsname + '\'' +
                ", tmsApplicationname='" + tmsApplicationname + '\'' +
                ", tmsVernum='" + tmsVernum + '\'' +
                ", tmsFeilname='" + tmsFeilname + '\'' +
                ", tmsFeiltype='" + tmsFeiltype + '\'' +
                ", tmsUpdatecontent='" + tmsUpdatecontent + '\'' +
                ", tmsState='" + tmsState + '\'' +
                ", curragePage='" + curragePage + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", userNameFromBusCookie='" + userNameFromBusCookie + '\'' +
                '}';
    }
}
