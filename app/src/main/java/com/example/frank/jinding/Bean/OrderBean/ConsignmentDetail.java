package com.example.frank.jinding.Bean.OrderBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ConsignmentDetail implements Serializable{
    private String consignmentId;

    private String consignmentCode;

    private String orderId;

    private String deviceTypeId;

    private Integer deviceNum;

    private String referenceCodes;

    private Float checkCharge;

    private String isPassCheck;

    private String dirUrl;

    private  String consignmentStatus;
    private String refuseReason;
    private String checkPersonId;

    private Date checkTime;
    private String mainCheckReference;

    public String getMainCheckReference() {
        return mainCheckReference;
    }

    public void setMainCheckReference(String mainCheckReference) {
        this.mainCheckReference = mainCheckReference;
    }

    public String getCheckPersonId() {
        return checkPersonId;
    }

    public void setCheckPersonId(String checkPersonId) {
        this.checkPersonId = checkPersonId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    private List<OrderDeviceDetail> orderDeviceDetailList;

    public List<OrderDeviceDetail> getOrderDeviceDetailList() {
        return orderDeviceDetailList;
    }

    public void setOrderDeviceDetailList(List<OrderDeviceDetail> orderDeviceDetailList) {
        this.orderDeviceDetailList = orderDeviceDetailList;
    }

    public String getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(String consignmentId) {
        this.consignmentId = consignmentId == null ? null : consignmentId.trim();
    }

    public String getConsignmentCode() {
        return consignmentCode;
    }

    public void setConsignmentCode(String consignmentCode) {
        this.consignmentCode = consignmentCode == null ? null : consignmentCode.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId == null ? null : deviceTypeId.trim();
    }

    public Integer getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(Integer deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getReferenceCodes() {
        return referenceCodes;
    }

    public void setReferenceCodes(String referenceCodes) {
        this.referenceCodes = referenceCodes == null ? null : referenceCodes.trim();
    }

    public Float getCheckCharge() {
        return checkCharge;
    }

    public void setCheckCharge(Float checkCharge) {
        this.checkCharge = checkCharge;
    }

    public String getIsPassCheck() {
        return isPassCheck;
    }

    public void setIsPassCheck(String isPassCheck) {
        this.isPassCheck = isPassCheck == null ? null : isPassCheck.trim();
    }

    public String getDirUrl() {
        return dirUrl;
    }

    public void setDirUrl(String dirUrl) {
        this.dirUrl = dirUrl == null ? null : dirUrl.trim();
    }

    public String getConsignmentStatus() {
        return consignmentStatus;
    }

    public void setConsignmentStatus(String consignmentStatus) {
        this.consignmentStatus = consignmentStatus == null ? null : consignmentStatus.trim();
    }

    public ConsignmentDetail(String consignmentId, String consignmentCode, String orderId, String deviceTypeId, Integer deviceNum, String referenceCodes, Float checkCharge, String isPassCheck, String dirUrl, String consignmentStatus) {
        this.consignmentId = consignmentId;
        this.consignmentCode = consignmentCode;
        this.orderId = orderId;
        this.deviceTypeId = deviceTypeId;
        this.deviceNum = deviceNum;
        this.referenceCodes = referenceCodes;
        this.checkCharge = checkCharge;
        this.isPassCheck = isPassCheck;
        this.dirUrl = dirUrl;
        this.consignmentStatus = consignmentStatus;
    }

    public ConsignmentDetail(String deviceTypeId, Integer deviceNum) {
        this.deviceTypeId = deviceTypeId;
        this.deviceNum = deviceNum;
    }

    public ConsignmentDetail() {
    }
}