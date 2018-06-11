package com.example.frank.jinding.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 2017/12/12.
 */

public class Consignment implements Serializable {
    private String device_type;
    private int device_number;
    private List<Device> deviceList;
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

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public int getDevice_number() {
        return device_number;
    }

    public void setDevice_number(int device_number) {
        this.device_number = device_number;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> device) {
        this.deviceList = device;
    }

    public Consignment(String device_type, int device_number, List<Device> device) {
        this.device_type = device_type;
        this.device_number = device_number;
        this.deviceList = device;
    }
    public Consignment(String device_type,int device_number){
        this.device_number=device_number;
        this.device_type=device_type;
    }

    public Consignment() {
    }
    public void updateDevice(int position,Device device){
        deviceList.set(position,device);
    }

    public String getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(String consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getConsignmentCode() {
        return consignmentCode;
    }

    public void setConsignmentCode(String consignmentCode) {
        this.consignmentCode = consignmentCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
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
        this.referenceCodes = referenceCodes;
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
        this.isPassCheck = isPassCheck;
    }

    public String getDirUrl() {
        return dirUrl;
    }

    public void setDirUrl(String dirUrl) {
        this.dirUrl = dirUrl;
    }

    public String getConsignmentStatus() {
        return consignmentStatus;
    }

    public void setConsignmentStatus(String consignmentStatus) {
        this.consignmentStatus = consignmentStatus;
    }
}
