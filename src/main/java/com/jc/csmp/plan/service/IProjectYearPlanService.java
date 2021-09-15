package com.jc.csmp.plan.service;

import com.jc.csmp.plan.domain.ProjectYearPlan;
import com.jc.csmp.plan.domain.StatisItemVO;
import com.jc.csmp.plan.domain.StatisVO;
import com.jc.csmp.plan.domain.SuggestVO;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.dic.domain.Dic;

import java.util.List;

/**
 * @author
 * @version 2020-07-09
 * @title
 */

public interface IProjectYearPlanService extends IBaseService<ProjectYearPlan> {

    Integer saveWorkflow(ProjectYearPlan projectPlan) throws CustomException;

    Integer updateWorkflow(ProjectYearPlan projectPlan) throws CustomException;

    PageManager workFlowTodoQueryByPage(ProjectYearPlan projectPlan, PageManager page);

    PageManager workFlowDoneQueryByPage(ProjectYearPlan projectPlan, PageManager page);

    PageManager workFlowInstanceQueryByPage(ProjectYearPlan projectPlan, PageManager page);

    Integer deleteByIds(ProjectYearPlan projectPlan) throws CustomException;

    List<Dic> selectPPTDic();

    List<SuggestVO> querySuggest(ProjectYearPlan cond);

    StatisItemVO queryStatisList(StatisVO cond);

    Long queryCount(ProjectYearPlan cond);

    ProjectYearPlan queryByPiid(String piid);


    ProjectYearPlan queryById(String id);

    List<ProjectYearPlan> queryByYear(Integer year);
}