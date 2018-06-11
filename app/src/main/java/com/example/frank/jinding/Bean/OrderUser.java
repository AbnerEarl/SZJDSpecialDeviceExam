package com.example.frank.jinding.Bean;

import java.io.Serializable;

/**
 * Created by DELL on 2017/12/13.
 */

public class OrderUser implements Serializable {
    private String area;
    private String  authorizedUnit;
    private String agent;
    private String phone;
    private String fax;
    private String useUnit;
    private String installUnit;
    private String payWay;
    private String getReportWay;
    private String projectName;
    private String expectCheckDate;
    private String projectAddress;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAuthorizedUnit() {
        return authorizedUnit;
    }

    public void setAuthorizedUnit(String authorizedUnit) {
        this.authorizedUnit = authorizedUnit;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getUseUnit() {
        return useUnit;
    }

    public void setUseUnit(String useUnit) {
        this.useUnit = useUnit;
    }

    public String getInstallUnit() {
        return installUnit;
    }

    public void setInstallUnit(String installUnit) {
        this.installUnit = installUnit;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getGetReportWay() {
        return getReportWay;
    }

    public void setGetReportWay(String getReportWay) {
        this.getReportWay = getReportWay;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getExpectCheckDate() {
        return expectCheckDate;
    }

    public void setExpectCheckDate(String expectCheckDate) {
        this.expectCheckDate = expectCheckDate;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public OrderUser(String area, String authorizedUnit, String agent, String phone, String fax, String useUnit, String installUnit, String payWay, String getReportWay, String projectName, String expectCheckDate, String projectAddress) {
        this.area = area;
        this.authorizedUnit = authorizedUnit;
        this.agent = agent;
        this.phone = phone;
        this.fax = fax;
        this.useUnit = useUnit;
        this.installUnit = installUnit;
        this.payWay = payWay;
        this.getReportWay = getReportWay;
        this.projectName = projectName;
        this.expectCheckDate = expectCheckDate;
        this.projectAddress = projectAddress;
    }

    public OrderUser() {
    }

    public OrderUser(String danwei,String projectName,String projectAddress,String expectCheckDate){
        this.authorizedUnit=danwei;
        this.projectName=projectName;
        this.projectAddress=projectAddress;
        this.expectCheckDate=expectCheckDate;
    }
}
