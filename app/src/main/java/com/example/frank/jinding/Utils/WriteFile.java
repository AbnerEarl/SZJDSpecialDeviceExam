package com.example.frank.jinding.Utils;

import com.example.frank.jinding.Bean.Report.SzjdBzQz15;
import com.example.frank.jinding.Report.CreateReport;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * File description.
 *
 * @author Frank
 * @date 2018/1/28
 * @emial 1320259466@qq.com
 * @description (about file's use)
 */

public class WriteFile {

    public static void writeff(String content, String path, String filecode, SzjdBzQz15 szjdBzQz15) {
        try {


            //String contentnew= CreateReport.MakeReport(filecode,content,szjdBzQz15) ;

                File targetFile = new File(path);
                //以指定文件创建RandomAccessFile对象
                RandomAccessFile raf = new RandomAccessFile(targetFile,"rw");
                //记住，输入的时候位置是要用到指针的
                //raf.seek(targetFile.length());
                raf.seek(0);
                //输出文件内容
                raf.write(content.getBytes());
                //关闭RandomAccessFile
                raf.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
