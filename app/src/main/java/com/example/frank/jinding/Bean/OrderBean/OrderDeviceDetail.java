package com.example.frank.jinding.Bean.OrderBean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by DELL on 2017/12/20.
 */

public class OrderDeviceDetail implements Serializable{
    private String deviceDetailId;

    private String deviceDetailCode;

    private String consignmentId;

    private String prrNo;

    private String deviceManufacCode;

    private String typeSpecification;

    private String checkTypeId;

    private Float installHeight;

    private String selfCode;

    private String statusCode;

    private String modifyPersonId;

    private Date modifyTime;
    private String monitorStatus;
    private String mainCheckReference;

    public String getMainCheckReference() {
        return mainCheckReference;
    }

    public void setMainCheckReference(String mainCheckReference) {
        this.mainCheckReference = mainCheckReference;
    }

    public String getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(String monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    private String dirUrl;

    public Float getDeviceCharge() {
        return deviceCharge;
    }

    public void setDeviceCharge(Float deviceCharge) {
        this.deviceCharge = deviceCharge;
    }

    private Float deviceCharge;

    public String getDeviceDetailId() {
        return deviceDetailId;
    }

    public void setDeviceDetailId(String deviceDetailId) {
        this.deviceDetailId = deviceDetailId == null ? null : deviceDetailId.trim();
    }

    public String getDeviceDetailCode() {
        return deviceDetailCode;
    }

    public void setDeviceDetailCode(String deviceDetailCode) {
        this.deviceDetailCode = deviceDetailCode == null ? null : deviceDetailCode.trim();
    }

    public String getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(String consignmentId) {
        this.consignmentId = consignmentId == null ? null : consignmentId.trim();
    }

    public String getPrrNo() {
        return prrNo;
    }

    public void setPrrNo(String prrNo) {
        this.prrNo = prrNo == null ? null : prrNo.trim();
    }

    public String getDeviceManufacCode() {
        return deviceManufacCode;
    }

    public void setDeviceManufacCode(String deviceManufacCode) {
        this.deviceManufacCode = deviceManufacCode == null ? null : deviceManufacCode.trim();
    }

    public String getTypeSpecification() {
        return typeSpecification;
    }

    public void setTypeSpecification(String typeSpecification) {
        this.typeSpecification = typeSpecification == null ? null : typeSpecification.trim();
    }

    public String getCheckTypeId() {
        return checkTypeId;
    }

    public void setCheckTypeId(String checkTypeId) {
        this.checkTypeId = checkTypeId == null ? null : checkTypeId.trim();
    }

    public Float getInstallHeight() {
        return installHeight;
    }

    public void setInstallHeight(Float installHeight) {
        this.installHeight = installHeight;
    }

    public String getSelfCode() {
        return selfCode;
    }

    public void setSelfCode(String selfCode) {
        this.selfCode = selfCode == null ? null : selfCode.trim();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode == null ? null : statusCode.trim();
    }

    public String getModifyPersonId() {
        return modifyPersonId;
    }

    public void setModifyPersonId(String modifyPersonId) {
        this.modifyPersonId = modifyPersonId == null ? null : modifyPersonId.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDirUrl() {
        return dirUrl;
    }

    public void setDirUrl(String dirUrl) {
        this.dirUrl = dirUrl == null ? null : dirUrl.trim();
    }

    public OrderDeviceDetail(String deviceManufacCode, String typeSpecification, String checkTypeId, Float installHeight, String selfCode) {

        this.deviceManufacCode = deviceManufacCode;
        this.typeSpecification = typeSpecification;
        this.checkTypeId = checkTypeId;
        this.installHeight = installHeight;
        this.selfCode = selfCode;

    }

    public OrderDeviceDetail() {
    }
}
