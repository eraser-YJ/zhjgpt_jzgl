package com.jc.csmp.plan.kit;

import com.jc.csmp.plan.domain.ProjectMonthPlan;
import com.jc.csmp.plan.domain.ProjectMonthPlanItem;
import com.jc.csmp.plan.domain.ProjectYearPlan;
import com.jc.csmp.plan.domain.ProjectYearPlanItem;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {

    /**
     * 头部信息生成
     * @param nowPlan
     * @return
     */
    public static ProjectMonthPlan convert(ProjectYearPlan nowPlan) {
        //属性基本项目
        nowPlan.setCreateDate(null);
        nowPlan.setModifyDate(null);
        ProjectMonthPlan plan = new ProjectMonthPlan();
        plan.setPlanYear(nowPlan.getPlanYear());
        plan.setPlanType(nowPlan.getPlanType());
        plan.setPlanAreaCode(nowPlan.getPlanAreaCode());
        plan.setPlanAreaName(nowPlan.getPlanAreaName());
        return plan;
    }

    /**
     * 明细数据生成
     * @param itemList
     * @return
     */
    public static List<ProjectMonthPlanItem> convert(List<ProjectYearPlanItem> itemList) {
        //属性基本项目  这个方式太慢了，采用原始赋值方式
        //List<ProjectMonthPlanItem> itemMonthList = JsonUtil.json2Array(JsonUtil.java2Json(itemList), ProjectMonthPlanItem.class);
        List<ProjectMonthPlanItem> resList = new ArrayList<>();
        ProjectMonthPlanItem vo;
        for (ProjectYearPlanItem item : itemList) {
            vo = new ProjectMonthPlanItem();
            vo.setProjectType(item.getProjectType());
            vo.setProjectTypeName(item.getProjectTypeName());
            vo.setProjectId(item.getProjectId());
            vo.setProjectName(item.getProjectName());
            vo.setDutyCompany(item.getDutyCompany());
            vo.setDutyPerson(item.getDutyPerson());
            vo.setGovDutyPerson(item.getGovDutyPerson());
            vo.setProjectDesc(item.getProjectDesc());
            vo.setContractCompany(item.getContractCompany());
            vo.setContractPerson(item.getContractPerson());
            vo.setProjectStartDate(item.getProjectStartDate());
            vo.setProjectEndDate(item.getProjectEndDate());
            vo.setProjectTotalDay(item.getProjectTotalDay());
            vo.setProjectTotalInvest(item.getProjectTotalInvest());
            vo.setProjectUsedInvest(item.getProjectUsedInvest());
            vo.setProjectNowInvest(item.getProjectNowInvest());
            vo.setProjectAfterInvest(item.getProjectAfterInvest());
            vo.setRemark(item.getRemark());
            vo.setItemId(item.getId());
            vo.setExtStr5(item.getId());
            resList.add(vo);
        }
        return resList;
    }
}
