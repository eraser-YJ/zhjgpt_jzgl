package com.jc.csmp.common.enums;

import com.jc.foundation.util.StringUtil;

/**
 * @Author 常鹏
 * @Date 2020/8/10 18:12
 * @Version 1.0
 */
public enum ProjectQuestionEnum {
    /***/
    quality("质量问题", "PROQUALITY", "qualityQuestion", "cm_project_question_quality"),
    safe("安全问题", "PROSAFE", "safeQuestion", "cm_project_question_safe"),
    scene("现场问题", "PROSCENE", "sceneQuestion", "cm_project_question_scene");

    private String title;
    private String numberKey;
    private String definitionId;
    private String businessTable;
    ProjectQuestionEnum(String title, String numberKey, String definitionId, String businessTable) {
        this.title = title;
        this.numberKey = numberKey;
        this.definitionId = definitionId;
        this.businessTable = businessTable;
    }

    public static ProjectQuestionEnum getByCode(String code) {
        if (StringUtil.isEmpty(code)) {
            return null;
        }
        ProjectQuestionEnum result = null;
        ProjectQuestionEnum[] array = values();
        for (ProjectQuestionEnum e : array) {
            if (e.toString().equals(code)) {
                result = e;
                break;
            }
        }
        return result;
    }

    public static ProjectQuestionEnum getIndexOfDefinitionId(String definitionId) {
        if (StringUtil.isEmpty(definitionId)) {
            return null;
        }
        ProjectQuestionEnum result = null;
        ProjectQuestionEnum[] array = values();
        for (ProjectQuestionEnum e : array) {
            if (definitionId.indexOf(e.getDefinitionId()) > -1) {
                result = e;
                break;
            }
        }
        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumberKey() {
        return numberKey;
    }

    public void setNumberKey(String numberKey) {
        this.numberKey = numberKey;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public String getBusinessTable() {
        return businessTable;
    }

    public void setBusinessTable(String businessTable) {
        this.businessTable = businessTable;
    }}
