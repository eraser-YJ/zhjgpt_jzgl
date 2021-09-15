package com.jc.csmp.common.enums;

import java.util.ArrayList;
import java.util.List;

/*
* 资料目录，项目阶段枚举*/
public enum ProjectStageForder {



//    empty("", "",""),
    xmlx("项目立项", "xmlx1","0",false,"-1"),




    kczb("勘察招标", "kczb","0",false,"-1"),
    sjzb("设计招标", "sjzb","0",false,"-1"),
    kczhb("勘察中标", "kczhb","0",false,"-1"),
    sjfa("设计方案", "sjfa","0",false,"-1"),
    zbba("招标备案", "zbba","0",false,"-1"),
    zbgs("中标公示", "zbgs","0",false,"-1"),
    htxx("合同信息", "htxx","0",false,"-1"),
    jgys("竣工验收（单项验收）", "jgys","0",false,"-1"),
    gcjs("工程决算", "gcjs","0",false,"-1"),
    sjzhb("设计中标", "sjzhb","0",false,"-1"),
    sgbg("施工变更", "sgbg","0",false,"-1"),
    gcjis("工程结算", "gcjis","0",false,"-1"),
    zbjh("招标计划", "zbjh","0",false,"-1"),

    xtzy("系统资源", "xtzy","5",true,"-1"),
    xtzy_hygl("会议管理", "xtzy_hygl","5",false,"xtzy"),
    xtzy_jcd("检查点", "xtzy_jcd","5",false,"xtzy"),
    xtzy_jdgl("进度管理", "xtzy_jdgl","5",false,"xtzy"),
    xtzy_rwgl("任务管理", "xtzy_rwgl","5",false,"xtzy"),
    xtzy_tzgl("图纸管理", "xtzy_tzgl","5",false,"xtzy"),
    xtzy_xmgl("项目管理", "xtzy_xmgl","5",false,"xtzy"),
    xtzy_xxjd("形象进度", "xtzy_xxjd","5",false,"xtzy"),
    xtzy_zzaq("质量安全", "xtzy_zzaq","5",false,"xtzy"),

    change("变更单","change","5",false,"-1"),
    relation("联系单","relation","5",false,"-1"),
    visa("签证单","visa","5",false,"-1"),
    sceneQuestion("安全问题","sceneQuestion","5",false,"-1"),
    plan("项目计划","plan","5",false,"-1"),

    mxtz("模型图纸", "mxtz","5",false,"-1"),
    bawj("备案文件", "bawj","5",false,"-1");


    private String name;
    private String code;
    private String folderType;
    private boolean haveChild;
    private String pareantCode;

    ProjectStageForder(String name, String code, String folderType, boolean haveChild, String pareantCode) {
        this.name = name;
        this.code = code;
        this.folderType = folderType;
        this.haveChild = haveChild;
        this.pareantCode = pareantCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFolderType() {
        return folderType;
    }

    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    public boolean isHaveChild() {
        return haveChild;
    }

    public void setHaveChild(boolean haveChild) {
        this.haveChild = haveChild;
    }

    public String getPareantCode() {
        return pareantCode;
    }

    public void setPareantCode(String pareantCode) {
        this.pareantCode = pareantCode;
    }

    public List<ProjectStageForder> getAllForder(){
        List<ProjectStageForder> projectStageForderList = new ArrayList<>();
        ProjectStageForder[] values = values();
        for(ProjectStageForder forder:values){
            projectStageForderList.add(forder);
        }
       return projectStageForderList;
    }
    public static List<ProjectStageForder> getFolderByType(String folderType){
        List<ProjectStageForder> projectStageForderList = new ArrayList<>();
        ProjectStageForder[] values = values();
        for(ProjectStageForder forder:values){
            if (folderType.equals(forder.getFolderType())){
                projectStageForderList.add(forder);
            }

        }
        return projectStageForderList;
    }

    public static List<ProjectStageForder> getCildren(String code){
        List<ProjectStageForder> projectStageForderList = new ArrayList<>();
        ProjectStageForder[] values = values();
        for(ProjectStageForder forder:values){
            if (code.equals(forder.getPareantCode())){
                projectStageForderList.add(forder);
            }

        }
        return projectStageForderList;
    }

    public static ProjectStageForder getFolderByCode(String code){
        ProjectStageForder[] values = values();
        for(ProjectStageForder forder:values){
            code.equals(forder.getCode());
            return forder;
        }
        return null;
    }

    public static ProjectStageForder getFolderByName(String name){
        ProjectStageForder[] values = values();
        for(ProjectStageForder forder:values){
            name.equals(forder.getName());
            return forder;
        }
        return null;
    }



}
