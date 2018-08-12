package com.example.frank.jinding.Conf;

/**
 * PROJECT_NAME:SpecialDeviceExam
 * PACKAGE_NAME:com.example.frank.jinding.Conf
 * USER:Frank
 * DATE:2018/8/4
 * TIME:19:45
 * DAY_NAME_FULL:星期六
 * DESCRIPTION:On the description and function of the document
 **/
public class OrderStatus {
    public static String ChangeOrderStatus(String orderStatus){
        String result="";
        if (orderStatus.equals("01")){
            result="协议待评审";
        }else if (orderStatus.equals("02")){
            result="部分协议待评审";
        }else if (orderStatus.equals("03")){
            result="待派工";
        }else if (orderStatus.equals("0301")){
            result="复检待派工";
        }else if (orderStatus.equals("04")){
            result="派工待确认";
        }else if (orderStatus.equals("0401")){
            result="复检派工待确认";
        }else if (orderStatus.equals("05")){
            result="仪器待申领";
        }else if (orderStatus.equals("0501")){
            result="复检仪器待申领";
        }else if (orderStatus.equals("06")){
            result="现场检验";
        }else if (orderStatus.equals("0601")){
            result="复检现场检验";
        }else if (orderStatus.equals("07")){
            result="撰写报告";
        }else if (orderStatus.equals("0701")){
            result="撰写报告（复检）";
        }else if (orderStatus.equals("08")){
            result="报告待校核";
        }else if (orderStatus.equals("0801")){
            result="报告待校核（复检）";
        }else if (orderStatus.equals("09")){
            result="报告待审核";
        }else if (orderStatus.equals("0901")){
            result="报告待审核（复检）";
        }else if (orderStatus.equals("10")){
            result="报告待审批";
        }else if (orderStatus.equals("1001")){
            result="报告待审批（复检）";
        }else if (orderStatus.equals("11")){
            result="复检整改中";
        }else if (orderStatus.equals("12")){
            result="复检申请待审核";
        }else if (orderStatus.equals("13")){
            result="待支付";
        }else if (orderStatus.equals("14")){
            result="财务异动";
        }else if (orderStatus.equals("15")){
            result="报告待发放";
        }else if (orderStatus.equals("16")){
            result="报告已发放";
        }else if (orderStatus.equals("17")){
            result="资料已归档";
        }else if (orderStatus.equals("18")){
            result="业务终结";
        }else if (orderStatus.equals("19")){
            result="已拒绝";
        }else if (orderStatus.equals("20")){
            result="校核返回";
        }else if (orderStatus.equals("2001")){
            result="校核返回（复检）";
        }else if (orderStatus.equals("21")){
            result="审核返回";
        }else if (orderStatus.equals("2101")){
            result="审核返回（复检）";
        }else if (orderStatus.equals("22")){
            result="审批返回";
        }else if (orderStatus.equals("2201")){
            result="审批返回（复检）";
        }else if (orderStatus.equals("23")){
            result="检验现场信息核对完成";
        }else if (orderStatus.equals("2301")){
            result="检验现场信息核对完成";
        }else if (orderStatus.equals("24")){
            result="财务异动待审核";
        }else if (orderStatus.equals("2401")){
            result="财务异动待审核（复检）";
        }else if (orderStatus.equals("25")){
            result="仪器申领待确认";
        }else if (orderStatus.equals("2501")){
            result="复检仪器申领待确认";
        }else if (orderStatus.equals("30")){
            result="初检重新派工待审核";
        }else if (orderStatus.equals("3001")){
            result="复检重新派工待审核";
        }else if (orderStatus.equals("31")){
            result="终止检验待审核";
        }else {
            result=orderStatus;
        }

        return result;
    }
}
