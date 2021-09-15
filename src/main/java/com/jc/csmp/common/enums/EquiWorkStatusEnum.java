package com.jc.csmp.common.enums;

public enum EquiWorkStatusEnum {
    normarl("正常"), warn("报警"), outline("离线");
    private String disName;

    EquiWorkStatusEnum(String inName) {
        disName = inName;
    }

    public String getDisName() {
        return disName;
    }

    public static EquiWorkStatusEnum getByCode(String code) {
        try {
            EquiWorkStatusEnum nowStatus = EquiWorkStatusEnum.valueOf(code);
            if (nowStatus != null) {
                return nowStatus;
            }
            return EquiWorkStatusEnum.normarl;
        } catch (Exception ex) {
            return EquiWorkStatusEnum.normarl;
        }
    }
}
