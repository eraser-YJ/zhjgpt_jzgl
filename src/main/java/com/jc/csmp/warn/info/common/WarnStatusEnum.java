package com.jc.csmp.warn.info.common;

public enum WarnStatusEnum {
    init("报警"), processed("已处置");
    private String disName;

    WarnStatusEnum(String inName) {
        disName = inName;
    }

    public String getDisName() {
        return disName;
    }

    public static WarnStatusEnum getByCode(String code) {
        try {
            WarnStatusEnum nowStatus = WarnStatusEnum.valueOf(code);
            if (nowStatus != null) {
                return nowStatus;
            }
            return WarnStatusEnum.init;
        } catch (Exception ex) {
            return WarnStatusEnum.init;
        }
    }
}
