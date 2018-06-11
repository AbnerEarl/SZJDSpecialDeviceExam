package com.example.frank.jinding.Utils;

import android.os.Environment;

import com.example.frank.jinding.Bean.Report.SzjdBzQz15;
import com.example.frank.jinding.Report.CreateReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * File description.
 *
 * @author Frank
 * @date 2018/1/28
 * @emial 1320259466@qq.com
 * @description (about file's use)
 */

public class ReadFile {
    public static void readff(String path, String filecode, SzjdBzQz15 szjdBzQz15) {
        StringBuilder sb = new StringBuilder("");
        try {
                FileInputStream fis = new FileInputStream(path);
                //将指定输入流包装成BufferedReader
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                String line = null;
                //循环读取文件内容
                while ((line = br.readLine()) != null) {
                    String message= CreateReport.MakeReport(filecode,line.toString(),szjdBzQz15) ;
                    sb.append(message);
                }
                br.close();

                WriteFile.writeff(sb.toString(),path,filecode,szjdBzQz15);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //return sb.toString();
    }
}
