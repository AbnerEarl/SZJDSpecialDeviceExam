package com.example.frank.jinding.Bean.OrderBean;

import java.io.Serializable;

/**
 * Created by DELL on 2017/12/25.
 */

public class DeviceType implements Serializable{
    private String device_type_name;
    private String device_type_code;
    private String device_type_id;

    public DeviceType() {
        super();
    }

    public String getDevice_type_id() {
        return device_type_id;
    }

    public String getDevice_type_name() {
        return device_type_name;
    }

    public DeviceType(String device_type_id, String device_type_code, String device_type_name) {
        super();
        this.device_type_id = device_type_id;
        this.device_type_name = device_type_name;
        this.device_type_code = device_type_code;
    }

    @Override
    public String toString() {
        return device_type_name;
    }

    public void setDevice_type_name(String device_type_name) {
        this.device_type_name = device_type_name;
    }

    public void setDevice_type_code(String device_type_code) {
        this.device_type_code = device_type_code;
    }

    public void setDevice_type_id(String device_type_id) {
        this.device_type_id = device_type_id;
    }

    public String getDevice_type_code() {
        return device_type_code;
    }
}
