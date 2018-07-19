package com.example.frank.jinding.Bean.Instrument;



import java.util.Date;

public class InstrumentStatus {

    private String instrumentId;

    private String instrumentCode;

    private String instrumentType;

    private Date validateDate;

    private String instrumentBoxCode;

    private String isUsing;

    private String isSubmitted;

    private String isBroken;

    private String deleteFlag;//删除标志：0 未删除/1 已删除

    private Boolean isChoose;

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId == null ? null : instrumentId.trim();
    }

    public String getInstrumentCode() {
        return instrumentCode;
    }

    public void setInstrumentCode(String instrumentCode) {
        this.instrumentCode = instrumentCode == null ? null : instrumentCode.trim();
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType == null ? null : instrumentType.trim();
    }

    public Date getValidateDate() {
        return validateDate;
    }

    public void setValidateDate(Date validateDate) {
        this.validateDate = validateDate;
    }

    public String getInstrumentBoxCode() {
        return instrumentBoxCode;
    }

    public void setInstrumentBoxCode(String instrumentBoxCode) {
        this.instrumentBoxCode = instrumentBoxCode == null ? null : instrumentBoxCode.trim();
    }

    public String getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(String isUsing) {
        this.isUsing = isUsing == null ? null : isUsing.trim();
    }

    public String getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(String isSubmitted) {
        this.isSubmitted = isSubmitted == null ? null : isSubmitted.trim();
    }

    public String getIsBroken() {
        return isBroken;
    }

    public void setIsBroken(String isBroken) {
        this.isBroken = isBroken == null ? null : isBroken.trim();
    }

    public Boolean getChoose() {
        return isChoose;
    }

    public void setChoose(Boolean choose) {
        isChoose = choose;
    }

    public InstrumentStatus(String instrumentId, String instrumentCode, String instrumentType, Date validateDate, String instrumentBoxCode, String isUsing, String isSubmitted, String isBroken, String deleteFlag, Boolean isChoose) {
        this.instrumentId = instrumentId;
        this.instrumentCode = instrumentCode;
        this.instrumentType = instrumentType;
        this.validateDate = validateDate;
        this.instrumentBoxCode = instrumentBoxCode;
        this.isUsing = isUsing;
        this.isSubmitted = isSubmitted;
        this.isBroken = isBroken;
        this.deleteFlag = deleteFlag;
        this.isChoose = isChoose;
    }
}