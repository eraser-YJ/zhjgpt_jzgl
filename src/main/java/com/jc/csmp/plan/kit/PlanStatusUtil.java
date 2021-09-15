package com.jc.csmp.plan.kit;

public class PlanStatusUtil {

    public static final Integer audit = Integer.valueOf(2);

    public static final Integer pass = Integer.valueOf(20);

    public static String getStatusValue(Integer value){
        //
        if (audit.equals(value)) {
            return "审批中";
        } else if (pass.equals(value)) {
            return "审批通过";
        }
        return "待提交";
    }

}
