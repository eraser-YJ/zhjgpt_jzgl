package com.jc.csmp.exchange.vo;

public class MonitorsEvent {
    private Long hkCode;
    private String hkName;
    private String xyCode;

    public MonitorsEvent(Long hkCode, String hkName, XyMonitorsEventType xyCode) {
        this.hkCode = hkCode;
        this.hkName = hkName;
        this.xyCode = xyCode.toString();
    }

    public Long getHkCode() {
        return hkCode;
    }

    public void setHkCode(Long hkCode) {
        this.hkCode = hkCode;
    }

    public String getHkName() {
        return hkName;
    }

    public void setHkName(String hkName) {
        this.hkName = hkName;
    }

    public String getXyCode() {
        return xyCode;
    }

    public void setXyCode(String xyCode) {
        this.xyCode = xyCode;
    }

    public String getXyName() {
        return XyMonitorsEventType.getByCode(xyCode).getDisName();
    }
}
