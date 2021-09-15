package com.jc.csmp.plan.domain.validator;

import com.jc.csmp.plan.domain.ProjectYearPlan;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author
 */
public class ProjectPlanValidator implements Validator {

    private static final String JC_SYS_010 = "JC_SYS_010";

    private static final String JC_SYS_011 = "JC_SYS_011";

    /**
     * @param arg0 校验的类型
     * @return 是否支持校验
     * @description 检验类能够校验的类
     * @author
     * @version 2020-07-09
     */
    public boolean supports(Class<?> arg0) {
        return ProjectYearPlan.class.equals(arg0);
    }

    /**
     * @param arg0 当前的实体类
     * @param arg1 错误的信息
     * @description 检验具体实现方法
     * @author
     * @version 2020-07-09
     */
    public void validate(Object arg0, Errors arg1) {
        ProjectYearPlan v = (ProjectYearPlan) arg0;

    }
}
