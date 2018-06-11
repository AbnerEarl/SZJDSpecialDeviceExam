package com.example.frank.jinding.Bean.OrderBean;

import com.example.frank.jinding.Bean.Consignment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class CheckOrder implements Serializable {
    private String orderId;

    private String orderCode;

    private String orderOrg;

    private String orderOrgId;

    private String applicantName;

    private String applicantId;

    private String applicantPhonenum;

    private String checkPhonenum;

    private String deviceUseOrg;

    private String useContactor;

    private String usePhoneNumber;

    private String deviceInstallOrg;

    private String installContactor;

    private String installPhoneNumber;

    private String projectName;

    private String projectAddress;

    private Date checkdateExpect;

    private String checkerLoginnames;

    private String paymentType;

    private String getReportType;

    private String contactPersonId;

    private String payeeId;

    private String salesmanId;

    private String checkPersonId;

    private String dirUrl;

    private String orderStatus;

    private String province;

    private String city;

    private String area;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    private String userEvaluate;

    public String getUserEvaluate() {
        return userEvaluate;
    }

    public void setUserEvaluate(String userEvaluate) {
        this.userEvaluate = userEvaluate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    private List<Consignment> consignmentList;

    public List<Consignment> getConsignmentList() {
        return consignmentList;
    }

    public void setConsignmentList(List<Consignment> consignmentList) {
        this.consignmentList = consignmentList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public String getOrderOrg() {
        return orderOrg;
    }

    public void setOrderOrg(String orderOrg) {
        this.orderOrg = orderOrg == null ? null : orderOrg.trim();
    }

    public String getOrderOrgId() {
        return orderOrgId;
    }

    public void setOrderOrgId(String orderOrgId) {
        this.orderOrgId = orderOrgId == null ? null : orderOrgId.trim();
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName == null ? null : applicantName.trim();
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId == null ? null : applicantId.trim();
    }

    public String getApplicantPhonenum() {
        return applicantPhonenum;
    }

    public void setApplicantPhonenum(String applicantPhonenum) {
        this.applicantPhonenum = applicantPhonenum == null ? null : applicantPhonenum.trim();
    }

    public String getCheckPhonenum() {
        return checkPhonenum;
    }

    public void setCheckPhonenum(String checkPhonenum) {
        this.checkPhonenum = checkPhonenum == null ? null : checkPhonenum.trim();
    }

    public String getDeviceUseOrg() {
        return deviceUseOrg;
    }

    public void setDeviceUseOrg(String deviceUseOrg) {
        this.deviceUseOrg = deviceUseOrg == null ? null : deviceUseOrg.trim();
    }

    public String getUseContactor() {
        return useContactor;
    }

    public void setUseContactor(String useContactor) {
        this.useContactor = useContactor == null ? null : useContactor.trim();
    }

    public String getDeviceInstallOrg() {
        return deviceInstallOrg;
    }

    public void setDeviceInstallOrg(String deviceInstallOrg) {
        this.deviceInstallOrg = deviceInstallOrg == null ? null : deviceInstallOrg.trim();
    }

    public String getInstallContactor() {
        return installContactor;
    }

    public void setInstallContactor(String installContactor) {
        this.installContactor = installContactor == null ? null : installContactor.trim();
    }

    public String getInstallPhoneNumber() {
        return installPhoneNumber;
    }

    public void setInstallPhoneNumber(String installPhoneNumber) {
        this.installPhoneNumber = installPhoneNumber == null ? null : installPhoneNumber.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress == null ? null : projectAddress.trim();
    }

    public Date getCheckdateExpect() {
        return checkdateExpect;
    }

    public void setCheckdateExpect(Date checkdateExpect) {
        this.checkdateExpect = checkdateExpect;
    }

    public String getCheckerLoginnames() {
        return checkerLoginnames;
    }

    public void setCheckerLoginnames(String checkerLoginnames) {
        this.checkerLoginnames = checkerLoginnames == null ? null : checkerLoginnames.trim();
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType == null ? null : paymentType.trim();
    }

    public String getGetReportType() {
        return getReportType;
    }

    public void setGetReportType(String getReportType) {
        this.getReportType = getReportType == null ? null : getReportType.trim();
    }

    public String getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(String contactPersonId) {
        this.contactPersonId = contactPersonId == null ? null : contactPersonId.trim();
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId == null ? null : payeeId.trim();
    }

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId == null ? null : salesmanId.trim();
    }

    public CheckOrder() {
    }

    public String getCheckPersonId() {
        return checkPersonId;
    }

    public void setCheckPersonId(String checkPersonId) {
        this.checkPersonId = checkPersonId == null ? null : checkPersonId.trim();
    }

    public String getDirUrl() {
        return dirUrl;
    }

    public void setDirUrl(String dirUrl) {
        this.dirUrl = dirUrl == null ? null : dirUrl.trim();
    }

    public String getUsePhoneNumber() {
        return usePhoneNumber;
    }

    public void setUsePhoneNumber(String usePhoneNumber) {
        this.usePhoneNumber = usePhoneNumber == null ? null : usePhoneNumber.trim();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public CheckOrder(String orderCode, String orderOrg, String orderOrgId, String applicantName, String applicantId, String applicantPhonenum, String checkPhonenum, String deviceUseOrg, String useContactor, String usePhoneNumber, String deviceInstallOrg, String installContactor, String installPhoneNumber, String projectName, String projectAddress, Date checkdateExpect, String checkerLoginnames, String paymentType, String getReportType, String contactPersonId, String payeeId, String salesmanId, String checkPersonId, String dirUrl, String orderStatus, List<Consignment> consignmentList) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.orderOrg = orderOrg;
        this.orderOrgId = orderOrgId;
        this.applicantName = applicantName;
        this.applicantId = applicantId;
        this.applicantPhonenum = applicantPhonenum;
        this.checkPhonenum = checkPhonenum;
        this.deviceUseOrg = deviceUseOrg;
        this.useContactor = useContactor;
        this.usePhoneNumber = usePhoneNumber;
        this.deviceInstallOrg = deviceInstallOrg;
        this.installContactor = installContactor;
        this.installPhoneNumber = installPhoneNumber;
        this.projectName = projectName;
        this.projectAddress = projectAddress;
        this.checkdateExpect = checkdateExpect;
        this.checkerLoginnames = checkerLoginnames;
        this.paymentType = paymentType;
        this.getReportType = getReportType;
        this.contactPersonId = contactPersonId;
        this.payeeId = payeeId;
        this.salesmanId = salesmanId;
        this.checkPersonId = checkPersonId;
        this.dirUrl = dirUrl;
        this.orderStatus = orderStatus;
        this.consignmentList = consignmentList;
    }
}