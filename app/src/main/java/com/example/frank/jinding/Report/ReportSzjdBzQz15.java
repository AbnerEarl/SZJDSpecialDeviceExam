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

public class ReportSzjdBzQz15 {

    public static String ReportSzjdBzQz15(SzjdBzQz15 szjdBzQz15, String p){

        String ph=p;

        if (ph.contains("###001")){
            ph=ph.replace("###001", szjdBzQz15.getReportCode());
        }

        else if (ph.contains("###002")){
            ph=ph.replace("###002", szjdBzQz15.getUseOrg());
        }

        else if (ph.contains("###003")){
            ph= ph.replace("###003", szjdBzQz15.getLastCheckCode());
        }

        else if (ph.contains("###004")){
            ph=ph.replace("###004", szjdBzQz15.getRecheckDate().toString());
        }
        else if (ph.contains("###005")){
            ph= ph.replace("###005", szjdBzQz15.getLastUnqualifiedCode());
        }
        else if (ph.contains("###006")){
            ph= ph.replace("###006", szjdBzQz15.getCheckResult());
        }
        else if (ph.contains("###007")){
            ph= ph.replace("###007", szjdBzQz15.getCheckConclusion());
        }
        else if (ph.contains("###008")){
            ph=ph.replace("###008", szjdBzQz15.getRecheckConclusion());
        }
        else if (ph.contains("###009")){
            ph= ph.replace("###009", szjdBzQz15.getRecheckDate().toString());
        }
        else if (ph.contains("###010")){
            ph=ph.replace("###010", szjdBzQz15.getNextCheckDate().toString());
        }
        else if (ph.contains("###011")){
            ph=ph.replace("###011", szjdBzQz15.getRemark());
        }
        else if (ph.contains("###012")){
            ph= ph.replace("###012", szjdBzQz15.getApprovalPerson());
        }
        else if (ph.contains("###013")){
            ph= ph.replace("###013", szjdBzQz15.getReviewPerson());
        }
        else if (ph.contains("###014")){
            ph= ph.replace("###014", szjdBzQz15.getCheckPerson());
        }








            /*if (ph.text().contains("###001")) {
                ph.replaceText("###001", szjdBzQz15.getReportCode());
            }
            if (ph.text().contains("###002")) {
                ph.replaceText("###002", szjdBzQz15.getUseOrg());
            }
            if (ph.text().contains("###003")) {
                ph.replaceText("###003", szjdBzQz15.getLastCheckCode());
            }
            if (ph.text().contains("###004")) {
                ph.replaceText("###004", szjdBzQz15.getRecheckDate().toString());
            }
            if (ph.text().contains("###005")) {
                ph.replaceText("###005", szjdBzQz15.getLastUnqualifiedCode());
            }
            if (ph.text().contains("###006")) {
                ph.replaceText("###006", szjdBzQz15.getCheckResult());
            }
            if (ph.text().contains("######007")) {
                ph.replaceText("###007", szjdBzQz15.getCheckConclusion());
            }
            if (ph.text().contains("###008")) {
                ph.replaceText("###008", szjdBzQz15.getRecheckConclusion());
            }
            if (ph.text().contains("###009")) {
                ph.replaceText("###009", szjdBzQz15.getRecheckDate().toString());
            }
            if (ph.text().contains("###010")) {
                ph.replaceText("###010", szjdBzQz15.getNextCheckDate().toString());
            }

            if (ph.text().contains("###011")) {
                ph.replaceText("###011", szjdBzQz15.getRemark());
            }

            if (ph.text().contains("###012")) {
                ph.replaceText("###012", szjdBzQz15.getApprovalPerson());
            }
            if (ph.text().contains("###013")) {
                ph.replaceText("###013", szjdBzQz15.getReviewPerson());
            }
            if (ph.text().contains("###014")) {
                ph.replaceText("###014", szjdBzQz15.getCheckPerson());
            }
*/
        return ph;
    }
}
