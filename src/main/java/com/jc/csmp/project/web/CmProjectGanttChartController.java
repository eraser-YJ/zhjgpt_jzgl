package com.jc.csmp.project.web;

import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import com.jc.csmp.project.plan.service.ICmProjectPlanService;
import com.jc.csmp.project.plan.service.ICmProjectPlanStageService;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/project/ganttChart")
public class CmProjectGanttChartController extends BaseController {

    @Autowired
    private ICmProjectInfoService cmProjectInfoService;
    @Autowired
    private ICmProjectPlanStageService cmProjectPlanStageService;
    @Autowired
    private ICmProjectPlanService projectPlanService;
    /**
     * @description 跳转方法
     * @return String 跳转的路径
     * @throws Exception
     * @author
     * @version  2020-08-05
     */
    @RequestMapping(value="manage.action",method= RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
    public String manage() throws Exception{
        return "csmp/project/info/ganttChart";
    }

    @RequestMapping(value="queryProjectPlan.action")
    @ResponseBody
    public List<CmProjectPlan> queryProjectPlan(String projectId) throws Exception{
        List<CmProjectPlan> list = new ArrayList<>();
        try {
            CmProjectPlan cmProjectPlan = new CmProjectPlan();
            cmProjectPlan.setProjectId(projectId);
            cmProjectPlan.setOrderBy("t.queue desc");
            list = projectPlanService.queryAll(cmProjectPlan);
        } catch (CustomException e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }
    @RequestMapping(value="queryProjectStage.action")
    @ResponseBody
    public List<CmProjectPlanStage> queryProjectStage(String projectId){
        List<CmProjectPlanStage> list = new ArrayList<>();
            CmProjectPlanStage cmProjectPlanStage = new CmProjectPlanStage();
            cmProjectPlanStage.setProjectId(projectId);
            cmProjectPlanStage.setOrderBy("t.queue desc");
            list =cmProjectPlanStageService.queryGenttCharts(cmProjectPlanStage);
        return list;
    }

}
