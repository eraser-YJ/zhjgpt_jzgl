package com.jc.resource.util;

/**
 * 资源表枚举
 * @Author 常鹏
 * @Date 2020/7/27 14:42
 * @Version 1.0
 */
public enum ResourceTableEnums {
    /***/
    pt_person_achievement("pt_person_achievement", "个人业绩", "id") {
        @Override
        public String getColumn(String key) {
            if (key.equals(ResourceDbServer.COMMON_PROJECT_UNIQUE)) {
                return "achievement_pnum";
            }
            return null;
        }
    };
    private String tableName;
    private String value;
    private String pkColumn;

    ResourceTableEnums(String tableName, String value, String pkColumn) {
        this.pkColumn = pkColumn;
        this.value = value;
        this.tableName = tableName;
    }

    public abstract String getColumn(String key);

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }}
