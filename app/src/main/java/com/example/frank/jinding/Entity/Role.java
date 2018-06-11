package com.example.frank.jinding.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Role implements Serializable{

    private static final long serialVersionUID=1L;


    private String roleId;

    private String roleName;

    private Integer useable;

    private String roleDescription;

    private Integer deleteFlag;


    private List<User> userList=new ArrayList<User>();//角色拥有的用户列表

    private List<Menu> menuList=new ArrayList<Menu>();//角色拥有的菜单列表

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Integer getUseable() {
        return useable;
    }

    public void setUseable(Integer useable) {
        this.useable = useable;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription == null ? null : roleDescription.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", useable=" + useable +
                ", roleDescription='" + roleDescription + '\'' +
                ", deleteFlag=" + deleteFlag +
                ", userList=" + userList +
                ", menuList=" + menuList +
                '}';
    }
}