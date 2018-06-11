package com.example.frank.jinding.Bean.financial;

import java.util.Date;



public class FinancialExceptionApplication {

    private String exceptionRecordId;
    private String deviceDetailId;
    private String applicantId;
    private Date applicantTime;
    private String applicationStatus;
    private String approvalId;
    private Date approvalTime;
    private String rejectReason;
    public String getExceptionRecordId() {
        return exceptionRecordId;
    }

    public void setExceptionRecordId(String exceptionRecordId) {
        this.exceptionRecordId = exceptionRecordId;
    }

    public String getDeviceDetailId() {
        return deviceDetailId;
    }

    public void setDeviceDetailId(String deviceDetailId) {
        this.deviceDetailId = deviceDetailId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public Date getApplicantTime() {
        return applicantTime;
    }

    public void setApplicantTime(Date applicantTime) {
        this.applicantTime = applicantTime;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public FinancialExceptionApplication() {
    }

}
