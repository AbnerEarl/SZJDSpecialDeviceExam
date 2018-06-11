package com.example.frank.jinding.Bean.financial;

import java.util.Date;


public class FinancialState {

    private String financialStateId;
    private String deviceDetailId;
    private float paidExpenses;
    private String updatePerson;
    private Date updateTime;

    public String getFinancialStateId() {
        return financialStateId;
    }

    public void setFinancialStateId(String financialStateId) {
        this.financialStateId = financialStateId;
    }


    public String getDeviceDetailId() {
        return deviceDetailId;
    }

    public void setDeviceDetailId(String deviceDetailId) {
        this.deviceDetailId = deviceDetailId;
    }

    public float getPaidExpenses() {
        return paidExpenses;
    }

    public void setPaidExpenses(float paidExpenses) {
        this.paidExpenses = paidExpenses;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
