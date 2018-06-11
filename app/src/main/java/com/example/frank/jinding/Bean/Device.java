package com.example.frank.jinding.Bean;

import java.io.Serializable;

/**
 * Created by DELL on 2017/12/12.
 */

public class Device implements Serializable{
    private String deviceNumber;
    private String numberType;
    private String height;
    private String checkType;
    private String autoNumber;

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        this.autoNumber = autoNumber;
    }

    public Device(String deviceNumber, String numberType, String height, String checkType, String autoNumber) {
        this.deviceNumber = deviceNumber;
        this.numberType = numberType;
        this.height = height;
        this.checkType = checkType;
        this.autoNumber = autoNumber;
    }

    public Device() {
    }
}
