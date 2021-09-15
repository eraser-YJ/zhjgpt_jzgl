package com.jc.resource.bean;

/**
 * @Author 常鹏
 * @Date 2020/7/27 10:36
 * @Version 1.0
 */
public class QueryDataParam {
    private String operationKey;
    private String value;
    private String operationAction;
    private String operationType;

    public String getOperationKey() {
        return operationKey;
    }

    public void setOperationKey(String operationKey) {
        this.operationKey = operationKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperationAction() {
        return operationAction;
    }

    public void setOperationAction(String operationAction) {
        this.operationAction = operationAction;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    //paramList.add(QueryDataParam.create(ResourceOperatorEnum.varchar.getType(), ResourceOperatorActionEnum.like.toString(), "company_name", "一号"));
    public static QueryDataParam create(String operationType, String operationAction, String operationKey, String value) {
        QueryDataParam param = new QueryDataParam();
        param.setOperationType(operationType);
        param.setOperationAction(operationAction);
        param.setOperationKey(operationKey);
        param.setValue(value);
        return param;
    }
}
