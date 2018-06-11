package com.example.frank.jinding.Bean.CheckOrderSubmission;

import java.util.Date;

public class CheckOrderSubmission {
    private String submissionId;

    private String orderId;

    private String submissionSeq;

    private Integer recheckSeq;

    private Date actrualDate;

    private String mainCheckerId;

    private String checkerIds;

    private String confirmedCheckerIds;

    private String submissionStatus;

    private String failReason;

    private String checkerRejectReason;

    private String remark;

    private Date submitTime;

    private String submitPersonId;

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubmissionSeq() {
        return submissionSeq;
    }

    public void setSubmissionSeq(String submissionSeq) {
        this.submissionSeq = submissionSeq;
    }

    public Integer getRecheckSeq() {
        return recheckSeq;
    }

    public void setRecheckSeq(Integer recheckSeq) {
        this.recheckSeq = recheckSeq;
    }

    public Date getActrualDate() {
        return actrualDate;
    }

    public void setActrualDate(Date actrualDate) {
        this.actrualDate = actrualDate;
    }

    public String getMainCheckerId() {
        return mainCheckerId;
    }

    public void setMainCheckerId(String mainCheckerId) {
        this.mainCheckerId = mainCheckerId;
    }

    public String getCheckerIds() {
        return checkerIds;
    }

    public void setCheckerIds(String checkerIds) {
        this.checkerIds = checkerIds;
    }

    public String getConfirmedCheckerIds() {
        return confirmedCheckerIds;
    }

    public void setConfirmedCheckerIds(String confirmedCheckerIds) {
        this.confirmedCheckerIds = confirmedCheckerIds;
    }

    public String getSubmissionStatus() {
        return submissionStatus;
    }

    public void setSubmissionStatus(String submissionStatus) {
        this.submissionStatus = submissionStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getCheckerRejectReason() {
        return checkerRejectReason;
    }

    public void setCheckerRejectReason(String checkerRejectReason) {
        this.checkerRejectReason = checkerRejectReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getSubmitPersonId() {
        return submitPersonId;
    }

    public void setSubmitPersonId(String submitPersonId) {
        this.submitPersonId = submitPersonId;
    }
}
