package com.jc.csmp.exchange.vo;

public enum XyMonitorsEventType {

    site_warn("工地现场的整体情况"), work_warn("作业情况"), person_warn("人员作业行为"),area_warn("危险区域进入情况");

    private String disName;

    XyMonitorsEventType(String inName) {
        disName = inName;
    }

    public String getDisName() {
        return disName;
    }

    public static XyMonitorsEventType getByCode(String code) {
        try {
            XyMonitorsEventType nowStatus = XyMonitorsEventType.valueOf(code);
            if (nowStatus != null) {
                return nowStatus;
            }
            return XyMonitorsEventType.work_warn;
        } catch (Exception ex) {
            return XyMonitorsEventType.work_warn;
        }
    }
}
