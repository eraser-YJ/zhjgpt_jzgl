package com.jc.system.api.domain;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public enum ApiInvokeStatus implements GenericEnum {
    /**
     * 状态应用
     */
    COMPLETE(0, "完成"), START(1, "开始"), ERROR(-1, "出错"), TIMEOUT(2, "超时");

    private int code;
    private String name;

    ApiInvokeStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ApiInvokeStatus valueOfEnum(int code) {
        ApiInvokeStatus[] aiss = values();
        for (ApiInvokeStatus ais : aiss) {
            if (ais.getCode() == code) {
                return ais;
            }
        }
        return null;
    }

}
