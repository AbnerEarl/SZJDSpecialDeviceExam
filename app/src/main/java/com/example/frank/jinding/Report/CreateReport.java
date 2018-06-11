package com.example.frank.jinding.Report;

import com.example.frank.jinding.Bean.Report.SzjdBzQz15;

import org.apache.poi.hwpf.usermodel.Paragraph;

/**
 * File description.
 *
 * @author Frank
 * @date 2018/1/25
 * @emial 1320259466@qq.com
 * @description (about file's use)
 */

public class CreateReport {

    public static String MakeReport(String filename, String p,SzjdBzQz15 szjdBzQz15){

        String paragraph=null;

        switch (filename) {
            case "SzjdBzQz15": {
                paragraph = ReportSzjdBzQz15.ReportSzjdBzQz15(szjdBzQz15, p);
                break;
            }
            case "1": {
                break;
            }
            case "2": {
                break;
            }
            case "3": {
                break;
            }
        }
        return paragraph;


    }





}
