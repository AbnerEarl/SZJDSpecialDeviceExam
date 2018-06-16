package com.henau.pictureselect.bean;

import java.io.File;

/**
 * Created by xxx on 2015/10/31.
 */
public class FolderBean {

    private String dir;//目录路径
    private String firstImagePath;//目录中第一张图片路径
    private String name;//目录名称
    private int count;//目录内文件个数

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        this.name = this.dir.substring(this.dir.lastIndexOf(File.separator) + 1);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
