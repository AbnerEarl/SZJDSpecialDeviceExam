package com.example.frank.jinding.Bean.Report;

import java.util.Date;

public class SzjdBzQz15 {
    private String reportId;

    private String deviceDetailId;

    private String reportCode;

    private String useOrg;

    private String lastCheckCode;

    private Date recheckDate;

    private String lastUnqualifiedCode;

    private String checkResult;

    private String checkConclusion;

    private String recheckConclusion;

    private Date nextCheckDate;

    private String remark;

    private String approvalPerson;

    private String reviewPerson;

    private String checkPerson;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId == null ? null : reportId.trim();
    }

    public String getDeviceDetailId() {
        return deviceDetailId;
    }

    public void setDeviceDetailId(String deviceDetailId) {
        this.deviceDetailId = deviceDetailId == null ? null : deviceDetailId.trim();
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode == null ? null : reportCode.trim();
    }

    public String getUseOrg() {
        return useOrg;
    }

    public void setUseOrg(String useOrg) {
        this.useOrg = useOrg == null ? null : useOrg.trim();
    }

    public String getLastCheckCode() {
        return lastCheckCode;
    }

    public void setLastCheckCode(String lastCheckCode) {
        this.lastCheckCode = lastCheckCode == null ? null : lastCheckCode.trim();
    }

    public Date getRecheckDate() {
        return recheckDate;
    }

    public void setRecheckDate(Date recheckDate) {
        this.recheckDate = recheckDate;
    }

    public String getLastUnqualifiedCode() {
        return lastUnqualifiedCode;
    }

    public void setLastUnqualifiedCode(String lastUnqualifiedCode) {
        this.lastUnqualifiedCode = lastUnqualifiedCode == null ? null : lastUnqualifiedCode.trim();
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult == null ? null : checkResult.trim();
    }

    public String getCheckConclusion() {
        return checkConclusion;
    }

    public void setCheckConclusion(String checkConclusion) {
        this.checkConclusion = checkConclusion == null ? null : checkConclusion.trim();
    }

    public String getRecheckConclusion() {
        return recheckConclusion;
    }

    public void setRecheckConclusion(String recheckConclusion) {
        this.recheckConclusion = recheckConclusion == null ? null : recheckConclusion.trim();
    }

    public Date getNextCheckDate() {
        return nextCheckDate;
    }

    public void setNextCheckDate(Date nextCheckDate) {
        this.nextCheckDate = nextCheckDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getApprovalPerson() {
        return approvalPerson;
    }

    public void setApprovalPerson(String approvalPerson) {
        this.approvalPerson = approvalPerson == null ? null : approvalPerson.trim();
    }

    public String getReviewPerson() {
        return reviewPerson;
    }

    public void setReviewPerson(String reviewPerson) {
        this.reviewPerson = reviewPerson == null ? null : reviewPerson.trim();
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson == null ? null : checkPerson.trim();
    }

    @Override
    public String toString() {
        return "SzjdBzQz15{" +
                "reportId='" + reportId + '\'' +
                ", deviceDetailId='" + deviceDetailId + '\'' +
                ", reportCode='" + reportCode + '\'' +
                ", useOrg='" + useOrg + '\'' +
                ", lastCheckCode='" + lastCheckCode + '\'' +
                ", recheckDate=" + recheckDate +
                ", lastUnqualifiedCode='" + lastUnqualifiedCode + '\'' +
                ", checkResult='" + checkResult + '\'' +
                ", checkConclusion='" + checkConclusion + '\'' +
                ", recheckConclusion='" + recheckConclusion + '\'' +
                ", nextCheckDate=" + nextCheckDate +
                ", remark='" + remark + '\'' +
                ", approvalPerson='" + approvalPerson + '\'' +
                ", reviewPerson='" + reviewPerson + '\'' +
                ", checkPerson='" + checkPerson + '\'' +
                '}';
    }
}