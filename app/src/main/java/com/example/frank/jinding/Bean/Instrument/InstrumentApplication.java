package com.example.frank.jinding.Bean.Instrument;


import java.util.Date;

public class InstrumentApplication {

    private String applicationRecordId;

    private String submissionId;

    private String instrumentCodes;

    private String isSuccess;

    private String failReason;

    private String confirmPersonId;

    private Date confirmTime;

    private String isReturned;

    private Date returnTime;

    private String applyPersonId;

    private Date applyTime;

    private String instrumentReturnCodes;

    public String getApplicationRecordId() {
        return applicationRecordId;
    }

    public void setApplicationRecordId(String applicationRecordId) {
        this.applicationRecordId = applicationRecordId;
    }

    public String getInstrumentReturnCodes() {
        return instrumentReturnCodes;
    }

    public void setInstrumentReturnCodes(String instrumentReturnCodes) {
        this.instrumentReturnCodes = instrumentReturnCodes;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyPersonId() {
        return applyPersonId;
    }

    public void setApplyPersonId(String applyPersonId) {
        this.applyPersonId = applyPersonId;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId == null ? null : submissionId.trim();
    }

    public String getInstrumentCodes() {
        return instrumentCodes;
    }

    public void setInstrumentCodes(String instrumentCodes) {
        this.instrumentCodes = instrumentCodes == null ? null : instrumentCodes.trim();
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess == null ? null : isSuccess.trim();
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason == null ? null : failReason.trim();
    }

    public String getConfirmPersonId() {
        return confirmPersonId;
    }

    public void setConfirmPersonId(String confirmPersonId) {
        this.confirmPersonId = confirmPersonId == null ? null : confirmPersonId.trim();
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(String isReturned) {
        this.isReturned = isReturned;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    @Override
    public String toString() {
        return "InstrumentApplication{" +
                "applicationRecordId='" + applicationRecordId + '\'' +
                ", submissionId='" + submissionId + '\'' +
                ", instrumentCodes='" + instrumentCodes + '\'' +
                ", isSuccess='" + isSuccess + '\'' +
                ", failReason='" + failReason + '\'' +
                ", confirmPersonId='" + confirmPersonId + '\'' +
                ", confirmTime=" + confirmTime +
                ", isReturned='" + isReturned + '\'' +
                ", returnTime=" + returnTime +
                ", applyPersonId='" + applyPersonId + '\'' +
                ", applyTime=" + applyTime +
                ", instrumentReturnCodes='" + instrumentReturnCodes + '\'' +
                '}';
    }
}