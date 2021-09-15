package com.jc.csmp.equipment.info.domain.validator;

import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.foundation.util.MessageUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @Version 1.0
 */
public class EquipmentInfoValidator implements Validator {
    private static final String JC_SYS_010 = "JC_SYS_010";
    private static final String JC_SYS_011 = "JC_SYS_011";
    private static final int FIFTY = 50;
    private static final int TWO_HUNDRED = 200;

    /**
     * @param arg0 校验的类型
     * @return 是否支持校验
     * @description 检验类能够校验的类
     * @Author 常鹏
     * @version 1.0
     */
    @Override
    public boolean supports(Class<?> arg0) {
        return EquipmentInfo.class.equals(arg0);
    }

    /**
     * @version 1.0
     */
    @Override
    public void validate(Object arg0, Errors arg1) {
        EquipmentInfo v = (EquipmentInfo) arg0;
        if (v.getId() != null && v.getId().length() > 64) {
            arg1.reject("id", MessageUtils.getMessage(JC_SYS_011, new Object[]{"64"}));
        }
        if (v.getProjectName() != null && v.getProjectName().length() > 200) {
            arg1.reject("projectName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
        }
        if (v.getUseCompanyCode() != null && v.getUseCompanyCode().length() > 100) {
            arg1.reject("useCompanyCode", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getUseCompanyName() != null && v.getUseCompanyName().length() > 100) {
            arg1.reject("useCompanyName", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getEquipmentType() != null && v.getEquipmentType().length() > 100) {
            arg1.reject("equipmentType", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getEquipmentCode() != null && v.getEquipmentCode().length() > 100) {
            arg1.reject("equipmentCode", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getVideoCode() != null && v.getVideoCode().length() > 100) {
            arg1.reject("videoCode", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getWarnInfo() != null && v.getWarnInfo().length() > 16383) {
            arg1.reject("warnInfo", MessageUtils.getMessage(JC_SYS_011, new Object[]{"16383"}));
        }
        if (v.getDriver1() != null && v.getDriver1().length() > 100) {
            arg1.reject("driver1", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getDriver1Mobile() != null && v.getDriver1Mobile().length() > 100) {
            arg1.reject("driver1Mobile", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getDriver2() != null && v.getDriver2().length() > 100) {
            arg1.reject("driver2", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getDriver2Mobile() != null && v.getDriver2Mobile().length() > 100) {
            arg1.reject("driver2Mobile", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getSignalman() != null && v.getSignalman().length() > 100) {
            arg1.reject("signalman", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getSignalmanMobile() != null && v.getSignalmanMobile().length() > 100) {
            arg1.reject("signalmanMobile", MessageUtils.getMessage(JC_SYS_011, new Object[]{"100"}));
        }
        if (v.getRemark() != null && v.getRemark().length() > 500) {
            arg1.reject("remark", MessageUtils.getMessage(JC_SYS_011, new Object[]{"500"}));
        }
        if (v.getCreateUser() != null && v.getCreateUser().length() > 50) {
            arg1.reject("createUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
        }
        if (v.getCreateUserDept() != null && v.getCreateUserDept().length() > 50) {
            arg1.reject("createUserDept", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
        }
        if (v.getCreateUserOrg() != null && v.getCreateUserOrg().length() > 50) {
            arg1.reject("createUserOrg", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
        }
        if (v.getModifyUser() != null && v.getModifyUser().length() > 50) {
            arg1.reject("modifyUser", MessageUtils.getMessage(JC_SYS_011, new Object[]{"50"}));
        }
        if (v.getExtStr1() != null && v.getExtStr1().length() > 200) {
            arg1.reject("extStr1", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
        }
        if (v.getExtStr2() != null && v.getExtStr2().length() > 200) {
            arg1.reject("extStr2", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
        }
        if (v.getExtStr3() != null && v.getExtStr3().length() > 200) {
            arg1.reject("extStr3", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
        }
        if (v.getExtStr4() != null && v.getExtStr4().length() > 200) {
            arg1.reject("extStr4", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
        }
        if (v.getExtStr5() != null && v.getExtStr5().length() > 200) {
            arg1.reject("extStr5", MessageUtils.getMessage(JC_SYS_011, new Object[]{"200"}));
        }
    }
}
