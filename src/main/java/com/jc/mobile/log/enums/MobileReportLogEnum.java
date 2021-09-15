package com.jc.mobile.log.enums;

import com.jc.foundation.util.StringUtil;
import com.jc.mobile.log.enums.service.*;

/**
 * @Author 常鹏
 * @Date 2020/8/7 15:57
 * @Version 1.0
 */
public enum MobileReportLogEnum {
    /***/
    empty("", null),
    progress("进度上报", new LogProjectPlanServiceImpl()),
    question("问题上报", new LogQuestionServiceImpl()),
    image("形象上报", new LogPlanImagesServiceImpl()),
    weekly("周报上报", new LogWeeklyServiceImpl()),
    money("产值上报", new LogProjectProductionReportServiceImpl());

    private String value;
    private ILogBusinessTypeEnumService enumService;
    MobileReportLogEnum(String value, ILogBusinessTypeEnumService enumService) {
        this.enumService = enumService;
        this.value = value;
    }

    public static MobileReportLogEnum getByCode(String code) {
        MobileReportLogEnum result = MobileReportLogEnum.empty;
        if (!StringUtil.isEmpty(code)) {
            MobileReportLogEnum[] enums = values();
            for (MobileReportLogEnum anEnum : enums) {
                if (anEnum.toString().equals(code)) {
                    result = anEnum;
                    break;
                }
            }
        }
        return result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ILogBusinessTypeEnumService getEnumService() {
        return enumService;
    }

    public void setEnumService(ILogBusinessTypeEnumService enumService) {
        this.enumService = enumService;
    }
}
