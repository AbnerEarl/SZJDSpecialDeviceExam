package com.example.frank.jinding.Entity;



import java.io.Serializable;


public class Menu implements Serializable{

    private static final long serialVersionUID = 1L;

    private String menuId;

    private String parentId;

    private String name;

    private String url;

    private Integer sort;

    private Integer isShow;

    private String permission;

    private Integer deleteFlag;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission == null ? null : permission.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            {
                return true;
            }
        }
        if (o == null || getClass() != o.getClass()) {
            {
                return false;
            }
        }

        Menu menu = (Menu) o;

        if (menuId != null ? !menuId.equals(menu.menuId) : menu.menuId != null) {
            return false;
        }
        if (parentId != null ? !parentId.equals(menu.parentId) : menu.parentId != null) {
            return false;
        }
        if (name != null ? !name.equals(menu.name) : menu.name != null) {
            return false;
        }
        if (url != null ? !url.equals(menu.url) : menu.url != null) {
            return false;
        }
        if (sort != null ? !sort.equals(menu.sort) : menu.sort != null) {
            return false;
        }
        if (isShow != null ? !isShow.equals(menu.isShow) : menu.isShow != null) {
            return false;
        }
        if (permission != null ? !permission.equals(menu.permission) : menu.permission != null) {
            return false;
        }
        return deleteFlag != null ? deleteFlag.equals(menu.deleteFlag) : menu.deleteFlag == null;
    }

    @Override
    public int hashCode() {
        int result = menuId != null ? menuId.hashCode() : 0;
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        result = 31 * result + (isShow != null ? isShow.hashCode() : 0);
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (deleteFlag != null ? deleteFlag.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Menu{" +
                "menuId='" + menuId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", sort=" + sort +
                ", isShow=" + isShow +
                ", permission='" + permission + '\'' +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}