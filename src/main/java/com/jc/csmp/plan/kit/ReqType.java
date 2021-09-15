package com.jc.csmp.plan.kit;

public enum ReqType {
    CODE_1(1,"上报"),CODE_2(2,"调整");
    private Integer code;
    private String disName;
    ReqType(Integer code,String disName){
        this.code = code;
        this.disName = disName;
    }

    public Integer getCode() {
        return code;
    }



    public String getDisName() {
        return disName;
    }

    public static ReqType getByReqType(Integer code){
            for(ReqType tt:ReqType.values()){
                if(tt.getCode().equals(code)){
                    return tt;
                }
            }
            return null;

    }

    public static String getByReqTypeValue(Integer code){
        ReqType type = getByReqType(code);
        if(type == null){
            return "";
        }
        return type.getDisName();

    }

}
