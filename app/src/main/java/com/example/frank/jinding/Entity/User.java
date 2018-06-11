package com.example.frank.jinding.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User {

    private static final long serialVersionUID=1L;

    private String userId;//用户ID

    private String userLoginname;//用户名

    private String userPassword;//密码

    private String userName;//用户名称

    private String companyId;//用户所属公司ID

    private String extraMenuIds;//额外权限

    private String userGender;//性别

    private String contactPeople;//机构客户特有的联系人字段

    private String phoneNum;//联系电话

    private String faxNum;//传真

    private String emailAddress;//电子邮件

    private String userAddress;//地址

    private String uscc;//统一社会信用代码 数据库也全使用小写

    private String bankAccountName;//银行账户名

    private String bankAccountNum;//银行账号

    private String depositBank;//开户行

    private Integer deleteFlag;//删除标记

    private List<Role> roleList=new ArrayList<Role>();//用户拥有的角色



    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserLoginname() {
        return userLoginname;
    }

    public void setUserLoginname(String userLoginname) {
        this.userLoginname = userLoginname == null ? null : userLoginname.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getExtraMenuIds() {
        return extraMenuIds;
    }

    public void setExtraMenuIds(String extraMenuIds) {
        this.extraMenuIds = extraMenuIds == null ? null : extraMenuIds.trim();
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender == null ? null : userGender.trim();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    public String getFaxNum() {
        return faxNum;
    }

    public void setFaxNum(String faxNum) {
        this.faxNum = faxNum == null ? null : faxNum.trim();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress == null ? null : emailAddress.trim();
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress == null ? null : userAddress.trim();
    }

    public String getUscc() {
        return uscc;
    }

    public void setUscc(String uscc) {
        this.uscc = uscc == null ? null : uscc.trim();
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName == null ? null : bankAccountName.trim();
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum == null ? null : bankAccountNum.trim();
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank == null ? null : depositBank.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public boolean isAdmin(){
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(String id){
        return id != null && "1".equals(id);
    }

    public String getContactPeople() {
        return contactPeople;
    }

    public void setContactPeople(String contactPeople) {
        this.contactPeople = contactPeople;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userLoginname='" + userLoginname + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", extraMenuIds='" + extraMenuIds + '\'' +
                ", userGender='" + userGender + '\'' +
                ", contactPeople='" + contactPeople + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", faxNum='" + faxNum + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", uscc='" + uscc + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankAccountNum='" + bankAccountNum + '\'' +
                ", depositBank='" + depositBank + '\'' +
                ", deleteFlag=" + deleteFlag +
                ", roleList=" + roleList +
                '}';
    }

}