package com.example.frank.jinding.Report;

import com.example.frank.jinding.Bean.Report.SzjdBzQz19;

import org.apache.poi.hwpf.usermodel.Paragraph;

/**
 * File description.
 *
 * @author Frank
 * @date 2018/1/25
 * @emial 1320259466@qq.com
 * @description (about file's use)
 */

public class ReportSzjdBzQz04 {

    public static Paragraph ReportSzjdBzQz04(SzjdBzQz19 szjdBzQz19, Paragraph p){
        p.replaceText("###001",szjdBzQz19.getReportCode());
        p.replaceText("###002",szjdBzQz19.getReportCode());
        p.replaceText("###003",szjdBzQz19.getPropertyOrg());
        p.replaceText("###004",szjdBzQz19.getInstallOrg());
        p.replaceText("###005",szjdBzQz19.getOrderOrg());
        p.replaceText("###006",szjdBzQz19.getCheckPeo());
        p.replaceText("###007",szjdBzQz19.getReportCode());
        p.replaceText("###008",szjdBzQz19.getWeather());
        p.replaceText("###009",szjdBzQz19.getTemperature());
        p.replaceText("###010",szjdBzQz19.getWindSpeed());
        p.replaceText("###011",szjdBzQz19.getInstallOrg());
        p.replaceText("###012",szjdBzQz19.getPropertyOrg());
        p.replaceText("###013",szjdBzQz19.getOrderOrg());
        p.replaceText("###014",szjdBzQz19.getMakeOrg());
        p.replaceText("###015",szjdBzQz19.getProductDate().toString());
        p.replaceText("###016",szjdBzQz19.getSupervisionOrg());
        p.replaceText("###017",szjdBzQz19.getInstallOrg());
        p.replaceText("###018",szjdBzQz19.getSpecifiedWeight());
        p.replaceText("###019",szjdBzQz19.getProductDate().toString());
        p.replaceText("###020",szjdBzQz19.getAdditionalWeight().toString());
        p.replaceText("###021",szjdBzQz19.getSelfCode());
        p.replaceText("###022",szjdBzQz19.getVerticalSpeed().toString());
        p.replaceText("###023",szjdBzQz19.getInstallHeight().toString());
        p.replaceText("###024",szjdBzQz19.getPlatformLength().toString());
        p.replaceText("###025",szjdBzQz19.getWireropeDiameter().toString());
        p.replaceText("###026",szjdBzQz19.getRatedLiftForce().toString());
        p.replaceText("###027",szjdBzQz19.getBrakingTorque().toString());
        p.replaceText("###028",szjdBzQz19.getMotorType());
        p.replaceText("###029",szjdBzQz19.getMotorSpeed().toString());
        p.replaceText("###030",szjdBzQz19.getPermissionImpact());
        p.replaceText("###031",szjdBzQz19.getRopeAngle());
        p.replaceText("###032",szjdBzQz19.getLockCodeLeft());
        p.replaceText("###033",szjdBzQz19.getLockCodeRight());
        p.replaceText("###034",szjdBzQz19.getValidDateLeft().toString());
        p.replaceText("###035",szjdBzQz19.getValidDateRight().toString());
        p.replaceText("###036",szjdBzQz19.getAdjustableHeight().toString());
        p.replaceText("###037",szjdBzQz19.getStretchLength().toString());
        p.replaceText("###038",szjdBzQz19.getLockRopeDistance());
        p.replaceText("###039",szjdBzQz19.getPlatformWeight().toString());
        p.replaceText("###040",szjdBzQz19.getCompleteMachineWeight().toString());
        p.replaceText("###041",szjdBzQz19.getMainInstruStatus().toString());
        p.replaceText("###042",szjdBzQz19.getMainInstruName());
        p.replaceText("###043",szjdBzQz19.getMainInstruCode());
        p.replaceText("###044",szjdBzQz19.getMainInstruStatus());
        p.replaceText("###045",szjdBzQz19.getMainInstruStatus());
        p.replaceText("###046",szjdBzQz19.getMainInstruName());
        p.replaceText("###047",szjdBzQz19.getMainInstruCode());
        p.replaceText("###048",szjdBzQz19.getMainInstruType());
        p.replaceText("###049",szjdBzQz19.getMainInstruCode());
        p.replaceText("###050",szjdBzQz19.getMainInstruName());
        p.replaceText("###051",szjdBzQz19.getMainInstruType());
        p.replaceText("###052",szjdBzQz19.getMainInstruCode());
        p.replaceText("###053",szjdBzQz19.getMainInstruStatus());
        p.replaceText("###054",szjdBzQz19.getProNotApproval().toString());
        p.replaceText("###142",szjdBzQz19.getComNotApproval().toString());
        p.replaceText("###141",szjdBzQz19.getProNotApprovalTotal().toString());
        p.replaceText("###55",szjdBzQz19.getProNotApprovalTotal().toString());
        p.replaceText("###056",szjdBzQz19.getNextTestDate().toString());
        p.replaceText("###057",szjdBzQz19.getCheckResult().toString());
        p.replaceText("###058",szjdBzQz19.getSignDate().toString());
        p.replaceText("###059",szjdBzQz19.getRemark1());
        p.replaceText("###060",szjdBzQz19.getApprovalPeo());
        p.replaceText("###061",szjdBzQz19.getReviewPeo());
        p.replaceText("###062",szjdBzQz19.getCheckPeo());
        p.replaceText("###063",szjdBzQz19.getDetectResult111());
        p.replaceText("###064",szjdBzQz19.getDetectConclusion111());
        p.replaceText("###065",szjdBzQz19.getDetectResult112());
        p.replaceText("###066",szjdBzQz19.getDetectConclusion112());
        p.replaceText("###067",szjdBzQz19.getDetectResult113());
        p.replaceText("###068",szjdBzQz19.getDetectConclusion113());
        p.replaceText("###069",szjdBzQz19.getDetectResult114());
        p.replaceText("###070",szjdBzQz19.getDetectConclusion114());
        p.replaceText("###071",szjdBzQz19.getDetectResult115());
        p.replaceText("###072",szjdBzQz19.getDetectConclusion115());
        p.replaceText("###073",szjdBzQz19.getDetectResult116());
        p.replaceText("###074",szjdBzQz19.getDetectConclusion116());

        p.replaceText("###075",szjdBzQz19.getDetectResult121());
        p.replaceText("###076",szjdBzQz19.getDetectConclusion121());
        p.replaceText("###077",szjdBzQz19.getDetectResult122());
        p.replaceText("###078",szjdBzQz19.getDetectConclusion122());
        p.replaceText("###079",szjdBzQz19.getDetectResult211());
        p.replaceText("###080",szjdBzQz19.getDetectConclusion211());
        p.replaceText("###081",szjdBzQz19.getDetectResult212());
        p.replaceText("###082",szjdBzQz19.getDetectConclusion212());
        p.replaceText("###083",szjdBzQz19.getDetectResult213());
        p.replaceText("###084",szjdBzQz19.getDetectConclusion213());
        p.replaceText("###085",szjdBzQz19.getDetectResult214());
        p.replaceText("###086",szjdBzQz19.getDetectConclusion214());
        p.replaceText("###087",szjdBzQz19.getDetectResult215().toString());
        p.replaceText("###088",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###089",szjdBzQz19.getDetectResult216());
        p.replaceText("###090",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###091",szjdBzQz19.getDetectResult221());
        p.replaceText("###092",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###093",szjdBzQz19.getDetectResult222());
        p.replaceText("###094",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###095",szjdBzQz19.getDetectResult232());
        p.replaceText("###096",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###097",szjdBzQz19.getDetectResult216());
        p.replaceText("###098",szjdBzQz19.getDetectResult216());
        p.replaceText("###099",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###100",szjdBzQz19.getDetectResult216());
        p.replaceText("###101",szjdBzQz19.getDetectResult216());
        p.replaceText("###102",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###103",szjdBzQz19.getDetectResult216());
        p.replaceText("###104",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###105",szjdBzQz19.getDetectResult216());
        p.replaceText("###106",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###107",szjdBzQz19.getDetectResult216());
        p.replaceText("###108",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###109",szjdBzQz19.getDetectResult216());
        p.replaceText("###110",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###111",szjdBzQz19.getDetectResult216());
        p.replaceText("###112",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###113",szjdBzQz19.getDetectResult216());
        p.replaceText("###114",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###115",szjdBzQz19.getDetectResult216());
        p.replaceText("###116",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###117",szjdBzQz19.getDetectResult216());
        p.replaceText("###118",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###119",szjdBzQz19.getDetectResult216());
        p.replaceText("###120",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###121",szjdBzQz19.getDetectResult216());
        p.replaceText("###122",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###123",szjdBzQz19.getDetectResult216());
        p.replaceText("###124",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###125",szjdBzQz19.getDetectResult216());
        p.replaceText("###126",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###127",szjdBzQz19.getDetectResult216());
        p.replaceText("###128",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###129",szjdBzQz19.getDetectResult216());
        p.replaceText("###130",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###131",szjdBzQz19.getDetectResult216());
        p.replaceText("###132",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###133",szjdBzQz19.getDetectResult216());
        p.replaceText("###134",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###135",szjdBzQz19.getDetectResult216());
        p.replaceText("###136",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###137",szjdBzQz19.getDetectResult216());
        p.replaceText("###138",szjdBzQz19.getDetectConclusion215());
        p.replaceText("###139",szjdBzQz19.getDetectResult216());
        p.replaceText("###140",szjdBzQz19.getDetectConclusion215());
















        return p;
    }
}
