package com.jc.csmp.common.enums;

import com.jc.foundation.util.StringUtil;

/**
 * 子系统标识
 * @author 常鹏
 * @date 2020-07-21
 */
public enum SubSystemSignEnum {
    /***/
    empty("", ""),
    project("项目管理", "784128602c5c4516abef6cbd8ce46dfa"),
    year("年度计划", "0e19a474fb8d49728ec0e7b577560917"),
    safe("安全监督", "f08c7faf831e4b9d8b4b534cf3baba2f"),
    full("全生命周期", "1e9d0ef9519e4346b190a1f512880c98"),
    zhgd("智慧工地", "bfe6ab0a980f4a43ad32a2523add970f"),
    theme("主题展示", "ad26aeef923441d699a9bf11f88171b8"),
    supervise("行业监管", "2f37c60d65184a8881c0b674e8d9f0a3"),
    resource("资源中心", "c8bbf6eac2224fb086589b14614713e1"),
    config("系统配置", "c8bbf6eac2224fb086589b14614713e1");
    private String value;
    private String menuId;
    SubSystemSignEnum(String value, String menuId) {
        this.value = value;
        this.menuId = menuId;
    }

    public static SubSystemSignEnum getByCode(String code) {
        SubSystemSignEnum result = SubSystemSignEnum.empty;
        if (!StringUtil.isEmpty(code)) {
            SubSystemSignEnum[] array = values();
            for (SubSystemSignEnum e : array) {
                if (e.toString().equals(code)) {
                    result = e;
                    break;
                }
            }
        }
        return result;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
