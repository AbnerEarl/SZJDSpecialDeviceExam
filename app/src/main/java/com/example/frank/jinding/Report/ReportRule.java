package com.example.frank.jinding.Report;

/**
 * PROJECT_NAME:SZJDSpecialDeviceExam
 * PACKAGE_NAME:com.example.frank.jinding.Report
 * USER:Frank
 * DATE:2018/6/2
 * TIME:4:51
 * DAY_NAME_FULL:星期六
 * DESCRIPTION:On the description and function of the document
 **/
public class ReportRule {

    public static String deviceReportCorrespondence(String deviceTypeId, String checkTypeId, String monitorStatus,String projectCity) {
        String report = "";
        //桥门式起重机
        if ((deviceTypeId == "2" || deviceTypeId == "4") && (monitorStatus == "1")) {
            report = "SzjdBzQz02";
        } else if (deviceTypeId == "2" || deviceTypeId == "4") {
            //无监控系统或未选
            report = "SzjdBzQz01";
        }
        //塔式起重机
        if (deviceTypeId.trim().equals("1") ) {
            report = "SzjdBzQz21";
            //如果检验类型为安装检验，则分有无监控系统
//            if(checkTypeId == "1") {
//                if (monitorStatus == "1") {
//                    report = "szjdBzQz04";
//                } else {
//                    report = "szjdBzQz03";
//                }
//            } else { //其他检验类型均对应省统表QZ21
//                report = "szjdBzQz21";
//            }
        }
        //施工升降机
        if (deviceTypeId .trim().equals( "3")) {
//            if(projectAddressFlag == "1")
//                report = "szjdBzQz17";
//            else
            report = "SzjdBzQz22";
        }
        //卷扬机、电动葫芦和流式起重机
        if (deviceTypeId == "5" || deviceTypeId == "6" || deviceTypeId == "8") {
            report = "SzjdBzQz12";
        }
        //高处吊篮
        if ((deviceTypeId == "27") && (checkTypeId == "1")) {
            report = "SzjdBzQz19";
        }
//        暂时不用复检报告
//        if(checkTypeId == "5"){  //复检
//            report = "szjdBzQz15";
//        }
//      物料提升机
        if ((deviceTypeId.trim().equals("7"))) {
            report = "SzjdBzQz23";
        }
//       防坠安全器
        if ((deviceTypeId == "9")) {
            report = "SzjdBzF01";
        }
        return report;
    }



}
